package eclipse.commons.analysis.sstanalysistestsuite.expressions.composedexpressionanalysistestsuite;

import org.junit.Test;

import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class BitwiseExpressionTest extends BaseSSTAnalysisTest {

	@Test
	public void bitwiseAndOnThreeInts() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT),
				newVariableDeclaration("$0", SSTAnalysisFixture.INT),
				newAssignment("$0",
						newBinaryExpression(newConstantValue("1"), newConstantValue("2"), BinaryOperator.BitwiseAnd)),
				newAssignment("i", newBinaryExpression(newReferenceExpression("$0"), newConstantValue("3"),
						BinaryOperator.BitwiseAnd)));
	}

	@Test
	public void bitwiseOrOnThreeInts() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT),
				newVariableDeclaration("$0", SSTAnalysisFixture.INT),
				newAssignment("$0",
						newBinaryExpression(newConstantValue("1"), newConstantValue("2"), BinaryOperator.BitwiseOr)),
				newAssignment("i", newBinaryExpression(newReferenceExpression("$0"), newConstantValue("3"),
						BinaryOperator.BitwiseOr)));
	}

	@Test
	public void bitwiseAndOnTwoInts() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i",
				newBinaryExpression(newConstantValue("101"), newConstantValue("1"), BinaryOperator.BitwiseAnd)));
	}

	@Test
	public void bitwiseAndOnTwoBools() {
		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN), newAssignment("b",
				newBinaryExpression(newConstantValue("true"), newConstantValue("false"), BinaryOperator.BitwiseAnd)));
	}

	@Test
	public void bitwiseOrOnTwoInts() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i",
				newBinaryExpression(newConstantValue("101"), newConstantValue("1"), BinaryOperator.BitwiseOr)));
	}

	@Test
	public void bitwiseOrOnTwoBools() {
		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN), newAssignment("b",
				newBinaryExpression(newConstantValue("true"), newConstantValue("false"), BinaryOperator.BitwiseOr)));
	}

	@Test
	public void bitwiseAndWithVariables() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.BOOLEAN),
				newAssignment("i", newConstantValue("false")), newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN),
				newAssignment("b", newBinaryExpression(newConstantValue("true"), newReferenceExpression("i"),
						BinaryOperator.BitwiseAnd)));
	}

	@Test
	public void bitwiseXorOnTwoInts() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i",
				newBinaryExpression(newConstantValue("101"), newConstantValue("1"), BinaryOperator.BitwiseXor)));
	}

	@Test
	public void bitwiseXorOnTwoBools() {
		assertMethod(newVariableDeclaration("b", SSTAnalysisFixture.BOOLEAN), newAssignment("b",
				newBinaryExpression(newConstantValue("true"), newConstantValue("false"), BinaryOperator.BitwiseXor)));
	}

	@Test
	public void bitwiseLeftShift() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i",
				newBinaryExpression(newConstantValue("100"), newConstantValue("1"), BinaryOperator.ShiftLeft)));
	}

	@Test
	public void bitwiseRightShift() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i",
				newBinaryExpression(newConstantValue("100"), newConstantValue("1"), BinaryOperator.ShiftRight)));
	}

	@Test
	public void bitwiseComplement() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT),
				newAssignment("i", newUnaryExpression(newConstantValue("100"), UnaryOperator.Complement)));
	}
}
