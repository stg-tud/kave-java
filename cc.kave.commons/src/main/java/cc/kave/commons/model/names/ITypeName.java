/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.names;

public interface ITypeName extends IGenericName {
	/**
	 * The name of the bundle (e.g., assembly) this type is declared in.
	 */
	IAssemblyName getAssembly();

	/**
	 * A full-qualified identifier of the namespace containing the type.
	 */
	INamespaceName getNamespace();

	/**
	 * The name of the type declaring this type or <code>null</code> if this
	 * type is not nested.
	 */
	ITypeName getDeclaringType();

	String getFullName();

	String getName();

	boolean isUnknownType();

	boolean isVoidType();

	/**
	 * Value types are simple (or primitive) types, enum types, struct types and
	 * nullable types (the extension of all other value types with the
	 * <code>null</code> value).
	 */
	boolean isValueType();

	/**
	 * @return Wheather this is a simple (or primitive) type
	 */
	boolean isSimpleType();

	boolean isEnumType();

	boolean isStructType();

	/**
	 * @return Wheather this is a value type that can also take the
	 *         <code>null</code> value
	 */
	boolean isNullableType();

	/**
	 * Reference types are (abstract) class types, interface types, array types,
	 * and delegate types.
	 */
	boolean isReferenceType();

	boolean isClassType();

	boolean isInterfaceType();

	boolean isDelegateType();

	boolean isNestedType();

	boolean isArrayType();

	ITypeName getArrayBaseType();

	/**
	 * Creates an array-type name from this name.
	 * 
	 * @param rank
	 *            The rank of the array; must be greater than 0.
	 */
	ITypeName DeriveArrayTypeName(int rank);

	boolean isTypeParameter();

	String getTypeParameterShortName();

	ITypeName getTypeParameterType();
}
