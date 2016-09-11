/**
 * Copyright 2016 Technische Universität Darmstadt
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
package exec.recommender_reimplementation.java_transformation;

import static cc.kave.commons.model.ssts.impl.SSTUtil.declare;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class JavaTypeTransformerTest extends JavaTransformationBaseTest {

	@Test
	public void doesNotTransformNonArray_TypeParameter() {
		IVariableDeclaration varDecl = declare("someVar", type("Object"));
		IVariableDeclaration expected = declare("someVar", type("Object"));
		assertTransformation(varDecl, expected);
	}

	@Test
	public void transformsArrayTypeInVariableDeclaration() {
		IVariableDeclaration varDecl = declare("someVar", type("Object[]"));
		IVariableDeclaration expected = declare("someVar", type("Object$Array"));
		assertTransformation(varDecl, expected);
	}

	@Test
	public void transformsArrayTypesInFieldDeclaration() {
		IFieldDeclaration fieldDecl = fieldDecl(field(type("Object[]"), type("Object[]"), "f"));
		IFieldDeclaration expected = fieldDecl(field(type("Object$Array"), type("Object$Array"), "f"));
		assertTransformation(fieldDecl, expected);
	}

	@Test
	public void transformsArrayTypesInFieldReference() {
		IFieldReference fieldRef = fieldRef("ref", field(type("Object[]"), type("Object[]"), "f"));
		IFieldReference expected = fieldRef("ref", field(type("Object$Array"), type("Object$Array"), "f"));
		assertTransformation(fieldRef, expected);
	}

	@Test
	public void transformsArrayTypesInMethodDeclaration() {
		IMethodName methodName = method(type("Object[]"), type("Object[]"), "m1", parameter(type("Object[]"), "p1"),
				parameter(type("Object[]"), "p2"));
		IMethodName expectedName = method(type("Object$Array"), type("Object$Array"), "m1",
				parameter(type("Object$Array"), "p1"), parameter(type("Object$Array"), "p2"));
		IMethodDeclaration methodDecl = methodDecl(methodName);
		IMethodDeclaration expected = methodDecl(expectedName);
		assertTransformation(methodDecl, expected);
	}

	
	@Test
	public void transformsArrayTypesInInvocation() {
		IMethodName methodName = method(type("Object[]"), type("Object[]"), "m1", parameter(type("Object[]"), "p1"),
				parameter(type("Object[]"), "p2"));
		IMethodName expectedName = method(type("Object$Array"), type("Object$Array"), "m1",
				parameter(type("Object$Array"), "p1"), parameter(type("Object$Array"), "p2"));
		IInvocationExpression invocation = invocation("", methodName);
		IInvocationExpression expected = invocation("", expectedName);
		assertTransformation(invocation, expected);
	}

	@Test
	public void transformsArrayTypesInMethodReference() {
		IMethodName methodName = method(type("Object[]"), type("Object[]"), "m1", parameter(type("Object[]"), "p1"),
				parameter(type("Object[]"), "p2"));
		IMethodName expectedName = method(type("Object$Array"), type("Object$Array"), "m1",
				parameter(type("Object$Array"), "p1"), parameter(type("Object$Array"), "p2"));
		MethodReference methodReference = new MethodReference();
		methodReference.setMethodName(methodName);
		MethodReference expected = new MethodReference();
		expected.setMethodName(expectedName);
		assertTransformation(methodReference, expected);
	}

	@Test
	public void transformsTypeParameterInVariableDeclaration() {
		IVariableDeclaration varDecl = declare("someVar", Names.newType("T"));
		IVariableDeclaration expected = declare("someVar", Names.newType("p:object"));
		assertTransformation(varDecl, expected);
	}

	@Test
	public void transformsTypeParameterInFieldDeclaration() {
		IFieldDeclaration fieldDecl = fieldDecl(field(Names.newType("T"), Names.newType("T"), "f"));
		IFieldDeclaration expected = fieldDecl(field(Names.newType("p:object"), Names.newType("p:object"), "f"));
		assertTransformation(fieldDecl, expected);
	}

	@Test
	public void transformsTypeParameterInFieldReference() {
		IFieldReference fieldRef = fieldRef("ref", field(Names.newType("T"), Names.newType("T"), "f"));
		IFieldReference expected = fieldRef("ref", field(Names.newType("p:object"), Names.newType("p:object"), "f"));
		assertTransformation(fieldRef, expected);
	}

	@Test
	public void transformsTypeParameterInMethodDeclaration() {
		IMethodName methodName = method(Names.newType("T"), Names.newType("T"), "m1", parameter(Names.newType("T"), "p1"),
				parameter(Names.newType("T"), "p2"));
		IMethodName expectedName = method(Names.newType("p:object"), Names.newType("p:object"), "m1", parameter(Names.newType("p:object"), "p1"),
				parameter(Names.newType("p:object"), "p2"));
		IMethodDeclaration methodDecl = methodDecl(methodName);
		IMethodDeclaration expected = methodDecl(expectedName);
		assertTransformation(methodDecl, expected);
	}

	@Test
	public void transformsTypeParameterInMethodParametersTypeHasSimiliarName() {
		IMethodName methodName = method(type("T1"), type("T1"), "m1", parameter(Names.newType("pKey"), "pKey1"),
				parameter(type("T1"), "p2"));
		IMethodName expectedName = method(type("T1"), type("T1"), "m1", parameter(Names.newType("p:object"), "pKey1"), parameter(type("T1"), "p2"));
		IMethodDeclaration methodDecl = methodDecl(methodName);
		IMethodDeclaration expected = methodDecl(expectedName);
		assertTransformation(methodDecl, expected);
	}

	@Test
	public void transformsTypeParameterInInvocation() {
		IMethodName methodName = method(Names.newType("T"), Names.newType("T"), "m1", parameter(Names.newType("T"), "p1"),
				parameter(Names.newType("T"), "p2"));
		IMethodName expectedName = method(Names.newType("p:object"), Names.newType("p:object"), "m1", parameter(Names.newType("p:object"), "p1"),
				parameter(Names.newType("p:object"), "p2"));
		IInvocationExpression invocation = invocation("", methodName);
		IInvocationExpression expected = invocation("", expectedName);
		assertTransformation(invocation, expected);
	}

	@Test
	public void transformsTypeParameterInMethodReference() {
		IMethodName methodName = method(Names.newType("T"), Names.newType("T"), "m1", parameter(Names.newType("T"), "p1"),
				parameter(Names.newType("T"), "p2"));
		IMethodName expectedName = method(Names.newType("p:object"), Names.newType("p:object"), "m1", parameter(Names.newType("p:object"), "p1"),
				parameter(Names.newType("p:object"), "p2"));
		MethodReference methodReference = new MethodReference();
		methodReference.setMethodName(methodName);
		MethodReference expected = new MethodReference();
		expected.setMethodName(expectedName);
		assertTransformation(methodReference, expected);
	}

	private void assertTransformation(ISSTNode node, ISSTNode expected) {
		ISSTNode actual = JavaTypeTransformer.transform(node);
		Assert.assertEquals(expected, actual);
	}

}
