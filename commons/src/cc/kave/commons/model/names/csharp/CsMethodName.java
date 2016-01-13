package cc.kave.commons.model.names.csharp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;

public class CsMethodName extends CsMemberName implements MethodName {
	private static final Map<String, CsMethodName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final MethodName UNKNOWN_NAME = newMethodName("[?] [?].???()");
	private static final Pattern signatureSyntax = Pattern
			.compile(".*\\]\\.((([^(\\[]+)(?:`[0-9]+\\[[^(]+\\]){0,1})\\(.*\\)).*");

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
		Matcher matcher = signatureSyntax.matcher(identifier);
		if (!matcher.matches()) {
			throw new RuntimeException("Invalid Signature Syntax: " + identifier);
		}
		return matcher.group(1);
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
		return getValueType();
	}

	public String getFullName() {
		Matcher matcher = signatureSyntax.matcher(identifier);
		if (!matcher.matches()) {
			throw new RuntimeException("Invalid Signature Syntax: " + identifier);
		}
		return matcher.group(2);
	}

	public String getName() {
		Matcher matcher = signatureSyntax.matcher(identifier);
		if (!matcher.matches()) {
			throw new RuntimeException("Invalid Signature Syntax: " + identifier);
		}
		return matcher.group(3);
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
