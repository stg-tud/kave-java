/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.ssts.impl.visitor.inlining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.ssts.IExpression;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ITypeCheckExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.expressions.assignable.BinaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CastExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.TypeCheckExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.CountReturnContext;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.CountReturnsVisitor;
import cc.kave.commons.model.ssts.references.IMemberReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.utils.SSTCloneUtil;

public class InliningIExpressionVisitor extends AbstractThrowingNodeVisitor<InliningContext, IExpression> {

	public IExpression visit(IInvocationExpression expr, InliningContext context) {
		IMethodDeclaration possibleMethod = context.getNonEntryPoint(expr.getMethodName());
		if (possibleMethod != null) {
			IMethodDeclaration method = SSTCloneUtil.clone(possibleMethod, IMethodDeclaration.class);
			context.addInlinedMethod(possibleMethod);
			List<IStatement> body = new ArrayList<>();
			Map<IVariableReference, IVariableReference> preChangedNames = new HashMap<>();

			if (method.getName().hasParameters()) {
				createParameterVariables(body, method.getName().getParameters(), expr.getParameters(), preChangedNames);
			}
			// Checking if guards statements are needed and setting them up if
			// so
			CountReturnContext countReturnContext = new CountReturnContext();
			method.accept(new CountReturnsVisitor(), countReturnContext);
			int returnStatementCount = countReturnContext.returnCount;
			context.setVoid(countReturnContext.isVoid);
			boolean guardsNeeded = returnStatementCount > 1 ? true : false;
			if (returnStatementCount == 1 && method.getBody().size() > 0) {
				guardsNeeded = !(method.getBody().get(method.getBody().size() - 1) instanceof IReturnStatement);
			}
			if (guardsNeeded) {
				setupGuardVariables(method.getName(), body, context);
			}
			// Add all Statements from the method body
			body.addAll(method.getBody());
			// enter a new Scope
			context.enterScope(body, preChangedNames);
			context.setInline(true);
			context.setGuardVariableNames(method.getName().getIdentifier());
			if (!guardsNeeded) {
				for (IStatement statement : body) {
					if (statement instanceof IReturnStatement) {
						IReturnStatement stmt = (IReturnStatement) statement;
						context.leaveScope();
						context.setInline(false);
						return stmt.getExpression();
					}
					statement.accept(context.getStatementVisitor(), context);
				}
			} else {
				context.visitBlock(body, context.getBody());
				context.leaveScope();
				context.setInline(false);
				if (!context.isVoid())
					return SSTUtil.referenceExprToVariable(context.getResultName());
				else
					return null;
			}
			context.leaveScope();
			context.setInline(false);
			return null;
		} else {
			InvocationExpression expression = new InvocationExpression();
			expression.setMethodName(expr.getMethodName());
			for (ISimpleExpression e : expr.getParameters()) {
				expression.getParameters().add(e);
			}
			// TODO: changed
			expression.setReference(expr.getReference());
			expr.getReference().accept(context.getReferenceVisitor(), context);

			return expression;
		}
	}

	private void setupGuardVariables(IMethodName methodName, List<IStatement> body, InliningContext context) {
		context.setGuardVariableNames(methodName.getIdentifier());
		if (!context.isVoid()) {
			IVariableDeclaration variable = SSTUtil.declare(context.getResultName(), methodName.getReturnType());
			body.add(variable);
		}
		body.add(SSTUtil.declare(context.getGotResultName(), context.GOT_RESULT_TYPE));
		ConstantValueExpression constant = new ConstantValueExpression();
		constant.setValue("true");
		body.add(SSTUtil.assignmentToLocal(context.getGotResultName(), constant));

	}

	private void createParameterVariables(List<IStatement> body, List<IParameterName> parameters,
			List<ISimpleExpression> expressions, Map<IVariableReference, IVariableReference> preChangedNames) {
		for (int i = 0; i < parameters.size(); i++) {
			IParameterName parameter = parameters.get(i);
			// TODO what to do when parameter has out/ref keyWord but is
			// no ReferenceExpression ?
			if (!parameter.isOptional() || expressions.size() == parameters.size()) {

				/*
				 * if (parameter.isPassedByReference() &&
				 * !parameter.isParameterArray() && i < expressions.size() &&
				 * expressions.get(i) instanceof ReferenceExpression) {
				 * ReferenceExpression refExpr = (ReferenceExpression)
				 * expressions.get(i); if (refExpr.getReference() instanceof
				 * VariableReference) {
				 * preChangedNames.put(SSTUtil.variableReference(parameter.
				 * getName()), (IVariableReference) refExpr.getReference()); }
				 * else if (refExpr.getReference() instanceof IMemberReference)
				 * { preChangedNames.put(SSTUtil.variableReference(parameter.
				 * getName()), ((IMemberReference)
				 * refExpr.getReference()).getReference()); } continue; } else
				 * if (parameter.isParameterArray()) {
				 * body.add(SSTUtil.declare(parameter.getName(),
				 * parameter.getValueType()));
				 * body.add(SSTUtil.assigmentToLocal(parameter.getName(), new
				 * UnknownExpression())); break; } else {
				 * body.add(SSTUtil.declare(parameter.getName(),
				 * parameter.getValueType()));
				 * body.add(SSTUtil.assigmentToLocal(parameter.getName(),
				 * expressions.get(i))); }
				 */
				if (parameter.isParameterArray() && !parameter.isPassedByReference()) {
					body.add(SSTUtil.declare(parameter.getName(), parameter.getValueType()));
					body.add(SSTUtil.assignmentToLocal(parameter.getName(), new UnknownExpression()));
					break;
				} else if (i < expressions.size() && expressions.get(i) instanceof ReferenceExpression) {
					ReferenceExpression refExpr = SSTCloneUtil.clone(expressions.get(i), ReferenceExpression.class);
					if (refExpr.getReference() instanceof VariableReference) {
						preChangedNames.put(SSTUtil.variableReference(parameter.getName()),
								(IVariableReference) refExpr.getReference());
					} else if (refExpr.getReference() instanceof IMemberReference) {
						preChangedNames.put(SSTUtil.variableReference(parameter.getName()),
								((IMemberReference) refExpr.getReference()).getReference());
					}
				} else if (i < expressions.size()) {
					body.add(SSTUtil.declare(parameter.getName(), parameter.getValueType()));
					body.add(SSTUtil.assignmentToLocal(parameter.getName(), expressions.get(i)));
				}
			}
		}

	}

