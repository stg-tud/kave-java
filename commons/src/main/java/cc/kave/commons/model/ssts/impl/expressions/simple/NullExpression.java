package cc.kave.commons.model.ssts.impl.expressions.simple;

import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class NullExpression implements INullExpression {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
