package cc.kave.commons.model.names.csharp;

import cc.kave.commons.model.names.MemberName;
import cc.kave.commons.model.names.TypeName;


public abstract class CsMemberName extends CsName implements MemberName {

	protected CsMemberName(String identifier) {
		super(identifier);
	}

	@Override
	public TypeName getDeclaringType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isStatic() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException();
	}
}
