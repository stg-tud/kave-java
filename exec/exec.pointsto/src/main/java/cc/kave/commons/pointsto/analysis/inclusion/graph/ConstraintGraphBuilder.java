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

import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.variableReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kave.commons.model.names.ILambdaName;
import cc.kave.commons.model.names.IMemberName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMemberReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.pointsto.analysis.inclusion.ConstraintResolver;
import cc.kave.commons.pointsto.analysis.inclusion.ConstructedTerm;
import cc.kave.commons.pointsto.analysis.inclusion.DeclarationLambdaStore;
import cc.kave.commons.pointsto.analysis.inclusion.LambdaTerm;
import cc.kave.commons.pointsto.analysis.inclusion.Projection;
import cc.kave.commons.pointsto.analysis.inclusion.RefTerm;
import cc.kave.commons.pointsto.analysis.inclusion.SetExpression;
import cc.kave.commons.pointsto.analysis.inclusion.SetVariable;
import cc.kave.commons.pointsto.analysis.inclusion.SetVariableFactory;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.AllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.UndefinedMemberAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.UniqueAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.ContextAnnotation;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.InclusionAnnotation;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.InvocationAnnotation;
import cc.kave.commons.pointsto.analysis.inclusion.annotations.StorageAnnotation;
import cc.kave.commons.pointsto.analysis.inclusion.contexts.ContextFactory;
import cc.kave.commons.pointsto.analysis.reference.DistinctLambdaParameterReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctMethodParameterReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctReference;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.kave.commons.pointsto.extraction.DeclarationMapper;

public class ConstraintGraphBuilder {

	private final Logger LOGGER = LoggerFactory.getLogger(ConstraintGraphBuilder.class);

	private final LanguageOptions languageOptions = LanguageOptions.getInstance();

	private final SetVariableFactory variableFactory = new SetVariableFactory();
	private final Map<DistinctReference, SetVariable> referenceVariables = new HashMap<>();
	private final Map<AllocationSite, SetVariable> objectVariables = new HashMap<>();

	private final Map<SetExpression, ConstraintNode> constraintNodes = new HashMap<>();

	private final Function<IReference, DistinctReference> referenceResolver;
	private final ContextFactory contextFactory;

	private final ConstraintResolver constraintResolver = new ConstraintResolver(this::getNode);
	private final DeclarationMapper declMapper;
	private final DeclarationLambdaStore declLambdaStore;

	private final RefTerm staticObject;
	private final Map<IMemberName, SetVariable> staticMembers = new HashMap<>();

	public ConstraintGraphBuilder(Function<IReference, DistinctReference> referenceResolver,
			DeclarationMapper declMapper, ContextFactory contextFactory) {
		this.referenceResolver = referenceResolver;
		this.declMapper = declMapper;
		this.declLambdaStore = new DeclarationLambdaStore(this::getVariable, variableFactory, constraintResolver,
				declMapper);
		this.contextFactory = contextFactory;

		staticObject = new RefTerm(new UniqueAllocationSite(TypeName.UNKNOWN_NAME), ConstructedTerm.BOTTOM);
	}

	public ContextFactory getContextFactory() {
		return contextFactory;
	}

	public ConstraintGraph createConstraintGraph() {
		initializeStaticMembers();
		return new ConstraintGraph(referenceVariables, declLambdaStore, constraintNodes, contextFactory);
	}

	private SetVariable getVariable(DistinctReference distRef) {
		Objects.requireNonNull(distRef);

		SetVariable variable = referenceVariables.get(distRef);
		if (variable == null) {
			variable = variableFactory.create();
			referenceVariables.put(distRef, variable);
		}

		return variable;
	}

	public SetVariable getVariable(IVariableReference varRef) {
		// redirect super-refs to this-refs
		if (languageOptions.getSuperName().equals(varRef.getIdentifier())) {
			varRef = variableReference(languageOptions.getThisName());
		}
		return getVariable(referenceResolver.apply(varRef));
	}

