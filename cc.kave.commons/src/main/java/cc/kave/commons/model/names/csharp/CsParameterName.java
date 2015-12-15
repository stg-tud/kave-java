package cc.kave.commons.model.names.csharp;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;

public class CsParameterName extends CsName implements ParameterName {

	public final String passByReferenceModifier = "ref";
	public final String outputModifier = "out";
	public final String varArgsModifier = "params";
	public final String optionalModifier = "opt";
	public final String ExtensionMethodModifier = "this";

	private static final Map<String, CsParameterName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final ParameterName UNKNOWN_NAME = newParameterName("[?] ???");

	public static ParameterName newParameterName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new CsParameterName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private CsParameterName(String identifier) {
		super(identifier);
	}

	public static ParameterName getUnknownName() {
		return CsParameterName.newParameterName("[?] ???");
	}

	public boolean isUnknown() {
		return this.equals(getUnknownName());
	}

	@Override
	public TypeName getValueType() {
		int startOfValueTypeIdentifier = identifier.indexOf('[') + 1;
		int endOfValueTypeIdentifier = identifier.lastIndexOf(']') + 1;
		int lengthOfValueTypeIdentifier = endOfValueTypeIdentifier - startOfValueTypeIdentifier
				+ getModifiers().length();
		return CsTypeName.newTypeName(identifier.substring(startOfValueTypeIdentifier, lengthOfValueTypeIdentifier));
	}

	@Override
	public String getName() {
		return identifier.substring(identifier.lastIndexOf(" ") + 1);
	}

	@Override
	public boolean isPassedByReference() {
		return getValueType().isReferenceType() || getModifiers().contains(passByReferenceModifier);
	}

	@Override
	public boolean isOutput() {
		return getModifiers().contains(outputModifier);
	}

	@Override
	public boolean isParameterArray() {
		return getModifiers().contains(varArgsModifier);
	}

	@Override
	public boolean isOptional() {
		return getModifiers().contains(optionalModifier);
	}

	private String getModifiers() {
		return identifier.substring(0, identifier.indexOf('['));
	}

	public boolean isExtensionMethodParameter() {
		return getModifiers().contains(ExtensionMethodModifier);
	}
}
