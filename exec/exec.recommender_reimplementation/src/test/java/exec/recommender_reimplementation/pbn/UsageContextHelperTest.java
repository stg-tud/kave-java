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
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.intType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.objectType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.stringType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.pointsto.analysis.PointsToQueryBuilder;
import cc.kave.commons.pointsto.analysis.types.TypeCollector;
import cc.kave.commons.utils.SSTNodeHierarchy;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;
import exec.recommender_reimplementation.frequency_recommender.TestUtil;

public class UsageContextHelperTest extends PBNAnalysisBaseTest {

	private UsageContextHelper usageContextHelper;

	private IMethodDeclaration methodDecl;

	private IInvocationExpression invocationWithParameters;
	
	@Before
	public void setUp() {
		invocationWithParameters = invokeWithParameters("a",
				method(voidType, DefaultClassContext, "m1", parameter(objectType, "foo")),
				referenceExpr(varRef("b")));
		
		setupEnclosingMethod(
				method(voidType, DefaultClassContext, "entry1", parameter(stringType, "b")),
				varDecl("a", intType),
				assign("a", referenceExpr(fieldReference("this", field(intType, DefaultClassContext, "Apple")))),
				invokeStmt(invocationWithParameters), 
						invokeStmt("a", method(voidType, DefaultClassContext, "m2")));

		context.getSST().getFields().add(fieldDecl(field(intType, DefaultClassContext, "SomeField")));

		methodDecl = context.getSST().getMethods().stream().findFirst().get();
		
		TypeCollector typeCollector = new TypeCollector(context);
		SSTNodeHierarchy sstNodeHierarchy = new SSTNodeHierarchy(context.getSST());
		usageContextHelper = new UsageContextHelper(typeCollector, context, new PointsToQueryBuilder(typeCollector,
				sstNodeHierarchy), sstNodeHierarchy);
	}

	@Test
	public void succesfullyCreatesUsageFromInvocation() {
		Query expected = new Query();
		expected.setType(convert(intType));

		Query actual = usageContextHelper.createNewObjectUsage(invocationWithParameters, null);

		assertEquals(expected, actual);
	}

	@Test
	public void returnsObjectTypeIfNoDirectSuperType() {
		ITypeName actual = usageContextHelper.findClassContext();

		assertEquals(objectType, actual);
	}

	@Test
	public void returnsDirectSuperClass() {
		TypeHierarchy typeHierarchy = (TypeHierarchy) context.getTypeShape().getTypeHierarchy();
		TypeHierarchy extendTypeHierarchy = new TypeHierarchy();
		ITypeName superType = Names.newType("System.Object, mscorlib");
		extendTypeHierarchy.setElement(superType);
		typeHierarchy.setExtends(extendTypeHierarchy);

		ITypeName actual = usageContextHelper.findClassContext();

		assertEquals(superType, actual);
	}

	@Test
	public void addsReceiverCallSite() {
		Query query = new Query();
		query.setType(convert(intType));

		usageContextHelper.addCallSite(query, invoke("a", method(voidType, DefaultClassContext, "SomeMethod")), -1);

		Set<CallSite> allCallsites = query.getAllCallsites();
		CallSite callSite = CallSites.createReceiverCallSite(convert(method(voidType, DefaultClassContext, "SomeMethod")));
		assertThat(allCallsites, Matchers.contains(callSite));

	}

	@Test
	public void addsParameterCallSite() {
		Query query = new Query();
		query.setType(convert(stringType));

		usageContextHelper.addCallSite(query, invokeWithParameters("a",
				method(voidType, DefaultClassContext, "m1"),
				referenceExpr(varRef("b"))), 0);

		Set<CallSite> allCallsites = query.getAllCallsites();
		CallSite callSite = CallSites.createParameterCallSite(convert(
				method(voidType, DefaultClassContext, "m1")), 0);
		assertThat(allCallsites, Matchers.contains(callSite));
	}

	@Test
	public void createsConstructorDefinitionSite() {
		InvocationExpression constructorInvocation = new InvocationExpression();
		VariableReference otherVarRef = new VariableReference();
		otherVarRef.setIdentifier("c");
		constructorInvocation.setReference(otherVarRef);
		IMethodName constructorMethodName = Names
				.newMethod("[System.String, mscore, 4.0.0.0] [System.String, mscore, 4.0.0.0]..ctor()");
		constructorInvocation.setMethodName(constructorMethodName);

		DefinitionSite constructorDefinitionSite = DefinitionSites
				.createDefinitionByConstructor(convert(constructorMethodName));

		DefinitionSite actual = UsageContextHelper.tryGetInvocationDefinition(constructorInvocation);

		assertThat(constructorDefinitionSite, Matchers.is(actual));
	}

	@Test
	public void createsReturnDefinitionSite() {
		DefinitionSite returnDefinitionSite = DefinitionSites.createDefinitionByReturn(convert(method(intType, DefaultClassContext, "m1")));
		DefinitionSite actual = UsageContextHelper.tryGetInvocationDefinition(invoke("a", method(intType, DefaultClassContext, "m1")));

		assertThat(returnDefinitionSite, Matchers.is(actual));
	}

	@Test
	public void createsFieldDefinitionSiteWhenFieldParameterOfInvocation() {
		invocationWithParameters.getParameters().add(referenceExpr(fieldReference("this", field(intType, DefaultClassContext, "SomeField"))));
		
		DefinitionSite fieldDefinitionSite = DefinitionSites.createDefinitionByField(convert(field(intType, DefaultClassContext, "SomeField")));

		DefinitionSite actual = usageContextHelper.findDefinitionSiteByReference(invocationWithParameters, 1, methodDecl);

		assertThat(actual, Matchers.is(fieldDefinitionSite));
	}

	@Test
	public void addsSuperMethodContext() {
		IMethodHierarchy methodHierarchy = methodHierarchy(methodDecl.getName(), method(voidType, DefaultClassContext, "SuperMethod"));
		resetMethodHierarchies(methodHierarchy);

		Query expected = new Query();
		expected.setMethodContext(convert(method(voidType, DefaultClassContext, "SuperMethod")));

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
		usageContextHelper.addDefinitionSite(actual, invocationWithParameters, 0, methodDecl);

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
