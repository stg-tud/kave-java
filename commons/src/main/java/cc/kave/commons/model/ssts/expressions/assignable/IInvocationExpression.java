package cc.kave.commons.model.ssts.expressions.assignable;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface IInvocationExpression extends IAssignableExpression {

	@NonNull
	IVariableReference getReference();

	@NonNull
	MethodName getMethodName();

	@NonNull
	List<ISimpleExpression> getParameters();
}
