package cc.kave.commons.model.names.csharp.parser;

public class TypeTestCase {

	private String identifier;
	private String namespace;
	private String assembly;

	public TypeTestCase(String identifier, String namespace, String assembly) {
		this.identifier = identifier;
		this.namespace = namespace;
		this.assembly = assembly;
	}

	public TypeTestCase(String identifier) {
		this.identifier = identifier;
	}

	public String getAssembly() {
		return assembly;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getNamespace() {
		return namespace;
	}
}
