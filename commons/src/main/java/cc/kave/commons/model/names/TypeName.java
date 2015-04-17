package cc.kave.commons.model.names;

public interface TypeName extends GenericName {
	/**
	 * The name of the bundle (e.g., assembly) this type is declared in.
	 */
	BundleName getAssembly();

	/**
	 * A full-qualified identifier of the namespace containing the type.
	 */
	NamespaceName getNamespace();

	/**
	 * The name of the type declaring this type or <code>null</code> if this
	 * type is not nested.
	 */
	TypeName getDeclaringType();

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

	TypeName getArrayBaseType();

	/**
	 * Creates an array-type name from this name.
	 * 
	 * @param rank
	 *            The rank of the array; must be greater than 0.
	 */
	TypeName DeriveArrayTypeName(int rank);

	boolean isTypeParameter();

	String TypeParameterShortName();

	TypeName TypeParameterType();
}
