package cc.kave.commons.model.ssts.blocks;

import java.util.Set;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;

public interface IForLoop extends IStatement {

	@Nonnull
	Set<IStatement> getInit();

	@Nonnull
	ILoopHeaderExpression getCondition();

	@Nonnull
	Set<IStatement> getStep();

	@Nonnull
	Set<IStatement> getBody();

}
