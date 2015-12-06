package cc.kave.commons.model.ssts.expressions.assignable;

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface IBooleanExpression extends IAssignableExpression {

	ISimpleExpression getLeft();

	// TODO: introduce enum, e.g., (==, !=, <, >)
	String getOperation();

	// use case: if(o == null) { ... }

	ISimpleExpression getRight();
}
