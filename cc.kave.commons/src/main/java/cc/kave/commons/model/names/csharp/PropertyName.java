package cc.kave.commons.model.names.csharp;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.IPropertyName;

public class PropertyName extends MemberName implements IPropertyName {
	private static final Map<String, PropertyName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final IPropertyName UNKNOWN_NAME = newPropertyName("[?] [?].???");
	public final String SETTER_MODIFIER = "set";
	public final String GETTER_MODIFIER = "get";

	public static IPropertyName newPropertyName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new PropertyName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	public boolean isUnknown() {
		return this.equals(UNKNOWN_NAME);
	}

	private PropertyName(String identifier) {
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
}
