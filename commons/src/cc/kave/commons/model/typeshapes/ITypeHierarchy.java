package cc.kave.commons.model.typeshapes;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import cc.kave.commons.model.names.TypeName;

public interface ITypeHierarchy {
	/// <summary>
	/// The type at this level in the type hierarchy.
	/// </summary>
	@NonNull
	TypeName getElement();

	/// <summary>
	/// The direct superclass of the type at this level.
	/// </summary>
	@Nullable
	ITypeHierarchy getExtends();

	/// <summary>
	/// The interfaces directly implemented by the type at this level.
	/// </summary>
	@NonNull

	Set<ITypeHierarchy> getImplements();

	/// <summary>
	/// <returns>Whether this type extends some superclass or implements any
	/// interfaces</returns>
	/// </summary>
	boolean hasSupertypes();

	/// <summary>
	/// <returns>Whether this type extends some superclass</returns>
	/// </summary>
	boolean hasSuperclass();

	/// <summary>
	/// <returns>Whether this type implements any interfaces</returns>
	/// </summary>
	boolean isImplementingInterfaces();

}
