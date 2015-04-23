package cc.kave.commons.model.ssts.blocks;

import java.util.Set;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;

public interface IUncheckedBlock extends IStatement {

	@Nonnull
	Set<IStatement> getBody();

}
