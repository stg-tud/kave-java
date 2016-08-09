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

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declare;
import static cc.kave.commons.model.ssts.impl.SSTUtil.invocationStatement;
import static cc.kave.commons.model.ssts.impl.SSTUtil.propertyReference;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.returnStatement;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.ssts.impl.SST;

public class PhantomClassVisitorTest extends PhantomClassVisitorBaseTest {

	@Before
	public void setUp() {
		sut = new PhantomClassVisitor();
	}

	@Test
	public void createsEmptySSTOnVariableDeclaration() {
		SST sst = defaultSST(declare("someVariable", type("T1")));
		Map<ITypeName, SST> actual = generatePhantomClasses(sst);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(type("T1"));
		expected.put(type("T1"), expectedSST);

		assertEquals(expected, actual);
	}

	@Test
	public void addsFieldDeclarationOnFieldReference() {
		SST sst = defaultSST(assign(varRef("someVariable"),
				refExpr(fieldRef("other", field(type("int"), type("T1"), "f1")))));

		Map<ITypeName, SST> actual = generatePhantomClasses(sst);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(type("T1"));
		expectedSST.getFields().add(fieldDecl(field(type("int"), type("T1"), "f1")));

		expected.put(type("T1"), expectedSST);

		assertEquals(expected, actual);
	}

	@Test
	public void handleGenericsForFieldDeclarations() {
		SST sst = defaultSST(assign(varRef("someVariable"),
				refExpr(fieldRef("other",
						field(type("System.Collections.Dictionary`2[[TKey -> Int32, P1],[TValue -> String, P1]]"),
								type("T1"), "f1")))));

		Map<ITypeName, SST> actual = generatePhantomClasses(sst);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(type("T1"));
		expectedSST.getFields()
				.add(fieldDecl(field(type("System.Collections.Dictionary`2[[TKey],[TValue]]"), type("T1"), "f1")));

		expected.put(type("T1"), expectedSST);

		assertEquals(expected, actual);
	}

	@Test
	public void addsPropertyDeclarationOnPropertyReference() {
		SST sst = defaultSST(assign(varRef("someVariable"),
				refExpr(propertyReference(varRef("other"), "get set [PropertyType,P] [T1,P1].P"))));

		Map<ITypeName, SST> actual = generatePhantomClasses(sst);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(type("T1"));
		expectedSST.getProperties().add(
				propertyDecl(PropertyName.newPropertyName("get set [PropertyType,P] [T1,P1].P")));

		expected.put(type("T1"), expectedSST);

		assertEquals(expected, actual);
	}

	@Test
	public void handleGenericsForPropertyDeclaration() {
		SST sst = defaultSST(assign(varRef("someVariable"), refExpr(propertyReference(varRef("other"),
				"get set [System.Collections.Dictionary`2[[TKey -> Int32, P1],[TValue -> String, P1]],P] [T1,P1].P"))));

		Map<ITypeName, SST> actual = generatePhantomClasses(sst);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(type("T1"));
		expectedSST.getProperties()
				.add(propertyDecl(PropertyName
						.newPropertyName("get set [System.Collections.Dictionary`2[[TKey],[TValue]],P] [T1,P1].P")));

		expected.put(type("T1"), expectedSST);

		assertEquals(expected, actual);
	}

	@Test
	public void addsMethodDeclarationOnInvocation_NoReturnType() {
		SST sst = defaultSST(invocationStatement(method(voidType, type("T1"), "m1")));

		Map<ITypeName, SST> actual = generatePhantomClasses(sst);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(type("T1"));
		expectedSST.getMethods().add(methodDecl(method(voidType, type("T1"), "m1")));

		expected.put(type("T1"), expectedSST);

		assertEquals(expected, actual);
	}
	
	@Test
	public void addsMethodDeclarationOnInvocation_ReturnReferenceType() {
		SST sst = defaultSST(invocationStatement(method(type("SomeType"), type("T1"), "m1")));

		Map<ITypeName, SST> actual = generatePhantomClasses(sst);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(type("T1"));
		expectedSST.getMethods().add(methodDecl(method(type("SomeType"), type("T1"), "m1"), returnStatement(constant("null"))));
		
		expected.put(type("T1"), expectedSST);

		assertEquals(expected, actual);
	}
	
	@Test
	public void addsMethodDeclarationOnInvocation_ReturnValueType() {
		SST sst = defaultSST(invocationStatement(method(type("System.Int32"), type("T1"), "m1")));

		Map<ITypeName, SST> actual = generatePhantomClasses(sst);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(type("T1"));
		expectedSST.getMethods().add(methodDecl(method(type("System.Int32"), type("T1"), "m1"), returnStatement(constant("0"))));
		
		expected.put(type("T1"), expectedSST);

		assertEquals(expected, actual);
	}
	
