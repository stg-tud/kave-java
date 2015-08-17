package cc.kave.commons.model.names.csharp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.BundleName;
import cc.kave.commons.model.names.NamespaceName;
import cc.kave.commons.model.names.TypeName;

public class CsTypeName extends CsName implements TypeName {
	private static final Map<String, TypeName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final TypeName UNKNOWN_NAME = newTypeName("?");

	public static TypeName newTypeName(String identifier) {
		identifier = fixLegacyNameFormat(identifier);

		if (!nameRegistry.containsKey(identifier)) {
			TypeName newName;
			newName = createTypeName(identifier);
			nameRegistry.put(identifier, (TypeName) newName);
		}

		return nameRegistry.get(identifier);
	}

	private static TypeName createTypeName(String identifier) {
		// checked first, because it's a special case
		if (CsUnknownTypeName.isUnknownTypeIdentifier(identifier)) {
			return new CsUnknownTypeName(identifier);
		}
		// checked second, since type parameters can have any kind of type
		if (CsTypeParameterName.isTypeParameterIdentifier(identifier)) {
			return new CsTypeParameterName(identifier);
		}
		// checked third, since the array's value type can have any kind of type
		if (CsArrayTypeName.isArrayTypeIdentifier(identifier)) {
			return new CsArrayTypeName(identifier);
		}
		if (CsInterfaceTypeName.isInterfaceTypeIdentifier(identifier)) {
			return new CsInterfaceTypeName(identifier);
		}
		if (CsStructTypeName.isStructTypeIdentifier(identifier)) {
			return new CsStructTypeName(identifier);
		}
		if (CsEnumTypeName.isEnumTypeIdentifier(identifier)) {
			return new CsEnumTypeName(identifier);
		}
		if (CsDelegateTypeName.isDelegateTypeIdentifier(identifier)) {
			return new CsDelegateTypeName(identifier);
		}
		return new CsTypeName(identifier);
	}

	private static String fixLegacyNameFormat(String identifier) {
		if (CsDelegateTypeName.isDelegateTypeIdentifier(identifier)) {
			return CsDelegateTypeName.fixLegacyDelegateNames(identifier);
		}
		return identifier;
	}

	protected CsTypeName(String identifier) {
		super(identifier);
	}

	@Override
	public boolean isGenericEntity() {
		return identifier.contains("[[");
	}

	@Override
	public boolean hasTypeParameters() {
		return identifier.contains("`");
	}

	@Override
	public List<TypeName> getTypeParameters() {
		return hasTypeParameters() ? CsGenericNameUtils.parseTypeParameters(getFullName()) : new ArrayList<TypeName>();
	}

	@Override
	public BundleName getAssembly() {
		int endOfTypeName = getLengthOfTypeName(identifier);
		String assemblyIdentifier = identifier.substring(endOfTypeName).trim();
		// TODO: (new char[] { ' ', ',' });
		return CsAssemblyName.newAssemblyName(assemblyIdentifier);
	}

	@Override
	public NamespaceName getNamespace() {
		String id = getRawFullName();
		int endIndexOfNamespaceIdentifier = id.lastIndexOf('.');
		return endIndexOfNamespaceIdentifier < 0 ? CsNamespaceName.getGlobalNamespace()
				: CsNamespaceName.newNamespaceName(id.substring(0, endIndexOfNamespaceIdentifier));
	}

