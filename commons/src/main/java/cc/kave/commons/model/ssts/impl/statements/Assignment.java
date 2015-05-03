package cc.kave.commons.model.ssts.impl.statements;

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class Assignment implements IAssignment {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public IAssignableReference getReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAssignableExpression getExpression() {
		// TODO Auto-generated method stub
		return null;
	}

}
