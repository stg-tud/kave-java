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

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsParameterName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.SwitchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.blocks.UsingBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;
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

		if (!condition.getBody().isEmpty()) {
			cc.kave.commons.model.ssts.impl.statements.ReturnStatement returnStatement = new cc.kave.commons.model.ssts.impl.statements.ReturnStatement();
			returnStatement.setExpression(conditionVisitor.getSimpleExpression());
			condition.getBody().add(returnStatement);

			loop.setCondition(condition);
		} else {
			loop.setCondition(conditionVisitor.getSimpleExpression());
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
				if (isArrayCreation(fragment.getInitializer())) {
					exprVisitor.setLastArrayAccess(variableReference);
				}

				fragment.getInitializer().accept(exprVisitor);

				if (!isSelfAssign(exprVisitor.getAssignableExpression(), variableReference)
						&& !isArrayCreation(fragment.getInitializer())) {
					cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();
					assignment.setReference(variableReference);
					assignment.setExpression(exprVisitor.getAssignableExpression());

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

				if (!label.isDefault()) {
					label.getExpression().accept(exprVisitor);

					caseBlock = new CaseBlock();
					caseBlock.setLabel(exprVisitor.getSimpleExpression());

					visitor = new BodyVisitor(nameGen, caseBlock.getBody());

					switchBlock.getSections().add(caseBlock);
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
	public boolean visit(org.eclipse.jdt.core.dom.ThrowStatement node) {
		ThrowStatement throwStmt = new ThrowStatement();

		ExpressionVisitor visitor = new ExpressionVisitor(nameGen, body);
		node.getExpression().accept(visitor);

		throwStmt.setReference(visitor.getVariableReference());

		body.add(throwStmt);

		return false;
	}

	@Override
	public boolean visit(TryStatement node) {
		List<CatchClause> catchClauses = node.catchClauses();
		boolean hasCatchOrFinally = !(catchClauses.isEmpty() && node.getFinally() == null);
		ITryBlock tryBlock = new TryBlock();

		List<VariableDeclarationExpression> resources = node.resources();

		if (!resources.isEmpty()) {
			List<IStatement> lastBody;

			if (hasCatchOrFinally) {
				lastBody = tryBlock.getBody();
			} else {
				lastBody = body;
			}

			ExpressionVisitor resourceVisitor = new ExpressionVisitor(nameGen, lastBody);
			for (int i = 0; i < resources.size(); i++) {
				resources.get(i).accept(resourceVisitor);

				UsingBlock usingBlock = new UsingBlock();
				usingBlock.setReference(resourceVisitor.getVariableReference());

				if (!hasCatchOrFinally && i == 0) {
					lastBody = body;
				}

				lastBody.add(usingBlock);

				resourceVisitor = new ExpressionVisitor(nameGen, usingBlock.getBody());
				lastBody = usingBlock.getBody();
			}

			BodyVisitor tryBodyVisitor = new BodyVisitor(nameGen, lastBody);
			node.getBody().accept(tryBodyVisitor);
		} else {

			BodyVisitor tryBodyVisitor = new BodyVisitor(nameGen, tryBlock.getBody());
			node.getBody().accept(tryBodyVisitor);
		}

		if (!catchClauses.isEmpty() || node.getFinally() != null) {
			for (CatchClause clause : catchClauses) {
				CatchBlock catchBlock = new CatchBlock();

				TypeName exceptionType = BindingFactory.getTypeName(clause.getException().getType().resolveBinding());
				String exceptionIdentifier = clause.getException().getName().getIdentifier();
				catchBlock.setParameter(constructParameterName(exceptionType, exceptionIdentifier));

				BodyVisitor catchBodyVisitor = new BodyVisitor(nameGen, catchBlock.getBody());
				clause.getBody().accept(catchBodyVisitor);
				tryBlock.getCatchBlocks().add(catchBlock);
			}

			if (node.getFinally() != null) {
				BodyVisitor finallyBodyVisitor = new BodyVisitor(nameGen, tryBlock.getFinally());
				node.getFinally().accept(finallyBodyVisitor);
			}

			body.add(tryBlock);
		}

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

	private boolean isArrayCreation(ASTNode node) {
		if (node instanceof ArrayCreation || node instanceof ArrayInitializer) {
			return true;
		}
		return false;
	}

	private ParameterName constructParameterName(TypeName type, String id) {
		String name = "[" + type.getIdentifier() + "] " + id;

		return CsParameterName.newParameterName(name);
	}
}
