package eclipse.commons.analysis.sstanalysistestsuite.statements;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;

public class BreakStatementAnalysisTest extends BaseSSTAnalysisTest {

	public BreakStatementAnalysisTest() {
		packageName = getClass().getSimpleName();
	}
	
	@Test
	public void breakInLoop() {
		updateContext();

		MethodDeclaration expected = newDefaultMethodDeclaration("");
		IMethodDeclaration actual = getFirstMethod();

		assertEquals(expected, actual);
	}

}
