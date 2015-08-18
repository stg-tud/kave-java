package cc.kave.commons.model.names.csharp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;

public class CsMethodName extends CsMemberName implements MethodName {
	private static final Map<String, CsMethodName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final MethodName UNKNOWN_NAME = newMethodName("[?] [?].???()");

	private static final Pattern signatureSyntax = Pattern
			.compile("\\]\\.((([^(\\[]+)(?:`[0-9]+\\[[^(]+\\]){0,1})\\(.*\\))$");

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
		return signatureSyntax.matcher(identifier).group(1);
		// return identifier.substring(identifier.lastIndexOf("].") + 2,
		// identifier.length());
	}

	@Override
	public List<ParameterName> getParameters() {
		return CsNameUtils.getParameterNames(identifier);
	}

	@Override
	public boolean hasParameters() {
		return !identifier.contains("()");
	}

	@Override
	public boolean isConstructor() {
		return getSignature().startsWith(".ctor(");
	}

	@Override
	public TypeName getReturnType() {
		return CsTypeName.newTypeName(identifier.substring(identifier.indexOf("[") + 1, identifier.indexOf("]")));
	}

	// TODO:
	public String getFullName() {
		String group = signatureSyntax.matcher(identifier).group(2);
		signatureSyntax.matcher(identifier).group(2);
		return signatureSyntax.matcher(identifier).group(2);
		// return "";
	}

	public String getName() {
		return signatureSyntax.matcher(identifier).group(3);
	}

	@Override
	public boolean hasTypeParameters() {
		return getFullName().contains("[[");
	}

	@Override
	public List<TypeName> getTypeParameters() {
		return hasTypeParameters() ? CsGenericNameUtils.parseTypeParameters(getFullName()) : new ArrayList<TypeName>();
	}

	@Override
	public boolean isGenericEntity() {
		return hasTypeParameters();
	}
}
