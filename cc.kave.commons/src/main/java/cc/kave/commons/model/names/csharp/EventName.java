package cc.kave.commons.model.names.csharp;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.IEventName;
import cc.kave.commons.model.names.ITypeName;

public class EventName extends MemberName implements IEventName {
	private static final Map<String, EventName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final IEventName UNKNOWN_NAME = newEventName("[?] [?].???");

	public static IEventName newEventName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new EventName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private EventName(String identifier) {
		super(identifier);
	}

	@Override
	public ITypeName getHandlerType() {
		return getValueType();
	}

	public boolean isUnknown() {
		return this.equals(UNKNOWN_NAME);
	}
}
