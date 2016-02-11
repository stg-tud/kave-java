package cc.kave.commons.model.names.csharp;

import cc.kave.commons.model.names.MemberName;
import cc.kave.commons.model.names.TypeName;

public abstract class CsMemberName extends CsName implements MemberName {

	public final String STATIC_MODIFIER = "static";

	protected CsMemberName(String identifier) {
		super(identifier);
	}

	public String getModifiers() {
		if (identifier.indexOf('[') == -1) {
			return "";
		}

		return identifier.substring(0, identifier.indexOf('['));
	}

	@Override
	public TypeName getDeclaringType() {
		int endOfValueType = CsNameUtils.endOfNextTypeIdentifier(identifier, 0);
		int startOfDeclaringType = CsNameUtils.startOfNextTypeIdentifier(identifier, endOfValueType) + 1;
		int endOfDeclaringType = CsNameUtils.endOfNextTypeIdentifier(identifier, endOfValueType) - 1;
		return CsTypeName.newTypeName(identifier.substring(startOfDeclaringType, endOfDeclaringType));
	}

	@Override
	public TypeName getValueType() {
		int start = identifier.indexOf("[");
		int end = CsNameUtils.getClosingBracketIndex(identifier, start);
		return CsTypeName.newTypeName(identifier.substring(start + 1, end - 1));
	}

	@Override
	public boolean isStatic() {
		return getModifiers().contains(STATIC_MODIFIER);
	}

	@Override
	public String getName() {
		return identifier.substring(identifier.lastIndexOf('.') + 1);
	}
}
