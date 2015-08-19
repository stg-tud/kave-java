package cc.kave.commons.model.ssts.references;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.EventName;

public interface IEventReference extends IAssignableReference, IMemberReference {

	@Nonnull
	EventName getEventName();
}
