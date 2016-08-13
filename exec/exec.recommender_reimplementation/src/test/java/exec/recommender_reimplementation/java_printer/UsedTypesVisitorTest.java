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
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.names.IAssemblyName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.impl.SST;
import exec.recommender_reimplementation.java_printer.javaPrinterTestSuite.JavaPrintingVisitorBaseTest;

public class UsedTypesVisitorTest extends JavaPrintingVisitorBaseTest{
	
	@Test
	public void addsTypeOnVariableDeclaration() {
		SST sst = defaultSST(declare("someVariable", type("T1")));
		
		Set<ITypeName> actual = getUsedTypes(sst);
		
		assertUsedTypeSet(actual, type("T1"));
	}
	
	@Test
	public void addsFieldTypeOnFieldReference() {
		SST sst = defaultSST(assign(varRef("someVariable"),
				refExpr(fieldRef("other", field(type("int"), type("T1"), "f1")))));

		Set<ITypeName> actual = getUsedTypes(sst);
		
		assertUsedTypeSet(actual, type("T1"));
	}

	@Test
	public void addsPropertyTypeOnPropertyReference() {
		SST sst = defaultSST(assign(varRef("someVariable"),
				refExpr(propertyReference(varRef("other"), "get set [PropertyType,P] [T1,P1].P"))));

		Set<ITypeName> actual = getUsedTypes(sst);
		
		assertUsedTypeSet(actual, type("T1"));
	}

	
	@Test
	public void addsTypeOnInvocation_ReferenceType() {
		SST sst = defaultSST(invocationStatement(method(type("SomeType"), type("T1"), "m1")));

		Set<ITypeName> actual = getUsedTypes(sst);
		
		assertUsedTypeSet(actual, type("T1"));
	}

	@Test
	public void addsTypeOnInvocation_SuperType() {
		SST sst = defaultSST(invocationStatement("super", method(voidType, type("SuperT1"), "m1")));

		Set<ITypeName> actual = getUsedTypes(sst);
		
		assertUsedTypeSet(actual, type("SuperT1"));
	}
	
	@Test
	public void ignoresFieldInSameClass() {
		SST sst = defaultSST(type("T1"), assign(varRef("someVariable"),
				refExpr(fieldRef("other", field(type("int"), type("T1"), "f1")))));

		Set<ITypeName> actual = getUsedTypes(sst);

		assertTrue(actual.isEmpty());
	}
	
	@Test
	public void ignoresInvocationOnSameClass() {
		SST sst = defaultSST(type("T1"), invocationStatement(method(type("int"), type("T1"), "m1")));

		Set<ITypeName> actual = getUsedTypes(sst);

		assertTrue(actual.isEmpty());
	}

	@Test
	public void ignoresPropertyInSameClass() {
		SST sst = defaultSST(type("T1"), assign(varRef("someVariable"),
				refExpr(propertyReference(varRef("other"), "get set [PropertyType,P] [T1,P1].P"))));

		Set<ITypeName> actual = getUsedTypes(sst);

		assertTrue(actual.isEmpty());
	}

	@Test
	public void ignoresJavaValueTypesInVariableDeclaration() {
		SST sst = defaultSST(declare("someVar", type("System.Int32")));

		Set<ITypeName> actual = getUsedTypes(sst);

		assertTrue(actual.isEmpty());
	}
	
	@Test
	public void returnsAssemblies() {
		SST sst = defaultSST(invocationStatement(method(type("SomeType"), type("T1"), "m1")));
		
		UsedTypesVisitor usedTypesVisitor = new UsedTypesVisitor();
		sst.accept(usedTypesVisitor, null);
		
		Set<IAssemblyName> expected = Sets.newHashSet(type("T1").getAssembly());
		Set<IAssemblyName> actual = usedTypesVisitor.getAssemblies();
		
		assertEquals(expected, actual);
	}

	private void assertUsedTypeSet(Set<ITypeName> actual, ITypeName... types) {
		Set<ITypeName> expected = Sets.newHashSet(types);
		assertEquals(expected, actual);		
	}

	private Set<ITypeName> getUsedTypes(SST sst) {
		UsedTypesVisitor usedTypesVisitor = new UsedTypesVisitor();
		sst.accept(usedTypesVisitor, null);
		return usedTypesVisitor.getUsedTypes();
	}
}