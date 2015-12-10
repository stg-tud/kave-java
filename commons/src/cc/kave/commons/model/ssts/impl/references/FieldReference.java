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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
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
