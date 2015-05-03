package cc.kave.commons.model.ssts.impl.statements;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ThrowStatement implements IThrowStatement {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public TypeName getException() {
		// TODO Auto-generated method stub
		return null;
	}

}
