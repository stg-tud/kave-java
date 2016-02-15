package eclipse.commons.analysis.sstanalysistestsuite.expressions;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class LoopHeaderExpressionAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void constantValue() {
		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setCondition(newConstantValue("true"));

		assertMethod(whileLoop);
	}

	@Test
	public void simpleExpression() {
		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setCondition(newReferenceExpression("b"));

		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN),
				newAssignment("b", newConstantValue("true")), whileLoop);
	}

	@Test
	public void methodInvocation() {
		WhileLoop whileLoop = new WhileLoop();
		LoopHeaderBlockExpression loopHeaderBlock = new LoopHeaderBlockExpression();
		loopHeaderBlock.getBody().add(newVariableDeclaration("$0", SSTAnalysisFixture.BOOLEAN));
		loopHeaderBlock.getBody().add(newAssignment("$0", newInvokeExpression("this", CsMethodName.newMethodName("[%boolean, rt.jar, 1.8] [loopheaderexpressionanalysistest.MethodInvocation, ?].isX()"))));
		loopHeaderBlock.getBody().add(newReturnStatement(newReferenceExpression("$0"), false));

		whileLoop.setCondition(loopHeaderBlock);

		assertMethod(whileLoop);
	}
}
