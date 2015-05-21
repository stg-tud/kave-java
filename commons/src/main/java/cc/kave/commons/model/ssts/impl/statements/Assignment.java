package cc.kave.commons.model.ssts.impl.statements;

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class Assignment implements IAssignment {

	private IAssignableReference reference;
	private IAssignableExpression expression;

	public Assignment() {
		this.reference = new UnknownReference();
		this.expression = new UnknownExpression();
	}

	@Override
	public IAssignableReference getReference() {
		return this.reference;
	}

	@Override
	public IAssignableExpression getExpression() {
		return this.expression;
	}

	public void setReference(IAssignableReference reference) {
		this.reference = reference;
	}

	public void setExpression(IAssignableExpression expression) {
		this.expression = expression;
	}

	@Override
	public int hashCode() {
		return 11 + (this.reference.hashCode() * 397) ^ this.expression.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Assignment))
			return false;
		Assignment other = (Assignment) obj;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
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
