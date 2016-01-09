package cc.kave.commons.model.ssts.impl.references;

import java.util.ArrayList;

import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class IndexAccessReference implements IIndexAccessReference {

	private IIndexAccessExpression expression;

	public IndexAccessReference() {
		this.expression = new IndexAccessExpression();
	}

	@Override
	public IIndexAccessExpression getExpression() {
		return this.expression;
	}

	public void setExpression(IIndexAccessExpression expression) {
		this.expression = expression;
	}

	@Override
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
		result = prime * result + ((expression == null) ? 0 : expression.hashCode());
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
		IndexAccessReference other = (IndexAccessReference) obj;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		return true;
	}

}
