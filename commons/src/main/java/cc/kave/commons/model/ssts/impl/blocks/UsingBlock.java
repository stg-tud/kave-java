package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class UsingBlock implements IUsingBlock {

	private IVariableReference reference;
	private List<IStatement> body;

	public UsingBlock() {
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

	private boolean equals(UsingBlock other) {
		return this.body.equals(other.getBody()) && this.reference.equals(other.getReference());
	}

	public boolean equals(Object obj) {
		return obj instanceof UsingBlock ? this.equals((UsingBlock) obj) : false;
	}

	public int hashCode() {
		return (39 + 6 * this.body.hashCode() + this.reference.hashCode());
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