	private SetVariable getVariable(AllocationSite allocationSite) {
		Objects.requireNonNull(allocationSite);

		SetVariable variable = objectVariables.get(allocationSite);
		if (variable == null) {
			variable = variableFactory.create();
			objectVariables.put(allocationSite, variable);
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

	private SetVariable getStaticMemberVariable(IMemberName member) {
		SetVariable var = staticMembers.get(member);
		if (var == null) {
			var = createTemporaryVariable();
			staticMembers.put(member, var);
		}
		return var;
	}

	/**
	 * Ensures that a member reference appears in the least solution of the {@link ConstraintGraph} by reading them into
	 * a variable associated to the {@link DistinctReference}.
	 */
	private void ensureStorageMemberHasVariable(IMemberReference memberRef, IMemberName member) {
		DistinctReference distRef = referenceResolver.apply(memberRef);
		if (!referenceVariables.containsKey(distRef)) {
			SetVariable temp = createTemporaryVariable();
			referenceVariables.put(distRef, temp);
			readMember(temp, memberRef, member);
		}
	}

	private void initializeStaticMembers() {
		for (Map.Entry<IMemberName, SetVariable> memberEntry : staticMembers.entrySet()) {
			if (declMapper.get(memberEntry.getKey()) != null) {
				// only initialize members that are without a definition
				continue;
			}

			ConstraintNode memberNode = getNode(memberEntry.getValue());
			if (memberNode.getPredecessors().isEmpty()) {
				RefTerm obj = new RefTerm(
						new UndefinedMemberAllocationSite(memberEntry.getKey(), memberEntry.getKey().getValueType()),
						createTemporaryVariable());
				memberNode.addPredecessor(
						new ConstraintEdge(getNode(obj), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));
			}
		}
	}

	public SetVariable createTemporaryVariable() {
		return variableFactory.create();
	}

	public void allocate(IVariableReference dest, AllocationSite allocationSite) {
		SetVariable destSetVar = getVariable(referenceResolver.apply(dest));
		allocate(destSetVar, allocationSite);
	}

	public void allocate(SetVariable destSetVar, AllocationSite allocationSite) {
		RefTerm ref = new RefTerm(allocationSite, getVariable(allocationSite));

		ConstraintNode destNode = getNode(destSetVar);
		destNode.addPredecessor(new ConstraintEdge(getNode(ref), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));
	}

	public void alias(IVariableReference dest, IVariableReference src) {
		SetVariable destSetVar = getVariable(referenceResolver.apply(dest));
		SetVariable srcSetVar = getVariable(referenceResolver.apply(src));

		// src ⊆ dest
		constraintResolver.addConstraint(srcSetVar, destSetVar, InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY);
	}

	public void alias(SetVariable dest, SetVariable src) {
		// src ⊆ dest
		constraintResolver.addConstraint(src, dest, InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY);
	}

	public void readMember(IVariableReference dest, IMemberReference src, IMemberName member) {
		SetVariable destSetVar = getVariable(dest);
		readMember(destSetVar, src, member);
	}

	public void readMember(SetVariable destSetVar, IMemberReference src, IMemberName member) {
		ensureStorageMemberHasVariable(src, member);

		if (member.isStatic()) {
			readStaticMember(destSetVar, member);
		} else {
			SetVariable recvSetVar = getVariable(src.getReference());
			SetVariable temp = createTemporaryVariable();

			Projection projection = new Projection(RefTerm.class, RefTerm.READ_INDEX, temp);
			// recv ⊆ proj
			ConstraintNode recvNode = getNode(recvSetVar);
			recvNode.addSuccessor(
					new ConstraintEdge(getNode(projection), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));

			// temp ⊆_m dest
			ConstraintNode tempNode = getNode(temp);
			tempNode.addSuccessor(
					new ConstraintEdge(getNode(destSetVar), new StorageAnnotation(member), ContextAnnotation.EMPTY));
		}
	}

	private void readStaticMember(SetVariable destVar, IMemberName member) {
		SetVariable memberVar = getStaticMemberVariable(member);
		// memberVar ⊆ dest
		constraintResolver.addConstraint(memberVar, destVar, InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY);
	}

	public void readArray(IVariableReference dest, IIndexAccessReference src) {
		readArray(getVariable(dest), src);
	}

	public void readArray(SetVariable destSetVar, IIndexAccessReference src) {
		SetVariable arraySetVar = getVariable(src.getExpression().getReference());
		SetVariable temp = createTemporaryVariable();

		Projection projection = new Projection(RefTerm.class, RefTerm.READ_INDEX, temp);
		// array ⊆ proj
		ConstraintNode arrayNode = getNode(arraySetVar);
		arrayNode.addSuccessor(
				new ConstraintEdge(getNode(projection), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));

		// temp ⊆ dest
		ConstraintNode tempNode = getNode(temp);
		tempNode.addSuccessor(
				new ConstraintEdge(getNode(destSetVar), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));
	}

	public void writeMember(IMemberReference dest, IVariableReference src, IMemberName member) {
		SetVariable srcSetVar = getVariable(src);
		writeMember(dest, srcSetVar, member);
	}

	public void writeMember(IMemberReference dest, SetVariable srcSetVar, IMemberName member) {
		ensureStorageMemberHasVariable(dest, member);

		if (member.isStatic()) {
			writeStaticMember(srcSetVar, member);
		} else {
			SetVariable recvSetVar = getVariable(dest.getReference());
			SetVariable temp = createTemporaryVariable();

			Projection projection = new Projection(RefTerm.class, RefTerm.WRITE_INDEX, temp);

			// recv ⊆ proj
			ConstraintNode recvNode = getNode(recvSetVar);
			recvNode.addSuccessor(
					new ConstraintEdge(getNode(projection), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));

			// src ⊆_m temp
			ConstraintNode tempNode = getNode(temp);
			tempNode.addPredecessor(
					new ConstraintEdge(getNode(srcSetVar), new StorageAnnotation(member), ContextAnnotation.EMPTY));
		}
	}

	private void writeStaticMember(SetVariable srcVar, IMemberName member) {
		SetVariable memberVar = getStaticMemberVariable(member);
		// src ⊆ memberVar
		constraintResolver.addConstraint(srcVar, memberVar, InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY);
	}

	public void writeArray(IIndexAccessReference dest, IVariableReference src) {
		writeArray(dest, getVariable(src));
	}

	public void writeArray(IIndexAccessReference dest, SetVariable srcSetVar) {
		SetVariable arraySetVar = getVariable(dest.getExpression().getReference());
		writeArray(arraySetVar, srcSetVar);
	}

	private void writeArray(SetVariable arraySetVar, SetVariable srcSetVar) {
		SetVariable temp = createTemporaryVariable();

		Projection projection = new Projection(RefTerm.class, RefTerm.WRITE_INDEX, temp);

		// array ⊆ proj
		ConstraintNode arrayNode = getNode(arraySetVar);
		arrayNode.addSuccessor(
				new ConstraintEdge(getNode(projection), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));

		// src ⊆ temp
		ConstraintNode tempNode = getNode(temp);
		tempNode.addPredecessor(
				new ConstraintEdge(getNode(srcSetVar), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));
	}

	public void invoke(SetVariable dest, IVariableReference recv, List<SetVariable> normalizedActualParameters,
			IMethodName method) {
		List<IParameterName> formalParameters = method.getParameters();
		ITypeName returnType = method.getReturnType();

		if (formalParameters.isEmpty() && !normalizedActualParameters.isEmpty()) {
			LOGGER.error("Attempted to invoke method {}.{} which expects zero parameters with {} actual parameters",
					method.getDeclaringType().getName(), method.getName(), normalizedActualParameters.size());
			normalizedActualParameters = Collections.emptyList();
		}

		LambdaTerm lambda = getInvocationLambda(normalizedActualParameters, dest, formalParameters, returnType);

		ConstraintNode recvNode;
		InvocationAnnotation inclusionAnnotation;
		if (method.isConstructor()) {
			// constructors use their destination as receiver and do not rely on dynamic dispatch
			recvNode = getNode(dest);
			inclusionAnnotation = new InvocationAnnotation(method, false);
		} else if (method.isExtensionMethod()) {
			// assume that the first parameter is the this-reference
			if (normalizedActualParameters.isEmpty()) {
				LOGGER.error("Ignoring an extension method call without any parameters");
				return;
			} else if (normalizedActualParameters.get(0) == null) {
				LOGGER.error("Ignoring an extension method call without a valid receiver");
				return;
			}
			recvNode = getNode(normalizedActualParameters.get(0));
			inclusionAnnotation = new InvocationAnnotation(method, false);
		} else if (method.isStatic() && !method.isExtensionMethod()) {
			recvNode = getNode(createTemporaryVariable());
			recvNode.addPredecessor(
					new ConstraintEdge(getNode(staticObject), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));
			// static methods are not dispatched virtually
			inclusionAnnotation = new InvocationAnnotation(method, false);
		} else {
			// super invocations are not subject to dynamic dispatch
			if (languageOptions.getSuperName().equals(recv.getIdentifier())) {
				inclusionAnnotation = new InvocationAnnotation(method, false);
			} else {
				inclusionAnnotation = new InvocationAnnotation(method);
			}

			SetVariable recvSetVar = getVariable(recv);
			// recv ⊆_method lambda
			recvNode = getNode(recvSetVar);
		}

		recvNode.addSuccessor(new ConstraintEdge(getNode(lambda), inclusionAnnotation, ContextAnnotation.EMPTY));
	}

	public void invokeDelegate(SetVariable dest, IVariableReference delegate,
			List<SetVariable> normalizedActualParameters, IMethodName method) {
		LambdaTerm lambda = getInvocationLambda(normalizedActualParameters, dest, method.getParameters(),
				method.getReturnType());

		// delegate ⊆ lambda
		ConstraintNode delegateNode = getNode(getVariable(delegate));
		delegateNode
				.addSuccessor(new ConstraintEdge(getNode(lambda), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));
	}

	public void invokeSetProperty(IPropertyReference propertyRef, SetVariable valueSetVar) {
		LambdaTerm invocationLambda = LambdaTerm
				.newPropertyLambda(Arrays.asList(ConstructedTerm.BOTTOM, valueSetVar, ConstructedTerm.BOTTOM));
		ConstraintNode recvNode;
		if (propertyRef.getPropertyName().isStatic()) {
			recvNode = getNode(createTemporaryVariable());
			recvNode.addPredecessor(
					new ConstraintEdge(getNode(staticObject), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));
		} else {
			recvNode = getNode(getVariable(propertyRef.getReference()));
		}

		// super invocations are not subject to dynamic dispatch
		boolean dynamicallyDispatched = !languageOptions.getSuperName()
				.equals(propertyRef.getReference().getIdentifier());

		// recv ⊆_property lambda
		recvNode.addSuccessor(new ConstraintEdge(getNode(invocationLambda),
				new InvocationAnnotation(propertyRef.getPropertyName(), dynamicallyDispatched),
				ContextAnnotation.EMPTY));
	}

	public void invokeGetProperty(SetVariable destSetVar, IPropertyReference propertyRef) {
		LambdaTerm invocationLambda = LambdaTerm
				.newPropertyLambda(Arrays.asList(ConstructedTerm.BOTTOM, ConstructedTerm.BOTTOM, destSetVar));
		ConstraintNode recvNode;
		if (propertyRef.getPropertyName().isStatic()) {
			recvNode = getNode(createTemporaryVariable());
			recvNode.addPredecessor(
					new ConstraintEdge(getNode(staticObject), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));
		} else {
			recvNode = getNode(getVariable(propertyRef.getReference()));
		}

		// super invocations are not subject to dynamic dispatch
		boolean dynamicallyDispatched = !languageOptions.getSuperName()
				.equals(propertyRef.getReference().getIdentifier());

		// recv ⊆_property lambda
		recvNode.addSuccessor(new ConstraintEdge(getNode(invocationLambda),
				new InvocationAnnotation(propertyRef.getPropertyName(), dynamicallyDispatched),
				ContextAnnotation.EMPTY));
	}

	public void storeFunction(IVariableReference dest, IMethodReference src) {
		storeFunction(getVariable(dest), src);
	}

	public void storeFunction(SetVariable destSetVar, IMethodReference src) {
		IMethodName method = src.getMethodName();
		SetVariable thisSetVar;
		if (method.isStatic() && !method.isExtensionMethod()) {
			thisSetVar = ConstructedTerm.BOTTOM;
		} else {
			thisSetVar = getVariable(src.getReference());
		}
		storeFunction(destSetVar, thisSetVar, method.getParameters(), method.getReturnType(),
				p -> new DistinctMethodParameterReference(p, method));
	}

	public SetVariable storeFunction(SetVariable dest, ILambdaExpression lambdaExpr) {
		ILambdaName lambdaName = lambdaExpr.getName();
		LambdaTerm term = storeFunction(dest, ConstructedTerm.BOTTOM, lambdaName.getParameters(),
				lambdaName.getReturnType(), p -> new DistinctLambdaParameterReference(p, lambdaExpr));

		if (!lambdaName.getReturnType().isVoidType()) {
			return term.getArgument(term.getNumberOfArguments() - 1);
		} else {
			return ConstructedTerm.BOTTOM;
		}
	}

	private LambdaTerm storeFunction(SetVariable dest, SetVariable thisSetVar, List<IParameterName> formalParameters,
			ITypeName returnType, Function<IParameterName, DistinctReference> distRefBuilder) {
		// construct lambda term
		List<SetVariable> variables = new ArrayList<>(formalParameters.size() + 2);
		variables.add(thisSetVar);
		for (IParameterName parameter : formalParameters) {
			DistinctReference parameterDistRef = distRefBuilder.apply(parameter);
			variables.add(getVariable(parameterDistRef));
		}
		if (!returnType.isVoidType()) {
			variables.add(createTemporaryVariable());
		}
		LambdaTerm lambda = LambdaTerm.newMethodLambda(variables, formalParameters, returnType);

		// lambda ⊆ dest
		ConstraintNode destNode = getNode(dest);
		destNode.addPredecessor(
				new ConstraintEdge(getNode(lambda), InclusionAnnotation.EMPTY, ContextAnnotation.EMPTY));

		return lambda;
	}

	public SetVariable getReturnVariable(IMemberName member) {
		LambdaTerm lambda = declLambdaStore.getDeclarationLambda(member);
		return lambda.getArgument(lambda.getNumberOfArguments() - 1);
	}

	private LambdaTerm getInvocationLambda(List<SetVariable> actualParameters, SetVariable dest,
			List<IParameterName> formalParameters, ITypeName returnType) {
		List<SetVariable> variables = new ArrayList<>(formalParameters.size() + 2);

		variables.add(ConstructedTerm.BOTTOM);
		SetVariable parameterArrayTemporary = null;

		for (int i = 0; i < actualParameters.size(); ++i) {
			SetVariable actualParameter = actualParameters.get(i);
			int formalParameterIndex = Math.min(i, formalParameters.size() - 1);
			IParameterName formalParameter = formalParameters.get(formalParameterIndex);

			if (formalParameter.isExtensionMethodParameter()) {
				// this-parameter handled prior to loop
				continue;
			}

			if (actualParameter == null) {
				actualParameter = ConstructedTerm.BOTTOM;
			}

			if (formalParameter.isParameterArray()) {
				if (parameterArrayTemporary == null) {
					parameterArrayTemporary = initializeParameterArray(formalParameter);
					variables.add(parameterArrayTemporary);
				}

				writeArray(parameterArrayTemporary, actualParameter);
			} else if (i > formalParameters.size() - 1) {
				LOGGER.error("Pruning {} extra method arguments", actualParameters.size() - formalParameters.size());
				break;
			} else {
				variables.add(actualParameter);
			}
		}

		// handle unset optional parameters
		for (int i = actualParameters.size(); i < formalParameters.size(); ++i) {
			IParameterName parameter = formalParameters.get(i);
			if (parameter.isParameterArray()) {
				parameterArrayTemporary = initializeParameterArray(parameter);
				variables.add(parameterArrayTemporary);
			} else {
				variables.add(ConstructedTerm.BOTTOM);
			}
		}

		if (!returnType.isVoidType()) {
			if (dest != null) {
				variables.add(dest);
			} else {
				variables.add(ConstructedTerm.BOTTOM);
			}
		}

		return LambdaTerm.newMethodLambda(variables, formalParameters, returnType);
	}

	private SetVariable initializeParameterArray(IParameterName parameter) {
		SetVariable array = createTemporaryVariable();
		allocate(array, new UniqueAllocationSite(parameter.getValueType().getArrayBaseType()));
		return array;
	}

}
