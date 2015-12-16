package cc.kave.commons.model.names.csharp;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.ILocalVariableName;
import cc.kave.commons.model.names.ITypeName;

public class LocalVariableName extends Name implements ILocalVariableName {
	private static final Map<String, LocalVariableName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final ILocalVariableName UNKNOWN_NAME = newLocalVariableName("[?] ???");

	public static ILocalVariableName newLocalVariableName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new LocalVariableName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private LocalVariableName(String identifier) {
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
	public ITypeName getValueType() {
		int lengthOfTypeIdentifier = identifier.lastIndexOf(']') - 1;
		return TypeName.newTypeName(identifier.substring(1, lengthOfTypeIdentifier));

	}
}
