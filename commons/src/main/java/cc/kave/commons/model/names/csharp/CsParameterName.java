package cc.kave.commons.model.names.csharp;

import java.util.Map;

import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;

import com.google.common.collect.MapMaker;

public class CsParameterName extends CsName implements ParameterName {
	private static final Map<String, CsParameterName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final ParameterName UNKNOWN_NAME = newParameterName("[?] ???");

	public static ParameterName newParameterName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new CsParameterName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private CsParameterName(String identifier) {
		super(identifier);
	}

	@Override
	public TypeName getValueType() {
		String typeName = identifier.substring(1, identifier.indexOf("]"));
		return CsTypeName.newTypeName(typeName);
	}

	@Override
	public String getName() {
		return identifier.substring(identifier.indexOf("] ") + 2, identifier.length());
	}

	@Override
	public boolean isPassedByReference() {
		return false;
	}

	@Override
	public boolean isOutput() {
		return false;
	}

	@Override
	public boolean isParameterArray() {
		return false;
	}

	@Override
	public boolean isOptional() {
		return false;
	}
}
