/**
 * Copyright 2016 Technische Universität Darmstadt
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
package exec.recommender_reimplementation.java_printer.javaPrinterTestSuite;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.assignmentToLocal;
import static cc.kave.commons.model.ssts.impl.SSTUtil.binExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declare;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareVar;
import static cc.kave.commons.model.ssts.impl.SSTUtil.doLoop;
import static cc.kave.commons.model.ssts.impl.SSTUtil.forEachLoop;
import static cc.kave.commons.model.ssts.impl.SSTUtil.forLoop;
import static cc.kave.commons.model.ssts.impl.SSTUtil.lockBlock;
import static cc.kave.commons.model.ssts.impl.SSTUtil.loopHeader;
import static cc.kave.commons.model.ssts.impl.SSTUtil.nullExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.referenceExprToVariable;
import static cc.kave.commons.model.ssts.impl.SSTUtil.returnStatement;
import static cc.kave.commons.model.ssts.impl.SSTUtil.usingBlock;
import static cc.kave.commons.model.ssts.impl.SSTUtil.whileLoop;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.UncheckedBlock;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;

public class BlockPrinterTest extends JavaPrintingVisitorBaseTest {

	@Test
	public void testForEachLoop() {
		assertPrint(forEachLoop("e", "elements", new ContinueStatement()),
				"for (Object e : elements)", "{", "    continue;", "}");
	}

	@Test
	public void testUsingBlock() {
		assertPrint(usingBlock(varRef("variable"), new BreakStatement()),
				"try (variable)", "{", "    break;", "}");
	}

	@Test
	public void testLockBlock() {
		assertPrint(lockBlock("variable", new ContinueStatement()),
				"synchronized (variable)", "{", "    continue;", "}");
	}
	
	@Test
	public void testUncheckedBlock() {
		UncheckedBlock sst = new UncheckedBlock();
		sst.getBody().add(new BreakStatement());

		assertPrint(sst, "break;", "");
	}
	
	@Test
	public void testIfElseBlock() {
		IfElseBlock sst = new IfElseBlock();
		sst.setCondition(constant("true"));
		sst.getThen().add(new ContinueStatement());
		sst.getElse().add(new BreakStatement());

		assertPrint(sst, "if (CSharpConverter.toBool(CSharpConstants.TRUE))", "{", "    continue;", "}", "else", "{",
				"    break;", "}");
	}

	@Test
	public void testIfElseBlock_NoElseBlock() {
		IfElseBlock sst = new IfElseBlock();
		sst.setCondition(constant("true"));
		sst.getThen().add(new ContinueStatement());

		assertPrint(sst, "if (CSharpConverter.toBool(CSharpConstants.TRUE))", "{", "    continue;", "}");
	}
	
	@Test
	public void testSimpleWhileLoop() {
		assertPrint(
				whileLoop(loopHeader(returnStatement(constant("true"))),
						new ContinueStatement(), new BreakStatement()),
				"while (CSharpConverter.toBool(CSharpConstants.TRUE))", "{", "    continue;", "    break;", "}");
	}

	@Test
	public void testComplexWhileLoop() {
		assertPrint(
				whileLoop(
						loopHeader(
								declareVar("var", Names.newType("SomeType, SomeAssembly")),
								assignmentToLocal("var", nullExpr()),
								returnStatement(referenceExprToVariable("var"))),
						new ContinueStatement(), new BreakStatement()),
				"SomeType var = null;", "var = null;", "while (CSharpConverter.toBool(var))", "{",
				"    continue;", "    break;", "    var = null;", "}");
	}

	@Test
	public void testSimpleDoLoop() {
		assertPrint(
				doLoop(loopHeader(returnStatement(constant("true"))),
						new ContinueStatement(), new BreakStatement()), "do",
				"{", "    continue;", "    break;", "}", "while (CSharpConverter.toBool(CSharpConstants.TRUE));");
	}

	@Test
	public void testComplexDoLoop() {
		assertPrint(
				doLoop(loopHeader(
						declareVar("var",
								Names.newType("SomeType, SomeAssembly")),
						assignmentToLocal("var", nullExpr()),
						returnStatement(referenceExprToVariable("var"))),
						new ContinueStatement(), new BreakStatement()),
				"SomeType var = null;", "var = null;", "do", "{", "    continue;",
				"    break;", "    var = null;", "}", "while (CSharpConverter.toBool(var));");
	}
	
	@Test
	public void testForLoop() {
		assertPrint(forLoop("var", loopHeader(
				declare(varRef("condition")),
				assign(varRef("condition"), binExpr(BinaryOperator.Equal, referenceExprToVariable("var"), constant("2"))),
				returnStatement(referenceExprToVariable("condition"))) , new ContinueStatement()), 
				"Object var = null;",
				"var = 0;", 
				"Object condition = null;",
				"condition = var == 2;",
				"for (;CSharpConverter.toBool(condition);)", "{", 
				"    continue;",
				"    var = 2;",
				"    condition = var == 2;",
				"}");
	}
}
