package cc.kave.commons.model.names.csharp;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.IFieldName;

public class FieldName extends MemberName implements IFieldName {
	private static final Map<String, FieldName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final IFieldName UNKNOWN_NAME = newFieldName("[?] [?].???");

	public static IFieldName newFieldName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new FieldName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private FieldName(String identifier) {
		super(identifier);
	}

	@Override
	public boolean isUnknown() {
		return this.equals(UNKNOWN_NAME);
	}
}