	@Test
	public void addsMethodDeclarationOnInvocation_DeclaringTypeAndReceiverType() {
		SST sst = defaultSST(declare("foo", type("T1")), invocationStatement("foo", method(type("SomeType"), type("SuperT1"), "m1")));
	
		Map<ITypeName, SST> actual = generatePhantomClasses(sst);
	
		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(type("T1"));
		expectedSST.getMethods().add(methodDecl(method(type("SomeType"), type("SuperT1"), "m1"), returnStatement(constant("null"))));
		
		SST expectedSuperSST = new SST();
		expectedSuperSST.setEnclosingType(type("SuperT1"));
		expectedSuperSST.getMethods().add(methodDecl(method(type("SomeType"), type("SuperT1"), "m1"), returnStatement(constant("null"))));
	
		expected.put(type("T1"), expectedSST);
		expected.put(type("SuperT1"), expectedSuperSST);
		assertEquals(expected, actual);
	}

	@Test
	public void addsMethodDeclarationOnInvocation_SuperType() {
		SST sst = defaultSST(invocationStatement("super", method(voidType, type("SuperT1"), "m1")));
	
		Map<ITypeName, SST> actual = generatePhantomClasses(sst);
	
		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSuperSST = new SST();
		expectedSuperSST.setEnclosingType(type("SuperT1"));
		expectedSuperSST.getMethods().add(methodDecl(method(voidType, type("SuperT1"), "m1")));
		expected.put(type("SuperT1"), expectedSuperSST);
	
		assertEquals(expected, actual);
	}

	@Test
	public void ignoresFieldInSameClass() {
		SST sst = defaultSST(type("T1"), assign(varRef("someVariable"),
				refExpr(fieldRef("other", field(type("int"), type("T1"), "f1")))));

		Map<ITypeName, SST> actual = generatePhantomClasses(sst);

		assertTrue(actual.isEmpty());
	}
	
	@Test
	public void ignoresInvocationOnSameClass() {
		SST sst = defaultSST(type("T1"), invocationStatement(method(type("int"), type("T1"), "m1")));

		Map<ITypeName, SST> actual = generatePhantomClasses(sst);

		assertTrue(actual.isEmpty());
	}

	@Test
	public void ignoresPropertyInSameClass() {
		SST sst = defaultSST(type("T1"), assign(varRef("someVariable"),
				refExpr(propertyReference(varRef("other"), "get set [PropertyType,P] [T1,P1].P"))));

		Map<ITypeName, SST> actual = generatePhantomClasses(sst);

		assertTrue(actual.isEmpty());
	}
	
	@Test
	public void ignoresInvocationOnValueType() {
		SST sst = defaultSST(type("T1"), invocationStatement(method(voidType, type("System.Int32"), "m1")));

		Map<ITypeName, SST> actual = generatePhantomClasses(sst);

		assertTrue(actual.isEmpty());
	}

	@Test
	public void ignoresJavaValueTypesInVariableDeclaration() {
		SST sst = defaultSST(declare("someVar", type("System.Int32")));

		Map<ITypeName, SST> actual = generatePhantomClasses(sst);

		assertTrue(actual.isEmpty());
	}

	@Test
	public void handlesGenericsForMethods() {
		SST sst = defaultSST(
				invocationStatement("super", method(voidType,
						type("System.Collections.Dictionary`2[[TKey -> Int32, P1],[TValue -> String, P1]]"), "m1")),
				invocationStatement("super",
						method(type("System.Collections.Dictionary`2[[TKey -> Int32, P1],[TValue -> String, P1]]"),
								type("T1"), "m2")));

		Map<ITypeName, SST> actual = generatePhantomClasses(sst);

		Map<ITypeName, SST> expected = Maps.newHashMap();
		SST expectedSST = new SST();
		expectedSST.setEnclosingType(type("System.Collections.Dictionary`2[[TKey],[TValue]]"));
		expectedSST.getMethods()
				.add(methodDecl(method(voidType, type("System.Collections.Dictionary`2[[TKey],[TValue]]"), "m1")));
		SST expectedSST2 = new SST();
		expectedSST2.setEnclosingType(type("T1"));
		expectedSST2.getMethods()
				.add(methodDecl(method(type("System.Collections.Dictionary`2[[TKey],[TValue]]"), type("T1"), "m2"),
						returnStatement(constant("null"))));
		expected.put(type("System.Collections.Dictionary`2[[TKey],[TValue]]"), expectedSST);
		expected.put(type("T1"), expectedSST2);

		assertEquals(expected, actual);
	}

	private Map<ITypeName, SST> generatePhantomClasses(SST sst) {
		Map<ITypeName, SST> phantomClasses = Maps.newHashMap();
		sst.accept(sut, phantomClasses);
		return phantomClasses;
	}

}
