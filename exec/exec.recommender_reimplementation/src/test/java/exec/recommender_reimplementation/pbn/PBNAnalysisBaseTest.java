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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Before;

import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.ParameterName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.pointsto.analysis.FieldSensitivity;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.unification.UnificationAnalysis;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.*;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;
import static cc.kave.commons.pointsto.extraction.CoReNameConverter.*;
import static org.junit.Assert.*;

public class PBNAnalysisBaseTest {

	protected PointsToContext context;

	protected static ITypeName DefaultClassContext = type("TDecl");

	protected static IMethodName DefaultMethodContext = method(type("A"), DefaultClassContext, "M");
	protected PointsToAnalysis pointsToAnalysis;

	@Before
	public void Setup() {
		context = new PointsToContext();
	}

	protected void setupDefaultEnclosingMethod(IStatement... statements) {
		setupEnclosingMethod(DefaultMethodContext, statements);
	}

	protected void setupEnclosingMethod(IMethodName enclosingMethod, IStatement... statements) {
		pointsToAnalysis = new UnificationAnalysis(FieldSensitivity.FULL);

		context.getTypeShape().setTypeHierarchy(defaultTypeHierarchy());
		context.getTypeShape().getMethodHierarchies().add(new MethodHierarchy(enclosingMethod));
		context.setSST(sst(DefaultClassContext, enclosingMethod, statements));
		context = pointsToAnalysis.compute(context);
	}
	
	protected void setupContextFor(ISST sst) {
		pointsToAnalysis = new UnificationAnalysis(FieldSensitivity.FULL);

		context.getTypeShape().setTypeHierarchy(defaultTypeHierarchy());
		Set<IMethodHierarchy> methodHierarchies = new HashSet<>();
		for (IMethodDeclaration methodDecl : sst.getEntryPoints()) {
			methodHierarchies.add(new MethodHierarchy(methodDecl.getName()));
		}
		context.getTypeShape().setMethodHierarchies(methodHierarchies);

		context.setSST(sst);

		context = pointsToAnalysis.compute(context);
	}
	
	protected void resetMethodHierarchies(IMethodHierarchy... methodHierarchies) {
		context.getTypeShape().getMethodHierarchies().clear();
		for (IMethodHierarchy methodHierarchy : methodHierarchies) {
			context.getTypeShape().getMethodHierarchies().add(methodHierarchy);
		}
	}

	/** Assertion Helpers **/

	protected void assertQueriesInDefault(List<Usage> queries, Usage... expecteds) {
		assertQueries(queries, DefaultMethodContext, expecteds);
	}

	protected void assertQueries(List<Usage> queries, IMethodName enclosingMethod, Usage... expecteds) {
		assertQueries(queries, enclosingMethod.getDeclaringType(), enclosingMethod, expecteds);
	}

	protected void assertQueries(List<Usage> queries, ITypeName enclosingClass, IMethodName enclosingMethod,
			Usage... expecteds) {
		for (Usage expected : expecteds) {
			((Query) expected).setClassContext(convert(enclosingClass));
			((Query) expected).setMethodContext(convert(enclosingMethod));
		}
		assertQueriesWithoutSettingContexts(queries, expecteds);
	}

	protected void assertQueriesWithoutSettingContexts(List<Usage> queries, Usage... expectedsArr) {
		List<Usage> expecteds = Lists.newArrayList(expectedsArr);
		assertThat(queries, Matchers.is(expecteds));
	}

	protected Usage findQueryWith(List<Usage> usages, ITypeName type) {
		for (Usage actual : usages) {
			if (Objects.equal(actual.getType(), convert(type))) {
				return actual;
			}
		}
		fail(String.format("no query found for type %1$s", type));
		return null;
	}

	protected void assertQueriesExistFor(List<Usage> usages, ITypeName... expecteds) {
		List<ICoReTypeName> expectedsCore = Arrays.asList(expecteds).stream().map(t -> convert(t))
				.collect(Collectors.toList());
		List<ICoReTypeName> actuals = usages.stream().map(u -> u.getType()).collect(Collectors.toList());
		assertThat(actuals, Matchers.is(expectedsCore));
	}

	protected void assertQueryWithType(Usage query, ITypeName expected) {
		ICoReTypeName actual = query.getType();
		assertEquals(convert(expected), actual);
	}

	protected void assertQueryWithDefinition(Usage query, DefinitionSite expected) {
		DefinitionSite actual = query.getDefinitionSite();
		assertEquals(expected, actual);
	}
	
	

	/** Query Helper **/

	protected Usage queryWithDefaultContext(ITypeName type, DefinitionSite definition, CallSite... callSites) {
		return query(type, DefaultClassContext, DefaultMethodContext, definition, callSites);
	}

	protected Usage query(ITypeName type, ITypeName classContext, IMethodName methodContext, DefinitionSite definition,
			CallSite... callSites) {
		Query query = new Query();
		query.setType(convert(type));
		query.setClassContext(convert(classContext));
		query.setMethodContext(convert(methodContext));
		query.setDefinition(definition);
		query.setAllCallsites(Sets.newHashSet(callSites));
		return query;

	}

	/** Instantiation Helpers **/

