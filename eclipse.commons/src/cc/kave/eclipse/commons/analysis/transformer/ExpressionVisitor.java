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
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Assignment.Operator;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.impl.expressions.assignable.BinaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CastExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.TypeCheckExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.IndexAccessReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.eclipse.commons.analysis.util.UniqueVariableNameGenerator;
import cc.kave.eclipse.namefactory.NodeFactory;
import cc.kave.eclipse.namefactory.NodeFactory.BindingFactory;

public class ExpressionVisitor extends ASTVisitor {

	private UniqueVariableNameGenerator nameGen;
	private List<IStatement> body;
	private IAssignableExpression assignableExpression;
	private IAssignableReference assignableReference;
	private ISimpleExpression simpleExpression;
	private VariableReference variableReference;
	private ILoopHeaderExpression loopHeaderExpression;

	public ExpressionVisitor(UniqueVariableNameGenerator nameGen, List<IStatement> body) {
		this.nameGen = nameGen;
		this.body = body;
	}

	private void createAndAssign(ITypeBinding typeBinding, IVariableReference variableReference,
			IAssignableExpression assign) {
		VariableDeclaration varDecl = new VariableDeclaration();
		varDecl.setType(BindingFactory.getTypeName(typeBinding));
		varDecl.setReference(variableReference);

		body.add(varDecl);

		if (!isSelfAssign(assign, variableReference)) {
			cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();
			assignment.setReference(variableReference);
			assignment.setExpression(assign);

			body.add(assignment);
		}
	}

	@Override
	public boolean visit(Assignment stmt) {
		ExpressionVisitor leftVisitor = new ExpressionVisitor(nameGen, body);
		ExpressionVisitor rightVisitor = new ExpressionVisitor(nameGen, body);
		stmt.getLeftHandSide().accept(leftVisitor);
		stmt.getRightHandSide().accept(rightVisitor);

		if (stmt.getOperator() == Operator.ASSIGN
				&& !isSelfAssign(rightVisitor.getAssignableExpression(), leftVisitor.getAssignableReference())) {
			cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();
			assignment.setReference(leftVisitor.getAssignableReference());
			assignment.setExpression(rightVisitor.getAssignableExpression());
			body.add(assignment);
		}
		return false;
	}

	@Override
	public boolean visit(BooleanLiteral node) {
		ConstantValueExpression bool = new ConstantValueExpression();
		bool.setValue(Boolean.toString(node.booleanValue()));
		assignableExpression = bool;
		simpleExpression = bool;
		return false;
	}

	@Override
	public boolean visit(ConditionalExpression node) {
		IfElseExpression ifElseExpression = new IfElseExpression();

		node.getExpression().accept(this);
		ifElseExpression.setCondition(getSimpleExpression());
		node.getThenExpression().accept(this);
		ifElseExpression.setThenExpression(getSimpleExpression());
		node.getElseExpression().accept(this);
		ifElseExpression.setElseExpression(getSimpleExpression());

		assignableExpression = ifElseExpression;
		return false;
	}

	@Override
	public boolean visit(FieldAccess node) {
		node.getExpression().accept(this);

		FieldReference fieldReference = new FieldReference();
		fieldReference.setFieldName((FieldName) NodeFactory.createNodeName(node));
		VariableReference fieldVarRef = new VariableReference();

		if (node.getExpression() instanceof ThisExpression || node.getExpression() == null) {
			fieldVarRef.setIdentifier("this");
			fieldReference.setReference(fieldVarRef);
		} else {
			fieldReference.setReference(this.getVariableReference());
		}

		ReferenceExpression refExpr = new ReferenceExpression();
		refExpr.setReference(fieldReference);

		variableReference = fieldVarRef;
		assignableExpression = refExpr;

		if (!isParentStatement(node) || node.getParent() instanceof MethodInvocation) {
			VariableReference genVar = new VariableReference();
			genVar.setIdentifier(nameGen.getNextVariableName());
			variableReference = genVar;

			createAndAssign(node.resolveTypeBinding(), genVar, refExpr);
		}
		return false;
	}

