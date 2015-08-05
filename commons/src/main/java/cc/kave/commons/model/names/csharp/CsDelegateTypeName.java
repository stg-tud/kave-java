package cc.kave.commons.model.names.csharp;

import java.util.List;

import cc.kave.commons.model.names.DelegateTypeName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;

public class CsDelegateTypeName extends CsTypeName implements DelegateTypeName {

	public static DelegateTypeName UNKNOWN_NAME = newDelegateTypeName("d:[?] [?].()");

	private static final String prefix = "d:";

	public static DelegateTypeName newDelegateTypeName(String identifier) {
		return (DelegateTypeName) CsTypeName.newTypeName(identifier);
	}

	/* package */ CsDelegateTypeName(String identifier) {
		super(identifier);
	}

	private static boolean isDelegateTypeIdentifier(String identifier) {
		return identifier.startsWith(prefix);
	}

	private MethodName getDelegateMethod() {
		return CsMethodName.newMethodName(identifier.substring(prefix.length()));
	}

	@Override
	public String getSignature() {
		String signature = identifier.substring(identifier.indexOf("]" + 2), identifier.indexOf("].(") + 1);
		if (!signature.contains("?")) {
			signature.substring(0, identifier.indexOf(","));
			// signature
		}
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
