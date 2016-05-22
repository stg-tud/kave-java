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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.pointsto.analysis.FieldSensitivity;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.unification.UnificationAnalysis;
import cc.kave.commons.pointsto.extraction.CoReNameConverter;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

import com.google.common.base.Verify;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import exec.recommender_reimplementation.frequency_recommender.TestUtil;
import static cc.kave.commons.pointsto.extraction.CoReNameConverter.*;

public class PBNAnalysisVisitorTest {

	public PBNAnalysisVisitor uut;
	private InvocationExpression invocation;
	private InvocationExpression invocation2;
	private ExpressionStatement exprStatement;
	private MethodDeclaration methodDecl;
	private SST sst;
	private ITypeName int32Type;
	private ITypeName stringType;
	private PointsToContext ptContext;
	private ITypeName enclosingType;
	private TestSSTBuilder builder;
	private FieldDeclaration fieldDecl;
	private VariableReference varRef;
	
	@Before
	public void contextCreation() {
		builder = new TestSSTBuilder();
		
		stringType = builder.getStringType();
		int32Type = builder.getInt32Type();

		enclosingType = stringType;
		sst = builder.createEmptySST(enclosingType);
				
		
		fieldDecl = new FieldDeclaration();
		fieldDecl.setName(FieldName.newFieldName("[" + int32Type + "] [MyClass, MyAssembly, 1.2.3.4].Apple"));
		
		IFieldReference fieldRef = builder.buildFieldReference("this", fieldDecl.getName());
		
		VariableDeclaration varDecl = new VariableDeclaration();
		varDecl.setType(int32Type);
		varRef = new VariableReference();
		varRef.setIdentifier("a");
		varDecl.setReference(varRef);
		
		VariableDeclaration varDecl2 = new VariableDeclaration();
		varDecl2.setType(builder.getStringType());
		VariableReference varRef2 = new VariableReference();
		varRef2.setIdentifier("b");
		varDecl2.setReference(varRef2);
		
		Assignment assignment = new Assignment();
		assignment.setReference(varRef);
		ReferenceExpression refExpr = new ReferenceExpression();
		refExpr.setReference(fieldRef);
		assignment.setExpression(refExpr);
		
		invocation = new InvocationExpression();
		invocation.setReference(varRef);
		invocation.setMethodName(TestUtil.method1);
		ReferenceExpression referenceExpression = new ReferenceExpression();
		referenceExpression.setReference(varRef2);
		invocation.setParameters(Lists.newArrayList(referenceExpression));
		exprStatement = new ExpressionStatement();
		exprStatement.setExpression(invocation);
		
		invocation2 = new InvocationExpression();
		invocation2.setReference(varRef);
		invocation2.setMethodName(TestUtil.method3);
		ExpressionStatement exprStatement2 = new ExpressionStatement();
		exprStatement2.setExpression(invocation2);
		
		methodDecl = new MethodDeclaration();
		methodDecl.setBody(Lists.newArrayList(varDecl, varDecl2, assignment, exprStatement, exprStatement2));
		methodDecl.setName(MethodName.newMethodName("[System.Void, mscorlib, 4.0.0.0] [SSTDiff.Util.StringSimilarity, SSTDiff].CompareStrings([" + stringType.getIdentifier() + "] b)"));
		methodDecl.setEntryPoint(true);
		
		sst.setMethods(Sets.newHashSet(methodDecl));
		sst.setFields(Sets.newHashSet(fieldDecl));
		
		PointsToAnalysis pointsToAnalysis = new UnificationAnalysis(FieldSensitivity.FULL);
		ptContext = pointsToAnalysis.compute(builder.createContext(sst));
		
		uut = new PBNAnalysisVisitor(ptContext);
	}
	
	@Test
	public void retrievesCorrectTypeForVariableReference() {
		Assert.assertEquals(convert(int32Type), uut.findTypeForVarReference(invocation));
	}
	
	@Test
	public void returnsUsageWhenAlreadyInList() {
		List<Usage> usageList = new ArrayList<>();
		Query query = new Query();
		query.setType(convert(int32Type));
		usageList.add(query);
		
		Optional<Usage> actual = uut.usageListContainsType(usageList, convert(int32Type));
		assertTrue(actual.isPresent());
	}
	
	@Test
	public void returnsEmptyOptionalWhenUsageNotPresentInList() {
		List<Usage> usageList = new ArrayList<>();
		Query query = new Query();
		query.setType(CoReNameConverter.convert(stringType));
		usageList.add(query);
		Optional<Usage> actual = uut.usageListContainsType(usageList, convert(int32Type));
		assertFalse(actual.isPresent());
	}
	
	@Test
	public void returnsEmptyOptionalWhenListEmpty() {
		List<Usage> usageList = new ArrayList<>();
		Optional<Usage> actual = uut.usageListContainsType(usageList, convert(int32Type));
		assertFalse(actual.isPresent());
	}
	
	@Test
	public void succesfullyCreatesUsageFromInvocation() {
		Query expected = new Query();
		expected.setType(convert(int32Type));
		
		Query actual = uut.createNewObjectUsage(invocation, null);
		
		Assert.assertEquals(expected,actual);
	}
	
