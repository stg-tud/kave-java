package cc.kave.commons.model.ssts.expressions.assignable;

import java.util.Set;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;

public interface ILambdaExpression extends IAssignableExpression {

	@Nonnull
	Set<IVariableDeclaration> getParameters();

	@Nonnull
	Set<IStatement> getBody();
}
