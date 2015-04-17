package cc.kave.commons.model.names.csharp;

import java.util.Map;

import cc.kave.commons.model.names.NamespaceName;

import com.google.common.collect.MapMaker;

public class CsNamespaceName extends CsName implements NamespaceName {
	private static final Map<String, CsNamespaceName> nameRegistry = new MapMaker()
			.weakValues().makeMap();

	public static NamespaceName newNamespaceName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new CsNamespaceName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private CsNamespaceName(String identifier) {
		super(identifier);
	}

	@Override
	public NamespaceName getParentNamespace() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isGlobalNamespace() {
		throw new UnsupportedOperationException();
	}
}
