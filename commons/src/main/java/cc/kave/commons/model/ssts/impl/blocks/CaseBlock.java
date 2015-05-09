package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;

public class CaseBlock implements ICaseBlock {

	private ISimpleExpression label;
	private List<IStatement> body;

	public CaseBlock() {
		label = new UnknownExpression();
		body = new ArrayList<IStatement>();
	}

	@Override
	public ISimpleExpression getLabel() {
		return label;
	}

	@Override
	public List<IStatement> getBody() {
		return body;
	}

	public void setLabel(ISimpleExpression label) {
		this.label = label;
	}

	public void setBody(List<IStatement> body) {
		this.body = body;
	}

	private boolean equals(CaseBlock other) {
		return this.body.equals(other.getBody()) && this.label.equals(other.getLabel());
	}

	public boolean equals(Object obj) {
		return obj instanceof CaseBlock ? this.equals((CaseBlock) obj) : false;
	}

	public int hashCode() {
		return 30 + (this.body.hashCode() * 397) ^ this.label.hashCode();
	}

}