	// TODO: Siehe C# implementation
	@Override
	public TypeName getDeclaringType() {
		// if (!isNestedType()) {
		// return null;
		// }
		//
		// String fullName = getFullName();
		//
		// if (hasTypeParameters()) {
		// fullName = fullName.substring(0, fullName.indexOf('`'));
		// }
		//
		// int endOfDeclaringTypeName = fullName.lastIndexOf("+");
		//
		// if (endOfDeclaringTypeName == -1) {
		// return UNKNOWN_NAME;
		// }
		//
		// String declaringTypeName = fullName.substring(0,
		// endOfDeclaringTypeName);
		//
		// if (declaringTypeName.indexOf("`") > -1 && hasTypeParameters()) {
		// int startIndex = 0;
		// int numberOfParameters = 0;
		//
		// while ((startIndex = declaringTypeName.indexOf("+", startIndex) + 1)
		// > 0) {
		// int endIndex = declaringTypeName.indexOf("+", startIndex);
		//
		// if (endIndex > -1) {
		// numberOfParameters += Integer
		// .parseInt(declaringTypeName.substring(startIndex, endIndex -
		// startIndex));
		// } else {
		// numberOfParameters +=
		// Integer.parseInt(declaringTypeName.substring(startIndex));
		// }
		//
		// List<TypeName> typeParameters = getTypeParameters();
		// declaringTypeName += "[["
		// + String.join("],[", typeParameters.toArray(new
		// CharSequence[typeParameters.size()])) + "]]";
		// }
		// return CsTypeName.newTypeName(declaringTypeName + ", " +
		// getAssembly());
		// }
		// return null;

		if (!isNestedType()) {
			return null;
		}

		String fullName = getFullName();
		if (hasTypeParameters()) {
			fullName = takeUntilChar(fullName, new char[] { '[', ',' });
		}
		int endOfDeclaringTypeName = fullName.lastIndexOf('+');
		if (endOfDeclaringTypeName == -1) {
			return UNKNOWN_NAME;
		}

		String declaringTypeName = fullName.substring(0, endOfDeclaringTypeName);
		if (declaringTypeName.indexOf('`') > -1 && hasTypeParameters()) {
			int startIndex = 0;
			int numberOfParameters = 0;
			while ((startIndex = declaringTypeName.indexOf('`', startIndex) + 1) > 0) {
				int endIndex = declaringTypeName.indexOf('+', startIndex);
				if (endIndex > -1) {
					numberOfParameters += Integer.parseInt(declaringTypeName.substring(startIndex, endIndex));
				} else {
					numberOfParameters += Integer.parseInt(declaringTypeName.substring(startIndex));
				}
			}

			List<TypeName> outerTypeParameters = getTypeParameters().subList(0, numberOfParameters);

			declaringTypeName += "[[";
			for (TypeName typeName : outerTypeParameters) {
				declaringTypeName += "],[" + typeName.getIdentifier();
			}
		}
		return newTypeName(declaringTypeName + ", " + getAssembly());
	}

	private String takeUntilChar(String fullName, char[] stopChars) {
		int i = 0;
		for (char c : getFullName().toCharArray()) {
			for (int j = 0; j < stopChars.length; j++) {
				if (stopChars[j] == c) {
					return fullName.substring(0, i);
				}
			}
			i++;
		}

		return fullName.substring(0, i);
	}

	@Override
	public String getFullName() {
		int length = getLengthOfTypeName(identifier);
		return identifier.substring(0, length);
	}

	protected static int getLengthOfTypeName(String identifier) {
		if (CsUnknownTypeName.isUnknownTypeIdentifier(identifier)) {
			return identifier.length();
		}
		int length = identifier.lastIndexOf(']') + 1;
		if (length > 0) {
			return length;
		}
		return identifier.indexOf(',');
	}

	@Override
	public String getName() {
		String rawFullName = getRawFullName();
		int endOfOutTypeName = rawFullName.lastIndexOf('+');

		if (endOfOutTypeName > -1) {
			rawFullName = rawFullName.substring(endOfOutTypeName + 1);
		}

		int endOfTypeName = rawFullName.lastIndexOf('`');

		if (endOfTypeName > -1) {
			rawFullName = rawFullName.substring(0, endOfTypeName);
		}

		int startIndexOfSimpleName = rawFullName.lastIndexOf('.');
		return rawFullName.substring(startIndexOfSimpleName + 1);
	}

	public String getRawFullName() {
		String fullName = getFullName();
		int indexOfGenericList = fullName.indexOf("[[");

		if (indexOfGenericList < 0) {
			indexOfGenericList = fullName.indexOf(", ");
		}
		return indexOfGenericList < 0 ? fullName : fullName.substring(0, indexOfGenericList);
	}

	@Override
	public boolean isUnknownType() {
		return false;
	}

	@Override
	public boolean isVoidType() {
		return false;
	}

	@Override
	public boolean isValueType() {
		return isStructType() || isEnumType() || isVoidType();
	}

	@Override
	public boolean isSimpleType() {
		return false;
	}

	@Override
	public boolean isEnumType() {
		return false;
	}

	@Override
	public boolean isStructType() {
		return false;
	}

	@Override
	public boolean isNullableType() {
		return false;
	}

	@Override
	public boolean isReferenceType() {
		return isClassType() || isInterfaceType() || isArrayType() || isDelegateType();
	}

	@Override
	public boolean isClassType() {
		return !isValueType() && !isInterfaceType() && !isArrayType() && !isDelegateType() && !isUnknownType();
	}

	@Override
	public boolean isInterfaceType() {
		return false;
	}

	@Override
	public boolean isDelegateType() {
		return false;
	}

	@Override
	public boolean isNestedType() {
		return getRawFullName().contains("+");
	}

	@Override
	public boolean isArrayType() {
		return false;
	}

	@Override
	public TypeName getArrayBaseType() {
		return null;
	}

	@Override
	public TypeName DeriveArrayTypeName(int rank) {
		return null;
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
}
