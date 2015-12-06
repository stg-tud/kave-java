/**
 * Copyright 2015 Simon Reuß
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
package cc.kave.commons.pointsto.analysis;

import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class TypeAliasedVisitor extends TraversingVisitor<TypeAliasedVisitorContext, Void> {

	@Override
	public Void visit(IMethodDeclaration stmt, TypeAliasedVisitorContext context) {
		context.enterMethod(stmt);
		super.visit(stmt, context);
		context.leaveMethod();

		return null;
	}

	@Override
	public Void visit(ITryBlock block, TypeAliasedVisitorContext context) {
		context.enterScope();
		visitStatements(block.getBody(), context);
		context.leaveScope();

		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			context.enterScope();
			context.declareParameter(catchBlock.getParameter());
			visitStatements(catchBlock.getBody(), context);
			context.leaveScope();
		}

		context.enterScope();
		visitStatements(block.getFinally(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, TypeAliasedVisitorContext context) {
		block.getReference().accept(this, context);

		for (ICaseBlock caseBlock : block.getSections()) {
			context.enterScope();
			caseBlock.getLabel().accept(this, context);
			visitStatements(caseBlock.getBody(), context);
			context.leaveScope();
		}

		context.enterScope();
		visitStatements(block.getDefaultSection(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, TypeAliasedVisitorContext context) {
		block.getCondition().accept(this, context);

		context.enterScope();
		visitStatements(block.getThen(), context);
		context.leaveScope();

		context.enterScope();
		visitStatements(block.getElse(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public Void visit(IForLoop block, TypeAliasedVisitorContext context) {
		context.enterScope();
		visitStatements(block.getInit(), context);
		block.getCondition().accept(this, context);
		visitStatements(block.getBody(), context);
		visitStatements(block.getStep(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public Void visit(IForEachLoop block, TypeAliasedVisitorContext context) {
		context.enterScope();
		block.getLoopedReference().accept(this, context);
		block.getDeclaration().accept(this, context);
		visitStatements(block.getBody(), context);
		context.leaveScope();

		return null;
	}

	@Override
	public Void visit(IUsingBlock block, TypeAliasedVisitorContext context) {
		context.enterScope();
		block.getReference().accept(this, context);
		visitStatements(block.getBody(), context);
		context.leaveScope();

		return null;
	}
	
	@Override
	public Void visit(IDoLoop block, TypeAliasedVisitorContext context) {
		context.enterScope();
		visitStatements(block.getBody(), context);
		context.leaveScope();
		block.getCondition().accept(this, context);

		return null;
	}
	
	@Override
	public Void visit(IWhileLoop block, TypeAliasedVisitorContext context) {
		block.getCondition().accept(this, context);
		context.enterScope();
		visitStatements(block.getBody(), context);
		context.leaveScope();
		
		return null;
	}
	
	@Override
	public Void visit(IUncheckedBlock block, TypeAliasedVisitorContext context) {
		context.enterScope();
		visitStatements(block.getBody(), context);
		context.leaveScope();
		
		return null;
	}

	@Override
	public Void visit(IVariableDeclaration stmt, TypeAliasedVisitorContext context) {
		context.declareVariable(stmt);
		return null;
	}

	@Override
	public Void visit(IFieldReference fieldRef, TypeAliasedVisitorContext context) {
		context.useFieldReference(fieldRef);
		return null;
	}

	@Override
	public Void visit(IVariableReference varRef, TypeAliasedVisitorContext context) {
		context.useVariableReference(varRef);
		return null;
	}

}
