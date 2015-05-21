package cc.kave.commons.model.names.csharp;

import java.util.Map;

import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.names.TypeName;

import com.google.common.collect.MapMaker;

public class CsPropertyName extends CsMemberName implements PropertyName {
	private static final Map<String, CsPropertyName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final PropertyName UNKNOWN_NAME = newPropertyName("[?] [?].???");

	public static PropertyName newPropertyName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new CsPropertyName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private CsPropertyName(String identifier) {
		super(identifier);
	}

	@Override
	public boolean hasSetter() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasGetter() {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeName getValueType() {
		throw new UnsupportedOperationException();
	}
}
