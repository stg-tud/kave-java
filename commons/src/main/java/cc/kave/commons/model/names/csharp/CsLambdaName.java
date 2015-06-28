package cc.kave.commons.model.names.csharp;

import java.util.List;
import java.util.Map;

import cc.kave.commons.model.names.LambdaName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;

import com.google.common.collect.MapMaker;

public class CsLambdaName extends CsName implements LambdaName {
	private static final Map<String, CsLambdaName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final CsLambdaName UNKNOWN_NAME = newLambdaName(UNKNOWN_NAME_IDENTIFIER);

	public static CsLambdaName newLambdaName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new CsLambdaName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private CsLambdaName(String identifier) {
		super(identifier);
	}

	@Override
	public String getSignature() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<ParameterName> getParameters() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasParameters() {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeName getReturnType() {
		throw new UnsupportedOperationException();
	}

}
