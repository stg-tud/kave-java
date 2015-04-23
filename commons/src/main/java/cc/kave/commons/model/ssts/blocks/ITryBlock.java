package cc.kave.commons.model.ssts.blocks;

import java.util.Set;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;

public interface ITryBlock extends IStatement {

	@Nonnull
	Set<IStatement> getBody();

	@Nonnull
	Set<ICatchBlock> getCatchBlocks();

	@Nonnull
	Set<IStatement> getFinally();

}
