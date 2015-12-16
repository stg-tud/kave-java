package cc.kave.commons.model.names;

import java.util.List;

public interface IGenericName extends IName {
	boolean isGenericEntity();

	/**
	 * Whether the name contains a list of type parameters.
	 */
	boolean hasTypeParameters();

	List<ITypeName> getTypeParameters();
}
