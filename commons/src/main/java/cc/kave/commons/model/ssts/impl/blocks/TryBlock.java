package cc.kave.commons.model.ssts.impl.blocks;

import java.util.Set;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class TryBlock implements ITryBlock {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public Set<IStatement> getBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ICatchBlock> getCatchBlocks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IStatement> getFinally() {
		// TODO Auto-generated method stub
		return null;
	}

}
