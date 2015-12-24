/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsPropertyName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.pointsto.LanguageOptions;
import cc.kave.commons.pointsto.analysis.types.TypeCollector;

public class TypeCollectorTest {

	private LanguageOptions languageOptions = LanguageOptions.getInstance();

	@Test
	public void testAllTypesOfStreamTest() {
		TestSSTBuilder builder = new TestSSTBuilder();
		Context streamTestContext = builder.createStreamTest();

		TypeCollector collector = new TypeCollector(streamTestContext);
		Set<TypeName> allTypes = collector.getTypes();

		Assert.assertFalse(allTypes.isEmpty());

		TypeName streamTestType = streamTestContext.getTypeShape().getTypeHierarchy().getElement();
		Set<TypeName> expectedTypes = Sets.newHashSet(streamTestType, languageOptions.getTopClass(),
				builder.getStringType(), builder.getFileStreamType(), builder.getByteArrayType(),
				builder.getInt32Type());
		expectedTypes.add(CsTypeName.UNKNOWN_NAME); // assume that enums are treated as unknown types
		Assert.assertEquals(expectedTypes, allTypes);
	}

	@Test
	public void testReferenceTypesOfStreamTest() {
		TestSSTBuilder builder = new TestSSTBuilder();
		Context streamTestContext = builder.createStreamTest();
		TypeCollector collector = new TypeCollector(streamTestContext);

		List<Boolean> testedMethods = new ArrayList<>(Arrays.asList(false, false, false));

		for (IMethodDeclaration methodDecl : streamTestContext.getSST().getMethods()) {
			MethodName method = methodDecl.getName();
			List<IStatement> stmts = methodDecl.getBody();
			if (method.isConstructor()) {
				Assert.assertEquals(1, stmts.size());
				IAssignment assignment = (IAssignment) stmts.get(0);
				Assert.assertEquals(builder.getStringType(), collector.getType(assignment.getReference()));
				testedMethods.set(0, true);
			} else if (method.getName().equals("OpenSource")) {
				IReturnStatement retStmt = (IReturnStatement) stmts.get(stmts.size() - 1);
				IReferenceExpression refExpr = (IReferenceExpression) retStmt.getExpression();
				Assert.assertEquals(builder.getFileStreamType(), collector.getType(refExpr.getReference()));
				testedMethods.set(1, true);
			}
		}

		Assert.assertEquals(Arrays.asList(true, true, false), testedMethods);
	}

	@Test
	public void testReturnTypes() {
		TestSSTBuilder builder = new TestSSTBuilder();

		// void return type should not be collected
		Assert.assertFalse(new TypeCollector(builder.createStreamTest()).getTypes().contains(builder.getVoidType()));

		SST sst = builder.createEmptySST(CsTypeName.newTypeName("Test.ReturnTest, Test"));
		IMethodDeclaration testVoidDecl = SSTUtil.declareMethod(CsMethodName.newMethodName("["
				+ builder.getVoidType().getIdentifier() + "] [" + sst.getEnclosingType().getIdentifier() + "].test1()"),
				true);
		IMethodDeclaration testStringDecl = SSTUtil
				.declareMethod(CsMethodName.newMethodName("[" + builder.getStringType().getIdentifier() + "] ["
						+ sst.getEnclosingType().getIdentifier() + "].test2()"), true);
		sst.setMethods(Sets.newHashSet(testVoidDecl, testStringDecl));
		Context context = builder.createContext(sst);

		TypeCollector collector = new TypeCollector(context);
		Set<TypeName> expected = Sets.newHashSet(sst.getEnclosingType(), languageOptions.getTopClass(),
				builder.getStringType());
		Assert.assertEquals(expected, collector.getTypes());
	}

	public void testInvocationReference() {
		TestSSTBuilder builder = new TestSSTBuilder();

		SST sst = builder.createEmptySST(CsTypeName.newTypeName("Test.InvocationReferenceTest, Test"));
		IInvocationExpression equalsInvocation = SSTUtil.invocationExpression(languageOptions.getSuperName(),
				CsMethodName.newMethodName(
						"[" + builder.getBooleanType() + "] [" + languageOptions.getTopClass().getIdentifier()
								+ "].Equals([" + languageOptions.getTopClass() + "] obj"),
				Iterators.forArray(SSTUtil.refExpr("tmp")));
		IMethodDeclaration testDecl = SSTUtil.declareMethod(
				CsMethodName.newMethodName("[" + builder.getVoidType().getIdentifier() + "] ["
						+ sst.getEnclosingType().getIdentifier() + "].test()"),
				true, SSTUtil.declareVar("tmp", languageOptions.getTopClass()),
				SSTUtil.assignmentToLocal("tmp", SSTUtil.nullExpr()), SSTUtil.expr(equalsInvocation));
		sst.setMethods(Sets.newHashSet(testDecl));

		TypeCollector collector = new TypeCollector(builder.createContext(sst));
		Assert.assertEquals(languageOptions.getTopClass(), collector.getType(equalsInvocation.getReference()));
	}

	@Test
	public void testSubReferenceTypes() {
		TestSSTBuilder builder = new TestSSTBuilder();

		SST sst = builder.createEmptySST(CsTypeName.newTypeName("Test.SubReferenceTest, Test"));
		TypeName testClass = CsTypeName.newTypeName("Test.SomeClassWithStringField, Test");
		FieldName testField = CsFieldName.newFieldName(
				"[" + builder.getStringType().getIdentifier() + "] [" + testClass.getIdentifier() + "].desc");
		PropertyName strLengthProperty = CsPropertyName.newPropertyName("[" + builder.getInt32Type().getIdentifier()
				+ "] [" + builder.getStringType().getIdentifier() + "].Length()");

		IFieldReference fieldReference = builder.buildFieldReference("dummy", testField);
		IPropertyReference propertyReference = builder.buildPropertyRef("str", strLengthProperty);

		IMethodDeclaration testDecl = SSTUtil.declareMethod(
				CsMethodName.newMethodName("[" + builder.getVoidType().getIdentifier() + "] ["
						+ sst.getEnclosingType().getIdentifier() + "].test()"),
				true, SSTUtil.declareVar("dummy", testClass), SSTUtil.assignmentToLocal("dummy", SSTUtil.nullExpr()),
				SSTUtil.declareVar("str", builder.getStringType()),
				SSTUtil.assignmentToLocal("str", SSTUtil.refExpr(fieldReference)),
				SSTUtil.expr(SSTUtil.refExpr(propertyReference)));
		sst.setMethods(Sets.newHashSet(testDecl));

		TypeCollector collector = new TypeCollector(builder.createContext(sst));
		Assert.assertEquals(testClass, collector.getType(fieldReference.getReference()));
		Assert.assertEquals(builder.getStringType(), collector.getType(propertyReference.getReference()));
	}
}