	protected static TypeHierarchy defaultTypeHierarchy() {
		TypeHierarchy typeHierarchy = new TypeHierarchy();
		typeHierarchy.setElement(DefaultClassContext);
		return typeHierarchy;
	}

	protected static MethodHierarchy methodHierarchy(IMethodName enclosingMethod, IMethodName superMethod) {
		MethodHierarchy methodHierarchy = new MethodHierarchy(enclosingMethod);
		methodHierarchy.setSuper(superMethod);
		return methodHierarchy;
	}

	protected static IFieldName field(ITypeName valType, ITypeName declType, String fieldName) {
		String field = String.format("[%1$s] [%2$s].%3$s", valType, declType, fieldName);
		return FieldName.newFieldName(field);
	}

	protected static IParameterName parameter(ITypeName valType, String paramName) {
		String param = String.format("[%1$s] %2$s", valType, paramName);
		return ParameterName.newParameterName(param);
	}

	protected static ITypeName type(String simpleName) {
		return TypeName.newTypeName(simpleName + ",P1");
	}

	protected static IMethodName method(ITypeName returnType, ITypeName declType, String simpleName,
			IParameterName... parameters) {
		String parameterStr = Joiner.on(", ").join(
				Arrays.asList(parameters).stream().map(p -> p.getIdentifier()).toArray());
		String methodIdentifier = String.format("[%1$s] [%2$s].%3$s(%4$s)", returnType, declType, simpleName,
				parameterStr);
		return MethodName.newMethodName(methodIdentifier);
	}

	protected static SST sst(ITypeName declaringType, IMethodName enclosingMethod, IStatement... statements) {
		SST sst = new SST();
		sst.setEnclosingType(declaringType);
		sst.setMethods(Sets.newHashSet(methodDecl(enclosingMethod, statements)));
		return sst;
	}

	protected static SST emptySST(ITypeName declaringType) {
		SST sst = new SST();
		sst.setEnclosingType(declaringType);
		return sst;
	}
	
	protected static IFieldDeclaration fieldDecl(IFieldName fieldName) {
		FieldDeclaration fieldDecl = new FieldDeclaration();
		fieldDecl.setName(fieldName);
		return fieldDecl;
	}

	protected static IMethodDeclaration methodDecl(IMethodName enclosingMethod, IStatement... statements) {
		MethodDeclaration methodDecl = new MethodDeclaration();
		methodDecl.setName(enclosingMethod);
		methodDecl.setBody(Lists.newArrayList(statements));
		methodDecl.setEntryPoint(true);
		return methodDecl;
	}

	protected static IVariableReference varRef(String id) {
		VariableReference varRef = new VariableReference();
		varRef.setIdentifier(id);
		return varRef;
	}

	protected static IFieldReference fieldReference(String id, IFieldName field) {
		FieldReference fieldRef = new FieldReference();
		fieldRef.setFieldName(field);
		fieldRef.setReference(variableReference(id));
		return fieldRef;
	}

	protected static IInvocationExpression constructor(ITypeName type) {
		return invokeStatic(method(voidType, type, ".ctor"));
	}

	protected static IAssignment assign(String varName, IAssignableExpression expr) {
		Assignment assignment = new Assignment();
		assignment.setReference(varRef(varName));
		assignment.setExpression(expr);
		return assignment;
	}

	protected static IConstantValueExpression constant(String value) {
		ConstantValueExpression constantValueExpression = new ConstantValueExpression();
		constantValueExpression.setValue(value);
		return constantValueExpression;
	}
	
	protected static IInvocationExpression invoke(String varName, IMethodName method) {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setReference(varRef(varName));
		invocation.setMethodName(method);
		return invocation;
	}

	protected static IInvocationExpression invokeStatic(IMethodName method) {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setMethodName(method);
		return invocation;
	}

	protected static IInvocationExpression invokeWithParameters(String varName, IMethodName method,
			ISimpleExpression... parameters) {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setReference(varRef(varName));
		invocation.setMethodName(method);
		invocation.setParameters(Lists.newArrayList(parameters));
		return invocation;
	}

	protected static IExpressionStatement invokeStmt(String varName, IMethodName method) {
		ExpressionStatement exprStmt = new ExpressionStatement();
		exprStmt.setExpression(invoke(varName, method));
		return exprStmt;
	}

	protected static IExpressionStatement invokeStmt(IInvocationExpression invocation) {
		ExpressionStatement exprStmt = new ExpressionStatement();
		exprStmt.setExpression(invocation);
		return exprStmt;
	}

	protected static IReferenceExpression referenceExpr(IReference reference) {
		ReferenceExpression refExpr = new ReferenceExpression();
		refExpr.setReference(reference);
		return refExpr;
	}

	protected static IVariableDeclaration varDecl(String name, ITypeName type) {
		VariableDeclaration varDecl = new VariableDeclaration();
		varDecl.setReference(varRef(name));
		varDecl.setType(type);
		return varDecl;
	}

	protected static CallSite someCallSiteOnType(String typeName) {
		return CallSites.createReceiverCallSite(convert(method(voidType, type(typeName), "M")));
	}

	protected static IMethodName someMethodOnType(String typeName) {
		return method(voidType, type(typeName), "M");
	}
}
