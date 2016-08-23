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
import static exec.recommender_reimplementation.java_printer.UsedTypesVisitor.CONSTANT_TYPES;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.names.IAssemblyName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.DelegateTypeName;
import cc.kave.commons.model.names.csharp.EventName;
import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.declarations.DelegateDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.EventDeclaration;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import exec.recommender_reimplementation.java_printer.javaPrinterTestSuite.JavaPrintingVisitorBaseTest;

public class UsedTypesVisitorTest extends JavaPrintingVisitorBaseTest {

	@Test
	public void addsTypeOnVariableDeclaration() {
		SST sst = defaultSST(declare("someVariable", type("T1")));

		assertUsedTypesForSST(sst, type("T1"));
	}

	@Test
	public void addsEventTypeOnEventDeclaration() {
		SST sst = new SST();
		EventDeclaration eventDecl = new EventDeclaration();
		eventDecl.setName(EventName.newEventName("[T1,P1] [?].???"));
		sst.getEvents().add(eventDecl);

		assertUsedTypesForSST(sst, type("T1"));
	}

	@Test
	public void addsFieldTypeOnFieldDeclaration() {
		SST sst = new SST();
		sst.getFields().add(fieldDecl(field(type("int"), type("T1"), "f1")));

		assertUsedTypesForSST(sst, type("int"));
	}

	@Test
	public void addsPropertyTypeOnPropertyDeclaration() {
		SST sst = new SST();
		sst.getProperties().add(propertyDecl(PropertyName.newPropertyName("get set [PropertyType,P1] [T1,P1].P")));

		assertUsedTypesForSST(sst, type("PropertyType"));
	}

	@Test
	public void addsReturnTypeOnMethodDeclaration() {
		SST sst = new SST();
		sst.setEnclosingType(type("T1"));
		sst.getMethods().add(methodDecl(method(type("ReturnType"), type("T1"), "m1")));

		assertUsedTypesForSST(sst, type("ReturnType"));
	}

	@Test
	public void addsMethodParametersOnMethodDeclaration() {
		SST sst = new SST();
		sst.setEnclosingType(type("T1"));
		sst.getMethods().add(methodDecl(method(voidType, type("T1"), "m1", parameter(type("T2"), "p1"))));

		assertUsedTypesForSST(sst, type("T2"));
	}

	@Test
	public void addsDelegateParametersOnDelegateDeclaration() {
		SST sst = new SST();
		DelegateDeclaration delegateDecl = new DelegateDeclaration();
		delegateDecl.setName(
				DelegateTypeName.newDelegateTypeName("d:[?] [?].([T1,P1] p1)"));
		sst.getDelegates().add(delegateDecl);

		assertUsedTypesForSST(sst, type("T1"));
	}

	@Test
	public void addsFieldTypeOnFieldReference() {
		SST sst = defaultSST(assign(
				varRef("someVariable"),
				refExpr(fieldRef("other", field(type("int"), type("T1"), "f1")))));

		assertUsedTypesForSST(sst, type("int"), type("T1"));
	}

	@Test
	public void addsPropertyTypeOnPropertyReference() {
		SST sst = defaultSST(assign(
				varRef("someVariable"),
				refExpr(propertyReference(varRef("other"),
						"get set [PropertyType,P1] [T1,P1].P"))));

		assertUsedTypesForSST(sst, type("PropertyType"), type("T1"));
	}

	@Test
	public void addsTypeOnInvocation_ReferenceType() {
		SST sst = defaultSST(invocationStatement(method(type("SomeType"),
				type("T1"), "m1")));

		assertUsedTypesForSST(sst,type("SomeType"), type("T1"));
	}

	@Test
	public void addsTypeOnInvocation_SuperType() {
		SST sst = defaultSST(invocationStatement("super",
				method(type("SomeType"), type("SuperT1"), "m1")));

		assertUsedTypesForSST(sst, type("SomeType"), type("SuperT1"));
	}

	@Test
	public void addsTypeOfMethodParametersOnInvocation() {
		SST sst = defaultSST(invocationStatement(
				"other",
				method(type("SomeType"), type("T1"), "m1", parameter(type("T2"), "p1"),
						parameter(type("T3"), "p2"))));

		assertUsedTypesForSST(sst, type("SomeType"), type("T1"), type("T2"), type("T3"));
	}

	@Test
	public void addsTypeOfMethodParameterWhenInvokingMethodInSameClass() {
		SST sst = defaultSST(type("T1"),
				invocationStatement("this",
				method(voidType, type("T1"), "m1", parameter(type("T2"), "p1"))));

		assertUsedTypesForSST(sst, type("T2"));
	}

