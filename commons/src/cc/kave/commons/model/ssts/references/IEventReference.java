package cc.kave.commons.model.ssts.references;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.names.EventName;

public interface IEventReference extends IAssignableReference, IMemberReference {

	@NonNull
	EventName getEventName();
}
