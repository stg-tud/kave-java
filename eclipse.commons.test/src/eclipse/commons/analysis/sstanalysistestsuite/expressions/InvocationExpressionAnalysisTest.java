package eclipse.commons.analysis.sstanalysistestsuite.expressions;

import org.junit.Test;

import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class InvocationExpressionAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void invocationOnVariable() {
		assertMethod(newVariableDeclaration("o", SSTAnalysisFixture.OBJECT),
				newAssignment("o", newInvokeConstructor(SSTAnalysisFixture.OBJECT_CTOR)),
				newInvokeStatement("o", SSTAnalysisFixture.getHashCode(getDeclaringType())));
	}

	@Test
	public void invocationOnVariableImplicitThis() {
		assertMethod(newInvokeStatement("this", SSTAnalysisFixture.getHashCode(getDeclaringType())));
	}
	
	@Test
	public void invocationOnVariableExplicitThis() {
		assertMethod(newInvokeStatement("this", SSTAnalysisFixture.getHashCode(getDeclaringType())));
	}
}
