package cc.kave.commons.model.names.csharp;

import java.util.Map;

import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.TypeName;

import com.google.common.collect.MapMaker;

public class CsFieldName extends CsMemberName implements FieldName {
	private static final Map<String, CsFieldName> nameRegistry = new MapMaker()
			.weakValues().makeMap();

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
	public TypeName getValueType() {
		throw new UnsupportedOperationException();
	}
}
