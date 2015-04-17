package cc.kave.commons.model.names.csharp;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;

public class CsParameterName extends CsName implements ParameterName {
	private static final Map<String, CsParameterName> nameRegistry = new MapMaker()
			.weakValues().makeMap();

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
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPassedByReference() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isOutput() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isParameterArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isOptional() {
		throw new UnsupportedOperationException();
	}
}
