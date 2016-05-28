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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Test;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.pointsto.analysis.types.TypeCollector;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import exec.recommender_reimplementation.frequency_recommender.TestUtil;

public class PBNAnalysisUtilTest extends PBNAnalysisBaseTest {

	@Test
	public void retrievesCorrectTypeForVariableReference() {
		assertEquals(int32Type, PBNAnalysisUtil.findTypeForVarReference(invocation, pbnAnalysisVisitor.getTypeCollector()));
	}
	
	@Test
	public void returnsMethodItselfIfNotOverriden() {
		IMethodName actual = PBNAnalysisUtil.findFirstMethodName(methodDecl.getName(), ptContext.getTypeShape());
		assertEquals(actual, methodDecl.getName());
	}

	@Test
	public void returnsFirstMethod() {
		IMethodHierarchy methodHierarchy = ptContext.getTypeShape().getMethodHierarchies().stream().findAny().get();
		IMethodName methodName = TestUtil.method3;
		methodHierarchy.setSuper(methodName);
		
		IMethodName actual = PBNAnalysisUtil.findFirstMethodName(methodDecl.getName(), ptContext.getTypeShape());
		assertEquals(methodName, actual);
	}
	
	@Test
	public void createsTypeListFromParameters() {
		VariableDeclaration varDeclA = new VariableDeclaration();
		varDeclA.setType(builder.getStringType());
		VariableReference varRefA = new VariableReference();
		varRefA.setIdentifier("fooA");
		varDeclA.setReference(varRefA);
			
		VariableDeclaration varDeclB = new VariableDeclaration();
		varDeclB.setType(builder.getInt32Type());
		VariableReference varRefB = new VariableReference();
		varRefB.setIdentifier("fooB");
		varDeclB.setReference(varRefB);
		
		VariableDeclaration varDeclC= new VariableDeclaration();
		varDeclC.setType(builder.getBooleanType());
		VariableReference varRefC = new VariableReference();
		varRefC.setIdentifier("fooC");
		varDeclC.setReference(varRefC);
				
		InvocationExpression someInvocation = new InvocationExpression();
		someInvocation.setReference(varRef);
		someInvocation.setMethodName(TestUtil.method3);
		someInvocation.setParameters(Lists.newArrayList(SSTUtil.refExpr(varRefA),SSTUtil.refExpr(varRefB),SSTUtil.refExpr(varRefC)));
		
		SST someSST = builder.createEmptySST(enclosingType);
		someSST.setMethods(Sets.newHashSet(SSTUtil.declareMethod(varDeclA, varDeclB, varDeclC, SSTUtil.expr(someInvocation))));
		
		TypeCollector typeCollector = new TypeCollector(builder.createContext(someSST));
		List<ITypeName> actualTypes = PBNAnalysisUtil.createTypeListFromParameters(someInvocation.getParameters(), typeCollector);
		
		List<ITypeName> expectedTypes = Lists.newArrayList(builder.getStringType(), builder.getInt32Type(), builder.getBooleanType());
		
		assertThat(actualTypes, Matchers.is(expectedTypes));
	}
	
	@Test
	public void detectsCallToSuperClass() {
		IMethodName methodName = TestUtil.method3;
		
		IInvocationExpression superInvocation = SSTUtil.invocationExpression("this", methodName);
		
		assertTrue(PBNAnalysisUtil.isCallToSuperClass(superInvocation, sst));
	}
	
	@Test
	public void callToMethodInClassReturnsFalse() {	
		IInvocationExpression someInvocation = SSTUtil.invocationExpression("this", methodDecl.getName());
		
		assertFalse(PBNAnalysisUtil.isCallToSuperClass(someInvocation, sst));
	}

	@Test
	public void callToOtherClassMethodReturnsFalse() {
		IMethodName methodName = TestUtil.method3;
		
		IInvocationExpression someInvocation = SSTUtil.invocationExpression("fooBar", methodName);
		
		assertFalse(PBNAnalysisUtil.isCallToSuperClass(someInvocation, sst));
	}
	
