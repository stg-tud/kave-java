package cc.kave.commons.model.ssts.impl.declarations;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.IEventName;
import cc.kave.commons.model.names.csharp.EventName;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class EventDeclaration implements IEventDeclaration {

	private IEventName name;

	public EventDeclaration() {
		this.name = EventName.UNKNOWN_NAME;
	}
	
	@Override
	public Iterable<ISSTNode> getChildren() {
		return Lists.newArrayList();
	}

	@Override
	public IEventName getName() {
		return name;
	}

	public void setName(IEventName name) {
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
