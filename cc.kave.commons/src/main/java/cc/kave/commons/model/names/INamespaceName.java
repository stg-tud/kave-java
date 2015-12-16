package cc.kave.commons.model.names;

public interface INamespaceName extends IName {
	INamespaceName getParentNamespace();

	String getName();

	/**
	 * @return Wheather this is the global (or default) namespace.
	 */
	boolean isGlobalNamespace();
}
