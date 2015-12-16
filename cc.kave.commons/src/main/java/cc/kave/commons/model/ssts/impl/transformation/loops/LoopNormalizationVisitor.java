package cc.kave.commons.model.ssts.impl.transformation.loops;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
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
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.visitor.AbstractNodeVisitor;
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

public class LoopNormalizationVisitor extends AbstractNodeVisitor<LoopNormalizationContext, List<IStatement>> {

	@Override
	public List<IStatement> visit(ISST sst, LoopNormalizationContext context) {

		for (IPropertyDeclaration property : sst.getProperties()) {
			property.accept(this, context);
		}
		for (IMethodDeclaration method : sst.getMethods()) {
			method.accept(this, context);
		}
		return null;
	}

	@Override
	public List<IStatement> visit(IMethodDeclaration method, LoopNormalizationContext context) {
		List<IStatement> body = method.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		update(method.getBody(), bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IAssignment stmt, LoopNormalizationContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IVariableDeclaration stmt, LoopNormalizationContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IBreakStatement stmt, LoopNormalizationContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IContinueStatement stmt, LoopNormalizationContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IExpressionStatement stmt, LoopNormalizationContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IGotoStatement stmt, LoopNormalizationContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(ILabelledStatement stmt, LoopNormalizationContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IReturnStatement stmt, LoopNormalizationContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IThrowStatement stmt, LoopNormalizationContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IDoLoop block, LoopNormalizationContext context) {
		// normalize inner loops
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		update(block.getBody(), bodyNormalized);

		// create condition
		IConstantValueExpression condition = SSTUtil.constant("true");

		IfElseBlock ifBlock = new IfElseBlock();
		// TODO insert negated loop condition
		// (break loop when condition evaluates to
		// false)
		// ifBlock.setCondition();
		ifBlock.setThen(Lists.newArrayList(new BreakStatement()));

		// assemble while loop
		List<IStatement> whileBody = new ArrayList<IStatement>();
		whileBody.addAll(bodyNormalized);
		whileBody.add(ifBlock);

		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setBody(whileBody);
		whileLoop.setCondition(condition);

		List<IStatement> normalized = new ArrayList<IStatement>();
		normalized.add(whileLoop);
		return normalized;
	}

	@Override
	public List<IStatement> visit(IForEachLoop block, LoopNormalizationContext context) {

		IVariableReference loopedReference = block.getLoopedReference();
		IVariableDeclaration variableDeclaration = block.getDeclaration();
		String variableName = variableDeclaration.getReference().getIdentifier();
		ITypeName variableType = variableDeclaration.getType();

		// normalize inner loops
		List<IStatement> bodyNormalized = visit(block.getBody(), context);

		// TODO distinction between Java / C#
		// declare iterator
		ITypeName iteratorTypeName = TypeName
				.newTypeName("java.util.Iterator`1[[T -> " + variableType.getIdentifier() + "]], jre, 1.6");
		IVariableDeclaration iteratorDecl = SSTUtil.declareVar("iterator", iteratorTypeName);

		// initialize iterator
		InvocationExpression invokeIterator = new InvocationExpression();
		invokeIterator.setMethodName(MethodName.newMethodName("iterator"));
		invokeIterator.setReference(loopedReference);
		IStatement iteratorInit = SSTUtil.assign(iteratorDecl.getReference(), invokeIterator);

		// create condition
		InvocationExpression invokeHasNext = new InvocationExpression();
		invokeHasNext.setMethodName(MethodName.newMethodName("hasNext"));
		invokeHasNext.setReference(iteratorDecl.getReference());
		ILoopHeaderBlockExpression condition = SSTUtil.loopHeader(SSTUtil.expr(invokeHasNext));

		// declare element
		IVariableDeclaration elemDecl = SSTUtil.declareVar(variableName, variableType);

		// assign next element
		InvocationExpression invokeNext = new InvocationExpression();
		invokeNext.setMethodName(MethodName.newMethodName("next"));
		invokeNext.setReference(iteratorDecl.getReference());
		IStatement elemAssign = SSTUtil.assign(elemDecl.getReference(), invokeNext);

		// assemble while loop
		List<IStatement> whileBody = new ArrayList<IStatement>();
		whileBody.add(elemAssign);
		whileBody.addAll(bodyNormalized);
		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setBody(whileBody);
		whileLoop.setCondition(condition);

		List<IStatement> normalized = new ArrayList<IStatement>();
		normalized.add(iteratorDecl);
		normalized.add(iteratorInit);
		normalized.add(elemDecl);
		normalized.add(whileLoop);
		return normalized;
	}

	@Override
	public List<IStatement> visit(IForLoop block, LoopNormalizationContext context) {
		ILoopHeaderExpression condition = block.getCondition();

		// normalize inner loops
		List<IStatement> initNormalized = visit(block.getInit(), context);
		List<IStatement> stepNormalized = visit(block.getStep(), context);
		List<IStatement> bodyNormalized = visit(block.getBody(), context);

		// TODO: limit scope of 'init' part?

		// handle 'continue' inside loop body
		List<IStatement> whileBody = context.replicateLoopStep(bodyNormalized, stepNormalized);
		whileBody.addAll(stepNormalized);
		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setBody(whileBody);
		whileLoop.setCondition(condition);

		List<IStatement> normalized = new ArrayList<IStatement>();
		normalized.addAll(initNormalized);
		normalized.add(whileLoop);
		return normalized;
	}

	@Override
	public List<IStatement> visit(IIfElseBlock block, LoopNormalizationContext context) {
		List<IStatement> thenBranch = block.getThen();
		List<IStatement> thenBranchNormalized = visit(thenBranch, context);
		update(block.getThen(), thenBranchNormalized);

		List<IStatement> elseBranch = block.getElse();
		List<IStatement> elseBranchNormalized = visit(elseBranch, context);
		update(block.getElse(), elseBranchNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(ILockBlock block, LoopNormalizationContext context) {
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		update(block.getBody(), bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(ISwitchBlock block, LoopNormalizationContext context) {
		for (ICaseBlock caseBlock : block.getSections()) {
			List<IStatement> body = caseBlock.getBody();
			List<IStatement> bodyNormalized = visit(body, context);
			update(caseBlock.getBody(), bodyNormalized);
		}

		List<IStatement> defaultSection = block.getDefaultSection();
		List<IStatement> defaultSectionNormalized = visit(defaultSection, context);
		update(block.getDefaultSection(), defaultSectionNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(ITryBlock block, LoopNormalizationContext context) {
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		update(block.getBody(), bodyNormalized);

		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			List<IStatement> catchBody = catchBlock.getBody();
			List<IStatement> catchBodyNormalized = visit(catchBody, context);
			update(catchBlock.getBody(), catchBodyNormalized);
		}

		List<IStatement> finallyStmt = block.getFinally();
		List<IStatement> finallyStmtNormalized = visit(finallyStmt, context);
		update(block.getFinally(), finallyStmtNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IUncheckedBlock block, LoopNormalizationContext context) {
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		update(block.getBody(), bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IUnsafeBlock block, LoopNormalizationContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IUsingBlock block, LoopNormalizationContext context) {
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		update(block.getBody(), bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IWhileLoop block, LoopNormalizationContext context) {
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		update(block.getBody(), bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IUnknownStatement unknownStmt, LoopNormalizationContext context) {
		return null;
	}

	@Override
	public List<IStatement> visit(IEventSubscriptionStatement stmt, LoopNormalizationContext context) {
		return null;
	}

	/**
	 * Helper method to visit list of statements.
	 */
	public List<IStatement> visit(List<IStatement> statements, LoopNormalizationContext context) {
		List<IStatement> normalized = new ArrayList<IStatement>();
		for (IStatement stmt : statements) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (stmtNormalized == null)
				normalized.add(stmt);
			else
				normalized.addAll(stmtNormalized);
		}
		return normalized;
	}

	public List<IStatement> update(List<IStatement> statements, List<IStatement> normalized) {
		if (normalized != null) {
			statements.clear();
			statements.addAll(normalized);
		}
		return statements;
	}
}
