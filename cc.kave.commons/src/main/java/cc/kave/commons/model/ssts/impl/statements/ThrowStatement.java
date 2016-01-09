package cc.kave.commons.model.ssts.impl.statements;

import java.util.ArrayList;

import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ThrowStatement implements IThrowStatement {

	private IVariableReference reference;

	public ThrowStatement() {
		this.reference = new VariableReference();
	}
	
	@Override
	public Iterable<ISSTNode> getChildren() {
		return new ArrayList<ISSTNode>();
	}

	@Override
	public IVariableReference getReference() {
		return this.reference;
	}

	public void setReference(IVariableReference reference) {
		this.reference = reference;
	}

	@Override
	public boolean isReThrow() {
		return reference.isMissing();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		ThrowStatement other = (ThrowStatement) obj;
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