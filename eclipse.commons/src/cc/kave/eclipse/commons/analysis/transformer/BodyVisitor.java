/**
 * Copyright 2015 Waldemar Graf
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

package cc.kave.eclipse.commons.analysis.transformer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.eclipse.commons.analysis.util.UniqueVariableNameGenerator;
import cc.kave.eclipse.namefactory.NodeFactory;
import cc.kave.eclipse.namefactory.NodeFactory.BindingFactory;

public class BodyVisitor extends ASTVisitor {

	private ExpressionVisitor exprVisitor;
	private UniqueVariableNameGenerator nameGen;
	private List<IStatement> body;

	public BodyVisitor(UniqueVariableNameGenerator nameGen, List<IStatement> body) {
		this.nameGen = nameGen;
		this.body = body;
		exprVisitor = new ExpressionVisitor(nameGen, body);
	}

	@Override
	public boolean visit(BreakStatement stmt) {
		body.add(new cc.kave.commons.model.ssts.impl.statements.BreakStatement());
		return false;
	}

	@Override
	public boolean visit(ContinueStatement stmt) {
		body.add(new cc.kave.commons.model.ssts.impl.statements.ContinueStatement());
		return false;
	}

	@Override
	public boolean visit(ReturnStatement stmt) {
		cc.kave.commons.model.ssts.impl.statements.ReturnStatement returnStmt = new cc.kave.commons.model.ssts.impl.statements.ReturnStatement();

		if (stmt.getExpression() == null) {
			returnStmt.setIsVoid(true);
			body.add(returnStmt);
		} else {
			stmt.getExpression().accept(exprVisitor);
			returnStmt.setExpression(exprVisitor.getSimpleExpression());
			body.add(returnStmt);
		}
		return false;
	}

	// @Override
	// public boolean visit(EmptyStatement stmt) {
	// body.add(getEmptyCompletionExpression());
	// return false;
	// }

	@Override
	public boolean visit(WhileStatement stmt) {
		WhileLoop loop = new WhileLoop();
		LoopHeaderBlockExpression condition = new LoopHeaderBlockExpression();
		ExpressionVisitor conditionVisitor = new ExpressionVisitor(nameGen, condition.getBody());

		stmt.getExpression().accept(conditionVisitor);
		loop.setCondition(conditionVisitor.getSimpleExpression());
		
		if(loop.getCondition() == null){
			loop.setCondition(condition);
		}

		body.add(loop);

		BodyVisitor visitor = new BodyVisitor(nameGen, loop.getBody());
		stmt.getBody().accept(visitor);

		return false;
	}

	@Override
	public boolean visit(IfStatement stmt) {
		IfElseBlock ifElseBlock = new IfElseBlock();

		stmt.getExpression().accept(exprVisitor);
		ifElseBlock.setCondition(exprVisitor.getSimpleExpression());

		if (stmt.getThenStatement() != null) {
			BodyVisitor visitor = new BodyVisitor(nameGen, ifElseBlock.getThen());
			stmt.getThenStatement().accept(visitor);
		}

		if (stmt.getElseStatement() != null) {
			BodyVisitor visitor = new BodyVisitor(nameGen, ifElseBlock.getElse());
			stmt.getElseStatement().accept(visitor);
		}

		body.add(ifElseBlock);
		return false;
	}

	@Override
	public boolean visit(ForStatement stmt) {

		ForLoop loop = new ForLoop();

		List<Expression> initializers = stmt.initializers();

		for (int i = 0; i < initializers.size(); i++) {
			ExpressionVisitor initVisitor = new ExpressionVisitor(nameGen, loop.getInit());
			initializers.get(i).accept(initVisitor);
		}

		if (stmt.getExpression() != null) {
			ExpressionVisitor conditionVisitor = new ExpressionVisitor(nameGen, body);
			stmt.getExpression().accept(conditionVisitor);
			loop.setCondition(conditionVisitor.getSimpleExpression());
		}

		List<Expression> updaters = stmt.updaters();

		for (int i = 0; i < updaters.size(); i++) {
			ExpressionVisitor updVisitor = new ExpressionVisitor(nameGen, loop.getStep());
			updaters.get(i).accept(updVisitor);
		}

		BodyVisitor visitor = new BodyVisitor(nameGen, loop.getBody());
		stmt.getBody().accept(visitor);

		body.add(loop);
		return false;
	}

	@Override
	public boolean visit(EnhancedForStatement stmt) {
		ForEachLoop loop = new ForEachLoop();

		String variableIdentifier = stmt.getParameter().getName().getIdentifier();
		TypeName type = BindingFactory.getTypeName(stmt.getParameter().getType().resolveBinding());

		VariableDeclaration decl = new VariableDeclaration();
		VariableReference ref = new VariableReference();

		ref.setIdentifier(variableIdentifier);
		decl.setReference(ref);
		decl.setType(type);
		stmt.getParameter();
		loop.setDeclaration(decl);

		stmt.getExpression().accept(exprVisitor);
		loop.setLoopedReference(exprVisitor.getVariableReference());

		BodyVisitor visitor = new BodyVisitor(nameGen, loop.getBody());
		stmt.getBody().accept(visitor);

		body.add(loop);
		return false;
	}

	@Override
	public boolean visit(VariableDeclarationStatement stmt) {
		for (int i = 0; i < stmt.fragments().size(); i++) {
			VariableDeclarationFragment fragment = (VariableDeclarationFragment) stmt.fragments().get(i);
			VariableDeclaration variableDeclaration = new VariableDeclaration();
			VariableReference variableReference = new VariableReference();

			variableReference.setIdentifier(fragment.getName().getIdentifier());
			variableDeclaration.setReference(variableReference);
			variableDeclaration.setType(NodeFactory.getBindingName(stmt.getType().resolveBinding()));

			body.add(variableDeclaration);

			if (fragment.getInitializer() != null) {
				cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();
				assignment.setReference(variableReference);
				fragment.getInitializer().accept(exprVisitor);
				assignment.setExpression(exprVisitor.getAssignableExpression());
				body.add(assignment);
			}
		}

		return false;
	}

	@Override
	public boolean visit(org.eclipse.jdt.core.dom.ExpressionStatement node) {
		node.accept(exprVisitor);
		return false;
	}

	public static ExpressionStatement getEmptyCompletionExpression() {
		ExpressionStatement expressionStatement = new ExpressionStatement();
		expressionStatement.setExpression(new CompletionExpression());
		return expressionStatement;
	}
}
