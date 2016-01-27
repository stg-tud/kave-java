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
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class ForLoopAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void basicForLoop() {
		ForLoop expected = new ForLoop();

		IStatement actual = getFirstStatement();

		assertEquals(expected, actual);
	}

	@Test
	public void init_VariableDeclaration() {
		ForLoop expected = new ForLoop();
		expected.getInit().add(newVariableDeclaration("i", SSTAnalysisFixture.INT));
		expected.getInit().add(newAssignment("i", newConstantValue("0")));

		IStatement actual = getFirstStatement();

		assertEquals(expected, actual);
	}

	@Test
	public void init_MultipleStatements() {
		MethodDeclaration expected = newDefaultMethodDeclaration();
		expected.getBody().add(newVariableDeclaration("i", SSTAnalysisFixture.INT));
		expected.getBody().add(newVariableDeclaration("j", SSTAnalysisFixture.INT));

		ForLoop loop = new ForLoop();
		loop.getInit().add(newAssignment("i", newConstantValue("0")));
		loop.getInit().add(newAssignment("j", newConstantValue("1")));
		expected.getBody().add(loop);
		IMethodDeclaration actual = getFirstMethod();

		assertEquals(expected, actual);
		assertFalse(true);
	}

	// TODO: AddMethod
	@Test
	public void condition() {
		ForLoop expected = new ForLoop();
		expected.setCondition(newConstantValue("true"));

		IStatement actual = getFirstStatement();

		assertEquals(expected, actual);
	}

	@Test
	public void step_SingleStatement() {
		MethodDeclaration expected = newDefaultMethodDeclaration();
		expected.getBody().add(newVariableDeclaration("i", SSTAnalysisFixture.INT));
		expected.getBody().add(newAssignment("i", newConstantValue("0")));

		ForLoop loop = new ForLoop();
		ComposedExpression composedExpr = new ComposedExpression();
		composedExpr.getReferences().add(newVariableReference("i"));
		loop.getStep().add(newAssignment("i", composedExpr));
		expected.getBody().add(loop);

		IMethodDeclaration actual = getFirstMethod();

		assertEquals(loop, actual);
	}

	@Test
	public void step_MultipleStatements() {
		MethodDeclaration expected = newDefaultMethodDeclaration();
		expected.getBody().add(newVariableDeclaration("i", SSTAnalysisFixture.INT));
		expected.getBody().add(newAssignment("i", newConstantValue("0")));

		ForLoop loop = new ForLoop();
		ComposedExpression composedExpr = new ComposedExpression();
		composedExpr.getReferences().add(newVariableReference("i"));
		loop.getStep().add(newAssignment("i", composedExpr));
		loop.getStep().add(newAssignment("i", composedExpr));
		expected.getBody().add(loop);

		IMethodDeclaration actual = getFirstMethod();

		assertEquals(expected, actual);
	}

	// TODO: Add Method
	@Test
	public void bodyWithStatement() {
		MethodDeclaration expected = newDefaultMethodDeclaration();

		ForLoop loop = new ForLoop();
		loop.getBody().add(newVariableDeclaration("i", SSTAnalysisFixture.INT));
		expected.getBody().add(loop);

		IMethodDeclaration actual = getFirstMethod();

		assertEquals(expected, actual);

	}
}
