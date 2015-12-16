package cc.kave.commons.model.ssts.declarations;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.ssts.IMemberDeclaration;

public interface IFieldDeclaration extends IMemberDeclaration {

	@Nonnull
	IFieldName getName();
}
