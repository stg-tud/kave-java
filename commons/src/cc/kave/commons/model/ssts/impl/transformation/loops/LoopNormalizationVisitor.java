package cc.kave.commons.model.ssts.impl.transformation.loops;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsTypeName;
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
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
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
		method.getBody().clear();
		method.getBody().addAll(bodyNormalized);
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
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		block.getBody().clear();
		block.getBody().addAll(bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IForEachLoop block, LoopNormalizationContext context) {
		// normalize inner loops
		List<IStatement> bodyNormalized = visit(block.getBody(), context);

		// create condition
		LoopHeaderBlockExpression condition = new LoopHeaderBlockExpression();
		String loopedReference = block.getLoopedReference().getIdentifier();
		MethodName method = CsMethodName.newMethodName("hasNext");
		condition.setBody(Lists.newArrayList(
				SSTUtil.invocationStatement(loopedReference, method, new ArrayList<ISimpleExpression>().iterator())));

		IVariableDeclaration varDecl = block.getDeclaration();
		IVariableReference varRef = varDecl.getReference();
		TypeName varType = varDecl.getType();

		// declare iterator
		// TODO: fix typename? (type parameters correct?)
		TypeName iteratorTypeName = CsTypeName
				.newTypeName("Iterator`1[[T -> " + varType.getIdentifier() + "]], package, 1.0.0.0");
		IVariableDeclaration iteratorDecl = SSTUtil.declareVar("iterator", iteratorTypeName);

		// assign iterator
		InvocationExpression invokeIterator = new InvocationExpression();
		invokeIterator.setMethodName(CsMethodName.newMethodName("iterator"));
		invokeIterator.setReference(varRef);
		IStatement iteratorInit = SSTUtil.assign(iteratorDecl.getReference(), invokeIterator);

		// declare element
		IVariableDeclaration elemDecl = SSTUtil.declareVar("elem", varType);

		// assign next element
		InvocationExpression invokeNext = new InvocationExpression();
		invokeNext.setMethodName(CsMethodName.newMethodName("next"));
		invokeNext.setReference(iteratorDecl.getReference());
		IStatement elemAssign = SSTUtil.assign(elemDecl.getReference(), invokeNext);

		// assemble while loop
		List<IStatement> whileBody = new ArrayList(); // TODO
		whileBody.addAll(bodyNormalized);
		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setBody(whileBody);
		whileLoop.setCondition(condition);

		List<IStatement> normalized = new ArrayList<IStatement>();
		normalized.add(iteratorDecl);
		normalized.add(iteratorInit);
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

		// TODO: put in some kind of block statement? (to limit scope of 'init'
		// part)
		// TODO: handle 'continue' inside loop body?
		List<IStatement> whileBody = bodyNormalized;
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
		block.getThen().clear();
		block.getThen().addAll(thenBranchNormalized);

		List<IStatement> elseBranch = block.getElse();
		List<IStatement> elseBranchNormalized = visit(elseBranch, context);
		block.getElse().clear();
		block.getElse().addAll(elseBranchNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(ILockBlock block, LoopNormalizationContext context) {
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		block.getBody().clear();
		block.getBody().addAll(bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(ISwitchBlock block, LoopNormalizationContext context) {
		for (ICaseBlock caseBlock : block.getSections()) {
			List<IStatement> body = caseBlock.getBody();
			List<IStatement> bodyNormalized = visit(body, context);
			caseBlock.getBody().clear();
			caseBlock.getBody().addAll(bodyNormalized);
		}

		List<IStatement> defaultSection = block.getDefaultSection();
		List<IStatement> defaultSectionNormalized = visit(defaultSection, context);
		block.getDefaultSection().clear();
		block.getDefaultSection().addAll(defaultSectionNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(ITryBlock block, LoopNormalizationContext context) {
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		block.getBody().clear();
		block.getBody().addAll(bodyNormalized);

		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			List<IStatement> catchBody = catchBlock.getBody();
			List<IStatement> catchBodyNormalized = visit(catchBody, context);
			catchBlock.getBody().clear();
			catchBlock.getBody().addAll(catchBodyNormalized);
		}

		List<IStatement> finallyStmt = block.getFinally();
		List<IStatement> finallyStmtNormalized = visit(finallyStmt, context);
		block.getFinally().clear();
		block.getFinally().addAll(finallyStmtNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IUncheckedBlock block, LoopNormalizationContext context) {
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		block.getBody().clear();
		block.getBody().addAll(bodyNormalized);
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
		block.getBody().clear();
		block.getBody().addAll(bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IWhileLoop block, LoopNormalizationContext context) {
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = visit(body, context);
		block.getBody().clear();
		block.getBody().addAll(bodyNormalized);
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
}
