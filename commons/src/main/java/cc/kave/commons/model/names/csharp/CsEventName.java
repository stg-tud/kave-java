package cc.kave.commons.model.names.csharp;

import java.util.Map;

import cc.kave.commons.model.names.EventName;
import cc.kave.commons.model.names.TypeName;

import com.google.common.collect.MapMaker;

public class CsEventName extends CsMemberName implements EventName {
	private static final Map<String, CsEventName> nameRegistry = new MapMaker()
			.weakValues().makeMap();

	public static EventName newEventName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new CsEventName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private CsEventName(String identifier) {
		super(identifier);
	}

	@Override
	public TypeName getHandlerType() {
		throw new UnsupportedOperationException();
	}
}
