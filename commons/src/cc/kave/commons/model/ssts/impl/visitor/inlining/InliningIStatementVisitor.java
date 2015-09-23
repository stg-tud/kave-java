package cc.kave.commons.model.ssts.impl.visitor.inlining;

import java.util.HashSet;
import java.util.Set;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.csharp.CsMethodName;
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
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.LockBlock;
import cc.kave.commons.model.ssts.impl.blocks.SwitchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.blocks.UncheckedBlock;
import cc.kave.commons.model.ssts.impl.blocks.UnsafeBlock;
import cc.kave.commons.model.ssts.impl.blocks.UsingBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.EventSubscriptionStatement;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.commons.model.ssts.impl.statements.LabelledStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.impl.visitor.AbstractNodeVisitor;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.RecursiveCallVisitor;
import cc.kave.commons.model.ssts.references.IAssignableReference;
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

public class InliningIStatementVisitor extends AbstractNodeVisitor<InliningContext, Void> {

	@Override
	public Void visit(ISST sst, InliningContext context) {
		context.setNonEntryPoints(sst.getNonEntryPoints());
		context.createSST(sst);
		if (sst.getEntryPoints().size() > 0) {
			for (IMethodDeclaration method : sst.getEntryPoints()) {
				method.accept(this, context);
			}
		} else {
			for (IMethodDeclaration method : sst.getMethods()) {
				context.addMethod(method);
			}
		}
		Set<IMethodDeclaration> notInlinedMethods = new HashSet<>();
		Set<MethodName> calls = new HashSet<>();
		for (IMethodDeclaration method : context.getNonEntryPoints()) {
			if (!context.getInlinedMethods().contains(method)) {
				notInlinedMethods.add(method);
				context.addMethod(method);
				Set<MethodName> invokes = new HashSet<>();
				method.accept(new RecursiveCallVisitor(), invokes);
				calls.addAll(invokes);
			}
		}

		for (IMethodDeclaration method : context.getInlinedMethods()) {
			if (calls.contains(method.getName())) {
				context.addMethod(method);
			}
		}

		return null;
	}

	public Void visit(IMethodDeclaration stmt, InliningContext context) {
		MethodDeclaration method = new MethodDeclaration();
		InliningUtil.visitScope(stmt.getBody(), context);
		method.getBody().addAll(context.getBody());
		method.setEntryPoint(true);
		method.setName(CsMethodName.newMethodName(stmt.getName().getIdentifier()));
		context.addMethod(method);

		// Clear body for next Method
		context.resetScope();
		return null;
	}

	public Void visit(IExpressionStatement stmt, InliningContext context) {
		ExpressionStatement expr = new ExpressionStatement();
		IAssignableExpression assExpr = (IAssignableExpression) stmt.getExpression()
				.accept(context.getExpressionVisitor(), context);
		if (assExpr != null) {
			expr.setExpression(assExpr);
			context.addStatement(expr);
		}
		return null;
	}

	public Void visit(IVariableDeclaration stmt, InliningContext context) {
		VariableDeclaration var = new VariableDeclaration();
		var.setReference((IVariableReference) stmt.getReference().accept(context.getReferenceVisitor(), context));
		var.setType(stmt.getType());
		context.addStatement(var);
		return null;
	}

	public Void visit(IAssignment stmt, InliningContext context) {
		Assignment assignment = new Assignment();
		assignment.setReference(
				(IAssignableReference) stmt.getReference().accept(context.getReferenceVisitor(), context));
		assignment.setExpression(
				(IAssignableExpression) stmt.getExpression().accept(context.getExpressionVisitor(), context));
		context.addStatement(assignment);
		return null;
	}

	public Void visit(IReturnStatement stmt, InliningContext context) {
		if (context.isInline()) {
			if (!context.isVoid()) {
				Assignment assignment = new Assignment();
				assignment.setReference(SSTUtil.variableReference(context.getResultName()));
				assignment.setExpression(stmt.getExpression());
				context.addStatement(assignment);
			}
			Assignment resultAssignment = new Assignment();
			resultAssignment.setReference(SSTUtil.variableReference(context.getGotResultName()));
			ConstantValueExpression constant = new ConstantValueExpression();
			constant.setValue("false");
			resultAssignment.setExpression(constant);
			context.addStatement(resultAssignment);
			context.setGuardNeeded(true);
			context.setGlobalGuardNeeded(true);
		} else {
			ReturnStatement statement = new ReturnStatement();
			statement.setIsVoid(stmt.isVoid());
			statement.setExpression(
					(ISimpleExpression) stmt.getExpression().accept(context.getExpressionVisitor(), context));
			context.addStatement(statement);
		}
		return null;
	}

	@Override
	public Void visit(IForLoop block, InliningContext context) {
		ForLoop loop = new ForLoop();
		loop.setCondition((ILoopHeaderExpression) block.getCondition().accept(context.getExpressionVisitor(), context));
		InliningUtil.visitBlock(block.getInit(), loop.getInit(), context);
		InliningUtil.visitBlock(block.getStep(), loop.getStep(), context);
		InliningUtil.visitBlock(block.getBody(), loop.getBody(), context);
		context.addStatement(loop);
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, InliningContext context) {
		WhileLoop loop = new WhileLoop();
		loop.setCondition((ILoopHeaderExpression) block.getCondition().accept(context.getExpressionVisitor(), context));
		InliningUtil.visitBlock(block.getBody(), loop.getBody(), context);
		context.addStatement(loop);
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, InliningContext context) {
		IfElseBlock ifelse = new IfElseBlock();
		ifelse.setCondition((ISimpleExpression) block.getCondition().accept(context.getExpressionVisitor(), context));
		InliningUtil.visitBlock(block.getElse(), ifelse.getElse(), context);
		InliningUtil.visitBlock(block.getThen(), ifelse.getThen(), context);
		context.addStatement(ifelse);
		return null;
	}

