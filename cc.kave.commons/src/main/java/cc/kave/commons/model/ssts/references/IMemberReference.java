package cc.kave.commons.model.ssts.references;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IReference;

public interface IMemberReference extends IReference {

	@Nonnull
	IVariableReference getReference();

}
