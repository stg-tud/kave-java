package cc.kave.commons.model.ssts.impl.expressions.simple;

import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class UnknownExpression implements IUnknownExpression {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
