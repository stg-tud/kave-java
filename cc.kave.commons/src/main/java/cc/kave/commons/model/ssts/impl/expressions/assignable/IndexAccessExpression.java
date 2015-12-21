package cc.kave.commons.model.ssts.impl.expressions.assignable;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class IndexAccessExpression implements IIndexAccessExpression {

	private IVariableReference reference;
	private List<ISimpleExpression> indices;

	public IndexAccessExpression() {
		this.reference = new VariableReference();
		this.indices = new ArrayList<ISimpleExpression>();
	}

	@Override
	public IVariableReference getReference() {
		return this.reference;
	}

	@Override
	public List<ISimpleExpression> getIndices() {
		return this.indices;
	}

	public void setReference(IVariableReference reference) {
		this.reference = reference;
	}

	public void setIndices(List<ISimpleExpression> indices) {
		this.indices = indices;
	}

	public Iterable<ISSTNode> getChildren() {
		return new ArrayList<ISSTNode>();
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((indices == null) ? 0 : indices.hashCode());
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndexAccessExpression other = (IndexAccessExpression) obj;
		if (indices == null) {
			if (other.indices != null)
				return false;
		} else if (!indices.equals(other.indices))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}

}
