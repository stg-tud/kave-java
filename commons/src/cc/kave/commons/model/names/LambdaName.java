package cc.kave.commons.model.names;

import java.util.List;

public interface LambdaName extends Name {

	String getSignature();

	List<ParameterName> getParameters();

	boolean hasParameters();

	TypeName getReturnType();
}
