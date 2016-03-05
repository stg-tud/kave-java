/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.extraction;

import java.util.Map;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.pointsto.analysis.visitors.TraversingVisitor;

public class CompletionExpressionContextVisitor
		extends TraversingVisitor<Map<Class<? extends ISSTNode>, ISSTNode>, Void> {

	@Override
	public Void visit(ISST sst, Map<Class<? extends ISSTNode>, ISSTNode> context) {
		try {
			return super.visit(sst, context);
		} catch (FoundCompletionExpressionException e) {
			return null;
		}
	}

	@Override
	public Void visit(IMethodDeclaration stmt, Map<Class<? extends ISSTNode>, ISSTNode> context) {
		context.put(IMethodDeclaration.class, stmt);
		return super.visit(stmt, context);
	}

	@Override
	public Void visit(IExpressionStatement stmt, Map<Class<? extends ISSTNode>, ISSTNode> context) {
		context.put(IStatement.class, stmt);
		return super.visit(stmt, context);
	}

	@Override
	public Void visit(ICompletionExpression entity, Map<Class<? extends ISSTNode>, ISSTNode> context) {
		if (entity == context.get(ICompletionExpression.class)) {
			throw new FoundCompletionExpressionException();
		}
		return null;
	}

	private static class FoundCompletionExpressionException extends RuntimeException {

		private static final long serialVersionUID = -649729722720468459L;

	}
}
