package cc.kave.commons.model.names.csharp;

import java.util.List;
import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.LambdaName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;

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
		int startOfSignature = identifier.indexOf('(');
		return identifier.substring(startOfSignature);
	}

	@Override
	public List<ParameterName> getParameters() {
		return CsNameUtils.getParameterNames(identifier);
	}

	@Override
	public boolean hasParameters() {
		return CsNameUtils.hasParameters(identifier);
	}

	@Override
	public TypeName getReturnType() {
		int startIndexOfValueTypeIdentifier = identifier.indexOf('[') + 1;
		int lastIndexOfValueTypeIdentifer = identifier.indexOf("]") + 1;
		int lengthOfValueTypeIdentifier = lastIndexOfValueTypeIdentifer - startIndexOfValueTypeIdentifier;
		return CsTypeName
				.newTypeName(identifier.substring(startIndexOfValueTypeIdentifier, lengthOfValueTypeIdentifier));
	}

}
