package eclipse.commons.analysis.sstanalysistestsuite.expressions;

import org.junit.Test;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.expressions.assignable.TypeCheckExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class TypeCheckAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void basicInstanceOf() {
		TypeCheckExpression typeCheckExpression = new TypeCheckExpression();
		typeCheckExpression.setReference(newVariableReference("o"));
		typeCheckExpression.setType(SSTAnalysisFixture.OBJECT);

		IfElseBlock ifElseBlock = new IfElseBlock();
		ifElseBlock.setCondition(newReferenceExpression("$0"));

		assertMethod(newVariableDeclaration("o", SSTAnalysisFixture.OBJECT), newAssignment("o", new NullExpression()),
				newVariableDeclaration("$0", SSTAnalysisFixture.BOOLEAN), newAssignment("$0", typeCheckExpression),
				ifElseBlock);
	}

	@Test
	public void qualifiedInstanceOf() {
		TypeCheckExpression typeCheckExpression = new TypeCheckExpression();
		typeCheckExpression.setReference(newVariableReference("o"));
		typeCheckExpression.setType(SSTAnalysisFixture.OBJECT);

		IfElseBlock ifElseBlock = new IfElseBlock();
		ifElseBlock.setCondition(newReferenceExpression("$0"));

		assertMethod(newVariableDeclaration("o", SSTAnalysisFixture.OBJECT), newAssignment("o", new NullExpression()),
				newVariableDeclaration("$0", SSTAnalysisFixture.BOOLEAN), newAssignment("$0", typeCheckExpression),
				ifElseBlock);
	}

	@Test
	public void invocationInstanceOf() {
		TypeCheckExpression typeCheckExpression = new TypeCheckExpression();
		typeCheckExpression.setReference(newVariableReference("$0"));
		typeCheckExpression.setType(SSTAnalysisFixture.OBJECT);

		IfElseBlock ifElseBlock = new IfElseBlock();
		ifElseBlock.setCondition(newReferenceExpression("$1"));

		MethodName methodName = CsMethodName.newMethodName(
				"[java.lang.Object, rt.jar, 1.8] [typecheckanalysistest.InvocationInstanceOf, ?].getObject()");

		assertMethod(newVariableDeclaration("o", SSTAnalysisFixture.OBJECT), newAssignment("o", new NullExpression()),
				newVariableDeclaration("$0", CsTypeName.newTypeName("java.lang.Object, rt.jar, 1.8")),
				newAssignment("$0", newInvokeExpression("this", methodName)),
				newVariableDeclaration("$1", SSTAnalysisFixture.BOOLEAN), newAssignment("$1", typeCheckExpression),
				ifElseBlock);
	}
}
