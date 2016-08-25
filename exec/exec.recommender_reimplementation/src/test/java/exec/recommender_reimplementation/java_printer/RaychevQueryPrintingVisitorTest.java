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

import static cc.kave.commons.model.ssts.impl.SSTUtil.completionExpr;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import exec.recommender_reimplementation.java_printer.JavaPrintingVisitor.InvalidJavaCodeException;

public class RaychevQueryPrintingVisitorTest extends RaychevPrintingVisitorBaseTest{

	@Test
	public void addsHoleMethodOnCompletionExpression() {
		assertPrint(completionExpr("variable"), "UNK.Must1(variable)");
	}

	@Test(expected = InvalidJavaCodeException.class)
	public void exceptionWhenCompletionExpressionHasNoValidVariableReference() {
		assertPrint(new CompletionExpression(), "");
	}

	@Test(expected = InvalidJavaCodeException.class)
	public void exceptionWhenCompletionExpressionHasEmptyVariableReference() {
		assertPrint(completionExpr(""), "");
	}

	@Test(expected = InvalidJavaCodeException.class)
	public void exceptionOnUnknownReference() {
		assertPrint(new UnknownReference());
	}

	@Test(expected = InvalidJavaCodeException.class)
	public void exceptionOnUnknownMethodNameInInvocation() {
		assertPrint(new InvocationExpression());
	}

}
