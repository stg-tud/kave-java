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

import org.junit.Test;

import cc.kave.commons.model.names.csharp.LambdaName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.impl.expressions.assignable.BinaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CastExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.TypeCheckExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;

public class ExpressionPrinterTest extends SSTPrintingVisitorBaseTest {
	@Test
	public void nullExpression() {
		assertPrint(new NullExpression(), "null");
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
	public void invocationExpression_ConstantValue() {
		InvocationExpression sst = new InvocationExpression();
		sst.setReference(varRef("this"));
		sst.setMethodName(MethodName.newMethodName("[R,P] [D,P].M([T,P] p)"));
		sst.getParameters().add(constant("1"));

		assertPrint(sst, "this.M(1)");
	}

	@Test
	public void invocationExpression_NullValue() {
		InvocationExpression sst = new InvocationExpression();
		sst.setReference(varRef("this"));
		sst.setMethodName(MethodName.newMethodName("[R,P] [D,P].M([T,P] p)"));
		sst.getParameters().add(new NullExpression());

		assertPrint(sst, "this.M(null)");
	}

	@Test
	public void invocationExpression_Static() {
		InvocationExpression sst = new InvocationExpression();
		sst.setReference(varRef("should be ignored anyways"));
		sst.setMethodName(MethodName.newMethodName("static [R,P] [D,P].M([T,P] p)"));
		sst.getParameters().add(new NullExpression());

		assertPrint(sst, "D.M(null)");
	}

	@Test
	public void ifElseExpression() {
		IfElseExpression sst = new IfElseExpression();
		sst.setCondition(constant("true"));
		sst.setThenExpression(constant("1"));
		sst.setElseExpression(constant("2"));

		assertPrint(sst, "(true) ? 1 : 2");
	}

	@Test
	public void referenceExpression() {
		ReferenceExpression sst = new ReferenceExpression();
		sst.setReference(varRef("variable"));
		assertPrint(sst, "variable");
	}

	@Test
	public void composedExpression() {
		ComposedExpression sst = new ComposedExpression();
		sst.getReferences().add(varRef("a"));
		sst.getReferences().add(varRef("b"));
		sst.getReferences().add(varRef("c"));

		assertPrint(sst, "composed(a, b, c)");
	}

	@Test
	public void loopHeaderBlockExpression() {
		LoopHeaderBlockExpression sst = new LoopHeaderBlockExpression();
		sst.getBody().add(new ContinueStatement());
		sst.getBody().add(new BreakStatement());

		assertPrint(sst, "", "{", "    continue;", "    break;", "}");
	}

	@Test
	public void unknownExpression() {
		UnknownExpression sst = new UnknownExpression();
		assertPrint(sst, "???");
	}

	@Test
	public void completionExpression_OnNothing() {
		CompletionExpression sst = new CompletionExpression();
		assertPrint(sst, "$");
	}

	@Test
	public void completionExpression_OnToken() {
		CompletionExpression sst = new CompletionExpression();
		sst.setToken("t");
		assertPrint(sst, "t$");
	}

	@Test
	public void completionExpression_OnVariableReference() {
		CompletionExpression sst = new CompletionExpression();
		sst.setObjectReference(varRef("o"));
		assertPrint(sst, "o.$");
	}

	@Test
	public void completionExpression_OnVariableReferenceWithToken() {
		CompletionExpression sst = new CompletionExpression();
		sst.setObjectReference(varRef("o"));
		sst.setToken("t");
		assertPrint(sst, "o.t$");
	}

	@Test
	public void completionExpression_OnTypeReference() {
		CompletionExpression sst = new CompletionExpression();
		sst.setTypeReference(TypeName.newTypeName("T,P"));
		assertPrint(sst, "T.$");
	}

	@Test
	public void completionExpression_OnTypeReferenceWithToken() {
		CompletionExpression sst = new CompletionExpression();
		sst.setTypeReference(TypeName.newTypeName("T,P"));
		sst.setToken("t");
		assertPrint(sst, "T.t$");
	}

	@Test
	public void completionExpression_OnTypeReference_GenericType() {
		CompletionExpression sst = new CompletionExpression();
		sst.setTypeReference(TypeName.newTypeName("T`1[[G -> A,P]],P"));
		assertPrint(sst, "T<A>.$");
	}

	@Test
	public void completionExpression_OnTypeReference_UnresolvedGenericType() {
		CompletionExpression sst = new CompletionExpression();
		sst.setTypeReference(TypeName.newTypeName("T`1[[G]],P"));
		assertPrint(sst, "T<?>.$");
	}

	@Test
	public void lambdaExpression() {
		LambdaExpression sst = new LambdaExpression();
		sst.setName(LambdaName.newLambdaName("[T,P]([C, A] p1, [C, B] p2)"));
		sst.getBody().add(new ContinueStatement());
		sst.getBody().add(new BreakStatement());

		assertPrint(sst, "(C p1, C p2) =>", "{", "    continue;", "    break;", "}");
	}

	@Test
	public void lambdaExpression_NoParametersAndEmptyBody() {
		LambdaExpression sst = new LambdaExpression();

		assertPrint(sst, "() => { }");
	}

	@Test
	public void binaryExpression() {
		BinaryExpression sst = new BinaryExpression();

		assertPrint(sst, "");
	}

	@Test
	public void castExpression() {
		CastExpression sst = new CastExpression();

		assertPrint(sst, "");
	}
	
	@Test
	public void indexAccessExpression() {
		IndexAccessExpression sst = new IndexAccessExpression();

		assertPrint(sst, "");
	}
	
	@Test
	public void typeCheckExpression() {
		TypeCheckExpression sst = new TypeCheckExpression();

		assertPrint(sst, "");
	}
	
	@Test
	public void unaryExpression() {
		UnaryExpression sst = new UnaryExpression();

		assertPrint(sst, "");
	}
}
