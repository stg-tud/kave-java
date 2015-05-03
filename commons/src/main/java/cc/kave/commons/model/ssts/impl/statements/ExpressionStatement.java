package cc.kave.commons.model.ssts.impl.statements;

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ExpressionStatement implements IExpressionStatement {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public IAssignableExpression getExpression() {
		// TODO Auto-generated method stub
		return null;
	}

}
