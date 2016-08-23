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

import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;
import static cc.kave.commons.pointsto.extraction.CoReNameConverter.convert;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Before;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
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
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
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

public class PBNAnalysisBaseTest {

	protected PointsToContext context;

	protected static ITypeName DefaultClassContext = type("TDecl");

	protected static IMethodName DefaultMethodContext = method(type("A"), DefaultClassContext, "M");
	protected PointsToAnalysis pointsToAnalysis;

	private UsageExtractor uut;

	@Before
	public void Setup() {
		context = new PointsToContext();
		uut = new UsageExtractor();
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
	
	protected PointsToContext getContextFor(ISST sst, TypeHierarchy typeHierarchy, MethodHierarchy... methodHierarchies) {
		pointsToAnalysis = new UnificationAnalysis(FieldSensitivity.FULL);
		
		PointsToContext ctx = new PointsToContext();
		ctx.getTypeShape().setTypeHierarchy(typeHierarchy);
		Set<IMethodHierarchy> methodHierarchiesSet = Sets.newHashSet(methodHierarchies);
		ctx.getTypeShape().setMethodHierarchies(methodHierarchiesSet);

		ctx.setSST(sst);

		ctx = pointsToAnalysis.compute(ctx);
		return ctx;
	}
	
	protected void resetMethodHierarchies(IMethodHierarchy... methodHierarchies) {
		context.getTypeShape().getMethodHierarchies().clear();
		for (IMethodHierarchy methodHierarchy : methodHierarchies) {
			context.getTypeShape().getMethodHierarchies().add(methodHierarchy);
		}
	}
	
	protected void resetTypeHierarchy(ITypeHierarchy typeHierarchy) {
		context.getTypeShape().setTypeHierarchy(typeHierarchy);
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
		assertThat(queries, Matchers.containsInAnyOrder(expectedsArr));
	}

	protected Usage findQueryWith(ITypeName type) {
    	List<Usage> usages = Lists.newArrayList();
        uut.extractUsageFromContext(context, usages);
		for (Usage actual : usages) {
			if (Objects.equal(actual.getType(), convert(type))) {
				return actual;
			}
		}
		fail(String.format("no query found for type %1$s", type));
		return null;
	}

	protected void assertQueriesExistFor(ITypeName... expecteds) {
    	List<Usage> usages = Lists.newArrayList();
        uut.extractUsageFromContext(context, usages);
		List<ICoReTypeName> expectedsCore = Arrays.asList(expecteds).stream().map(t -> convert(t))
				.collect(Collectors.toList());
		List<ICoReTypeName> actuals = usages.stream().map(u -> u.getType()).collect(Collectors.toList());
		assertThat(actuals, Matchers.containsInAnyOrder(expectedsCore.toArray()));
	}
	
    protected Usage assertSingleQuery()
    {
    	List<Usage> actuals = Lists.newArrayList();
        uut.extractUsageFromContext(context, actuals);
        assertEquals(1, actuals.size());
        return actuals.get(0);
    }

	protected void assertSingleQueryWithType(ITypeName expected) {
		Usage query = assertSingleQuery();
		ICoReTypeName actual = query.getType();
		assertEquals(convert(expected), actual);
	}

	protected void assertSingleQueryWithDefinition(DefinitionSite expected) {
		Usage query = assertSingleQuery();
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
		return typeHierarchy(DefaultClassContext);
	}
	
	protected static TypeHierarchy typeHierarchy(ITypeName element) {
		TypeHierarchy typeHierarchy = new TypeHierarchy();
		typeHierarchy.setElement(element);
		return typeHierarchy;
	}
	
	protected static TypeHierarchy typeHierarchy(ITypeName element, ITypeName superType) {
		TypeHierarchy typeHierarchy = new TypeHierarchy();
		typeHierarchy.setElement(element);
		TypeHierarchy _extends = new TypeHierarchy();
		_extends.setElement(superType);
		typeHierarchy.setExtends(_extends);
		return typeHierarchy;
	}

	protected static MethodHierarchy methodHierarchy(IMethodName enclosingMethod, IMethodName superMethod) {
		MethodHierarchy methodHierarchy = new MethodHierarchy(enclosingMethod);
		methodHierarchy.setSuper(superMethod);
		return methodHierarchy;
	}
	
	protected static MethodHierarchy methodHierarchy(IMethodName enclosingMethod) {
		MethodHierarchy methodHierarchy = new MethodHierarchy(enclosingMethod);
		return methodHierarchy;
	}

	protected static IFieldName field(ITypeName valType, ITypeName declType, String fieldName) {
		String field = String.format("[%1$s] [%2$s].%3$s", valType.getIdentifier(), declType.getIdentifier(),
				fieldName);
		return Names.newField(field);
	}

	protected static IParameterName parameter(ITypeName valType, String paramName) {
		String param = String.format("[%1$s] %2$s", valType.getIdentifier(), paramName);
		return Names.newParameter(param);
	}

	protected static ITypeName type(String simpleName) {
		return Names.newType(simpleName + ",P1");
	}

	protected static IMethodName method(ITypeName returnType, ITypeName declType, String simpleName,
			IParameterName... parameters) {
		String parameterStr = Joiner.on(", ").join(
				Arrays.asList(parameters).stream().map(p -> p.getIdentifier()).toArray());
		String methodIdentifier = String.format("[%1$s] [%2$s].%3$s(%4$s)", returnType.getIdentifier(),
				declType.getIdentifier(), simpleName,
				parameterStr);
		return Names.newMethod(methodIdentifier);
	}

	protected static SST sst(ITypeName declaringType, IMethodName enclosingMethod, IStatement... statements) {
		SST sst = new SST();
		sst.setEnclosingType(declaringType);
		sst.setMethods(Sets.newHashSet(methodDecl(enclosingMethod, true, statements)));
		return sst;
	}
	
	protected static SST sst(ITypeName declaringType, IMethodDeclaration... methodDecls) {
		SST sst = new SST();
		sst.setEnclosingType(declaringType);
		sst.setMethods(Sets.newHashSet(methodDecls));
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

	protected static IMethodDeclaration methodDecl(IMethodName enclosingMethod,boolean entryPoint, IStatement... statements) {
		MethodDeclaration methodDecl = new MethodDeclaration();
		methodDecl.setName(enclosingMethod);
		methodDecl.setBody(Lists.newArrayList(statements));
		methodDecl.setEntryPoint(entryPoint);
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
	
	protected static ITryBlock tryBlock(List<IStatement> body, List<IStatement> catchBlockBody) {
		TryBlock tryBlock = new TryBlock();
		tryBlock.setBody(body);
		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setBody(catchBlockBody);
		tryBlock.setCatchBlocks(Lists.newArrayList(catchBlock));
		return tryBlock;
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
