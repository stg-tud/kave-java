package cc.kave.commons.model.ssts.expressions.loopheader;

import java.util.Set;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;

public interface ILoopHeaderBlockExpression extends ILoopHeaderExpression {

	@Nonnull
	Set<IStatement> getBody();

}
