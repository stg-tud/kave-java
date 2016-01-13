package cc.kave.commons.model.names.csharp;

import cc.kave.commons.model.names.BundleName;
import cc.kave.commons.model.names.NamespaceName;
import cc.kave.commons.model.names.TypeName;

public class CsUnknownTypeName extends CsTypeName {
	public static final String IDENTIFIER = "?";

	public static TypeName getInstance() {
		return CsTypeName.newTypeName(IDENTIFIER);
	}

	public static boolean isUnknownTypeIdentifier(String identifier) {
		return identifier.equals(IDENTIFIER);
	}

	protected CsUnknownTypeName(String identifier) {
		super(identifier);
	}

	static TypeName get(String identifier) {
		return CsTypeName.newTypeName(identifier);
	}

	@Override
	public boolean isUnknownType() {
		return true;
	}

	@Override
	public BundleName getAssembly() {
		return CsAssemblyName.getUnknownName();
	}

	@Override
	public NamespaceName getNamespace() {
		return CsNamespaceName.getUnknownName();
	}
}