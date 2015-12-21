package cc.kave.commons.model.ssts.impl.visitor.inlining.util;

import java.util.List;
import java.util.Set;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
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
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
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

public class InvocationMethodNameVisitor extends AbstractThrowingNodeVisitor<Set<IMethodName>, Void> {

	@Override
	public Void visit(IMethodDeclaration stmt, Set<IMethodName> context) {
		visit(stmt.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IVariableDeclaration stmt, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IAssignment stmt, Set<IMethodName> context) {
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IBreakStatement stmt, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IContinueStatement stmt, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IExpressionStatement stmt, Set<IMethodName> context) {
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IGotoStatement stmt, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(ILabelledStatement stmt, Set<IMethodName> context) {
		stmt.getStatement().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IReturnStatement stmt, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IThrowStatement stmt, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IDoLoop block, Set<IMethodName> context) {
		visit(block.getBody(), context);
		block.getCondition().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, Set<IMethodName> context) {
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IForLoop block, Set<IMethodName> context) {
		visit(block.getBody(), context);
		visit(block.getInit(), context);
		visit(block.getStep(), context);
		block.getCondition().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, Set<IMethodName> context) {
		visit(block.getElse(), context);
		visit(block.getThen(), context);
		return null;
	}

	@Override
	public Void visit(ILockBlock stmt, Set<IMethodName> context) {
		visit(stmt.getBody(), context);
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, Set<IMethodName> context) {
		visit(block.getDefaultSection(), context);
		for (ICaseBlock caseBlock : block.getSections())
			visit(caseBlock.getBody(), context);
		return null;
	}

	@Override
	public Void visit(ITryBlock block, Set<IMethodName> context) {
		visit(block.getBody(), context);
		visit(block.getFinally(), context);
		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			visit(catchBlock.getBody(), context);
		}
		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, Set<IMethodName> context) {
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IUsingBlock block, Set<IMethodName> context) {
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, Set<IMethodName> context) {
		visit(block.getBody(), context);
		block.getCondition().accept(this, context);
		return null;
	}

	@Override
	public Void visit(ICompletionExpression entity, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IComposedExpression expr, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IIfElseExpression expr, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IInvocationExpression entity, Set<IMethodName> context) {
		context.add(entity.getMethodName());
		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, Set<IMethodName> context) {
		visit(expr.getBody(), context);
		return null;
	}

	@Override
	public Void visit(ILoopHeaderBlockExpression expr, Set<IMethodName> context) {
		visit(expr.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IConstantValueExpression expr, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(INullExpression expr, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IReferenceExpression expr, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IEventReference eventRef, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IFieldReference fieldRef, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IMethodReference methodRef, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IPropertyReference methodRef, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IVariableReference varRef, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IUnknownReference unknownRef, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IUnknownExpression unknownExpr, Set<IMethodName> context) {
		return null;
	}

	@Override
	public Void visit(IUnknownStatement unknownStmt, Set<IMethodName> context) {
		return null;
	}

	public void visit(List<IStatement> body, Set<IMethodName> context) {
		for (IStatement statement : body) {
			statement.accept(this, context);
		}
	}

	public Void visit(IEventSubscriptionStatement stmt, Set<IMethodName> context) {
		return null;
	}
}
