package cc.kave.commons.model.ssts.impl.statements;

import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ReturnStatement implements IReturnStatement {

	private ISimpleExpression expression;

	public ReturnStatement() {
		this.expression = new UnknownExpression();
	}

	@Override
	public ISimpleExpression getExpression() {
		return this.expression;
	}

	public void setExpression(ISimpleExpression expression) {
		this.expression = expression;
	}

	@Override
	public int hashCode() {
		return 17 + this.expression.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ReturnStatement))
			return false;
		ReturnStatement other = (ReturnStatement) obj;
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
