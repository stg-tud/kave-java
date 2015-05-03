package cc.kave.commons.model.ssts.impl.statements;

import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ContinueStatement implements IContinueStatement {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
