package eclipse.commons.analysis.sstanalysistestsuite.statements;

import org.junit.Test;

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
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", newConstantValue("3")));
	}

	@Test
	public void assigningVariables() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT),
				newVariableDeclaration("j", SSTAnalysisFixture.INT), newAssignment("j", newConstantValue("0")),
				newAssignment("i", newReferenceExpression("j")));
	}

	@Test
	public void assigningMethods() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT),
				newAssignment("i", newReferenceExpression(newMethodRef(SSTAnalysisFixture.HASHCODE, "this"))));
	}

	@Test
	public void assigningField() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT),
				newAssignment("i", newReferenceExpression(newFieldReference("f", SSTAnalysisFixture.INT, "this"))));
	}

	@Test
	public void assigningInvocationExpression() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT),
				newAssignment("i", newInvokeExpression("this", SSTAnalysisFixture.HASHCODE)));
	}

	@Test
	public void assigningSelfAssignIsIgnored() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", newConstantValue("0")));
	}
}
