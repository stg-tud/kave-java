package eclipse.commons.analysis.sstanalysistestsuite.expressions.composedexpressionanalysistestsuite;

import org.junit.Test;

import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class BooleanExpressionTest extends BaseSSTAnalysisTest {

	@Test
	public void booleanAndOnThreeValues() {
		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN),
				newVariableDeclaration("$0", SSTAnalysisFixture.BOOLEAN),
				newAssignment("$0",
						newBinaryExpression(newConstantValue("true"), newConstantValue("false"), BinaryOperator.And)),
				newAssignment("b", newBinaryExpression(newReferenceExpression("$0"), newConstantValue("true"),
						BinaryOperator.And)));
	}

	@Test
	public void booleanAndOnTwoValues() {
		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN), newAssignment("b",
				newBinaryExpression(newConstantValue("true"), newConstantValue("false"), BinaryOperator.And)));
	}

	@Test
	public void booleanAndWithVariables() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.BOOLEAN),
				newAssignment("i", newConstantValue("false")), newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN),
				newAssignment("b", newBinaryExpression(newConstantValue("true"), newReferenceExpression("i"),
						BinaryOperator.And)));
	}

	@Test
	public void booleanEqualityOnTwoValues() {
		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN), newAssignment("b",
				newBinaryExpression(newConstantValue("1"), newConstantValue("2"), BinaryOperator.Equal)));
	}

	@Test
	public void booleanInequalityOnTwoValues() {
		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN), newAssignment("b",
				newBinaryExpression(newConstantValue("1"), newConstantValue("2"), BinaryOperator.NotEqual)));
	}

	@Test
	public void booleanOrOnTwoValues() {
		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN), newAssignment("b",
				newBinaryExpression(newConstantValue("true"), newConstantValue("false"), BinaryOperator.Or)));
	}

	@Test
	public void relationalGreater() {
		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN), newAssignment("b",
				newBinaryExpression(newConstantValue("2"), newConstantValue("1"), BinaryOperator.GreaterThan)));
	}

	@Test
	public void relationalGreaterOrEqual() {
		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN),
				newAssignment("b", newBinaryExpression(newConstantValue("2"), newConstantValue("1"),
						BinaryOperator.GreaterThanOrEqual)));
	}

	@Test
	public void relationalLess() {
		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN), newAssignment("b",
				newBinaryExpression(newConstantValue("2"), newConstantValue("1"), BinaryOperator.LessThan)));
	}

	@Test
	public void relationalLessOrEqual() {
		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN), newAssignment("b",
				newBinaryExpression(newConstantValue("2"), newConstantValue("1"), BinaryOperator.LessThanOrEqual)));
	}

	@Test
	public void negationExpression() {
		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN),
				newAssignment("b", newUnaryExpression(newConstantValue("false"), UnaryOperator.Not)));
	}

}
