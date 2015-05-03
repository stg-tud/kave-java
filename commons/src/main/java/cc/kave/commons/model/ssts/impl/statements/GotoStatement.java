package cc.kave.commons.model.ssts.impl.statements;

import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class GotoStatement implements IGotoStatement {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

}