	@Override
	public Void visit(IDoLoop block, InliningContext context) {
		DoLoop loop = new DoLoop();
		loop.setCondition((ILoopHeaderExpression) block.getCondition().accept(context.getExpressionVisitor(), context));
		InliningUtil.visitBlock(block.getBody(), loop.getBody(), context);
		context.addStatement(loop);
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, InliningContext context) {
		ForEachLoop loop = new ForEachLoop();
		loop.setDeclaration((IVariableDeclaration) InliningUtil.visit(block.getDeclaration(), context));
		loop.setLoopedReference(
				(IVariableReference) block.getLoopedReference().accept(context.getReferenceVisitor(), context));
		InliningUtil.visitBlock(block.getBody(), loop.getBody(), context);
		context.addStatement(loop);
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, InliningContext context) {
		SwitchBlock switchBlock = new SwitchBlock();
		switchBlock
				.setReference((IVariableReference) block.getReference().accept(context.getReferenceVisitor(), context));
		InliningUtil.visitBlock(block.getDefaultSection(), switchBlock.getDefaultSection(), context);
		for (ICaseBlock caseBlock : block.getSections()) {
			CaseBlock newCase = new CaseBlock();
			InliningUtil.visitBlock(caseBlock.getBody(), newCase.getBody(), context);
			newCase.setLabel(caseBlock.getLabel());
			switchBlock.getSections().add(newCase);
		}
		context.addStatement(switchBlock);
		return null;
	}

	@Override
	public Void visit(ILockBlock stmt, InliningContext context) {
		LockBlock block = new LockBlock();
		block.setReference((IVariableReference) stmt.getReference().accept(context.getReferenceVisitor(), context));
		InliningUtil.visitBlock(stmt.getBody(), block.getBody(), context);
		context.addStatement(block);
		return null;
	}

	@Override
	public Void visit(IUsingBlock block, InliningContext context) {
		UsingBlock usingBlock = new UsingBlock();
		usingBlock
				.setReference((IVariableReference) block.getReference().accept(context.getReferenceVisitor(), context));
		InliningUtil.visitBlock(block.getBody(), usingBlock.getBody(), context);
		context.addStatement(usingBlock);
		return null;
	}

	@Override
	public Void visit(ITryBlock block, InliningContext context) {
		TryBlock tryBlock = new TryBlock();
		context.enterBlock();
		for (IStatement statement : block.getBody()) {
			statement.accept(this, context);
		}
		context.leaveBlock(tryBlock.getBody());
		InliningUtil.visitBlock(block.getFinally(), tryBlock.getFinally(), context);
		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			CatchBlock newCatch = new CatchBlock();
			InliningUtil.visitBlock(catchBlock.getBody(), newCatch.getBody(), context);
			newCatch.setKind(catchBlock.getKind());
			newCatch.setParameter(catchBlock.getParameter());
			tryBlock.getCatchBlocks().add(newCatch);
		}
		context.addStatement(tryBlock);
		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, InliningContext context) {
		UncheckedBlock unchecked = new UncheckedBlock();
		InliningUtil.visitBlock(block.getBody(), unchecked.getBody(), context);
		context.addStatement(unchecked);
		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, InliningContext context) {
		UnsafeBlock unsafe = new UnsafeBlock();
		context.addStatement(unsafe);
		return null;
	}

	@Override
	public Void visit(IContinueStatement stmt, InliningContext context) {
		ContinueStatement statement = new ContinueStatement();
		context.addStatement(statement);
		return null;
	}

	@Override
	public Void visit(IBreakStatement stmt, InliningContext context) {
		BreakStatement statement = new BreakStatement();
		context.addStatement(statement);
		return null;
	}

	@Override
	public Void visit(IUnknownStatement unknownStmt, InliningContext context) {
		UnknownStatement statement = new UnknownStatement();
		context.addStatement(statement);
		return null;
	}

	@Override
	public Void visit(ILabelledStatement stmt, InliningContext context) {
		LabelledStatement statement = new LabelledStatement();
		statement.setLabel(stmt.getLabel());
		statement.setStatement(InliningUtil.visit(stmt.getStatement(), context));
		context.addStatement(statement);
		return null;
	}

	@Override
	public Void visit(IGotoStatement stmt, InliningContext context) {
		GotoStatement statement = new GotoStatement();
		statement.setLabel(stmt.getLabel());
		context.addStatement(statement);
		return null;
	}

	@Override
	public Void visit(IThrowStatement stmt, InliningContext context) {
		ThrowStatement statement = new ThrowStatement();
		statement.setException(stmt.getException());
		context.addStatement(statement);
		return null;
	}

	@Override
	public Void visit(IEventSubscriptionStatement stmt, InliningContext context) {
		EventSubscriptionStatement statement = new EventSubscriptionStatement();
		statement.setExpression(
				(IAssignableExpression) stmt.getExpression().accept(context.getExpressionVisitor(), context));
		statement.setOperation(stmt.getOperation());
		statement.setReference(
				(IAssignableReference) stmt.getReference().accept(context.getReferenceVisitor(), context));
		context.addStatement(statement);
		return null;
	}

}
