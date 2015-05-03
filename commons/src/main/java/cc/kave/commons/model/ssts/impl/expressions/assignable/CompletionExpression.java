package cc.kave.commons.model.ssts.impl.expressions.assignable;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class CompletionExpression implements ICompletionExpression {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public TypeName getTypeReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVariableReference getObjectReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
