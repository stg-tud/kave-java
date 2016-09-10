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
package exec.recommender_reimplementation.pbn;

import static cc.kave.commons.pointsto.extraction.CoReNameConverter.convert;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.booleanType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.intType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.objectType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.stringType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.pointsto.analysis.types.TypeCollector;
import cc.kave.commons.utils.SSTNodeHierarchy;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Usage;

public class PBNAnalysisUtilTest extends PBNAnalysisBaseTest {

	@Test
	public void retrievesCorrectTypeForVariableReference() {
		IInvocationExpression invocation = invoke("a",DefaultMethodContext);
		setupDefaultEnclosingMethod(varDecl("a", intType), invokeStmt(invocation));
		assertEquals(intType, PBNAnalysisUtil.findTypeForVarReference(invocation, new TypeCollector(context)));
	}
	
	@Test
	public void returnsMethodItselfIfNotOverriden() {
		setupDefaultEnclosingMethod();
		IMethodName actual = PBNAnalysisUtil.findFirstMethodName(DefaultMethodContext, context.getTypeShape());
		assertEquals(actual, DefaultMethodContext);
	}

	@Test
	public void returnsFirstMethod() {
		setupDefaultEnclosingMethod();
		IMethodHierarchy methodHierarchy = methodHierarchy(
				method(voidType, DefaultClassContext, "someMethod"), 
				method(voidType, DefaultClassContext, "someSuperMethod"));
		resetMethodHierarchies(methodHierarchy);
		
		IMethodName actual = PBNAnalysisUtil.findFirstMethodName(method(voidType, DefaultClassContext, "someMethod"), context.getTypeShape());
		
		assertEquals(method(voidType, DefaultClassContext, "someSuperMethod"), actual);
	}
	
	@Test
	public void createsTypeListFromParameters() {
		IInvocationExpression invokeWithParameters = invokeWithParameters("a", DefaultMethodContext, 
				referenceExpr(varRef("fooA")),
				referenceExpr(varRef("fooB")),
				referenceExpr(varRef("fooC")));
		
		setupDefaultEnclosingMethod(
				varDecl("fooA", stringType),
				varDecl("fooB", intType),
				varDecl("fooC", booleanType),
				invokeStmt(invokeWithParameters));
		
		List<ITypeName> actualTypes = PBNAnalysisUtil.createTypeListFromParameters(invokeWithParameters.getParameters(), new TypeCollector(context));
		
		List<ITypeName> expectedTypes = Lists.newArrayList(stringType, intType, booleanType);
		
		assertThat(actualTypes, Matchers.is(expectedTypes));
	}
	
	@Test
	public void detectsCallToSuperClass() {
		setupDefaultEnclosingMethod();
		assertTrue(PBNAnalysisUtil.isCallToSuperClass(invoke("this", method(voidType, DefaultClassContext, "M")), context.getSST()));
	}
	
	@Test
	public void callToMethodInClassReturnsFalse() {	
		setupEnclosingMethod(method(voidType, DefaultClassContext, "M"));
		assertFalse(PBNAnalysisUtil.isCallToSuperClass(invoke("this", method(voidType, DefaultClassContext, "M")), context.getSST()));
	}

	@Test
	public void callToOtherClassMethodReturnsFalse() {
		assertFalse(PBNAnalysisUtil.isCallToSuperClass(invoke("a", someMethodOnType("SomeOtherClass")), context.getSST()));
	}
	
	@Test
	public void detectCallToEntryPoint() {	
		setupEnclosingMethod(method(voidType, DefaultClassContext, "SomeEntryPoint"));
		assertTrue(PBNAnalysisUtil.isMethodCallToEntryPoint(method(voidType, DefaultClassContext, "SomeEntryPoint"), context.getSST()));
	}

	@Test
	public void callToOtherMethodReturnsFalse() {
		setupDefaultEnclosingMethod();
		assertFalse(PBNAnalysisUtil.isMethodCallToEntryPoint(method(voidType, DefaultClassContext, "SomeOtherMethod"), context.getSST()));
	}
	
	@Test
	public void getsListOfAssignments() {
		IAssignment someAssignment = assign("foo", invoke("this", method(voidType, DefaultClassContext, "M"))); 
		IAssignment someOtherAssignment = assign("bar", constant("someValue"));
		
		List<IAssignment> expectedAssignments = Lists.newArrayList(someAssignment, someOtherAssignment);
		IMethodDeclaration methodDecl = methodDecl(DefaultMethodContext, true, someAssignment, someOtherAssignment);
		
		assertThat(PBNAnalysisUtil.getAssignmentList(methodDecl), Matchers.is(expectedAssignments));
	}
	
	@Test
	public void returnsIndexOfParameter() {
		IInvocationExpression invocation = invokeWithParameters("a", DefaultMethodContext, referenceExpr(varRef("foo")));
		setupDefaultEnclosingMethod(varDecl("foo", stringType), invokeStmt(invocation));
		assertEquals(0, PBNAnalysisUtil.getIndexOfParameter(invocation.getParameters(), stringType, new TypeCollector(context)));
	}
	
	@Test
	public void returnsNegativeIndexWhenParameterNotFound() {
		IInvocationExpression invocation = invokeWithParameters("a", DefaultMethodContext, referenceExpr(varRef("bar")));
		setupDefaultEnclosingMethod(varDecl("foo", stringType), invokeStmt(invocation));
		assertEquals(-1, PBNAnalysisUtil.getIndexOfParameter(invocation.getParameters(), intType, new TypeCollector(context)));
	}
	
