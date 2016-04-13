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

package cc.kave.commons.model.names.csharp;

import java.util.List;

import cc.kave.commons.model.names.IAssemblyName;
import cc.kave.commons.model.names.INamespaceName;
import cc.kave.commons.model.names.ITypeName;

public class TypeParameterName extends Name implements ITypeName {

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

	/// <summary>
	/// Gets the <see cref="ITypeName" /> for the identifer
	/// <code>'short name' -&gt; 'actual-type identifier'</code>.
	/// </summary>
	public static ITypeName newTypeParameterName(String typeParameterShortName, String actualTypeIdentifier) {
		if (UnknownTypeName.isUnknownTypeIdentifier(actualTypeIdentifier)) {
			return newTypeParameterName(typeParameterShortName);
		}
		return newTypeParameterName(typeParameterShortName + ParameterNameTypeSeparater + actualTypeIdentifier);
	}

	static boolean isTypeParameterIdentifier(String identifier) {
		if (UnknownTypeName.isUnknownTypeIdentifier(identifier)) {
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
	public boolean isGenericEntity() {
		return getTypeParameterType().isGenericEntity();
	}

	@Override
	public boolean hasTypeParameters() {
		return getTypeParameterType().hasTypeParameters();
	}

	@Override
	public List<ITypeName> getTypeParameters() {
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
	public boolean isUnknownType() {
		return getTypeParameterType().isUnknownType();
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
	public boolean isArrayType() {
		return getTypeParameterType().isArrayType();
	}

	@Override
	public ITypeName getArrayBaseType() {
		return getTypeParameterType().getArrayBaseType();
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
	public ITypeName DeriveArrayTypeName(int rank) {
		if (rank <= 0) {
			throw new RuntimeException("rank smaller than 1");
		}
		String typeParameterShortName = getTypeParameterShortName();
		String suffix = identifier.substring(typeParameterShortName.length());
		return TypeName.newTypeName(
				String.format("{%s}[{%s}]{%s}", typeParameterShortName, "," + String.valueOf(rank - 1), suffix));
	}

	@Override
	public String getTypeParameterShortName()

	{

		int endOfTypeParameterName = identifier.indexOf(' ');
		return endOfTypeParameterName == -1 ? identifier : identifier.substring(0, endOfTypeParameterName);

	}

	@Override
	public ITypeName getTypeParameterType() {
		int startOfTypeName = getTypeParameterShortName().length() + ParameterNameTypeSeparater.length();
		return startOfTypeName >= identifier.length() ? UnknownTypeName.getInstance()
				: TypeName.newTypeName(identifier.substring(startOfTypeName));

	}
}
