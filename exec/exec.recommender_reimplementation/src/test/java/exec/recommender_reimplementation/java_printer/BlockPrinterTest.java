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
package exec.recommender_reimplementation.java_printer;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.TypeName;
import static cc.kave.commons.model.ssts.impl.SSTUtil.*;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;

public class BlockPrinterTest extends JavaPrintingVisitorBaseTest {

	@Test
	public void testForEachLoop() {
		assertPrint(forEachLoop("e", "elements", new ContinueStatement()),
				"for (? e : elements)", "{", "    continue;", "}");
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
	public void testSimpleWhileLoop() {
		assertPrint(
				whileLoop(loopHeader(returnStatement(constant("true"))),
						new ContinueStatement(), new BreakStatement()),
				"while (true)", "{", "    continue;", "    break;", "}");
	}

	@Test
	public void testComplexWhileLoop() {
		assertPrint(
				whileLoop(
						loopHeader(
								declareVar("var", TypeName
										.newTypeName("SomeType, SomeAssembly")),
								assignmentToLocal("var", nullExpr()),
								returnStatement(referenceExprToVariable("var"))),
						new ContinueStatement(), new BreakStatement()),
				"SomeType var;", "var = null;", "while (var)", "{",
				"    continue;", "    break;", "    var = null;", "}");
	}

	@Test
	public void testSimpleDoLoop() {
		assertPrint(
				doLoop(loopHeader(returnStatement(referenceExprToVariable("true"))),
						new ContinueStatement(), new BreakStatement()), "do",
				"{", "    continue;", "    break;", "}", "while (true);");
	}

	@Test
	public void testComplexDoLoop() {
		assertPrint(
				doLoop(loopHeader(
						declareVar("var",
								TypeName.newTypeName("SomeType, SomeAssembly")),
						assignmentToLocal("var", nullExpr()),
						returnStatement(referenceExprToVariable("var"))),
						new ContinueStatement(), new BreakStatement()),
				"SomeType var;", "var = null;", "do", "{", "    continue;",
				"    break;", "    var = null;", "}", "while (var);");
	}
	
	@Test
	public void testForLoop() {
		assertPrint(forLoop("var", loopHeader(
				declare(varRef("condition")),
				assign(varRef("condition"), binExpr(BinaryOperator.Equal, referenceExprToVariable("var"), constant("2"))),
				returnStatement(referenceExprToVariable("condition"))) , new ContinueStatement()), 
				"? var;", 
				"var = 0;", 
				"? condition;",
				"condition = var == 2;",
				"for (;condition;)", "{", 
				"    continue;",
				"    var = 2;",
				"    condition = var == 2;",
				"}");
	}

}
