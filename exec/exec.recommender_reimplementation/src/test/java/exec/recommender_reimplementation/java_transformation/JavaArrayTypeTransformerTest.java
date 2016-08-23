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

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class JavaArrayTypeTransformerTest extends JavaTransformationBaseTest {

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

	private void assertTransformation(ISSTNode node, ISSTNode expected) {
		ISSTNode actual = JavaArrayTypeTransformer.transform(node);
		Assert.assertEquals(expected, actual);
	}

}
