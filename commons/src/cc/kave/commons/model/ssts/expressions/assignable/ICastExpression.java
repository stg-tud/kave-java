package cc.kave.commons.model.ssts.expressions.assignable;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface ICastExpression extends IAssignableExpression {

	@Nonnull
	TypeName getTargetType();

	@Nonnull
	IVariableReference getReference();

}
