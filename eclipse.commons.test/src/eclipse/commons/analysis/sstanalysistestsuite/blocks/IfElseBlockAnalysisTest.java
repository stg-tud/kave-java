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

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;

public class IfElseBlockAnalysisTest extends BaseSSTAnalysisTest {

	private final String projectName = "testproject";
	private final String packageName = "ifelseblockanalysistest;";

	@Test
	public void basicIfElseBlock() {
		updateContext(projectName, packageName + "BasicIfElseBlock.java");

		IfElseBlock expected = new IfElseBlock();
		expected.setCondition(newConstantValue("true"));

		IStatement actual = getFirstStatement();

		assertEquals(expected, actual);
	}

	@Test
	public void withThenStatement() {
		updateContext(projectName, packageName + "WithThenStatement.java");

		IfElseBlock expected = new IfElseBlock();
		expected.setCondition(newConstantValue("true"));
		expected.getThen().add(newEmptyCompletionExpression());

		IStatement actual = getFirstStatement();

		assertEquals(expected, actual);
	}

	@Test
	public void withElseStatement() {
		updateContext(projectName, packageName + "WithElseStatement.java");

		IfElseBlock expected = new IfElseBlock();
		expected.setCondition(newConstantValue("true"));
		expected.getElse().add(newEmptyCompletionExpression());

		IStatement actual = getFirstStatement();

		assertEquals(expected, actual);
	}

	@Test
	public void withStatements() {
		updateContext(projectName, packageName + "WithStatements.java");

		IfElseBlock expected = new IfElseBlock();
		expected.setCondition(newConstantValue("true"));
		expected.getThen().add(newEmptyCompletionExpression());
		expected.getElse().add(newEmptyCompletionExpression());

		IStatement actual = getFirstStatement();

		assertEquals(expected, actual);
	}

	@Test
	public void noElseBlock() {
		updateContext(projectName, packageName + "NoElseBlock.java");

		IfElseBlock expected = new IfElseBlock();
		expected.setCondition(newConstantValue("true"));
		expected.getThen().add(newEmptyCompletionExpression());

		IStatement actual = getFirstStatement();

		assertEquals(expected, actual);
	}
}
