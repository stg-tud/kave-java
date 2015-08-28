package cc.kave.commons.model.ssts.blocks;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;

public interface IDoLoop extends IStatement {

	@NonNull
	ILoopHeaderExpression getCondition();

	@NonNull
	List<IStatement> getBody();

}
