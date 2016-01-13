package cc.kave.commons.model.typeshapes;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

public interface ITypeShape {
	/// <summary>
	/// A description of the enclosing class, including its parent class and
	/// implemented interfaces.
	/// </summary>
	@NonNull
	ITypeHierarchy getTypeHierarchy();

	void setTypeHierarchy(ITypeHierarchy typeHierarchy);

	/// <summary>
	/// All Methods that are overridden in the class under edit (including
	/// information about the first and super
	/// declaration).
	/// </summary>
	@NonNull
	Set<IMethodHierarchy> getMethodHierarchies();

	void setMethodHierarchies(Set<IMethodHierarchy> methodHierarchies);

}
