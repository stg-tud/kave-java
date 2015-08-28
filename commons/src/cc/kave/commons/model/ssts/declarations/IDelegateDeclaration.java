package cc.kave.commons.model.ssts.declarations;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.IMemberDeclaration;

public interface IDelegateDeclaration extends IMemberDeclaration {

	@NonNull
	TypeName getName();

}
