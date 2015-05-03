package cc.kave.commons.model.ssts.impl.references;

import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class UnknownReference implements IUnknownReference {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
