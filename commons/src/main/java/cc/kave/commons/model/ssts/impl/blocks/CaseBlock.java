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
		body = new ArrayList<>();
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CaseBlock))
			return false;
		CaseBlock other = (CaseBlock) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

}
