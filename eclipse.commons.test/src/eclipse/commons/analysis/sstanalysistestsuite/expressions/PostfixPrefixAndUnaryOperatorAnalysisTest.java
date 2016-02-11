package eclipse.commons.analysis.sstanalysistestsuite.expressions;

import org.junit.Test;

import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class PostfixPrefixAndUnaryOperatorAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void postfixPlusPlus() {
		UnaryExpression unaryExpression = newUnaryExpression(newReferenceExpression("i"), UnaryOperator.PostIncrement);

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", newConstantValue("0")),
				newAssignment("i", unaryExpression));
	}

	@Test
	public void postfixMinusMinus() {
		UnaryExpression unaryExpression = newUnaryExpression(newReferenceExpression("i"), UnaryOperator.PostDecrement);

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", newConstantValue("0")),
				newAssignment("i", unaryExpression));
	}

	@Test
	public void prefixPlusPlus() {
		UnaryExpression unaryExpression = newUnaryExpression(newReferenceExpression("i"), UnaryOperator.PreIncrement);

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", newConstantValue("0")),
				newAssignment("i", unaryExpression));
	}

	@Test
	public void prefixMinusMinus() {
		UnaryExpression unaryExpression = newUnaryExpression(newReferenceExpression("i"), UnaryOperator.PreDecrement);

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", newConstantValue("0")),
				newAssignment("i", unaryExpression));
	}

	@Test
	public void unaryMinus() {
		UnaryExpression unaryExpression = newUnaryExpression(newReferenceExpression("i"), UnaryOperator.Minus);

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", newConstantValue("1")),
				newVariableDeclaration("j", SSTAnalysisFixture.INT), newAssignment("j", unaryExpression));
	}

	@Test
	public void unaryNot() {
		UnaryExpression unaryExpression = newUnaryExpression(newConstantValue("true"), UnaryOperator.Not);

		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN), newAssignment("b", unaryExpression));
	}
}
