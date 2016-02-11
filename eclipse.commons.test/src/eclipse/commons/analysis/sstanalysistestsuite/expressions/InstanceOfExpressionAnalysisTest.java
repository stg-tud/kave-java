package eclipse.commons.analysis.sstanalysistestsuite.expressions;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.expressions.assignable.TypeCheckExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class InstanceOfExpressionAnalysisTest extends BaseSSTAnalysisTest {

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
		assertMethod();
	}
}
