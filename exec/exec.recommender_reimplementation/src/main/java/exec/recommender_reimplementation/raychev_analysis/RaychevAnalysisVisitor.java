/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.recommender_reimplementation.raychev_analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.PointsToQuery;
import cc.kave.commons.pointsto.analysis.PointsToQueryBuilder;
import cc.kave.commons.pointsto.analysis.types.TypeCollector;
import cc.kave.commons.pointsto.analysis.utils.EnclosingNodeHelper;
import cc.kave.commons.pointsto.analysis.visitors.TraversingVisitor;
import cc.kave.commons.utils.SSTNodeHierarchy;

public class RaychevAnalysisVisitor extends TraversingVisitor<HistoryMap, Object> {

	private PointsToQueryBuilder queryBuilder;

	private PointsToAnalysis pointsToAnalysis;

	private EnclosingNodeHelper enclosingNodes;

	private TypeCollector typeCollector;

	private Map<Set<AbstractLocation>, Set<ConcreteHistory>> returnConcreteHistories;

	public RaychevAnalysisVisitor(PointsToContext pointsToContext) {
		typeCollector = new TypeCollector(pointsToContext);
		enclosingNodes = new EnclosingNodeHelper(new SSTNodeHierarchy(pointsToContext.getSST()));
		queryBuilder = new PointsToQueryBuilder(typeCollector, enclosingNodes);
		pointsToAnalysis = pointsToContext.getPointerAnalysis();

		returnConcreteHistories = new HashMap<>();
	}

	@Override
	public Object visit(IMethodDeclaration stmt, HistoryMap historyMap) {
		super.visit(stmt, historyMap);

		// add concreteHistories which stopped on return statements to
		// historyMap
		for (Entry<Set<AbstractLocation>, Set<ConcreteHistory>> entry : returnConcreteHistories.entrySet()) {
			if (historyMap.containsKey(entry.getKey())) {
				historyMap.get(entry.getKey()).getHistorySet().addAll(entry.getValue());
			}
		}
		returnConcreteHistories.clear();

		return null;
	}

	@Override
	public Object visit(IAssignment assignment, HistoryMap historyMap) {
		IAssignableExpression expression = assignment.getExpression();
		if (expression instanceof IInvocationExpression) {
			IInvocationExpression invocation = (IInvocationExpression) expression;
			if (invocation.getMethodName().isConstructor()) {
				handleObjectAllocation(assignment, historyMap);
			}
			addInteractionForReturn(assignment, historyMap, invocation.getMethodName());
		}

		// Handle Property Get
		IPropertyReference propertyReference = expressionContainsPropertyReference(assignment.getExpression());
		if (propertyReference != null) {
			Set<AbstractLocation> propertyAbstractLocations = findAbstractLocationsForReference(propertyReference,
					assignment);
			AbstractHistory propertyGetAbstractHistory = historyMap
					.getOrCreateAbstractHistory(propertyAbstractLocations);
			Interaction propertyInteraction = new Interaction(createPropertyMethodName(propertyReference), 0,
					InteractionType.PROPERTY_GET);
			propertyGetAbstractHistory.addInteraction(propertyInteraction);
		}

		// Handle Property Set
		IAssignableReference reference = assignment.getReference();
		if (reference instanceof IPropertyReference) {
			Set<AbstractLocation> abstractLocations = findAbstractLocationsForAssignment(assignment);
			AbstractHistory propertySetAbstractHistory = historyMap.getOrCreateAbstractHistory(abstractLocations);

			Interaction interaction = new Interaction(createPropertyMethodName((IPropertyReference) reference), 0,
					InteractionType.PROPERTY_SET);

			propertySetAbstractHistory.addInteraction(interaction);
		}

		return super.visit(assignment, historyMap);
	}

	@Override
	public Object visit(IIfElseBlock block, HistoryMap historyMap) {
		block.getCondition().accept(this, historyMap);

		HistoryMap cloneElseBranch = historyMap.clone();
		visitStatements(block.getThen(), historyMap);
		visitStatements(block.getElse(), cloneElseBranch);
		historyMap.mergeInto(cloneElseBranch);

		historyMap.checkForAbstractHistoryThreshold();
		return null;
	}

