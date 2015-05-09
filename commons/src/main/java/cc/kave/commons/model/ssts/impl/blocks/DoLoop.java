package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class DoLoop implements IDoLoop {

	private ILoopHeaderExpression condition;
	private List<IStatement> body;

	public DoLoop() {
		this.condition = new UnknownExpression();
		this.body = new ArrayList<IStatement>();
	}

	@Override
	public ILoopHeaderExpression getCondition() {
		return this.condition;
	}

	@Override
	public List<IStatement> getBody() {
		return this.body;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	public void setCondition(ILoopHeaderExpression condition) {
		this.condition = condition;
	}

	private boolean equals(DoLoop other) {
		return this.body.equals(other.getBody()) && this.condition.equals(other.getCondition());
	}

	public boolean equals(Object obj) {
		return obj instanceof DoLoop ? this.equals((DoLoop) obj) : false;
	}

	public int hashCode() {
		return 32 + (this.body.hashCode() * 397) ^ this.condition.hashCode();
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}
}
