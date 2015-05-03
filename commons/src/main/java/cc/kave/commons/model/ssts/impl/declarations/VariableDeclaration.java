package cc.kave.commons.model.ssts.impl.declarations;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class VariableDeclaration implements IVariableDeclaration {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public IVariableReference getReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeName getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getIsMissing() {
		// TODO Auto-generated method stub
		return false;
	}

}
