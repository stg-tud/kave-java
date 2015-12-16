package cc.kave.commons.model.names.csharp;

import java.util.List;

import cc.kave.commons.model.names.IBundleName;
import cc.kave.commons.model.names.IDelegateTypeName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.INamespaceName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;

public class DelegateTypeName extends TypeName implements IDelegateTypeName {

	public static IDelegateTypeName UNKNOWN_NAME = newDelegateTypeName("d:[?] [?].()");

	static final String PREFIX = "d:";

	public static IDelegateTypeName newDelegateTypeName(String identifier) {
		return (IDelegateTypeName) TypeName.newTypeName(identifier);
	}

	public static String fixLegacyDelegateNames(String identifier) {
		// fix legacy delegate names
		if (!identifier.contains("(")) {
			identifier = String.format("%s[%s] [%s].()", PREFIX, UnknownTypeName.IDENTIFIER,
					identifier.substring(PREFIX.length()));
		}
		return identifier;
	}

	/* package */ DelegateTypeName(String identifier) {
		super(identifier);
	}

	static boolean isDelegateTypeIdentifier(String identifier) {
		return identifier.startsWith(PREFIX);
	}

	private IMethodName getDelegateMethod() {
		return MethodName.newMethodName(identifier.substring(PREFIX.length()));
	}

	public ITypeName getDelegateType() {
		return getDelegateMethod().getDeclaringType();
	}

	@Override
	public String getSignature() {
		int endOfValueType = CsNameUtils.endOfNextTypeIdentifier(identifier, 2);
		int endOfDelegateType = CsNameUtils.endOfNextTypeIdentifier(identifier, endOfValueType);
		return getName() + identifier.substring(endOfDelegateType + 1);
	}

	@Override
	public List<IParameterName> getParameters() {
		return getDelegateMethod().getParameters();
	}

	@Override
	public boolean hasParameters() {
		return getDelegateMethod().hasParameters();
	}

	@Override
	public ITypeName getReturnType() {
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
	public ITypeName getArrayBaseType() {
		return null;
	}

	public ITypeName deriveArrayTypeName(int rank) {
		return ArrayTypeName.from(this, rank);
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
	public ITypeName getTypeParameterType() {
		return null;
	}

	@Override
	public IBundleName getAssembly() {
		return getDelegateType().getAssembly();
	}

	@Override
	public String getFullName() {
		return getDelegateMethod().getDeclaringType().getFullName();
	}

	@Override
	public INamespaceName getNamespace() {
		return getDelegateType().getNamespace();
	}

	@Override
	public ITypeName getDeclaringType() {
		return getDelegateType().getDeclaringType();
	}
}
