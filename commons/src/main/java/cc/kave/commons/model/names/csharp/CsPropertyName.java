package cc.kave.commons.model.names.csharp;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.names.TypeName;

public class CsPropertyName extends CsMemberName implements PropertyName {
	private static final Map<String, CsPropertyName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final PropertyName UNKNOWN_NAME = newPropertyName("[?] [?].???");
	public final String SETTER_MODIFIER = "set";
	public final String GETTER_MODIFIER = "get";

	public static PropertyName newPropertyName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new CsPropertyName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	public boolean isUnknown() {
		return this.equals(UNKNOWN_NAME);
	}

	private CsPropertyName(String identifier) {
		super(identifier);
	}

	@Override
	public boolean hasSetter() {
		return getModifiers().contains(SETTER_MODIFIER);
	}

	@Override
	public boolean hasGetter() {
		return getModifiers().contains(GETTER_MODIFIER);
	}

	@Override
	public TypeName getValueType() {
		return CsTypeName.UNKNOWN_NAME;
	}
}
