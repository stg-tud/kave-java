package cc.kave.commons.model.ssts.declarations;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.names.EventName;
import cc.kave.commons.model.ssts.IMemberDeclaration;

public interface IEventDeclaration extends IMemberDeclaration {

	@NonNull
	EventName getName();

}
