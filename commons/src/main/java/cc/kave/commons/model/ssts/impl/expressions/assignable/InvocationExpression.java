package cc.kave.commons.model.ssts.impl.expressions.assignable;

import java.util.Set;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class InvocationExpression implements IInvocationExpression {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public IVariableReference getReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodName getMethodName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ISimpleExpression> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

}
