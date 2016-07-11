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

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.ssts.IStatement;
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
import cc.kave.commons.model.ssts.statements.IAssignment;
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
	
	private PointsToContext pointsToContext;
	
	private PointsToQueryBuilder queryBuilder;
	
	private PointsToAnalysis pointsToAnalysis;

	static final int LOOP_ITERATIONS = 2;
	
	static final int HISTORY_THRESHOLD = 16;
	
	private Random randomizer;
	
	private EnclosingNodeHelper enclosingNodes;

	private TypeCollector typeCollector;

	public RaychevAnalysisVisitor(PointsToContext pointsToContext) {
		this.pointsToContext = pointsToContext;
		
		typeCollector = new TypeCollector(pointsToContext);
		enclosingNodes = new EnclosingNodeHelper(new SSTNodeHierarchy(pointsToContext.getSST()));
		queryBuilder = new PointsToQueryBuilder(typeCollector, enclosingNodes);
		pointsToAnalysis = pointsToContext.getPointerAnalysis();
				
		randomizer = new Random();
	}
	
	@Override
	public Object visit(IAssignment assignment, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		IAssignableExpression expression = assignment.getExpression();
		if(expression instanceof IInvocationExpression) {
			IInvocationExpression invocation = (IInvocationExpression) expression;
			if(invocation.getMethodName().isConstructor()) {
				handleObjectAllocation(assignment, historyMap);
			}
			addInteractionForReturn(assignment, historyMap, invocation.getMethodName());
		}
		return super.visit(assignment, historyMap);
	}
		
	@Override
	public Object visit(IIfElseBlock block, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		block.getCondition().accept(this, historyMap);
		
		Map<Set<AbstractLocation>,AbstractHistory> cloneThenBranch = cloneHistoryMap(historyMap);
		Map<Set<AbstractLocation>,AbstractHistory> cloneElseBranch = cloneHistoryMap(historyMap);
		visitStatements(block.getThen(), cloneThenBranch);
		visitStatements(block.getElse(), cloneElseBranch);
		
		mergeHistoryMaps(historyMap, cloneThenBranch);
		mergeHistoryMaps(historyMap, cloneElseBranch);
		
		checkForAbstractHistoryThreshold(historyMap);
		return null;
	}
	
	@Override
	public Object visit(ITryBlock block, Map<Set<AbstractLocation>, AbstractHistory> context) {
		// TODO Auto-generated method stub
		return super.visit(block, context);
	}
	
	@Override
	public Object visit(IDoLoop block, Map<Set<AbstractLocation>, AbstractHistory> context) {
		// TODO Auto-generated method stub
		return super.visit(block, context);
	}
	
	@Override
	public Object visit(IForEachLoop block, Map<Set<AbstractLocation>, AbstractHistory> context) {
		// TODO Auto-generated method stub
		return super.visit(block, context);
	}
	
	@Override
	public Object visit(IForLoop block, Map<Set<AbstractLocation>, AbstractHistory> context) {
		// TODO Auto-generated method stub
		return super.visit(block, context);
	}
	
	@Override
	public Object visit(IWhileLoop block, Map<Set<AbstractLocation>, AbstractHistory> context) {
		// TODO Auto-generated method stub
		return super.visit(block, context);
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
	
	public void addInteractionToAbstractHistory(AbstractHistory abstractHistory, Interaction interaction) {
		abstractHistory.getAbstractHistory().add(interaction);
		
		for (ConcreteHistory concreteHistory : abstractHistory.getHistorySet()) {
			concreteHistory.add(interaction);
		}
	}

	public void addInteractionForParameter(ISimpleExpression expression, IInvocationExpression invocation,
			int parameterPosition, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		Set<AbstractLocation> abstractLocations = tryFindAbstractLocationsForSimpleExpression(expression);
		if (abstractLocations != null) {
			Interaction interaction = new Interaction(invocation.getMethodName(), parameterPosition,
					InteractionType.MethodCall);
			addInteractionToAbstractHistory(getOrCreateAbstractHistory(abstractLocations, historyMap), interaction);
		}
	}

	public void addInteractionForReceiver(IInvocationExpression invocation,
			Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		Set<AbstractLocation> abstractLocations = findAbstractLocationForInvocation(invocation);
		if (!abstractLocations.isEmpty()) {
			Interaction interaction = new Interaction(invocation.getMethodName(), 0, InteractionType.MethodCall);
			addInteractionToAbstractHistory(getOrCreateAbstractHistory(abstractLocations, historyMap), interaction);
		}
	}

	public void addInteractionForReturn(IAssignment assignment, Map<Set<AbstractLocation>, AbstractHistory> historyMap, IMethodName methodName) {
		Set<AbstractLocation> abstractLocations = findAbstractLocationsForAssignment(assignment);
		AbstractHistory abstractHistory = getOrCreateAbstractHistory(abstractLocations, historyMap);
		
		Interaction interaction = new Interaction(methodName, Interaction.RETURN, InteractionType.MethodCall);
		addInteractionToAbstractHistory(abstractHistory, interaction);
	}

	public void handleObjectAllocation(IAssignment assignment, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		Set<AbstractLocation> abstractLocations = findAbstractLocationsForAssignment(assignment);
		AbstractHistory abstractHistory = new AbstractHistory();
		historyMap.put(abstractLocations, abstractHistory);
		abstractHistory.getHistorySet().add(new ConcreteHistory());
	}

	public AbstractHistory getOrCreateAbstractHistory(
			Set<AbstractLocation> abstractLocations, Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		if(historyMap.containsKey(abstractLocations))
			return historyMap.get(abstractLocations);
		
		AbstractHistory abstractHistory = new AbstractHistory();
		historyMap.put(abstractLocations, abstractHistory);
		abstractHistory.getHistorySet().add(new ConcreteHistory());
		return abstractHistory;
	}

	public Set<AbstractLocation> findAbstractLocationsForAssignment(IAssignment assignment) {
		PointsToQuery query = queryBuilder.newQuery(assignment.getReference(), assignment);
		Set<AbstractLocation> abstractLocations = pointsToAnalysis.query(query);
		return abstractLocations;
	}

	public Set<AbstractLocation> findAbstractLocationForInvocation(IInvocationExpression invocation) {
		IStatement enclosingStatement = enclosingNodes.getEnclosingStatement(invocation);
		PointsToQuery query = queryBuilder.newQuery(invocation.getReference(), enclosingStatement);
		
		Set<AbstractLocation> abstractLocations = pointsToAnalysis.query(query);
				
		return abstractLocations;
	}

	public Set<AbstractLocation> tryFindAbstractLocationsForSimpleExpression(ISimpleExpression expression) {
		IStatement enclosingStatement = enclosingNodes.getEnclosingStatement(expression);
		if(expression instanceof IReferenceExpression) {
			IReferenceExpression refExpr = (IReferenceExpression) expression;
			PointsToQuery query = queryBuilder.newQuery(refExpr.getReference(), enclosingStatement);
			Set<AbstractLocation> abstractLocations = pointsToAnalysis.query(query);
			return abstractLocations;
		}
		return null;
	}

	public void checkForAbstractHistoryThreshold(Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		if(historyMap.size() > HISTORY_THRESHOLD) {
			evictRandomAbstractHistory(historyMap);
		}
	}
	
	public void evictRandomAbstractHistory(Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		int size = historyMap.size();
		int indexRandomEntry = randomizer.nextInt(size);
		
		Entry<Set<AbstractLocation>,AbstractHistory> entryToRemove = null;
		
		int i = 0;
		for (Entry<Set<AbstractLocation>, AbstractHistory> entry : historyMap.entrySet()) {
			if(i == indexRandomEntry) {
				entryToRemove = entry;
				break;
			}
			i++;
		}
		
		if(entryToRemove != null) historyMap.remove(entryToRemove.getKey(), entryToRemove.getValue());
	}
	
	public void mergeHistoryMaps(Map<Set<AbstractLocation>, AbstractHistory> historyMap,
			Map<Set<AbstractLocation>, AbstractHistory> clone) {
		for (Entry<Set<AbstractLocation>, AbstractHistory> entry : clone.entrySet()) {
			Set<AbstractLocation> key = entry.getKey();
			if(historyMap.containsKey(key)) {
				historyMap.get(key).mergeAbstractHistory(entry.getValue());
			}
			else {
				historyMap.put(entry.getKey(), entry.getValue());
			}
		}
		
	}

	public Map<Set<AbstractLocation>,AbstractHistory> cloneHistoryMap(Map<Set<AbstractLocation>, AbstractHistory> historyMap) {
		Map<Set<AbstractLocation>,AbstractHistory> clone = new HashMap<>();
		for (Entry<Set<AbstractLocation>, AbstractHistory> entry : historyMap.entrySet()) {
			clone.put(entry.getKey(), entry.getValue().clone());
		}
		
		return clone;
	}
	
}
