/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.utils.sstprinter.visitortestsuite;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.assignmentToLocal;
import static cc.kave.commons.model.ssts.impl.SSTUtil.binExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declare;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareVar;
import static cc.kave.commons.model.ssts.impl.SSTUtil.doLoop;
import static cc.kave.commons.model.ssts.impl.SSTUtil.forLoop;
import static cc.kave.commons.model.ssts.impl.SSTUtil.loopHeader;
import static cc.kave.commons.model.ssts.impl.SSTUtil.nullExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.referenceExprToVariable;
import static cc.kave.commons.model.ssts.impl.SSTUtil.returnStatement;
import static cc.kave.commons.model.ssts.impl.SSTUtil.whileLoop;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.ParameterName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.LockBlock;
import cc.kave.commons.model.ssts.impl.blocks.SwitchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.blocks.UncheckedBlock;
import cc.kave.commons.model.ssts.impl.blocks.UnsafeBlock;
import cc.kave.commons.model.ssts.impl.blocks.UsingBlock;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;

public class BlockPrinterTest extends SSTPrintingVisitorBaseTest {

	@Test
	public void testForEachLoop() {
		ForEachLoop sst = new ForEachLoop();
		sst.setDeclaration(SSTUtil.declare("e", TypeName.newTypeName("T,P")));
		sst.setLoopedReference(SSTUtil.variableReference("elements"));
		sst.getBody().add(new ContinueStatement());

		assertPrint(sst, "foreach (T e in elements)", "{", "    continue;", "}");
	}

	@Test
	public void testSwitchBlock() {
		SwitchBlock sst = new SwitchBlock();
		CaseBlock case1 = new CaseBlock();
		case1.setLabel(constant("1"));
		case1.getBody().add(new BreakStatement());
		case1.getBody().add(new BreakStatement());
		CaseBlock case2 = new CaseBlock();
		case2.setLabel(constant("2"));
		case2.getBody().add(new BreakStatement());
		sst.setReference(SSTUtil.variableReference("a"));
		sst.getDefaultSection().add(new BreakStatement());
		sst.getSections().add(case1);
		sst.getSections().add(case2);

		assertPrint(sst, "switch (a)", "{", "    case 1:", "        break;", "        break;", "    case 2:",
				"        break;", "    default:", "        break;", "}");
	}

	@Test
	public void testSwitchBlock_NoDefaultBlock() {
		SwitchBlock sst = new SwitchBlock();
		CaseBlock case1 = new CaseBlock();
		case1.setLabel(constant("1"));
		case1.getBody().add(new BreakStatement());
		case1.getBody().add(new BreakStatement());
		CaseBlock case2 = new CaseBlock();
		case2.setLabel(constant("2"));
		case2.getBody().add(new BreakStatement());
		sst.setReference(SSTUtil.variableReference("a"));
		sst.getSections().add(case1);
		sst.getSections().add(case2);

		assertPrint(sst, "switch (a)", "{", "    case 1:", "        break;", "        break;", "    case 2:",
				"        break;", "}");
	}

	@Test
	public void testTryBlock() {
		TryBlock sst = new TryBlock();
		ThrowStatement s = new ThrowStatement();
		s.setReference(varRef("ExceptionType"));
		CatchBlock catch1 = new CatchBlock();
		catch1.setParameter(ParameterName.newParameterName("[ExceptionType,P] e"));
		catch1.getBody().add(new BreakStatement());
		sst.getCatchBlocks().add(catch1);
		sst.getFinally().add(new ContinueStatement());
		sst.getBody().add(s);

		assertPrint(sst, "try", "{", "    throw new ExceptionType();", "}", "catch (ExceptionType e)", "{",
				"    break;", "}", "finally", "{", "    continue;", "}");
	}

	@Test
	public void testTryBlock_NoFinallyBlock() {
		TryBlock sst = new TryBlock();
		ThrowStatement s = new ThrowStatement();
		s.setReference(varRef("ExceptionType"));
		CatchBlock catch1 = new CatchBlock();
		catch1.setParameter(ParameterName.newParameterName("[ExceptionType,P] e"));
		catch1.getBody().add(new BreakStatement());
		sst.getCatchBlocks().add(catch1);
		sst.getBody().add(s);

		assertPrint(sst, "try", "{", "    throw new ExceptionType();", "}", "catch (ExceptionType e)", "{",
				"    break;", "}");
	}

	@Test
	public void testTryBlock_GeneralCatchBlock() {
		TryBlock sst = new TryBlock();
		ThrowStatement s = new ThrowStatement();
		s.setReference(varRef("ExceptionType"));
		CatchBlock catch1 = new CatchBlock();
		catch1.setKind(CatchBlockKind.General);
		catch1.getBody().add(new BreakStatement());
		sst.getCatchBlocks().add(catch1);
		sst.getBody().add(s);

		assertPrint(sst, "try", "{", "    throw new ExceptionType();", "}", "catch", "{", "    break;", "}");
	}

	@Test
	public void testTryBlock_UnnamedCatchBlock() {
		TryBlock sst = new TryBlock();
		ThrowStatement s = new ThrowStatement();
		s.setReference(varRef("ExceptionType"));
		CatchBlock catch1 = new CatchBlock();
		catch1.setParameter(ParameterName.newParameterName("[ExceptionType,P] e"));
		catch1.setKind(CatchBlockKind.Unnamed);
		catch1.getBody().add(new BreakStatement());
		sst.getCatchBlocks().add(catch1);
		sst.getBody().add(s);

		assertPrint(sst, "try", "{", "    throw new ExceptionType();", "}", "catch (ExceptionType)", "{", "    break;",
				"}");
	}

	@Test
	public void testUncheckedBlock() {
		UncheckedBlock sst = new UncheckedBlock();
		sst.getBody().add(new BreakStatement());

		assertPrint(sst, "unchecked", "{", "    break;", "}");
	}

	@Test
	public void testUnsafeBlock() {
		UnsafeBlock sst = new UnsafeBlock();
		assertPrint(sst, "unsafe { /* content ignored */ }");
	}

	@Test
	public void testUsingBlock() {
		UsingBlock sst = new UsingBlock();
		sst.setReference(SSTUtil.variableReference("variable"));
		sst.getBody().add(new BreakStatement());

		assertPrint(sst, "using (variable)", "{", "    break;", "}");
	}

	@Test
	public void testIfElseBlock() {
		IfElseBlock sst = new IfElseBlock();
		sst.setCondition(constant("true"));
		sst.getThen().add(new ContinueStatement());
		sst.getElse().add(new BreakStatement());

		assertPrint(sst, "if (true)", "{", "    continue;", "}", "else", "{", "    break;", "}");
	}

	@Test
	public void testIfElseBlock_NoElseBlock() {
		IfElseBlock sst = new IfElseBlock();
		sst.setCondition(constant("true"));
		sst.getThen().add(new ContinueStatement());

		assertPrint(sst, "if (true)", "{", "    continue;", "}");
	}

	@Test
	public void testLockBlock() {
		LockBlock sst = new LockBlock();
		sst.setReference(SSTUtil.variableReference("variable"));
		sst.getBody().add(new ContinueStatement());

		assertPrint(sst, "lock (variable)", "{", "    continue;", "}");
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
