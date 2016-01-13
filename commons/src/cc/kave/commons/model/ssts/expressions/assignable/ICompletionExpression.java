package cc.kave.commons.model.ssts.expressions.assignable;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface ICompletionExpression extends IAssignableExpression {

	@Nullable
	TypeName getTypeReference();

	@Nullable
	IVariableReference getObjectReference();

	@NonNull
	String getToken();
}
