package cc.kave.commons.model.names.csharp;

import java.util.Map;

import cc.kave.commons.model.names.BundleName;
import cc.kave.commons.model.names.BundleVersion;

import com.google.common.collect.MapMaker;

public class CsAssemblyName extends CsName implements BundleName {
	private static final Map<String, CsAssemblyName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final BundleName UNKNOWN_NAME = newAssemblyName(UNKNOWN_NAME_IDENTIFIER);

	public static BundleName newAssemblyName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new CsAssemblyName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private CsAssemblyName(String identifier) {
		super(identifier);
	}

	@Override
	public BundleVersion getVersion() {
		return CsAssemblyVersion.newAssemblyVersion(identifier.substring(identifier.indexOf(",") + 1,
				identifier.length()));
	}

	@Override
	public String getName() {
		return identifier.substring(0, identifier.indexOf(","));
	}

	public static BundleName unknownName() {
		return CsAssemblyName.newAssemblyName(unknownNameIdentifier);
	}

	public boolean isUnknown() {
		return this.equals(unknownName());
	}
}
