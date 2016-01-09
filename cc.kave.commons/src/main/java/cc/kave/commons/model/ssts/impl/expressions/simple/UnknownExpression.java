package cc.kave.commons.model.ssts.impl.expressions.simple;

import java.util.ArrayList;

import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class UnknownExpression implements IUnknownExpression {
	
	@Override
	public Iterable<ISSTNode> getChildren() {
		return new ArrayList<ISSTNode>();
	}

	public int hashCode() {
		return 378;
	}

	public boolean equals(Object obj) {
		return obj instanceof UnknownExpression ? true : false;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
