package cc.kave.commons.model.ssts.references;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.names.FieldName;

public interface IFieldReference extends IAssignableReference, IMemberReference {

	@NonNull
	FieldName getFieldName();
}
