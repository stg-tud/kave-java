package cc.kave.commons.model.ssts.impl.declarations;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class VariableDeclaration implements IVariableDeclaration {

	private IVariableReference reference;
	private TypeName type;
	private boolean isMissing;

	public VariableDeclaration() {
		this.reference = new VariableReference();
		this.type = CsTypeName.UNKNOWN_NAME;
	}

	@Override
	public IVariableReference getReference() {
		return this.reference;
	}

	@Override
	public TypeName getType() {
		return this.type;
	}

	@Override
	public boolean getIsMissing() {
		return this.isMissing;
	}

	public void setReference(IVariableReference reference) {
		this.reference = reference;
	}

	public void setType(TypeName type) {
		this.type = type;
	}

	public void setMissing(boolean isMissing) {
		this.isMissing = isMissing;
	}

	@Override
	public int hashCode() {
		return 20 + (reference.hashCode() * 397) ^ type.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VariableDeclaration other = (VariableDeclaration) obj;
		if (isMissing != other.isMissing)
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
