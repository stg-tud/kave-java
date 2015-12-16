package cc.kave.commons.model.names;

public interface IParameterName extends IName {
	ITypeName getValueType();

	String getName();

	boolean isPassedByReference();

	boolean isOutput();

	boolean isParameterArray();

	boolean isOptional();

	boolean isExtensionMethodParameter();
}
