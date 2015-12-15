package cc.kave.commons.model.names;

public interface ParameterName extends Name {
	TypeName getValueType();

	String getName();

	boolean isPassedByReference();

	boolean isOutput();

	boolean isParameterArray();

	boolean isOptional();

	boolean isExtensionMethodParameter();
}