	@Override
	public boolean visit(InstanceofExpression node) {
		node.getLeftOperand().accept(this);
		VariableReference genVar = new VariableReference();
		genVar.setIdentifier(nameGen.getNextVariableName());

		VariableDeclaration varDecl = new VariableDeclaration();
		varDecl.setType(BindingFactory.getTypeName(node.resolveTypeBinding()));
		varDecl.setReference(genVar);

		body.add(varDecl);

		TypeCheckExpression typeCheckExpr = new TypeCheckExpression();
		typeCheckExpr.setReference(getVariableReference());
		typeCheckExpr.setType(BindingFactory.getTypeName(node.getRightOperand().resolveBinding()));

		cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();
		assignment.setReference(genVar);
		assignment.setExpression(typeCheckExpr);
		body.add(assignment);

		ReferenceExpression refExpr = new ReferenceExpression();
		refExpr.setReference(genVar);

		simpleExpression = refExpr;
		assignableExpression = typeCheckExpr;
		variableReference = genVar;
		return false;
	}

	@Override
	public boolean visit(NullLiteral node) {
		NullExpression nullValue = new NullExpression();
		assignableExpression = nullValue;
		simpleExpression = nullValue;
		return false;
	}

	@Override
	public boolean visit(NumberLiteral node) {
		ConstantValueExpression number = new ConstantValueExpression();
		String token = node.getToken();

		if (token.endsWith("f")) {
			token = token.substring(0, token.length() - 1);
		}

		number.setValue(token);
		assignableExpression = number;
		simpleExpression = number;
		return false;
	}

	@Override
	public boolean visit(org.eclipse.jdt.core.dom.CastExpression node) {
		node.getExpression().accept(this);

		VariableReference genVar = new VariableReference();
		genVar.setIdentifier(nameGen.getNextVariableName());

		CastExpression castExpr = new CastExpression();
		castExpr.setOperator(CastOperator.Cast);
		castExpr.setReference(genVar);
		TypeName type = BindingFactory.getTypeName(node.getType().resolveBinding());
		castExpr.setTargetType(type);

		if (isParentStatement(node)) {
			VariableDeclaration varDecl = new VariableDeclaration();
			varDecl.setReference(genVar);
			varDecl.setType(BindingFactory.getTypeName(node.getExpression().resolveTypeBinding()));
			body.add(varDecl);

			cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();
			assignment.setReference(genVar);
			assignment.setExpression(this.getAssignableExpression());
			body.add(assignment);
		}

		assignableExpression = castExpr;

		return false;
	}

	@Override
	public boolean visit(org.eclipse.jdt.core.dom.ClassInstanceCreation node) {

		InvocationExpression invocation = new InvocationExpression();

		@SuppressWarnings("unchecked")
		List<Expression> arguments = node.arguments();
		List<ISimpleExpression> simpleExpressions = new ArrayList<ISimpleExpression>();

		for (Expression expr : arguments) {
			ExpressionVisitor visitor = new ExpressionVisitor(nameGen, body);
			expr.accept(visitor);
			simpleExpressions.add(visitor.getSimpleExpression());
		}

		invocation.setMethodName((MethodName) NodeFactory.createNodeName(node));
		invocation.setParameters(simpleExpressions);

		if (node.getParent() instanceof org.eclipse.jdt.core.dom.ExpressionStatement) {
			ExpressionStatement stmt = new ExpressionStatement();
			stmt.setExpression(invocation);
			body.add(stmt);
		} else if (!isParentStatement(node) || node.getParent() instanceof EnhancedForStatement
				|| node.getParent() instanceof ThrowStatement) {
			VariableReference varRef = new VariableReference();
			varRef.setIdentifier(nameGen.getNextVariableName());
			variableReference = varRef;

			createAndAssign(node.resolveTypeBinding(), varRef, invocation);

			ReferenceExpression refExpr = new ReferenceExpression();
			refExpr.setReference(varRef);

			simpleExpression = refExpr;
		}

		assignableExpression = invocation;
		return false;
	}

