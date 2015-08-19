package cc.kave.commons.model.ssts.statements;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;

public interface IExpressionStatement extends IStatement {

	@Nonnull
	IAssignableExpression getExpression();
}
