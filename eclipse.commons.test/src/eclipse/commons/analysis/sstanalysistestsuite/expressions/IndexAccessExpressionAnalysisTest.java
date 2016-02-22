package eclipse.commons.analysis.sstanalysistestsuite.expressions;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.references.IndexAccessReference;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class IndexAccessExpressionAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void indexAccess() {
		IndexAccessExpression indexAccess = new IndexAccessExpression();
		indexAccess.setReference(newVariableReference("i"));
		indexAccess.getIndices().add(newConstantValue("0"));

		assertMethod(newVariableDeclaration("j", SSTAnalysisFixture.INT), newAssignment("j", indexAccess));

	}

	@Test
	public void variableAsIndex() {
		IndexAccessExpression indexAccess = new IndexAccessExpression();
		indexAccess.setReference(newVariableReference("i"));
		indexAccess.getIndices().add(newReferenceExpression("k"));

		assertMethod(newVariableDeclaration("k", SSTAnalysisFixture.INT), newAssignment("k", newConstantValue("1")),
				newVariableDeclaration("j", SSTAnalysisFixture.INT), newAssignment("j", indexAccess));
	}

	@Test
	public void indexAccessOnMethodResult() {
		IndexAccessExpression indexAccess = new IndexAccessExpression();
		indexAccess.setReference(newVariableReference("$0"));
		indexAccess.getIndices().add(newConstantValue("1"));

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT),
				newVariableDeclaration("$0", SSTAnalysisFixture.INT_ARRAY),
				newAssignment("$0",
						newInvokeExpression("this",
								CsMethodName.newMethodName(
										"[%int[], rt.jar, 1.8] [indexaccessexpressionanalysistest.IndexAccessOnMethodResult, ?].getArray()"))),
				newAssignment("i", indexAccess));
	}

	@Test
	public void assigningValueToArrayElement() {
		IndexAccessExpression indexAccessExpression = new IndexAccessExpression();
		indexAccessExpression.setReference(newVariableReference("i"));
		indexAccessExpression.getIndices().add(newConstantValue("1"));

		IndexAccessReference indexAccessReference = new IndexAccessReference();
		indexAccessReference.setExpression(indexAccessExpression);

		assertMethod(newAssignment(indexAccessReference, newConstantValue("4")));
	}

	@Test
	public void twoIndices() {
		assertMethod(newAssignment("", newConstantValue("4")));
	}

	@Test
	public void arrayDeclaration() {
	}

	@Test
	public void arrayDeclarationWithParameters() {
	}

}
