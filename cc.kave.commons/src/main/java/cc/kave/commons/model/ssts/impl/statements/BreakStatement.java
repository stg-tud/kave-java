package cc.kave.commons.model.ssts.impl.statements;

import java.util.ArrayList;

import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class BreakStatement implements IBreakStatement {
	
	@Override
	public Iterable<ISSTNode> getChildren() {
		return new ArrayList<ISSTNode>();
	}

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
