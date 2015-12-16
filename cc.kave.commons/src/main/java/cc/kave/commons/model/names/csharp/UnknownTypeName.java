package cc.kave.commons.model.names.csharp;

import cc.kave.commons.model.names.IBundleName;
import cc.kave.commons.model.names.INamespaceName;
import cc.kave.commons.model.names.ITypeName;

public class UnknownTypeName extends TypeName {
	public static final String IDENTIFIER = "?";

	public static ITypeName getInstance() {
		return TypeName.newTypeName(IDENTIFIER);
	}

	public static boolean isUnknownTypeIdentifier(String identifier) {
		return identifier.equals(IDENTIFIER);
	}

	protected UnknownTypeName(String identifier) {
		super(identifier);
	}

	static ITypeName get(String identifier) {
		return TypeName.newTypeName(identifier);
	}

	@Override
	public boolean isUnknownType() {
		return true;
	}

	@Override
	public IBundleName getAssembly() {
		return AssemblyName.getUnknownName();
	}

	@Override
	public INamespaceName getNamespace() {
		return NamespaceName.getUnknownName();
	}
}