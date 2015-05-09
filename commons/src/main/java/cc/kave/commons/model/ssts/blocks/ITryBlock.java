package cc.kave.commons.model.ssts.blocks;

import java.util.List;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;

public interface ITryBlock extends IStatement {

	@Nonnull
	List<IStatement> getBody();

	@Nonnull
	List<ICatchBlock> getCatchBlocks();

	@Nonnull
	List<IStatement> getFinally();

}
