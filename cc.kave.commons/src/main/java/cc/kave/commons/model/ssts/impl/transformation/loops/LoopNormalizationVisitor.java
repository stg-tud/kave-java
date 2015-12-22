/*
 * Copyright 2015 Carina Oberle
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
package cc.kave.commons.model.ssts.impl.transformation.loops;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.constant;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareVar;
import static cc.kave.commons.model.ssts.impl.SSTUtil.expr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.loopHeader;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
<<<<<<< Upstream, based on origin/master
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
=======
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
>>>>>>> c88ec0c Cleanup
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.transformation.AbstractStatementNormalizationVisitor;
import cc.kave.commons.model.ssts.references.IVariableReference;
<<<<<<< Upstream, based on origin/master
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
=======
>>>>>>> c88ec0c Cleanup

public class LoopNormalizationVisitor extends AbstractStatementNormalizationVisitor<Void> {

	private StatementInsertionVisitor insertionVisitor;
	
	public LoopNormalizationVisitor() {
		this.insertionVisitor = new StatementInsertionVisitor();
	}

	@Override
	public List<IStatement> visit(IDoLoop block, Void context) {
		// normalize inner loops
		super.visit(block, context);

		// create condition
		IConstantValueExpression condition = constant("true");

		IfElseBlock ifBlock = new IfElseBlock();
		// TODO insert negated loop condition
		// (break loop when condition evaluates to
		// false)
		// ifBlock.setCondition();
		ifBlock.setThen(Lists.newArrayList(new BreakStatement()));

		// assemble while loop
		List<IStatement> whileBody = new ArrayList<IStatement>();
		whileBody.addAll(block.getBody());
		whileBody.add(ifBlock);

		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setBody(whileBody);
		whileLoop.setCondition(condition);

		List<IStatement> normalized = new ArrayList<IStatement>();
		normalized.add(whileLoop);
		return normalized;
	}

	@Override
	public List<IStatement> visit(IForEachLoop block, Void context) {
		// normalize inner loops
		super.visit(block, context);

		IVariableReference loopedReference = block.getLoopedReference();
		IVariableDeclaration variableDeclaration = block.getDeclaration();
		String variableName = variableDeclaration.getReference().getIdentifier();
		ITypeName variableType = variableDeclaration.getType();

		// TODO distinction between Java / C#
		// declare iterator
		ITypeName iteratorTypeName = TypeName
				.newTypeName("java.util.Iterator`1[[T -> " + variableType.getIdentifier() + "]], jre, 1.6");
		IVariableDeclaration iteratorDecl = declareVar("iterator", iteratorTypeName);

		// initialize iterator
		InvocationExpression invokeIterator = new InvocationExpression();
		invokeIterator.setMethodName(MethodName.newMethodName("iterator"));
		invokeIterator.setReference(loopedReference);
		IStatement iteratorInit = assign(iteratorDecl.getReference(), invokeIterator);

		// create condition
		InvocationExpression invokeHasNext = new InvocationExpression();
		invokeHasNext.setMethodName(MethodName.newMethodName("hasNext"));
		invokeHasNext.setReference(iteratorDecl.getReference());
		ILoopHeaderBlockExpression condition = loopHeader(expr(invokeHasNext));

		// declare element
		IVariableDeclaration elemDecl = declareVar(variableName, variableType);

		// assign next element
		InvocationExpression invokeNext = new InvocationExpression();
		invokeNext.setMethodName(MethodName.newMethodName("next"));
		invokeNext.setReference(iteratorDecl.getReference());
		IStatement elemAssign = assign(elemDecl.getReference(), invokeNext);

		// assemble while loop
		List<IStatement> whileBody = new ArrayList<IStatement>();
		whileBody.add(elemAssign);
		whileBody.addAll(block.getBody());
		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setBody(whileBody);
		whileLoop.setCondition(condition);

		List<IStatement> normalized = new ArrayList<IStatement>();
		normalized.add(iteratorDecl);
		normalized.add(iteratorInit);
		normalized.add(elemDecl);
		normalized.add(whileLoop);
		return normalized;
	}

	@Override
	public List<IStatement> visit(IForLoop block, Void context) {
		// normalize inner loops
		super.visit(block, context);
		
		ILoopHeaderExpression condition = block.getCondition();
		// TODO: limit scope of 'init' part?

		// handle 'continue' inside loop body
		List<IStatement> whileBody = replicateLoopStep(block.getBody(), block.getStep());
		whileBody.addAll(block.getStep());
		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setBody(whileBody);
		whileLoop.setCondition(condition);

		List<IStatement> normalized = new ArrayList<IStatement>();
		normalized.addAll(block.getInit());
		normalized.add(whileLoop);
		return normalized;
	}
	
	/**
	 * Place loop step before continue statements.
	 */
	public List<IStatement> replicateLoopStep(List<IStatement> statements, List<IStatement> loopStep) {
		return insertionVisitor.visit(statements, new StepInsertionContext(loopStep));
	}
}
