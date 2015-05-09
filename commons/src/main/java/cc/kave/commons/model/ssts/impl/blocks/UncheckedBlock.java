package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class UncheckedBlock implements IUncheckedBlock {

	private List<IStatement> body;

	public UncheckedBlock() {
		this.body = new ArrayList<IStatement>();
	}

	@Override
	public List<IStatement> getBody() {
		return this.body;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	private boolean equals(UncheckedBlock other) {
		return this.body.equals(other.getBody());
	}

	public boolean equals(Object obj) {
		return obj instanceof UncheckedBlock ? this.equals((UncheckedBlock) obj) : false;
	}

	public int hashCode() {
		return 372 + this.body.hashCode();
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
