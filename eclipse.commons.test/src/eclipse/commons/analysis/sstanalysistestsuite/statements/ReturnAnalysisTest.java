package eclipse.commons.analysis.sstanalysistestsuite.statements;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;

public class ReturnAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void voidReturn() {
		ReturnStatement returnStmt = new ReturnStatement();
		returnStmt.setIsVoid(true);
		assertMethod(returnStmt);
	}

	@Test
	public void valueReturn() {
		String qualifiedName = packageName.toLowerCase() + "." + capitalizeString(name.getMethodName());
		String identifier = "[%int, rt.jar, 1.8] [" + qualifiedName + ", ?].method()";
		MethodName name = CsMethodName.newMethodName(identifier);

		MethodDeclaration expected = new MethodDeclaration();
		expected.setName(name);
		ReturnStatement returnStmt = new ReturnStatement();
		returnStmt.setExpression(newConstantValue("1"));
		expected.getBody().add(returnStmt);

		IMethodDeclaration actual = getFirstMethod();

		assertEquals(expected, actual);
	}

}
