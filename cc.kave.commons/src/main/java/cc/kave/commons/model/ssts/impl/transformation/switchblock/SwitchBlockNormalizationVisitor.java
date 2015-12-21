package cc.kave.commons.model.ssts.impl.transformation.switchblock;

import java.util.List;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
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
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
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

public class SwitchBlockNormalizationVisitor extends AbstractThrowingNodeVisitor<Void, List<IStatement>> {

	@Override
	public List<IStatement> visit(ISST sst, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IDelegateDeclaration stmt, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IEventDeclaration stmt, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IFieldDeclaration stmt, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IMethodDeclaration stmt, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IPropertyDeclaration stmt, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IVariableDeclaration stmt, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IAssignment stmt, Void context) {
		return null;
	}
	
	@Override
	public List<IStatement> visit(IBreakStatement stmt, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IContinueStatement stmt, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IExpressionStatement stmt, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IGotoStatement stmt, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(ILabelledStatement stmt, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IReturnStatement stmt, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IThrowStatement stmt, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IDoLoop block, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IForEachLoop block, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IForLoop block, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IIfElseBlock block, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(ILockBlock stmt, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(ISwitchBlock block, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(ITryBlock block, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IUncheckedBlock block, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IUnsafeBlock block, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IUsingBlock block, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IWhileLoop block, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(ICompletionExpression entity, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IComposedExpression expr, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IIfElseExpression expr, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IInvocationExpression entity, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(ILambdaExpression expr, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(ILoopHeaderBlockExpression expr, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IConstantValueExpression expr, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(INullExpression expr, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IReferenceExpression expr, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IEventReference eventRef, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IFieldReference fieldRef, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IMethodReference methodRef, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IPropertyReference methodRef, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IVariableReference varRef, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IUnknownReference unknownRef, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IUnknownExpression unknownExpr, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IUnknownStatement unknownStmt, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IEventSubscriptionStatement stmt, Void context) {
		return null;
	}

	public List<IStatement> visit(ICastExpression expr, Void context) {
		return null;
	}

	public List<IStatement> visit(IIndexAccessExpression expr, Void context) {
		return null;
	}

	public List<IStatement> visit(ITypeCheckExpression expr, Void context) {
		return null;
	}

	public List<IStatement> visit(IIndexAccessReference indexAccessRef, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IBinaryExpression indexAccessRef, Void context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IUnaryExpression indexAccessRef, Void context) {
		return null;
	}
}