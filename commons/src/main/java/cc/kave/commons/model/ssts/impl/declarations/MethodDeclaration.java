package cc.kave.commons.model.ssts.impl.declarations;

import java.util.Set;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class MethodDeclaration implements IMethodDeclaration {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public MethodName getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getIsEntryPoint() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<IStatement> getBody() {
		// TODO Auto-generated method stub
		return null;
	}

}
