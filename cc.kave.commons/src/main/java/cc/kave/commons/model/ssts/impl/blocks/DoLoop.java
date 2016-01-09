package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class DoLoop implements IDoLoop {

	private ILoopHeaderExpression condition;
	private List<IStatement> body;

	public DoLoop() {
		this.condition = new UnknownExpression();
		this.body = new ArrayList<>();
	}
	
	@Override
	public Iterable<ISSTNode> getChildren() {
		List<ISSTNode> children = Lists.newArrayList(condition);
		children.addAll(body);
		return children;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((condition == null) ? 0 : condition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DoLoop))
			return false;
		DoLoop other = (DoLoop) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		return true;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
