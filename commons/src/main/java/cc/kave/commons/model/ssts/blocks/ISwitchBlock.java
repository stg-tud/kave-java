package cc.kave.commons.model.ssts.blocks;

import java.util.List;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface ISwitchBlock extends IStatement {

	@Nonnull
	IVariableReference getReference();

	@Nonnull
	List<ICaseBlock> getSections();

	@Nonnull
	List<IStatement> getDefaultSection();

}
