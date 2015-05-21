package cc.kave.commons.model.ssts.impl.statements;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class LabelledStatement implements ILabelledStatement {

	private String label;
	private IStatement statement;

	public LabelledStatement() {
		this.label = "";
		this.statement = new UnknownStatement();
	}

	@Override
	public String getLabel() {
		return this.label;
	}

	@Override
	public IStatement getStatement() {
		return this.statement;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setStatement(IStatement statement) {
		this.statement = statement;
	}

	@Override
	public int hashCode() {
		return 15 + (this.label.hashCode() * 397) ^ this.statement.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LabelledStatement))
			return false;
		LabelledStatement other = (LabelledStatement) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (statement == null) {
			if (other.statement != null)
				return false;
		} else if (!statement.equals(other.statement))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
