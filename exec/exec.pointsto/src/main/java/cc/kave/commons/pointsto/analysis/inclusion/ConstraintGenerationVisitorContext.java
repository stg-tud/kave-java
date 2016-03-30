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
package cc.kave.commons.pointsto.analysis.inclusion;

import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.parameter;
import static cc.kave.commons.pointsto.analysis.utils.SSTBuilder.variableReference;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IMemberName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.ssts.IExpression;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.AllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.ContextAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.EntryPointAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.ExprAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.allocations.StmtAllocationSite;
import cc.kave.commons.pointsto.analysis.inclusion.contexts.ContextFactory;
import cc.kave.commons.pointsto.analysis.inclusion.graph.ConstraintGraph;
import cc.kave.commons.pointsto.analysis.inclusion.graph.ConstraintGraphBuilder;
import cc.kave.commons.pointsto.analysis.reference.DistinctKeywordReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctReferenceCreationVisitor;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.kave.commons.pointsto.analysis.utils.PropertyAsFieldPredicate;
import cc.kave.commons.pointsto.analysis.visitors.DistinctReferenceVisitorContext;
import cc.kave.commons.pointsto.analysis.visitors.ThisReferenceOption;
import cc.kave.commons.pointsto.extraction.DeclarationMapper;

public class ConstraintGenerationVisitorContext extends DistinctReferenceVisitorContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConstraintGenerationVisitorContext.class);

	private final LanguageOptions languageOptions = LanguageOptions.getInstance();

	private final IParameterName thisParameter;

	private final DistinctReferenceCreationVisitor distinctReferenceCreationVisitor = new DistinctReferenceCreationVisitor();
	private final SimpleExpressionReader simpleExpressionReader;
	private final AssignableReferenceWriter destinationReferenceWriter;

	private final ConstraintGraphBuilder builder;

	private IMemberName currentMember;
	private IAssignment lastAssignment;

	private final Deque<Pair<ILambdaExpression, SetVariable>> lambdaStack = new ArrayDeque<>();

	private SetVariable contextThisVariable;

	public ConstraintGenerationVisitorContext(Context context, ContextFactory contextFactory) {
		super(context, ThisReferenceOption.PER_MEMBER);
		thisParameter = parameter(languageOptions.getThisName(),
				context.getTypeShape().getTypeHierarchy().getElement());
		DeclarationMapper declMapper = new DeclarationMapper(context);
		builder = new ConstraintGraphBuilder(this::getDistinctReference, declMapper, contextFactory);

		PropertyAsFieldPredicate treatPropertyAsField = new PropertyAsFieldPredicate(declMapper);
		simpleExpressionReader = new SimpleExpressionReader(builder, treatPropertyAsField);
		destinationReferenceWriter = new AssignableReferenceWriter(builder, treatPropertyAsField);

		initializeContext(context);
	}

	private void initializeContext(Context context) {
		DistinctKeywordReference thisDistRef = new DistinctKeywordReference(languageOptions.getThisName(),
				thisParameter.getValueType());
		namesToReferences.create(languageOptions.getThisName(), thisDistRef);
		AllocationSite thisAllocationSite = new ContextAllocationSite(context);
		builder.allocate(thisDistRef.getReference(), thisAllocationSite);
		contextThisVariable = builder.getVariable(thisDistRef.getReference());

		// call entry points
		for (IMethodDeclaration entryPointDecl : context.getSST().getEntryPoints()) {
			IMethodName method = entryPointDecl.getName();
			List<IParameterName> formalParameters = method.getParameters();
			List<SetVariable> actualParameters = formalParameters.stream().map(parameter -> {
				SetVariable parameterVar = builder.createTemporaryVariable();
				builder.allocate(parameterVar, new EntryPointAllocationSite(method, parameter));
				return parameterVar;
			}).collect(Collectors.toList());

			SetVariable dest = null;
			if (method.isConstructor()) {
				// constructor invocations require an allocated destination
				dest = builder.createTemporaryVariable();
				builder.allocate(dest, thisAllocationSite);
			}

			builder.invoke(dest, thisDistRef.getReference(), actualParameters, method);
		}
	}

	public ConstraintGraph createConstraintGraph() {
		return builder.createConstraintGraph();
	}

	private DistinctReference getDistinctReference(IReference ref) {
		return ref.accept(distinctReferenceCreationVisitor, namesToReferences);
	}

	public void setLastAssignment(IAssignment assignment) {
		this.lastAssignment = assignment;
	}

	public IAssignableReference getDestinationForExpr(IExpression expr) {
		if (lastAssignment == null || lastAssignment.getReference() instanceof IUnknownReference
				|| lastAssignment.getExpression() != expr) {
			return null;
		} else {
			return lastAssignment.getReference();
		}
	}

	@Override
	public void enterMember(IMemberName member) {
		super.enterMember(member);
		// connect context-this to method-this
		builder.alias(builder.getVariable(variableReference(languageOptions.getThisName())), contextThisVariable);
		// enter member, operations above happen in the global context
		this.currentMember = member;
	}

	@Override
	public void leaveMember() {
		super.leaveMember();
		this.currentMember = null;
	}

	public void enterLambda(ILambdaExpression lambdaExpr) {
		IAssignableReference destRef = getDestinationForExpr(lambdaExpr);

		SetVariable tempDest = (destRef != null) ? builder.createTemporaryVariable() : ConstructedTerm.BOTTOM;
		SetVariable returnVar = builder.storeFunction(tempDest, lambdaExpr);
		lambdaStack.addFirst(ImmutablePair.of(lambdaExpr, returnVar));

		if (destRef != null) {
			assign(destRef, tempDest);
		}
	}

	public void leaveLambda() {
		lambdaStack.removeFirst();
	}

	public ConstraintGraphBuilder getBuilder() {
		return builder;
	}

	public void assign(IAssignableReference dest, IReference src) {
		SetVariable srcSetVar = simpleExpressionReader.read(src);
		if (srcSetVar != null) {
			destinationReferenceWriter.assign(dest, srcSetVar);
		}
	}

	public void assign(IAssignableReference dest, ISimpleExpression src) {
		SetVariable srcSetVar = simpleExpressionReader.read(src);
		if (srcSetVar != null) {
			destinationReferenceWriter.assign(dest, srcSetVar);
		}
	}

	public void assign(IAssignableReference dest, SetVariable srcSetVar) {
		destinationReferenceWriter.assign(dest, srcSetVar);
	}

	public void expressionAllocation(IExpression expr) {
		IAssignableReference destRef = getDestinationForExpr(expr);
		if (destRef != null) {
			SetVariable temp = builder.createTemporaryVariable();
			builder.allocate(temp, new ExprAllocationSite(expr));
			assign(destRef, temp);
		}
	}

	public void invoke(IInvocationExpression invocation) {
		IAssignableReference destRef = getDestinationForExpr(invocation);
		SetVariable tempDest = (destRef != null) ? builder.createTemporaryVariable() : null;
		IMethodName method = invocation.getMethodName();

		if (method.isConstructor()) {
			if (tempDest == null) {
				// constructor invocation needs an allocated destination
				tempDest = builder.createTemporaryVariable();
			}
			builder.allocate(tempDest, new StmtAllocationSite(lastAssignment));
		}

		// sometimes the context analysis screws up and a method is called with less arguments than it has formal
		// parameters
		// unfortunately, we cannot assume that the positions of available arguments match the intended formal
		// parameters as arguments might be missing at any position
		List<IParameterName> formalParameters = method.getParameters();
		int parameterDiff = invocation.getParameters().size() - formalParameters.size()
				+ languageOptions.countOptionalParameters(formalParameters);
		if (parameterDiff < 0) {
			LOGGER.error("Skipping a method invocation which has less arguments than formal parameters");
		} else {
			List<SetVariable> actualParameters = simpleExpressionReader.read(invocation.getParameters());
			if (languageOptions.isDelegateInvocation(method)) {
				builder.invokeDelegate(tempDest, invocation.getReference(), actualParameters, method);
			} else {
				builder.invoke(tempDest, invocation.getReference(), actualParameters, method);
			}
		}

		if (destRef != null) {
			assign(destRef, tempDest);
		}
	}

	public void registerReturnedExpression(ISimpleExpression expr) {
		SetVariable returnedValue = simpleExpressionReader.read(expr);
		if (returnedValue != null) {
			if (lambdaStack.isEmpty()) {
				builder.alias(builder.getReturnVariable(currentMember), returnedValue);
			} else {
				builder.alias(lambdaStack.getFirst().getValue(), returnedValue);
			}
		}
	}

}
