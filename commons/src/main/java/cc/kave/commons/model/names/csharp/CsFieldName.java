package cc.kave.commons.model.names.csharp;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.TypeName;

import cc.kave.commons.model.names.FieldName;

public class CsFieldName extends CsMemberName implements FieldName {
	private static final Map<String, CsFieldName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final FieldName UNKNOWN_NAME = newFieldName("[?] [?].???");

	public static FieldName newFieldName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new CsFieldName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private CsFieldName(String identifier) {
		super(identifier);
	}

	@Override
	public boolean isUnknown() {
		return this.equals(UNKNOWN_NAME);
	}
	
	@Override
	public TypeName getValueType() {
		return CsTypeName.UNKNOWN_NAME;
	}
}
