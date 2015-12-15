package cc.kave.commons.model.names;

import java.util.List;

public interface MethodName extends MemberName, GenericName {
	String getSignature();

	List<ParameterName> getParameters();

	boolean hasParameters();

	boolean isConstructor();

	TypeName getReturnType();

	boolean isExtensionMethod();
}
