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

import static cc.kave.commons.model.ssts.impl.SSTUtil.*;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.csharp.LambdaName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CastExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.TypeCheckExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;

public class ExpressionPrinterTest extends JavaPrintingVisitorBaseTest {

	@Test
	public void lambdaExpression() {
		LambdaExpression sst = new LambdaExpression();
		sst.setName(LambdaName.newLambdaName("[T,P]([C, A] p1, [C, B] p2)"));
		sst.setBody(Lists.newArrayList(new ContinueStatement(), new BreakStatement()));

		assertPrint(sst, "(C p1, C p2) ->", "{", "    continue;", "    break;", "}");
	}

	@Test
	public void lambdaExpression_NoParametersAndEmptyBody() {
		LambdaExpression sst = new LambdaExpression();

		assertPrint(sst, "() -> { }");
	}
	
	@Test
	public void testCastExpression() {
		CastExpression sst = new CastExpression();
		sst.setReference(SSTUtil.variableReference("x"));
		assertPrint(sst, "(?) x");
	}

	@Test
	public void testCastExpressionSafe() {
		CastExpression sst = new CastExpression();
		sst.setOperator(CastOperator.SafeCast);
		sst.setReference(SSTUtil.variableReference("x"));
		sst.setTargetType(TypeName.newTypeName("SomeType, SomeAssembly"));
		assertPrint(sst, "x instanceof SomeType ? (SomeType) x : null");
	}

	@Test
	public void testTypeCheckExpression() {
		TypeCheckExpression sst = new TypeCheckExpression();
		sst.setReference(varRef("x"));
		assertPrint(sst, "x instanceof ?");
	}

	@Test
	public void testIndexAccessExpressionArray() {
		IndexAccessExpression indexAccessExpression = new IndexAccessExpression();
		indexAccessExpression.setIndices(Lists.newArrayList(constant("1"), constant("2")));
		indexAccessExpression.setReference(varRef("x"));

		IMethodDeclaration methodDecl = declareMethod(declareVar("x", type("String[]")), expr(indexAccessExpression));
		assertPrint(methodDecl, "? ???()", "{", "    String[] x;", "    x[1,2];", "}");
	}

	@Test
	public void testIndexAccessExpressionList() {
		IndexAccessExpression indexAccessExpression = new IndexAccessExpression();
		indexAccessExpression.setIndices(Lists.newArrayList(constant("1")));
		indexAccessExpression.setReference(varRef("x"));

		IMethodDeclaration methodDecl = declareMethod(declareVar("x", type("List")), expr(indexAccessExpression));
		assertPrint(methodDecl, "? ???()", "{", "    List x;", "    x.get(1);", "}");
	}

	@Test
	public void constantValueExpression_WithoutValue() {
		assertPrint(constant(""), "\"...\"");
	}

	@Test
	public void constantValueExpression_WithString() {
		assertPrint(constant("val"), "\"val\"");
	}

	@Test
	public void constantValueExpression_NullLiteralIsUsedAsString() {
		assertPrint(constant("null"), "\"null\"");
	}

	@Test
	public void constantValueExpression_WithInt() {
		assertPrint(constant("1"), "1");
	}

	@Test
	public void constantValueExpression_WithFloat() {
		assertPrint(constant("0.123"), "0.123");
	}

	@Test
	public void constantValueExpression_WithBoolTrue() {
		assertPrint(constant("true"), "true");
	}

	@Test
	public void constantValueExpression_WithBoolFalse() {
		assertPrint(constant("false"), "false");
	}

	@Test
	public void constantValueExpression_Null() {
		IMethodDeclaration methodDecl = declareMethod(declareVar("x", type("s:System.Int32")),
				assign(variableReference("x"), new ConstantValueExpression()));
		assertPrint(methodDecl, "? ???()", "{", "    int x;", "    x = 0;", "}");
	}

	@Test
	public void composedExpression() {
		IMethodDeclaration methodDecl = declareMethod(declareVar("x", type("s:System.Int32")),
				assign(variableReference("x"), new ComposedExpression()));

		assertPrint(methodDecl, "? ???()", "{", "    int x;", "    x = 0;", "}");
	}

	@Test
	public void unknownExpressionNestedIntoBinaryExpression() {
		IMethodDeclaration methodDecl = declareMethod(
				declareVar("x", type("s:System.Int32")),
				assign(variableReference("x"),
						binExpr(BinaryOperator.Plus, new UnknownExpression(), new UnknownExpression())));

		assertPrint(methodDecl, "? ???()", "{", "    int x;", "    x = 0 + 0;", "}");
	}
		
}
