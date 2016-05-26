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

import org.junit.Before;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.pointsto.analysis.FieldSensitivity;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.unification.UnificationAnalysis;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import exec.recommender_reimplementation.frequency_recommender.TestUtil;

public class PBNAnalysisBaseTest {

	public PBNAnalysisVisitor pbnAnalysisVisitor;
	protected InvocationExpression invocation;
	protected InvocationExpression invocation2;
	protected ExpressionStatement exprStatement;
	protected MethodDeclaration methodDecl;
	protected SST sst;
	protected ITypeName int32Type;
	protected ITypeName stringType;
	protected PointsToContext ptContext;
	protected ITypeName enclosingType;
	protected TestSSTBuilder builder;
	protected FieldDeclaration fieldDecl;
	protected VariableReference varRef;
	protected IFieldReference fieldRef;
	protected ReferenceExpression referenceExpression;
	protected Assignment assignment;
	
	@Before
	public void contextCreation() {
		builder = new TestSSTBuilder();
		
		stringType = builder.getStringType();
		int32Type = builder.getInt32Type();

		enclosingType = stringType;
		sst = builder.createEmptySST(enclosingType);
				
		
		fieldDecl = new FieldDeclaration();
		fieldDecl.setName(FieldName.newFieldName("[" + int32Type + "] [MyClass, MyAssembly, 1.2.3.4].Apple"));
		
		fieldRef = builder.buildFieldReference("this", fieldDecl.getName());
		
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
		
		assignment = new Assignment();
		assignment.setReference(varRef);
		ReferenceExpression refExpr = new ReferenceExpression();
		refExpr.setReference(fieldRef);
		assignment.setExpression(refExpr);
		
		invocation = new InvocationExpression();
		invocation.setReference(varRef);
		invocation.setMethodName(TestUtil.method1);
		referenceExpression = new ReferenceExpression();
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
		
		pbnAnalysisVisitor = new PBNAnalysisVisitor(ptContext);

	}
	
}
