package cc.kave.commons.model.ssts.references;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;

public interface IIndexAccessReference extends IAssignableReference {

	@NonNull
	IIndexAccessExpression getExpression();
}