package cc.kave.commons.model.ssts.impl.visitor;

import cc.kave.commons.model.ssts.ISST;
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
		stmt.getBody().stream().forEach(s -> s.accept(this, context));
		return counter;
	}

	@Override
	public Integer visit(IVariableDeclaration stmt, Void context) {
		stmt.getReference().accept(this, context);
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
		block.getBody().stream().forEach(s -> s.accept(this, context));
		return null;
	}

	@Override
	public Integer visit(IForEachLoop block, Void context) {
		block.getBody().stream().forEach(s -> s.accept(this, context));
		block.getDeclaration().accept(this, context);
		block.getLoopedReference().accept(this, context);
		return null;
	}

	@Override
	public Integer visit(IForLoop block, Void context) {
		block.getCondition().accept(this, context);
		block.getInit().stream().forEach(s -> s.accept(this, context));
		block.getBody().stream().forEach(s -> s.accept(this, context));
		block.getStep().stream().forEach(s -> s.accept(this, context));
		return null;
	}

	@Override
	public Integer visit(IIfElseBlock block, Void context) {
		block.getCondition().accept(this, context);
		block.getElse().stream().forEach(s -> s.accept(this, context));
		block.getThen().stream().forEach(s -> s.accept(this, context));
		return null;
	}

	@Override
	public Integer visit(ILockBlock stmt, Void context) {
		stmt.getBody().stream().forEach(s -> s.accept(this, context));
		return null;
	}

	@Override
	public Integer visit(ISwitchBlock block, Void context) {
		block.getDefaultSection().stream().forEach(s -> s.accept(this, context));
		block.getReference().accept(this, context);
		block.getSections().stream().forEach(s -> s.getBody().stream().forEach(f -> f.accept(this, context)));
		block.getSections().stream().forEach(s -> s.getLabel().accept(this, context));
		return null;
	}

	@Override
	public Integer visit(ITryBlock block, Void context) {
		block.getFinally().stream().forEach(s -> s.accept(this, context));
		block.getBody().stream().forEach(s -> s.accept(this, context));
		block.getCatchBlocks().stream().forEach(c -> c.getBody().stream().forEach(s -> s.accept(this, context)));
		return null;
	}

	@Override
	public Integer visit(IUncheckedBlock block, Void context) {
		block.getBody().stream().forEach(s -> s.accept(this, context));
		return null;
	}

	@Override
	public Integer visit(IUnsafeBlock block, Void context) {
		return null;
	}

	@Override
	public Integer visit(IUsingBlock block, Void context) {
		block.getBody().stream().forEach(s -> s.accept(this, context));
		block.getReference().accept(this, context);
		return null;
	}

	@Override
	public Integer visit(IWhileLoop block, Void context) {
		block.getBody().stream().forEach(s -> s.accept(this, context));
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
		expr.getBody().stream().forEach(s -> s.accept(this, context));
		return null;
	}

	@Override
	public Integer visit(ILoopHeaderBlockExpression expr, Void context) {
		expr.getBody().stream().forEach(s -> s.accept(this, context));
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

}
