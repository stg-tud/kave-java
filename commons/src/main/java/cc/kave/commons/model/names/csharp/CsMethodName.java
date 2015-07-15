package cc.kave.commons.model.names.csharp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;

import com.google.common.collect.MapMaker;

public class CsMethodName extends CsMemberName implements MethodName {
	private static final Map<String, CsMethodName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final MethodName UNKNOWN_NAME = newMethodName("[?] [?].???()");

	public static MethodName newMethodName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new CsMethodName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private CsMethodName(String identifier) {
		super(identifier);
	}

	@Override
	public String getSignature() {
		return identifier.substring(identifier.indexOf("].") + 2, identifier.length());
	}

	@Override
	public List<ParameterName> getParameters() {
		List<ParameterName> list = new ArrayList<ParameterName>();

		int low = identifier.indexOf("([", 0) + 1;
		while (hasParameters() && low != -1) {
			int up = Math.min(identifier.indexOf(", [", low), identifier.indexOf(")", low));

			if (up == -1) {
				up = Math.max(identifier.indexOf(", [", low), identifier.indexOf(")", low));
			}

			list.add(CsParameterName.newParameterName(identifier.substring(low, up)));
			low = identifier.indexOf("[", low + 1);
		}
		return list;
	}

	@Override
	public boolean hasParameters() {
		return !identifier.contains("()");
	}

	@Override
	public boolean isConstructor() {
		boolean voidReturn = getReturnType().getIdentifier().substring(0, 4).equals("void");
		boolean upperCase = getSignature().substring(0, 1).equals(getSignature().substring(0, 1).toUpperCase());
		return voidReturn && upperCase;
	}

	@Override
	public TypeName getReturnType() {
		return CsTypeName.newTypeName(identifier.substring(identifier.indexOf("[") + 1, identifier.indexOf("]")));
	}
}
