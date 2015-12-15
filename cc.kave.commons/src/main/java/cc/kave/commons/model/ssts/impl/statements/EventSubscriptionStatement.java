package cc.kave.commons.model.ssts.impl.statements;

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.statements.EventSubscriptionOperation;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class EventSubscriptionStatement implements IEventSubscriptionStatement {
	private IAssignableReference reference;
	private EventSubscriptionOperation operation;
	private IAssignableExpression expression;

	public EventSubscriptionStatement() {
		reference = new UnknownReference();
		expression = new UnknownExpression();
		operation = EventSubscriptionOperation.Add;
	}

	public IAssignableReference getReference() {
		return reference;
	}

	public void setReference(IAssignableReference reference) {
		this.reference = reference;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expression == null) ? 0 : expression.hashCode());
		result = prime * result + ((operation == null) ? 0 : operation.hashCode());
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
		EventSubscriptionStatement other = (EventSubscriptionStatement) obj;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		if (operation != other.operation)
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}

	public EventSubscriptionOperation getOperation() {
		return operation;
	}

	public void setOperation(EventSubscriptionOperation operation) {
		this.operation = operation;
	}

	public IAssignableExpression getExpression() {
		return expression;
	}

	public void setExpression(IAssignableExpression expression) {
		this.expression = expression;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}
}
