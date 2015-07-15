package cc.kave.commons.model.names.csharp;

import cc.kave.commons.model.names.TypeName;

public class CsUnknownTypeName extends CsTypeName {

	protected CsUnknownTypeName(String identifier) {
		super(identifier);
	}

	public static TypeName newTypeName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			CsUnknownTypeName newName = new CsUnknownTypeName(identifier);

			nameRegistry.put(identifier, newName);
		}
		return nameRegistry.get(identifier);
	}

}
