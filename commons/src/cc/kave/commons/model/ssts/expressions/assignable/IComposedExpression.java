package cc.kave.commons.model.ssts.expressions.assignable;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface IComposedExpression extends IAssignableExpression {

	@NonNull
	List<IVariableReference> getReferences();

}
