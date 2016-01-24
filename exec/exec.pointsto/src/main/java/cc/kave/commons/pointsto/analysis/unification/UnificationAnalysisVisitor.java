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
package cc.kave.commons.pointsto.analysis.unification;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.pointsto.analysis.FailSafeNodeVisitor;
import cc.kave.commons.pointsto.analysis.ScopingVisitor;
import cc.kave.commons.pointsto.analysis.reference.DistinctMethodParameterReference;
import cc.kave.commons.pointsto.analysis.reference.DistinctReference;

public class UnificationAnalysisVisitor extends ScopingVisitor<UnificationAnalysisVisitorContext, Void> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UnificationAnalysisVisitor.class);

	private ReferenceAssignmentHandler referenceAssignmentHandler = new ReferenceAssignmentHandler();
	private MethodParameterVisitor parameterVisitor = new MethodParameterVisitor();

	@Override
	public Void visit(ISST sst, UnificationAnalysisVisitorContext context) {
		super.visit(sst, context);
		context.finalize();
		return null;
	}

	@Override
	public Void visit(IPropertyDeclaration decl, UnificationAnalysisVisitorContext context) {
		context.setCurrentMember(decl.getName());
		return super.visit(decl, context);
	}

	@Override
	public Void visit(IMethodDeclaration decl, UnificationAnalysisVisitorContext context) {
		context.setCurrentMember(decl.getName());
		return super.visit(decl, context);
	}

	@Override
	public Void visit(IAssignment stmt, UnificationAnalysisVisitorContext context) {
		context.setLastAssignment(stmt);

		IAssignableReference destRef = stmt.getReference();
		IAssignableExpression srcExpr = stmt.getExpression();

		// TODO IndexAccessExpr
		if (srcExpr instanceof IReferenceExpression) {
			IReference srcRef = ((IReferenceExpression) srcExpr).getReference();

			if (destRef instanceof IUnknownReference || srcRef instanceof IUnknownReference) {
				LOGGER.error("Ignoring an assignment with an unknown reference");
			} else {
				referenceAssignmentHandler.setContext(context);
				referenceAssignmentHandler.process(destRef, srcRef);
			}
		}

		return super.visit(stmt, context);
	}

	@Override
	public Void visit(IInvocationExpression entity, UnificationAnalysisVisitorContext context) {
		MethodName method = entity.getMethodName();

		// TODO replace with isUnknown once fixed
		if (method.equals(CsMethodName.UNKNOWN_NAME)) {
			// assume that unknown methods return a newly allocated object
			context.allocate(entity);
			return super.visit(entity, context);
		}

		if (method.isConstructor()) {
			context.allocate(entity);
		}

		List<ISimpleExpression> parameters = entity.getParameters();
		int numberOfFormalParameters = method.getParameters().size();
		for (int i = 0; i < parameters.size(); ++i) {
			ISimpleExpression parameterExpr = parameters.get(i);
			if (parameterExpr instanceof IReferenceExpression) {
				IReference parameterRef = ((IReferenceExpression) parameterExpr).getReference();

				// due to parameter arrays the number of actual parameters can be greater than the number for formal
				// parameters
				int formalParameterIndex = Math.min(i, numberOfFormalParameters - 1);
				ParameterName formalParameter = method.getParameters().get(formalParameterIndex);
				DistinctMethodParameterReference formalParameterRef = new DistinctMethodParameterReference(
						formalParameter, method);

				parameterRef.accept(parameterVisitor, new ContextReferencePair(context, formalParameterRef));
			}
		}

		if (!method.getReturnType().isVoidType()) {
			context.requestReturnReference(entity);
		}

		return super.visit(entity, context);
	}

	@Override
	public Void visit(IReturnStatement stmt, UnificationAnalysisVisitorContext context) {
		ISimpleExpression expr = stmt.getExpression();
		if (expr instanceof IReferenceExpression) {
			IReference ref = ((IReferenceExpression) expr).getReference();
			context.registerReturnReference(ref);
		}

		return null;
	}

	private static class ContextReferencePair {
		public UnificationAnalysisVisitorContext context;
		public DistinctReference reference;

		public ContextReferencePair(UnificationAnalysisVisitorContext context, DistinctReference reference) {
			this.context = context;
			this.reference = reference;
		}

	}

	private static class MethodParameterVisitor extends FailSafeNodeVisitor<ContextReferencePair, Void> {

		@Override
		public Void visit(IVariableReference varRef, ContextReferencePair context) {
			context.context.registerParameterReference(context.reference, varRef);
			return null;
		}

		@Override
		public Void visit(IFieldReference fieldRef, ContextReferencePair context) {
			context.context.registerParameterReference(context.reference, fieldRef);
			return null;
		}

		@Override
		public Void visit(IPropertyReference propertyRef, ContextReferencePair context) {
			context.context.registerParameterReference(context.reference, propertyRef);
			return null;
		}

		@Override
		public Void visit(IUnknownReference unknownRef, ContextReferencePair context) {
			LoggerFactory.getLogger(MethodParameterVisitor.class).error("Ignoring an unknown reference");
			return null;
		}
	}
}
