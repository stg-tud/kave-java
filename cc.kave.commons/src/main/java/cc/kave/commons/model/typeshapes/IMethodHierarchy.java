package cc.kave.commons.model.typeshapes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cc.kave.commons.model.names.IMethodName;

public interface IMethodHierarchy {
	/// <summary>
	/// The name of a method.
	/// </summary>
	@Nonnull
	IMethodName getElement();

	void setElement(@Nonnull IMethodName name);

	/// <summary>
	/// The implementation of the enclosing method that is referred to by
	/// calling
	/// <code>super.'methodName'(...)</code>.
	/// </summary>
	@Nullable
	IMethodName getSuper();

	void setSuper(IMethodName name);

	/// <summary>
	/// The declarations of the enclosing method, i.e., the method names
	/// specified in interfaces or the highest
	/// parent class that the enclosing method is an implementation of.
	/// </summary>

	@Nullable
	IMethodName getFirst();

	void setFirst(IMethodName name);

	/// <summary>
	/// Whether or not this is a hierarchy of a method that overrides or
	/// implements a declaration from further up in the
	/// type hierarchy.
	/// </summary>
	boolean isDeclaredInParentHierarchy();

}
