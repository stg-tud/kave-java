package eclipse.commons.analysis.sstanalysistestsuite.statements;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;

public class ContinueStatementAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void continueInLoop() {
		MethodDeclaration expected = newDefaultMethodDeclaration();
		WhileLoop loop = new WhileLoop();
		loop.setCondition(newConstantValue("true"));
		loop.getBody().add(new ContinueStatement());
		expected.getBody().add(loop);

		IMethodDeclaration actual = getFirstMethod();

		assertEquals(expected, actual);
	}

}
