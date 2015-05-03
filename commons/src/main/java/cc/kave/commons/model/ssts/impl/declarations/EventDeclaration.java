package cc.kave.commons.model.ssts.impl.declarations;

import cc.kave.commons.model.names.EventName;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class EventDeclaration implements IEventDeclaration {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public EventName getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
