package cc.kave.commons.model.names.csharp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;

public class MethodName extends MemberName implements IMethodName {
	private static final Map<String, MethodName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final IMethodName UNKNOWN_NAME = newMethodName("[?] [?].???()");
	private static final Pattern signatureSyntax = Pattern
			.compile(".*\\]\\.((([^(\\[]+)(?:`[0-9]+\\[[^(]+\\]){0,1})\\(.*\\)).*");
	// "\\]\\.((([^([]+)(?:`[0-9]+\\[[^(]+\\]){0,1})\\(.*\\))$"

	public static IMethodName newMethodName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new MethodName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private MethodName(String identifier) {
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
	public List<IParameterName> getParameters() {
		return CsNameUtils.getParameterNames(identifier);
	}

	@Override
	public boolean hasParameters() {
		return !identifier.contains("()");
	}

	@Override
	public boolean isConstructor() {
		return getSignature().startsWith(".ctor(") || getSignature().startsWith(".cctor(");
	}

	@Override
	public ITypeName getReturnType() {
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
	public List<ITypeName> getTypeParameters() {
		return hasTypeParameters() ? CsGenericNameUtils.parseTypeParameters(getFullName()) : new ArrayList<ITypeName>();
	}

	@Override
	public boolean isGenericEntity() {
		return hasTypeParameters();
	}

	@Override
	public boolean isExtensionMethod() {
		return isStatic() && getParameters().size() > 0 && getParameters().get(0).isExtensionMethodParameter();
	}
}
