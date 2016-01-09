package cc.kave.commons.model.ssts.impl.expressions.simple;

import java.util.ArrayList;

import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ConstantValueExpression implements IConstantValueExpression {

	private String value;
	
	@Override
	public Iterable<ISSTNode> getChildren() {
		return new ArrayList<ISSTNode>();
	}

	@Override
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return 102 + (this.value != null ? this.value.hashCode() : 0);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ConstantValueExpression))
			return false;
		ConstantValueExpression other = (ConstantValueExpression) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
