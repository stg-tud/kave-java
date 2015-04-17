package cc.kave.commons.model.names;

import java.util.List;

public interface MethodName {
	String getSignature();

	List<ParameterName> getParameters();

	boolean hasParameters();

	boolean isConstructor();

	TypeName getReturnType();
}
