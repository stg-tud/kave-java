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

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class ForEachLoopAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void basicForEachLoop() {
		ForEachLoop loop = new ForEachLoop();
		loop.setDeclaration(newVariableDeclaration("i", SSTAnalysisFixture.INT));
		loop.setLoopedReference(newVariableReference("list"));

		assertMethod(newVariableDeclaration("list", SSTAnalysisFixture.LIST),
				newAssignment("list", newInvokeConstructor(SSTAnalysisFixture.ARRAYLIST_CTOR)), loop);
	}

	@Test
	public void withStatementInBody() {
		ForEachLoop loop = new ForEachLoop();
		loop.setDeclaration(newVariableDeclaration("i", SSTAnalysisFixture.INT));
		loop.setLoopedReference(newVariableReference("list"));
		loop.getBody().add(new BreakStatement());
		
		assertMethod(newVariableDeclaration("list", SSTAnalysisFixture.LIST),
				newAssignment("list", newInvokeConstructor(SSTAnalysisFixture.ARRAYLIST_CTOR)), loop);
	}

	@Test
	public void inlineDefinitionOfEnumerable() {
		ForEachLoop loop = new ForEachLoop();
		loop.setDeclaration(newVariableDeclaration("i", SSTAnalysisFixture.INT));
		loop.setLoopedReference(newVariableReference("$0"));

		assertMethod(newVariableDeclaration("$0", SSTAnalysisFixture.ARRAY_LIST),
				newAssignment("$0", newInvokeConstructor(SSTAnalysisFixture.ARRAYLIST_CTOR)), loop);
	}
}
