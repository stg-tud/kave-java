package cc.kave.commons.model.ssts.impl.expressions.assignable;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ComposedExpression implements IComposedExpression {

	private List<IVariableReference> references;

	public ComposedExpression() {
		this.references = new ArrayList<IVariableReference>();
	}

	@Override
	public List<IVariableReference> getReferences() {
		return this.references;
	}

	public void setReferences(List<IVariableReference> references) {
		this.references = references;
	}

	@Override
	public int hashCode() {
		return (5 + this.references.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ComposedExpression))
			return false;
		ComposedExpression other = (ComposedExpression) obj;
		if (references == null) {
			if (other.references != null)
				return false;
		} else if (!references.equals(other.references))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
