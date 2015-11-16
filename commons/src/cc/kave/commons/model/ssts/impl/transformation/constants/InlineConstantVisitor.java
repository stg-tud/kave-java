package cc.kave.commons.model.ssts.impl.transformation.constants;

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
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
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

public class InlineConstantVisitor extends AbstractNodeVisitor<InlineConstantContext, Void> {

	@Override
	public Void visit(ISST sst, InlineConstantContext context) {
		context.collectConstants(sst);
		for (IPropertyDeclaration property : sst.getProperties()) {
			property.accept(this, context);
		}
		for (IMethodDeclaration method : sst.getMethods()) {
			method.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(IFieldDeclaration stmt, InlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IAssignment stmt, InlineConstantContext context) {
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IMethodDeclaration stmt, InlineConstantContext context) {
		for (IStatement s : stmt.getBody()) {
			s.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(IPropertyDeclaration stmt, InlineConstantContext context) {
		for (IStatement s : stmt.getGet()) {
			s.accept(this, context);
		}
		for (IStatement s : stmt.getSet()) {
			s.accept(this, context);
		}

		return null;
	}

	@Override
	public Void visit(IVariableDeclaration stmt, InlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IBreakStatement stmt, InlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IContinueStatement stmt, InlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IExpressionStatement stmt, InlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IGotoStatement stmt, InlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(ILabelledStatement stmt, InlineConstantContext context) {
		stmt.getStatement().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IReturnStatement stmt, InlineConstantContext context) {
		ISimpleExpression expr = stmt.getExpression();
		if (expr.accept(context.getExprVisitor(), context.getConstants())) {
			if (stmt instanceof ReturnStatement) {
				((ReturnStatement) stmt).setExpression(new ConstantValueExpression());
			}
		}
		return null;
	}

	@Override
	public Void visit(IThrowStatement stmt, InlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IDoLoop block, InlineConstantContext context) {
		block.getCondition().accept(this, context);
		for (IStatement s : block.getBody()) {
			s.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, InlineConstantContext context) {
		for (IStatement s : block.getBody()) {
			s.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(IForLoop block, InlineConstantContext context) {
		block.getCondition().accept(this, context);
		for (IStatement s : block.getInit()) {
			s.accept(this, context);
		}
		for (IStatement s : block.getBody()) {
			s.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, InlineConstantContext context) {
		block.getCondition().accept(this, context);
		for (IStatement s : block.getThen()) {
			s.accept(this, context);
		}
		for (IStatement s : block.getElse()) {
			s.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(ILockBlock stmt, InlineConstantContext context) {
		for (IStatement s : stmt.getBody()) {
			s.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, InlineConstantContext context) {
		for (IStatement s : block.getDefaultSection()) {
			s.accept(this, context);
		}
		for (ICaseBlock caseBlock : block.getSections()) {
			for (IStatement stm : caseBlock.getBody()) {
				stm.accept(this, context);
			}
		}
		return null;
	}

	@Override
	public Void visit(ITryBlock block, InlineConstantContext context) {
		for (IStatement s : block.getBody()) {
			s.accept(this, context);
		}
		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			for (IStatement s : catchBlock.getBody()) {
				s.accept(this, context);
			}
		}
		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, InlineConstantContext context) {
		for (IStatement s : block.getBody()) {
			s.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, InlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IUsingBlock block, InlineConstantContext context) {
		for (IStatement s : block.getBody()) {
			s.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, InlineConstantContext context) {
		block.getCondition().accept(this, context);
		for (IStatement s : block.getBody()) {
			s.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(ICompletionExpression entity, InlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IComposedExpression expr, InlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IIfElseExpression expr, InlineConstantContext context) {
		expr.getCondition().accept(this, context);
		expr.getThenExpression().accept(this, context);
		expr.getElseExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IInvocationExpression entity, InlineConstantContext context) {
		List<ISimpleExpression> parameters = new ArrayList<ISimpleExpression>();
		for (ISimpleExpression expr : entity.getParameters()) {
			if (expr.accept(context.getExprVisitor(), context.getConstants())) {
				parameters.add(new ConstantValueExpression());
			} else {
				parameters.add(expr);
			}
		}
		if (entity instanceof InvocationExpression) {
			((InvocationExpression) entity).setParameters(parameters);
		}
		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, InlineConstantContext context) {
		for (IStatement s : expr.getBody()) {
			s.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(ILoopHeaderBlockExpression expr, InlineConstantContext context) {
		for (IStatement s : expr.getBody()) {
			s.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(IUnknownExpression unknownExpr, InlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IUnknownStatement unknownStmt, InlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IEventSubscriptionStatement stmt, InlineConstantContext context) {
		stmt.getExpression().accept(this, context);
		return null;
	}
}