	@Override
	public boolean visit(org.eclipse.jdt.core.dom.MethodInvocation node) {

		MethodName methodName = (MethodName) NodeFactory.createNodeName(node);

		ExpressionStatement stmt = new ExpressionStatement();
		InvocationExpression invocation = new InvocationExpression();
		VariableReference varRef = new VariableReference();
		ReferenceExpression refExpr = new ReferenceExpression();

		if (node.getExpression() == null || node.getExpression() instanceof ThisExpression) {
			varRef.setIdentifier("this");
		} else {
			node.getExpression().accept(this);
			varRef = getVariableReference();
		}

		if (methodName.isStatic()) {
			varRef = new VariableReference();
		}

		@SuppressWarnings("unchecked")
		List<Expression> arguments = node.arguments();
		List<ISimpleExpression> simpleExpressions = new ArrayList<ISimpleExpression>();

		for (Expression expr : arguments) {
			ExpressionVisitor visitor = new ExpressionVisitor(nameGen, body);
			expr.accept(visitor);
			simpleExpressions.add(visitor.getSimpleExpression());
		}

		invocation.setParameters(simpleExpressions);
		invocation.setMethodName(methodName);
		invocation.setReference(varRef);
		stmt.setExpression(invocation);

		MethodReference methodReference = new MethodReference();
		methodReference.setMethodName(methodName);
		methodReference.setReference(varRef);

		ReferenceExpression methodRefExpr = new ReferenceExpression();
		methodRefExpr.setReference(methodReference);

		if (isParentAssignment(node) && node.getExpression() == null) {
			assignableExpression = methodRefExpr;
		} else {
			assignableExpression = invocation;
		}

		variableReference = varRef;

		if (!isParentStatement(node) || isParentLoop(node) || node.getParent() instanceof InstanceofExpression
				|| node.getParent() instanceof MethodInvocation) {
			VariableReference genVar = new VariableReference();
			genVar.setIdentifier(nameGen.getNextVariableName());

			refExpr.setReference(genVar);

			variableReference = genVar;
			assignableReference = genVar;
			simpleExpression = refExpr;
			assignableExpression = invocation;

			createAndAssign(node.resolveTypeBinding(), genVar, invocation);
		} else if (node.getParent() instanceof Statement) {
			body.add(stmt);
		}

		return false;
	}

	@Override
	public boolean visit(PostfixExpression node) {
		ExpressionVisitor visitor = new ExpressionVisitor(nameGen, body);
		UnaryExpression unaryExpression = new UnaryExpression();
		cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();

		node.getOperand().accept(visitor);

		switch (node.getOperator().toString()) {

		case "++":
			unaryExpression.setOperator(UnaryOperator.PostIncrement);
			unaryExpression.setOperand(visitor.getSimpleExpression());
			assignableExpression = unaryExpression;
			break;

		case "--":
			unaryExpression.setOperator(UnaryOperator.PostDecrement);
			unaryExpression.setOperand(visitor.getSimpleExpression());
			assignableExpression = unaryExpression;
			break;

		default:
			break;
		}

		if (isPreOrPostfixExpression(node.getOperator().toString())) {
			assignment.setExpression(unaryExpression);
			assignment.setReference(visitor.getAssignableReference());
			body.add(assignment);
		}
		return false;
	}

