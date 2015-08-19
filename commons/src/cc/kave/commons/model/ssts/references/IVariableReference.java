package cc.kave.commons.model.ssts.references;

import javax.annotation.Nonnull;

public interface IVariableReference extends IAssignableReference {

	@Nonnull
	String getIdentifier();

	boolean isMissing();
}
