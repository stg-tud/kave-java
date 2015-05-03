package cc.kave.commons.model.ssts.impl.blocks;

import java.util.Set;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class UncheckedBlock implements IUncheckedBlock {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public Set<IStatement> getBody() {
		// TODO Auto-generated method stub
		return null;
	}

}
