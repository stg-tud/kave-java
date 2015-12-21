package cc.kave.commons.model.ssts.impl.visitor;

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
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

/**
 * extend this base class if you would like to reuse the traversing logic
 */
public class AbstractTraversingNodeVisitor<TContext, TReturn> implements ISSTNodeVisitor<TContext, TReturn> {

	@Override
	public TReturn visit(ISST sst, TContext context) {
		for (IDelegateDeclaration decl : sst.getDelegates()) {
			decl.accept(this, context);
		}
		for (IEventDeclaration decl : sst.getEvents()) {
			decl.accept(this, context);
		}
		for (IFieldDeclaration decl : sst.getFields()) {
			decl.accept(this, context);
		}
		for (IMethodDeclaration decl : sst.getMethods()) {
			decl.accept(this, context);
		}
		for (IPropertyDeclaration decl : sst.getProperties()) {
			decl.accept(this, context);
		}
		return null;
	}

	@Override
	public TReturn visit(IDelegateDeclaration stmt, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IEventDeclaration stmt, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IFieldDeclaration stmt, TContext context) {
		return null;
	}

	@Override
	public TReturn visit(IMethodDeclaration decl, TContext context) {
		visit(decl.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(IPropertyDeclaration decl, TContext context) {
		visit(decl.getGet(), context);
		visit(decl.getSet(), context);
		return null;
	}

	@Override
	public TReturn visit(IVariableDeclaration stmt, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IAssignment stmt, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IBreakStatement stmt, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IContinueStatement stmt, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IExpressionStatement stmt, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IGotoStatement stmt, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(ILabelledStatement stmt, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IReturnStatement stmt, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IThrowStatement stmt, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IEventSubscriptionStatement stmt, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IDoLoop block, TContext context) {
		block.getCondition().accept(this, context);
		visit(block.getBody(), context);
		return null;
	}

	private void visit(List<IStatement> body, TContext context) {
		for (IStatement stmt : body) {
			stmt.accept(this, context);
		}
	}

	@Override
	public TReturn visit(IForEachLoop block, TContext context) {
		block.getDeclaration().accept(this, context);
		block.getLoopedReference().accept(this, context);
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(IForLoop block, TContext context) {
		visit(block.getInit(), context);
		block.getCondition().accept(this, context);
		visit(block.getStep(), context);
		visit(block.getBody(), context);
		return null;
	}

	@Override
	public TReturn visit(IIfElseBlock block, TContext context) {
		block.getCondition().accept(this, context);
		visit(block.getThen(), context);
		visit(block.getElse(), context);
		return null;
	}

	@Override
	public TReturn visit(ILockBlock stmt, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(ISwitchBlock block, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(ITryBlock block, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IUncheckedBlock block, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IUnsafeBlock block, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IUsingBlock block, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IWhileLoop block, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(ICompletionExpression entity, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IComposedExpression expr, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IIfElseExpression expr, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IInvocationExpression entity, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(ILambdaExpression expr, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(ILoopHeaderBlockExpression expr, TContext context) {
		throw new RuntimeException("not implemented yet!");

	}

	@Override
	public TReturn visit(IConstantValueExpression expr, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public TReturn visit(INullExpression expr, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public TReturn visit(IReferenceExpression expr, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public TReturn visit(ICastExpression expr, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public TReturn visit(IIndexAccessExpression expr, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public TReturn visit(ITypeCheckExpression expr, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public TReturn visit(IBinaryExpression indexAccessRef, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public TReturn visit(IUnaryExpression indexAccessRef, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public TReturn visit(IEventReference eventRef, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public TReturn visit(IFieldReference fieldRef, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public TReturn visit(IMethodReference methodRef, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public TReturn visit(IPropertyReference methodRef, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public TReturn visit(IVariableReference varRef, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public TReturn visit(IIndexAccessReference indexAccessRef, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public TReturn visit(IUnknownReference unknownRef, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public TReturn visit(IUnknownExpression unknownExpr, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

	@Override
	public TReturn visit(IUnknownStatement unknownStmt, TContext context) {
		throw new RuntimeException("not implemented yet!");
	}

}