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

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.LockBlock;
import cc.kave.commons.model.ssts.impl.blocks.UsingBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;

public class BlockPrinterTest extends JavaPrintingVisitorBaseTest{
	
	@Test
	public void testForEachLoop() {
		ForEachLoop sst = new ForEachLoop();
		sst.setDeclaration(SSTUtil.declare("e", TypeName.newTypeName("T,P")));
		sst.setLoopedReference(SSTUtil.variableReference("elements"));
		sst.getBody().add(new ContinueStatement());

		assertPrint(sst, "for (T e : elements)", "{", "    continue;", "}");
	}
	
	@Test
	public void testUsingBlock() {
		UsingBlock sst = new UsingBlock();
		sst.setReference(SSTUtil.variableReference("variable"));
		sst.getBody().add(new BreakStatement());

		assertPrint(sst, "try (variable)", "{", "    break;", "}");
	}
	
	@Test
	public void testLockBlock() {
		LockBlock sst = new LockBlock();
		sst.setReference(SSTUtil.variableReference("variable"));
		sst.getBody().add(new ContinueStatement());

		assertPrint(sst, "synchronized (variable)", "{", "    continue;", "}");
	}
	
	@Test
	public void testSimpleWhileLoop() {
		WhileLoop sst = new WhileLoop();
		LoopHeaderBlockExpression loopHeader = new LoopHeaderBlockExpression();
		ReturnStatement returnStatement = new ReturnStatement();
		returnStatement.setExpression(constant("true"));
		loopHeader.getBody().add(returnStatement);
		sst.setCondition(loopHeader);
		sst.getBody().add(new ContinueStatement());
		sst.getBody().add(new BreakStatement());

		assertPrint(sst, "while (true)", "{", "    continue;", "    break;",
				"}");
	}
	
	@Test
	public void testComplexWhileLoop() {
		WhileLoop sst = new WhileLoop();
		LoopHeaderBlockExpression loopHeader = new LoopHeaderBlockExpression();
		IVariableDeclaration varDecl = SSTUtil.declareVar("var", TypeName.newTypeName("SomeType, SomeAssembly"));
		IAssignment assignment = SSTUtil.assignmentToLocal("var", SSTUtil.nullExpr());
		ReturnStatement returnStatement = new ReturnStatement();
		IReferenceExpression referenceExpression = SSTUtil.referenceExprToVariable("var");
		returnStatement.setExpression(referenceExpression);
		loopHeader.setBody(Lists.newArrayList(varDecl, assignment, returnStatement));
		sst.setCondition(loopHeader);
		sst.getBody().add(new ContinueStatement());
		sst.getBody().add(new BreakStatement());

		assertPrint(sst, "SomeType var;", "var = null;", "while (var)", "{", "    continue;", "    break;", "    var = null;",
				"}");
	}
	
	@Test
	public void testSimpleDoLoop() {
		DoLoop sst = new DoLoop();
		LoopHeaderBlockExpression loopHeader = new LoopHeaderBlockExpression();
		ReturnStatement returnStatement = new ReturnStatement();
		returnStatement.setExpression(constant("true"));
		loopHeader.getBody().add(returnStatement);
		sst.setCondition(loopHeader);
		sst.getBody().add(new ContinueStatement());
		sst.getBody().add(new BreakStatement());
		assertPrint(sst, "do", "{", "    continue;", "    break;", "}", "while (true);");
	}
	
	@Test
	public void testComplexDoLoop() {
		DoLoop sst = new DoLoop();
		LoopHeaderBlockExpression loopHeader = new LoopHeaderBlockExpression();
		IVariableDeclaration varDecl = SSTUtil.declareVar("var", TypeName.newTypeName("SomeType, SomeAssembly"));
		IAssignment assignment = SSTUtil.assignmentToLocal("var", SSTUtil.nullExpr());
		ReturnStatement returnStatement = new ReturnStatement();
		IReferenceExpression referenceExpression = SSTUtil.referenceExprToVariable("var");
		returnStatement.setExpression(referenceExpression);
		loopHeader.setBody(Lists.newArrayList(varDecl, assignment, returnStatement));
		sst.setCondition(loopHeader);
		sst.getBody().add(new ContinueStatement());
		sst.getBody().add(new BreakStatement());

		assertPrint(sst, "SomeType var;", "var = null;", "do", "{", "    continue;", "    break;", "    var = null;",
				"}", "while (var);");
	}

}
