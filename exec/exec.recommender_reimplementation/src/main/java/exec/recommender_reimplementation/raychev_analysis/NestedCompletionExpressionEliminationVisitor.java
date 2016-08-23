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

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.statements.IAssignment;

public class NestedCompletionExpressionEliminationVisitor extends AbstractTraversingNodeVisitor<Void, Void> {
	private EliminationStrategy strategy;

	public enum EliminationStrategy {
		DELETE, REPLACE
	}

	public NestedCompletionExpressionEliminationVisitor(EliminationStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	protected void visit(List<IStatement> body, Void context) {
		List<IStatement> statementsClone = new ArrayList<>(body);
		for (IStatement stmt : statementsClone) {
			if (stmt instanceof IAssignment) {
				if (containsCompletionExpression((IAssignment) stmt)) {
					if (strategy == EliminationStrategy.REPLACE) {
						IStatement replacementNode = transformAssignment((IAssignment) stmt);
						body.add(body.indexOf(stmt), replacementNode);
					}
					body.remove(stmt);
				}
				continue;
			}
			stmt.accept(this, context);
		}
	}

	private boolean containsCompletionExpression(IAssignment stmt) {
		return stmt.getExpression() instanceof ICompletionExpression;
	}

	private IStatement transformAssignment(IAssignment assignment) {
		ExpressionStatement exprStmt = new ExpressionStatement();
		exprStmt.setExpression(assignment.getExpression());
		return exprStmt;
	}
}
