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
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.eclipse.commons.analysis.completiontarget.CompletionTargetMarker;
import cc.kave.eclipse.commons.analysis.completiontarget.CompletionTargetMarker.CompletionCase;
import cc.kave.eclipse.commons.analysis.util.UniqueVariableNameGenerator;
import cc.kave.eclipse.namefactory.NodeFactory.BindingFactory;

public class BodyVisitor extends ASTVisitor {

	private CompletionTargetMarker marker;
	private ExpressionVisitor exprVisitor;
	private UniqueVariableNameGenerator nameGen;
	private List<IStatement> body = new ArrayList<>();

	public static ExpressionStatement getEmptyCompletionExpression() {
		ExpressionStatement expressionStatement = new ExpressionStatement();
		expressionStatement.setExpression(new CompletionExpression());
		return expressionStatement;
	}

	public BodyVisitor(UniqueVariableNameGenerator nameGen, CompletionTargetMarker marker, List<IStatement> body) {
		this.marker = marker;
		this.nameGen = nameGen;
		this.body = body;
		exprVisitor = new ExpressionVisitor(nameGen, marker);
	}

	@Override
	public boolean visit(BreakStatement stmt) {
		addIf(stmt, CompletionCase.EmptyCompletionBefore, body);

		body.add(new cc.kave.commons.model.ssts.impl.statements.BreakStatement());

		addIf(stmt, CompletionCase.EmptyCompletionAfter, body);

		return super.visit(stmt);
	};

	@Override
	public boolean visit(VariableDeclarationFragment decl) {
		if (isTargetMatch(decl, CompletionCase.EmptyCompletionBefore)) {
			body.add(getEmptyCompletionExpression());
		}

		String identifier = BindingFactory.getBindingName(decl.resolveBinding().getType());
		TypeName type = CsTypeName.newTypeName(identifier);
		String id = decl.getName().getIdentifier();

		// var id = decl.DeclaredName;
		// TypeName type;
		// try
		// {
		// type = decl.Type.GetName();
		// }
		// catch (AssertException)
		// {
		// // TODO this is an intermediate "fix"... the analysis sometimes fails
		// here ("cannot create name for anonymous type")
		// type = CsTypeName.UNKNOWN_NAME;
		// }
		body.add(SSTUtil.declare(id, type));

		IAssignableExpression initializer = null;

		// if (decl.Initial != null)
		// {
		// initializer = exprVisitor.ToAssignableExpr(decl.Initial, body);
		// }
		// else if (marker.getAffectedNode() == decl && marker.getCase() ==
		// CompletionCase.Undefined)
		// {
		// initializer = new CompletionExpression();
		// }

		if (initializer != null) {
			body.add(SSTUtil.assignmentToLocal(id, initializer));
		}

		if (decl == marker.getAffectedNode() && marker.getCase() == CompletionCase.EmptyCompletionAfter) {
			body.add(getEmptyCompletionExpression());
		}

		return super.visit(decl);
	}

	@Override
	public boolean visit(Assignment expr) {
		if (isTargetMatch(expr, CompletionCase.EmptyCompletionBefore)) {
			body.add(getEmptyCompletionExpression());
		}

		boolean isTarget = isTargetMatch(expr, CompletionCase.Undefined);

		// if(exprVisitor)
		// ;
		// char sstRef = ;
		// new UnknownReference();

		// var sstExpr =
		// isTarget
		// ? new CompletionExpression()
		// : exprVisitor.ToAssignableExpr(expr.Source, body);
		//
		// var operation = TryGetEventSubscriptionOperation(expr);
		// if (operation.HasValue)
		// {
		// body.add(
		// new EventSubscriptionStatement
		// {
		// Reference = sstRef,
		// Operation = operation.Value,
		// Expression = sstExpr
		// });
		// }
		// else
		// {
		// body.add(
		// new Assignment
		// {
		// Reference = sstRef,
		// Expression = IsFancyAssign(expr) ? new ComposedExpression() : sstExpr
		// });
		// }
		//
		// if (isTargetMatch(expr, CompletionCase.EmptyCompletionAfter))
		// {
		// body.add(getEmptyCompletionExpression);
		// }
		return super.visit(expr);
	}

	@Override
	public boolean visit(ReturnStatement stmt) {
		if (isTargetMatch(stmt, CompletionCase.EmptyCompletionBefore)) {
			body.add(getEmptyCompletionExpression());
		}

		cc.kave.commons.model.ssts.impl.statements.ReturnStatement returnStmt = new cc.kave.commons.model.ssts.impl.statements.ReturnStatement();

		if (stmt.getExpression() == null) {
			returnStmt.setIsVoid(true);
			body.add(returnStmt);
		} else {
			ISimpleExpression simpleExpression = exprVisitor.toSimpleExpression(stmt.getExpression(), body);

			if (simpleExpression == null) {
				returnStmt.setExpression(simpleExpression);
			} else {
				returnStmt.setExpression(new UnknownExpression());
			}
			body.add(returnStmt);
		}

		if (isTargetMatch(stmt, CompletionCase.EmptyCompletionAfter)) {
			body.add(getEmptyCompletionExpression());
		}
		return super.visit(stmt);
	}

	@Override
	public boolean visit(EmptyStatement stmt) {
		if (stmt == marker.getAffectedNode()) {
			body.add(getEmptyCompletionExpression());
		}
		return super.visit(stmt);
	}

