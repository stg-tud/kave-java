package cc.kave.commons.model.names.csharp;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.LocalVariableName;
import cc.kave.commons.model.names.TypeName;

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
	public boolean isUnknown() {
		return this.equals(UNKNOWN_NAME);
	}

	@Override
	public String getName() {
		int indexOfName = identifier.lastIndexOf(']') + 2;
		return identifier.substring(indexOfName);
	}

	@Override
	public TypeName getValueType() {
		int lengthOfTypeIdentifier = identifier.lastIndexOf(']') - 1;
		return CsTypeName.newTypeName(identifier.substring(1, lengthOfTypeIdentifier));

	}
}
