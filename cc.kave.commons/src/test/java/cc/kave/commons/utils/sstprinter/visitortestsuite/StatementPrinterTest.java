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

import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.commons.model.ssts.impl.statements.LabelledStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;
import cc.kave.commons.model.ssts.statements.IAssignment;

public class StatementPrinterTest extends SSTPrintingVisitorBaseTest {
	@Test
	public void BreakStatement() {
		BreakStatement sst = new BreakStatement();
		AssertPrint(sst, "break;");
	}

	@Test

	public void ContinueStatement() {
		ContinueStatement sst = new ContinueStatement();
		AssertPrint(sst, "continue;");
	}

	@Test

	public void Assignment() {
		IAssignment sst = SSTUtil.assignmentToLocal("var", constant("true"));
		AssertPrint(sst, "var = true;");
	}

	@Test

	public void GotoStatement() {
		GotoStatement sst = new GotoStatement();
		sst.setLabel("L");

		AssertPrint(sst, "goto L;");
	}

	@Test

	public void LabelledStatement() {
		LabelledStatement sst = new LabelledStatement();
		sst.setLabel("L");
		sst.setStatement(new ContinueStatement());

		AssertPrint(sst, "L:", "continue;");
	}

	@Test

	public void ThrowStatement() {
		ThrowStatement sst = new ThrowStatement();
		VariableReference ref = new VariableReference();
		ref.setIdentifier("T");
		sst.setReference(ref);

		// note: we can ignore exception constructors and throwing existing
		// objects
		AssertPrint(sst, "throw new T();");
	}

	@Test

	public void ReturnStatement() {
		ReturnStatement sst = new ReturnStatement();
		sst.setExpression(constant("val"));

		AssertPrint(sst, "return \"val\";");
	}

	@Test

	public void ReturnStatement_Void() {
		cc.kave.commons.model.ssts.impl.statements.ReturnStatement sst = new ReturnStatement();
		sst.setIsVoid(true);
		AssertPrint(sst, "return;");
	}

	@Test

	public void ExpressionStatement() {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setReference(SSTUtil.variableReference("this"));
		invocation.setMethodName(MethodName.newMethodName("[ReturnType,P] [DeclaringType,P].M([ParameterType,P] p)"));
		invocation.getParameters().add(constant("1"));

		ExpressionStatement sst = new ExpressionStatement();
		sst.setExpression(invocation);
		AssertPrint(sst, "this.M(1);");
	}

	@Test

	public void UnknownStatement() {
		UnknownStatement sst = new UnknownStatement();
		AssertPrint(sst, "???;");
	}
}
