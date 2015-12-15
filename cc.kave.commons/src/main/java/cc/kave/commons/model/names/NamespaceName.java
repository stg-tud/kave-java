package cc.kave.commons.model.names;

public interface NamespaceName extends Name {
	NamespaceName getParentNamespace();

	String getName();

	/**
	 * @return Wheather this is the global (or default) namespace.
	 */
	boolean isGlobalNamespace();
}
