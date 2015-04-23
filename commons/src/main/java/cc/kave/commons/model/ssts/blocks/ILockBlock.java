package cc.kave.commons.model.ssts.blocks;

import java.util.Set;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface ILockBlock extends IStatement {

	@Nonnull
	IVariableReference getReference();

	@Nonnull
	Set<IStatement> getBody();

}