	@Test
	public void returnsParameterIndexInEntryPointForReceiver() {
		IInvocationExpression someInvocation = invoke("a", method(voidType, DefaultClassContext, "SomeOtherMethod"));
		IMethodDeclaration someMethodDecl = methodDecl(
				method(voidType, DefaultClassContext, "SomeMethod", parameter(intType, "a")),
				true, invokeStmt(someInvocation));
		SST sst = new SST();
		sst.setMethods(Sets.newHashSet(someMethodDecl));
		
		assertEquals(0, PBNAnalysisUtil.getParameterIndexInEntryPoint(someInvocation, -1, someMethodDecl));
	}
	
	@Test
	public void returnsParameterIndexInEntryPointForParameter() {
		IInvocationExpression someInvocation = invokeWithParameters("a", method(voidType, DefaultClassContext, "SomeOtherMethod"), referenceExpr(varRef("b")));
		IMethodDeclaration someMethodDecl = methodDecl(
				method(voidType, DefaultClassContext, "SomeMethod", parameter(intType, "a"), parameter(stringType, "b")),
				true, invokeStmt(someInvocation));
		SST sst = new SST();
		sst.setMethods(Sets.newHashSet(someMethodDecl));
		
		assertEquals(1, PBNAnalysisUtil.getParameterIndexInEntryPoint(someInvocation, 0, someMethodDecl));
	}
	
	@Test
	public void returnsExpressionStatementForInvocation() {
		IInvocationExpression invocation = invokeStatic(DefaultMethodContext);
		IExpressionStatement exprStatement = invokeStmt(invocation);
		setupDefaultEnclosingMethod(exprStatement);
		assertThat(PBNAnalysisUtil.getStatementParentForExpression(invocation, new SSTNodeHierarchy(context.getSST())), Matchers.is(exprStatement));
	}
	
	@Test
	public void returnsTrueForSimilarUsages() {
		Usage queryA = query(
				intType,
				objectType,
				method(voidType, DefaultClassContext, "entry1", parameter(stringType, "b")),
				DefinitionSites.createDefinitionByField(convert(field(intType, DefaultClassContext, "Apple"))),
				CallSites.createReceiverCallSite(convert(method(voidType, DefaultClassContext, "m1",
						parameter(objectType, "foo")))),
				CallSites.createReceiverCallSite(convert(method(voidType, DefaultClassContext, "m2"))));
		
		Usage queryB = query(
				intType,
				objectType,
				method(voidType, DefaultClassContext, "entry1", parameter(stringType, "b")),
				DefinitionSites.createDefinitionByField(convert(field(intType, DefaultClassContext, "Apple"))),
				CallSites.createReceiverCallSite(convert(method(voidType, DefaultClassContext, "m1",
						parameter(objectType, "foo")))),
				CallSites.createReceiverCallSite(convert(method(voidType, DefaultClassContext, "m2"))));
		
		assertTrue(PBNAnalysisUtil.similarUsage(queryA, queryB));
	}
	
	@Test
	public void returnsFalseForDifferentUsages() {
		Usage queryA = query(
				intType,
				objectType,
				method(voidType, DefaultClassContext, "entry1", parameter(stringType, "b")),
				DefinitionSites.createDefinitionByField(convert(field(intType, DefaultClassContext, "Apple"))),
				CallSites.createReceiverCallSite(convert(method(voidType, DefaultClassContext, "m1",
						parameter(objectType, "foo")))),
				CallSites.createReceiverCallSite(convert(method(voidType, DefaultClassContext, "m2"))));

		Usage queryB = query(
				stringType,
				objectType,
				method(voidType, DefaultClassContext, "entry1", parameter(stringType, "b")),
				DefinitionSites.createDefinitionByParam(
						convert(method(voidType, DefaultClassContext, "entry1", parameter(stringType, "b"))), 0),
				CallSites.createParameterCallSite(
						convert(method(voidType, DefaultClassContext, "m1", parameter(objectType, "foo"))), 0));
		assertFalse(PBNAnalysisUtil.similarUsage(queryA, queryB));
	}
	
	@Test
	public void returnsSimilarUsageFromUsageList() {
		Usage queryA = query(
				intType,
				objectType,
				method(voidType, DefaultClassContext, "entry1", parameter(stringType, "b")),
				DefinitionSites.createDefinitionByField(convert(field(intType, DefaultClassContext, "Apple"))),
				CallSites.createReceiverCallSite(convert(method(voidType, DefaultClassContext, "m1",
						parameter(objectType, "foo")))),
				CallSites.createReceiverCallSite(convert(method(voidType, DefaultClassContext, "m2"))));

		Usage queryB = query(
				stringType,
				objectType,
				method(voidType, DefaultClassContext, "entry1", parameter(stringType, "b")),
				DefinitionSites.createDefinitionByParam(
						convert(method(voidType, DefaultClassContext, "entry1", parameter(stringType, "b"))), 0),
				CallSites.createParameterCallSite(
						convert(method(voidType, DefaultClassContext, "m1", parameter(objectType, "foo"))), 0));
		
		Usage similarUsage = query(
				intType,
				objectType,
				method(voidType, DefaultClassContext, "entry1", parameter(stringType, "b")),
				DefinitionSites.createDefinitionByField(convert(field(intType, DefaultClassContext, "Apple"))),
				CallSites.createReceiverCallSite(convert(method(voidType, DefaultClassContext, "m1",
						parameter(objectType, "foo")))),
				CallSites.createReceiverCallSite(convert(method(voidType, DefaultClassContext, "m2"))));

		List<Usage> usageList = Lists.newArrayList(queryA, queryB);
		
		Optional<Usage> actualUsage = PBNAnalysisUtil.usageListContainsSimilarUsage(usageList, similarUsage);
		
		assertTrue(actualUsage.isPresent());
		assertThat(actualUsage.get(), Matchers.is(similarUsage));
	}
}
