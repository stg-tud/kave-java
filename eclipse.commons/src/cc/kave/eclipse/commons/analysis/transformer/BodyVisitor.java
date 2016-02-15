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
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.SwitchBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;
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
		ILoopHeaderBlockExpression condition = new LoopHeaderBlockExpression();
		ExpressionVisitor conditionVisitor = new ExpressionVisitor(nameGen, condition.getBody());

		stmt.getExpression().accept(conditionVisitor);
		loop.setCondition(conditionVisitor.getSimpleExpression());

		if (!condition.getBody().isEmpty()) {
			cc.kave.commons.model.ssts.impl.statements.ReturnStatement returnStatement = new cc.kave.commons.model.ssts.impl.statements.ReturnStatement();
			returnStatement.setExpression(conditionVisitor.getSimpleExpression());
			condition.getBody().add(returnStatement);

			loop.setCondition(condition);
		}

		body.add(loop);

		BodyVisitor visitor = new BodyVisitor(nameGen, loop.getBody());
		stmt.getBody().accept(visitor);

		return false;
	}

	@Override
	public boolean visit(DoStatement stmt) {
		DoLoop loop = new DoLoop();
		ILoopHeaderBlockExpression condition = new LoopHeaderBlockExpression();
		ExpressionVisitor conditionVisitor = new ExpressionVisitor(nameGen, condition.getBody());

		stmt.getExpression().accept(conditionVisitor);
		loop.setCondition(conditionVisitor.getSimpleExpression());

		if (!condition.getBody().isEmpty()) {
			cc.kave.commons.model.ssts.impl.statements.ReturnStatement returnStatement = new cc.kave.commons.model.ssts.impl.statements.ReturnStatement();
			returnStatement.setExpression(conditionVisitor.getSimpleExpression());
			condition.getBody().add(returnStatement);

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
			LoopHeaderBlockExpression condition = new LoopHeaderBlockExpression();
			ExpressionVisitor conditionVisitor = new ExpressionVisitor(nameGen, condition.getBody());

			stmt.getExpression().accept(conditionVisitor);
			loop.setCondition(conditionVisitor.getSimpleExpression());

			if (!condition.getBody().isEmpty()) {
				cc.kave.commons.model.ssts.impl.statements.ReturnStatement returnStatement = new cc.kave.commons.model.ssts.impl.statements.ReturnStatement();
				returnStatement.setExpression(conditionVisitor.getSimpleExpression());
				condition.getBody().add(returnStatement);

				loop.setCondition(condition);
			}

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

				if (!isSelfAssign(exprVisitor.getAssignableExpression(), variableReference)) {
					body.add(assignment);
				}
			}
		}

		return false;
	}

	@Override
	public boolean visit(SwitchStatement node) {
		node.getExpression().accept(exprVisitor);

		SwitchBlock switchBlock = new SwitchBlock();

		if (exprVisitor.getVariableReference() != null) {
			switchBlock.setReference(exprVisitor.getVariableReference());
		} else if (exprVisitor.getSimpleExpression() instanceof ConstantValueExpression) {
			VariableReference genVar = new VariableReference();
			genVar.setIdentifier(nameGen.getNextVariableName());
			switchBlock.setReference(genVar);

			VariableDeclaration varDecl = new VariableDeclaration();
			varDecl.setType(BindingFactory.getTypeName(node.getExpression().resolveTypeBinding()));
			varDecl.setReference(genVar);

			body.add(varDecl);

			cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();
			assignment.setReference(genVar);
			assignment.setExpression(exprVisitor.getSimpleExpression());

			body.add(assignment);
		}

		List<Statement> statements = node.statements();
		CaseBlock caseBlock = null;
		BodyVisitor visitor = null;

		for (int i = 0; i < statements.size(); i++) {

			if (statements.get(i) instanceof SwitchCase) {
				SwitchCase label = (SwitchCase) statements.get(i);

				if (label.getExpression() != null) {
					label.getExpression().accept(exprVisitor);

					if (caseBlock != null) {
						switchBlock.getSections().add(caseBlock);
					}

					caseBlock = new CaseBlock();
					caseBlock.setLabel(exprVisitor.getSimpleExpression());

					visitor = new BodyVisitor(nameGen, caseBlock.getBody());
				} else {
					visitor = new BodyVisitor(nameGen, switchBlock.getDefaultSection());
				}
			} else {
				statements.get(i).accept(visitor);
			}
		}

		body.add(switchBlock);

		return false;
	}

	@Override
	public boolean visit(org.eclipse.jdt.core.dom.ExpressionStatement node) {
		node.accept(exprVisitor);
		return false;
	}

	private boolean isSelfAssign(IAssignableExpression assign, IVariableReference varRef) {
		if (assign instanceof ReferenceExpression && varRef.equals(((ReferenceExpression) assign).getReference())) {
			return true;
		}
		return false;
	}
}
