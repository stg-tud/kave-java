package cc.kave.commons.model.names.csharp;

import java.util.List;

import cc.kave.commons.model.names.DelegateTypeName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;

public class CsDelegateTypeName extends CsTypeName implements DelegateTypeName {

	public static DelegateTypeName UNKNOWN_NAME = newDelegateTypeName("d:[?] [?].()");

	public static DelegateTypeName newDelegateTypeName(String identifier) {
		return (DelegateTypeName) CsTypeName.newTypeName(identifier);
	}

	/* package */CsDelegateTypeName(String identifier) {
		super(identifier);
	}

	@Override
	public String getSignature() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<ParameterName> getParameters() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasParameters() {
		throw new UnsupportedOperationException();
	}

	@Override
	public TypeName getReturnType() {
		throw new UnsupportedOperationException();
	}

}
