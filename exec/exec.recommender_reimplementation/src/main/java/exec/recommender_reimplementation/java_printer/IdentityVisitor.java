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
package exec.recommender_reimplementation.java_printer;

import cc.kave.commons.model.ssts.ISST;
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
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
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
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class IdentityVisitor<TContext> extends AbstractThrowingNodeVisitor<TContext,ISSTNode> {
	
	@Override
	public ISSTNode visit(ISST sst, TContext context) {
		return sst;
	}

	@Override
	public ISSTNode visit(IDelegateDeclaration stmt, TContext context) {
		return stmt;
	}

	@Override
	public ISSTNode visit(IEventDeclaration stmt, TContext context) {
		return stmt;
	}

	@Override
	public ISSTNode visit(IFieldDeclaration stmt, TContext context) {
		return stmt;
	}

	@Override
	public ISSTNode visit(IMethodDeclaration stmt, TContext context) {
		return stmt;
	}

	@Override
	public ISSTNode visit(IPropertyDeclaration stmt, TContext context) {
		return stmt;
	}

	@Override
	public ISSTNode visit(IVariableDeclaration stmt, TContext context) {
		return stmt;
	}

	@Override
	public ISSTNode visit(IAssignment stmt, TContext context) {
		return stmt;
	}

	@Override
	public ISSTNode visit(IBreakStatement stmt, TContext context) {
		return stmt;	
	}

	@Override
	public ISSTNode visit(IContinueStatement stmt, TContext context) {
		return stmt;
	}

	@Override
	public ISSTNode visit(IExpressionStatement stmt, TContext context) {
		return stmt;
	}

	@Override
	public ISSTNode visit(IGotoStatement stmt, TContext context) {
		return stmt;
	}

	@Override
	public ISSTNode visit(ILabelledStatement stmt, TContext context) {
		return stmt;
	}

	@Override
	public ISSTNode visit(IReturnStatement stmt, TContext context) {
		return stmt;
	}

	@Override
	public ISSTNode visit(IThrowStatement stmt, TContext context) {
		return stmt;
	}

	@Override
	public ISSTNode visit(IEventSubscriptionStatement stmt, TContext context) {
		return stmt;
	}

	@Override
	public ISSTNode visit(IDoLoop block, TContext context) {
		return block;
	}

	@Override
	public ISSTNode visit(IForEachLoop block, TContext context) {
		return block;
	}

	@Override
	public ISSTNode visit(IForLoop block, TContext context) {
		return block;
	}

	@Override
	public ISSTNode visit(IIfElseBlock block, TContext context) {
		return block;
	}

	@Override
	public ISSTNode visit(ILockBlock block, TContext context) {
		return block;
	}

	@Override
	public ISSTNode visit(ISwitchBlock block, TContext context) {
		return block;
	}

	@Override
	public ISSTNode visit(ITryBlock block, TContext context) {
		return block;
	}

	@Override
	public ISSTNode visit(IUncheckedBlock block, TContext context) {
		return block;
	}

	@Override
	public ISSTNode visit(IUnsafeBlock block, TContext context) {
		return block;
	}

	@Override
	public ISSTNode visit(IUsingBlock block, TContext context) {
		return block;
	}

	@Override
	public ISSTNode visit(IWhileLoop block, TContext context) {
		return block;
	}

	@Override
	public ISSTNode visit(ICompletionExpression entity, TContext context) {
		return entity;
	}

	@Override
	public ISSTNode visit(IComposedExpression expr, TContext context) {
		return expr;
	}

	@Override
	public ISSTNode visit(IIfElseExpression expr, TContext context) {
		return expr;
	}

	@Override
	public ISSTNode visit(IInvocationExpression entity, TContext context) {
		return entity;
	}

	@Override
	public ISSTNode visit(ILambdaExpression expr, TContext context) {
		return expr;
	}

	@Override
	public ISSTNode visit(ILoopHeaderBlockExpression expr, TContext context) {
		return expr;
	}

	@Override
	public ISSTNode visit(IConstantValueExpression expr, TContext context) {
		return expr;
	}

	@Override
	public ISSTNode visit(INullExpression expr, TContext context) {
		return expr;
	}

	@Override
	public ISSTNode visit(IReferenceExpression expr, TContext context) {
		return expr;
	}

	@Override
	public ISSTNode visit(IEventReference eventRef, TContext context) {
		return eventRef;
	}

	@Override
	public ISSTNode visit(IFieldReference fieldRef, TContext context) {
		return fieldRef;
	}

	@Override
	public ISSTNode visit(IMethodReference methodRef, TContext context) {
		return methodRef;
	}

	@Override
	public ISSTNode visit(IPropertyReference propertyRef, TContext context) {
		return propertyRef;
	}

	@Override
	public ISSTNode visit(IVariableReference varRef, TContext context) {
		return varRef;
	}

	@Override
	public ISSTNode visit(IUnknownReference unknownRef, TContext context) {
		return unknownRef;
	}

	@Override
	public ISSTNode visit(IUnknownExpression unknownExpr, TContext context) {
		return unknownExpr;
	}

	@Override
	public ISSTNode visit(IUnknownStatement unknownStmt, TContext context) {
		return unknownStmt;
	}

	@Override
	public ISSTNode visit(ICastExpression expr, TContext context) {
		return expr;
	}

	@Override
	public ISSTNode visit(IIndexAccessExpression expr, TContext context) {
		return expr;
	}

	@Override
	public ISSTNode visit(ITypeCheckExpression expr, TContext context) {
		return expr;
	}

	@Override
	public ISSTNode visit(IBinaryExpression expr, TContext context) {
		return expr;
	}

	@Override
	public ISSTNode visit(IUnaryExpression expr, TContext context) {
		return expr;
	}

	@Override
	public ISSTNode visit(IIndexAccessReference indexAccessRef, TContext context) {
		return indexAccessRef;
	}
}
