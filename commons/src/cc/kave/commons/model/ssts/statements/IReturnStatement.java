package cc.kave.commons.model.ssts.statements;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface IReturnStatement extends IStatement {

	@Nonnull
	ISimpleExpression getExpression();

	boolean isVoid();

}
