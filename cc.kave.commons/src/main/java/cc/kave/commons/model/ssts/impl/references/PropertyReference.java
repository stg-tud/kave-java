package cc.kave.commons.model.ssts.impl.references;

import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class PropertyReference implements IPropertyReference {

	private IVariableReference reference;
	private IPropertyName propertyName;

	public PropertyReference() {
		this.reference = new VariableReference();
		this.propertyName = PropertyName.UNKNOWN_NAME;
	}

	@Override
	public IVariableReference getReference() {
		return this.reference;
	}

	@Override
	public IPropertyName getPropertyName() {
		return this.propertyName;
	}

	public void setReference(IVariableReference reference) {
		this.reference = reference;
	}

	public void setPropertyName(IPropertyName propertyName) {
		this.propertyName = propertyName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
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
		PropertyReference other = (PropertyReference) obj;
		if (propertyName == null) {
			if (other.propertyName != null)
				return false;
		} else if (!propertyName.equals(other.propertyName))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
