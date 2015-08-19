package cc.kave.commons.model.ssts.impl.references;

import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.names.csharp.CsPropertyName;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class PropertyReference implements IPropertyReference {

	private IVariableReference reference;
	private PropertyName propertyName;

	public PropertyReference() {
		this.reference = new VariableReference();
		this.propertyName = CsPropertyName.UNKNOWN_NAME;
	}

	@Override
	public IVariableReference getReference() {
		return this.reference;
	}

	@Override
	public PropertyName getPropertyName() {
		return this.propertyName;
	}

	public void setReference(IVariableReference reference) {
		this.reference = reference;
	}

	public void setPropertyName(PropertyName propertyName) {
		this.propertyName = propertyName;
	}

	@Override
	public int hashCode() {
		return 505239 + this.propertyName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PropertyReference))
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
