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
import static org.junit.Assert.*;

import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.pointsto.analysis.PointsToQueryBuilder;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;
import exec.recommender_reimplementation.frequency_recommender.TestUtil;

public class UsageContextHelperTest extends PBNAnalysisBaseTest {

	private UsageContextHelper usageContextHelper;
	
	@Before
	public void setUp() {
		usageContextHelper = new UsageContextHelper(pbnAnalysisVisitor.getTypeCollector(), ptContext, new PointsToQueryBuilder(pbnAnalysisVisitor.getTypeCollector(), pbnAnalysisVisitor.getSSTNodeHierarchy()), pbnAnalysisVisitor.getSSTNodeHierarchy());
	}
	
	@Test
	public void succesfullyCreatesUsageFromInvocation() {
		Query expected = new Query();
		expected.setType(convert(int32Type));
		
		Query actual = usageContextHelper.createNewObjectUsage(invocation, null);
		
		assertEquals(expected,actual);
	}

	@Test
	public void returnsClassItselfNoDirectSuperType() {
		ITypeName actual = usageContextHelper.findClassContext();
		
		assertEquals(enclosingType, actual);
	}
	
	@Test
	public void returnsDirectSuperClass() {
		TypeHierarchy typeHierarchy = (TypeHierarchy) ptContext.getTypeShape().getTypeHierarchy();
		TypeHierarchy extendTypeHierarchy = new TypeHierarchy();
		ITypeName superType = TypeName.newTypeName("System.Object, mscorlib");
		extendTypeHierarchy.setElement(superType);
		typeHierarchy.setExtends(extendTypeHierarchy);
		
		ITypeName actual = usageContextHelper.findClassContext();
		
		assertEquals(superType, actual);
	}
	
	@Test
	public void addsReceiverCallSite() {
		Query query = new Query();
		query.setType(convert(int32Type));
		
		usageContextHelper.addCallSite(query, invocation, -1);
		
		Set<CallSite> allCallsites = query.getAllCallsites();
		CallSite callSite = CallSites.createReceiverCallSite(convert(invocation.getMethodName()));
		assertThat(allCallsites, Matchers.contains(callSite));
		
	}
	
	@Test
	public void addsParameterCallSite() {
		Query query = new Query();
		query.setType(convert(stringType));
		
		usageContextHelper.addCallSite(query, invocation, 0);
		
		Set<CallSite> allCallsites = query.getAllCallsites();
		CallSite callSite = CallSites.createParameterCallSite(convert(invocation.getMethodName()),0);
		assertThat(allCallsites, Matchers.contains(callSite));
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
		
		DefinitionSite actual = UsageContextHelper.tryGetInvocationDefinition(constructorInvocation);
		
		assertThat(constructorDefinitionSite, Matchers.is(actual));
	}
	
	@Test
	public void createsReturnDefinitionSite() {
		DefinitionSite returnDefinitionSite = DefinitionSites.createDefinitionByReturn(convert(invocation.getMethodName()));
		DefinitionSite actual = UsageContextHelper.tryGetInvocationDefinition(invocation);
		
		assertThat(returnDefinitionSite, Matchers.is(actual));
	}
	
	@Test
	public void createsFieldDefinitionSiteWhenFieldParameterOfInvocation() {
		ReferenceExpression referenceExpression = new ReferenceExpression();
		referenceExpression.setReference(fieldRef);
		invocation.setParameters(Lists.newArrayList(referenceExpression));
		
		DefinitionSite fieldDefinitionSite = DefinitionSites.createDefinitionByField(convert(fieldRef.getFieldName()));
		
		DefinitionSite actual = usageContextHelper.findDefinitionSiteByReference(invocation, 0, (IMethodDeclaration) methodDecl);
		
		assertThat(actual, Matchers.is(fieldDefinitionSite));
	}
	
	@Test
	public void addsSuperMethodContext() {
		IMethodHierarchy methodHierarchy = ptContext.getTypeShape().getMethodHierarchies().stream().findAny().get();
		IMethodName methodName = TestUtil.method3;
		methodHierarchy.setSuper(methodName);
		
		Query expected = new Query();
		expected.setMethodContext(convert(methodName));
		
		Query actual = new Query();
		usageContextHelper.addMethodContext(actual, methodDecl);
		
		assertThat(actual, Matchers.is(expected));
	}
	
	@Test
	public void addMethodContext() {
		Query expected = new Query();
		expected.setMethodContext(convert(methodDecl.getName()));
		
		Query actual = new Query();
		usageContextHelper.addMethodContext(actual, methodDecl);
		
		assertThat(actual, Matchers.is(expected));
	}
	
	@Test
	public void addsParameterDefinitionSite() {
		Query expected = new Query();
		expected.setDefinition(DefinitionSites.createDefinitionByParam(convert(methodDecl.getName()), 0));
		
		Query actual = new Query();
		usageContextHelper.addDefinitionSite(actual, invocation, 0, methodDecl);
		
		assertThat(actual, Matchers.is(expected));
	}
	
	@Test
	public void addThisDefinitionSite() {
IMethodName methodName = TestUtil.method3;
		
		IInvocationExpression superInvocation = SSTUtil.invocationExpression("this", methodName);

		Query expected = new Query();
		expected.setDefinition(DefinitionSites.createDefinitionByThis());
		
		Query actual = new Query();
		usageContextHelper.addDefinitionSite(actual, superInvocation, -1, methodDecl);
		
		assertThat(actual, Matchers.is(expected));
	}
}
