package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class UncheckedBlock implements IUncheckedBlock {

	private List<IStatement> body;

	public UncheckedBlock() {
		this.body = new ArrayList<>();
	}
	
	@Override
	public Iterable<ISSTNode> getChildren() {
		List<ISSTNode> children = new ArrayList<>(body);
		return children;
	}

	@Override
	public List<IStatement> getBody() {
		return this.body;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UncheckedBlock))
			return false;
		UncheckedBlock other = (UncheckedBlock) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
