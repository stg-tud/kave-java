package cc.kave.commons.model.ssts.impl.declarations;

import cc.kave.commons.model.names.EventName;
import cc.kave.commons.model.names.csharp.CsEventName;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class EventDeclaration implements IEventDeclaration {

	private EventName name;

	public EventDeclaration() {
		this.name = CsEventName.UNKNOWN_NAME;
	}

	@Override
	public EventName getName() {
		return name;
	}

	public void setName(EventName name) {
		this.name = name;
	}

	private boolean equals(EventDeclaration other) {
		return this.name.equals(other.getName());
	}

	public boolean equals(Object obj) {
		return obj instanceof EventDeclaration ? this.equals((EventDeclaration) obj) : false;
	}

	public int hashCode() {
		return 22 + this.name.hashCode();
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
