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

	private final String projectName = "testproject";
	private final String packageName = "forloopanalysistest;";

	@Test
	public void basicLoop() {
		updateContext(projectName, packageName + "BasicForLoop.java");

		ForLoop expected = new ForLoop();

		IStatement actual = getFirstStatement();

		assertEquals(expected, actual);
	}

	@Test
	public void init_VariableDeclaration() {
		updateContext(projectName, packageName
				+ "Init_VariableDeclaration.java");

		ForLoop expected = new ForLoop();
		expected.getInit().add(
				newVariableDeclaration("i", SSTAnalysisFixture.INT));
		expected.getInit().add(newAssignment("i", newConstantValue("0")));

		IStatement actual = getFirstStatement();

		assertEquals(expected, actual);
	}

	@Test
	public void init_MultipleStatements() {
		updateContext(projectName, packageName + "Init_MultipleStatements.java");

		MethodDeclaration method = new MethodDeclaration();
		method.getBody().add(
				newVariableDeclaration("i", SSTAnalysisFixture.INT));
		method.getBody().add(
				newVariableDeclaration("j", SSTAnalysisFixture.INT));
		method.setName(defaultMethodName(packageName
				+ ".Init_MultipleStatements"));

		ForLoop expected = new ForLoop();
		expected.getInit().add(newAssignment("i", newConstantValue("0")));
		expected.getInit().add(newAssignment("j", newConstantValue("1")));
		IStatement actual = getFirstStatement();

		assertEquals(expected, actual);
		assertFalse(true);
	}

	@Test
	public void condition() {
		updateContext(projectName, packageName + "Condition.java");

		ForLoop expected = new ForLoop();
		expected.setCondition(newConstantValue("true"));

		IStatement actual = getFirstStatement();

		assertEquals(expected, actual);
	}

	@Test
	public void step_SingleStatement() {
		updateContext(projectName, packageName + "Step_SingleStatement.java");

		IMethodDeclaration method = new MethodDeclaration();
		method.getBody().add(
				newVariableDeclaration("i", SSTAnalysisFixture.INT));
		method.getBody().add(newAssignment("i", newConstantValue("0")));

		ForLoop expected = new ForLoop();
		ComposedExpression composedExpr = new ComposedExpression();
		composedExpr.getReferences().add(newVariableReference("i"));
		expected.getStep().add(newAssignment("i", composedExpr));

		IMethodDeclaration actual = getFirstMethod();

		assertEquals(expected, actual);
	}

	@Test
	public void step_MultipleStatements() {
		updateContext(projectName, packageName + "Step_MultipleStatements.java");

		IMethodDeclaration method = new MethodDeclaration();
		method.getBody().add(
				newVariableDeclaration("i", SSTAnalysisFixture.INT));
		method.getBody().add(newAssignment("i", newConstantValue("0")));

		ForLoop expected = new ForLoop();
		ComposedExpression composedExpr = new ComposedExpression();
		composedExpr.getReferences().add(newVariableReference("i"));
		expected.getStep().add(newAssignment("i", composedExpr));
		expected.getStep().add(newAssignment("i", composedExpr));

		IMethodDeclaration actual = getFirstMethod();

		assertEquals(expected, actual);
	}

	@Test
	public void bodyWithStatement() {
		updateContext(projectName, packageName + "BodyWithStatement.java");

		ForLoop expected = new ForLoop();
		expected.setCondition(new UnknownExpression());
		expected.getBody().add(
				newVariableDeclaration("i", SSTAnalysisFixture.INT));
		
		IStatement actual = getFirstStatement();

		assertEquals(expected, actual);

	}
}
