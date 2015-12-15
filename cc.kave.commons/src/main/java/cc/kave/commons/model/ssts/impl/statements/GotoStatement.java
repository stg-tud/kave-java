package cc.kave.commons.model.ssts.impl.statements;

import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class GotoStatement implements IGotoStatement {

	private String label;

	public GotoStatement() {
		this.label = "";
	}

	@Override
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public int hashCode() {
		return 14 + this.label.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GotoStatement))
			return false;
		GotoStatement other = (GotoStatement) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
