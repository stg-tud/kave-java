package cc.kave.commons.model.names;

import java.util.List;

public interface ILambdaName extends IName {

	String getSignature();

	List<IParameterName> getParameters();

	boolean hasParameters();

	ITypeName getReturnType();
}
