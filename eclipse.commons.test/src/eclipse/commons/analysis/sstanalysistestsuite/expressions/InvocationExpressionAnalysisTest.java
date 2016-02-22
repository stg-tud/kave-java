package eclipse.commons.analysis.sstanalysistestsuite.expressions;

import org.junit.Test;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class InvocationExpressionAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void invocationOnVariable() {
		assertMethod(newVariableDeclaration("o", SSTAnalysisFixture.OBJECT),
				newAssignment("o", newInvokeConstructor(SSTAnalysisFixture.OBJECT_CTOR)),
				newInvokeStatement("o", SSTAnalysisFixture.HASHCODE));
	}

	@Test
	public void invocationOnVariableImplicitThis() {
		assertMethod(newInvokeStatement("this", SSTAnalysisFixture.HASHCODE));
	}

	@Test
	public void invocationOnVariableExplicitThis() {
		assertMethod(newInvokeStatement("this", SSTAnalysisFixture.HASHCODE));
	}

	@Test
	public void invocationOnInvocation() {
		assertMethod(newVariableDeclaration("$0", SSTAnalysisFixture.STRING),
				newAssignment("$0", newInvokeExpression("this", SSTAnalysisFixture.TO_STRING)),
				newInvokeStatement("$0", SSTAnalysisFixture.LENGTH));
	}

	@Test
	public void invocationOnInvocationChain() {
		MethodName stringImplementationName = CsMethodName
				.newMethodName("[java.lang.String, rt.jar, 1.8] [java.lang.String, rt.jar, 1.8].toString()");

		assertMethod(newVariableDeclaration("$0", SSTAnalysisFixture.STRING),
				newAssignment("$0", newInvokeExpression("this", SSTAnalysisFixture.TO_STRING)),
				newVariableDeclaration("$1", SSTAnalysisFixture.STRING),
				newAssignment("$1", newInvokeExpression("$0", stringImplementationName)),
				newInvokeStatement("$1", SSTAnalysisFixture.LENGTH));
	}

	@Test
	public void invocationOnFieldExplicitThis() {
		assertMethod(newVariableDeclaration("$0", SSTAnalysisFixture.STRING),
				newAssignment("$0", newReferenceExpression(newFieldReference("s", SSTAnalysisFixture.STRING, "this"))),
				newInvokeStatement("$0", SSTAnalysisFixture.LENGTH));
	}

	@Test
	public void invocationOnFieldImplicitThis() {
		assertMethod(newVariableDeclaration("$0", SSTAnalysisFixture.STRING),
				newAssignment("$0", newReferenceExpression(newFieldReference("s", SSTAnalysisFixture.STRING, "this"))),
				newInvokeStatement("$0", SSTAnalysisFixture.LENGTH));
	}

	@Test
	public void nestedInOtherCall() {
		assertMethod(newVariableDeclaration("$0", SSTAnalysisFixture.INT),
				newAssignment("$0", newInvokeExpression("this", SSTAnalysisFixture.HASHCODE)),
				newInvokeStatement("this", SSTAnalysisFixture.OBJECT_EQUALS, newReferenceExpression("$0")));
	}

	@Test
	public void staticInvocationImplicit() {
		assertMethod(newInvokeStaticStatement(CsMethodName.newMethodName(
				"static [%void, rt.jar, 1.8] [invocationexpressionanalysistest.StaticInvocationImplicit, ?].f()")));
	}

	@Test
	public void staticInvocationExplicit() {
		assertMethod(newInvokeStaticStatement(CsMethodName.newMethodName(
				"static [%void, rt.jar, 1.8] [invocationexpressionanalysistest.StaticInvocationExplicit, ?].f()")));
	}
}