	@Override
	public boolean visit(ContinueStatement stmt) {
		if (isTargetMatch(stmt, CompletionCase.EmptyCompletionBefore)) {
			body.add(getEmptyCompletionExpression());
		}

		body.add(new cc.kave.commons.model.ssts.impl.statements.ContinueStatement());

		if (isTargetMatch(stmt, CompletionCase.EmptyCompletionAfter)) {
			body.add(getEmptyCompletionExpression());
		}
		return super.visit(stmt);
	}

	@Override
	public boolean visit(IfStatement stmt) {
		if (isTargetMatch(stmt, CompletionCase.EmptyCompletionBefore)) {
			body.add(getEmptyCompletionExpression());
		}
		IfElseBlock ifElseBlock = new IfElseBlock();
		// TODO: Was bedeutet stmt.Condition?
		// Condition = _exprVisitor.ToSimpleExpression(stmt.Condition, body) ??
		// new UnknownExpression()
		ifElseBlock.setCondition(exprVisitor.toSimpleExpression(null, body));

		if (isTargetMatch(stmt, CompletionCase.InBody)) {
			ifElseBlock.getThen().add(getEmptyCompletionExpression());
		}
		if (isTargetMatch(stmt, CompletionCase.InElse)) {
			ifElseBlock.getElse().add(getEmptyCompletionExpression());
		}
		if (stmt.getThenStatement() != null) {
			stmt.getThenStatement().accept(new BodyVisitor(nameGen, marker, ifElseBlock.getThen()));
		}
		if (stmt.getElseStatement() != null) {
			stmt.getElseStatement().accept(new BodyVisitor(nameGen, marker, ifElseBlock.getElse()));
		}

		body.add(ifElseBlock);

		if (isTargetMatch(stmt, CompletionCase.EmptyCompletionAfter)) {
			body.add(getEmptyCompletionExpression());
		}
		return super.visit(stmt);
	}

	@Override
	public boolean visit(WhileStatement stmt) {

		if (marker.getAffectedNode() == stmt && marker.getCase() == CompletionCase.EmptyCompletionBefore) {
			body.add(getEmptyCompletionExpression());
		}

		WhileLoop loop = new WhileLoop();
		// TODO: Condition = _exprVisitor.ToLoopHeaderExpression(stmt.Condition,
		// body)
		loop.setCondition(exprVisitor.toLoopHeaderExpression(stmt, body));

		body.add(loop);

		stmt.getBody().accept(new BodyVisitor(nameGen, marker, loop.getBody()));

		if (marker.getAffectedNode() == stmt && marker.getCase() == CompletionCase.InBody) {
			loop.getBody().add(getEmptyCompletionExpression());
		}

		if (marker.getAffectedNode() == stmt && marker.getCase() == CompletionCase.EmptyCompletionAfter) {
			body.add(getEmptyCompletionExpression());
		}
		return super.visit(stmt);
	}

	@Override
	public boolean visit(ForStatement stmt) {
		if (isTargetMatch(stmt, CompletionCase.EmptyCompletionBefore)) {
			body.add(getEmptyCompletionExpression());
		}

		ForLoop forLoop = new ForLoop();
		body.add(forLoop);

		if (isTargetMatch(stmt, CompletionCase.InBody)) {
			forLoop.getBody().add(getEmptyCompletionExpression());
		}

		// TODO: Lösung finden...
		// VisitForStatement_Init(stmt.Initializer, forLoop.Init, body);
		// forLoop.Condition =
		// _exprVisitor.ToLoopHeaderExpression(stmt.Condition, body);
		//
		// foreach (var expr in stmt.IteratorExpressionsEnumerable)
		// {
		// expr.Accept(this, forLoop.getStep());
		// }

		if (stmt.getBody() != null) {
			stmt.getBody().accept(new BodyVisitor(nameGen, marker, forLoop.getBody()));
		}

		if (isTargetMatch(stmt, CompletionCase.EmptyCompletionAfter)) {
			body.add(getEmptyCompletionExpression());
		}
		return super.visit(stmt);
	}

	@Override
	public boolean visit(EnhancedForStatement stmt) {
		if (isTargetMatch(stmt, CompletionCase.EmptyCompletionBefore)) {
			body.add(getEmptyCompletionExpression());
		}

		ForEachLoop loop = new ForEachLoop();
		// TODO: stmt.Collection analogie suchen
		// LoopedReference = _exprVisitor.ToVariableRef(stmt.Collection, body)
		// loop.setLoopedReference(exprVisitor.toVariableRef(stmt.get));
		body.add(loop);

		// TODO : ???
		// if (stmt.IteratorDeclaration != null &&
		// stmt.IteratorDeclaration.DeclaredElement != null)
		// {
		// var localVar =
		// stmt.IteratorDeclaration.DeclaredElement.GetName<LocalVariableName>();
		// loop.Declaration = new VariableDeclaration
		// {
		// Reference = new VariableReference {Identifier = localVar.Name},
		// Type = localVar.ValueType
		// };
		// }

		if (isTargetMatch(stmt, CompletionCase.InBody)) {
			loop.getBody().add(getEmptyCompletionExpression());
		}

		if (stmt.getBody() != null) {
			stmt.getBody().accept(new BodyVisitor(nameGen, marker, loop.getBody()));
		}

		if (isTargetMatch(stmt, CompletionCase.EmptyCompletionAfter)) {
			body.add(getEmptyCompletionExpression());
		}
		return super.visit(stmt);
	}

	private void addIf(ASTNode node, CompletionCase completionCase, List<IStatement> body) {
		if (isTargetMatch(node, completionCase)) {
			body.add(getEmptyCompletionExpression());
		}
	}

	private boolean isTargetMatch(ASTNode o, CompletionCase completionCase) {
		return o == marker.getAffectedNode() && marker.getCase() == completionCase;
	}
}
