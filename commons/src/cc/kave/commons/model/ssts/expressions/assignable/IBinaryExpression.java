package cc.kave.commons.model.ssts.expressions.assignable;

<<<<<<< HEAD
import org.eclipse.jdt.annotation.NonNull;
=======
import javax.annotation.Nonnull;
>>>>>>> added Unary/Binary Expression to SST model

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface IBinaryExpression extends IAssignableExpression {

	BinaryOperator getOperator();

	@NonNull
	ISimpleExpression getRightOperand();

	@NonNull
	ISimpleExpression getLeftOperand();
}