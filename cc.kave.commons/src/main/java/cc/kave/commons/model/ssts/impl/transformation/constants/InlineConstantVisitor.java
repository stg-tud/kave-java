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
package cc.kave.commons.model.ssts.impl.transformation.constants;

import java.util.ArrayList;
import java.util.List;

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
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
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

public class InlineConstantVisitor extends AbstractThrowingNodeVisitor<IInlineConstantContext, Void> {

	@Override
	public Void visit(ISST sst, IInlineConstantContext context) {
		context.collectConstants(sst);
		for (IPropertyDeclaration property : sst.getProperties()) {
			property.accept(this, context);
		}
		for (IMethodDeclaration method : sst.getMethods()) {
			method.accept(this, context);
		}
		return null;
	}

	@Override
	public Void visit(IFieldDeclaration stmt, IInlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IAssignment stmt, IInlineConstantContext context) {
		stmt.getExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IMethodDeclaration stmt, IInlineConstantContext context) {
		this.visit(stmt.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IPropertyDeclaration stmt, IInlineConstantContext context) {
		this.visit(stmt.getGet(), context);
		this.visit(stmt.getSet(), context);
		return null;
	}

	@Override
	public Void visit(IVariableDeclaration stmt, IInlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IBreakStatement stmt, IInlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IContinueStatement stmt, IInlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IExpressionStatement stmt, IInlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IGotoStatement stmt, IInlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(ILabelledStatement stmt, IInlineConstantContext context) {
		stmt.getStatement().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IReturnStatement stmt, IInlineConstantContext context) {
		ISimpleExpression expr = stmt.getExpression();
		if (context.isConstant(expr)) {
			if (stmt instanceof ReturnStatement) {
				((ReturnStatement) stmt).setExpression(new ConstantValueExpression());
			}
		}
		return null;
	}

	@Override
	public Void visit(IThrowStatement stmt, IInlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IDoLoop block, IInlineConstantContext context) {
		block.getCondition().accept(this, context);
		this.visit(block.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, IInlineConstantContext context) {
		this.visit(block.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IForLoop block, IInlineConstantContext context) {
		block.getCondition().accept(this, context);
		this.visit(block.getInit(), context);
		this.visit(block.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, IInlineConstantContext context) {
		block.getCondition().accept(this, context);
		this.visit(block.getThen(), context);
		this.visit(block.getElse(), context);
		return null;
	}

	@Override
	public Void visit(ILockBlock stmt, IInlineConstantContext context) {
		this.visit(stmt.getBody(), context);
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, IInlineConstantContext context) {
		this.visit(block.getDefaultSection(), context);
		for (ICaseBlock caseBlock : block.getSections()) {
			this.visit(caseBlock.getBody(), context);
		}
		return null;
	}

	@Override
	public Void visit(ITryBlock block, IInlineConstantContext context) {
		this.visit(block.getBody(), context);
		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			this.visit(catchBlock.getBody(), context);
		}
		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, IInlineConstantContext context) {
		this.visit(block.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, IInlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IUsingBlock block, IInlineConstantContext context) {
		this.visit(block.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, IInlineConstantContext context) {
		block.getCondition().accept(this, context);
		this.visit(block.getBody(), context);
		return null;
	}

	@Override
	public Void visit(ICompletionExpression entity, IInlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IComposedExpression expr, IInlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IIfElseExpression expr, IInlineConstantContext context) {
		expr.getCondition().accept(this, context);
		expr.getThenExpression().accept(this, context);
		expr.getElseExpression().accept(this, context);
		return null;
	}

	@Override
	public Void visit(IInvocationExpression entity, IInlineConstantContext context) {
		List<ISimpleExpression> parameters = new ArrayList<ISimpleExpression>();
		for (ISimpleExpression expr : entity.getParameters()) {
			if (context.isConstant(expr)) {
				parameters.add(new ConstantValueExpression());
			} else {
				parameters.add(expr);
			}
		}
		if (entity instanceof InvocationExpression) {
			((InvocationExpression) entity).setParameters(parameters);
		}
		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, IInlineConstantContext context) {
		this.visit(expr.getBody(), context);
		return null;
	}

	@Override
	public Void visit(ILoopHeaderBlockExpression expr, IInlineConstantContext context) {
		this.visit(expr.getBody(), context);
		return null;
	}

	@Override
	public Void visit(IUnknownExpression unknownExpr, IInlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IUnknownStatement unknownStmt, IInlineConstantContext context) {
		return null;
	}

	@Override
	public Void visit(IEventSubscriptionStatement stmt, IInlineConstantContext context) {
		stmt.getExpression().accept(this, context);
		return null;
	}
	
	/**
	 * Helper method to visit list of statements.
	 */
	public Void visit(List<IStatement> stmts, IInlineConstantContext context) {
		for (IStatement s : stmts) {
			s.accept(this, context);
		}
		return null;
	}
}
