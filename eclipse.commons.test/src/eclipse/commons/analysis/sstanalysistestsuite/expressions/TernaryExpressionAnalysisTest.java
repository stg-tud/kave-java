package eclipse.commons.analysis.sstanalysistestsuite.expressions;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class TernaryExpressionAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void constantTernaryExpression() {
		IfElseExpression ifElseExpression = new IfElseExpression();
		ifElseExpression.setCondition(newConstantValue("true"));
		ifElseExpression.setThenExpression(newConstantValue("1"));
		ifElseExpression.setElseExpression(newConstantValue("2"));

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", ifElseExpression));
	}

	@Test
	public void ternaryExpressionWithVariables() {
		IfElseExpression ifElseExpression = new IfElseExpression();
		ifElseExpression.setCondition(newReferenceExpression("b"));
		ifElseExpression.setThenExpression(newConstantValue("1"));
		ifElseExpression.setElseExpression(newConstantValue("2"));

		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN),
				newAssignment("b", newConstantValue("true")), newVariableDeclaration("i", SSTAnalysisFixture.INT),
				newAssignment("i", ifElseExpression));
	}
}
