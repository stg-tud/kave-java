package cc.kave.commons.model.ssts.impl.transformation.loops;

import java.util.List;

import cc.kave.commons.model.ssts.IStatement;

public class LoopNormalizationContext {
	private StatementInsertionVisitor insertionVisitor;

	public LoopNormalizationContext() {
		this.insertionVisitor = new StatementInsertionVisitor();
	}

	public List<IStatement> replicateLoopStep(List<IStatement> statements, List<IStatement> loopStep) {
		return insertionVisitor.visit(statements, new StepInsertionContext(loopStep));
	}

}
