package cc.kave.commons.model.names.csharp;

import cc.kave.commons.model.names.IMemberName;
import cc.kave.commons.model.names.ITypeName;

public abstract class MemberName extends Name implements IMemberName {

	public final String STATIC_MODIFIER = "static";

	protected MemberName(String identifier) {
		super(identifier);
	}

	public String getModifiers() {
		return identifier.substring(0, identifier.indexOf('['));
	}

	@Override
	public ITypeName getDeclaringType() {
		int endOfValueType = CsNameUtils.endOfNextTypeIdentifier(identifier, 0);
		int startOfDeclaringType = CsNameUtils.startOfNextTypeIdentifier(identifier, endOfValueType) + 1;
		int endOfDeclaringType = CsNameUtils.endOfNextTypeIdentifier(identifier, endOfValueType) - 1;
		return TypeName.newTypeName(identifier.substring(startOfDeclaringType, endOfDeclaringType));
	}

	@Override
	public ITypeName getValueType() {
		int start = identifier.indexOf("[");
		int end = CsNameUtils.getClosingBracketIndex(identifier, start);
		return TypeName.newTypeName(identifier.substring(start + 1, end - 1));
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
