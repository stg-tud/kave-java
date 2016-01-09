package cc.kave.commons.model.ssts.impl.expressions.assignable;

import java.util.ArrayList;

import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class UnaryExpression implements IUnaryExpression {

	private UnaryOperator operator;
	private ISimpleExpression operand;

	public UnaryExpression() {
		this.operand = new UnknownExpression();
		this.operator = UnaryOperator.Unknown;
	}

	@Override
	public UnaryOperator getOperator() {
		return this.operator;
	}

	@Override
	public ISimpleExpression getOperand() {
		return this.operand;
	}

	public void setOperator(UnaryOperator operator) {
		this.operator = operator;
	}

	public void setOperand(ISimpleExpression operand) {
		this.operand = operand;
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
		result = prime * result + ((operand == null) ? 0 : operand.hashCode());
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
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
		UnaryExpression other = (UnaryExpression) obj;
		if (operand == null) {
			if (other.operand != null)
				return false;
		} else if (!operand.equals(other.operand))
			return false;
		if (operator != other.operator)
			return false;
		return true;
	}

}
