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
package cc.kave.commons.pointsto.analysis.methods;

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
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
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
import cc.kave.commons.pointsto.analysis.visitors.TraversingVisitor;

public class EnclosingMethodVisitor extends TraversingVisitor<EnclosingMethodVisitorContext, Void> {

	@Override
	public Void visit(IMethodDeclaration stmt, EnclosingMethodVisitorContext context) {
		context.setCurrentMethod(stmt.getName());
		return super.visit(stmt, context);
	}

	@Override
	public Void visit(IPropertyDeclaration stmt, EnclosingMethodVisitorContext context) {
		// statements inside the get or set section of a property have no enclosing method
		context.setCurrentMethod(null);
		return null;
	}

	@Override
	public Void visit(IAssignment stmt, EnclosingMethodVisitorContext context) {
		context.registerStatement(stmt);
		return null;
	}

	@Override
	public Void visit(IBreakStatement stmt, EnclosingMethodVisitorContext context) {
		context.registerStatement(stmt);
		return null;
	}

	@Override
	public Void visit(IContinueStatement stmt, EnclosingMethodVisitorContext context) {
		context.registerStatement(stmt);
		return null;
	}

	@Override
	public Void visit(IDoLoop block, EnclosingMethodVisitorContext context) {
		context.registerStatement(block);
		return super.visit(block, context);
	}

	@Override
	public Void visit(IEventSubscriptionStatement stmt, EnclosingMethodVisitorContext context) {
		context.registerStatement(stmt);
		return null;
	}

	@Override
	public Void visit(IExpressionStatement stmt, EnclosingMethodVisitorContext context) {
		context.registerStatement(stmt);
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, EnclosingMethodVisitorContext context) {
		context.registerStatement(block);
		return super.visit(block, context);
	}

	@Override
	public Void visit(IForLoop block, EnclosingMethodVisitorContext context) {
		context.registerStatement(block);
		return super.visit(block, context);
	}

	@Override
	public Void visit(IGotoStatement stmt, EnclosingMethodVisitorContext context) {
		context.registerStatement(stmt);
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, EnclosingMethodVisitorContext context) {
		context.registerStatement(block);
		return super.visit(block, context);
	}

	@Override
	public Void visit(ILabelledStatement stmt, EnclosingMethodVisitorContext context) {
		context.registerStatement(stmt);
		return super.visit(stmt, context);
	}

	@Override
	public Void visit(ILockBlock block, EnclosingMethodVisitorContext context) {
		context.registerStatement(block);
		return super.visit(block, context);
	}

	@Override
	public Void visit(IReturnStatement stmt, EnclosingMethodVisitorContext context) {
		context.registerStatement(stmt);
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, EnclosingMethodVisitorContext context) {
		context.registerStatement(block);
		return super.visit(block, context);
	}

	@Override
	public Void visit(IThrowStatement stmt, EnclosingMethodVisitorContext context) {
		context.registerStatement(stmt);
		return null;
	}

	@Override
	public Void visit(ITryBlock block, EnclosingMethodVisitorContext context) {
		context.registerStatement(block);
		return super.visit(block, context);
	}

	@Override
	public Void visit(IUncheckedBlock block, EnclosingMethodVisitorContext context) {
		context.registerStatement(block);
		return super.visit(block, context);
	}

	@Override
	public Void visit(IUnknownStatement unknownStmt, EnclosingMethodVisitorContext context) {
		context.registerStatement(unknownStmt);
		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, EnclosingMethodVisitorContext context) {
		context.registerStatement(block);
		return super.visit(block, context);
	}

	@Override
	public Void visit(IUsingBlock block, EnclosingMethodVisitorContext context) {
		context.registerStatement(block);
		return super.visit(block, context);
	}

	@Override
	public Void visit(IVariableDeclaration stmt, EnclosingMethodVisitorContext context) {
		context.registerStatement(stmt);
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, EnclosingMethodVisitorContext context) {
		context.registerStatement(block);
		return super.visit(block, context);
	}
}
