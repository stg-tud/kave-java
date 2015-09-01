package cc.kave.commons.model.ssts.impl.visitor.inlining.util;

import java.util.List;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
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
import cc.kave.commons.model.ssts.impl.visitor.AbstractNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;

public class CountReturnsVisitor extends AbstractNodeVisitor<CountReturnContext, Void> {

	@Override
	public Void visit(ISST sst, CountReturnContext context) {
		for (IMethodDeclaration method : sst.getMethods()) {
			method.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(IMethodDeclaration stmt, CountReturnContext context) {
		visit(stmt.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IVariableDeclaration stmt, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IAssignment stmt, CountReturnContext context) {
		stmt.getExpression().accept(this, context);
		stmt.getReference().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IBreakStatement stmt, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IContinueStatement stmt, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IExpressionStatement stmt, CountReturnContext context) {
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IGotoStatement stmt, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(ILabelledStatement stmt, CountReturnContext context) {
		stmt.getStatement().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IReturnStatement stmt, CountReturnContext context) {
		stmt.getExpression().accept(this, context);
		context.returnCount++;
		context.isVoid = stmt.isVoid();
		return null;
	}

	@Override
	public Void visit(IThrowStatement stmt, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IDoLoop block, CountReturnContext context) {
		block.getCondition().accept(this, context);
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, CountReturnContext context) {
		visit(block.getBody(), context);
		block.getDeclaration().accept(this, context);
		block.getLoopedReference().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IForLoop block, CountReturnContext context) {
		block.getCondition().accept(this, context);
		visit(block.getInit(), context);
		visit(block.getBody(), context);
		visit(block.getStep(), context);
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, CountReturnContext context) {
		block.getCondition().accept(this, context);
		visit(block.getElse(), context);
		visit(block.getThen(), context);
		return null;
	}

	@Override
	public Void visit(ILockBlock stmt, CountReturnContext context) {
		visit(stmt.getBody(), context);
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, CountReturnContext context) {
		visit(block.getDefaultSection(), context);
		block.getReference().accept(this, context);
		for (ICaseBlock caseBlock : block.getSections())
			visit(caseBlock.getBody(), context);
		return null;
	}

	@Override
	public Void visit(ITryBlock block, CountReturnContext context) {
		visit(block.getBody(), context);
		visit(block.getFinally(), context);
		for (ICatchBlock catchBlock : block.getCatchBlocks())
			visit(catchBlock.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, CountReturnContext context) {
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IUsingBlock block, CountReturnContext context) {
		visit(block.getBody(), context);
		block.getReference().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, CountReturnContext context) {
		visit(block.getBody(), context);
		block.getCondition().accept(this, context);
		return null;
	}

	@Override
	public Void visit(ICompletionExpression entity, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IComposedExpression expr, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IIfElseExpression expr, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IInvocationExpression entity, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, CountReturnContext context) {
		visit(expr.getBody(), context);
		return null;
	}

	@Override
	public Void visit(ILoopHeaderBlockExpression expr, CountReturnContext context) {
		visit(expr.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IConstantValueExpression expr, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(INullExpression expr, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IReferenceExpression expr, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IEventReference eventRef, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IFieldReference fieldRef, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IMethodReference methodRef, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IPropertyReference methodRef, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IVariableReference varRef, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IUnknownReference unknownRef, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IUnknownExpression unknownExpr, CountReturnContext context) {
		return null;
	}

	@Override
	public Void visit(IUnknownStatement unknownStmt, CountReturnContext context) {
		return null;
	}

	public void visit(List<IStatement> body, CountReturnContext context) {
		for (IStatement statement : body) {
			statement.accept(this, context);
		}
	}

	public Void visit(IEventSubscriptionStatement stmt, CountReturnContext context) {
		stmt.getExpression().accept(this, context);
		return null;
	}

}