	@Test
	public void detectCallToEntryPoint() {	
		assertTrue(PBNAnalysisUtil.isMethodCallToEntryPoint(methodDecl.getName(), sst));
	}

	@Test
	public void callToOtherMethodReturnsFalse() {
		IMethodName methodName = TestUtil.method3;
		assertFalse(PBNAnalysisUtil.isMethodCallToEntryPoint(methodName, sst));
	}
	
	@Test
	public void getsListOfAssignments() {
		IAssignment someAssignment = SSTUtil.assign(SSTUtil.variableReference("foo"), SSTUtil.invocationExpression("this", TestUtil.method2)); 
		IAssignment someOtherAssignment = SSTUtil.assign(SSTUtil.variableReference("bar"), SSTUtil.constant("someValue")); 
		List<IAssignment> expectedAssignments = Lists.newArrayList(someAssignment, someOtherAssignment,assignment);
		
		methodDecl.setBody(Lists.newArrayList(someAssignment, someOtherAssignment, assignment));
		
		assertThat(PBNAnalysisUtil.getAssignmentList(methodDecl.getBody()), Matchers.is(expectedAssignments));
	}
	
	@Test
	public void returnsIndexOfParameter() {
		assertEquals(0, PBNAnalysisUtil.getIndexOfParameter(invocation.getParameters(), builder.getStringType(), pbnAnalysisVisitor.getTypeCollector()));
	}
	
	@Test
	public void returnsNegativeIndexWhenParameterNotFound() {
		assertEquals(-1, PBNAnalysisUtil.getIndexOfParameter(invocation.getParameters(), builder.getInt32Type(), pbnAnalysisVisitor.getTypeCollector()));

	}
	
	@Test
	public void returnsParameterIndexInEntryPointForReceiver() {
		IInvocationExpression someInvocation = SSTUtil.invocationExpression("b", TestUtil.method1);
		
		assertEquals(0, PBNAnalysisUtil.getParameterIndexInEntryPoint(someInvocation, -1, methodDecl));
	}
	
	@Test
	public void returnsParameterIndexInEntryPointForParameter() {
		IInvocationExpression someInvocation = SSTUtil.invocationExpr(TestUtil.method1, SSTUtil.referenceExprToVariable("b"));
		IMethodDeclaration someMethodDecl = SSTUtil.declareMethod(MethodName.newMethodName("[System.Void, mscorlib, 4.0.0.0] [SSTDiff.Util.StringSimilarity, SSTDiff].CompareStrings([" + int32Type.getIdentifier() + "] c,[" + stringType.getIdentifier() + "] b)"), true, SSTUtil.expr(someInvocation));
		
		assertEquals(1, PBNAnalysisUtil.getParameterIndexInEntryPoint(someInvocation, 0, someMethodDecl));
	}
	
	@Test
	public void returnsExpressionStatementForInvocation() {
		assertThat(PBNAnalysisUtil.getStatementParentForExpression(invocation, pbnAnalysisVisitor.getSSTNodeHierarchy()), Matchers.is(exprStatement));
	}
	
	@Test
	public void returnsTrueForSimilarUsages() {
		Query queryA = new Query();
		queryA.setType(convert(int32Type));
		queryA.setClassContext(convert(enclosingType));
		queryA.setMethodContext(convert(methodDecl.getName()));
		queryA.setDefinition(DefinitionSites.createDefinitionByField(convert(fieldDecl.getName())));
		HashSet<CallSite> callSiteSet = Sets.newHashSet(
				CallSites.createReceiverCallSite(convert(invocation.getMethodName())),
				CallSites.createReceiverCallSite(convert(invocation2.getMethodName())));
		
		queryA.setAllCallsites(callSiteSet);
		
		Query queryB = new Query();
		queryB.setType(convert(int32Type));
		queryB.setClassContext(convert(enclosingType));
		queryB.setMethodContext(convert(methodDecl.getName()));
		queryB.setDefinition(DefinitionSites.createDefinitionByField(convert(fieldDecl.getName())));
		HashSet<CallSite> callSiteSetB = Sets.newHashSet(
				CallSites.createReceiverCallSite(convert(invocation.getMethodName())),
				CallSites.createReceiverCallSite(convert(invocation2.getMethodName())));
		
		queryB.setAllCallsites(callSiteSetB);
		
		assertTrue(PBNAnalysisUtil.similarUsage(queryA, queryB));
	}
	
