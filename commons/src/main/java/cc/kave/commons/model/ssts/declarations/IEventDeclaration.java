package cc.kave.commons.model.ssts.declarations;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.EventName;
import cc.kave.commons.model.ssts.IMemberDeclaration;

public interface IEventDeclaration extends IMemberDeclaration {

	@Nonnull
	EventName getName();

}
