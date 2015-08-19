package cc.kave.commons.model.ssts.impl.statements;

import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class BreakStatement implements IBreakStatement {

	public boolean equals(Object obj) {
		return obj instanceof BreakStatement ? true : false;
	}

	public int hashCode() {
		return 12;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
