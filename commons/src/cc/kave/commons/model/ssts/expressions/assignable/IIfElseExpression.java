package cc.kave.commons.model.ssts.expressions.assignable;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface IIfElseExpression extends IAssignableExpression {

	@NonNull
	ISimpleExpression getCondition();

	@NonNull
	ISimpleExpression getThenExpression();

	@NonNull
	ISimpleExpression getElseExpression();

}
