package cc.kave.commons.model.ssts.impl.blocks;

import java.util.Set;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class IfElseBlock implements IIfElseBlock {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public ISimpleExpression getCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IStatement> getThen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IStatement> getElese() {
		// TODO Auto-generated method stub
		return null;
	}

}
