package cc.kave.commons.model.ssts.references;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.IFieldName;

public interface IFieldReference extends IAssignableReference, IMemberReference {

	@Nonnull
	IFieldName getFieldName();
}
