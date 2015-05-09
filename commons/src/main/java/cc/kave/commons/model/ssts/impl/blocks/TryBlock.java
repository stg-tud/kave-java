package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class TryBlock implements ITryBlock {

	private List<IStatement> body;
	private List<ICatchBlock> catchBlocks;
	private List<IStatement> _finally;

	public TryBlock() {
		this.body = new ArrayList<IStatement>();
		this.catchBlocks = new ArrayList<ICatchBlock>();
		this._finally = new ArrayList<IStatement>();
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

	private boolean equals(TryBlock other) {
		return this.body.equals(other.getBody()) && this.catchBlocks.equals(other.getCatchBlocks())
				&& this._finally.equals(other.getFinally());
	}

	public boolean equals(Object obj) {
		return obj instanceof TryBlock ? this.equals((TryBlock) obj) : false;
	}

	public int hashCode() {
		int hashCode = 37 + this.body.hashCode();
		hashCode = (hashCode * 397) ^ this.catchBlocks.hashCode();
		hashCode = (hashCode * 397) ^ this._finally.hashCode();
		return hashCode;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
