package cc.kave.commons.model.ssts.impl.visitor.inlining;

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
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.commons.model.ssts.impl.statements.LabelledStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.impl.visitor.AbstractNodeVisitor;
import cc.kave.commons.model.ssts.references.IAssignableReference;
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
import cc.kave.commons.utils.visitor.InliningContext;
import cc.kave.commons.utils.visitor.InliningUtils;

public class InliningIStatementVisitor extends AbstractNodeVisitor<InliningContext, Void> {

	@Override
	public Void visit(ISST sst, InliningContext context) {
		context.setNonEntryPoints(sst.getNonEntryPoints());
		context.createSST(sst);
		for (IMethodDeclaration method : sst.getEntryPoints()) {
			method.accept(this, context);
		}
		return null;
	}

	public Void visit(IMethodDeclaration stmt, InliningContext context) {
		context.enterScope(stmt.getBody());
		for (IStatement statement : stmt.getBody())
			statement.accept(this, context);
		MethodDeclaration method = new MethodDeclaration();
		method.getBody().addAll(context.getBody());
		method.setEntryPoint(true);
		method.setName(stmt.getName());
		context.addMethod(method);
		context.leaveScope();
		return null;
	}

	public Void visit(IExpressionStatement stmt, InliningContext context) {
		ExpressionStatement expr = new ExpressionStatement();
		IAssignableExpression assExpr = (IAssignableExpression) stmt.getExpression()
				.accept(new InliningIExpressionVisitor(), context);
		if (assExpr != null) {
			expr.setExpression(assExpr);
			context.addStatement(expr);
		}
		return null;
	}

	public Void visit(IVariableDeclaration stmt, InliningContext context) {
		VariableDeclaration var = new VariableDeclaration();
		var.setReference((IVariableReference) stmt.getReference().accept(new InliningIReferenceVisitor(), context));
		context.addStatement(var);
		return null;
	}

	public Void visit(IAssignment stmt, InliningContext context) {
		Assignment assignment = new Assignment();
		assignment.setReference(
				(IAssignableReference) stmt.getReference().accept(new InliningIReferenceVisitor(), context));
		assignment.setExpression(
				(IAssignableExpression) stmt.getExpression().accept(new InliningIExpressionVisitor(), context));
		context.addStatement(assignment);
		return null;
	}

	public Void visit(IReturnStatement stmt, InliningContext context) {
		ReturnStatement returnStmt = new ReturnStatement();
		returnStmt.setExpression(
				(ISimpleExpression) stmt.getExpression().accept(new InliningIExpressionVisitor(), context));
		context.addStatement(returnStmt);
		return null;
	}

	@Override
	public Void visit(IForLoop block, InliningContext context) {
		ForLoop loop = new ForLoop();
		loop.setCondition(
				(ILoopHeaderExpression) block.getCondition().accept(new InliningIExpressionVisitor(), context));
		InliningUtils.visit(block.getInit(), loop.getInit(), context);
		InliningUtils.visit(block.getStep(), loop.getStep(), context);
		InliningUtils.visit(block.getBody(), loop.getBody(), context);
		context.addStatement(loop);
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, InliningContext context) {
		WhileLoop loop = new WhileLoop();
		loop.setCondition(
				(ILoopHeaderExpression) block.getCondition().accept(new InliningIExpressionVisitor(), context));
		InliningUtils.visit(block.getBody(), loop.getBody(), context);
		context.addStatement(loop);
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, InliningContext context) {
		IfElseBlock ifelse = new IfElseBlock();
		ifelse.setCondition((ISimpleExpression) block.getCondition().accept(new InliningIExpressionVisitor(), context));
		InliningUtils.visit(block.getElse(), ifelse.getElse(), context);
		InliningUtils.visit(block.getThen(), ifelse.getThen(), context);
		context.addStatement(ifelse);
		return null;
	}

	@Override
	public Void visit(IDoLoop block, InliningContext context) {
		DoLoop loop = new DoLoop();
		loop.setCondition(
				(ILoopHeaderExpression) block.getCondition().accept(new InliningIExpressionVisitor(), context));
		InliningUtils.visit(block.getBody(), loop.getBody(), context);
		context.addStatement(loop);
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, InliningContext context) {
		ForEachLoop loop = new ForEachLoop();
		loop.setDeclaration((IVariableDeclaration) InliningUtils.visit(block.getDeclaration(), context));
		loop.setLoopedReference(
				(IVariableReference) block.getLoopedReference().accept(new InliningIReferenceVisitor(), context));
		InliningUtils.visit(block.getBody(), loop.getBody(), context);
		context.addStatement(loop);
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, InliningContext context) {
		SwitchBlock switchBlock = new SwitchBlock();
		switchBlock.setReference(
				(IVariableReference) block.getReference().accept(new InliningIReferenceVisitor(), context));
		InliningUtils.visit(block.getDefaultSection(), switchBlock.getDefaultSection(), context);
		for (ICaseBlock caseBlock : block.getSections()) {
			CaseBlock newCase = new CaseBlock();
			InliningUtils.visit(caseBlock.getBody(), newCase.getBody(), context);
			newCase.setLabel(caseBlock.getLabel());
			switchBlock.getSections().add(newCase);
		}
		context.addStatement(switchBlock);
		return null;
	}

	@Override
	public Void visit(ILockBlock stmt, InliningContext context) {
		LockBlock block = new LockBlock();
		block.setReference((IVariableReference) stmt.getReference().accept(new InliningIReferenceVisitor(), context));
		InliningUtils.visit(stmt.getBody(), block.getBody(), context);
		context.addStatement(block);
		return null;
	}

	@Override
	public Void visit(IUsingBlock block, InliningContext context) {
		UsingBlock usingBlock = new UsingBlock();
		usingBlock.setReference(
				(IVariableReference) block.getReference().accept(new InliningIReferenceVisitor(), context));
		InliningUtils.visit(block.getBody(), usingBlock.getBody(), context);
		context.addStatement(usingBlock);
		return null;
	}

	@Override
	public Void visit(ITryBlock block, InliningContext context) {
		TryBlock tryBlock = new TryBlock();
		InliningUtils.visit(block.getBody(), tryBlock.getBody(), context);
		InliningUtils.visit(block.getFinally(), tryBlock.getFinally(), context);
		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			CatchBlock newCatch = new CatchBlock();
			InliningUtils.visit(catchBlock.getBody(), newCatch.getBody(), context);
			newCatch.setGeneral(catchBlock.isGeneral());
			newCatch.setUnnamed(catchBlock.isUnnamed());
			newCatch.setParameter(catchBlock.getParameter());
			tryBlock.getCatchBlocks().add(newCatch);
		}
		context.addStatement(tryBlock);
		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, InliningContext context) {
		UncheckedBlock unchecked = new UncheckedBlock();
		InliningUtils.visit(block.getBody(), unchecked.getBody(), context);
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
		statement.setStatement(InliningUtils.visit(stmt.getStatement(), context));
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
}
