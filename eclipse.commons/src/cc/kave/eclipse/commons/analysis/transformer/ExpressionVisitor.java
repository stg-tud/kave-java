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
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.ThisExpression;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ILoopHeaderExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.eclipse.commons.analysis.util.UniqueVariableNameGenerator;
import cc.kave.eclipse.namefactory.NodeFactory;

public class ExpressionVisitor extends ASTVisitor {

	private UniqueVariableNameGenerator nameGen;
	private List<IStatement> body;
	private IAssignableExpression assignableExpression;
	private IAssignableReference assignableReference;
	private ISimpleExpression simpleExpression;
	private IVariableReference variableReference;
	private ILoopHeaderExpression loopHeaderExpression;
	private IVariableReference lastReference;

	public ExpressionVisitor(UniqueVariableNameGenerator nameGen, List<IStatement> body) {
		this.nameGen = nameGen;
		this.body = body;
	}

	public ISimpleExpression createSimpleExpression(ASTNode expression, List<IStatement> body) {

		switch (expression.getNodeType()) {
		case ASTNode.BOOLEAN_LITERAL:
			ConstantValueExpression bool = new ConstantValueExpression();
			bool.setValue(Boolean.toString(((BooleanLiteral) expression).booleanValue()));
			return bool;
		case ASTNode.NUMBER_LITERAL:
			ConstantValueExpression number = new ConstantValueExpression();
			String token = ((NumberLiteral) expression).getToken();

			if (token.endsWith("f")) {
				token = token.substring(0, token.length() - 1);
			}

			number.setValue(token);
			return number;
		case ASTNode.NULL_LITERAL:
			return new NullExpression();
		case ASTNode.STRING_LITERAL:
			ConstantValueExpression string = new ConstantValueExpression();
			string.setValue(((StringLiteral) expression).getLiteralValue());
			return string;
		default:
			int i = 1 + 2 + 3 - 4;
			return null;
		}
	}

	@Override
	public void endVisit(NumberLiteral node) {
		ConstantValueExpression number = new ConstantValueExpression();
		String token = node.getToken();

		if (token.endsWith("f")) {
			token = token.substring(0, token.length() - 1);
		}

		if (node.getParent() instanceof PrefixExpression
				&& ((PrefixExpression) node.getParent()).getOperator().toString().equals("-")) {
			token = "-" + token;
		}

		number.setValue(token);
		simpleExpression = number;
	}

	@Override
	public void endVisit(BooleanLiteral node) {
		ConstantValueExpression bool = new ConstantValueExpression();
		bool.setValue(Boolean.toString(node.booleanValue()));
		simpleExpression = bool;
	}

	@Override
	public void endVisit(StringLiteral node) {
		ConstantValueExpression string = new ConstantValueExpression();
		string.setValue(node.getLiteralValue());
		simpleExpression = string;
	}

	@Override
	public void endVisit(NullLiteral node) {
		NullExpression nullValue = new NullExpression();
		simpleExpression = nullValue;
	}

	public IVariableReference createVariableReference(ASTNode expression, List<IStatement> body) {
		VariableReference ref = new VariableReference();

		return null;
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
			varRef.setIdentifier(node.getExpression().toString());
		}

		invocation.setMethodName(methodName);
		invocation.setParameters(Arrays.asList());
		invocation.setReference(varRef);
		stmt.setExpression(invocation);

		body.add(stmt);

		// System.out.println("-------------endvisit----------------------");
		// System.out.println("Identifier: " + node.getName().getIdentifier());
		// System.out.println(node.getExpression() != null ? "Expression: " +
		// node.getExpression().toString() : "null");
		// System.out.println("Arguments: " + node.arguments().toString());
	}

	@Override
	public boolean visit(org.eclipse.jdt.core.dom.MethodInvocation node) {
		// System.out.println("-------------visit----------------------");
		// System.out.println("Identifier: " + node.getName().getIdentifier());
		// System.out.println(node.getExpression() != null ? "Expression: " +
		// node.getExpression().toString() : "null");
		// System.out.println("Arguments: " + node.arguments().toString());

		return true;
	}

	@Override
	public void endVisit(org.eclipse.jdt.core.dom.ClassInstanceCreation node) {
		
		InvocationExpression invocation = new InvocationExpression();
		invocation.setMethodName((MethodName) NodeFactory.createNodeName(node));
		
		List<Expression> arguments = node.arguments();
		List<ISimpleExpression> simpleExpressions = new ArrayList<ISimpleExpression>();
		
		for(Expression expr : arguments){
			ExpressionVisitor visitor = new ExpressionVisitor(nameGen, body);
			expr.accept(visitor);
			simpleExpressions.add(visitor.getSimpleExpression());
		}
//		invocation.setParameters(Arrays.asList(node.arguments()));
		
		node.getExpression();
		node.getType();
		
		simpleExpression = null;
	}

	@Override
	public void endVisit(FieldAccess node) {

	}

	@Override
	public void endVisit(SuperFieldAccess node) {
	}

	public IAssignableExpression getAssignableExpression() {
		return assignableExpression;
	}

	public IAssignableReference getAssignableReference() {
		return assignableReference;
	}

	public ISimpleExpression getSimpleExpression() {
		return simpleExpression;
	}

	public IVariableReference getVariableReference() {
		return variableReference;
	}

	public ILoopHeaderExpression getLoopHeaderExpression() {
		return loopHeaderExpression;
	}
}
