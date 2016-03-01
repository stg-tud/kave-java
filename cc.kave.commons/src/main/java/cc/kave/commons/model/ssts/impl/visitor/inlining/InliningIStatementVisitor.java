/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.ssts.impl.visitor.inlining;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;
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
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
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
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
import cc.kave.commons.model.ssts.impl.visitor.inlining.util.InvocationMethodNameVisitor;
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
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.utils.SSTCloneUtil;

public class InliningIStatementVisitor extends AbstractThrowingNodeVisitor<InliningContext, Void> {

	@Override
	public Void visit(ISST sst, InliningContext context) {
		ISST clone = SSTCloneUtil.clone(sst, ISST.class);
		context.setNonEntryPoints(clone.getNonEntryPoints());
		context.createSST(clone);
		if (clone.getEntryPoints().size() > 0) {
			for (IMethodDeclaration method : clone.getEntryPoints()) {
				method.accept(this, context);
			}
		} else {
			for (IMethodDeclaration method : clone.getMethods()) {
				context.addMethod(method);
			}
		}
		Set<IMethodDeclaration> notInlinedMethods = new HashSet<>();
		Set<IMethodName> calls = new HashSet<>();
		for (IMethodDeclaration method : context.getNonEntryPoints()) {
			if (!context.getInlinedMethods().contains(method)) {
				notInlinedMethods.add(method);
				context.addMethod(method);
				Set<IMethodName> invokes = new HashSet<>();
				method.accept(new InvocationMethodNameVisitor(), invokes);
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
		context.visitScope(stmt.getBody());
		method.getBody().addAll(context.getBody());
		method.setEntryPoint(true);
		method.setName(MethodName.newMethodName(stmt.getName().getIdentifier()));
		context.addMethod(method);

		// Clear body for next Method
		context.resetScope();
		return null;
	}

	public Void visit(IExpressionStatement stmt, InliningContext context) {
		IAssignableExpression assExpr = (IAssignableExpression) stmt.getExpression()
				.accept(context.getExpressionVisitor(), context);
		if (assExpr != null) {
			((ExpressionStatement) stmt).setExpression(assExpr);
			context.addStatement(stmt);
		}
		return null;
	}

	public Void visit(IVariableDeclaration stmt, InliningContext context) {
		// TODO: changed
		stmt.getReference().accept(context.getReferenceVisitor(), context);
		context.addStatement(stmt);
		return null;
	}

	public Void visit(IAssignment stmt, InliningContext context) {
		// TODO: changed
		stmt.getReference().accept(context.getReferenceVisitor(), context);
		IAssignableExpression assExpr = (IAssignableExpression) stmt.getExpression()
				.accept(context.getExpressionVisitor(), context);
		if (assExpr != null) {
			((Assignment) stmt).setExpression(assExpr);
		}
		context.addStatement(stmt);
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
			stmt.getExpression().accept(context.getExpressionVisitor(), context);
			context.addStatement(stmt);
		}
		return null;
	}

	@Override
	public Void visit(IForLoop block, InliningContext context) {
		ForLoop stmt = (ForLoop) block;
		block.getCondition().accept(context.getExpressionVisitor(), context);
		List<IStatement> newBody = Lists.newArrayList();
		context.visitBlock(block.getInit(), newBody);
		stmt.setInit(newBody);
		newBody = Lists.newArrayList();
		context.visitBlock(block.getStep(), newBody);
		stmt.setStep(newBody);
		newBody = Lists.newArrayList();
		context.visitBlock(block.getBody(), newBody);
		stmt.setBody(newBody);
		context.addStatement(block);
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, InliningContext context) {
		block.getCondition().accept(context.getExpressionVisitor(), context);
		List<IStatement> newBody = Lists.newArrayList();
		context.visitBlock(block.getBody(), newBody);
		((WhileLoop) block).setBody(newBody);
		context.addStatement(block);
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, InliningContext context) {
		block.getCondition().accept(context.getExpressionVisitor(), context);
		List<IStatement> newBody = Lists.newArrayList();
		context.visitBlock(block.getElse(), newBody);
		((IfElseBlock) block).setElse(newBody);
		newBody = Lists.newArrayList();
		context.visitBlock(block.getThen(), newBody);
		((IfElseBlock) block).setThen(newBody);
		context.addStatement(block);
		return null;
	}

	@Override
	public Void visit(IDoLoop block, InliningContext context) {
		block.getCondition().accept(context.getExpressionVisitor(), context);
		List<IStatement> newBody = Lists.newArrayList();
		context.visitBlock(block.getBody(), newBody);
		((DoLoop) block).setBody(newBody);
		context.addStatement(block);
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, InliningContext context) {
		// TODO: changed
		ForEachLoop loop = (ForEachLoop) block;
		loop.setDeclaration((IVariableDeclaration) context.visit(block.getDeclaration(), context));
		block.getLoopedReference().accept(context.getReferenceVisitor(), context);
		List<IStatement> newBody = Lists.newArrayList();
		context.visitBlock(block.getBody(), newBody);
		loop.getBody().clear();
		loop.getBody().addAll(newBody);
		context.addStatement(loop);
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, InliningContext context) {
		// TODO: changed
		SwitchBlock switchBlock = (SwitchBlock) block;
		block.getReference().accept(context.getReferenceVisitor(), context);
		context.visitBlock(block.getDefaultSection(), switchBlock.getDefaultSection());
		List<ICaseBlock> caseblocks = Lists.newArrayList();
		for (ICaseBlock caseBlock : block.getSections()) {
			CaseBlock newCase = new CaseBlock();
			context.visitBlock(caseBlock.getBody(), newCase.getBody());
			newCase.setLabel(caseBlock.getLabel());
			caseblocks.add(newCase);
		}
		switchBlock.getSections().clear();
		switchBlock.getSections().addAll(caseblocks);
		context.addStatement(switchBlock);
		return null;
	}

	@Override
	public Void visit(ILockBlock stmt, InliningContext context) {
		// TODO: changed
		LockBlock block = (LockBlock) stmt;
		stmt.getReference().accept(context.getReferenceVisitor(), context);
		List<IStatement> newBody = Lists.newArrayList();
		context.visitBlock(stmt.getBody(), newBody);
		block.setBody(newBody);
		context.addStatement(block);
		return null;
	}

	@Override
	public Void visit(IUsingBlock block, InliningContext context) {
		// TODO: changed
		UsingBlock usingBlock = (UsingBlock) block;
		block.getReference().accept(context.getReferenceVisitor(), context);
		List<IStatement> newBody = Lists.newArrayList();
		context.visitBlock(block.getBody(), newBody);
		usingBlock.setBody(newBody);
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
		context.visitBlock(block.getFinally(), tryBlock.getFinally());
		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			CatchBlock newCatch = new CatchBlock();
			context.visitBlock(catchBlock.getBody(), newCatch.getBody());
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
		context.visitBlock(block.getBody(), unchecked.getBody());
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
		statement.setStatement(context.visit(stmt.getStatement(), context));
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
		statement.setReference(stmt.getReference());
		context.addStatement(statement);
		return null;
	}

	@Override
	public Void visit(IEventSubscriptionStatement stmt, InliningContext context) {
		// TODO: changed
		IAssignableExpression assExpr = (IAssignableExpression) stmt.getExpression()
				.accept(context.getExpressionVisitor(), context);
		if (assExpr != null) {
			((EventSubscriptionStatement) stmt).setExpression(assExpr);
		}
		stmt.getReference().accept(context.getReferenceVisitor(), context);
		context.addStatement(stmt);
		return null;
	}

}