	public IExpression visit(IReferenceExpression expr, InliningContext context) {
		// TODO: changed
		ReferenceExpression refExpr = new ReferenceExpression();
		refExpr.setReference(expr.getReference());
		expr.getReference().accept(context.getReferenceVisitor(), context);
		return refExpr;
	}

	@Override
	public IExpression visit(IConstantValueExpression expr, InliningContext context) {
		ConstantValueExpression constant = new ConstantValueExpression();
		constant.setValue(expr.getValue());
		return constant;
	}

	@Override
	public IExpression visit(IComposedExpression expr, InliningContext context) {
		// TODO: changed
		ComposedExpression composed = new ComposedExpression();
		for (IVariableReference ref : expr.getReferences()) {
			composed.getReferences().add(ref);
			ref.accept(context.getReferenceVisitor(), context);
		}
		return composed;
	}

	@Override
	public IExpression visit(ILoopHeaderBlockExpression expr, InliningContext context) {
		LoopHeaderBlockExpression loopHeader = new LoopHeaderBlockExpression();
		context.visitBlock(expr.getBody(), loopHeader.getBody());
		return loopHeader;
	}

	@Override
	public IExpression visit(IIfElseExpression expr, InliningContext context) {
		IfElseExpression expression = new IfElseExpression();
		expression
				.setCondition((ISimpleExpression) expr.getCondition().accept(context.getExpressionVisitor(), context));
		expression.setElseExpression(
				(ISimpleExpression) expr.getElseExpression().accept(context.getExpressionVisitor(), context));
		expression.setThenExpression(
				(ISimpleExpression) expr.getThenExpression().accept(context.getExpressionVisitor(), context));
		return expression;
	}

	@Override
	public IExpression visit(INullExpression expr, InliningContext context) {
		NullExpression expression = new NullExpression();
		return expression;
	}

	@Override
	public IExpression visit(IUnknownExpression unknownExpr, InliningContext context) {
		UnknownExpression expression = new UnknownExpression();
		return expression;
	}

	@Override
	public IExpression visit(ILambdaExpression expr, InliningContext context) {
		LambdaExpression expression = new LambdaExpression();
		expression.setName(expr.getName());
		context.visitBlock(expr.getBody(), expression.getBody());
		return expression;
	}

	@Override
	public IExpression visit(ICompletionExpression entity, InliningContext context) {
		// TODO: changed
		CompletionExpression expression = new CompletionExpression();
		IVariableReference objectReference = entity.getVariableReference();
		if (objectReference != null){
			expression.setObjectReference(
					objectReference);
			objectReference.accept(context.getReferenceVisitor(), context);
		}
		expression.setTypeReference(entity.getTypeReference());
		expression.setToken(entity.getToken());
		return expression;
		// TODO tests
	}

	@Override
	public IExpression visit(IUnaryExpression expr, InliningContext context) {
		UnaryExpression expression = new UnaryExpression();
		expression.setOperator(expr.getOperator());
		expression.setOperand((ISimpleExpression) expr.getOperand().accept(context.getExpressionVisitor(), context));
		return expression;
	}

	@Override
	public IExpression visit(ICastExpression expr, InliningContext context) {
		// TODO: changed
		CastExpression expression = new CastExpression();
		expression.setOperator(expr.getOperator());
		expression.setTargetType(expr.getTargetType());
		expression
				.setReference(expr.getReference());
		expr.getReference().accept(context.getReferenceVisitor(), context);
		return expression;
	}

	@Override
	public IExpression visit(ITypeCheckExpression expr, InliningContext context) {
		// TODO: changed
		TypeCheckExpression expression = new TypeCheckExpression();
		expression
				.setReference(expr.getReference());
		expr.getReference().accept(context.getReferenceVisitor(), context);
		expression.setType(expr.getType());
		return expression;
	}

	@Override
	public IExpression visit(IBinaryExpression expr, InliningContext context) {
		BinaryExpression expression = new BinaryExpression();
		expression.setOperator(expr.getOperator());
		expression.setLeftOperand(
				(ISimpleExpression) expr.getLeftOperand().accept(context.getExpressionVisitor(), context));
		expression.setRightOperand(
				(ISimpleExpression) expr.getRightOperand().accept(context.getExpressionVisitor(), context));
		return expression;
	}

	@Override
	public IExpression visit(IIndexAccessExpression expr, InliningContext context) {
		// TODO: changed
		IndexAccessExpression expression = new IndexAccessExpression();
		for (ISimpleExpression e : expr.getIndices())
			expression.getIndices().add((ISimpleExpression) e.accept(context.getExpressionVisitor(), context));
		expression
				.setReference(expr.getReference());
		expr.getReference().accept(context.getReferenceVisitor(), context);
		return expression;
	}
}
