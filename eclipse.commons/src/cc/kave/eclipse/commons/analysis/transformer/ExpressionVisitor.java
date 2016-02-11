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
import org.eclipse.jdt.core.dom.Assignment.Operator;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
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
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CastExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.TypeCheckExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.eclipse.commons.analysis.util.UniqueVariableNameGenerator;
import cc.kave.eclipse.namefactory.NodeFactory;
import cc.kave.eclipse.namefactory.NodeFactory.BindingFactory;

public class ExpressionVisitor extends ASTVisitor {

	private static UniqueVariableNameGenerator nameGen;
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

	@Override
	public void endVisit(Assignment stmt) {
		ExpressionVisitor leftVisitor = new ExpressionVisitor(nameGen, body);
		ExpressionVisitor rightVisitor = new ExpressionVisitor(nameGen, body);
		stmt.getLeftHandSide().accept(leftVisitor);
		stmt.getRightHandSide().accept(rightVisitor);

		if (stmt.getOperator() == Operator.ASSIGN) {
			cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();
			assignment.setReference(leftVisitor.getAssignableReference());
			assignment.setExpression(rightVisitor.getSimpleExpression());
			body.add(assignment);
		}
	}

	@Override
	public void endVisit(BooleanLiteral node) {
		ConstantValueExpression bool = new ConstantValueExpression();
		bool.setValue(Boolean.toString(node.booleanValue()));
		assignableExpression = bool;
		simpleExpression = bool;
	}

	@Override
	public void endVisit(NullLiteral node) {
		NullExpression nullValue = new NullExpression();
		assignableExpression = nullValue;
		simpleExpression = nullValue;
	}

	@Override
	public void endVisit(NumberLiteral node) {
		ConstantValueExpression number = new ConstantValueExpression();
		String token = node.getToken();

		if (token.endsWith("f")) {
			token = token.substring(0, token.length() - 1);
		}

		number.setValue(token);
		assignableExpression = number;
		simpleExpression = number;
	}

	@Override
	public void endVisit(org.eclipse.jdt.core.dom.ClassInstanceCreation node) {

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

		if (node.getParent() instanceof EnhancedForStatement) {
			VariableReference varRef = new VariableReference();
			varRef.setIdentifier(nameGen.getNextVariableName());

			VariableDeclaration varDecl = new VariableDeclaration();
			varDecl.setReference(varRef);
			varDecl.setType(BindingFactory.getTypeName(node.resolveTypeBinding()));

			cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();
			assignment.setExpression(invocation);
			assignment.setReference(varRef);

			variableReference = varRef;

			body.add(varDecl);
			body.add(assignment);
		}

		assignableExpression = invocation;
	}

