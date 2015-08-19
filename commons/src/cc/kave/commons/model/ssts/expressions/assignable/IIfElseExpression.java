package cc.kave.commons.model.ssts.expressions.assignable;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface IIfElseExpression extends IAssignableExpression {

	@Nonnull
	ISimpleExpression getCondition();

	@Nonnull
	ISimpleExpression getThenExpression();

	@Nonnull
	ISimpleExpression getElseExpression();

}
