/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.analysis.inclusion.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

import cc.kave.commons.model.names.IMemberName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.pointsto.analysis.inclusion.ConstraintResolver;
import cc.kave.commons.pointsto.analysis.inclusion.ConstructedTerm;
import cc.kave.commons.pointsto.analysis.inclusion.DeclarationLambdaStore;
import cc.kave.commons.pointsto.analysis.inclusion.LambdaTerm;
import cc.kave.commons.pointsto.analysis.inclusion.RefTerm;
import cc.kave.commons.pointsto.analysis.inclusion.SetExpression;
import cc.kave.commons.pointsto.analysis.inclusion.SetVariable;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.ContextAnnotation;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.InclusionAnnotation;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.InvocationAnnotation;
import cc.kave.commons.pointsto.analysis.inclusion.contexts.Context;
import cc.kave.commons.pointsto.analysis.inclusion.contexts.ContextFactory;
import cc.kave.commons.pointsto.analysis.inclusion.contexts.EmptyContextFactory;
import cc.kave.commons.pointsto.analysis.references.DistinctReference;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;

public class ConstraintGraph {

	private final LanguageOptions languageOptions = LanguageOptions.getInstance();

	private final BiMap<DistinctReference, SetVariable> referenceVariables;
	private final DeclarationLambdaStore declLambdaStore;
	private final Map<SetExpression, ConstraintNode> constraintNodes;

	private final ConstraintResolver constraintResolver = new ConstraintResolver(this::getNode);
	private final ContextFactory contextFactory;

	private Multimap<SetVariable, ConstraintEdge> leastSolution = HashMultimap.create();

	ConstraintGraph(Map<DistinctReference, SetVariable> referenceVariables, DeclarationLambdaStore declLambdaStore,
			Map<SetExpression, ConstraintNode> constraintNodes, ContextFactory contextFactory) {
		this.referenceVariables = HashBiMap.create(referenceVariables);
		this.declLambdaStore = new DeclarationLambdaStore(declLambdaStore, this::getVariable, constraintResolver);
		this.constraintNodes = constraintNodes;
		this.contextFactory = contextFactory;
	}

	private SetVariable getVariable(DistinctReference distRef) {
		Objects.requireNonNull(distRef);

		SetVariable variable = referenceVariables.get(distRef);
		if (variable == null) {
			variable = declLambdaStore.getVariableFactory().create();
			referenceVariables.put(distRef, variable);
		}

		return variable;
	}

	private ConstraintNode getNode(SetExpression setExpr) {
		Objects.requireNonNull(setExpr);

		ConstraintNode node = constraintNodes.get(setExpr);
		if (node == null) {
			node = new ConstraintNode(setExpr);
			constraintNodes.put(setExpr, node);
		}

		return node;
	}

	private LambdaTerm getDeclarationLambda(IMemberName member) {
		return declLambdaStore.getDeclarationLambda(member);
	}

	private Set<SetVariable> getSetVariables() {
		Set<SetVariable> variables = new HashSet<>();
		Set<ConstraintNode> visited = new HashSet<>();
		Deque<ConstraintNode> worklist = new ArrayDeque<>(constraintNodes.values());

		// use depth first search to retrieve all SetVariables
		while (!worklist.isEmpty()) {
			ConstraintNode node = worklist.removeFirst();
			if (visited.contains(node)) {
				continue;
			}
			visited.add(node);

			SetExpression setExpr = node.getSetExpression();
			if (setExpr instanceof SetVariable) {
				variables.add((SetVariable) setExpr);
			}

			for (ConstraintEdge edge : Iterables.concat(node.getPredecessors(), node.getSuccessors())) {
				if (!visited.contains(edge.getTarget())) {
					worklist.addFirst(edge.getTarget());
				}
			}
		}
		return variables;
	}

	public void computeClosure() {
		LinkedHashSet<ConstraintNode> worklist = new LinkedHashSet<>(constraintNodes.values());

		while (!worklist.isEmpty()) {
			ConstraintNode node = worklist.iterator().next();
			worklist.remove(node);

			worklist.addAll(processNode(node));
		}
	}

