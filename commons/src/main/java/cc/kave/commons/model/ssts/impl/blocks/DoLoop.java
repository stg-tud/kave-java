package cc.kave.commons.model.ssts.impl.blocks;

import java.util.Set;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class DoLoop implements IDoLoop {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public ILoopHeaderExpression getCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IStatement> getBody() {
		// TODO Auto-generated method stub
		return null;
	}

}
