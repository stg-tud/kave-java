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

import cc.kave.commons.model.names.csharp.LambdaName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CastExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.TypeCheckExpression;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
public class ExpressionPrinterTest extends JavaPrintingVisitorBaseTest{
	
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
}
