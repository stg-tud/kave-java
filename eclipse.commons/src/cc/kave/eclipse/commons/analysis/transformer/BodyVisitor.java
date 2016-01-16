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

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.eclipse.commons.analysis.completiontarget.CompletionTargetMarker;
import cc.kave.eclipse.commons.analysis.completiontarget.CompletionTargetMarker.CompletionCase;
import cc.kave.eclipse.commons.analysis.util.UniqueVariableNameGenerator;
import cc.kave.eclipse.namefactory.NodeFactory;
import cc.kave.eclipse.namefactory.NodeFactory.BindingFactory;

public class BodyVisitor extends ASTVisitor{

	private ExpressionVisitor exprVisitor;
	private UniqueVariableNameGenerator nameGen;
	private List<IStatement> body;

	public BodyVisitor(UniqueVariableNameGenerator nameGen) {
		this.nameGen = nameGen;
		exprVisitor = new ExpressionVisitor(nameGen);
	}

	public void visitStatement(ASTNode stmt, List<IStatement> body) {

		switch (stmt.getNodeType()) {
		case ASTNode.BLOCK:
			visit((Block) stmt, body);
			break;
		case ASTNode.BREAK_STATEMENT:
			visit((BreakStatement) stmt, body);
			break;
		case ASTNode.WHILE_STATEMENT:
			visit((WhileStatement) stmt, body);
			break;
		case ASTNode.EMPTY_STATEMENT:
			visit((EmptyStatement) stmt, body);
			break;
		case ASTNode.RETURN_STATEMENT:
			visit((ReturnStatement) stmt, body);
			break;
		case ASTNode.IF_STATEMENT:
			visit((IfStatement) stmt, body);
			break;
		case ASTNode.FOR_STATEMENT:
			visit((ForStatement) stmt, body);
			break;
		default:
			break;
		}

	}

	public void visit(Block stmt, List<IStatement> body) {
		List<Statement> statements = stmt.statements();

		for (Statement statement : statements) {
			visitStatement(statement, body);
		}

	}

	public void visit(BreakStatement stmt, List<IStatement> body) {
		body.add(new cc.kave.commons.model.ssts.impl.statements.BreakStatement());
	}

	public void visit(ReturnStatement stmt, List<IStatement> body) {
		cc.kave.commons.model.ssts.impl.statements.ReturnStatement returnStmt = new cc.kave.commons.model.ssts.impl.statements.ReturnStatement();

		if (stmt.getExpression() == null) {
			returnStmt.setIsVoid(true);
			body.add(returnStmt);
		} else {
			ISimpleExpression expression = exprVisitor.createSimpleExpression(
					stmt.getExpression(), body);

			returnStmt.setExpression(expression);
			body.add(returnStmt);
		}
	}

	public void visit(EmptyStatement stmt, List<IStatement> body) {
		body.add(getEmptyCompletionExpression());
	}

	public void visit(WhileStatement stmt, List<IStatement> body) {
		WhileLoop loop = new WhileLoop();
		ISimpleExpression condition = exprVisitor.createSimpleExpression(
				stmt.getExpression(), body);
		loop.setCondition(condition);
		body.add(loop);

		visitStatement(stmt.getBody(), loop.getBody());
	}

	public void visit(IfStatement stmt, List<IStatement> body) {
		IfElseBlock ifElseBlock = new IfElseBlock();

		ISimpleExpression condition = exprVisitor.createSimpleExpression(
				stmt.getExpression(), body);
		ifElseBlock.setCondition(condition);

		if (stmt.getThenStatement() != null) {
			visitStatement(stmt.getThenStatement(), ifElseBlock.getThen());
		}

		if (stmt.getElseStatement() != null) {
			visitStatement(stmt.getElseStatement(), ifElseBlock.getElse());
		}

		body.add(ifElseBlock);
	}

	public void visit(ForStatement stmt, List<IStatement> body) {

		ForLoop loop = new ForLoop();

		List<Expression> initializers = stmt.initializers();
		List<ISimpleExpression> inits = new ArrayList<>();

		for (int i = 0; i < initializers.size(); i++) {
			// body oder welche liste?
			inits.add(exprVisitor.createSimpleExpression(initializers.get(i), body));
		}

		if (stmt.getExpression() != null) {
			loop.setCondition(exprVisitor.createSimpleExpression(stmt.getExpression(),
					body));
		}

		List<Expression> updaters = stmt.updaters();
		List<ISimpleExpression> upd = new ArrayList<>();

		for (int i = 0; i < updaters.size(); i++) {
			upd.add(exprVisitor.createSimpleExpression(updaters.get(i), body));
		}

		visitStatement(stmt.getBody(), loop.getBody());

		body.add(loop);
	}

	public void visit(EnhancedForStatement stmt, List<IStatement> body) {
		ForEachLoop loop = new ForEachLoop();

		String variableIdentifier = stmt.getParameter().getName()
				.getIdentifier();
		String typeIdentifier = BindingFactory.getBindingName(stmt
				.getParameter().getType().resolveBinding());

		VariableDeclaration decl = new VariableDeclaration();
		VariableReference ref = new VariableReference();

		ref.setIdentifier(variableIdentifier);
		decl.setReference(ref);
		decl.setType(CsTypeName.newTypeName(typeIdentifier));
		stmt.getParameter();
		loop.setDeclaration(decl);

		loop.setLoopedReference(exprVisitor.createVariableReference(stmt.getExpression(), body));

		visitStatement(stmt.getBody(), loop.getBody());

		body.add(loop);
	}

	public static ExpressionStatement getEmptyCompletionExpression() {
		ExpressionStatement expressionStatement = new ExpressionStatement();
		expressionStatement.setExpression(new CompletionExpression());
		return expressionStatement;
	}
}
