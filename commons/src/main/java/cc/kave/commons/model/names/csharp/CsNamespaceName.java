package cc.kave.commons.model.names.csharp;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.NamespaceName;

public class CsNamespaceName extends CsName implements NamespaceName {
	private static final Map<String, CsNamespaceName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final String GLOBAL_NAMESPACE_IDENTIFIER = "";
	public static final NamespaceName a = CsNamespaceName.newNamespaceName(GLOBAL_NAMESPACE_IDENTIFIER);
	public static final NamespaceName UNKNOWN_NAME = newNamespaceName(UNKNOWN_NAME_IDENTIFIER);

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
		if (isGlobalNamespace()) {
			return null;
		}

		int i = identifier.lastIndexOf(".");
		if (i == -1) {
			return CsNamespaceName.newNamespaceName(GLOBAL_NAMESPACE_IDENTIFIER);
		} else {
			return CsNamespaceName.newNamespaceName(identifier.substring(0, i));
		}
	}

	@Override
	public String getName() {
		return identifier.substring(identifier.lastIndexOf(".") + 1);
	}

	@Override
	public boolean isGlobalNamespace() {
		return identifier.equals(GLOBAL_NAMESPACE_IDENTIFIER);
	}

	public static NamespaceName getGlobalNamespace() {
		return CsNamespaceName.newNamespaceName(GLOBAL_NAMESPACE_IDENTIFIER);
	}

	public static NamespaceName getUnknownName() {
		return CsNamespaceName.newNamespaceName(CsName.UNKNOWN_NAME_IDENTIFIER);
	}
}
