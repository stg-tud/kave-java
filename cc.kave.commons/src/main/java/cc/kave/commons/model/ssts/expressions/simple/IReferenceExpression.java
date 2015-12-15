package cc.kave.commons.model.ssts.expressions.simple;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;

public interface IReferenceExpression extends ISimpleExpression {

	@NonNull
	IReference getReference();
}
