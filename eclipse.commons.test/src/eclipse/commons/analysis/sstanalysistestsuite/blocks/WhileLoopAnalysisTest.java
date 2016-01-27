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

import java.util.ArrayList;

import org.junit.Test;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;

public class WhileLoopAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void basicLoop() {
		WhileLoop expected = new WhileLoop();
		expected.setCondition(newConstantValue("true"));

		IStatement actual = getFirstStatement();

		assertEquals(expected, actual);
	}

	@Test
	public void withStatementInBody() {
		WhileLoop expected = new WhileLoop();
		expected.setCondition(newConstantValue("true"));
		expected.getBody().add(new BreakStatement());

		IStatement actual = getFirstStatement();

		assertEquals(expected, actual);
	}
}
