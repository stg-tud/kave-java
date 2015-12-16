package cc.kave.commons.model.names;

import java.util.List;

public interface IDelegateTypeName extends ITypeName {
	String getSignature();

	List<IParameterName> getParameters();

	boolean hasParameters();

	ITypeName getReturnType();
}
