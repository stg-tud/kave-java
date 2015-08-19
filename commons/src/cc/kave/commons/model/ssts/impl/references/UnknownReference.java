package cc.kave.commons.model.ssts.impl.references;

import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class UnknownReference implements IUnknownReference {

	public boolean equals(Object obj) {
		return obj instanceof UnknownReference ? true : false;
	}

	public int hashCode() {
		return -612359;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
