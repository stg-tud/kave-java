package cc.kave.commons.model.ssts.blocks;

import java.util.List;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface IIfElseBlock extends IStatement {

	@Nonnull
	ISimpleExpression getCondition();

	@Nonnull
	List<IStatement> getThen();

	@Nonnull
	List<IStatement> getElse();

}
