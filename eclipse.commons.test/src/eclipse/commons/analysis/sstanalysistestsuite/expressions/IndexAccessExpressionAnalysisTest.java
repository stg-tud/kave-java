package eclipse.commons.analysis.sstanalysistestsuite.expressions;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
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
		IndexAccessExpression arrayAccess = new IndexAccessExpression();
		arrayAccess.setReference(newVariableReference("i"));
		arrayAccess.getIndices().add(newConstantValue("1"));

		IndexAccessExpression valueAccess = new IndexAccessExpression();
		valueAccess.setReference(newVariableReference("$0"));
		valueAccess.getIndices().add(newConstantValue("2"));

		assertMethod(newVariableDeclaration("j", SSTAnalysisFixture.INT),
				newVariableDeclaration("$0", SSTAnalysisFixture.INT_ARRAY), newAssignment("$0", arrayAccess),
				newAssignment("j", valueAccess));
	}

	@Test
	public void assigningValueToArrayElementTwoDimensional() {
		IndexAccessExpression arrayAccess = new IndexAccessExpression();
		arrayAccess.setReference(newVariableReference("i"));
		arrayAccess.getIndices().add(newConstantValue("2"));

		IndexAccessExpression valueAccess = new IndexAccessExpression();
		valueAccess.setReference(newVariableReference("$0"));
		valueAccess.getIndices().add(newConstantValue("1"));

		IndexAccessReference indexAccessReference = new IndexAccessReference();
		indexAccessReference.setExpression(valueAccess);

		assertMethod(newVariableDeclaration("$0", SSTAnalysisFixture.INT_ARRAY), newAssignment("$0", arrayAccess),
				newAssignment(indexAccessReference, newConstantValue("5")));
	}

	@Test
	public void arrayDeclaration() {
		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT_ARRAY));
	}

	@Test
	public void arrayDeclarationWithParameters() {
		IndexAccessExpression indexAccessExpression0 = newIndexAccessExpression("i", newConstantValue("0"));
		IndexAccessExpression indexAccessExpression1 = newIndexAccessExpression("i", newConstantValue("1"));
		IndexAccessExpression indexAccessExpression2 = newIndexAccessExpression("i", newConstantValue("2"));

		IndexAccessReference indexAccessReference0 = new IndexAccessReference();
		indexAccessReference0.setExpression(indexAccessExpression0);

		IndexAccessReference indexAccessReference1 = new IndexAccessReference();
		indexAccessReference1.setExpression(indexAccessExpression1);

		IndexAccessReference indexAccessReference2 = new IndexAccessReference();
		indexAccessReference2.setExpression(indexAccessExpression2);

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT_ARRAY),
				newAssignment(indexAccessReference0, newConstantValue("1")),
				newAssignment(indexAccessReference1, newConstantValue("2")),
				newAssignment(indexAccessReference2, newConstantValue("3")));
	}

	@Test
	public void arrayDeclarationWithNew() {
		IndexAccessExpression indexAccessExpression0 = newIndexAccessExpression("i", newConstantValue("0"));
		IndexAccessExpression indexAccessExpression1 = newIndexAccessExpression("i", newConstantValue("1"));
		IndexAccessExpression indexAccessExpression2 = newIndexAccessExpression("i", newConstantValue("2"));

		IndexAccessReference indexAccessReference0 = new IndexAccessReference();
		indexAccessReference0.setExpression(indexAccessExpression0);

		IndexAccessReference indexAccessReference1 = new IndexAccessReference();
		indexAccessReference1.setExpression(indexAccessExpression1);

		IndexAccessReference indexAccessReference2 = new IndexAccessReference();
		indexAccessReference2.setExpression(indexAccessExpression2);

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT_ARRAY),
				newAssignment(indexAccessReference0, newConstantValue("1")),
				newAssignment(indexAccessReference1, newConstantValue("2")),
				newAssignment(indexAccessReference2, newConstantValue("3")));
	}

}
