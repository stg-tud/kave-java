package cc.kave.commons.model.ssts.impl.statements;

import java.util.ArrayList;

import cc.kave.commons.model.ssts.statements.IUnknownStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class UnknownStatement implements IUnknownStatement {

	@Override
	public Iterable<ISSTNode> getChildren() {
		return new ArrayList<ISSTNode>();
	}
	
	public boolean equals(Object obj) {
		return obj instanceof UnknownStatement ? true : false;
	}

	public int hashCode() {
		return -102;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
