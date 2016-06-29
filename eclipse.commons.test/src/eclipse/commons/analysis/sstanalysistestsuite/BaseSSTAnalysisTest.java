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

package eclipse.commons.analysis.sstanalysistestsuite;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.PrefixExpression.Operator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsParameterName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.IExpression;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.CastOperator;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.BinaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CastExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.IndexAccessReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

public abstract class BaseSSTAnalysisTest {

	protected SST context;
	protected static String packageName;
	private String projectName = "testproject";

	@Rule
	public TestName name = new TestName();

	public BaseSSTAnalysisTest() {
		packageName = getClass().getSimpleName();
	}

	protected MethodDeclaration newMethodDeclaration(String identifier) {
		MethodDeclaration decl = new MethodDeclaration();
		decl.setName(CsMethodName.newMethodName(identifier));

		return decl;
	}

	protected MethodDeclaration newDefaultMethodDeclaration() {
		MethodDeclaration decl = new MethodDeclaration();
		decl.setName(newDefaultMethodName());

		return decl;
	}

	protected MethodName newDefaultMethodName() {
		String qualifiedName = packageName.toLowerCase() + "." + capitalizeString(name.getMethodName());
		String identifier = "[%void, rt.jar, 1.8] [" + qualifiedName + ", ?].method()";
		return CsMethodName.newMethodName(identifier);
	}

	protected FieldDeclaration newFieldDeclaration(String identifier) {
		FieldDeclaration decl = new FieldDeclaration();
		decl.setName(CsFieldName.newFieldName(identifier));

		return decl;
	}

	protected ConstantValueExpression newConstantValue(String v) {
		ConstantValueExpression constExpr = new ConstantValueExpression();
		constExpr.setValue(v);
		return constExpr;
	}

	protected ExpressionStatement newEmptyCompletionExpression() {
		ExpressionStatement expressionStatement = new ExpressionStatement();
		expressionStatement.setExpression(new CompletionExpression());
		return expressionStatement;
	}

	protected VariableDeclaration newVariableDeclaration(String varName, TypeName type) {
		VariableDeclaration var = new VariableDeclaration();
		var.setType(type);
		var.setReference(newVariableReference(varName));

		return var;
	}

	protected static VariableReference newVariableReference(String id) {
		VariableReference varRef = new VariableReference();
		varRef.setIdentifier(id);

		return varRef;
	}

	protected FieldReference newFieldReference(String id, TypeName type, String target) {
		FieldReference fieldRef = new FieldReference();
		fieldRef.setFieldName(CsFieldName.newFieldName(newMemberName(id, type)));
		fieldRef.setReference(newVariableReference(target));

		return fieldRef;
	}

	protected FieldReference newFieldReference(FieldName name, String target) {
		FieldReference fieldRef = new FieldReference();
		fieldRef.setFieldName(name);
		fieldRef.setReference(newVariableReference(target));

		return fieldRef;
	}

	protected ISimpleExpression newReferenceExpression(String id) {
		ReferenceExpression ref = new ReferenceExpression();
		ref.setReference(newVariableReference(id));

		return ref;
	}

	protected UnaryExpression newUnaryExpression(ISimpleExpression operand, UnaryOperator operator) {
		UnaryExpression unaryExpression = new UnaryExpression();
		unaryExpression.setOperand(operand);
		unaryExpression.setOperator(operator);

		return unaryExpression;
	}

	protected ISimpleExpression newReferenceExpression(IReference reference) {
		ReferenceExpression ref = new ReferenceExpression();
		ref.setReference(reference);

		return ref;
	}

	private String newMemberName(String name, TypeName type) {
		return "[" + type.getIdentifier() + "] [" + getDeclaringType().getIdentifier() + "]." + name;
	}

	protected static Assignment newAssignment(String id, IAssignableExpression expr) {
		Assignment assignment = new Assignment();
		assignment.setReference(newVariableReference(id));
		assignment.setExpression(expr);
		return assignment;
	}

	protected static Assignment newAssignment(IAssignableReference ref, IAssignableExpression expr) {
		Assignment assignment = new Assignment();
		assignment.setReference(ref);
		assignment.setExpression(expr);
		return assignment;
	}

	protected static InvocationExpression newInvokeConstructor(MethodName methodName, ISimpleExpression... parameters) {
		assertThat("methodName is not a constructor", methodName.isConstructor());
		InvocationExpression invocation = new InvocationExpression();
		invocation.setMethodName(methodName);
		invocation.setParameters(Arrays.asList(parameters));

		return invocation;
	}

	protected static ExpressionStatement newInvokeStatement(String target, MethodName methodName,
			ISimpleExpression... parameters) {
		assertThat("methodName is not static", !methodName.isStatic());
		InvocationExpression invocation = newInvokeExpression(target, methodName, parameters);
		return expressionToStatement(invocation);
	}

	protected static ExpressionStatement newInvokeStaticStatement(MethodName methodName,
			ISimpleExpression... parameters) {
		assertThat("methodName is static", methodName.isStatic());
		InvocationExpression invocation = newInvokeExpression("", methodName, parameters);
		return expressionToStatement(invocation);
	}

