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
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;

public class ExpressionPrinterTest extends SSTPrintingVisitorBaseTest {
	@Test
	public void NullExpression() {
		AssertPrint(new NullExpression(), "null");
	}

	@Test

	public void ConstantValueExpression_WithoutValue() {
		AssertPrint(constant(""), "\"...\"");
	}

	@Test

	public void ConstantValueExpression_WithString() {
		AssertPrint(constant("val"), "\"val\"");
	}

	@Test

	public void ConstantValueExpression_NullLiteralIsUsedAsString() {
		AssertPrint(constant("null"), "\"null\"");
	}

	@Test

	public void ConstantValueExpression_WithInt() {
		AssertPrint(constant("1"), "1");
	}

	@Test

	public void ConstantValueExpression_WithFloat() {
		AssertPrint(constant("0.123"), "0.123");
	}

	@Test

	public void ConstantValueExpression_WithBoolTrue() {
		AssertPrint(constant("true"), "true");
	}

	@Test

	public void ConstantValueExpression_WithBoolFalse() {
		AssertPrint(constant("false"), "false");
	}

	@Test

	public void InvocationExpression_ConstantValue() {
		InvocationExpression sst = new InvocationExpression();
		sst.setReference(varRef("this"));
		sst.setMethodName(MethodName.newMethodName("[R,P] [D,P].M([T,P] p)"));
		sst.getParameters().add(constant("1"));

		AssertPrint(sst, "this.M(1)");
	}

	@Test

	public void InvocationExpression_NullValue() {
		InvocationExpression sst = new InvocationExpression();
		sst.setReference(varRef("this"));
		sst.setMethodName(MethodName.newMethodName("[R,P] [D,P].M([T,P] p)"));
		sst.getParameters().add(new NullExpression());

		AssertPrint(sst, "this.M(null)");
	}

	@Test

	public void InvocationExpression_Static() {
		InvocationExpression sst = new InvocationExpression();
		sst.setReference(varRef("should be ignored anyways"));
		sst.setMethodName(MethodName.newMethodName("static [R,P] [D,P].M([T,P] p)"));
		sst.getParameters().add(new NullExpression());

		AssertPrint(sst, "D.M(null)");
	}

	@Test

	public void IfElseExpression() {
		IfElseExpression sst = new IfElseExpression();
		sst.setCondition(constant("true"));
		sst.setThenExpression(constant("1"));
		sst.setElseExpression(constant("2"));

		AssertPrint(sst, "(true) ? 1 : 2");
	}

	@Test

	public void ReferenceExpression() {
		ReferenceExpression sst = new ReferenceExpression();
		sst.setReference(varRef("variable"));
		AssertPrint(sst, "variable");
	}

	@Test

	public void ComposedExpression() {
		ComposedExpression sst = new ComposedExpression();
		sst.getReferences().add(varRef("a"));
		sst.getReferences().add(varRef("b"));
		sst.getReferences().add(varRef("c"));

		AssertPrint(sst, "composed(a, b, c)");
	}

	@Test

	public void LoopHeaderBlockExpression() {
		LoopHeaderBlockExpression sst = new LoopHeaderBlockExpression();
		sst.getBody().add(new ContinueStatement());
		sst.getBody().add(new BreakStatement());

		AssertPrint(sst, "", "{", "    continue;", "    break;", "}");
	}

	@Test

	public void UnknownExpression() {
		UnknownExpression sst = new UnknownExpression();
		AssertPrint(sst, "???");
	}

	@Test

	public void CompletionExpression_OnNothing() {
		CompletionExpression sst = new CompletionExpression();
		AssertPrint(sst, "$");
	}

	@Test

	public void CompletionExpression_OnToken() {
		CompletionExpression sst = new CompletionExpression();
		sst.setToken("t");
		AssertPrint(sst, "t$");
	}

	@Test

	public void CompletionExpression_OnVariableReference() {
		CompletionExpression sst = new CompletionExpression();
		sst.setObjectReference(varRef("o"));
		AssertPrint(sst, "o.$");
	}

	@Test

	public void CompletionExpression_OnVariableReferenceWithToken() {
		CompletionExpression sst = new CompletionExpression();
		sst.setObjectReference(varRef("o"));
		sst.setToken("t");
		AssertPrint(sst, "o.t$");
	}

	@Test

	public void CompletionExpression_OnTypeReference() {
		CompletionExpression sst = new CompletionExpression();
		sst.setTypeReference(TypeName.newTypeName("T,P"));
		AssertPrint(sst, "T.$");
	}

	@Test

	public void CompletionExpression_OnTypeReferenceWithToken() {
		CompletionExpression sst = new CompletionExpression();
		sst.setTypeReference(TypeName.newTypeName("T,P"));
		sst.setToken("t");
		AssertPrint(sst, "T.t$");
	}

	@Test

	public void CompletionExpression_OnTypeReference_GenericType() {
		CompletionExpression sst = new CompletionExpression();
		sst.setTypeReference(TypeName.newTypeName("T`1[[G -> A,P]],P"));
		AssertPrint(sst, "T<A>.$");
	}

	@Test

	public void CompletionExpression_OnTypeReference_UnresolvedGenericType() {
		CompletionExpression sst = new CompletionExpression();
		sst.setTypeReference(TypeName.newTypeName("T`1[[G]],P"));
		AssertPrint(sst, "T<?>.$");
	}

	@Test

	public void LambdaExpression() {
		LambdaExpression sst = new LambdaExpression();
		sst.setName(LambdaName.newLambdaName("[T,P]([C, A] p1, [C, B] p2)"));
		sst.getBody().add(new ContinueStatement());
		sst.getBody().add(new BreakStatement());

		AssertPrint(sst, "(C p1, C p2) =>", "{", "    continue;", "    break;", "}");
	}

	@Test

	public void LambdaExpression_NoParametersAndEmptyBody() {
		LambdaExpression sst = new LambdaExpression();

		AssertPrint(sst, "() => { }");
	}

}
