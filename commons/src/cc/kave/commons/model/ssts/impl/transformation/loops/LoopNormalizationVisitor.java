package cc.kave.commons.model.ssts.impl.transformation.loops;

import java.util.ArrayList;
import java.util.List;

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
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.visitor.AbstractNodeVisitor;
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
		List<IStatement> bodyNormalized = new ArrayList<IStatement>();
		for (IStatement stmt : body) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (stmtNormalized == null)
				bodyNormalized.add(stmt);
			else
				bodyNormalized.addAll(stmtNormalized);
		}
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
		List<IStatement> bodyNormalized = new ArrayList<IStatement>();
		for (IStatement stmt : body) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (stmtNormalized == null)
				bodyNormalized.add(stmt);
			else
				bodyNormalized.addAll(stmtNormalized);
		}
		block.getBody().clear();
		block.getBody().addAll(bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IForEachLoop block, LoopNormalizationContext context) {
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = new ArrayList<IStatement>();
		for (IStatement stmt : body) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (stmtNormalized == null)
				bodyNormalized.add(stmt);
			else
				bodyNormalized.addAll(stmtNormalized);
		}
		block.getBody().clear();
		block.getBody().addAll(bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IForLoop block, LoopNormalizationContext context) {
		ILoopHeaderExpression condition = block.getCondition();

		List<IStatement> init = block.getInit();
		List<IStatement> initNormalized = new ArrayList<IStatement>();
		for (IStatement stmt : init) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (stmtNormalized == null)
				initNormalized.add(stmt);
			else
				initNormalized.addAll(stmtNormalized);
		}

		List<IStatement> step = block.getStep();
		List<IStatement> stepNormalized = new ArrayList<IStatement>();
		for (IStatement stmt : step) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (stmtNormalized == null)
				stepNormalized.add(stmt);
			else
				stepNormalized.addAll(stmtNormalized);
		}

		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = new ArrayList<IStatement>();
		for (IStatement stmt : body) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (stmtNormalized == null)
				bodyNormalized.add(stmt);
			else
				bodyNormalized.addAll(stmtNormalized);
		}
		List<IStatement> whileBody = bodyNormalized;
		whileBody.addAll(step);
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
		List<IStatement> thenBranchNormalized = new ArrayList<IStatement>();
		for (IStatement stmt : thenBranch) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (stmtNormalized == null)
				thenBranchNormalized.add(stmt);
			else
				thenBranchNormalized.addAll(stmtNormalized);
		}
		thenBranch.clear();
		thenBranch.addAll(thenBranchNormalized);

		List<IStatement> elseBranch = block.getElse();
		List<IStatement> elseBranchNormalized = new ArrayList<IStatement>();
		for (IStatement stmt : elseBranch) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (stmtNormalized == null)
				elseBranchNormalized.add(stmt);
			else
				elseBranchNormalized.addAll(stmtNormalized);
		}
		elseBranch.clear();
		elseBranch.addAll(elseBranchNormalized);

		return null;
	}

	@Override
	public List<IStatement> visit(ILockBlock block, LoopNormalizationContext context) {
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = new ArrayList<IStatement>();
		for (IStatement stmt : body) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (stmtNormalized == null)
				bodyNormalized.add(stmt);
			else
				bodyNormalized.addAll(stmtNormalized);
		}
		block.getBody().clear();
		block.getBody().addAll(bodyNormalized);

		return null;
	}

	@Override
	public List<IStatement> visit(ISwitchBlock block, LoopNormalizationContext context) {
		for (ICaseBlock caseBlock : block.getSections()) {
			List<IStatement> body = caseBlock.getBody();
			List<IStatement> bodyNormalized = new ArrayList<IStatement>();
			for (IStatement stmt : body) {
				List<IStatement> stmtNormalized = stmt.accept(this, context);
				if (stmtNormalized == null)
					bodyNormalized.add(stmt);
				else
					bodyNormalized.addAll(stmtNormalized);
			}
			caseBlock.getBody().clear();
			caseBlock.getBody().addAll(bodyNormalized);
		}

		List<IStatement> defaultSection = block.getDefaultSection();
		List<IStatement> defaultSectionNormalized = new ArrayList<IStatement>();
		for (IStatement stmt : defaultSection) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (stmtNormalized == null)
				defaultSectionNormalized.add(stmt);
			else
				defaultSectionNormalized.addAll(stmtNormalized);
		}
		block.getDefaultSection().clear();
		block.getDefaultSection().addAll(defaultSectionNormalized);

		return null;
	}

	@Override
	public List<IStatement> visit(ITryBlock block, LoopNormalizationContext context) {
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = new ArrayList<IStatement>();
		for (IStatement stmt : body) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (stmtNormalized == null)
				bodyNormalized.add(stmt);
			else
				bodyNormalized.addAll(stmtNormalized);
		}
		block.getBody().clear();
		block.getBody().addAll(bodyNormalized);

		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			List<IStatement> catchBody = catchBlock.getBody();
			List<IStatement> catchBodyNormalized = new ArrayList<IStatement>();
			for (IStatement stmt : catchBody) {
				List<IStatement> stmtNormalized = stmt.accept(this, context);
				if (stmtNormalized == null)
					catchBodyNormalized.add(stmt);
				else
					catchBodyNormalized.addAll(stmtNormalized);
			}
			catchBlock.getBody().clear();
			catchBlock.getBody().addAll(catchBodyNormalized);
		}
		for (IStatement stmt : block.getFinally()) {
			stmt.accept(this, context);
		}
		List<IStatement> finallyStmt = block.getFinally();
		List<IStatement> finallyStmtNormalized = new ArrayList<IStatement>();
		for (IStatement stmt : finallyStmt) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (stmtNormalized == null)
				finallyStmtNormalized.add(stmt);
			else
				finallyStmtNormalized.addAll(stmtNormalized);
		}
		block.getFinally().clear();
		block.getFinally().addAll(finallyStmtNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IUncheckedBlock block, LoopNormalizationContext context) {
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = new ArrayList<IStatement>();
		for (IStatement stmt : body) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (stmtNormalized == null)
				bodyNormalized.add(stmt);
			else
				bodyNormalized.addAll(stmtNormalized);
		}
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
		List<IStatement> bodyNormalized = new ArrayList<IStatement>();
		for (IStatement stmt : body) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (stmtNormalized == null)
				bodyNormalized.add(stmt);
			else
				bodyNormalized.addAll(stmtNormalized);
		}
		block.getBody().clear();
		block.getBody().addAll(bodyNormalized);
		return null;
	}

	@Override
	public List<IStatement> visit(IWhileLoop block, LoopNormalizationContext context) {
		List<IStatement> body = block.getBody();
		List<IStatement> bodyNormalized = new ArrayList<IStatement>();
		for (IStatement stmt : body) {
			List<IStatement> stmtNormalized = stmt.accept(this, context);
			if (stmtNormalized == null)
				bodyNormalized.add(stmt);
			else
				bodyNormalized.addAll(stmtNormalized);
		}
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
}
