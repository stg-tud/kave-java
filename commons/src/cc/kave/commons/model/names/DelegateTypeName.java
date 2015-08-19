package cc.kave.commons.model.names;

import java.util.List;

public interface DelegateTypeName extends TypeName {
	String getSignature();

	List<ParameterName> getParameters();

	boolean hasParameters();

	TypeName getReturnType();
}
