package cc.kave.commons.model.ssts.impl.references;

import cc.kave.commons.model.names.EventName;
import cc.kave.commons.model.names.csharp.CsEventName;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class EventReference implements IEventReference {

	private IVariableReference reference;
	private EventName eventName;

	public EventReference() {
		this.reference = new VariableReference();
		this.eventName = CsEventName.UNKNOWN_NAME;
	}

	@Override
	public IVariableReference getReference() {
		return reference;
	}

	@Override
	public EventName getEventName() {
		return eventName;
	}

	public void setReference(IVariableReference reference) {
		this.reference = reference;
	}

	public void setEventName(EventName eventName) {
		this.eventName = eventName;
	}

	@Override
	public int hashCode() {
		return 175 + this.eventName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof EventReference))
			return false;
		EventReference other = (EventReference) obj;
		if (eventName == null) {
			if (other.eventName != null)
				return false;
		} else if (!eventName.equals(other.eventName))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
