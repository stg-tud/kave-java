package cc.kave.commons.model.ssts.impl.visitor.inlining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.ssts.IExpression;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.visitor.AbstractNodeVisitor;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.CountReturnsVisitor;
import cc.kave.commons.model.ssts.references.IMemberReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IReturnStatement;

public class InliningIExpressionVisitor extends AbstractNodeVisitor<InliningContext, IExpression> {

	public IExpression visit(IInvocationExpression expr, InliningContext context) {
		IMethodDeclaration method = context.getNonEntryPoint(expr.getMethodName());
		if (method != null) {
			List<IStatement> body = new ArrayList<>();
			Map<IVariableReference, IVariableReference> preChangedNames = new HashMap<>();
			if (method.getName().hasParameters()) {
				List<ParameterName> parameters = method.getName().getParameters();
				for (int i = 0; i < parameters.size(); i++) {
					ParameterName parameter = parameters.get(i);
					// TODO what to do when parameter has out/ref keyWord but is
					// no ReferenceExpression ?
					if (parameter.isPassedByReference() && expr.getParameters().get(i) instanceof ReferenceExpression) {
						ReferenceExpression refExpr = (ReferenceExpression) expr.getParameters().get(i);
						if (refExpr.getReference() instanceof VariableReference) {
							preChangedNames.put(SSTUtil.variableReference(parameter.getName()),
									(IVariableReference) refExpr.getReference());
						} else if (refExpr.getReference() instanceof IMemberReference) {
							preChangedNames.put(SSTUtil.variableReference(parameter.getName()),
									((IMemberReference) refExpr.getReference()).getReference());
						}
						continue;
					} else if (parameter.isParameterArray()) {
						body.add(SSTUtil.declare(parameter.getName(), parameter.getValueType()));
						body.add(SSTUtil.assigmentToLocal(parameter.getName(), new UnknownExpression()));
						break;
					}
					if (i < expr.getParameters().size()) {
						body.add(SSTUtil.declare(parameter.getName(), parameter.getValueType()));
						body.add(SSTUtil.assigmentToLocal(parameter.getName(), expr.getParameters().get(i)));
					}
				}
			}
			// Setting up guarding if needed
			boolean guardsNeeded = method.accept(new CountReturnsVisitor(), null) > 1 ? true : false;
			if (guardsNeeded) {
				IVariableDeclaration variable = SSTUtil.declare(
						InliningUtil.RESULT_NAME + method.getName().getIdentifier(), method.getName().getReturnType());
				body.add(variable);
				body.add(SSTUtil.declare(InliningUtil.RESULT_FLAG + method.getName().getIdentifier(),
						InliningUtil.GOT_RESULT_TYPE));
				ConstantValueExpression constant = new ConstantValueExpression();
				constant.setValue("true");
				body.add(SSTUtil.assigmentToLocal(InliningUtil.RESULT_FLAG + method.getName().getIdentifier(),
						constant));
			}
			body.addAll(method.getBody());
			context.enterScope(body, preChangedNames);
			if (guardsNeeded)
				context.setResultName(method.getName().getIdentifier());
			context.setInline(true);
			if (!guardsNeeded) {
				for (IStatement statement : body) {
					if (statement instanceof IReturnStatement) {
						IReturnStatement stmt = (IReturnStatement) statement;
						context.leaveScope();
						return stmt.getExpression();
					}
					statement.accept(context.getStatementVisitor(), context);
				}
			} else {
				InliningUtil.visit(body, context.getBody(), context);
				context.leaveScope();
				context.setInline(false);
				return SSTUtil.referenceExprToVariable(InliningUtil.RESULT_NAME + method.getName().getIdentifier());
			}
			context.leaveScope();
			context.setInline(false);
			return null;
		} else {
			return expr;
		}
	}

	public IExpression visit(IReferenceExpression expr, InliningContext context) {
		ReferenceExpression refExpr = new ReferenceExpression();
		refExpr.setReference(expr.getReference().accept(context.getReferenceVisitor(), context));
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
		ComposedExpression composed = new ComposedExpression();
		for (IVariableReference ref : expr.getReferences())
			composed.getReferences().add((IVariableReference) ref.accept(context.getReferenceVisitor(), context));
		return composed;
	}

	@Override
	public IExpression visit(ILoopHeaderBlockExpression expr, InliningContext context) {
		LoopHeaderBlockExpression loopHeader = new LoopHeaderBlockExpression();
		InliningUtil.visit(expr.getBody(), loopHeader.getBody(), context);
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
		InliningUtil.visit(expr.getBody(), expression.getBody(), context);
		return expression;
	}

	@Override
	public IExpression visit(ICompletionExpression entity, InliningContext context) {
		CompletionExpression expression = new CompletionExpression();
		expression.setObjectReference(
				(IVariableReference) entity.getObjectReference().accept(context.getReferenceVisitor(), context));
		expression.setToken(entity.getToken());
		expression.setTypeReference(entity.getTypeReference());
		return expression;
	}
}
