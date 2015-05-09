package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class ForLoop implements IForLoop {

	private List<IStatement> init;
	private List<IStatement> step;
	private List<IStatement> body;
	private ILoopHeaderExpression condition;

	public ForLoop() {
		this.init = new ArrayList<IStatement>();
		this.step = new ArrayList<IStatement>();
		this.body = new ArrayList<IStatement>();
		this.condition = new UnknownExpression();
	}

	@Override
	public List<IStatement> getInit() {
		return this.init;
	}

	@Override
	public ILoopHeaderExpression getCondition() {
		return this.condition;
	}

	@Override
	public List<IStatement> getStep() {
		return this.step;
	}

	@Override
	public List<IStatement> getBody() {
		return this.body;
	}

	public void setInit(List<IStatement> init) {
		this.init = init;
	}

	public void setStep(List<IStatement> step) {
		this.step = step;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	public void setCondition(ILoopHeaderExpression condition) {
		this.condition = condition;
	}

	private boolean equals(ForLoop other) {
		return this.body.equals(other.getBody()) && this.step.equals(other.getStep())
				&& this.init.equals(other.getStep()) && this.condition.equals(other.getCondition());
	}

	public boolean equals(Object obj) {
		return obj instanceof ForLoop ? this.equals((ForLoop) obj) : false;
	}

	public int hashCode() {
		int hashCode = 34 + this.init.hashCode();
		hashCode = (hashCode * 397) ^ this.step.hashCode();
		hashCode = (hashCode * 397) ^ this.body.hashCode();
		hashCode = (hashCode * 397) ^ this.condition.hashCode();
		return hashCode;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
