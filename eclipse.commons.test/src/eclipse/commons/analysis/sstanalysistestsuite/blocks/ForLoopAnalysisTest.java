/**
 * Copyright 2015 Waldemar Graf
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eclipse.commons.analysis.sstanalysistestsuite.blocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class ForLoopAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void basicForLoop() {
		assertMethod(new ForLoop());
	}

	@Test
	public void init_VariableDeclaration() {
		ForLoop loop = new ForLoop();
		loop.getInit().add(newVariableDeclaration("i", SSTAnalysisFixture.INT));
		loop.getInit().add(newAssignment("i", newConstantValue("0")));

		assertMethod(loop);
	}

	@Test
	public void init_MultipleStatements() {
		ForLoop loop = new ForLoop();
		loop.getInit().add(newAssignment("i", newConstantValue("0")));
		loop.getInit().add(newAssignment("j", newConstantValue("1")));

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT),
				newVariableDeclaration("j", SSTAnalysisFixture.INT), loop);
	}

	@Test
	public void condition() {
		ForLoop loop = new ForLoop();
		loop.setCondition(newConstantValue("true"));

		assertMethod(loop);

		IStatement actual = getFirstStatement();

		assertEquals(loop, actual);
	}

	@Test
	public void step_SingleStatement() {
		ForLoop loop = new ForLoop();
		loop.getStep()
				.add(newAssignment("i", newUnaryExpression(newReferenceExpression("i"), UnaryOperator.PostIncrement)));

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", newConstantValue("0")),
				loop);
	}

	@Test
	public void step_MultipleStatements() {
		ForLoop loop = new ForLoop();
		UnaryExpression unaryExpr = newUnaryExpression(newReferenceExpression("i"), UnaryOperator.PostIncrement);
		loop.getStep().add(newAssignment("i", unaryExpr));
		loop.getStep().add(newAssignment("i", unaryExpr));

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", newConstantValue("0")),
				loop);
	}

	@Test
	public void bodyWithStatement() {
		ForLoop loop = new ForLoop();
		loop.getBody().add(newVariableDeclaration("i", SSTAnalysisFixture.INT));

		assertMethod(loop);
	}
}
