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
		this._else = new ArrayList<>();
		this.then = new ArrayList<>();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_else == null) ? 0 : _else.hashCode());
		result = prime * result + ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + ((then == null) ? 0 : then.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IfElseBlock))
			return false;
		IfElseBlock other = (IfElseBlock) obj;
		if (_else == null) {
			if (other._else != null)
				return false;
		} else if (!_else.equals(other._else))
			return false;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (then == null) {
			if (other.then != null)
				return false;
		} else if (!then.equals(other.then))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}
}
