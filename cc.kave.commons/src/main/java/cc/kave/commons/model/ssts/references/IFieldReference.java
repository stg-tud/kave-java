package cc.kave.commons.model.ssts.references;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.FieldName;

public interface IFieldReference extends IAssignableReference, IMemberReference {

	@Nonnull
	FieldName getFieldName();
}