	@Test
	public void returnsFalseForDifferentUsages() {
		Query queryA = new Query();
		queryA.setType(convert(int32Type));
		queryA.setClassContext(convert(enclosingType));
		queryA.setMethodContext(convert(methodDecl.getName()));
		queryA.setDefinition(DefinitionSites.createDefinitionByField(convert(fieldDecl.getName())));
		HashSet<CallSite> callSiteSet = Sets.newHashSet(
				CallSites.createReceiverCallSite(convert(invocation.getMethodName())),
				CallSites.createReceiverCallSite(convert(invocation2.getMethodName())));
		
		queryA.setAllCallsites(callSiteSet);
		
		Query queryB = new Query();
		queryB.setType(convert(stringType));
		queryB.setClassContext(convert(enclosingType));
		queryB.setMethodContext(convert(methodDecl.getName()));
		queryB.setDefinition(DefinitionSites.createDefinitionByParam(convert(methodDecl.getName()), 0));
		queryB.setAllCallsites(Sets.newHashSet(CallSites.createParameterCallSite(convert(invocation.getMethodName()),0)));
		
		assertFalse(PBNAnalysisUtil.similarUsage(queryA, queryB));
	}
	
	@Test
	public void returnsSimilarUsageFromUsageList() {
		Query queryA = new Query();
		queryA.setType(convert(int32Type));
		queryA.setClassContext(convert(enclosingType));
		queryA.setMethodContext(convert(methodDecl.getName()));
		queryA.setDefinition(DefinitionSites.createDefinitionByField(convert(fieldDecl.getName())));
		HashSet<CallSite> callSiteSet = Sets.newHashSet(
				CallSites.createReceiverCallSite(convert(invocation.getMethodName())),
				CallSites.createReceiverCallSite(convert(invocation2.getMethodName())));
		
		queryA.setAllCallsites(callSiteSet);
		
		Query similarUsage = new Query();
		similarUsage.setType(convert(int32Type));
		similarUsage.setClassContext(convert(enclosingType));
		similarUsage.setMethodContext(convert(methodDecl.getName()));
		similarUsage.setDefinition(DefinitionSites.createDefinitionByField(convert(fieldDecl.getName())));
		HashSet<CallSite> callSiteSetB = Sets.newHashSet(
				CallSites.createReceiverCallSite(convert(invocation.getMethodName())),
				CallSites.createReceiverCallSite(convert(invocation2.getMethodName())));
		
		similarUsage.setAllCallsites(callSiteSetB);
		
		Query queryB = new Query();
		queryB.setType(convert(stringType));
		queryB.setClassContext(convert(enclosingType));
		queryB.setMethodContext(convert(methodDecl.getName()));
		queryB.setDefinition(DefinitionSites.createDefinitionByParam(convert(methodDecl.getName()), 0));
		queryB.setAllCallsites(Sets.newHashSet(CallSites.createParameterCallSite(convert(invocation.getMethodName()),0)));
		
		List<Usage> usageList = Lists.newArrayList(queryA, queryB);
		
		Optional<Usage> actualUsage = PBNAnalysisUtil.usageListContainsSimilarUsage(usageList, similarUsage);
		
		assertTrue(actualUsage.isPresent());
		assertThat(actualUsage.get(), Matchers.is(similarUsage));
	}
}
