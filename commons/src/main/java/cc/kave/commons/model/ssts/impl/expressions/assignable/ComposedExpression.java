package cc.kave.commons.model.ssts.impl.expressions.assignable;

import java.util.Set;

import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ComposedExpression implements IComposedExpression {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public Set<IVariableReference> getReferences() {
		// TODO Auto-generated method stub
		return null;
	}

}