	@Override
	public boolean visit(PrefixExpression node) {
		ExpressionVisitor visitor = new ExpressionVisitor(nameGen, body);
		UnaryExpression unaryExpression = new UnaryExpression();
		cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();

		node.getOperand().accept(visitor);

		switch (node.getOperator().toString()) {
		case "++":
			unaryExpression.setOperator(UnaryOperator.PreIncrement);
			unaryExpression.setOperand(visitor.getSimpleExpression());
			assignableExpression = unaryExpression;
			break;

		case "--":
			unaryExpression.setOperator(UnaryOperator.PreDecrement);
			unaryExpression.setOperand(visitor.getSimpleExpression());
			assignableExpression = unaryExpression;
			break;

		case "-":
			unaryExpression.setOperator(UnaryOperator.Minus);
			unaryExpression.setOperand(visitor.getSimpleExpression());
			assignableExpression = unaryExpression;
			break;

		case "+":
			unaryExpression.setOperator(UnaryOperator.Plus);
			unaryExpression.setOperand(visitor.getSimpleExpression());
			assignableExpression = unaryExpression;
			break;

		case "!":
			unaryExpression.setOperator(UnaryOperator.Not);
			unaryExpression.setOperand(visitor.getSimpleExpression());
			assignableExpression = unaryExpression;
			break;

		case "~":
			unaryExpression.setOperator(UnaryOperator.Complement);
			unaryExpression.setOperand(visitor.getSimpleExpression());
			assignableExpression = unaryExpression;
			break;

		default:
			break;
		}

		if (isPreOrPostfixExpression(node.getOperator().toString())) {
			assignment.setExpression(unaryExpression);
			assignment.setReference(visitor.getAssignableReference());
			body.add(assignment);
		}
		return false;
	}

	@Override
	public boolean visit(QualifiedName node) {
		node.getQualifier().accept(this);

		ExpressionVisitor visitor = new ExpressionVisitor(new UniqueVariableNameGenerator(), new ArrayList<>());
		node.getQualifier().accept(visitor);

		VariableReference targetRef = visitor.getVariableReference();

		FieldReference fieldRef = new FieldReference();
		fieldRef.setFieldName((FieldName) NodeFactory.createNodeName(node));
		fieldRef.setReference(targetRef);

		ReferenceExpression refExpr = new ReferenceExpression();
		refExpr.setReference(fieldRef);

		assignableExpression = refExpr;

		if (!isParentStatement(node)) {
			VariableReference genVar = new VariableReference();
			genVar.setIdentifier(nameGen.getNextVariableName());
			variableReference = genVar;

			createAndAssign(node.resolveTypeBinding(), genVar, refExpr);
		}

		return false;

	}

	@Override
	public boolean visit(SimpleName node) {

		if (node.resolveBinding() instanceof IVariableBinding) {
			IVariableBinding varBinding = (IVariableBinding) node.resolveBinding();

			ReferenceExpression refExpr = new ReferenceExpression();
			VariableReference varId = new VariableReference();
			varId.setIdentifier(node.getIdentifier());

			if (varBinding.isField()) {
				VariableReference thisRef = new VariableReference();
				thisRef.setIdentifier("this");

				String fieldName = BindingFactory.getBindingName(varBinding);

				FieldReference fieldRef = new FieldReference();
				fieldRef.setFieldName(CsFieldName.newFieldName(fieldName));
				fieldRef.setReference(thisRef);

				refExpr.setReference(fieldRef);

				assignableReference = fieldRef;
			} else {

				refExpr.setReference(varId);

				assignableReference = varId;
			}

			variableReference = varId;
			simpleExpression = refExpr;
			assignableExpression = refExpr;

			IVariableBinding var = (IVariableBinding) node.resolveBinding();

			if ((!isParentStatement(node) && node.getParent() instanceof MethodInvocation && !var.isParameter()
					&& !isArgument(node)) || (node.getParent() instanceof MethodInvocation && var.isField())) {
				String identifier = nameGen.getNextVariableName();
				VariableReference genVar = new VariableReference();
				genVar.setIdentifier(identifier);

				variableReference = genVar;

				createAndAssign(node.resolveTypeBinding(), genVar, refExpr);
			}
		}
		return false;
	}

	@Override
	public boolean visit(StringLiteral node) {
		ConstantValueExpression string = new ConstantValueExpression();
		string.setValue(node.getLiteralValue());
		simpleExpression = string;
		return false;
	}

