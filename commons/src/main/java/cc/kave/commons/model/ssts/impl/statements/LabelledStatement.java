package cc.kave.commons.model.ssts.impl.statements;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class LabelledStatement implements ILabelledStatement {

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IStatement getStatement() {
		// TODO Auto-generated method stub
		return null;
	}

}
