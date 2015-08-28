package cc.kave.commons.model.ssts.blocks;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface IIfElseBlock extends IStatement {

	@NonNull
	ISimpleExpression getCondition();

	@NonNull
	List<IStatement> getThen();

	@NonNull
	List<IStatement> getElse();

}
