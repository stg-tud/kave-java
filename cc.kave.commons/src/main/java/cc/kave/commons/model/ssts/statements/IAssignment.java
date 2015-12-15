package cc.kave.commons.model.ssts.statements;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;

public interface IAssignment extends IStatement {

	@Nonnull
	IAssignableReference getReference();

	@Nonnull
	IAssignableExpression getExpression();
}
