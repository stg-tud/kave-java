package cc.kave.commons.model.ssts.impl.expressions.assignable;

import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class IfElseExpression implements IIfElseExpression {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public ISimpleExpression getCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISimpleExpression getThenExpression() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISimpleExpression getElseExpression() {
		// TODO Auto-generated method stub
		return null;
	}

}
