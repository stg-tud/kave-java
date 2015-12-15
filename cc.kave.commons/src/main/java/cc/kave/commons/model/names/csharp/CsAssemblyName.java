package cc.kave.commons.model.names.csharp;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.BundleName;
import cc.kave.commons.model.names.BundleVersion;

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
		if (isUnknown()) {
			return CsAssemblyVersion.UNKNOWN_NAME;
		} else if (!identifier.contains(",")) {
			return CsAssemblyVersion.UNKNOWN_NAME;
		} else {
			return CsAssemblyVersion
					.newAssemblyVersion(identifier.substring(identifier.indexOf(",") + 2, identifier.length()));
		}

	}

	@Override
	public String getName() {
		if (isUnknown()) {
			return identifier;
		} else if (!identifier.contains(",")) {
			return identifier;
		} else {
			return identifier.substring(0, identifier.indexOf(",")).trim();
		}
	}

	public static BundleName getUnknownName() {
		return CsAssemblyName.newAssemblyName(UNKNOWN_NAME_IDENTIFIER);
	}

	public boolean isUnknown() {
		return this.equals(getUnknownName());
	}
}
