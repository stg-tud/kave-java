package cc.kave.commons.model.ssts.statements;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;

public interface IExpressionStatement extends IStatement {

	@NonNull
	IAssignableExpression getExpression();
}
