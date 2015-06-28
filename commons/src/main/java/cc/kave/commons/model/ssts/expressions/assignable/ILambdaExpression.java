package cc.kave.commons.model.ssts.expressions.assignable;

import java.util.List;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.LambdaName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;

public interface ILambdaExpression extends IAssignableExpression {

	@Nonnull
	LambdaName getName();

	@Nonnull
	List<IStatement> getBody();
}
