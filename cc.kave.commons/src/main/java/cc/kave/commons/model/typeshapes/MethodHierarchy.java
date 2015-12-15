package cc.kave.commons.model.typeshapes;

import javax.annotation.Nonnull;

import com.google.gson.annotations.SerializedName;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.csharp.CsMethodName;

public class MethodHierarchy implements IMethodHierarchy {

	private MethodName element;
	@SerializedName("Super")
	private MethodName _super;
	private MethodName first;

	public MethodHierarchy() {
		element = CsMethodName.UNKNOWN_NAME;
	}

	public MethodHierarchy(@Nonnull MethodName m) {
		this.element = m;
	}

	@Override
	public MethodName getElement() {
		return element;
	}

	@Override
	public void setElement(MethodName name) {
		this.element = name;
	}

	@Override
	public MethodName getSuper() {
		return _super;
	}

	@Override
	public void setSuper(MethodName name) {
		this._super = name;
	}

	@Override
	public MethodName getFirst() {
		return first;
	}

	@Override
	public void setFirst(MethodName name) {
		this.first = name;
	}

	@Override
	public boolean isDeclaredInParentHierarchy() {
		return first != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_super == null) ? 0 : _super.hashCode());
		result = prime * result + ((element == null) ? 0 : element.hashCode());
		result = prime * result + ((first == null) ? 0 : first.hashCode());
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
		MethodHierarchy other = (MethodHierarchy) obj;
		if (_super == null) {
			if (other._super != null)
				return false;
		} else if (!_super.equals(other._super))
			return false;
		if (element == null) {
			if (other.element != null)
				return false;
		} else if (!element.equals(other.element))
			return false;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		return true;
	}

}
