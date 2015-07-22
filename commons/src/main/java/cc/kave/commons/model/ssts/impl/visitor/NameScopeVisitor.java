package cc.kave.commons.model.ssts.impl.visitor;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

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

public class NameScopeVisitor extends AbstractNodeVisitor<ScopingContext, Set<IVariableReference>> {

	@Override
	public Set<IVariableReference> visit(ISST sst, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IDelegateDeclaration stmt, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IEventDeclaration stmt, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IFieldDeclaration stmt, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IMethodDeclaration stmt, ScopingContext context) {
		Set<IVariableReference> names = new HashSet<>();
		if (context.getStatement() == null)
			context.setAfterStatement(true);
		else
			context.setAfterStatement(false);
		for (IStatement statement : stmt.getBody()) {
			if (statement.equals(context.getStatement()))
				context.setAfterStatement(true);
			names.addAll(statement.accept(this, context));
		}
		return names;
	}

	@Override
	public Set<IVariableReference> visit(IPropertyDeclaration stmt, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IVariableDeclaration stmt, ScopingContext context) {
		return Sets.newHashSet(stmt.getReference().accept(this, context));
	}

	@Override
	public Set<IVariableReference> visit(IAssignment stmt, ScopingContext context) {
		Set<IVariableReference> names = new HashSet<>();
		names.addAll(stmt.getReference().accept(this, context));
		names.addAll(stmt.getExpression().accept(this, context));
		return names;
	}

	@Override
	public Set<IVariableReference> visit(IBreakStatement stmt, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IContinueStatement stmt, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IExpressionStatement stmt, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IGotoStatement stmt, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(ILabelledStatement stmt, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IReturnStatement stmt, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IThrowStatement stmt, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IDoLoop block, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IForEachLoop block, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IForLoop block, ScopingContext context) {
		Set<IVariableReference> names = new HashSet<>();
		boolean tmp = context.isInBlock();
		if (context.getStatement() != null && context.getStatement().equals(block))
			context.setInBlock(true);
		else
			context.setInBlock(false);
		for (IStatement statement : block.getBody())
			names.addAll(statement.accept(this, context));

		if (context.isAfterStatement() || context.isInBlock()) {
			context.setInBlock(true);
			for (IStatement statement : block.getInit())
				names.addAll(statement.accept(this, context));
			names.addAll(block.getCondition().accept(this, context));
			for (IStatement statement : block.getStep())
				names.addAll(statement.accept(this, context));
		}
		context.setInBlock(tmp);

		return names;
	}

	@Override
	public Set<IVariableReference> visit(IIfElseBlock block, ScopingContext context) {
		Set<IVariableReference> names = new HashSet<>();
		names.addAll(block.getCondition().accept(this, context));
		for (IStatement statement : block.getThen())
			names.addAll(statement.accept(this, context));
		for (IStatement statement : block.getElse())
			names.addAll(statement.accept(this, context));
		return names;
	}

	@Override
	public Set<IVariableReference> visit(ILockBlock stmt, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(ISwitchBlock block, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(ITryBlock block, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IUncheckedBlock block, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IUnsafeBlock block, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IUsingBlock block, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IWhileLoop block, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(ICompletionExpression entity, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IComposedExpression expr, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IIfElseExpression expr, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IInvocationExpression entity, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(ILambdaExpression expr, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(ILoopHeaderBlockExpression expr, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IConstantValueExpression expr, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(INullExpression expr, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IReferenceExpression expr, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IEventReference eventRef, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IFieldReference fieldRef, ScopingContext context) {
		boolean tmp = context.isInBlock();
		context.setInBlock(true);
		Set<IVariableReference> names = new HashSet<>();
		names.addAll(fieldRef.getReference().accept(this, context));
		context.setInBlock(tmp);
		return names;
	}

	@Override
	public Set<IVariableReference> visit(IMethodReference methodRef, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IPropertyReference methodRef, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IVariableReference varRef, ScopingContext context) {
		if (context.isInBlock())
			return Sets.newHashSet(varRef);
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IUnknownReference unknownRef, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IUnknownExpression unknownExpr, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

	@Override
	public Set<IVariableReference> visit(IUnknownStatement unknownStmt, ScopingContext context) {
		// TODO Auto-generated method stub
		return Sets.newHashSet();
	}

}
