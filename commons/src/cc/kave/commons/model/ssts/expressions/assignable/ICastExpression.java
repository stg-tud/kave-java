package cc.kave.commons.model.ssts.expressions.assignable;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface ICastExpression extends IAssignableExpression {

	@NonNull
	TypeName getTargetType();

	@NonNull
	IVariableReference getReference();

	CastOperator getOperator();
}