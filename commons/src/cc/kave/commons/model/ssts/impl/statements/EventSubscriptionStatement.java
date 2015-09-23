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
	}

	public IAssignableReference getReference() {
		return reference;
	}

	public void setReference(IAssignableReference reference) {
		this.reference = reference;
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
