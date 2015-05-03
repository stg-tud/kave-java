package cc.kave.commons.model.ssts.impl.references;

import cc.kave.commons.model.names.EventName;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class EventReference implements IEventReference {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public IVariableReference getReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventName getEventName() {
		// TODO Auto-generated method stub
		return null;
	}

}
