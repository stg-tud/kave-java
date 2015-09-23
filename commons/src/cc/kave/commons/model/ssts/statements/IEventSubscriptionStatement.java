package cc.kave.commons.model.ssts.statements;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;

public interface IEventSubscriptionStatement extends IStatement {

	@Nonnull
	IAssignableReference getReference();

	@Nonnull
	EventSubscriptionOperation getOperation();

	void setOperation(EventSubscriptionOperation operation);

	@Nonnull
	IAssignableExpression getExpression();

	void setExpression(IAssignableExpression expr);

}