	private Set<ConstraintNode> processNode(ConstraintNode node) {
		Set<ConstraintNode> changedNodes = new HashSet<>();
		Set<ConstraintEdge> objectEdges = new HashSet<>();
		Set<ConstraintEdge> invocationEdges = new HashSet<>();

		Collection<ConstraintEdge> predecessors = new ArrayList<>(node.getPredecessors());
		Collection<ConstraintEdge> successors = new ArrayList<>(node.getSuccessors());

		for (ConstraintEdge preEdge : predecessors) {
			SetExpression preEdgeTarget = preEdge.getTarget().getSetExpression();
			if (preEdgeTarget instanceof RefTerm) {
				objectEdges.add(preEdge);
			}

			for (ConstraintEdge succEdge : successors) {
				SetExpression succEdgeTarget = succEdge.getTarget().getSetExpression();
				if (succEdgeTarget instanceof LambdaTerm
						&& succEdge.getInclusionAnnotation() instanceof InvocationAnnotation) {
					invocationEdges.add(succEdge);
				}

				if (match(preEdge, succEdge)) {
					boolean bothConstructedTerms = preEdgeTarget instanceof ConstructedTerm
							&& succEdgeTarget instanceof ConstructedTerm;
					if (bothConstructedTerms && preEdgeTarget.getClass() != succEdgeTarget.getClass()) {
						// prevent adding a constraint between RefTerm and LambdaTerm
						continue;
					}

					InclusionAnnotation newInclAnnotation = concat(preEdge.getInclusionAnnotation(),
							succEdge.getInclusionAnnotation());
					ContextAnnotation newContextAnnotation = concat(preEdge.getContextAnnotation(),
							succEdge.getContextAnnotation());
					changedNodes.addAll(constraintResolver.addConstraint(preEdgeTarget, succEdgeTarget,
							newInclAnnotation, newContextAnnotation));
				}
			}
		}

		processInvocations(objectEdges, invocationEdges, changedNodes);
		return changedNodes;
	}

	private void processInvocations(Set<ConstraintEdge> objectEdges, Set<ConstraintEdge> invocationEdges,
			Set<ConstraintNode> changedNodes) {
		for (ConstraintEdge objectEdge : objectEdges) {
			RefTerm object = (RefTerm) objectEdge.getTarget().getSetExpression();

			for (ConstraintEdge invocationEdge : invocationEdges) {
				if (match(objectEdge, invocationEdge)) {
					InvocationAnnotation invocationAnnotation = (InvocationAnnotation) invocationEdge
							.getInclusionAnnotation();
					// virtual call resolution
					IMemberName staticMember = invocationAnnotation.getMember();
					ITypeName dynamicType = object.getAllocationSite().getType();
					IMemberName targetMember;
					if (!invocationAnnotation.isDynamicallyDispatched() || dynamicType == null
							|| dynamicType.isUnknownType()) {
						targetMember = staticMember;
					} else {
						targetMember = languageOptions.resolveVirtual(staticMember, dynamicType);
					}
					LambdaTerm declarationLambda = getDeclarationLambda(targetMember);

					ContextAnnotation contextAnnotation = concat(objectEdge.getContextAnnotation(),
							invocationEdge.getContextAnnotation());
					Context newContext = contextFactory.create(object, invocationAnnotation, contextAnnotation);
					if (contextAnnotation.isEmpty() && contextFactory.getClass() != EmptyContextFactory.class) {
						contextAnnotation = new ContextAnnotation(Context.WILDCARD, Context.WILDCARD);
					}

					changedNodes.addAll(constraintResolver.addConstraint(object, declarationLambda.getArgument(0),
							InclusionAnnotation.EMPTY,
							createContextAnnotation(contextAnnotation.getLeft(), newContext)));
					changedNodes.addAll(constraintResolver.resolve(declarationLambda,
							(LambdaTerm) invocationEdge.getTarget().getSetExpression(), InclusionAnnotation.EMPTY,
							createContextAnnotation(newContext, contextAnnotation.getRight())));
				}
			}
		}
	}

