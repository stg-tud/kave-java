package cc.kave.commons.model.ssts.impl.expressions.assignable;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class LambdaExpression implements ILambdaExpression {

	private List<IVariableDeclaration> parameters;
	private List<IStatement> body;

	public LambdaExpression() {
		this.parameters = new ArrayList<IVariableDeclaration>();
		this.body = new ArrayList<IStatement>();
	}

	@Override
	public List<IVariableDeclaration> getParameters() {
		return this.parameters;
	}

	@Override
	public List<IStatement> getBody() {
		return this.body;
	}

	public void setParameters(List<IVariableDeclaration> parameters) {
		this.parameters = parameters;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	@Override
	public int hashCode() {
		return (2990306 + this.body.hashCode() * 5 + this.parameters.hashCode() * 3);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LambdaExpression))
			return false;
		LambdaExpression other = (LambdaExpression) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
