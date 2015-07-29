package cc.kave.commons.model.ssts.impl.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.IReference;
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
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
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
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IAssignableReference;
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
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class InliningVisitor extends AbstractNodeVisitor<InliningContext, ISSTNode> {

	NameGrabber nameGrabber = new NameGrabber();

	@Override
	public ISSTNode visit(ISST sst, InliningContext context) {

		return null;
	}

	@Override
	public ISSTNode visit(IMethodDeclaration stmt, InliningContext context) {
		nameGrabber.grabFields(stmt.getBody());
		context.getCurrentBlock().addAll(stmt.getBody());
		for (IStatement statement : stmt.getBody()) {
			IStatement clone = (IStatement) statement.accept(this, context);
			if (clone != null) {
				context.getNewMethodBody().add(clone);
				if (!(clone instanceof IForLoop))
					context.getBefore().add(clone);
				context.getCurrentBlock().remove(0);
				context.setNames(nameGrabber.getNames(context.getBefore(), context.getCurrentBlock()));
			}
		}
		MethodDeclaration method = new MethodDeclaration();
		method.setBody(context.getNewMethodBody());
		method.setEntryPoint(stmt.isEntryPoint());
		method.setName(stmt.getName());
		return method;
	}

	@Override
	public ISSTNode visit(IPropertyDeclaration stmt, InliningContext context) {
		// ?
		return null;
	}

	@Override
	public ISSTNode visit(IAssignment stmt, InliningContext context) {
		Assignment assignment = new Assignment();
		assignment.setExpression((IAssignableExpression) stmt.getExpression().accept(this, context));
		assignment.setReference((IAssignableReference) stmt.getReference().accept(this, context));
		return assignment;
	}

	@Override
	public ISSTNode visit(IExpressionStatement stmt, InliningContext context) {
		ExpressionStatement clone = new ExpressionStatement();
		IAssignableExpression expr = (IAssignableExpression) stmt.getExpression().accept(this, context);
		if (expr == null)
			return null;
		clone.setExpression(expr);
		return clone;
	}

	@Override
	public ISSTNode visit(ILabelledStatement stmt, InliningContext context) {
		stmt.getStatement().accept(this, context);
		return null;
	}

	@Override
	public ISSTNode visit(IReturnStatement stmt, InliningContext context) {
		if (context.isInline())
			return stmt.getExpression().accept(this, context);
		else {
			ReturnStatement clone = new ReturnStatement();
			clone.setExpression((ISimpleExpression) clone.accept(this, context));
			clone.setIsVoid(stmt.isVoid());
			return clone;
		}
	}

	@Override
	public ISSTNode visit(IDoLoop block, InliningContext context) {
		block.getCondition().accept(this, context);
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public ISSTNode visit(IForEachLoop block, InliningContext context) {
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public ISSTNode visit(IForLoop block, InliningContext context) {
		ForLoop clone = new ForLoop();
		List<IStatement> saveNewBody = new ArrayList<>();
		List<IStatement> saveCurrentBlock = new ArrayList<>();
		saveCurrentBlock.addAll(context.getCurrentBlock());
		saveNewBody.addAll(context.getNewMethodBody());
		clone.setCondition((ILoopHeaderBlockExpression) block.getCondition().accept(this, context));
		for (IStatement statement : block.getInit()) {
			clone.getInit().add((IStatement) statement.accept(this, context));
		}
		for (IStatement statement : block.getStep()) {
			clone.getStep().add((IStatement) statement.accept(this, context));
		}
		context.setNewMethodBody(Lists.newArrayList());
		context.setCurrentBlock(Lists.newArrayList());
		context.getCurrentBlock().addAll(block.getBody());
		for (IStatement statement : block.getBody()) {
			IStatement cloneStatement = (IStatement) statement.accept(this, context);
			if (cloneStatement != null) {
				context.getNewMethodBody().add(cloneStatement);
				context.getCurrentBlock().remove(0);
			}
		}
		clone.getBody().addAll(context.getNewMethodBody());
		context.setCurrentBlock(saveCurrentBlock);
		context.setNewMethodBody(saveNewBody);
		return clone;
	}

	@Override
	public ISSTNode visit(IIfElseBlock block, InliningContext context) {
		block.getCondition().accept(this, context);
		for (IStatement statement : block.getThen())
			statement.accept(this, context);
		for (IStatement statement : block.getElse())
			statement.accept(this, context);
		return null;
	}

	@Override
	public ISSTNode visit(ILockBlock stmt, InliningContext context) {
		for (IStatement statement : stmt.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public ISSTNode visit(ISwitchBlock block, InliningContext context) {
		for (ICaseBlock caseBlock : block.getSections())
			for (IStatement statement : caseBlock.getBody())
				statement.accept(this, context);
		for (IStatement statement : block.getDefaultSection())
			statement.accept(this, context);
		return null;
	}

	@Override
	public ISSTNode visit(ITryBlock block, InliningContext context) {
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
	public ISSTNode visit(IUncheckedBlock block, InliningContext context) {
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public ISSTNode visit(IUsingBlock block, InliningContext context) {
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public ISSTNode visit(IWhileLoop block, InliningContext context) {
		block.getCondition().accept(this, context);
		for (IStatement statement : block.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public ISSTNode visit(ICompletionExpression entity, InliningContext context) {
		return null;
	}

	@Override
	public ISSTNode visit(IComposedExpression expr, InliningContext context) {
		ComposedExpression clone = new ComposedExpression();
		for (IVariableReference ref : expr.getReferences())
			clone.getReferences().add((IVariableReference) ref.accept(this, context));
		return clone;
	}

	@Override
	public ISSTNode visit(IIfElseExpression expr, InliningContext context) {
		expr.getCondition().accept(this, context);
		expr.getElseExpression().accept(this, context);
		expr.getThenExpression().accept(this, context);
		return null;
	}

	@Override
	public ISSTNode visit(IInvocationExpression entity, InliningContext context) {
		//
		IMethodDeclaration method = context.getNonEntryPoint(entity.getMethodName());
		if (method == null) {
			InvocationExpression clone = new InvocationExpression();
			clone.setMethodName(entity.getMethodName());
			clone.setParameters(entity.getParameters());
			clone.setReference(entity.getReference());
			return clone;
		}
		context.setInline(true);
		Set<IVariableReference> inlineNames = nameGrabber.getNames(Lists.newArrayList(), method.getBody());
		for (IVariableReference ref : inlineNames) {
			if (context.getNames().contains(ref)) {
				IVariableReference newName = getNewRef(ref);
				context.getChangedNames().put(ref, newName);
				context.getNames().add(newName);
			} else
				context.getNames().add(ref);
		}
		for (IStatement statement : method.getBody()) {
			if (statement instanceof IReturnStatement) {
				IReturnStatement returnState = (IReturnStatement) statement;
				if (!returnState.isVoid()) {
					ISSTNode node = statement.accept(this, context);
					context.setInline(false);
					return node;
				}
			}
			IStatement clone = (IStatement) statement.accept(this, context);
			context.getNewMethodBody().add(clone);
			context.getBefore().add(clone);
			context.setNames(nameGrabber.getNames(context.getBefore(), context.getCurrentBlock()));
		}
		context.setInline(false);
		return null;
	}

	@Override
	public ISSTNode visit(ILambdaExpression expr, InliningContext context) {
		for (IStatement statement : expr.getBody())
			statement.accept(this, context);
		return null;
	}

	@Override
	public ISSTNode visit(ILoopHeaderBlockExpression expr, InliningContext context) {
		LoopHeaderBlockExpression clone = new LoopHeaderBlockExpression();
		for (IStatement statement : expr.getBody())
			clone.getBody().add((IStatement) statement.accept(this, context));
		return clone;
	}

	@Override
	public ISSTNode visit(IUnsafeBlock block, InliningContext context) {
		return null;
	}

	@Override
	public ISSTNode visit(IConstantValueExpression expr, InliningContext context) {
		ConstantValueExpression clone = new ConstantValueExpression();
		clone.setValue(expr.getValue());
		return clone;
	}

	@Override
	public ISSTNode visit(INullExpression expr, InliningContext context) {
		return null;
	}

	@Override
	public ISSTNode visit(IReferenceExpression expr, InliningContext context) {
		ReferenceExpression clone = new ReferenceExpression();
		clone.setReference((IReference) expr.getReference().accept(this, context));
		return clone;
	}

	@Override
	public ISSTNode visit(IEventReference eventRef, InliningContext context) {
		return null;
	}

	@Override
	public ISSTNode visit(IFieldReference fieldRef, InliningContext context) {
		return null;
	}

	@Override
	public ISSTNode visit(IMethodReference methodRef, InliningContext context) {
		return null;
	}

	@Override
	public ISSTNode visit(IPropertyReference methodRef, InliningContext context) {
		return null;
	}

	@Override
	public ISSTNode visit(IVariableReference varRef, InliningContext context) {
		VariableReference clone = new VariableReference();
		clone.setIdentifier(varRef.getIdentifier());
		if (context.isInline() && context.getChangedNames().containsKey(varRef)) {
			clone.setIdentifier(context.getChangedNames().get(varRef).getIdentifier());
		}
		return clone;
	}

	@Override
	public ISSTNode visit(IUnknownReference unknownRef, InliningContext context) {
		return null;
	}

	@Override
	public ISSTNode visit(IUnknownExpression unknownExpr, InliningContext context) {
		return null;
	}

	@Override
	public ISSTNode visit(IUnknownStatement unknownStmt, InliningContext context) {
		return null;
	}

	@Override
	public ISSTNode visit(IVariableDeclaration stmt, InliningContext context) {
		VariableDeclaration newVar = new VariableDeclaration();
		newVar.setReference((IVariableReference) stmt.getReference().accept(this, context));
		newVar.setType(stmt.getType());
		return newVar;
	}

	private IVariableReference getNewRef(IVariableReference reference) {
		VariableReference ref = new VariableReference();
		ref.setIdentifier("$" + nameGrabber.getCounter() + "_" + reference.getIdentifier());
		return ref;
	}

	@Override
	public ISSTNode visit(IGotoStatement stmt, InliningContext context) {
		return null;
	}

	@Override
	public ISSTNode visit(IThrowStatement stmt, InliningContext context) {
		return null;
	}

	@Override
	public ISSTNode visit(IBreakStatement stmt, InliningContext context) {
		return null;
	}

	@Override
	public ISSTNode visit(IContinueStatement stmt, InliningContext context) {
		return null;
	}

	@Override
	public ISSTNode visit(IDelegateDeclaration stmt, InliningContext context) {
		return null;
	}

	@Override
	public ISSTNode visit(IEventDeclaration stmt, InliningContext context) {
		return null;
	}

	@Override
	public ISSTNode visit(IFieldDeclaration stmt, InliningContext context) {
		return null;
	}

}
