package eclipse.commons.analysis.sstanalysistestsuite.expressions;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class ObjectCreationExpressionAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void basicObjectCreation() {
		assertMethod(expressionToStatement(newInvokeConstructor(SSTAnalysisFixture.OBJECT_CTOR)));
	}

	@Test
	public void objectCreationWithParameter() {
		MethodName arrayCtor = CsMethodName.newMethodName(
				"[java.util.ArrayList`1[[T -> T]], ?] [java.util.ArrayList`1[[T -> T]], ?]..ctor([%int, rt.jar, 1.8] ?)");
		assertMethod(expressionToStatement(newInvokeConstructor(arrayCtor, newConstantValue("0"))));
	}

	@Test
	public void nestedObjectCreation() {
		MethodName stringCtor = CsMethodName.newMethodName(
				"[java.lang.String, rt.jar, 1.8] [java.lang.String, rt.jar, 1.8]..ctor([java.lang.String, rt.jar, 1.8] ?)");

		assertMethod(newVariableDeclaration("$0", SSTAnalysisFixture.STRING),
				newAssignment("$0", newInvokeConstructor(SSTAnalysisFixture.STRING_CTOR)), expressionToStatement(
						newInvokeConstructor(stringCtor, newReferenceExpression(newVariableReference("$0")))));
	}

	// TODO: can not create parameterized names
	@Ignore
	@Test
	public void parameterizedObjectCreation() {
	}

	@Ignore
	@Test
	public void parameterizedObjectCreationWithQualifiedName() {
		assertMethod();
	}
}