	@Override
	public Object visit(ITryBlock block, HistoryMap historyMap) {
		visitStatements(block.getBody(), historyMap);

		List<HistoryMap> tempListAbstractHistories = new ArrayList<>();

		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			HistoryMap cloneCatchBlock = historyMap.clone();
			visitStatements(catchBlock.getBody(), cloneCatchBlock);
			tempListAbstractHistories.add(cloneCatchBlock);
		}

		for (HistoryMap map : tempListAbstractHistories) {
			historyMap.mergeInto(map);
		}

		// finally block runs always
		visitStatements(block.getFinally(), historyMap);

		historyMap.checkForAbstractHistoryThreshold();
		return null;
	}

	@Override
	public Object visit(IDoLoop block, HistoryMap historyMap) {
		List<ISSTNode> loopedNodes = new ArrayList<>();
		loopedNodes.addAll(block.getBody());
		loopedNodes.add(block.getCondition());

		// do loop always runs at least one time
		loopedNodes.forEach(node -> node.accept(this, historyMap));

		// adds one loop iteration for concrete histories
		HistoryMap cloneLoopTwoIterations = historyMap.clone();
		loopedNodes.forEach(node -> node.accept(this, cloneLoopTwoIterations));

		historyMap.mergeInto(cloneLoopTwoIterations);

		historyMap.checkForAbstractHistoryThreshold();
		return null;
	}

	@Override
	public Object visit(IForEachLoop block, HistoryMap historyMap) {
		block.getDeclaration().accept(this, historyMap);

		block.getLoopedReference().accept(this, historyMap);

		List<ISSTNode> loopedNodes = Lists.newArrayList(block.getBody());
		loopNodesTwoIterations(loopedNodes, historyMap);

		historyMap.checkForAbstractHistoryThreshold();
		return null;
	}

	@Override
	public Object visit(IForLoop block, HistoryMap historyMap) {
		visitStatements(block.getInit(), historyMap);
		block.getCondition().accept(this, historyMap);

		List<ISSTNode> loopedNodes = new ArrayList<>();

		loopedNodes.addAll(block.getBody());
		loopedNodes.addAll(block.getStep());
		loopedNodes.add(block.getCondition());
		loopNodesTwoIterations(loopedNodes, historyMap);

		historyMap.checkForAbstractHistoryThreshold();

		return null;
	}

	@Override
	public Object visit(IWhileLoop block, HistoryMap historyMap) {
		List<ISSTNode> loopedNodes = new ArrayList<>();
		loopedNodes.add(block.getCondition());
		loopedNodes.addAll(block.getBody());

		loopNodesTwoIterations(loopedNodes, historyMap);

		historyMap.checkForAbstractHistoryThreshold();
		return null;
	}

	@Override
	public Object visit(IInvocationExpression invocation, HistoryMap historyMap) {
		addInteractionForReceiver(invocation, historyMap);

		int parameterPosition = 1;
		for (ISimpleExpression expression : invocation.getParameters()) {
			addInteractionForParameter(expression, invocation, parameterPosition, historyMap);
			parameterPosition++;
		}

		return super.visit(invocation, historyMap);
	}

	@Override
	public Object visit(IReturnStatement stmt, HistoryMap historyMap) {
		stmt.getExpression().accept(this, historyMap);

		for (Entry<Set<AbstractLocation>, AbstractHistory> entry : historyMap.entrySet()) {
			returnConcreteHistories.put(entry.getKey(), Sets.newHashSet(entry.getValue().getHistorySet()));
			entry.getValue().getHistorySet().clear();
		}
		return null;
	}

	private void addInteractionForParameter(ISimpleExpression expression, IInvocationExpression invocation,
			int parameterPosition, HistoryMap historyMap) {
		Set<AbstractLocation> abstractLocations = tryFindAbstractLocationsForSimpleExpression(expression);
		if (abstractLocations != null) {
			Interaction interaction = new Interaction(invocation.getMethodName(), parameterPosition,
					InteractionType.METHOD_CALL);
			historyMap.getOrCreateAbstractHistory(abstractLocations).addInteraction(interaction);
		}
	}

	private void addInteractionForReceiver(IInvocationExpression invocation, HistoryMap historyMap) {
		Set<AbstractLocation> abstractLocations = findAbstractLocationForInvocation(invocation);
		if (!abstractLocations.isEmpty()) {
			Interaction interaction = new Interaction(invocation.getMethodName(), 0, InteractionType.METHOD_CALL);
			historyMap.getOrCreateAbstractHistory(abstractLocations).addInteraction(interaction);
		}
	}

	private void addInteractionForReturn(IAssignment assignment, HistoryMap historyMap, IMethodName methodName) {
		Set<AbstractLocation> abstractLocations = findAbstractLocationsForAssignment(assignment);
		AbstractHistory abstractHistory = historyMap.getOrCreateAbstractHistory(abstractLocations);

		Interaction interaction = new Interaction(methodName, Interaction.RETURN, InteractionType.METHOD_CALL);
		abstractHistory.addInteraction(interaction);
	}

	private IMethodName createPropertyMethodName(IPropertyReference reference) {
		IPropertyName propertyName = reference.getPropertyName();
		return MethodName.newMethodName(String.format("[%1$s] [%2$s].%3$s()", propertyName.getValueType(),
				propertyName.getDeclaringType(), propertyName.getName()));
	}

	private IPropertyReference expressionContainsPropertyReference(IAssignableExpression expression) {
		if (expression instanceof IReferenceExpression) {
			IReferenceExpression refExpr = (IReferenceExpression) expression;
			if (refExpr.getReference() instanceof IPropertyReference) {
				return (IPropertyReference) refExpr.getReference();
			}
		}
		return null;
	}

	private void handleObjectAllocation(IAssignment assignment, HistoryMap historyMap) {
		Set<AbstractLocation> abstractLocations = findAbstractLocationsForAssignment(assignment);
		AbstractHistory abstractHistory = new AbstractHistory();
		historyMap.put(abstractLocations, abstractHistory);
		abstractHistory.getHistorySet().add(new ConcreteHistory());
	}

	private void loopNodesTwoIterations(List<ISSTNode> nodes, HistoryMap historyMap) {

		HistoryMap cloneLoopOneIteration = historyMap.clone();
		nodes.forEach(node -> node.accept(this, cloneLoopOneIteration));

		HistoryMap cloneLoopTwoIterations = cloneLoopOneIteration.clone();
		nodes.forEach(node -> node.accept(this, cloneLoopTwoIterations));

		historyMap.mergeInto(cloneLoopOneIteration);
		historyMap.mergeInto(cloneLoopTwoIterations);
	}

	private Set<AbstractLocation> findAbstractLocationsForAssignment(IAssignment assignment) {
		return findAbstractLocationsForReference(assignment.getReference(), assignment);
	}

	private Set<AbstractLocation> findAbstractLocationsForReference(IReference reference, IStatement statement) {
		PointsToQuery query = queryBuilder.newQuery(reference, statement);
		Set<AbstractLocation> abstractLocations = pointsToAnalysis.query(query);
		return abstractLocations;
	}

	private Set<AbstractLocation> findAbstractLocationForInvocation(IInvocationExpression invocation) {
		IStatement enclosingStatement = enclosingNodes.getEnclosingStatement(invocation);
		return findAbstractLocationsForReference(invocation.getReference(), enclosingStatement);
	}

	private Set<AbstractLocation> tryFindAbstractLocationsForSimpleExpression(ISimpleExpression expression) {
		IStatement enclosingStatement = enclosingNodes.getEnclosingStatement(expression);
		if (expression instanceof IReferenceExpression) {
			IReferenceExpression refExpr = (IReferenceExpression) expression;
			PointsToQuery query = queryBuilder.newQuery(refExpr.getReference(), enclosingStatement);
			Set<AbstractLocation> abstractLocations = pointsToAnalysis.query(query);
			return abstractLocations;
		}
		return null;
	}

}
