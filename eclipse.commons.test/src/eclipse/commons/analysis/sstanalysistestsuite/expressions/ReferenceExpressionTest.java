package eclipse.commons.analysis.sstanalysistestsuite.expressions;

import org.junit.Test;

import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class ReferenceExpressionTest extends BaseSSTAnalysisTest {

	@Test
	public void simpleDeclaration() {
		assertMethod(newVariableDeclaration("o", SSTAnalysisFixture.OBJECT), newAssignment("o", new NullExpression()));
	}

	@Test
	public void referenceOnReference() {
		TypeName type = CsTypeName.newTypeName("referenceexpressiontest.ReferenceOnReference, ?");

		VariableDeclaration varDecl = newVariableDeclaration("c",
				CsTypeName.newTypeName("referenceexpressiontest.ReferenceOnReference, ?"));

		VariableDeclaration tempVarDecl1 = newVariableDeclaration("$0",
				type);

		ISimpleExpression tempRef1 = newReferenceExpression(newFieldReference("a", type, "this"));

		Assignment assignTemp1 = newAssignment("$0", tempRef1);

		VariableDeclaration tempVarDecl2 = newVariableDeclaration("$1", type);

		ISimpleExpression tempRef2 = newReferenceExpression(newFieldReference("b", type, "$0"));

		Assignment assignTemp2 = newAssignment("$1", tempRef2);

		ISimpleExpression fieldRef = newReferenceExpression(newFieldReference("a", type, "$1"));
		Assignment assignVar = newAssignment("c", fieldRef);

		assertMethod(varDecl, tempVarDecl1, assignTemp1, tempVarDecl2, assignTemp2, assignVar);
	}

	@Test
	public void referenceOnInvocation() {
		TypeName type = CsTypeName.newTypeName("referenceexpressiontest.ReferenceOnInvocation, ?");

		VariableDeclaration varDecl = newVariableDeclaration("c",
				CsTypeName.newTypeName("referenceexpressiontest.ReferenceOnInvocation, ?"));

		VariableDeclaration tempVarDecl = newVariableDeclaration("$0",
				CsTypeName.newTypeName("referenceexpressiontest.ReferenceOnInvocation, ?"));

		MethodName name = CsMethodName.newMethodName(
				"[referenceexpressiontest.ReferenceOnInvocation, ?] [referenceexpressiontest.ReferenceOnInvocation, ?].getC()");
		InvocationExpression tempInvocation = newInvokeExpression("a", name);

		Assignment assignTemp = newAssignment("$0", tempInvocation);

		ISimpleExpression fieldRef = newReferenceExpression(newFieldReference("a", type, "$0"));
		Assignment assignVar = newAssignment("c", fieldRef);

		assertMethod(varDecl, tempVarDecl, assignTemp, assignVar);
	}

	@Test
	public void nestedVariableReferencesExpressions() {
		VariableDeclaration varDecl = newVariableDeclaration("o", SSTAnalysisFixture.OBJECT);
		Assignment assignNull = newAssignment("o", new NullExpression());
		ExpressionStatement invokeStmt = newInvokeStatement("this", SSTAnalysisFixture.OBJECT_EQUALS,
				newReferenceExpression("o"));
		assertMethod(varDecl, assignNull, invokeStmt);
	}

}
