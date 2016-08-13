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
package exec.recommender_reimplementation.java_transformation;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.binExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.expr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.propertyReference;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.referenceExprToVariable;
import static cc.kave.commons.model.ssts.impl.SSTUtil.returnStatement;
import static exec.recommender_reimplementation.java_transformation.PropertyTransformationHelper.VOID_TYPE;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.BinaryExpression;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;

import com.google.common.collect.Lists;

public class PropertyTransformationTest extends JavaTransformationBaseTest {

	@Test
	public void transformReturnsNullWhenInputIsNull() {
		sut = new JavaTransformationVisitor(new SST());
		assertNull(sut.transform(null));
		assertNull(null, sut.transform(null, SST.class));
	}

	@Test
	public void PropertyDeclaration_GetterOnly() {
		PropertyDeclaration propertyDecl = new PropertyDeclaration();
		propertyDecl.setName(PropertyName.newPropertyName("get [PropertyType,P1] [DeclaringType,P1].P"));

		assertPropertyDeclaration(propertyDecl, defaultSSTWithBackingField(
				fieldDecl(field(type("PropertyType"), type("DeclaringType"), "Property$P")),
				methodDecl(method(type("PropertyType"), type("DeclaringType"), "get$P"), returnStatement(
						refExpr(fieldRef("this", field(type("PropertyType"), type("DeclaringType"), "Property$P")))))));
	}

	@Test
	public void PropertyDeclaration_SetterOnly() {
		PropertyDeclaration propertyDecl = new PropertyDeclaration();
		propertyDecl.setName(PropertyName.newPropertyName("set [PropertyType,P1] [DeclaringType,P1].P"));

		assertPropertyDeclaration(propertyDecl, defaultSSTWithBackingField(
				fieldDecl(field(type("PropertyType"), type("DeclaringType"), "Property$P")),
				methodDecl(method(VOID_TYPE, type("DeclaringType"), "set$P", parameter(type("PropertyType"), "value")),
						assign(fieldRef("this", field(type("PropertyType"), type("DeclaringType"), "Property$P")),
								refExpr(varRef("value"))))));
	}

	@Test
	public void PropertyDeclaration() {
		PropertyDeclaration propertyDecl = new PropertyDeclaration();
		propertyDecl.setName(PropertyName.newPropertyName("get set [PropertyType,P1] [DeclaringType,P1].P"));

		assertPropertyDeclaration(propertyDecl,
				defaultSSTWithBackingField(
						fieldDecl(
								field(type("PropertyType"), type("DeclaringType"),
										"Property$P")),
						methodDecl(method(VOID_TYPE, type("DeclaringType"), "set$P",
								parameter(type("PropertyType"), "value")), assign(
										fieldRef("this",
												field(type("PropertyType"), type("DeclaringType"), "Property$P")),
										refExpr(varRef("value")))),
						methodDecl(method(type("PropertyType"), type("DeclaringType"), "get$P"),
								returnStatement(refExpr(fieldRef("this",
										field(type("PropertyType"), type("DeclaringType"), "Property$P")))))));
	}

	@Test
	public void PropertyDeclaration_RemovesTrailingParenthesis() {
		PropertyDeclaration propertyDecl = new PropertyDeclaration();
		propertyDecl.setName(PropertyName.newPropertyName("get set [PropertyType,P1] [DeclaringType,P1].P()"));

		assertPropertyDeclaration(propertyDecl,
				defaultSSTWithBackingField(
						fieldDecl(
								field(type("PropertyType"), type("DeclaringType"),
										"Property$P")),
						methodDecl(method(VOID_TYPE, type("DeclaringType"), "set$P",
								parameter(type("PropertyType"), "value")), assign(
										fieldRef("this",
												field(type("PropertyType"), type("DeclaringType"), "Property$P")),
										refExpr(varRef("value")))),
						methodDecl(method(type("PropertyType"), type("DeclaringType"), "get$P"),
								returnStatement(refExpr(fieldRef("this",
										field(type("PropertyType"), type("DeclaringType"), "Property$P")))))));
	}

