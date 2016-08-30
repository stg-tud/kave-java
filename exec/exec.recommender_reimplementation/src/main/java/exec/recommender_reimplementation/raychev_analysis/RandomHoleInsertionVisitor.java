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
import java.util.Random;

import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.visitor.IdentityVisitor;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class RandomHoleInsertionVisitor extends IdentityVisitor<Void> {

	private Random random;

	private static final double RANDOM_THRESHOLD = 0.5;

	private List<IName> expectedProposals;

	private boolean generatedRandomHole;

	public RandomHoleInsertionVisitor() {
		random = new Random();
		expectedProposals = new ArrayList<>();
	}

	@Override
	public ISSTNode visit(IMethodDeclaration stmt, Void context) {
		if (stmt.getName().isConstructor()) {
			return stmt;
		}
		return super.visit(stmt, context);
	}

	@Override
	public ISSTNode visit(IAssignment stmt, Void context) {
		if (generatedRandomHole) {
			return super.visit(stmt, context);
		}
		if (stmt.getExpression() instanceof IInvocationExpression && isRandomlySelectedInvocation()) {
			Assignment assignmentImpl = (Assignment) stmt;
			assignmentImpl.setExpression(getTransformedExpression((IInvocationExpression) stmt.getExpression()));
		}

		return super.visit(stmt, context);
	}

	private boolean isRandomlySelectedInvocation() {
		double randomNumber = random.nextDouble();
		return randomNumber > RANDOM_THRESHOLD;
	}

	private IAssignableExpression getTransformedExpression(IInvocationExpression invocation) {
		if (isValidInvocation(invocation)) {
			ICompletionExpression completionExpression = transformInvocation(invocation);
			expectedProposals.add(invocation.getMethodName());
			generatedRandomHole = true;
			return completionExpression;
		}
		return invocation;
	}

	private boolean isValidInvocation(IInvocationExpression invocation) {
		return !invocation.getReference().isMissing() 
				&& !invocation.getReference().getIdentifier().equals("this")
				&& !invocation.getMethodName().isStatic();
	}

	private ICompletionExpression transformInvocation(IInvocationExpression invocation) {
		CompletionExpression completionExpression = new CompletionExpression();
		completionExpression.setObjectReference(invocation.getReference());
		return completionExpression;
	}

	public List<IName> getExpectedProposals() {
		return expectedProposals;
	}

	public boolean hasGeneratedRandomHole() {
		return generatedRandomHole;
	}
}