	@Override
	public void endVisit(org.eclipse.jdt.core.dom.MethodInvocation node) {

		MethodName methodName = (MethodName) NodeFactory.createNodeName(node);

		ExpressionStatement stmt = new ExpressionStatement();
		InvocationExpression invocation = new InvocationExpression();
		VariableReference varRef = new VariableReference();

		if (node.getExpression() == null || node.getExpression() instanceof ThisExpression) {
			varRef.setIdentifier("this");
		} else {
			node.getExpression().accept(this);
			varRef = getVariableReference();
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

		assignableExpression = invocation;
		variableReference = varRef;

		stmt.setExpression(invocation);

		if (!(node.getParent() instanceof org.eclipse.jdt.core.dom.CastExpression)) {
			body.add(stmt);
		}
	}

	@Override
	public void endVisit(QualifiedName node) {
		node.getQualifier().accept(this);

		String lastVar = nameGen.getLastVariableName();

		VariableReference targetRef = new VariableReference();
		targetRef.setIdentifier(lastVar);

		FieldReference fieldRef = new FieldReference();
		fieldRef.setFieldName((FieldName) NodeFactory.createNodeName(node));
		fieldRef.setReference(targetRef);

		ReferenceExpression refExpr = new ReferenceExpression();
		refExpr.setReference(fieldRef);

		assignableExpression = refExpr;

		if (!isParentStatement(node)) {

			VariableDeclaration varDecl = new VariableDeclaration();
			VariableReference varRef = new VariableReference();
			varRef.setIdentifier(nameGen.getNextVariableName());
			varDecl.setType(BindingFactory.getTypeName(node.resolveTypeBinding()));
			varDecl.setReference(varRef);

			body.add(varDecl);

			cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();

			assignment.setReference(varRef);
			assignment.setExpression(refExpr);
			body.add(assignment);

		}

	}

	@Override
	public void endVisit(SimpleName node) {

		if (node.resolveBinding() instanceof IVariableBinding) {
			IVariableBinding varBinding = (IVariableBinding) node.resolveBinding();

			VariableReference varRef = new VariableReference();
			FieldReference fieldRef = new FieldReference();
			ReferenceExpression refExpr = new ReferenceExpression();

			simpleExpression = refExpr;

			if (varBinding.isField()) {
				String fieldName = BindingFactory.getBindingName(varBinding);
				varRef.setIdentifier("this");
				fieldRef.setFieldName(CsFieldName.newFieldName(fieldName));
				fieldRef.setReference(varRef);
				refExpr.setReference(fieldRef);

				assignableReference = fieldRef;
				variableReference = varRef;
			} else {
				refExpr.setReference(varRef);
				varRef.setIdentifier(node.getIdentifier());

				variableReference = varRef;
				assignableReference = varRef;
			}

			if (!isParentStatement(node)) {
				String identifier = nameGen.getNextVariableName();
				VariableReference genVar = new VariableReference();
				genVar.setIdentifier(identifier);

				VariableDeclaration varDecl = new VariableDeclaration();
				varDecl.setReference(genVar);
				varDecl.setType(BindingFactory.getTypeName(node.resolveTypeBinding()));
				body.add(varDecl);

				cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();
				assignment.setExpression(refExpr);
				assignment.setReference(genVar);
				body.add(assignment);
			}
		}
	}

	@Override
	public void endVisit(StringLiteral node) {
		ConstantValueExpression string = new ConstantValueExpression();
		string.setValue(node.getLiteralValue());
		simpleExpression = string;
	}

	@Override
	public void endVisit(FieldAccess node) {
	}

	@Override
	public void endVisit(SuperFieldAccess node) {
	}

	@Override
	public void endVisit(PrefixExpression node) {
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
	}

	@Override
	public void endVisit(PostfixExpression node) {
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
	}

	@Override
	public void endVisit(VariableDeclarationExpression node) {

		for (int i = 0; i < node.fragments().size(); i++) {
			VariableDeclarationFragment fragment = (VariableDeclarationFragment) node.fragments().get(i);
			VariableDeclaration variableDeclaration = new VariableDeclaration();
			VariableReference variableReference = new VariableReference();

			variableReference.setIdentifier(fragment.getName().getIdentifier());
			variableDeclaration.setReference(variableReference);
			variableDeclaration.setType(NodeFactory.getBindingName(node.getType().resolveBinding()));

			body.add(variableDeclaration);

			if (fragment.getInitializer() != null) {
				cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();
				assignment.setReference(variableReference);
				assignment.setExpression(this.getAssignableExpression());
				fragment.getInitializer().accept(this);
				body.add(assignment);
			}
		}
	}

	@Override
	public void endVisit(org.eclipse.jdt.core.dom.CastExpression node) {
		node.getExpression().accept(this);

		VariableReference genVar = new VariableReference();
		genVar.setIdentifier(nameGen.getNextVariableName());

		CastExpression castExpr = new CastExpression();
		castExpr.setOperator(CastOperator.Cast);
		castExpr.setReference(genVar);
		castExpr.setTargetType(createTypeName(node.getType().resolveBinding()));

		VariableDeclaration varDecl = new VariableDeclaration();
		varDecl.setReference(genVar);
		varDecl.setType(BindingFactory.getTypeName(node.getExpression().resolveTypeBinding()));
		body.add(varDecl);

		cc.kave.commons.model.ssts.impl.statements.Assignment assignment = new cc.kave.commons.model.ssts.impl.statements.Assignment();
		assignment.setReference(genVar);
		assignment.setExpression(this.getAssignableExpression());
		body.add(assignment);

		assignableExpression = castExpr;
	}

	@Override
	public void endVisit(ConditionalExpression node) {
		IfElseExpression ifElseExpression = new IfElseExpression();

		node.getExpression().accept(this);
		ifElseExpression.setCondition(getSimpleExpression());
		node.getThenExpression().accept(this);
		ifElseExpression.setThenExpression(getSimpleExpression());
		node.getElseExpression().accept(this);
		ifElseExpression.setElseExpression(getSimpleExpression());

		assignableExpression = ifElseExpression;
	}

	@Override
	public void endVisit(InstanceofExpression node) {
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

	@Override
	public boolean visit(org.eclipse.jdt.core.dom.MethodInvocation node) {
		return false;
	}

	@Override
	public boolean visit(org.eclipse.jdt.core.dom.CastExpression node) {
		return false;
	}

	@Override
	public boolean visit(FieldAccess node) {
		return false;
	}

	@Override
	public boolean visit(SuperFieldAccess node) {
		return false;
	}

	@Override
	public boolean visit(SimpleName node) {
		return false;
	}

	@Override
	public boolean visit(QualifiedName node) {
		return false;
	}

	@Override
	public boolean visit(Assignment node) {
		return false;
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		return false;
	}

	@Override
	public boolean visit(ConditionalExpression node) {
		return false;
	}

	@Override
	public boolean visit(InstanceofExpression node) {
		return false;
	}

	private boolean isParentStatement(ASTNode node) {
		ASTNode parent = node.getParent();
		if (parent instanceof Statement || parent instanceof VariableDeclarationFragment || parent instanceof Assignment
				|| parent instanceof MethodInvocation || parent instanceof PrefixExpression
				|| parent instanceof PostfixExpression || parent instanceof ConditionalExpression
				|| parent instanceof InstanceofExpression) {
			return true;
		}

		return false;
	}

	private TypeName createTypeName(ITypeBinding binding) {
		String identifier;

		switch (binding.getQualifiedName()) {
		case "java.lang.Integer":
			identifier = "%int, rt.jar, 1.8";
			break;
		case "java.lang.Long":
			identifier = "%long, rt.jar, 1.8";
			break;
		case "java.lang.Short":
			identifier = "%short, rt.jar, 1.8";
			break;
		case "java.lang.Float":
			identifier = "%float, rt.jar, 1.8";
			break;
		case "java.lang.Double":
			identifier = "%double, rt.jar, 1.8";
			break;
		case "java.lang.Boolean":
			identifier = "%boolean, rt.jar, 1.8";
			break;
		case "java.lang.Byte":
			identifier = "%byte, rt.jar, 1.8";
			break;
		case "java.lang.Void":
			identifier = "%void, rt.jar, 1.8";
			break;
		case "java.lang.Char":
			identifier = "%char, rt.jar, 1.8";
			break;
		default:
			return BindingFactory.getTypeName(binding);
		}

		return CsTypeName.newTypeName(identifier);
	}

	private boolean isPreOrPostfixExpression(String operator) {
		String[] prefixes = { "--", "++", "~" };

		for (int i = 0; i < prefixes.length; i++) {
			if (operator.equals(prefixes[i])) {
				return true;
			}
		}
		return false;
	}
}
