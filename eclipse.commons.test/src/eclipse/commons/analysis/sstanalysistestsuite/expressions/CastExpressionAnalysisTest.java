package eclipse.commons.analysis.sstanalysistestsuite.expressions;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsMethodName;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class CastExpressionAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void castingConstantValueKeyword() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.LONG),
				newVariableDeclaration("$0", SSTAnalysisFixture.INT), newAssignment("$0", newConstantValue("3")),
				newAssignment("i", newCastExpression(SSTAnalysisFixture.INT, newVariableReference("$0"))));
	}

	@Test
	public void castingConstantValueClassType() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.LONG),
				newVariableDeclaration("$0", SSTAnalysisFixture.INT), newAssignment("$0", newConstantValue("3")),
				newAssignment("i", newCastExpression(SSTAnalysisFixture.INT, newVariableReference("$0"))));
	}

	@Test
	public void castingConstantValueClassTypeFullQualified() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.LONG),
				newVariableDeclaration("$0", SSTAnalysisFixture.INT), newAssignment("$0", newConstantValue("3")),
				newAssignment("i", newCastExpression(SSTAnalysisFixture.INT, newVariableReference("$0"))));
	}

	@Test
	public void castingMethodResult() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.LONG),
				newVariableDeclaration("$0", SSTAnalysisFixture.INT),
				newAssignment("$0",
						newInvokeExpression("this",
								CsMethodName.newMethodName(
										"[%int, rt.jar, 1.8] [castexpressionanalysistest.CastingMethodResult, ?].z()"))),
				newAssignment("i", newCastExpression(SSTAnalysisFixture.INT, newVariableReference("$0"))));
	}
}
