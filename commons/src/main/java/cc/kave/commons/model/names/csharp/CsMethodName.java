package cc.kave.commons.model.names.csharp;

import java.util.List;
import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;

public class CsMethodName extends CsMemberName implements MethodName {
	private static final Map<String, CsMethodName> nameRegistry = new MapMaker()
			.weakValues().makeMap();

	public static MethodName newMethodName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new CsMethodName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private CsMethodName(String identifier) {
		super(identifier);
	}

	@Override
	public String getSignature() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<ParameterName> getParameters() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasParameters() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isConstructor() {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeName getReturnType() {
		throw new UnsupportedOperationException();
	}
}
