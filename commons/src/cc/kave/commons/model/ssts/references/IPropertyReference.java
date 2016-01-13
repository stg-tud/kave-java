package cc.kave.commons.model.ssts.references;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.names.PropertyName;

public interface IPropertyReference extends IAssignableReference, IMemberReference {

	@NonNull
	PropertyName getPropertyName();
}
