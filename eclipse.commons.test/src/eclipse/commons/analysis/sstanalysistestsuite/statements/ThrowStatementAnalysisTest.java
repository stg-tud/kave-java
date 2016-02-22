package eclipse.commons.analysis.sstanalysistestsuite.statements;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class ThrowStatementAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void basicThrowStatement() {
		ThrowStatement throwStmt = new ThrowStatement();
		throwStmt.setReference(newVariableReference("$0"));

		assertMethod(newVariableDeclaration("$0", SSTAnalysisFixture.EXCEPTION),
				newAssignment("$0", newInvokeConstructor(SSTAnalysisFixture.EXCEPTION_CTOR)), throwStmt);
	}

	@Test
	public void throwWithArgument() throws Exception {
		ThrowStatement throwStmt = new ThrowStatement();
		throwStmt.setReference(newVariableReference("$0"));

		assertMethod(newVariableDeclaration("$0", SSTAnalysisFixture.EXCEPTION),
				newAssignment("$0", newInvokeConstructor(
						SSTAnalysisFixture.constructCtor(SSTAnalysisFixture.EXCEPTION, SSTAnalysisFixture.STRING),
						newConstantValue("arg1"))),
				throwStmt);
	}

	@Test
	public void throwVariable() {
		ThrowStatement throwStmt = new ThrowStatement();
		throwStmt.setReference(newVariableReference("e"));

		assertMethod(newVariableDeclaration("e", SSTAnalysisFixture.EXCEPTION),
				newAssignment("e", newInvokeConstructor(SSTAnalysisFixture.EXCEPTION_CTOR)), throwStmt);
	}
}
