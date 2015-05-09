package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class WhileLoop implements IWhileLoop {

	private ILoopHeaderExpression condition;
	private List<IStatement> body;

	public WhileLoop() {
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

	public void setCondition(ILoopHeaderExpression condition) {
		this.condition = condition;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	private boolean equals(WhileLoop other) {
		return this.body.equals(other.getBody()) && this.condition.equals(other.getCondition());
	}

	public boolean equals(Object obj) {
		return obj instanceof WhileLoop ? this.equals((WhileLoop) obj) : false;
	}

	public int hashCode() {
		return (40 + (this.body.hashCode() * 397) ^ this.condition.hashCode());
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
