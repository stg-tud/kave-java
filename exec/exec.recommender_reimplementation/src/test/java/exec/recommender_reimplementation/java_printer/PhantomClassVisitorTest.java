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
package exec.recommender_reimplementation.java_printer;

import static cc.kave.commons.model.ssts.impl.SSTUtil.*;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;

public class PhantomClassVisitorTest extends PhantomClassVisitorBaseTest {

	@Before
	public void setUp() {
		sut = new PhantomClassVisitor();
	}

	@Test
	public void createsEmptySSTOnVariableDeclaration() {
		SST sst = new SST();
		IMethodDeclaration methodDecl = declareMethod(declare("someVariable", type("T1")));
		sst.getMethods().add(methodDecl);

		sst.accept(sut, null);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(type("T1"));
		expected.put(type("T1"), expectedSST);

		assertEquals(expected, sut.getPhantomSSTs());
	}

	@Test
	public void addsFieldDeclarationOnFieldReference() {
		SST sst = new SST();
		IMethodDeclaration methodDecl = declareMethod(assign(varRef("someVariable"),
				refExpr(fieldRef("other", field(type("int"), type("T1"), "f1")))));
		sst.getMethods().add(methodDecl);

		sst.accept(sut, null);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(type("T1"));
		expectedSST.getFields().add(fieldDecl(field(type("int"), type("T1"), "f1")));

		expected.put(type("T1"), expectedSST);

		assertEquals(expected, sut.getPhantomSSTs());
	}

	@Test
	public void addsPropertyDeclarationOnPropertyReference() {
		SST sst = new SST();
		IMethodDeclaration methodDecl = declareMethod(assign(varRef("someVariable"),
				refExpr(propertyReference(varRef("other"), "get set [PropertyType,P] [T1,P1].P"))));
		sst.getMethods().add(methodDecl);

		sst.accept(sut, null);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(type("T1"));
		expectedSST.getProperties().add(
				propertyDecl(PropertyName.newPropertyName("get set [PropertyType,P] [T1,P1].P")));

		expected.put(type("T1"), expectedSST);

		assertEquals(expected, sut.getPhantomSSTs());
	}

	@Test
	public void addsMethodDeclarationOnInvocation_NoReturnType() {
		SST sst = new SST();
		IMethodDeclaration methodDecl = declareMethod(invocationStatement(method(voidType, type("T1"), "m1")));
		sst.getMethods().add(methodDecl);

		sst.accept(sut, null);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(type("T1"));
		expectedSST.getMethods().add(methodDecl(method(voidType, type("T1"), "m1")));

		expected.put(type("T1"), expectedSST);

		assertEquals(expected, sut.getPhantomSSTs());
	}
	
	@Test
	public void addsMethodDeclarationOnInvocation_ReferenceType() {
		SST sst = new SST();
		IMethodDeclaration methodDecl = declareMethod(invocationStatement(method(type("SomeType"), type("T1"), "m1")));
		sst.getMethods().add(methodDecl);

		sst.accept(sut, null);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(type("T1"));
		expectedSST.getMethods().add(methodDecl(method(type("SomeType"), type("T1"), "m1"), returnStatement(constant("null"))));
		
		expected.put(type("T1"), expectedSST);

		assertEquals(expected, sut.getPhantomSSTs());
	}
	
	@Test
	public void addsMethodDeclarationOnInvocation_ValueType() {
		SST sst = new SST();
		IMethodDeclaration methodDecl = declareMethod(invocationStatement(method(type("System.Int32"), type("T1"), "m1")));
		sst.getMethods().add(methodDecl);

		sst.accept(sut, null);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(type("T1"));
		expectedSST.getMethods().add(methodDecl(method(type("System.Int32"), type("T1"), "m1"), returnStatement(constant("0"))));
		
		expected.put(type("T1"), expectedSST);

		assertEquals(expected, sut.getPhantomSSTs());
	}

	@Test
	public void ignoresFieldInSameClass() {
		SST sst = new SST();
		sst.setEnclosingType(type("T1"));
		IMethodDeclaration methodDecl = declareMethod(assign(varRef("someVariable"),
				refExpr(fieldRef("other", field(type("int"), type("T1"), "f1")))));
		sst.getMethods().add(methodDecl);

		sst.accept(sut, null);

		assertTrue(sut.getPhantomSSTs().isEmpty());
	}
	
	@Test
	public void ignoresInvocationOnSameClass() {
		SST sst = new SST();
		sst.setEnclosingType(type("T1"));
		IMethodDeclaration methodDecl = declareMethod(invocationStatement(method(type("int"), type("T1"), "m1")));
		sst.getMethods().add(methodDecl);

		sst.accept(sut, null);

		assertTrue(sut.getPhantomSSTs().isEmpty());
	}

	@Test
	public void ignoresPropertyInSameClass() {
		SST sst = new SST();
		sst.setEnclosingType(type("T1"));
		IMethodDeclaration methodDecl = declareMethod(assign(varRef("someVariable"),
				refExpr(propertyReference(varRef("other"), "get set [PropertyType,P] [T1,P1].P"))));
		sst.getMethods().add(methodDecl);

		sst.accept(sut, null);

		assertTrue(sut.getPhantomSSTs().isEmpty());
	}

	@Test
	public void ignoresJavaValueTypesInVariableDeclaration() {
		SST sst = new SST();
		sst.setEnclosingType(type("T1"));
		IMethodDeclaration methodDecl = declareMethod(declare("someVar", type("System.Int32")));
		sst.getMethods().add(methodDecl);

		sst.accept(sut, null);

		assertTrue(sut.getPhantomSSTs().isEmpty());
	}

}
