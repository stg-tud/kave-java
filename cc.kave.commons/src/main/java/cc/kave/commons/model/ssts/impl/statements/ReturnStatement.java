package cc.kave.commons.model.ssts.impl.statements;

import java.util.ArrayList;

import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ReturnStatement implements IReturnStatement {

	private ISimpleExpression expression;
	private boolean isVoid;

	public ReturnStatement() {
		this.expression = new UnknownExpression();
		this.isVoid = false;
	}
	
	@Override
	public Iterable<ISSTNode> getChildren() {
		return new ArrayList<ISSTNode>();
	}

	@Override
	public boolean isVoid() {
		return isVoid;
	}

	public void setIsVoid(boolean isVoid) {
		this.isVoid = isVoid;
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expression == null) ? 0 : expression.hashCode());
		result = prime * result + (isVoid ? 1231 : 1237);
		return result;
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
		if (isVoid != other.isVoid)
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
