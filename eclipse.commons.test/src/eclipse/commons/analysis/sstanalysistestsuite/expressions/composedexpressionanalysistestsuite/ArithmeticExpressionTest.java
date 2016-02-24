package eclipse.commons.analysis.sstanalysistestsuite.expressions.composedexpressionanalysistestsuite;

import org.junit.Test;

import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.impl.expressions.assignable.BinaryExpression;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class ArithmeticExpressionTest extends BaseSSTAnalysisTest {

	@Test
	public void addingTwoInts() {
		BinaryExpression binaryExpression = newBinaryExpression(newConstantValue("2"), newConstantValue("3"),
				BinaryOperator.Plus);

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", binaryExpression));
	}

	@Test
	public void addingThreeInts() {
		BinaryExpression refBinaryExpression = newBinaryExpression(newConstantValue("2"), newConstantValue("3"),
				BinaryOperator.Plus);

		BinaryExpression binaryExpression = newBinaryExpression(newReferenceExpression(newVariableReference("$0")),
				newConstantValue("4"), BinaryOperator.Plus);

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT),
				newVariableDeclaration("$0", SSTAnalysisFixture.INT), newAssignment("$0", refBinaryExpression),
				newAssignment("i", binaryExpression));
	}

	@Test
	public void addingVariables() {
		BinaryExpression binaryExpression = newBinaryExpression(newReferenceExpression("i"), newConstantValue("3"),
				BinaryOperator.Plus);

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", newConstantValue("2")),
				newVariableDeclaration("j", SSTAnalysisFixture.INT), newAssignment("j", binaryExpression));
	}

	@Test
	public void subtractingTwoInts() {
		BinaryExpression binaryExpression = newBinaryExpression(newConstantValue("2"), newConstantValue("3"),
				BinaryOperator.Minus);

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", binaryExpression));
	}

	@Test
	public void dividingTwoInts() {
		BinaryExpression binaryExpression = newBinaryExpression(newConstantValue("2"), newConstantValue("3"),
				BinaryOperator.Divide);

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", binaryExpression));
	}

	@Test
	public void multiplyingTwoInts() {
		BinaryExpression binaryExpression = newBinaryExpression(newConstantValue("2"), newConstantValue("3"),
				BinaryOperator.Multiply);

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", binaryExpression));
	}

	@Test
	public void moduloTwoInts() {
		BinaryExpression binaryExpression = newBinaryExpression(newConstantValue("2"), newConstantValue("3"),
				BinaryOperator.Modulo);

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", binaryExpression));
	}

	@Test
	public void nestedArithmeticExpressions() {
		BinaryExpression binaryExpression0 = newBinaryExpression(newConstantValue("2"), newConstantValue("3"),
				BinaryOperator.Multiply);

		BinaryExpression binaryExpression1 = newBinaryExpression(newConstantValue("4"), newConstantValue("5"),
				BinaryOperator.Divide);

		BinaryExpression binaryExpression2 = newBinaryExpression(newReferenceExpression("$0"),
				newReferenceExpression("$1"), BinaryOperator.Minus);

		BinaryExpression binaryExpression3 = newBinaryExpression(newConstantValue("1"), newReferenceExpression("$2"),
				BinaryOperator.Plus);

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT),
				newVariableDeclaration("$0", SSTAnalysisFixture.INT), newAssignment("$0", binaryExpression0),
				newVariableDeclaration("$1", SSTAnalysisFixture.INT), newAssignment("$1", binaryExpression1),
				newVariableDeclaration("$2", SSTAnalysisFixture.INT), newAssignment("$2", binaryExpression2),
				newAssignment("i", binaryExpression3));
	}
}
