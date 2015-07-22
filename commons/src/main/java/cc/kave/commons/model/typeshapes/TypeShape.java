package cc.kave.commons.model.typeshapes;

import java.util.HashSet;
import java.util.Set;

public class TypeShape implements ITypeShape {

	private ITypeHierarchy typeHierarchy;
	private Set<IMethodHierarchy> methodHierarchies;

	public TypeShape() {
		this.typeHierarchy = new TypeHierarchy();
		this.methodHierarchies = new HashSet<>();
	}

	@Override
	public ITypeHierarchy getTypeHierarchy() {
		return this.typeHierarchy;
	}

	@Override
	public void setTypeHierarchy(ITypeHierarchy typeHierarchy) {
		this.typeHierarchy = typeHierarchy;
	}

	@Override
	public Set<IMethodHierarchy> getMethodHierarchies() {
		return this.methodHierarchies;
	}

	@Override
	public void setMethodHierarchies(Set<IMethodHierarchy> methodHierarchies) {
		this.methodHierarchies = methodHierarchies;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((methodHierarchies == null) ? 0 : methodHierarchies.hashCode());
		result = prime * result + ((typeHierarchy == null) ? 0 : typeHierarchy.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypeShape other = (TypeShape) obj;
		if (methodHierarchies == null) {
			if (other.methodHierarchies != null)
				return false;
		} else if (!methodHierarchies.equals(other.methodHierarchies))
			return false;
		if (typeHierarchy == null) {
			if (other.typeHierarchy != null)
				return false;
		} else if (!typeHierarchy.equals(other.typeHierarchy))
			return false;
		return true;
	}

}
