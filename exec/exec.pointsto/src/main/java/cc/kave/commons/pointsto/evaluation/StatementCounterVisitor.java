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
package cc.kave.commons.pointsto.evaluation;

import java.util.stream.Stream;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ITypeCheckExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
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
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class StatementCounterVisitor implements ISSTNodeVisitor<Void, Integer> {

	private int visitStatements(Iterable<IStatement> stmts) {
		int accu = 0;
		for (IStatement stmt : stmts) {
			accu += stmt.accept(this, null);
		}
		return accu;
	}

	@Override
	public Integer visit(ISST sst, Void context) {
		Stream<IStatement> methodStmts = sst.getMethods().stream().flatMap(decl -> decl.getBody().stream());
		Stream<IStatement> propertyStmts = sst.getProperties().stream()
				.flatMap(decl -> Stream.concat(decl.getGet().stream(), decl.getSet().stream()));
		return Stream.concat(methodStmts, propertyStmts).parallel().mapToInt(stmt -> stmt.accept(this, null)).sum();
	}

	@Override
	public Integer visit(IDelegateDeclaration stmt, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IEventDeclaration stmt, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IFieldDeclaration stmt, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IMethodDeclaration stmt, Void context) {
		return visitStatements(stmt.getBody());
	}

	@Override
	public Integer visit(IPropertyDeclaration stmt, Void context) {
		return visitStatements(stmt.getGet()) + visitStatements(stmt.getSet());
	}

	@Override
	public Integer visit(IVariableDeclaration stmt, Void context) {
		return 1;
	}

	@Override
	public Integer visit(IAssignment stmt, Void context) {
		return 1;
	}

	@Override
	public Integer visit(IBreakStatement stmt, Void context) {
		return 1;
	}

	@Override
	public Integer visit(IContinueStatement stmt, Void context) {
		return 1;
	}

	@Override
	public Integer visit(IExpressionStatement stmt, Void context) {
		return 1;
	}

	@Override
	public Integer visit(IGotoStatement stmt, Void context) {
		return 1;
	}

	@Override
	public Integer visit(ILabelledStatement stmt, Void context) {
		return 1;
	}

	@Override
	public Integer visit(IReturnStatement stmt, Void context) {
		return 1;
	}

	@Override
	public Integer visit(IThrowStatement stmt, Void context) {
		return 1;
	}

	@Override
	public Integer visit(IEventSubscriptionStatement stmt, Void context) {
		return 1;
	}

	@Override
	public Integer visit(IDoLoop block, Void context) {
		return 1 + visitStatements(block.getBody());
	}

	@Override
	public Integer visit(IForEachLoop block, Void context) {
		return 1 + visitStatements(block.getBody());
	}

	@Override
	public Integer visit(IForLoop block, Void context) {
		return 1 + visitStatements(block.getInit()) + visitStatements(block.getStep())
				+ visitStatements(block.getBody());
	}

	@Override
	public Integer visit(IIfElseBlock block, Void context) {
		return 1 + visitStatements(block.getThen()) + visitStatements(block.getElse());
	}

	@Override
	public Integer visit(ILockBlock block, Void context) {
		return 1 + visitStatements(block.getBody());
	}

	@Override
	public Integer visit(ISwitchBlock block, Void context) {
		int accu = 0;
		for (ICaseBlock caseBlock : block.getSections()) {
			accu += visitStatements(caseBlock.getBody());
		}
		return 1 + accu + visitStatements(block.getDefaultSection());
	}

	@Override
	public Integer visit(ITryBlock block, Void context) {
		int accu = 0;
		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			accu += visitStatements(catchBlock.getBody());
		}
		return 1 + accu + visitStatements(block.getFinally()) + visitStatements(block.getBody());
	}

	@Override
	public Integer visit(IUncheckedBlock block, Void context) {
		return 1 + visitStatements(block.getBody());
	}

	@Override
	public Integer visit(IUnsafeBlock block, Void context) {
		return 1;
	}

	@Override
	public Integer visit(IUsingBlock block, Void context) {
		return 1 + visitStatements(block.getBody());
	}

	@Override
	public Integer visit(IWhileLoop block, Void context) {
		return 1 + visitStatements(block.getBody());
	}

	@Override
	public Integer visit(IBinaryExpression expr, Void context) {
		return 0;
	}

	@Override
	public Integer visit(ICastExpression expr, Void context) {
		return 0;
	}

	@Override
	public Integer visit(ICompletionExpression expr, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IComposedExpression expr, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IIfElseExpression expr, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IIndexAccessExpression expr, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IInvocationExpression entity, Void context) {
		return 0;
	}

	@Override
	public Integer visit(ILambdaExpression expr, Void context) {
		return 0;
	}

	@Override
	public Integer visit(ITypeCheckExpression expr, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IUnaryExpression expr, Void context) {
		return 0;
	}

	@Override
	public Integer visit(ILoopHeaderBlockExpression expr, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IConstantValueExpression expr, Void context) {
		return 0;
	}

	@Override
	public Integer visit(INullExpression expr, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IReferenceExpression expr, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IEventReference ref, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IFieldReference ref, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IIndexAccessReference ref, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IMethodReference ref, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IPropertyReference ref, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IVariableReference ref, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IUnknownReference ref, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IUnknownExpression expr, Void context) {
		return 0;
	}

	@Override
	public Integer visit(IUnknownStatement stmt, Void context) {
		return 1;
	}

}
