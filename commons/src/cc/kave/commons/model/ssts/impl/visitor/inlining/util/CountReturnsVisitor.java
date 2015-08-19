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
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;

public class CountReturnsVisitor extends AbstractNodeVisitor<Void, Integer> {

	private Integer counter = new Integer(0);

	@Override
	public Integer visit(ISST sst, Void context) {
		for (IMethodDeclaration method : sst.getMethods()) {
			method.accept(this, context);
		}
		return counter;
	}

	@Override
	public Integer visit(IMethodDeclaration stmt, Void context) {
		visit(stmt.getBody());
		return counter;
	}

	@Override
	public Integer visit(IVariableDeclaration stmt, Void context) {
		return null;
	}

	@Override
	public Integer visit(IAssignment stmt, Void context) {
		stmt.getExpression().accept(this, context);
		stmt.getReference().accept(this, context);
		return null;
	}

	@Override
	public Integer visit(IBreakStatement stmt, Void context) {
		return null;
	}

	@Override
	public Integer visit(IContinueStatement stmt, Void context) {
		return null;
	}

	@Override
	public Integer visit(IExpressionStatement stmt, Void context) {
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public Integer visit(IGotoStatement stmt, Void context) {
		return null;
	}

	@Override
	public Integer visit(ILabelledStatement stmt, Void context) {
		stmt.getStatement().accept(this, context);
		return null;
	}

	@Override
	public Integer visit(IReturnStatement stmt, Void context) {
		stmt.getExpression().accept(this, context);
		counter++;
		return null;
	}

	@Override
	public Integer visit(IThrowStatement stmt, Void context) {
		return null;
	}

	@Override
	public Integer visit(IDoLoop block, Void context) {
		block.getCondition().accept(this, context);
		visit(block.getBody());
		return null;
	}

	@Override
	public Integer visit(IForEachLoop block, Void context) {
		visit(block.getBody());
		block.getDeclaration().accept(this, context);
		block.getLoopedReference().accept(this, context);
		return null;
	}

	@Override
	public Integer visit(IForLoop block, Void context) {
		block.getCondition().accept(this, context);
		visit(block.getInit());
		visit(block.getBody());
		visit(block.getStep());
		return null;
	}

	@Override
	public Integer visit(IIfElseBlock block, Void context) {
		block.getCondition().accept(this, context);
		visit(block.getElse());
		visit(block.getThen());
		return null;
	}

	@Override
	public Integer visit(ILockBlock stmt, Void context) {
		visit(stmt.getBody());
		return null;
	}

	@Override
	public Integer visit(ISwitchBlock block, Void context) {
		visit(block.getDefaultSection());
		block.getReference().accept(this, context);
		for (ICaseBlock caseBlock : block.getSections())
			visit(caseBlock.getBody());
		return null;
	}

	@Override
	public Integer visit(ITryBlock block, Void context) {
		visit(block.getBody());
		visit(block.getFinally());
		for (ICatchBlock catchBlock : block.getCatchBlocks())
			visit(catchBlock.getBody());
		return null;
	}

	@Override
	public Integer visit(IUncheckedBlock block, Void context) {
		visit(block.getBody());
		return null;
	}

	@Override
	public Integer visit(IUnsafeBlock block, Void context) {
		return null;
	}

	@Override
	public Integer visit(IUsingBlock block, Void context) {
		visit(block.getBody());
		block.getReference().accept(this, context);
		return null;
	}

	@Override
	public Integer visit(IWhileLoop block, Void context) {
		visit(block.getBody());
		block.getCondition().accept(this, context);
		return null;
	}

	@Override
	public Integer visit(ICompletionExpression entity, Void context) {
		return null;
	}

	@Override
	public Integer visit(IComposedExpression expr, Void context) {
		return null;
	}

	@Override
	public Integer visit(IIfElseExpression expr, Void context) {
		return null;
	}

	@Override
	public Integer visit(IInvocationExpression entity, Void context) {
		return null;
	}

	@Override
	public Integer visit(ILambdaExpression expr, Void context) {
		visit(expr.getBody());
		return null;
	}

	@Override
	public Integer visit(ILoopHeaderBlockExpression expr, Void context) {
		visit(expr.getBody());
		return null;
	}

	@Override
	public Integer visit(IConstantValueExpression expr, Void context) {
		return null;
	}

	@Override
	public Integer visit(INullExpression expr, Void context) {
		return null;
	}

	@Override
	public Integer visit(IReferenceExpression expr, Void context) {
		return null;
	}

	@Override
	public Integer visit(IEventReference eventRef, Void context) {
		return null;
	}

	@Override
	public Integer visit(IFieldReference fieldRef, Void context) {
		return null;
	}

	@Override
	public Integer visit(IMethodReference methodRef, Void context) {
		return null;
	}

	@Override
	public Integer visit(IPropertyReference methodRef, Void context) {
		return null;
	}

	@Override
	public Integer visit(IVariableReference varRef, Void context) {
		return null;
	}

	@Override
	public Integer visit(IUnknownReference unknownRef, Void context) {
		return null;
	}

	@Override
	public Integer visit(IUnknownExpression unknownExpr, Void context) {
		return null;
	}

	@Override
	public Integer visit(IUnknownStatement unknownStmt, Void context) {
		return null;
	}

	public void visit(List<IStatement> body) {
		for (IStatement statement : body) {
			statement.accept(this, null);
		}
	}

}
