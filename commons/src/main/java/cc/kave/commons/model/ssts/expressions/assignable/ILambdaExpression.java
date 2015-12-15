package cc.kave.commons.model.ssts.expressions.assignable;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.names.LambdaName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;

public interface ILambdaExpression extends IAssignableExpression {

	@NonNull
	LambdaName getName();

	@NonNull
	List<IStatement> getBody();
}
