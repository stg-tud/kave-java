package cc.kave.commons.model.names;

import java.util.List;

public interface GenericName extends Name {
	boolean isGenericEntity();

	/**
	 * Whether the name contains a list of type parameters.
	 */
	boolean hasTypeParameters();

	List<TypeName> getTypeParameters();
}
