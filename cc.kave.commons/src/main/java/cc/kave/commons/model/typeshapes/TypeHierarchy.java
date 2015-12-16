package cc.kave.commons.model.typeshapes;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.annotations.SerializedName;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.TypeName;

public class TypeHierarchy implements ITypeHierarchy {

	private ITypeName element;
	@SerializedName("Extends")
	private ITypeHierarchy _extends;
	@SerializedName("Implements")
	private Set<ITypeHierarchy> _implements;

	public TypeHierarchy() {
		this.element = TypeName.UNKNOWN_NAME;
		this._implements = new HashSet<>();
	}

	public TypeHierarchy(String elementQualifiedName) {
		this.element = TypeName.newTypeName(elementQualifiedName);
	}

	public void setExtends(ITypeHierarchy _extends) {
		this._extends = _extends;
	}

	public void setImplements(Set<ITypeHierarchy> _implements) {
		this._implements = _implements;
	}

	public void setElement(ITypeName element) {
		this.element = element;
	}

	@Override
	public ITypeName getElement() {
		return this.element;
	}

	@Override
	public ITypeHierarchy getExtends() {
		return this._extends;
	}

	@Override
	public Set<ITypeHierarchy> getImplements() {
		return this._implements;
	}

	@Override
	public boolean hasSupertypes() {
		return this.hasSuperclass() || this.isImplementingInterfaces();
	}

	@Override
	public boolean hasSuperclass() {
		return this._extends != null;
	}

	@Override
	public boolean isImplementingInterfaces() {
		return this._implements.size() > 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_extends == null) ? 0 : _extends.hashCode());
		result = prime * result + ((_implements == null) ? 0 : _implements.hashCode());
		result = prime * result + ((element == null) ? 0 : element.hashCode());
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
		TypeHierarchy other = (TypeHierarchy) obj;
		if (_extends == null) {
			if (other._extends != null)
				return false;
		} else if (!_extends.equals(other._extends))
			return false;
		if (_implements == null) {
			if (other._implements != null)
				return false;
		} else if (!_implements.equals(other._implements))
			return false;
		if (element == null) {
			if (other.element != null)
				return false;
		} else if (!element.equals(other.element))
			return false;
		return true;
	}

}
