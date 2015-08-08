package cc.kave.commons.model.ssts.impl.visitor.inlining;

import cc.kave.commons.model.ssts.IExpression;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
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
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractNodeVisitor;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.utils.visitor.InliningContext;
import cc.kave.commons.utils.visitor.InliningUtils;

public class InliningIExpressionVisitor extends AbstractNodeVisitor<InliningContext, IExpression> {

	public IExpression visit(IInvocationExpression expr, InliningContext context) {
		IMethodDeclaration method = context.getNonEntryPoint(expr.getMethodName());
		if (method != null) {
			// TODO: Method Parameter
			// TODO: GUARDS
			context.enterScope(method.getBody());
			context.setInline(true);
			for (IStatement statement : method.getBody()) {
				if (statement instanceof IReturnStatement) {
					IReturnStatement stmt = (IReturnStatement) statement;
					context.leaveScope();
					return stmt.getExpression();
				}
				statement.accept(new InliningIStatementVisitor(), context);
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
		refExpr.setReference(expr.getReference().accept(new InliningIReferenceVisitor(), context));
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
			composed.getReferences().add((IVariableReference) ref.accept(new InliningIReferenceVisitor(), context));
		return composed;
	}

	@Override
	public IExpression visit(ILoopHeaderBlockExpression expr, InliningContext context) {
		LoopHeaderBlockExpression loopHeader = new LoopHeaderBlockExpression();
		InliningUtils.visit(expr.getBody(), loopHeader.getBody(), context);
		return loopHeader;
	}

	@Override
	public IExpression visit(IIfElseExpression expr, InliningContext context) {
		IfElseExpression expression = new IfElseExpression();
		expression.setCondition(
				(ISimpleExpression) expr.getCondition().accept(new InliningIExpressionVisitor(), context));
		expression.setElseExpression(
				(ISimpleExpression) expr.getElseExpression().accept(new InliningIExpressionVisitor(), context));
		expression.setThenExpression(
				(ISimpleExpression) expr.getThenExpression().accept(new InliningIExpressionVisitor(), context));
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
		InliningUtils.visit(expr.getBody(), expression.getBody(), context);
		return expression;
	}

	@Override
	public IExpression visit(ICompletionExpression entity, InliningContext context) {
		CompletionExpression expression = new CompletionExpression();
		expression.setObjectReference(
				(IVariableReference) entity.getObjectReference().accept(new InliningIReferenceVisitor(), context));
		expression.setToken(entity.getToken());
		expression.setTypeReference(entity.getTypeReference());
		return expression;
	}
}
