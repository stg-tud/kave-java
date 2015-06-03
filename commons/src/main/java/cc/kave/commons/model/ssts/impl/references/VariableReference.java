package cc.kave.commons.model.ssts.impl.references;

import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class VariableReference implements IVariableReference {

	private final String defaultIdentifier = "";

	private String identifier;

	public VariableReference() {
		this.identifier = this.defaultIdentifier;
	}

	@Override
	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public boolean isMissing() {
		return identifier.equals(defaultIdentifier);
	}

	@Override
	public int hashCode() {
		return 724584 + this.identifier.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof VariableReference))
			return false;
		VariableReference other = (VariableReference) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