	@Test
	public void returnsMethodItselfIfNotOverriden() {
		IMethodName actual = uut.findFirstMethodName(methodDecl.getName());
		Assert.assertEquals(actual, methodDecl.getName());
	}
	
	@Test
	public void returnsFirstMethod() {
		IMethodHierarchy methodHierarchy = ptContext.getTypeShape().getMethodHierarchies().stream().findAny().get();
		IMethodName methodName = TestUtil.method3;
		methodHierarchy.setFirst(methodName);
		
		IMethodName actual = uut.findFirstMethodName(methodDecl.getName());
		Assert.assertEquals(methodName, actual);
	}
	
	@Test
	public void returnsClassItselfNoDirectSuperType() {
		ITypeName actual = uut.findClassContext();
		
		Assert.assertEquals(enclosingType, actual);
	}
	
	@Test
	public void returnsDirectSuperClass() {
		TypeHierarchy typeHierarchy = (TypeHierarchy) ptContext.getTypeShape().getTypeHierarchy();
		TypeHierarchy extendTypeHierarchy = new TypeHierarchy();
		ITypeName superType = TypeName.newTypeName("System.Object, mscorlib");
		extendTypeHierarchy.setElement(superType);
		typeHierarchy.setExtends(extendTypeHierarchy);
		
		ITypeName actual = uut.findClassContext();
		
		Assert.assertEquals(superType, actual);
	}
	
	@Test
	public void addsReceiverCallSite() {
		Query query = new Query();
		query.setType(convert(int32Type));
		
		uut.addCallSite(query, invocation, -1);
		
		Set<CallSite> allCallsites = query.getAllCallsites();
		assertTrue(allCallsites.size() == 1);
		CallSite callSite = CallSites.createReceiverCallSite(convert(invocation.getMethodName()));
		Assert.assertThat(allCallsites, Matchers.contains(callSite));
		
	}
	
	@Test
	public void addsParameterCallSite() {
		Query query = new Query();
		query.setType(convert(stringType));
		
		uut.addCallSite(query, invocation, 0);
		
		Set<CallSite> allCallsites = query.getAllCallsites();
		assertTrue(allCallsites.size() == 1);
		CallSite callSite = CallSites.createParameterCallSite(convert(invocation.getMethodName()),0);
		Assert.assertThat(allCallsites, Matchers.contains(callSite));
	}
	
	@Test
	public void createsUsageListForExampleSST() {
		// also tests creation of Field and Parameter Definition Site
		List<Usage> usageList = new ArrayList<>();
		sst.accept(uut, usageList);
		
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
		
		Assert.assertThat(usageList, Matchers.containsInAnyOrder(queryA, queryB));
	}
	
	@Test
	public void ignoresStatementsInExceptionHandling() {
		InvocationExpression otherInvocation = Mockito.mock(InvocationExpression.class);
		otherInvocation.setReference(varRef);
		otherInvocation.setMethodName(TestUtil.method1);
		exprStatement = new ExpressionStatement();
		exprStatement.setExpression(otherInvocation);
		
		TryBlock tryBlock = new TryBlock();
		CatchBlock catchBlock = new CatchBlock();
		
		catchBlock.setBody(Lists.newArrayList(exprStatement));
		List<ICatchBlock> catchBlocks = Lists.newArrayList(catchBlock); 
		tryBlock.setCatchBlocks(catchBlocks);
		
		methodDecl.getBody().add(tryBlock);
		
		methodDecl.accept(uut, Lists.newArrayList());
		
		verify(otherInvocation, never()).accept(eq(uut), Mockito.any());
	}
	
	@Test
	public void detectsCallsToSuperClassCorrectly() {
		assertFalse(uut.isCallToSuperClass(invocation));
		
		InvocationExpression otherInvocation = new InvocationExpression();
		VariableReference otherVarRef = new VariableReference();
		otherVarRef.setIdentifier("this");
		otherInvocation.setReference(otherVarRef);
		otherInvocation.setMethodName(TestUtil.method1);
		assertTrue(uut.isCallToSuperClass(otherInvocation));
	}
	
	@Test
	public void createsConstructorDefinitionSite() {
		InvocationExpression constructorInvocation = new InvocationExpression();
		VariableReference otherVarRef = new VariableReference();
		otherVarRef.setIdentifier("c");
		constructorInvocation.setReference(otherVarRef);
		IMethodName constructorMethodName = MethodName.newMethodName("[System.String, mscore, 4.0.0.0] [System.String, mscore, 4.0.0.0]..ctor()");
		constructorInvocation.setMethodName(constructorMethodName);
		
		DefinitionSite constructorDefinitionSite = DefinitionSites.createDefinitionByConstructor(convert(constructorMethodName));
		
		DefinitionSite actual = uut.tryGetInvocationDefinition(constructorInvocation);
		
		Assert.assertThat(constructorDefinitionSite, Matchers.is(actual));
	}
	
	@Test
	public void createsReturnDefinitionSite() {
		DefinitionSite returnDefinitionSite = DefinitionSites.createDefinitionByReturn(convert(invocation.getMethodName()));
		DefinitionSite actual = uut.tryGetInvocationDefinition(invocation);
		
		Assert.assertThat(returnDefinitionSite, Matchers.is(actual));
	}
}
