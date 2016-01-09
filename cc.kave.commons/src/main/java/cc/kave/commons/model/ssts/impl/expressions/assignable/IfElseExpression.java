package cc.kave.commons.model.ssts.impl.expressions.assignable;

import java.util.ArrayList;

import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class IfElseExpression implements IIfElseExpression {

	private ISimpleExpression condition;
	private ISimpleExpression thenExpression;
	private ISimpleExpression elseExpression;

	public IfElseExpression() {
		this.condition = new UnknownExpression();
		this.thenExpression = new UnknownExpression();
		this.elseExpression = new UnknownExpression();
	}
	
	@Override
	public Iterable<ISSTNode> getChildren() {
		return new ArrayList<ISSTNode>();
	}

	@Override
	public ISimpleExpression getCondition() {
		return this.condition;
	}

	@Override
	public ISimpleExpression getThenExpression() {
		return this.thenExpression;
	}

	@Override
	public ISimpleExpression getElseExpression() {
		return this.elseExpression;
	}

	public void setCondition(ISimpleExpression condition) {
		this.condition = condition;
	}

	public void setThenExpression(ISimpleExpression thenExpression) {
		this.thenExpression = thenExpression;
	}

	public void setElseExpression(ISimpleExpression elseExpression) {
		this.elseExpression = elseExpression;
	}

	@Override
	public int hashCode() {
		int hashCode = 10 + this.condition.hashCode();
		hashCode = (hashCode * 397) ^ this.thenExpression.hashCode();
		hashCode = (hashCode * 397) ^ this.elseExpression.hashCode();
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IfElseExpression))
			return false;
		IfElseExpression other = (IfElseExpression) obj;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (elseExpression == null) {
			if (other.elseExpression != null)
				return false;
		} else if (!elseExpression.equals(other.elseExpression))
			return false;
		if (thenExpression == null) {
			if (other.thenExpression != null)
				return false;
		} else if (!thenExpression.equals(other.thenExpression))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}
}
