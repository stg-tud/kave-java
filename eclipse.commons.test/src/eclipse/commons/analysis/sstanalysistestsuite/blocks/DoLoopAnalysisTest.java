package eclipse.commons.analysis.sstanalysistestsuite.blocks;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class DoLoopAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void basicDoLoop() {
		DoLoop loop = new DoLoop();
		loop.setCondition(newConstantValue("true"));

		assertMethod(loop);
	}

	@Test
	public void withStatementInBody() {
		DoLoop loop = new DoLoop();
		loop.setCondition(newConstantValue("true"));
		loop.getBody().add(new BreakStatement());

		assertMethod(loop);
	}

	@Test
	public void loopHeaderBlockCondition() {
		DoLoop loop = new DoLoop();

		ReturnStatement returnStatement = new ReturnStatement();
		returnStatement.setExpression(newReferenceExpression("$0"));

		LoopHeaderBlockExpression blockExpression = new LoopHeaderBlockExpression();
		blockExpression.getBody().add(newVariableDeclaration("$0", SSTAnalysisFixture.BOOLEAN));
		blockExpression.getBody().add(newAssignment("$0", newInvokeExpression("this", CsMethodName
				.newMethodName("[%boolean, rt.jar, 1.8] [doloopanalysistest.LoopHeaderBlockCondition, ?].isX()"))));
		blockExpression.getBody().add(returnStatement);

		loop.setCondition(blockExpression);

		assertMethod(loop);
	}

}
