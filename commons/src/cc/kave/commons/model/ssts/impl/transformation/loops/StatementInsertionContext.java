package cc.kave.commons.model.ssts.impl.transformation.loops;

import java.util.List;

import cc.kave.commons.model.ssts.IStatement;

public abstract class StatementInsertionContext {
	protected List<IStatement> statements;

	public StatementInsertionContext(List<IStatement> statements) {
		this.statements = statements;
	}

	public boolean insertBefore(IStatement stmt) {
		return false;
	}

	public boolean insertAfter(IStatement stmt) {
		return false;
	}
}
