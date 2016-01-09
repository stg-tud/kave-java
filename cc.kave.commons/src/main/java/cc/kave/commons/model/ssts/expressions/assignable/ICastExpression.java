package cc.kave.commons.model.ssts.expressions.assignable;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface ICastExpression extends IAssignableExpression {

	@Nonnull
	ITypeName getTargetType();
	
	CastOperator getOperator();

	@Nonnull
	IVariableReference getReference();

}