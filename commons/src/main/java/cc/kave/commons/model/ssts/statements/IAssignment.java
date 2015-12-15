package cc.kave.commons.model.ssts.statements;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;

public interface IAssignment extends IStatement {

	@NonNull
	IAssignableReference getReference();

	@NonNull
	IAssignableExpression getExpression();
}
