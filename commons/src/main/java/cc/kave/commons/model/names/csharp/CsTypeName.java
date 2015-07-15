package cc.kave.commons.model.names.csharp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.kave.commons.model.names.BundleName;
import cc.kave.commons.model.names.NamespaceName;
import cc.kave.commons.model.names.TypeName;

import com.google.common.collect.MapMaker;

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
		if (isGenericEntity()) {
			i = identifier.indexOf("]]") + 4;
		} else {
			i = identifier.indexOf(",") + 2;
		}

		return CsAssemblyName.newAssemblyName(identifier.substring(i, identifier.lastIndexOf(",")));
	}

	@Override
	public NamespaceName getNamespace() {
		int i;
		if (isGenericEntity()) {
			i = identifier.indexOf("`");
		} else {
			i = identifier.indexOf(",");
		}
		i = identifier.substring(0, i).lastIndexOf(".");
		return CsNamespaceName.newNamespaceName(identifier.substring(0, i));
	}

	@Override
	public TypeName getDeclaringType() {
		// TODO: missing
		throw new UnsupportedOperationException();
	}

	@Override
	public String getFullName() {
		int i = identifier.indexOf("]]");
		if (i < 0) {
			i = identifier.indexOf(",");
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

	@Override
	public boolean isUnknownType() {
		return identifier.equals("?");
	}

	@Override
	public boolean isVoidType() {
		return false;
	}

	@Override
	public boolean isValueType() {
		return false;
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
		return false;
	}

	@Override
	public boolean isClassType() {
		return false;
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
		return false;
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
