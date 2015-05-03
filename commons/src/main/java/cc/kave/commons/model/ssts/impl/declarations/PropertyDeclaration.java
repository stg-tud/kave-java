package cc.kave.commons.model.ssts.impl.declarations;

import java.util.Set;

import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class PropertyDeclaration implements IPropertyDeclaration {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public PropertyName getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IStatement> getGet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IStatement> getSet() {
		// TODO Auto-generated method stub
		return null;
	}

}
