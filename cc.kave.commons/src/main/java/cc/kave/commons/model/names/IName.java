package cc.kave.commons.model.names;

public interface IName {
	/**
	 * Returns a unique representation of the qualified name.
	 */
	String getIdentifier();

	/**
	 * Tells if this instance refers to an unknown name.
     */
    boolean isUnknown();
}