	@Override
	public boolean visit(SuperFieldAccess node) {
		return false;
	}

	@Override
	public boolean visit(ArrayCreation node) {
		node.getInitializer().accept(this);
		return false;
	}

	@Override
	public boolean visit(ArrayInitializer node) {
		IndexAccessExpression indexAccessExpr = new IndexAccessExpression();
		VariableReference varRef = new VariableReference();

		int i = 0;
		List<Expression> expressions = node.expressions();
		for (Expression expression : expressions) {
			expression.accept(this);

			ConstantValueExpression index = new ConstantValueExpression();
			index.setValue(Integer.toString(i++));

			indexAccessExpr.getIndices().add(index);
			indexAccessExpr.setReference(varRef);

			IndexAccessReference indexAccessReference = new IndexAccessReference();
			indexAccessReference.setExpression(indexAccessExpr);

			cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();
			assignment.setExpression(getAssignableExpression());
			assignment.setReference(indexAccessReference);

			body.add(assignment);
		}

		assignableExpression = indexAccessExpr;

		return false;
	}

	@Override
	public boolean visit(ArrayAccess node) {
		IndexAccessExpression indexAccessExpression = new IndexAccessExpression();

		VariableReference genVar = new VariableReference();
		if (!(node.getArray() instanceof ArrayAccess) && node.getParent() instanceof ArrayAccess) {
			genVar.setIdentifier(nameGen.getNextVariableName());
			variableReference = genVar;

			createAndAssign(node.resolveTypeBinding(), genVar, indexAccessExpression);
		}

		node.getArray().accept(this);

		if (genVar.getIdentifier().equals("") && node.getArray() instanceof ArrayAccess) {
			genVar.setIdentifier(nameGen.getLastVariableName());
			indexAccessExpression.setReference(genVar);
		} else {
			indexAccessExpression.setReference(getVariableReference());
		}

		node.getIndex().accept(this);
		indexAccessExpression.getIndices().add(getSimpleExpression());
		assignableExpression = indexAccessExpression;

		IndexAccessReference indexAccessReference = new IndexAccessReference();
		indexAccessReference.setExpression(indexAccessExpression);

		assignableReference = indexAccessReference;

		return false;
	}

	@Override
	public boolean visit(VariableDeclarationExpression node) {

		for (int i = 0; i < node.fragments().size(); i++) {
			VariableDeclarationFragment fragment = (VariableDeclarationFragment) node.fragments().get(i);
			VariableDeclaration variableDeclaration = new VariableDeclaration();
			VariableReference variableReference = new VariableReference();

			variableReference.setIdentifier(fragment.getName().getIdentifier());
			variableDeclaration.setReference(variableReference);
			variableDeclaration.setType(NodeFactory.getBindingName(node.getType().resolveBinding()));

			body.add(variableDeclaration);

			if (fragment.getInitializer() != null) {
				fragment.getInitializer().accept(this);
				cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();
				assignment.setReference(variableReference);
				assignment.setExpression(this.getAssignableExpression());
				body.add(assignment);

				this.variableReference = variableReference;
			}

		}
		return false;
	}

	@Override
	public boolean visit(InfixExpression node) {
		BinaryExpression binaryExpression = new BinaryExpression();
		BinaryOperator binaryOperator = toBinaryOperator(node.getOperator().toString());
		binaryExpression.setOperator(binaryOperator);

		node.getLeftOperand().accept(this);
		binaryExpression.setLeftOperand(getSimpleExpression());

		node.getRightOperand().accept(this);
		binaryExpression.setRightOperand(getSimpleExpression());

		List<Expression> extendedOperands = node.extendedOperands();
		for (int i = 0; i < extendedOperands.size(); i++) {
			VariableReference genVar = new VariableReference();
			genVar.setIdentifier(nameGen.getNextVariableName());

			createAndAssign(node.resolveTypeBinding(), genVar, binaryExpression);

			binaryExpression = new BinaryExpression();
			binaryExpression.setOperator(binaryOperator);

			ReferenceExpression leftOperand = new ReferenceExpression();
			leftOperand.setReference(genVar);

			binaryExpression.setLeftOperand(leftOperand);

			extendedOperands.get(i).accept(this);
			binaryExpression.setRightOperand(getSimpleExpression());
		}

		assignableExpression = binaryExpression;

		return false;
	}

