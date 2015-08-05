package cc.kave.commons.model.names.csharp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.BundleName;
import cc.kave.commons.model.names.NamespaceName;
import cc.kave.commons.model.names.TypeName;

public class CsTypeName extends CsName implements TypeName {
	private static final Map<String, CsTypeName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final TypeName UNKNOWN_NAME = newTypeName("?");

	public static TypeName newTypeName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			CsTypeName newName;
			if (identifier.startsWith("d:")) {
				newName = new CsDelegateTypeName(identifier);
			} else if (CsUnknownTypeName.IsUnknownTypeIdentifier(identifier)) {
				newName = new CsUnknownTypeName(identifier);
			} else {
				newName = new CsTypeName(identifier);
			}
			nameRegistry.put(identifier, newName);
		}
		return nameRegistry.get(identifier);
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
		int i = identifier.indexOf("->", 0);
		;
		List<TypeName> list = new ArrayList<TypeName>();
		while (i < identifier.indexOf("]]") && i >= 0) {
			list.add(CsTypeName.newTypeName(identifier.substring(i + 3, identifier.indexOf("]", i + 3))));
			i = identifier.indexOf("->", ++i);
		}
		return list;
	}

	@Override
	public BundleName getAssembly() {
		int i;

		if (!identifier.contains(",")) {
			return CsAssemblyName.newAssemblyName(identifier);
		} else if (isGenericEntity()) {
			i = identifier.indexOf("]]") + 4;
		} else {
			i = identifier.indexOf(",") + 2;
		}

		return CsAssemblyName.newAssemblyName(identifier.substring(i));
	}

	@Override
	public NamespaceName getNamespace() {
		int i;
		String identifier = this.identifier;

		if (isGenericEntity()) {
			i = identifier.indexOf("`");
		} else {
			i = identifier.indexOf(",");
		}

		if (i == -1) {
			return CsNamespaceName.newNamespaceName(identifier);
		}

		identifier = identifier.substring(0, i);
		i = identifier.lastIndexOf(".");

		if (i == -1) {
			return CsNamespaceName.newNamespaceName(identifier);
		} else {
			return CsNamespaceName.newNamespaceName(identifier.substring(0, i));
		}
	}

	@Override
	public TypeName getDeclaringType() {
		if (!isNestedType()) {
			return null;
		}

		String fullName = getFullName();
		int endOfDeclaringType = fullName.lastIndexOf("+");
		String declaringTypeName = fullName.substring(0, endOfDeclaringType);

		if (declaringTypeName.indexOf("`") > 1 && hasTypeParameters()) {
			int startIndex = 0;
			int numberOfParameters = 0;

			while ((startIndex = declaringTypeName.indexOf("+", startIndex) + 1) > 0) {
				int endIndex = declaringTypeName.indexOf("+", startIndex);
				if (endIndex > -1) {
					numberOfParameters += Integer
							.parseInt(declaringTypeName.substring(startIndex, endIndex - startIndex));
				} else {
					numberOfParameters += Integer.parseInt(declaringTypeName.substring(startIndex));
				}
				List<TypeName> typeParameters = getTypeParameters();
				declaringTypeName += "[["
						+ String.join("],[", typeParameters.toArray(new CharSequence[typeParameters.size()])) + "]]";
			}
			return CsTypeName.newTypeName(declaringTypeName + ", " + getAssembly());
		}
		return null;
	}

	@Override
	public String getFullName() {
		int i = identifier.indexOf("]]");
		if (i < 0) {
			i = identifier.indexOf(",");
			if (i < 0) {
				return identifier;
			}
		} else {
			i += 2;
		}
		return identifier.substring(0, i);
	}

	@Override
	public String getName() {
		String name = getFullName();
		if (isGenericEntity()) {
			name = name.substring(0, name.indexOf("[[") - 2);
		}
		int i = name.lastIndexOf(".") + 1;
		return name.substring(i, name.length());
	}

	public String getRawFullName() {

		String fullName = getFullName();
		int i = fullName.indexOf("[[");

		if (i == -1) {
			i = fullName.indexOf(", ");
		}
		return i < 0 ? fullName : fullName.substring(0, i);

	}

	// TODO: wrong implementation?
	@Override
	public boolean isUnknownType() {
		return this.equals(CsUnknownTypeName.getInstance());
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
	public String TypeParameterShortName() {
		return null;
	}

	@Override
	public TypeName TypeParameterType() {
		return null;
	}
}
