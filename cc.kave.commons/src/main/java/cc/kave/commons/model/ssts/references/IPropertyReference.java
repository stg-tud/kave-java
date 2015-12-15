package cc.kave.commons.model.ssts.references;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.PropertyName;

public interface IPropertyReference extends IAssignableReference, IMemberReference {

	@Nonnull
	PropertyName getPropertyName();
}
