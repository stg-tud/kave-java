package cc.kave.commons.model.ssts.expressions.assignable;

import java.util.Set;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;

public interface IComposedExpression extends IAssignableExpression {

	@Nonnull
	Set<IVariableReference> getReferences();

}