	public Multimap<DistinctReference, ConstraintEdge> computeLeastSolution() {
		leastSolution.clear();
		for (SetVariable var : getSetVariables()) {
			computeLeastSolution(var, new HashSet<>());
		}

		Multimap<DistinctReference, ConstraintEdge> distRefLS = HashMultimap.create();
		for (SetVariable var : leastSolution.keySet()) {
			DistinctReference distRef = referenceVariables.inverse().get(var);
			// temporary variables do not have a distinct reference
			if (distRef != null) {
				distRefLS.putAll(distRef, leastSolution.get(var));
			}
		}
		return distRefLS;
	}

	private Collection<ConstraintEdge> computeLeastSolution(SetVariable v, Set<SetVariable> visited) {
		if (visited.contains(v) || leastSolution.containsKey(v)) {
			return leastSolution.get(v);
		}
		visited.add(v);

		ConstraintNode node = getNode(v);
		Set<ConstraintEdge> setVarEdges = new HashSet<>();
		for (ConstraintEdge preEdge : node.getPredecessors()) {
			SetExpression targetExpr = preEdge.getTarget().getSetExpression();
			if (targetExpr instanceof SetVariable) {
				setVarEdges.add(preEdge);
			} else if (targetExpr instanceof ConstructedTerm) {
				leastSolution.put(v, preEdge);
			}
		}

		for (ConstraintEdge setVarEdge : setVarEdges) {
			SetVariable u = (SetVariable) setVarEdge.getTarget().getSetExpression();
			Collection<ConstraintEdge> uLS = computeLeastSolution(u, visited);
			for (ConstraintEdge uEdge : uLS) {
				if (match(uEdge, setVarEdge)) {
					InclusionAnnotation newInclusionAnnotation = concat(uEdge.getInclusionAnnotation(),
							setVarEdge.getInclusionAnnotation());
					ContextAnnotation newContextAnnotation = concat(uEdge.getContextAnnotation(),
							setVarEdge.getContextAnnotation());
					leastSolution.put(v,
							new ConstraintEdge(uEdge.getTarget(), newInclusionAnnotation, newContextAnnotation));
				}
			}
		}

		return leastSolution.get(v);
	}

	private boolean match(ConstraintEdge edge1, ConstraintEdge edge2) {
		return match(edge1.getInclusionAnnotation(), edge2.getInclusionAnnotation())
				&& match(edge1.getContextAnnotation(), edge2.getContextAnnotation());
	}

	private boolean match(InclusionAnnotation a, InclusionAnnotation b) {
		if (InclusionAnnotation.EMPTY.equals(a) || InclusionAnnotation.EMPTY.equals(b)) {
			return true;
		}

		if (a.equals(b)) {
			return true;
		}

		return false;
	}

	private InclusionAnnotation concat(InclusionAnnotation a, InclusionAnnotation b) {
		if (InclusionAnnotation.EMPTY.equals(a)) {
			return b;
		} else if (InclusionAnnotation.EMPTY.equals(b)) {
			return a;
		}

		return InclusionAnnotation.EMPTY;
	}

	private boolean match(ContextAnnotation s1, ContextAnnotation s2) {
		if (s1.isEmpty() || s2.isEmpty()) {
			return true;
		}

		if (s1.getRight().equals(s2.getLeft())) {
			return true;
		}

		if (Context.WILDCARD.equals(s1.getRight()) || Context.WILDCARD.equals(s2.getLeft())) {
			return true;
		}

		return false;
	}

	private ContextAnnotation concat(ContextAnnotation s1, ContextAnnotation s2) {
		if (s1.isEmpty()) {
			return s2;
		} else if (s2.isEmpty()) {
			return s1;
		}

		return new ContextAnnotation(s1.getLeft(), s2.getRight());
	}

	private ContextAnnotation createContextAnnotation(Context left, Context right) {
		if (Context.EMPTY.equals(left) && Context.EMPTY.equals(right)) {
			return ContextAnnotation.EMPTY;
		} else {
			return new ContextAnnotation(left, right);
		}
	}

}
