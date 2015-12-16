package cc.kave.commons.model.ssts.expressions.assignable;

import java.util.List;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface IInvocationExpression extends IAssignableExpression {

	@Nonnull
	IVariableReference getReference();

	@Nonnull
	IMethodName getMethodName();

	@Nonnull
	List<ISimpleExpression> getParameters();
}
