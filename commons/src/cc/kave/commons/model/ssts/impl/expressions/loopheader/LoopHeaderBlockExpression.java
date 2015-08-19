package cc.kave.commons.model.ssts.impl.expressions.loopheader;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class LoopHeaderBlockExpression implements ILoopHeaderBlockExpression {

	private List<IStatement> body;

	public LoopHeaderBlockExpression() {
		this.body = new ArrayList<>();
	}

	@Override
	public List<IStatement> getBody() {
		return this.body;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	@Override
	public int hashCode() {
		return (4874 + this.body.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LoopHeaderBlockExpression))
			return false;
		LoopHeaderBlockExpression other = (LoopHeaderBlockExpression) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}
}
