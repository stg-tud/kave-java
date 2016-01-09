package cc.kave.commons.model.ssts.impl.expressions.simple;

import java.util.ArrayList;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ReferenceExpression implements IReferenceExpression {

	private IReference reference;

	public ReferenceExpression() {
		this.reference = new UnknownReference();
	}
	
	@Override
	public Iterable<ISSTNode> getChildren() {
		return new ArrayList<ISSTNode>();
	}

	@Override
	public IReference getReference() {
		return this.reference;
	}

	public void setReference(IReference reference) {
		this.reference = reference;
	}

	@Override
	public int hashCode() {
		return (29 + this.reference.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ReferenceExpression))
			return false;
		ReferenceExpression other = (ReferenceExpression) obj;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
