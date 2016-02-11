package eclipse.commons.analysis.sstanalysistestsuite.expressions;

import org.junit.Test;

import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class ConstantValueExpressionAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void intValue() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", newConstantValue("3")));
	}

	@Test
	public void plusIntPrefix() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT),
				newAssignment("i", newUnaryExpression(newConstantValue("3"), UnaryOperator.Plus)));
	}

	@Test
	public void minusIntPrefix() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT),
				newAssignment("i", newUnaryExpression(newConstantValue("3"), UnaryOperator.Minus)));
	}

	@Test
	public void doubleValueWithF() {
		assertMethod(newVariableDeclaration("d", SSTAnalysisFixture.DOUBLE),
				newAssignment("d", newConstantValue("3.0")));
	}

	@Test
	public void booleanValue() {
		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN),
				newAssignment("b", newConstantValue("false")));
	}

	@Test
	public void nullExpression() {
		assertMethod(newVariableDeclaration("s", SSTAnalysisFixture.STRING), newAssignment("s", new NullExpression()));
	}
}