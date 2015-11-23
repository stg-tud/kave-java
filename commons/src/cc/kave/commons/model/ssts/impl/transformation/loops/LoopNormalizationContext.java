package cc.kave.commons.model.ssts.impl.transformation.loops;

import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class LoopNormalizationContext {
	private StatementInsertionVisitor insertionVisitor;

	public LoopNormalizationContext() {
		this.insertionVisitor = new StatementInsertionVisitor();
	}

	public void replicateLoopStep(ISSTNode node, List<IStatement> loopStep) {
		node.accept(insertionVisitor, new StepInsertionContext(loopStep));
	}

	public List<IStatement> replicateLoopStep(List<IStatement> statements, List<IStatement> loopStep) {
		return insertionVisitor.visit(statements, new StepInsertionContext(loopStep));
	}

}
