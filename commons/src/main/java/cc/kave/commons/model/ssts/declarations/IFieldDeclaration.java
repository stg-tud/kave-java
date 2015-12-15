package cc.kave.commons.model.ssts.declarations;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.ssts.IMemberDeclaration;

public interface IFieldDeclaration extends IMemberDeclaration {

	@NonNull
	FieldName getName();
}
