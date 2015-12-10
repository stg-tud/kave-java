package cc.kave.commons.model.ssts.impl.expressions.assignable;

import java.util.ArrayList;

import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class BinaryExpression implements IBinaryExpression {

	private ISimpleExpression leftOperand;
	private BinaryOperator operator;
	private ISimpleExpression rightOperand;

	public BinaryExpression() {
		leftOperand = new UnknownExpression();
		rightOperand = new UnknownExpression();
		operator = BinaryOperator.Unknown;
	}

	@Override
	public ISimpleExpression getLeftOperand() {
		return this.leftOperand;
	}

	@Override
	public BinaryOperator getOperator() {
		return this.operator;
	}

	@Override
	public ISimpleExpression getRightOperand() {
		return this.rightOperand;
	}

	public void setOperator(BinaryOperator operator) {
		this.operator = operator;
	}

	public void setLeftOperand(ConstantValueExpression leftOperand) {
		this.leftOperand = leftOperand;
	}

	public void setRightOperand(ReferenceExpression rightOperand) {
		this.rightOperand = rightOperand;
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
		result = prime * result + ((leftOperand == null) ? 0 : leftOperand.hashCode());
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
		result = prime * result + ((rightOperand == null) ? 0 : rightOperand.hashCode());
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
		BinaryExpression other = (BinaryExpression) obj;
		if (leftOperand == null) {
			if (other.leftOperand != null)
				return false;
		} else if (!leftOperand.equals(other.leftOperand))
			return false;
		if (operator != other.operator)
			return false;
		if (rightOperand == null) {
			if (other.rightOperand != null)
				return false;
		} else if (!rightOperand.equals(other.rightOperand))
			return false;
		return true;
	}

}
