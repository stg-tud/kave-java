package cc.kave.commons.model.ssts.expressions.loopheader;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;

public interface ILoopHeaderBlockExpression extends ILoopHeaderExpression {

	@NonNull
	List<IStatement> getBody();

}
