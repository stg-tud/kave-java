package cc.kave.commons.model.names.csharp;

import cc.kave.commons.model.names.MemberName;
import cc.kave.commons.model.names.TypeName;

public abstract class CsMemberName extends CsName implements MemberName {

	public final String STATIC_MODIFIER = "static";

	protected CsMemberName(String identifier) {
		super(identifier);
	}

	public String getModifiers() {
		return identifier.substring(0, identifier.indexOf('['));
	}

	@Override
	public TypeName getDeclaringType() {
		throw new UnsupportedOperationException();
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
