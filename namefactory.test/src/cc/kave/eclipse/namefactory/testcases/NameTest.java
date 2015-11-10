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

package cc.kave.eclipse.namefactory.testcases;

import static cc.kave.commons.model.names.csharp.CsFieldName.newFieldName;
import static cc.kave.commons.model.names.csharp.CsLocalVariableName.newLocalVariableName;
import static cc.kave.commons.model.names.csharp.CsMethodName.newMethodName;
import static cc.kave.commons.model.names.csharp.CsNamespaceName.newNamespaceName;
import static cc.kave.commons.model.names.csharp.CsParameterName.newParameterName;
import static cc.kave.commons.model.names.csharp.CsTypeName.newTypeName;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.Test;

import cc.kave.commons.model.names.LocalVariableName;
import cc.kave.commons.model.names.Name;
import cc.kave.eclipse.namefactory.NodeFactory;
import cc.kave.eclipse.namefactory.NodeNameFinder;

public class NameTest extends BaseNameTest {

	@Test
	public void methodDeclaration() {
		ASTNode node = findMethodDeclaration("example.classes;TestClass;method");
		Name expected = newMethodName("[%int, rt.jar, 1.8] [example.classes.TestClass, ?].method()");
		assertName(expected, node);
	}

	@Test
	public void methodDeclarationWithSignature() {
		ASTNode node = findMethodDeclaration("example.classes;TestClass;method(int a)");
		Name expected = newMethodName(
				"[%int, rt.jar, 1.8] [example.classes.TestClass, ?].method([%int, rt.jar, 1.8] a)");
		assertName(expected, node);
	}

	@Test
	public void overriddenMethodDeclaration() {
		ASTNode node = findMethodDeclaration("example.classes;TestClass;equals");
		Name expected = newMethodName(
				"[%boolean, rt.jar, 1.8] [example.classes.TestClass, ?].equals([java.lang.Object, rt.jar, 1.8] obj)");
		assertName(expected, node);
	}

	@Test
	public void fieldDeclaration() {
		ASTNode node = findField("example.classes;TestClass;intTest");
		Name expected = newFieldName("[%int, rt.jar, 1.8] [example.classes.TestClass, ?].intTest");
		assertName(expected, node);
	}

	@Test
	public void packageDeclaration() {
		ASTNode node = findPackage("example.classes;TestClass");
		Name expected = newNamespaceName("example.classes");
		assertName(expected, node);
	}

	@Test
	public void importDeclaration() {
		ASTNode node = findImport("example.classes;TestClass;ArrayList");
		Name expected = newTypeName("java.util.ArrayList, rt.jar, 1.8");
		assertName(expected, node);
	}

	@Test
	public void variableDeclaration() {
		ASTNode node = findVariable("example.classes;TestClass;localVariable");
		Name expected = newLocalVariableName("[%int, rt.jar, 1.8] localVariable");
		assertName(expected, node);
	}

	@Test
	public void methodInvocation() {
		ASTNode node = findMethodInvocation("example.classes;TestClass;accessMethod");
		Name expected = newMethodName("[%void, rt.jar, 1.8] [example.classes.TestClass, ?].accessMethod()");
		assertName(expected, node);
	}

	@Test
	public void superMethodInvocation() {
		ASTNode node = findMethodInvocation("example.classes;TestClass;equals");
		Name expected = newMethodName(
				"[%boolean, rt.jar, 1.8] [example.classes.TestSuperClass, ?].equals([java.lang.Object, rt.jar, 1.8] ?)");
		assertName(expected, node);
	}

	@Test
	public void enumType() {
		ASTNode node = findVariable("example.classes;TestClass;enumType");
		LocalVariableName expected = (LocalVariableName) NodeFactory.createNodeName(node);
		assertTrue(expected.getValueType().isEnumType());
	}

	@Test
	public void interfaceType() {
		ASTNode node = findVariable("example.classes;TestClass;interfaceType");
		LocalVariableName expected = (LocalVariableName) NodeFactory.createNodeName(node);
		assertTrue(expected.getValueType().isInterfaceType());
	}

	@Test
	public void arrayType() {
		ASTNode node = findVariable("example.classes;TestClass;array");
		LocalVariableName expected = (LocalVariableName) NodeFactory.createNodeName(node);
		assertTrue(expected.getValueType().isArrayType());
	}

	@Test
	public void qualifiedName() {
		ASTNode node = findQualifiedName("example.classes;TestClass;FRIDAY");
		Name expected = newFieldName("[e: example.classes.TestEnum, ?] [example.classes.TestClass, ?] FRIDAY");
		assertName(expected, node);
	}

	@Test
	public void staticMethod() {
		ASTNode node = findMethodDeclaration("example.classes;TestClass;staticMethod");
		Name expected = newMethodName("static [%void, rt.jar, 1.8] [example.classes.TestClass, ?].staticMethod()");
		assertName(expected, node);
	}

	@Test
	public void superOverride() {
		ASTNode node = findMethodDeclaration("example.classes;TestClass;equals");
		Name expected = newMethodName(
				"[%boolean, rt.jar, 1.8] [example.classes.TestSuperClass, ?].equals([java.lang.Object, rt.jar, 1.8] ?)");
		assertEquals(expected, NodeNameFinder.getSuperMethodName(node));
	}

	@Test
	public void firstOverride() {
		ASTNode node = findMethodDeclaration("example.classes;TestClass;equals");
		Name expected = newMethodName(
				"[%boolean, rt.jar, 1.8] [java.lang.Object, rt.jar, 1.8].equals([java.lang.Object, rt.jar, 1.8] ?)");
		assertEquals(expected, NodeNameFinder.getFirstMethodName(node));
	}

	@Test
	public void returnType() {
		ASTNode node = findMethodDeclaration("example.classes;TestClass;returnsObject");
		Name expected = newTypeName("java.lang.String, rt.jar, 1.8");
		assertEquals(expected, NodeNameFinder.getReturnType(node));
	}

	@Test
	public void multipleParameters() {
		ASTNode node = findMethodDeclaration("example.classes;TestClass;parametersMethod");
		Name expected;

		expected = newParameterName("[%int, rt.jar, 1.8] a");
		assertEquals(expected, NodeNameFinder.getParameterFromMethod(node, 0));

		expected = newParameterName("[java.lang.String, rt.jar, 1.8] b");
		assertEquals(expected, NodeNameFinder.getParameterFromMethod(node, 1));

		expected = newParameterName("params [java.lang.String[], ?] c");
		assertEquals(expected, NodeNameFinder.getParameterFromMethod(node, 2));
	}

	@Test
	public void constructor() {
		ASTNode node = findMethodDeclaration("example.classes;TestClass;TestClass");
		Name expected = newMethodName("[example.classes.TestClass, ?] [example.classes.TestClass, ?]..ctor()");
		assertName(expected, node);
	}
	
	@Test
	public void defaultPackage(){
		ASTNode node = findField(";PackageTest;a");
		Name expected = newFieldName("[%int, rt.jar, 1.8] [PackageTest, ?].a");
		assertName(expected, node);
	}
}