	protected static InvocationExpression newInvokeExpression(String target, MethodName methodName,
			ISimpleExpression... parameters) {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setReference(newVariableReference(target));
		invocation.setMethodName(methodName);
		invocation.setParameters(Arrays.asList(parameters));
		return invocation;
	}

	protected static ReturnStatement newReturnStatement(ISimpleExpression expression, boolean isVoid) {
		ReturnStatement returnStatement = new ReturnStatement();
		returnStatement.setExpression(expression);
		returnStatement.setIsVoid(isVoid);
		return returnStatement;
	}

	protected static ExpressionStatement expressionToStatement(IAssignableExpression expression) {
		ExpressionStatement stmt = new ExpressionStatement();
		stmt.setExpression(expression);
		return stmt;
	}

	protected IAssignableExpression newCastExpression(TypeName target, VariableReference reference) {
		CastExpression castExpression = new CastExpression();
		castExpression.setTargetType(target);
		castExpression.setReference(reference);
		castExpression.setOperator(CastOperator.Cast);

		return castExpression;
	}

	protected IAssignableExpression newComposedExpression(String... id) {
		ComposedExpression comp = new ComposedExpression();

		for (int i = 0; i < id.length; i++) {
			comp.getReferences().add(newVariableReference(id[i]));
		}

		return comp;
	}

	protected MethodReference newMethodRef(MethodName methodName, String target) {
		MethodReference methodReference = new MethodReference();
		methodReference.setMethodName(methodName);
		methodReference.setReference(newVariableReference(target));

		return methodReference;
	}

	protected BinaryExpression newBinaryExpression(ISimpleExpression leftOperand, ISimpleExpression rightOperand,
			BinaryOperator operator) {
		BinaryExpression binaryExpression = new BinaryExpression();
		binaryExpression.setLeftOperand(leftOperand);
		binaryExpression.setRightOperand(rightOperand);
		binaryExpression.setOperator(operator);

		return binaryExpression;
	}

	protected IndexAccessExpression newIndexAccessExpression(String target, ISimpleExpression... indices) {
		IndexAccessExpression indexAccessExpression = new IndexAccessExpression();
		indexAccessExpression.setReference(newVariableReference(target));
		indexAccessExpression.setIndices(Arrays.asList(indices));

		return indexAccessExpression;
	}

	protected IMethodDeclaration getFirstMethod() {
		Set<IMethodDeclaration> methods = context.getMethods();

		for (IMethodDeclaration decl : methods) {
			if (decl.getName().getName().equals("method")) {
				return decl;
			}
		}

		return (IMethodDeclaration) context.getMethods().toArray()[0];
	}

	protected IStatement getFirstStatement() {
		return getFirstMethod().getBody().get(0);
	}

	protected void assertMethod(IStatement... stmt) {
		MethodDeclaration expected = newDefaultMethodDeclaration();
		expected.setBody(Arrays.asList(stmt));

		IMethodDeclaration actual = getFirstMethod();

		assertEquals(
				"Different amount of statements:\n---------------------------\n" + expected.getBody().toString()
						+ "\n---------------------------\n" + actual.getBody().toString() + "\n",
				expected.getBody().size(), actual.getBody().size());
		assertEquals(expected, actual);
	}

	protected void assertMethod(MethodName name, IStatement... stmt) {
		MethodDeclaration expected = newDefaultMethodDeclaration();
		expected.setBody(Arrays.asList(stmt));
		expected.setName(name);

		IMethodDeclaration actual = getFirstMethod();

		assertEquals(
				"Different amount of statements:\n---------------------------\n" + expected.getBody().toString()
						+ "\n---------------------------\n" + actual.getBody().toString() + "\n",
				expected.getBody().size(), actual.getBody().size());
		assertEquals(expected, actual);
	}

	protected TypeName getDeclaringType() {
		return CsTypeName.newTypeName(packageName.toLowerCase() + "." + capitalizeString(name.getMethodName() + ", ?"));
	}

	protected <Decl> List<Decl> newList(Decl... item) {
		List<Decl> list = new ArrayList<Decl>();

		for (Decl decl : item) {
			list.add(decl);
		}

		return list;
	}

	protected <Decl> Set<Decl> newSet(Decl... item) {
		Set<Decl> set = new HashSet<Decl>();

		for (Decl decl : item) {
			set.add(decl);
		}

		return set;
	}

	/*
	 * Has to be called for every new test class. Testcases need to have the
	 * name of the tested compilationunit and the testclass must be named after
	 * the package of the compilationunits.
	 */
	@Before
	public void updateContext() {
		String cu = capitalizeString(name.getMethodName());
		String qualifiedName = packageName.toLowerCase() + ";" + cu + ".java";

		AstParser parser = new AstParser(projectName, qualifiedName);
		context = parser.getContext();
	}

	protected String capitalizeString(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}

	protected ParameterName constructParameterName(TypeName type, String id) {
		String name = "[" + type.getIdentifier() + "] " + id;

		return CsParameterName.newParameterName(name);
	}
}
