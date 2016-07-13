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
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
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

public class RaychevAnalysisVisitor extends TraversingVisitor<Map<Set<AbstractLocation>, AbstractHistory>, Object> {

	private PointsToQueryBuilder queryBuilder;

	private PointsToAnalysis pointsToAnalysis;

	static final int LOOP_ITERATIONS = 2;

	static final int HISTORY_THRESHOLD = 16;

	private Random randomizer;

	private EnclosingNodeHelper enclosingNodes;

	private TypeCollector typeCollector;
	
	private Map<Set<AbstractLocation>, Set<ConcreteHistory>> returnConcreteHistories;

	public RaychevAnalysisVisitor(PointsToContext pointsToContext) {
		typeCollector = new TypeCollector(pointsToContext);
		enclosingNodes = new EnclosingNodeHelper(new SSTNodeHierarchy(pointsToContext.getSST()));
		queryBuilder = new PointsToQueryBuilder(typeCollector, enclosingNodes);
		pointsToAnalysis = pointsToContext.getPointerAnalysis();

		randomizer = new Random();
		
		returnConcreteHistories = new HashMap<>();
	}
	
	@Override
	public Object visit(ISST sst, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		super.visit(sst, historyMap);
		
		// add concreteHistories which stopped on return statements to historyMap
		for (Entry<Set<AbstractLocation>, Set<ConcreteHistory>> entry : returnConcreteHistories.entrySet()) {
			if(historyMap.containsKey(entry.getKey())) {
				historyMap.get(entry.getKey()).getHistorySet().addAll(entry.getValue());
			}
		}
		
		return null;
	}

	@Override
	public Object visit(IAssignment assignment, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		IAssignableExpression expression = assignment.getExpression();
		if (expression instanceof IInvocationExpression) {
			IInvocationExpression invocation = (IInvocationExpression) expression;
			if (invocation.getMethodName().isConstructor()) {
				handleObjectAllocation(assignment, historyMap);
			}
			addInteractionForReturn(assignment, historyMap, invocation.getMethodName());
		}
		
		// TODO: does points to analysis creates AbstractLocations for Properties?
		
		// Handle Property Get
		IPropertyReference propertyReference = expressionContainsPropertyReference(assignment.getExpression());
		if(propertyReference != null) {
			Set<AbstractLocation> propertyAbstractLocations = findAbstractLocationsForReference(propertyReference,assignment);
			AbstractHistory propertyAbstractHistory = getOrCreateAbstractHistory(propertyAbstractLocations, historyMap);
			Interaction propertyInteraction = new Interaction(createPropertyMethodName(propertyReference), 0, InteractionType.PropertyGet);
			addInteractionToAbstractHistory(propertyAbstractHistory, propertyInteraction);
		}
		
		// Handle Property Set 
		IAssignableReference reference = assignment.getReference();
		if(reference instanceof IPropertyReference) {
			Set<AbstractLocation> abstractLocations = findAbstractLocationsForAssignment(assignment);
			AbstractHistory abstractHistory = getOrCreateAbstractHistory(abstractLocations, historyMap);
			
			Interaction interaction = new Interaction(createPropertyMethodName((IPropertyReference) reference), 0, InteractionType.PropertySet);
			
			addInteractionToAbstractHistory(abstractHistory, interaction);
		}
		
		return super.visit(assignment, historyMap);
	}

	@Override
	public Object visit(IIfElseBlock block, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		block.getCondition().accept(this, historyMap);

		if(block.getElse().isEmpty()) {
			Map<Set<AbstractLocation>, AbstractHistory> cloneThenBranch = cloneHistoryMap(historyMap);
			visitStatements(block.getThen(), cloneThenBranch);
			mergeHistoryMaps(historyMap, cloneThenBranch);
		}
		else {
			Map<Set<AbstractLocation>, AbstractHistory> cloneElseBranch = cloneHistoryMap(historyMap);
			visitStatements(block.getThen(), historyMap);
			visitStatements(block.getElse(), cloneElseBranch);
			mergeHistoryMaps(historyMap, cloneElseBranch);
		}
		
		checkForAbstractHistoryThreshold(historyMap);
		return null;
	}

	@Override
	public Object visit(ITryBlock block, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		visitStatements(block.getBody(), historyMap);
		
		List<Map<Set<AbstractLocation>, AbstractHistory>> tempListAbstractHistories = new ArrayList<>();

		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			Map<Set<AbstractLocation>, AbstractHistory> cloneCatchBlock = cloneHistoryMap(historyMap);
			visitStatements(catchBlock.getBody(), cloneCatchBlock);
			tempListAbstractHistories.add(cloneCatchBlock);
		}
		
