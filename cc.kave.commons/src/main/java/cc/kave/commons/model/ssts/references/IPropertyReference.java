package cc.kave.commons.model.ssts.references;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.IPropertyName;

public interface IPropertyReference extends IAssignableReference, IMemberReference {

	@Nonnull
	IPropertyName getPropertyName();
}
