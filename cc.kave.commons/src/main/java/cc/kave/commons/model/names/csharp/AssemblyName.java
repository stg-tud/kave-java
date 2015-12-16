package cc.kave.commons.model.names.csharp;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.IBundleName;
import cc.kave.commons.model.names.IBundleVersion;

public class AssemblyName extends Name implements IBundleName {
	private static final Map<String, AssemblyName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final IBundleName UNKNOWN_NAME = newAssemblyName(UNKNOWN_NAME_IDENTIFIER);

	public static IBundleName newAssemblyName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new AssemblyName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private AssemblyName(String identifier) {
		super(identifier);
	}

	@Override
	public IBundleVersion getVersion() {
		if (isUnknown()) {
			return AssemblyVersion.UNKNOWN_NAME;
		} else if (!identifier.contains(",")) {
			return AssemblyVersion.UNKNOWN_NAME;
		} else {
			return AssemblyVersion
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

	public static IBundleName getUnknownName() {
		return AssemblyName.newAssemblyName(UNKNOWN_NAME_IDENTIFIER);
	}

	public boolean isUnknown() {
		return this.equals(getUnknownName());
	}
}
