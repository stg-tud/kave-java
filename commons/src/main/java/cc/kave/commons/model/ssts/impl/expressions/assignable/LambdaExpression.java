package cc.kave.commons.model.ssts.impl.expressions.assignable;

import java.util.Set;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class LambdaExpression implements ILambdaExpression {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public Set<IVariableDeclaration> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IStatement> getBody() {
		// TODO Auto-generated method stub
		return null;
	}

}
