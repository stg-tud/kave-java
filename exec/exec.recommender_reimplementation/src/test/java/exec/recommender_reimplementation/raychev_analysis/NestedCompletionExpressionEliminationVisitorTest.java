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
package exec.recommender_reimplementation.raychev_analysis;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareMethod;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;
import static exec.recommender_reimplementation.raychev_analysis.NestedCompletionExpressionEliminationVisitor.EliminationStrategy.DELETE;
import static exec.recommender_reimplementation.raychev_analysis.NestedCompletionExpressionEliminationVisitor.EliminationStrategy.REPLACE;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;

public class NestedCompletionExpressionEliminationVisitorTest {

	@Test
	public void deletesAssignmentOnDeleteStrategy() {
		IMethodDeclaration methodDecl = declareMethod(assign(variableReference("foo"), new CompletionExpression()));
		IMethodDeclaration expected = declareMethod();

		NestedCompletionExpressionEliminationVisitor sut = new NestedCompletionExpressionEliminationVisitor(DELETE);
		methodDecl.accept(sut, null);

		assertEquals(expected, methodDecl);
	}

	@Test
	public void replacesAssignmentOnReplaceStrategy() {
		IMethodDeclaration methodDecl = declareMethod(assign(variableReference("foo"), new CompletionExpression()));
		IMethodDeclaration expected = declareMethod(SSTUtil.expr(new CompletionExpression()));

		NestedCompletionExpressionEliminationVisitor sut = new NestedCompletionExpressionEliminationVisitor(REPLACE);
		methodDecl.accept(sut, null);

		assertEquals(expected, methodDecl);
	}
}