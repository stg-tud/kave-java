package eclipse.commons.analysis.sstanalysistestsuite.expressions;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsLambdaName;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class LambdaExpressionAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void basicLambda() {
		LambdaExpression lambdaExpression = new LambdaExpression();
		lambdaExpression.setName(CsLambdaName.newLambdaName("[%void, rt.jar, 1.8] ()"));
		lambdaExpression.getBody().add(newInvokeStatement("this", SSTAnalysisFixture.HASHCODE));

		assertMethod(newVariableDeclaration("run", SSTAnalysisFixture.RUNNABLE),
				newAssignment("run", lambdaExpression));
	}

	@Test
	public void lambdaWithDeclaration() {
		LambdaExpression lambdaExpression = new LambdaExpression();
		lambdaExpression.setName(
				CsLambdaName.newLambdaName("[java.lang.Integer, rt.jar, 1.8] ([java.lang.Integer, rt.jar, 1.8] i)"));
		lambdaExpression.getBody().add(
				newReturnStatement(newReferenceExpression(newMethodRef(SSTAnalysisFixture.HASHCODE, "this")), false));

		assertMethod(newVariableDeclaration("func", SSTAnalysisFixture.FUNCTION),
				newAssignment("func", lambdaExpression));
	}

	@Test
	public void lambdaWithReturnAndBlock() {
		LambdaExpression lambdaExpression = new LambdaExpression();
		lambdaExpression.setName(
				CsLambdaName.newLambdaName("[java.lang.Integer, rt.jar, 1.8] ([java.lang.Integer, rt.jar, 1.8] i)"));
		lambdaExpression.getBody().add(newInvokeStatement("this", SSTAnalysisFixture.HASHCODE));
		lambdaExpression.getBody().add(newReturnStatement(newReferenceExpression("i"), false));

		assertMethod(newVariableDeclaration("func", SSTAnalysisFixture.FUNCTION),
				newAssignment("func", lambdaExpression));
	}

	@Test
	public void lambdaWithImplicitReturn() {
		LambdaExpression lambdaExpression = new LambdaExpression();
		lambdaExpression.setName(
				CsLambdaName.newLambdaName("[java.lang.Integer, rt.jar, 1.8] ([java.lang.Integer, rt.jar, 1.8] i)"));
		lambdaExpression.getBody().add(newReturnStatement(newReferenceExpression("i"), false));

		assertMethod(newVariableDeclaration("func", SSTAnalysisFixture.FUNCTION),
				newAssignment("func", lambdaExpression));
	}

	@Test
	public void lambdaWithTwoArguments() {
		LambdaExpression lambdaExpression = new LambdaExpression();
		lambdaExpression.setName(CsLambdaName.newLambdaName(
				"[java.lang.Integer, rt.jar, 1.8] ([java.lang.Integer, rt.jar, 1.8] i, [java.lang.Boolean, rt.jar, 1.8] b)"));
		lambdaExpression.getBody().add(newReturnStatement(newReferenceExpression("i"), false));

		assertMethod(newVariableDeclaration("func", SSTAnalysisFixture.BIFUNCTION),
				newAssignment("func", lambdaExpression));
	}
}
