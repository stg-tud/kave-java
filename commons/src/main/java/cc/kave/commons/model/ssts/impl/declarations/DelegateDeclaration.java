package cc.kave.commons.model.ssts.impl.declarations;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class DelegateDeclaration implements IDelegateDeclaration {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public TypeName getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
