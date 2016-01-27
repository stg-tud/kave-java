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

import org.junit.Test;

import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class ForEachLoopAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void basicForEachLoop() {
		MethodDeclaration expected = newDefaultMethodDeclaration();
		// TODO: List statt boolean
		expected.getBody().add(
				newVariableDeclaration("list", SSTAnalysisFixture.BOOLEAN));
		expected.getBody()
				.add(newAssignment(
						"list",
						newInvokeConstructor(SSTAnalysisFixture.LIST_OF_INT_INIT)));
		ForEachLoop loop = new ForEachLoop();
		loop.setDeclaration(newVariableDeclaration("i", SSTAnalysisFixture.INT));
		loop.setLoopedReference(newVariableReference("list"));
		expected.getBody().add(loop);

		IMethodDeclaration actual = getFirstMethod();

		assertEquals(expected, actual);
	}

	@Test
	public void withStatementInBody() {
		MethodDeclaration expected = newDefaultMethodDeclaration();
		// TODO: List statt boolean
		expected.getBody().add(
				newVariableDeclaration("list", SSTAnalysisFixture.BOOLEAN));
		expected.getBody()
				.add(newAssignment(
						"list",
						newInvokeConstructor(SSTAnalysisFixture.LIST_OF_INT_INIT)));
		ForEachLoop loop = new ForEachLoop();
		loop.setDeclaration(newVariableDeclaration("i", SSTAnalysisFixture.INT));
		loop.setLoopedReference(newVariableReference("list"));
		loop.getBody().add(new BreakStatement());
		expected.getBody().add(loop);

		IMethodDeclaration actual = getFirstMethod();

		assertEquals(expected, actual);
	}

	@Test
	public void inlineDefinitionOfEnumerable() {
		MethodDeclaration expected = newDefaultMethodDeclaration();
		// TODO: List statt boolean
		expected.getBody().add(
				newVariableDeclaration("list", SSTAnalysisFixture.BOOLEAN));
		ForEachLoop loop = new ForEachLoop();
		loop.setDeclaration(newVariableDeclaration("i", SSTAnalysisFixture.INT));
		loop.setLoopedReference(newVariableReference("list"));
		loop.getBody().add(new BreakStatement());
		expected.getBody().add(loop);

		IMethodDeclaration actual = getFirstMethod();

		assertEquals(expected, actual);
	}
}
