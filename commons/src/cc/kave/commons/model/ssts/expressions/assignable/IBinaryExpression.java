package cc.kave.commons.model.ssts.expressions.assignable;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface IBinaryExpression extends IAssignableExpression {

	BinaryOperator getOperator();

	@NonNull
	ISimpleExpression getRightOperand();

	@NonNull
	ISimpleExpression getLeftOperand();

}
