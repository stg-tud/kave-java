package cc.kave.commons.model.names.csharp;

import java.util.Map;

import cc.kave.commons.model.names.LocalVariableName;
import cc.kave.commons.model.names.TypeName;

import com.google.common.collect.MapMaker;

public class CsLocalVariableName extends CsName implements LocalVariableName {
	private static final Map<String, CsLocalVariableName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final LocalVariableName UNKNOWN_NAME = newLocalVariableName("[?] ???");

	public static LocalVariableName newLocalVariableName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new CsLocalVariableName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private CsLocalVariableName(String identifier) {
		super(identifier);
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeName getValueType() {
		throw new UnsupportedOperationException();
	}
}
