package cc.kave.commons.model.names;

public interface ParameterName {
	TypeName getValueType();

	String getName();

	boolean isPassedByReference();

	boolean isOutput();

	boolean isParameterArray();

	boolean isOptional();
}
