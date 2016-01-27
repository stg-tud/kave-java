package eclipse.commons.analysis.sstanalysistestsuite.statements;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class AssignmentAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void variableInitAssignment() {
		assertMethod((newVariableDeclaration("i", SSTAnalysisFixture.INT)), newAssignment("i", newConstantValue("3")));
	}

	@Test
	public void fieldAssignment() {
		assertMethod(newAssignment(newFieldReference("i", SSTAnalysisFixture.INT, "this"), newConstantValue("3")));
	}

	@Test
	public void variableAssignment() {
	}

	// TODO: tests not complete

}
