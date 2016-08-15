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
import static cc.kave.commons.model.ssts.impl.SSTUtil.expr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.invocationStatement;
import static cc.kave.commons.model.ssts.impl.SSTUtil.propertyReference;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.returnStatement;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.references.MethodReference;

public class PhantomClassVisitorTest extends PhantomClassVisitorBaseTest {

	@Before
	public void setUp() {
		sut = new PhantomClassVisitor();
	}

	@Test
	public void createsEmptySSTOnVariableDeclaration() {
		ISST sst = defaultSST(declare("someVariable", type("T1")));

		assertEmptySSTs(sst, type("T1"));
	}

	@Test
	public void createsEmptySSTForValueTypeOnFieldDeclaration() {
		SST sst = new SST();
		sst.getFields().add(fieldDecl(field(type("int"), type("T1"), "f1")));

		assertEmptySSTs(sst, type("int"));
	}

	@Test
	public void createsEmptySSTForValueTypeOnPropertyDeclaration() {
		SST sst = new SST();
		sst.getProperties().add(propertyDecl(PropertyName.newPropertyName("get set [PropertyType,P1] [T1,P1].P")));

		assertEmptySSTs(sst, type("PropertyType"));
	}

	@Test
	public void createsEmptySSTForReturnTypeOnMethodDeclaration() {
		SST sst = new SST();
		sst.setEnclosingType(type("T1"));
		sst.getMethods().add(methodDecl(method(type("ReturnType"), type("T1"), "m1")));

		assertEmptySSTs(sst, type("ReturnType"));
	}

	@Test
	public void createsEmptySSTForMethodParametersOnMethodDeclaration() {
		SST sst = new SST();
		sst.setEnclosingType(type("T1"));
		sst.getMethods().add(methodDecl(method(voidType, type("T1"), "m1", parameter(type("T2"), "p1"))));

		assertEmptySSTs(sst, type("T2"));
	}

	@Test
	public void addsFieldDeclarationOnFieldReference() {
		SST sst = defaultSST(
				assign(varRef("someVariable"), refExpr(fieldRef("other", field(type("int"), type("T1"), "f1")))));

		assertGeneratedFieldsInSST(sst, type("T1"), field(type("int"), type("T1"), "f1"));
	}

	@Test
	public void handleGenericsForFieldDeclarations() {
		SST sst = defaultSST(assign(varRef("someVariable"),
				refExpr(fieldRef("other",
						field(type("System.Collections.Dictionary`2[[TKey -> Int32, P1],[TValue -> String, P1]]"),
								type("T1"), "f1")))));

		assertGeneratedFieldsInSST(sst, type("T1"),
				field(type("System.Collections.Dictionary`2[[TKey],[TValue]]"), type("T1"), "f1"));
	}

	@Test
	public void addsPropertyDeclarationOnPropertyReference() {
		SST sst = defaultSST(assign(varRef("someVariable"),
				refExpr(propertyReference(varRef("other"), "get set [PropertyType,P] [T1,P1].P"))));

		assertGeneratedPropertiesInSST(sst, type("T1"),
				PropertyName.newPropertyName("get set [PropertyType,P] [T1,P1].P"));
	}

	@Test
	public void handleGenericsForPropertyDeclaration() {
		SST sst = defaultSST(assign(varRef("someVariable"), refExpr(propertyReference(varRef("other"),
				"get set [System.Collections.Dictionary`2[[TKey -> Int32, P1],[TValue -> String, P1]],P] [T1,P1].P"))));

		assertGeneratedPropertiesInSST(sst, type("T1"),
				PropertyName.newPropertyName("get set [System.Collections.Dictionary`2[[TKey],[TValue]],P] [T1,P1].P"));
	}

	@Test
	public void addsMethodDeclarationOnInvocation_NoReturnType() {
		SST sst = defaultSST(invocationStatement(method(voidType, type("T1"), "m1")));

		assertGeneratedMethodsInSST(sst, type("T1"), methodDecl(method(voidType, type("T1"), "m1")));
	}

	@Test
	public void addsMethodDeclarationOnInvocation_ReturnReferenceType() {
		SST sst = defaultSST(invocationStatement(method(type("SomeType"), type("T1"), "m1")));

		assertGeneratedMethodsInSST(sst, type("T1"),
				methodDecl(method(type("SomeType"), type("T1"), "m1"), returnStatement(constant("null"))));
	}

	@Test
	public void addsMethodDeclarationOnInvocation_ReturnValueType() {
		SST sst = defaultSST(invocationStatement(method(type("System.Int32"), type("T1"), "m1")));

		assertGeneratedMethodsInSST(sst, type("T1"),
				methodDecl(method(type("System.Int32"), type("T1"), "m1"), returnStatement(constant("0"))));
	}

	@Test
	public void addsMethodDeclarationOnInvocation_DeclaringTypeAndReceiverType() {
		SST sst = defaultSST(declare("foo", type("T1")),
				invocationStatement("foo", method(type("SomeType"), type("SuperT1"), "m1")));

		assertGeneratedSSTs(sst,
				createSSTWithMethods(type("T1"),
						methodDecl(method(type("SomeType"), type("SuperT1"), "m1"), returnStatement(constant("null")))),
				createSSTWithMethods(type("SuperT1"), methodDecl(method(type("SomeType"), type("SuperT1"), "m1"),
						returnStatement(constant("null")))));
	}