	@Test
	public void PropertyDeclaration_WithBodies() {
		PropertyDeclaration propertyDecl = new PropertyDeclaration();
		propertyDecl.setName(PropertyName.newPropertyName("get set [PropertyType,P1] [DeclaringType,P1].P"));
		propertyDecl.getGet().add(new ContinueStatement());
		propertyDecl.getGet().add(new BreakStatement());
		propertyDecl.getSet().add(new BreakStatement());
		propertyDecl.getSet().add(new ContinueStatement());

		assertPropertyDeclaration(propertyDecl, defaultSST(
				methodDecl(method(type("PropertyType"), type("DeclaringType"), "get$P"), new ContinueStatement(),
						new BreakStatement()),
				methodDecl(method(VOID_TYPE, type("DeclaringType"), "set$P", parameter(type("PropertyType"), "value")),
						new BreakStatement(), new ContinueStatement())));
	}

	@Test
	public void PropertyDeclaration_WithOnlyGetterBody() {
		PropertyDeclaration propertyDecl = new PropertyDeclaration();
		propertyDecl.setName(PropertyName.newPropertyName("get [PropertyType,P1] [DeclaringType,P1].P"));
		propertyDecl.getGet().add(new BreakStatement());

		assertPropertyDeclaration(propertyDecl, defaultSST(
				methodDecl(method(type("PropertyType"), type("DeclaringType"), "get$P"), new BreakStatement())));
	}

	@Test
	public void PropertyDeclaration_WithOnlySetterBody() {
		PropertyDeclaration propertyDecl = new PropertyDeclaration();
		propertyDecl.setName(PropertyName.newPropertyName("set [PropertyType,P1] [DeclaringType,P1].P"));
		propertyDecl.getSet().add(new BreakStatement());
		assertPropertyDeclaration(propertyDecl,
				defaultSST(methodDecl(
						method(VOID_TYPE, type("DeclaringType"), "set$P", parameter(type("PropertyType"), "value")),
						new BreakStatement())));
	}

	@Test
	public void testPropertySet() {
		assertAroundMethodDeclaration(
				Lists.newArrayList(
						assign(propertyReference(varRef("this"), "get set [PropertyType,P1] [DeclaringType,P1].P"),
								referenceExprToVariable("var"))), //
				expr(invocation("this",
						method(VOID_TYPE, type("DeclaringType"), "set$P", parameter(type("PropertyType"), "value")),
						referenceExprToVariable("var"))));
	}

	@Test
	public void testPropertySetWithAssignableExpression() {
		assertAroundMethodDeclaration(
				Lists.newArrayList(
						assign(propertyReference(varRef("this"), "get set [PropertyType,P1] [DeclaringType,P1].P"),
								new BinaryExpression())), //
				assign(fieldRef("this", field(type("PropertyType"), type("DeclaringType"), "Property$P")),
						binExpr(BinaryOperator.Unknown, constant("null"), constant("null"))));
	}

	@Test
	public void testPropertyGet() {
		assertAroundMethodDeclaration(
				Lists.newArrayList(assign(varRef("var"),
						refExpr(propertyReference(varRef("this"), "get set [PropertyType,P1] [DeclaringType,P1].P")))), //
				assign(varRef("var"),
						invocation("this", method(type("PropertyType"), type("DeclaringType"), "get$P"))));
	}

	@Test
	public void propertyReferenceInReferenceExpression() {
		assertAroundMethodDeclaration(
				Lists.newArrayList(expr(
						refExpr(propertyReference(varRef("this"), "get set [PropertyType,P1] [DeclaringType,P1].P")))), //
				expr(refExpr(fieldRef("this", field(type("PropertyType"), type("DeclaringType"), "Property$P")))));
	}

}
