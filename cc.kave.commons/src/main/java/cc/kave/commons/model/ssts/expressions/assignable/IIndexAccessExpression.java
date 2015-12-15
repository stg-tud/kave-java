package cc.kave.commons.model.ssts.expressions.assignable;

import java.util.List;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface IIndexAccessExpression extends IAssignableExpression {

	@Nonnull
	IVariableReference getReference();

	@Nonnull
	List<ISimpleExpression> getIndices();
}