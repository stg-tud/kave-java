package cc.kave.commons.model.ssts.impl.expressions.simple;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ReferenceExpression implements IReferenceExpression {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public IReference getReference() {
		// TODO Auto-generated method stub
		return null;
	}

}
