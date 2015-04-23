package cc.kave.commons.model.ssts.blocks;

import java.util.Set;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface IIfElseBlock extends IStatement {

	@Nonnull
	ISimpleExpression getCondition();

	@Nonnull
	Set<IStatement> getThen();

	@Nonnull
	Set<IStatement> getElese();

}
