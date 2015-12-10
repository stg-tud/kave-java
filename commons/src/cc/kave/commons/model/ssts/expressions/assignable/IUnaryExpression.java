package cc.kave.commons.model.ssts.expressions.assignable;

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface IUnaryExpression extends IAssignableExpression {

	UnaryOperator getOperator();

	ISimpleExpression getOperand();
}
