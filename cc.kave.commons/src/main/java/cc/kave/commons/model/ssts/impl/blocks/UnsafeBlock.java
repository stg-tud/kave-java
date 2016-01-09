package cc.kave.commons.model.ssts.impl.blocks;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class UnsafeBlock implements IUnsafeBlock {
	
	@Override
	public Iterable<ISSTNode> getChildren() {
		return Lists.newArrayList();
	}

	public boolean equals(Object obj) {
		return obj instanceof UnsafeBlock ? true : false;
	}

	public int hashCode() {
		return 38;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}
}
