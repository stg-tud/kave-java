package cc.kave.commons.model.ssts.blocks;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface ICaseBlock {

	@NonNull
	ISimpleExpression getLabel();

	@NonNull
	List<IStatement> getBody();
}
