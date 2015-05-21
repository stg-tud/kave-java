package cc.kave.commons.model.ssts.impl.statements;

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ExpressionStatement implements IExpressionStatement {

	private IAssignableExpression expression;

	public ExpressionStatement() {
		this.expression = new UnknownExpression();
	}

	@Override
	public IAssignableExpression getExpression() {
		return this.expression;
	}

	public void setExpression(IAssignableExpression expression) {
		this.expression = expression;
	}

	@Override
	public int hashCode() {
		return 12946783 + this.expression.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ExpressionStatement))
			return false;
		ExpressionStatement other = (ExpressionStatement) obj;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
