package cc.kave.commons.model.names.csharp;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.AssemblyName;
import cc.kave.commons.model.names.AssemblyVersion;

public class CsAssemblyName extends CsName implements AssemblyName {
	private static final Map<String, CsAssemblyName> nameRegistry = new MapMaker()
			.weakValues().makeMap();

	public static AssemblyName newAssemblyName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new CsAssemblyName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private CsAssemblyName(String identifier) {
		super(identifier);
	}

	@Override
	public AssemblyVersion getAssemblyVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException();
	}
}
