package cc.kave.commons.model.ssts.impl.transformation.loops;

import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.statements.IContinueStatement;

public class StepInsertionContext extends StatementInsertionContext {

	public StepInsertionContext(List<IStatement> statements) {
		super(statements);
	}

	@Override
	public boolean insertBefore(IStatement stmt) {
		return stmt instanceof IContinueStatement;
	}
}
