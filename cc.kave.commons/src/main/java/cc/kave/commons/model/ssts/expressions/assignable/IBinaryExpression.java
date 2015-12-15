package cc.kave.commons.model.ssts.expressions.assignable;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface IBinaryExpression extends IAssignableExpression {

	BinaryOperator getOperator();

	@Nonnull
	ISimpleExpression getRightOperand();

	@Nonnull
	ISimpleExpression getLeftOperand();
}