	@Test
	public void addsTypeOfCatchBlockParameter() {
		CatchBlock catchBlock1 = new CatchBlock();
		catchBlock1.setParameter(parameter(type("SomeException"), "e"));
		CatchBlock catchBlock2 = new CatchBlock();
		catchBlock2.setParameter(parameter(type("OtherException"), "e"));
		TryBlock tryBlock = new TryBlock();
		tryBlock.setCatchBlocks(Lists.newArrayList(catchBlock1, catchBlock2));
		SST sst = defaultSST(tryBlock);
		assertUsedTypesForSST(sst, type("SomeException"), type("OtherException"));
	}

	@Test
	public void addsTypesOnMethodReference() {
		MethodReference methodRef = new MethodReference();
		methodRef.setMethodName(method(type("T1"), type("T2"), "m1"));
		SST sst = defaultSST(assign(varRef("someVar"), refExpr(methodRef)));
		assertUsedTypesForSST(sst, type("T1"), type("T2"));
	}

	@Test
	public void addsMethodParameterOnMethodReference() {
		MethodReference methodRef = new MethodReference();
		methodRef.setMethodName(method(voidType, type("T1"), "m1",
				parameter(type("T2"), "p1")));
		methodRef.setReference(varRef("this"));
		SST sst = defaultSST(type("T1"), assign(varRef("someVar"), refExpr(methodRef)));
		
		assertUsedTypesForSST(sst, type("T2"));
	}
	
	@Test
	public void addsTypeOnEventReference() {
		EventReference eventRef = new EventReference();
		eventRef.setEventName(EventName.newEventName("[T1,P1] [T2,P1].e"));
		eventRef.setReference(varRef("other"));
		SST sst = defaultSST(assign(varRef("someVar"), refExpr(eventRef)));
		
		assertUsedTypesForSST(sst, type("T1"), type("T2"));
	}

	@Test
	public void ignoresDeclaringTypeOnEventReferenceInSameClass() {
		EventReference eventRef = new EventReference();
		eventRef.setEventName(EventName.newEventName("[T2,P1] [T1,P1].e"));
		eventRef.setReference(varRef("this"));
		SST sst = defaultSST(type("T1"), assign(varRef("someVar"), refExpr(eventRef)));
		
		assertUsedTypesForSST(sst, type("T2"));
	}
	
	@Test
	public void ignoresDeclaringTypeOnFieldInSameClass() {
		SST sst = defaultSST(
				type("T1"),
				assign(varRef("someVariable"),
						refExpr(fieldRef("other",
								field(type("int"), type("T1"), "f1")))));

		assertUsedTypesForSST(sst, type("int"));
	}

	@Test
	public void ignoresDeclaringTypeInInvocationOnSameClass() {
		SST sst = defaultSST(type("T1"),
				invocationStatement(method(type("int"), type("T1"), "m1")));
		
		assertUsedTypesForSST(sst, type("int"));
	}

	@Test
	public void ignoresDeclaringTypeOnMethodOnSameClass() {
		MethodReference methodRef = new MethodReference();
		methodRef.setMethodName(method(voidType, type("T1"), "m1"));
		methodRef.setReference(varRef("this"));
		SST sst = defaultSST(type("T1"), assign(varRef("someType"), refExpr(methodRef)));
		
		assertUsedTypesEmpty(sst);
	}

	@Test
	public void ignoresDeclaringTypeOnPropertyInSameClass() {
		SST sst = defaultSST(
				type("T1"),
				assign(varRef("someVariable"),
						refExpr(propertyReference(varRef("other"),
								"get set [PropertyType,P1] [T1,P1].P"))));

		assertUsedTypesForSST(sst, type("PropertyType"));
	}

	@Test
	public void returnsAssemblies() {
		SST sst = defaultSST(invocationStatement(method(type("SomeType"),
				type("T1"), "m1")));

		UsedTypesVisitor usedTypesVisitor = new UsedTypesVisitor();
		sst.accept(usedTypesVisitor, null);

		Set<IAssemblyName> expected = Sets.newHashSet(type("T1").getAssembly());
		for (ITypeName typeName : CONSTANT_TYPES) {
			expected.add(typeName.getAssembly());
		}
		Set<IAssemblyName> actual = usedTypesVisitor.getAssemblies();

		assertEquals(expected, actual);
	}

	private void assertUsedTypesEmpty(SST sst) {
		Set<ITypeName> actual = getUsedTypes(sst);
		Set<ITypeName> expected = Sets.newHashSet(CONSTANT_TYPES);
		assertEquals(expected, actual);
	}

	private void assertUsedTypesForSST(SST sst, ITypeName... types) {
		Set<ITypeName> actual = getUsedTypes(sst);
		Set<ITypeName> expected = Sets.newHashSet(types);
		expected.addAll(UsedTypesVisitor.CONSTANT_TYPES);
		assertEquals(expected, actual);
	}

	private Set<ITypeName> getUsedTypes(SST sst) {
		UsedTypesVisitor usedTypesVisitor = new UsedTypesVisitor();
		sst.accept(usedTypesVisitor, null);
		return usedTypesVisitor.getUsedTypes();
	}
}