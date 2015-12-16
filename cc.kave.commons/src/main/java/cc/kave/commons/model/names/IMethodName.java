package cc.kave.commons.model.names;

import java.util.List;

public interface IMethodName extends IMemberName, IGenericName {
	String getSignature();

	List<IParameterName> getParameters();

	boolean hasParameters();

	boolean isConstructor();

	ITypeName getReturnType();

	boolean isExtensionMethod();
}
