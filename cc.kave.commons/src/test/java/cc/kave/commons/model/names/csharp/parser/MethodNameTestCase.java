package cc.kave.commons.model.names.csharp.parser;

import java.util.List;

public class MethodNameTestCase {

	private String identifier;
	private String declaringType;
	private String returnType;
	private String simpleName;
	private boolean isStatic;
	private boolean isGeneric;
	private List<String> parameters;
	private List<String> typeParameters;

	public MethodNameTestCase(String identifier, String declaringType, String returnType, String simpleName,
			boolean isStatic, boolean isGeneric, List<String> parameters, List<String> typeParameters) {
		this.identifier = identifier;
		this.declaringType = declaringType;
		this.returnType = returnType;
		this.simpleName = simpleName;
		this.isStatic = isStatic;
		this.parameters = parameters;
		this.isGeneric = isGeneric;
		this.typeParameters = typeParameters;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getDeclaringType() {
		return declaringType;
	}

	public String getReturnType() {
		return returnType;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public boolean isGeneric() {
		return isGeneric;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public List<String> getTypeParameters() {
		return typeParameters;
	}

}
