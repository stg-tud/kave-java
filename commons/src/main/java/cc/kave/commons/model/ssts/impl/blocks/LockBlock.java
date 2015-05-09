package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class LockBlock implements ILockBlock {

	private IVariableReference reference;
	private List<IStatement> body;

	public LockBlock() {
		this.reference = new VariableReference();
		this.body = new ArrayList<IStatement>();
	}

	@Override
	public IVariableReference getReference() {
		return this.reference;
	}

	@Override
	public List<IStatement> getBody() {
		return this.body;
	}

	public void setReference(IVariableReference reference) {
		this.reference = reference;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	private boolean equals(LockBlock other) {
		return this.reference.equals(other.getReference()) && this.body.equals(other.getBody());
	}

	public boolean equals(Object obj) {
		return obj instanceof LockBlock ? this.equals((LockBlock) obj) : false;
	}

	public int hashCode() {
		return (16 + this.reference.hashCode() * 8 + this.body.hashCode());
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}
}
