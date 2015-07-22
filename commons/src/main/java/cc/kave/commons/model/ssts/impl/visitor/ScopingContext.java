package cc.kave.commons.model.ssts.impl.visitor;

import cc.kave.commons.model.ssts.IStatement;

public class ScopingContext {
	private IStatement statement;
	private boolean inBlock;
	private boolean afterStatement;

	public ScopingContext(IStatement statement, boolean inBlock) {
		this.statement = statement;
		this.inBlock = inBlock;
		this.afterStatement = false;
	}

	public IStatement getStatement() {
		return statement;
	}

	public void setStatement(IStatement statement) {
		this.statement = statement;
	}

	public boolean isInBlock() {
		return inBlock;
	}

	public void setInBlock(boolean inBlock) {
		this.inBlock = inBlock;
	}

	public boolean isAfterStatement() {
		return afterStatement;
	}

	public void setAfterStatement(boolean afterStatement) {
		this.afterStatement = afterStatement;
	}

}