	public IAssignableExpression getAssignableExpression() {
		return assignableExpression;
	}

	public IAssignableReference getAssignableReference() {
		return assignableReference;
	}

	public ILoopHeaderExpression getLoopHeaderExpression() {
		return loopHeaderExpression;
	}

	public ISimpleExpression getSimpleExpression() {
		return simpleExpression;
	}

	public VariableReference getVariableReference() {
		return variableReference;
	}

	private boolean isParentStatement(ASTNode node) {
		ASTNode parent = node.getParent();
		if (parent instanceof Statement || parent instanceof VariableDeclarationFragment || parent instanceof Assignment
				|| parent instanceof MethodInvocation || parent instanceof PrefixExpression
				|| parent instanceof PostfixExpression || parent instanceof ConditionalExpression
				|| parent instanceof InstanceofExpression
				|| parent instanceof org.eclipse.jdt.core.dom.CastExpression) {
			return true;
		}

		return false;
	}

	private boolean isParentLoop(ASTNode node) {
		ASTNode parent = node.getParent();

		while (!(parent instanceof TypeDeclaration)) {
			if (parent instanceof DoStatement || parent instanceof ForStatement || parent instanceof WhileStatement) {
				return true;
			}
			parent = parent.getParent();
		}

		return false;
	}

	private boolean isParentAssignment(ASTNode node) {
		ASTNode parent = node.getParent();

		if (parent instanceof Assignment || parent instanceof VariableDeclarationFragment) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isPreOrPostfixExpression(String operator) {
		String[] exprString = { "--", "++", "~" };

		for (int i = 0; i < exprString.length; i++) {
			if (operator.equals(exprString[i])) {
				return true;
			}
		}
		return false;
	}

	private boolean isArgument(ASTNode node) {
		if (node.getParent() instanceof MethodInvocation) {
			MethodInvocation m = (MethodInvocation) node.getParent();
			if (m.arguments().contains(node)) {
				return true;
			}
		}
		return false;
	}

	private boolean isSelfAssign(IAssignableExpression assign, IAssignableReference varRef) {
		if (assign instanceof ReferenceExpression && varRef.equals(((ReferenceExpression) assign).getReference())) {
			return true;
		}
		return false;
	}

	private BinaryOperator toBinaryOperator(String operator) {

		switch (operator) {
		// arithmetic operators
		case "+":
			return BinaryOperator.Plus;
		case "-":
			return BinaryOperator.Minus;
		case "*":
			return BinaryOperator.Multiply;
		case "/":
			return BinaryOperator.Divide;
		case "%":
			return BinaryOperator.Modulo;

		// logical operators
		case "<":
			return BinaryOperator.LessThan;
		case "<=":
			return BinaryOperator.LessThanOrEqual;
		case ">":
			return BinaryOperator.GreaterThan;
		case ">=":
			return BinaryOperator.GreaterThanOrEqual;
		case "==":
			return BinaryOperator.Equal;
		case "!=":
			return BinaryOperator.NotEqual;
		case "&&":
			return BinaryOperator.And;
		case "||":
			return BinaryOperator.Or;

		// bitwise operators
		case "&":
			return BinaryOperator.BitwiseAnd;
		case "|":
			return BinaryOperator.BitwiseOr;
		case "^":
			return BinaryOperator.BitwiseXor;
		case "<<":
			return BinaryOperator.ShiftLeft;
		case ">>":
			return BinaryOperator.ShiftRight;

		default:
			return null;
		}
	}
}
