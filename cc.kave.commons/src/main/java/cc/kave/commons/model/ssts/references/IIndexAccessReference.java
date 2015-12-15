package cc.kave.commons.model.ssts.references;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;

public interface IIndexAccessReference extends IAssignableReference {

	@Nonnull
	IIndexAccessExpression getExpression();
}