	@Test
	public void addsMethodDeclarationOnInvocation_SuperType() {
		SST sst = defaultSST(invocationStatement("super", method(voidType, type("SuperT1"), "m1")));

		assertGeneratedMethodsInSST(sst, type("SuperT1"), methodDecl(method(voidType, type("SuperT1"), "m1")));
	}

	@Test
	public void addsMethodParametersOnInvocation() {
		SST sst = defaultSST(type("T1"),
				expr(invocation("this",
				method(voidType, type("T1"), "m1", parameter(type("T2"), "p1"), parameter(type("T3"), "p2")),
				constant("1"), constant("2"))));
		assertEmptySSTs(sst, type("T2"), type("T3"));
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
		assertEmptySSTs(sst, type("SomeException"), type("OtherException"));
	}

	@Test
	public void addsMethodDeclarationOnMethodReference() {
		MethodReference methodRef = new MethodReference();
		methodRef.setMethodName(method(type("T1"), type("T2"), "m1"));
		SST sst = defaultSST(assign(varRef("someVar"), refExpr(methodRef)));
		assertGeneratedMethodsInSST(sst, type("T2"),
				methodDecl(method(type("T1"), type("T2"), "m1"), returnStatement(constant("null"))));
	}

	@Test
	public void addsMethodParameterOnMethodReference() {
		MethodReference methodRef = new MethodReference();
		methodRef.setMethodName(method(voidType, type("T1"), "m1", parameter(type("T2"), "p1")));
		methodRef.setReference(varRef("this"));
		SST sst = defaultSST(assign(varRef("someVar"), refExpr(methodRef)));

		assertEmptySSTs(sst, type("T2"));
	}

	@Test
	public void addsMethodDeclarationOnMethodReference_DeclaringTypeAndReceiverType() {
		MethodReference methodRef = new MethodReference();
		methodRef.setMethodName(method(type("SomeType"), type("SuperT1"), "m1"));
		methodRef.setReference(varRef("foo"));
		SST sst = defaultSST(declare("foo", type("T1")),assign(varRef("someVar"), refExpr(methodRef)));

		assertGeneratedSSTs(sst,
				createSSTWithMethods(type("T1"),
						methodDecl(method(type("SomeType"), type("SuperT1"), "m1"), returnStatement(constant("null")))),
				createSSTWithMethods(type("SuperT1"), methodDecl(method(type("SomeType"), type("SuperT1"), "m1"),
						returnStatement(constant("null")))));
	}

	@Test
	public void ignoresFieldInSameClass() {
		SST sst = defaultSST(type("T1"),
				assign(varRef("someVariable"), refExpr(fieldRef("other", field(type("int"), type("T1"), "f1")))));

		assertNoSSTsGenerated(sst);
	}

	@Test
	public void ignoresMethodReferenceToSameClass() {
		MethodReference methodRef = new MethodReference();
		methodRef.setMethodName(method(voidType, type("T1"), "m1"));
		methodRef.setReference(varRef("this"));
		SST sst = defaultSST(assign(varRef("someVar"), refExpr(methodRef)));

		assertNoSSTsGenerated(sst);
	}

	@Test
	public void ignoresInvocationOnSameClass() {
		SST sst = defaultSST(type("T1"), invocationStatement(method(type("int"), type("T1"), "m1")));

		assertNoSSTsGenerated(sst);
	}

	@Test
	public void ignoresPropertyInSameClass() {
		SST sst = defaultSST(type("T1"), assign(varRef("someVariable"),
				refExpr(propertyReference(varRef("other"), "get set [PropertyType,P] [T1,P1].P"))));

		assertNoSSTsGenerated(sst);
	}

	@Test
	public void ignoresInvocationOnValueType() {
		SST sst = defaultSST(type("T1"), invocationStatement(method(voidType, type("System.Int32"), "m1")));

		assertNoSSTsGenerated(sst);
	}

	@Test
	public void ignoresJavaValueTypesInVariableDeclaration() {
		SST sst = defaultSST(declare("someVar", type("System.Int32")));

		assertNoSSTsGenerated(sst);
	}

	@Test
	public void handlesGenericsForMethods() {
		SST sst = defaultSST(
				invocationStatement("super", method(voidType,
						type("System.Collections.Dictionary`2[[TKey -> Int32, P1],[TValue -> String, P1]]"), "m1")),
				invocationStatement("super",
						method(type("System.Collections.Dictionary`2[[TKey -> Int32, P1],[TValue -> String, P1]]"),
								type("T1"), "m2")));

		assertGeneratedSSTs(sst,
				createSSTWithMethods(type("System.Collections.Dictionary`2[[TKey],[TValue]]"),
						methodDecl(method(voidType, type("System.Collections.Dictionary`2[[TKey],[TValue]]"), "m1"))),
				createSSTWithMethods(type("T1"),
						methodDecl(method(type("System.Collections.Dictionary`2[[TKey],[TValue]]"), type("T1"), "m2"),
								returnStatement(constant("null")))));
	}

}
