package cc.kave.commons.model.names.csharp;

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
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasTypeParameters() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<TypeName> getTypeParameters() {
		throw new UnsupportedOperationException();
	}

	@Override
	public BundleName getAssembly() {
		throw new UnsupportedOperationException();
	}

	@Override
	public NamespaceName getNamespace() {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeName getDeclaringType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getFullName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isUnknownType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isVoidType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isValueType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSimpleType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEnumType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isStructType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isNullableType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isReferenceType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isClassType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isInterfaceType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDelegateType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isNestedType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isArrayType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeName getArrayBaseType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeName DeriveArrayTypeName(int rank) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isTypeParameter() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String TypeParameterShortName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeName TypeParameterType() {
		throw new UnsupportedOperationException();
	}
}
