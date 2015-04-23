package cc.kave.commons.model.ssts.expressions.simple;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface IReferenceExpression extends ISimpleExpression {

	@Nonnull
	IReference getReference();
}
