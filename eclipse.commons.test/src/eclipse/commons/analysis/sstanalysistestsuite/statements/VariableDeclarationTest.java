package eclipse.commons.analysis.sstanalysistestsuite.statements;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class VariableDeclarationTest extends BaseSSTAnalysisTest {

	@Test
	public void basicVariableDeclaration() {
		MethodDeclaration expected = newDefaultMethodDeclaration();
		IVariableDeclaration decl = newVariableDeclaration("i", SSTAnalysisFixture.INT);
		expected.getBody().add(decl);

		IMethodDeclaration actual = getFirstMethod();

		assertEquals(expected, actual);
	}

	@Test
	public void declarationWithAssignment() {
		MethodDeclaration expected = newDefaultMethodDeclaration();
		VariableDeclaration decl = newVariableDeclaration("i", SSTAnalysisFixture.INT);
		Assignment assignment = newAssignment("i", newConstantValue("3"));
		expected.getBody().add(decl);
		expected.getBody().add(assignment);

		IMethodDeclaration actual = getFirstMethod();

		assertEquals(expected, actual);
	}

}
