package cc.kave.commons.model.names;

public interface IMemberName extends IName {
	ITypeName getDeclaringType();

	ITypeName getValueType();

	boolean isStatic();

	String getName();
}
