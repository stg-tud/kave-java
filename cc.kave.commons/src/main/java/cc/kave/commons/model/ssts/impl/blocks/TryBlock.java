package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;

public class TryBlock implements ITryBlock {

	private List<IStatement> body;
	private List<ICatchBlock> catchBlocks;
	@SerializedName("Finally")
	private List<IStatement> _finally;

	public TryBlock() {
		this.body = new ArrayList<>();
		this.catchBlocks = new ArrayList<>();
		this._finally = new ArrayList<>();
	}
	
	@Override
	public Iterable<ISSTNode> getChildren() {
		List<ISSTNode> children = new ArrayList<>(body);
		children.addAll(_finally);
		return children;
	}

	@Override
	public List<IStatement> getBody() {
		return this.body;
	}

	@Override
	public List<ICatchBlock> getCatchBlocks() {
		return this.catchBlocks;
	}

	@Override
	public List<IStatement> getFinally() {
		return this._finally;
	}

	public void setFinally(List<IStatement> _finally) {
		this._finally = _finally;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	public void setCatchBlocks(List<ICatchBlock> catchBlocks) {
		this.catchBlocks = catchBlocks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_finally == null) ? 0 : _finally.hashCode());
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((catchBlocks == null) ? 0 : catchBlocks.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TryBlock))
			return false;
		TryBlock other = (TryBlock) obj;
		if (_finally == null) {
			if (other._finally != null)
				return false;
		} else if (!_finally.equals(other._finally))
			return false;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (catchBlocks == null) {
			if (other.catchBlocks != null)
				return false;
		} else if (!catchBlocks.equals(other.catchBlocks))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
