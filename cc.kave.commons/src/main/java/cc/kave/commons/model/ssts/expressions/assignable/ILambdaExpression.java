package cc.kave.commons.model.ssts.expressions.assignable;

import java.util.List;

import javax.annotation.Nonnull;

import cc.kave.commons.model.names.ILambdaName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;

public interface ILambdaExpression extends IAssignableExpression {

	@Nonnull
	ILambdaName getName();

	@Nonnull
	List<IStatement> getBody();
}
