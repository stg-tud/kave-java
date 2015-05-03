package cc.kave.commons.model.ssts.impl.statements;

import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ReturnStatement implements IReturnStatement {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public ISimpleExpression getExpression() {
		// TODO Auto-generated method stub
		return null;
	}

}