		for (Map<Set<AbstractLocation>, AbstractHistory> map : tempListAbstractHistories) {
			mergeHistoryMaps(historyMap, map);
		}

		// finally block runs always
		visitStatements(block.getFinally(), historyMap);

		checkForAbstractHistoryThreshold(historyMap);
		return null;
	}

	@Override
	public Object visit(IDoLoop block, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		List<ISSTNode> loopedNodes = new ArrayList<>();
		loopedNodes.addAll(block.getBody());
		loopedNodes.add(block.getCondition());

		// do loop always runs at least one time 
		loopedNodes.forEach(node -> node.accept(this, historyMap));
		
		// adds one loop iteration for concrete histories
		Map<Set<AbstractLocation>, AbstractHistory> cloneLoopTwoIterations = cloneHistoryMap(historyMap);
		loopedNodes.forEach(node -> node.accept(this, cloneLoopTwoIterations));
				
		mergeHistoryMaps(historyMap, cloneLoopTwoIterations);

		checkForAbstractHistoryThreshold(historyMap);
		return null;
	}

	@Override
	public Object visit(IForEachLoop block, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		block.getDeclaration().accept(this, historyMap);

		block.getLoopedReference().accept(this, historyMap);

		List<ISSTNode> loopedNodes = Lists.newArrayList(block.getBody());
		loopNodes(loopedNodes, historyMap);
		
		checkForAbstractHistoryThreshold(historyMap);
		return null;
	}

	@Override
	public Object visit(IForLoop block, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		List<ISSTNode> loopedNodes = new ArrayList<>();
		loopedNodes.addAll(block.getInit());
		loopedNodes.add(block.getCondition());
		loopedNodes.addAll(block.getStep());
		loopedNodes.addAll(block.getBody());

		loopNodes(loopedNodes, historyMap);
		
		checkForAbstractHistoryThreshold(historyMap);
		return null;
	}

	@Override
	public Object visit(IWhileLoop block, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		List<ISSTNode> loopedNodes = new ArrayList<>();
		loopedNodes.add(block.getCondition());
		loopedNodes.addAll(block.getBody());

		loopNodes(loopedNodes, historyMap);

		checkForAbstractHistoryThreshold(historyMap);
		return null;
	}

	@Override
	public Object visit(IInvocationExpression invocation, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		addInteractionForReceiver(invocation, historyMap);

		int parameterPosition = 1;
		for (ISimpleExpression expression : invocation.getParameters()) {
			addInteractionForParameter(expression, invocation, parameterPosition, historyMap);
			parameterPosition++;
		}

		return super.visit(invocation, historyMap);
	}
	
	@Override
	public Object visit(IReturnStatement stmt, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		stmt.getExpression().accept(this, historyMap);
		
		Map<Set<AbstractLocation>, AbstractHistory> clone = cloneHistoryMap(historyMap);
		for (Entry<Set<AbstractLocation>, AbstractHistory> entry : clone.entrySet()) {
			returnConcreteHistories.put(entry.getKey(), entry.getValue().getHistorySet());
		}
		
		return null;
	}

	private void addInteractionToAbstractHistory(AbstractHistory abstractHistory, Interaction interaction) {
		abstractHistory.getAbstractHistory().add(interaction);

		for (ConcreteHistory concreteHistory : abstractHistory.getHistorySet()) {
			concreteHistory.add(interaction);
		}
	}

	private void addInteractionForParameter(ISimpleExpression expression, IInvocationExpression invocation,
			int parameterPosition, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		Set<AbstractLocation> abstractLocations = tryFindAbstractLocationsForSimpleExpression(expression);
		if (abstractLocations != null) {
			Interaction interaction = new Interaction(invocation.getMethodName(), parameterPosition,
					InteractionType.MethodCall);
			addInteractionToAbstractHistory(getOrCreateAbstractHistory(abstractLocations, historyMap), interaction);
		}
	}

	private void addInteractionForReceiver(IInvocationExpression invocation,
			Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		Set<AbstractLocation> abstractLocations = findAbstractLocationForInvocation(invocation);
		if (!abstractLocations.isEmpty()) {
			Interaction interaction = new Interaction(invocation.getMethodName(), 0, InteractionType.MethodCall);
			addInteractionToAbstractHistory(getOrCreateAbstractHistory(abstractLocations, historyMap), interaction);
		}
	}

	private void addInteractionForReturn(IAssignment assignment, Map<Set<AbstractLocation>, AbstractHistory> historyMap,
			IMethodName methodName) {
		Set<AbstractLocation> abstractLocations = findAbstractLocationsForAssignment(assignment);
		AbstractHistory abstractHistory = getOrCreateAbstractHistory(abstractLocations, historyMap);
		
		Interaction interaction = new Interaction(methodName, Interaction.RETURN, InteractionType.MethodCall);
		
		addInteractionToAbstractHistory(abstractHistory, interaction);
	}

	private IMethodName createPropertyMethodName(IPropertyReference reference) {
		IPropertyName propertyName = reference.getPropertyName();
		return MethodName.newMethodName(String.format("[%1$s] [%2$s].%3$s()", propertyName.getValueType(), propertyName.getDeclaringType(), propertyName.getName()));
	}

	private IPropertyReference expressionContainsPropertyReference(IAssignableExpression expression) {
		if(expression instanceof IReferenceExpression) {
			IReferenceExpression refExpr = (IReferenceExpression) expression;
			if(refExpr.getReference() instanceof IPropertyReference) {
				return (IPropertyReference) refExpr.getReference();
			}
		}
		return null;
	}

	private void handleObjectAllocation(IAssignment assignment, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		Set<AbstractLocation> abstractLocations = findAbstractLocationsForAssignment(assignment);
		AbstractHistory abstractHistory = new AbstractHistory();
		historyMap.put(abstractLocations, abstractHistory);
		abstractHistory.getHistorySet().add(new ConcreteHistory());
	}

	private void loopNodes(List<ISSTNode> nodes, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {

		Map<Set<AbstractLocation>, AbstractHistory> cloneLoopOneIteration = cloneHistoryMap(historyMap);
		nodes.forEach(node -> node.accept(this, cloneLoopOneIteration));
		
		// TODO: if necessary this could be optimized to add everything added in previous iteration to all histories two times
		Map<Set<AbstractLocation>, AbstractHistory> cloneLoopTwoIterations = cloneHistoryMap(historyMap);
		nodes.forEach(node -> node.accept(this, cloneLoopTwoIterations));
		nodes.forEach(node -> node.accept(this, cloneLoopTwoIterations));
				
		mergeHistoryMaps(historyMap, cloneLoopOneIteration);
		mergeHistoryMaps(historyMap, cloneLoopTwoIterations);
	}

	private AbstractHistory getOrCreateAbstractHistory(Set<AbstractLocation> abstractLocations,
			Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		if (historyMap.containsKey(abstractLocations))
			return historyMap.get(abstractLocations);

		AbstractHistory abstractHistory = new AbstractHistory();
		historyMap.put(abstractLocations, abstractHistory);
		abstractHistory.getHistorySet().add(new ConcreteHistory());
		return abstractHistory;
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

	private void checkForAbstractHistoryThreshold(Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		if (historyMap.size() > HISTORY_THRESHOLD) {
			evictRandomAbstractHistory(historyMap);
		}
	}

	void evictRandomAbstractHistory(Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		int size = historyMap.size();
		int indexRandomEntry = randomizer.nextInt(size);

		Entry<Set<AbstractLocation>, AbstractHistory> entryToRemove = null;

		int i = 0;
		for (Entry<Set<AbstractLocation>, AbstractHistory> entry : historyMap.entrySet()) {
			if (i == indexRandomEntry) {
				entryToRemove = entry;
				break;
			}
			i++;
		}

		if (entryToRemove != null)
			historyMap.remove(entryToRemove.getKey(), entryToRemove.getValue());
	}

	private void mergeHistoryMaps(Map<Set<AbstractLocation>, AbstractHistory> historyMap,
			Map<Set<AbstractLocation>, AbstractHistory> clone) {
		for (Entry<Set<AbstractLocation>, AbstractHistory> entry : clone.entrySet()) {
			Set<AbstractLocation> key = entry.getKey();
			if (historyMap.containsKey(key)) {
				historyMap.get(key).mergeAbstractHistory(entry.getValue());
			} else {
				historyMap.put(entry.getKey(), entry.getValue());
			}
		}

	}

	private Map<Set<AbstractLocation>, AbstractHistory> cloneHistoryMap(
			Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		Map<Set<AbstractLocation>, AbstractHistory> clone = new HashMap<>();
		for (Entry<Set<AbstractLocation>, AbstractHistory> entry : historyMap.entrySet()) {
			clone.put(entry.getKey(), entry.getValue().clone());
		}

		return clone;
	}

}
