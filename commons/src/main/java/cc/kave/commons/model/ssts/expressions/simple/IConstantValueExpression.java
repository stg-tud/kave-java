package cc.kave.commons.model.ssts.expressions.simple;

import org.eclipse.jdt.annotation.Nullable;

import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface IConstantValueExpression extends ISimpleExpression {

	@Nullable
	String getValue();

}
