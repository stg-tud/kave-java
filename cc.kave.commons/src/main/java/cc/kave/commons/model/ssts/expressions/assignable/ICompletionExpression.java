package cc.kave.commons.model.ssts.expressions.assignable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface ICompletionExpression extends IAssignableExpression {

	@Nullable
	ITypeName getTypeReference();

	@Nullable
	IVariableReference getVariableReference();

	@Nonnull
	String getToken();
}
