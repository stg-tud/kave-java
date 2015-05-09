package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class IfElseBlock implements IIfElseBlock {

	private List<IStatement> then;
	private List<IStatement> _else;
	private ISimpleExpression condition;

	public IfElseBlock() {
		this.condition = new UnknownExpression();
		this._else = new ArrayList<IStatement>();
		this.then = new ArrayList<IStatement>();
	}

	@Override
	public ISimpleExpression getCondition() {
		return this.condition;
	}

	@Override
	public List<IStatement> getThen() {
		return this.then;
	}

	@Override
	public List<IStatement> getElse() {
		return this._else;
	}

	public void setElse(List<IStatement> _else) {
		this._else = _else;
	}

	public void setThen(List<IStatement> then) {
		this.then = then;
	}

	public void setCondition(ISimpleExpression condition) {
		this.condition = condition;
	}

	private boolean equals(IfElseBlock other) {
		return this.then.equals(other.getThen()) && this._else.equals(other.getElse())
				&& this.condition.equals(other.getCondition());
	}

	public boolean equals(Object obj) {
		return obj instanceof IfElseBlock ? this.equals((IfElseBlock) obj) : false;
	}

	public int hashCode() {
		int hashCode = 35 + this.then.hashCode();
		hashCode = (hashCode * 397) ^ this._else.hashCode();
		hashCode = (hashCode * 397) ^ this.condition.hashCode();
		return hashCode;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}
}
