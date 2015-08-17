package cc.kave.commons.model.names.csharp;

import java.util.List;

import cc.kave.commons.model.names.BundleName;
import cc.kave.commons.model.names.DelegateTypeName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.NamespaceName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;

public class CsDelegateTypeName extends CsTypeName implements DelegateTypeName {

	public static DelegateTypeName UNKNOWN_NAME = newDelegateTypeName("d:[?] [?].()");

	static final String PREFIX = "d:";

	public static DelegateTypeName newDelegateTypeName(String identifier) {
		return (DelegateTypeName) CsTypeName.newTypeName(identifier);
	}

	public static String fixLegacyDelegateNames(String identifier) {
		// fix legacy delegate names
		if (!identifier.contains("(")) {
			identifier = String.format("%s[%s] [%s].()", PREFIX, CsUnknownTypeName.IDENTIFIER,
					identifier.substring(PREFIX.length()));
		}
		return identifier;
	}

	/* package */ CsDelegateTypeName(String identifier) {
		super(identifier);
	}

	static boolean isDelegateTypeIdentifier(String identifier) {
		return identifier.startsWith(PREFIX);
	}

	private MethodName getDelegateMethod() {
		return CsMethodName.newMethodName(identifier.substring(PREFIX.length()));
	}

	public TypeName getDelegateType() {
		return getDelegateMethod().getDeclaringType();
	}

	@Override
	public String getSignature() {
		// int start = identifier.indexOf("[");
		// start = identifier.indexOf("[",
		// CsNameUtils.getClosingBracketIndex(identifier, start));
		// start = CsNameUtils.getClosingBracketIndex(identifier, start);
		// return getName() + identifier.substring(start + 1);
		int endOfValueType = CsNameUtils.endOfNextTypeIdentifier(identifier, 2);
		int endOfDelegateType = CsNameUtils.endOfNextTypeIdentifier(identifier, endOfValueType);
		return getName() + identifier.substring(endOfDelegateType + 1);
	}

	@Override
	public List<ParameterName> getParameters() {
		return getDelegateMethod().getParameters();
	}

	@Override
	public boolean hasParameters() {
		return getDelegateMethod().hasParameters();
	}

	@Override
	public TypeName getReturnType() {
		return getDelegateMethod().getReturnType();
	}

	@Override
	public boolean isInterfaceType() {
		return false;
	}

	@Override
	public boolean isDelegateType() {
		return true;
	}

	@Override
	public boolean isNestedType() {
		return getDelegateType().isNestedType();
	}

	@Override
	public boolean isArrayType() {
		return false;
	}

	@Override
	public TypeName getArrayBaseType() {
		return null;
	}

	public TypeName deriveArrayTypeName(int rank) {
		return CsArrayTypeName.from(this, rank);
	}

	@Override
	public boolean isTypeParameter() {
		return false;
	}

	@Override
	public String getTypeParameterShortName() {
		return null;
	}

	@Override
	public TypeName getTypeParameterType() {
		return null;
	}

	@Override
	public BundleName getAssembly() {
		return getDelegateType().getAssembly();
	}

	@Override
	public String getFullName() {
		return getDelegateMethod().getDeclaringType().getFullName();
	}

	@Override
	public NamespaceName getNamespace() {
		return getDelegateType().getNamespace();
	}
}
