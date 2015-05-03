package cc.kave.commons.model.ssts.impl.declarations;

import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class FieldDeclaration implements IFieldDeclaration {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public FieldName getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
