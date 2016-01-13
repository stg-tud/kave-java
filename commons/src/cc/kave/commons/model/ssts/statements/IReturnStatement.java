package cc.kave.commons.model.ssts.statements;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface IReturnStatement extends IStatement {

	@NonNull
	ISimpleExpression getExpression();

	boolean isVoid();

}
