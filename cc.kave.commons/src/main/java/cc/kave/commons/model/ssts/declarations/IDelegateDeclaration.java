package cc.kave.commons.model.ssts.declarations;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.IMemberDeclaration;

public interface IDelegateDeclaration extends IMemberDeclaration {

	@Nonnull
	TypeName getName();

}
