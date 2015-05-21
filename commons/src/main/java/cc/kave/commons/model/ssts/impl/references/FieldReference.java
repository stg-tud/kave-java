package cc.kave.commons.model.ssts.impl.references;

import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class FieldReference implements IFieldReference {

	private IVariableReference reference;
	private FieldName fieldName;

	public FieldReference() {
		this.reference = new VariableReference();
		this.fieldName = CsFieldName.UNKNOWN_NAME;
	}

	@Override
	public IVariableReference getReference() {
		return this.reference;
	}

	@Override
	public FieldName getFieldName() {
		return this.fieldName;
	}

	public void setReference(IVariableReference reference) {
		this.reference = reference;
	}

	public void setFieldName(FieldName fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public int hashCode() {
		return 345 + this.fieldName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FieldReference))
			return false;
		FieldReference other = (FieldReference) obj;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
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
