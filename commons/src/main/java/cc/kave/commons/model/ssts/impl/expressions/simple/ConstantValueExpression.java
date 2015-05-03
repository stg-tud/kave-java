package cc.kave.commons.model.ssts.impl.expressions.simple;

import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ConstantValueExpression implements IConstantValueExpression {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
