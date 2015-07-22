package cc.kave.commons.model.ssts.impl.visitor;

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
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
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

public class InliningVisitor extends AbstractNodeVisitor<Void, Void> {

	@Override
	public Void visit(ISST sst, Void context) {
		for (IMethodDeclaration method : sst.getEntryPoints()) {
			method.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(IMethodDeclaration stmt, Void context) {
		for (IStatement statement : stmt.getBody()) {
			statement.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(IPropertyDeclaration stmt, Void context) {
		// ?
		return null;
	}

	@Override
	public Void visit(IAssignment stmt, Void context) {
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IExpressionStatement stmt, Void context) {
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(ILabelledStatement stmt, Void context) {
		stmt.getStatement().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IReturnStatement stmt, Void context) {
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IDoLoop block, Void context) {
		block.getCondition().accept(this, context);
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, Void context) {
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(IForLoop block, Void context) {
		block.getCondition().accept(this, context);
		for (IStatement statement : block.getInit())
			statement.accept(this, context);
		for (IStatement statement : block.getStep())
			statement.accept(this, context);
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, Void context) {
		block.getCondition().accept(this, context);
		for (IStatement statement : block.getThen())
			statement.accept(this, context);
		for (IStatement statement : block.getElse())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(ILockBlock stmt, Void context) {
		for (IStatement statement : stmt.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, Void context) {
		for (ICaseBlock caseBlock : block.getSections())
			for (IStatement statement : caseBlock.getBody())
				statement.accept(this, context);
		for (IStatement statement : block.getDefaultSection())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(ITryBlock block, Void context) {
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		for (ICatchBlock catchBlock : block.getCatchBlocks())
			for (IStatement statement : catchBlock.getBody())
				statement.accept(this, context);
		for (IStatement statement : block.getFinally())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, Void context) {
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(IUsingBlock block, Void context) {
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, Void context) {
		block.getCondition().accept(this, context);
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(ICompletionExpression entity, Void context) {
		return null;
	}

	@Override
	public Void visit(IComposedExpression expr, Void context) {
		return null;
	}

	@Override
	public Void visit(IIfElseExpression expr, Void context) {
		expr.getCondition().accept(this, context);
		expr.getElseExpression().accept(this, context);
		expr.getThenExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IInvocationExpression entity, Void context) {
		//

		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, Void context) {
		for (IStatement statement : expr.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(ILoopHeaderBlockExpression expr, Void context) {
		for (IStatement statement : expr.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, Void context) {
		return null;
	}

	@Override
	public Void visit(IConstantValueExpression expr, Void context) {
		return null;
	}

	@Override
	public Void visit(INullExpression expr, Void context) {
		return null;
	}

	@Override
	public Void visit(IReferenceExpression expr, Void context) {
		return null;
	}

	@Override
	public Void visit(IEventReference eventRef, Void context) {
		return null;
	}

	@Override
	public Void visit(IFieldReference fieldRef, Void context) {
		return null;
	}

	@Override
	public Void visit(IMethodReference methodRef, Void context) {
		return null;
	}

	@Override
	public Void visit(IPropertyReference methodRef, Void context) {
		return null;
	}

	@Override
	public Void visit(IVariableReference varRef, Void context) {
		return null;
	}

	@Override
	public Void visit(IUnknownReference unknownRef, Void context) {
		return null;
	}

	@Override
	public Void visit(IUnknownExpression unknownExpr, Void context) {
		return null;
	}

	@Override
	public Void visit(IUnknownStatement unknownStmt, Void context) {
		return null;
	}

	@Override
	public Void visit(IVariableDeclaration stmt, Void context) {
		return null;
	}

	@Override
	public Void visit(IGotoStatement stmt, Void context) {
		return null;
	}

	@Override
	public Void visit(IThrowStatement stmt, Void context) {
		return null;
	}

	@Override
	public Void visit(IBreakStatement stmt, Void context) {
		return null;
	}

	@Override
	public Void visit(IContinueStatement stmt, Void context) {
		return null;
	}

	@Override
	public Void visit(IDelegateDeclaration stmt, Void context) {
		return null;
	}

	@Override
	public Void visit(IEventDeclaration stmt, Void context) {
		return null;
	}

	@Override
	public Void visit(IFieldDeclaration stmt, Void context) {
		return null;
	}

}
