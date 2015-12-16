package cc.kave.commons.model.names.csharp;

import java.util.List;
import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.ILambdaName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;

public class LambdaName extends Name implements ILambdaName {
	private static final Map<String, LambdaName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final LambdaName UNKNOWN_NAME = newLambdaName(UNKNOWN_NAME_IDENTIFIER);

	public static LambdaName newLambdaName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new LambdaName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private LambdaName(String identifier) {
		super(identifier);
	}

	@Override
	public String getSignature() {
		int startOfSignature = identifier.indexOf('(');
		return identifier.substring(startOfSignature);
	}

	@Override
	public List<IParameterName> getParameters() {
		return CsNameUtils.getParameterNames(identifier);
	}

	@Override
	public boolean hasParameters() {
		return CsNameUtils.hasParameters(identifier);
	}

	@Override
	public ITypeName getReturnType() {
		int startIndexOfValueTypeIdentifier = identifier.indexOf('[') + 1;
		int lastIndexOfValueTypeIdentifer = identifier.indexOf("]") + 1;
		int lengthOfValueTypeIdentifier = lastIndexOfValueTypeIdentifer - startIndexOfValueTypeIdentifier;
		return TypeName
				.newTypeName(identifier.substring(startIndexOfValueTypeIdentifier, lengthOfValueTypeIdentifier));
	}

}
