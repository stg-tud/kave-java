/**
 * Copyright 2015 Waldemar Graf
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.kave.commons.model.naming.impl.v0.types;

import java.util.List;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.impl.v0.BaseName;
import cc.kave.commons.model.naming.types.IArrayTypeName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.IPredefinedTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;

public class TypeParameterName extends BaseName implements ITypeParameterName, IArrayTypeName {

	public TypeParameterName(String identifier) {
		super(identifier);
	}

	/// <summary>
	/// The separator between the parameter type's short name and its type.
	/// </summary>
	private final static String ParameterNameTypeSeparater = " -> ";

	/// <summary>
	/// Constructor for reflective recreation of names. See <see
	/// cref="Get(string,string)" /> for details on how to
	/// instantiate type-parameter names.
	/// </summary>
	public static ITypeName newTypeParameterName(String identifier) {
		return TypeName.newTypeName(identifier);
	}

	static boolean isTypeParameterIdentifier(String identifier) {
		if (TypeUtils.isUnknownTypeIdentifier(identifier)) {
			return false;
		}
		return isFreeTypeParameterIdentifier(identifier) || isBoundTypeParameterIdentifier(identifier);
	}

	private static boolean isFreeTypeParameterIdentifier(String identifier) {
		return !identifier.contains(",") && !identifier.contains("[");
	}

	private static boolean isBoundTypeParameterIdentifier(String identifier) {
		// "T -> System.Void, mscorlib, ..." is a type parameter, because it
		// contains the separator.
		// "System.Nullable`1[[T -> System.Int32, mscorlib, 4.0.0.0]], ..." is
		// not, because
		// the separator is only in the type's parameter-type list, i.e., after
		// the '`'.
		int indexOfMapping = identifier.indexOf(ParameterNameTypeSeparater);
		int endOfTypeName = identifier.indexOf('`');
		return indexOfMapping >= 0 && (endOfTypeName == -1 || endOfTypeName > indexOfMapping);
	}

	@Override
	public boolean hasTypeParameters() {
		return getTypeParameterType().hasTypeParameters();
	}

	@Override
	public List<ITypeParameterName> getTypeParameters() {
		return getTypeParameterType().getTypeParameters();
	}

	@Override
	public IAssemblyName getAssembly() {
		return getTypeParameterType().getAssembly();
	}

	@Override
	public INamespaceName getNamespace() {
		return getTypeParameterType().getNamespace();
	}

	@Override
	public ITypeName getDeclaringType() {
		return getTypeParameterType().getDeclaringType();
	}

	@Override
	public String getFullName() {
		return getTypeParameterType().getFullName();
	}

	@Override
	public String getName() {
		return getTypeParameterType().getName();
	}

	@Override
	public boolean isVoidType() {
		return getTypeParameterType().isVoidType();
	}

	@Override
	public boolean isValueType() {
		return getTypeParameterType().isValueType();
	}

	@Override
	public boolean isSimpleType() {
		return getTypeParameterType().isSimpleType();
	}

	@Override
	public boolean isEnumType() {
		return getTypeParameterType().isEnumType();
	}

	@Override
	public boolean isStructType() {
		return getTypeParameterType().isStructType();
	}

	@Override
	public boolean isNullableType() {
		return getTypeParameterType().isNullableType();
	}

	@Override
	public boolean isReferenceType() {
		return getTypeParameterType().isReferenceType();
	}

	@Override
	public boolean isClassType() {
		return getTypeParameterType().isClassType();
	}

	@Override
	public boolean isInterfaceType() {
		return getTypeParameterType().isInterfaceType();
	}

	@Override
	public boolean isDelegateType() {
		return getTypeParameterType().isDelegateType();
	}

	@Override
	public boolean isNestedType() {
		return getTypeParameterType().isNestedType();
	}

	@Override
	public boolean isArray() {
		return getTypeParameterType().isArray();
	}

	@Override
	public ITypeName getArrayBaseType() {
		return getTypeParameterType().asArrayTypeName().getArrayBaseType();
	}

	@Override
	public boolean isTypeParameter() {
		return true;
	}

	@Override
	public boolean isUnknown() {
		return this.equals(UNKNOWN_NAME);
	}

	@Override
	public String getTypeParameterShortName() {
		int endOfTypeParameterName = identifier.indexOf(' ');
		return endOfTypeParameterName == -1 ? identifier : identifier.substring(0, endOfTypeParameterName);
	}

	@Override
	public ITypeName getTypeParameterType() {
		int startOfTypeName = getTypeParameterShortName().length() + ParameterNameTypeSeparater.length();
		return startOfTypeName >= identifier.length() ? Names.getUnknownType()
				: TypeName.newTypeName(identifier.substring(startOfTypeName));

	}

	@Override
	public IDelegateTypeName asDelegateTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IArrayTypeName asArrayTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITypeParameterName asTypeParameterName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPredefined() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IPredefinedTypeName asPredefinedTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRank() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isBound() {
		// TODO Auto-generated method stub
		return false;
	}
}
