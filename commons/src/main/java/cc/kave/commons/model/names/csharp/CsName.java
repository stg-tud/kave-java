package cc.kave.commons.model.names.csharp;

import java.util.Map;

import cc.kave.commons.model.names.Name;

import com.google.common.collect.MapMaker;

public class CsName implements Name {

	protected final static String UNKNOWN_NAME_IDENTIFIER = "???";

	private static final Map<String, CsName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final Name UNKNOWN_NAME = newName(UNKNOWN_NAME_IDENTIFIER);

	public static Name newName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new CsName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	protected String identifier;

	protected CsName(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public final String getIdentifier() {
		return identifier;
	}

	@Override
	public boolean isUnknown() {
		throw new UnsupportedOperationException();
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CsName other = (CsName) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}

	@Override
	public final String toString() {
		return identifier;
	}
}
