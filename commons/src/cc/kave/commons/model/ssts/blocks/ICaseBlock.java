package cc.kave.commons.model.ssts.blocks;

import java.util.List;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface ICaseBlock {

	@Nonnull
	ISimpleExpression getLabel();

	@Nonnull
	List<IStatement> getBody();
}
