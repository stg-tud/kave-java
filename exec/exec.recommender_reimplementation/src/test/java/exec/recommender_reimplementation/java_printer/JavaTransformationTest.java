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
package exec.recommender_reimplementation.java_printer;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.binExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareMethod;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareVar;
import static cc.kave.commons.model.ssts.impl.SSTUtil.expr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.propertyReference;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.referenceExprToVariable;
import static cc.kave.commons.model.ssts.impl.SSTUtil.returnStatement;
import static cc.kave.commons.model.ssts.impl.SSTUtil.unaryExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;
import static exec.recommender_reimplementation.java_printer.JavaTransformationVisitor.VOID_TYPE;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.SwitchBlock;
import cc.kave.commons.model.ssts.impl.blocks.UnsafeBlock;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.BinaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IndexAccessExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.EventSubscriptionStatement;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;

public class JavaTransformationTest extends JavaTransformationBaseTest {

	@Test
	public void transformReturnsNullWhenInputIsNull() {
		sut = new JavaTransformationVisitor(new SST());
		assertEquals(null, sut.transform(null));
		assertEquals(null, sut.transform(null, SST.class));
	}

	@Test
	public void indexAccessExpressionWithArray() {
		IndexAccessExpression indexAccessExpression = new IndexAccessExpression();
		indexAccessExpression.setIndices(Lists.newArrayList(constant("1"), constant("2")));
		indexAccessExpression.setReference(varRef("x"));

		assertAroundMethodDeclaration(
				Lists.newArrayList(declareVar("x", type("String[]")), expr(indexAccessExpression)),
				declareVar("x", type("String[]")), expr(indexAccessExpression));
	}

	@Test
	public void testIndexAccessExpressionList() {
		IndexAccessExpression indexAccessExpression = new IndexAccessExpression();
		indexAccessExpression.setIndices(Lists.newArrayList(constant("1")));
		indexAccessExpression.setReference(varRef("x"));

		ITypeName listType = type("List");
		assertAroundMethodDeclaration(Lists.newArrayList(declareVar("x", listType), expr(indexAccessExpression)),
				declareVar("x", listType), expr(invocation("x", method(null, listType, "get"), constant("1"))));
	}

	@Test
	public void testIndexAccessExpressionInAssignment() {
		IndexAccessExpression indexAccessExpression = new IndexAccessExpression();
		indexAccessExpression.setIndices(Lists.newArrayList(constant("1")));
		indexAccessExpression.setReference(varRef("x"));

		ITypeName listType = type("List");
		assertAroundMethodDeclaration(
				Lists.newArrayList(declareVar("x", listType), assign(varRef("y"), indexAccessExpression)),
				declareVar("x", listType),
				assign(varRef("y"), invocation("x", method(null, listType, "get"), constant("1"))));
	}

	@Test
	public void transformsDelegateMethods() {
		InvocationExpression delegateInvoke = new InvocationExpression();
		delegateInvoke.setMethodName(MethodName.newMethodName(
				"[System.Void, mscorlib, 4.0.0.0] [d:[System.Void, mscorlib, 4.0.0.0] [P1.DelegateTest+Del, P1].([System.String, mscorlib, 4.0.0.0] message)].Invoke([System.String, mscorlib, 4.0.0.0] message)"));

		InvocationExpression expected = new InvocationExpression();
		expected.setMethodName(MethodName
				.newMethodName(
						"[System.Void, mscorlib, 4.0.0.0] [P1.DelegateTest, P1].Delegate$Invoke([System.String, mscorlib, 4.0.0.0] message)"));

		assertNode(delegateInvoke, expected);
	}

	@Test
	public void constantValueExpression_Null() {
		assertAroundMethodDeclaration(
				Lists.newArrayList(declareVar("x", type("s:System.Int32")),
						assign(varRef("x"), new ConstantValueExpression())),
				declareVar("x", type("s:System.Int32")), assign(varRef("x"), constant("0")));
	}

	@Test
	public void composedExpressionInAssignment() {
		assertAroundMethodDeclaration(
				Lists.newArrayList(declareVar("x", type("s:System.Int32")),
						assign(variableReference("x"), new ComposedExpression())),
				declareVar("x", type("s:System.Int32")), assign(varRef("x"), constant("0")));
	}

	@Test
	public void composedExpressionInExprStatement() {
		assertAroundMethodDeclaration(Lists.newArrayList(declareVar("x", type("T1")), expr(new ComposedExpression())),
				declareVar("x", type("T1")), expr(constant("null")));
	}

	@Test
	public void unknownExpressionNestedInBinaryExpression() {
		assertAroundMethodDeclaration(
				Lists.newArrayList(declareVar("x", type("s:System.Int32")),
						assign(variableReference("x"),
								binExpr(BinaryOperator.Plus, new UnknownExpression(), new UnknownExpression()))),
				declareVar("x", type("s:System.Int32")),
				assign(variableReference("x"), binExpr(BinaryOperator.Plus, constant("0"), constant("0"))));
	}

	@Test
	public void unknownExpressionNestedInUnaryExpression() {
		assertAroundMethodDeclaration(
				Lists.newArrayList(declareVar("x", type("s:System.Int32")),
						assign(variableReference("x"), unaryExpr(UnaryOperator.Plus, new UnknownExpression()))),
				declareVar("x", type("s:System.Int32")),
				assign(variableReference("x"), unaryExpr(UnaryOperator.Plus, constant("0"))));
	}

	@Test
	public void testEmptyConditionInIfElseBlock() {
		IfElseBlock node = new IfElseBlock();
		node.getThen().add(new ContinueStatement());
		node.getElse().add(new BreakStatement());

		IfElseBlock expected = new IfElseBlock();
		expected.setCondition(constant("false"));
		expected.getThen().add(new ContinueStatement());
		expected.getElse().add(new BreakStatement());

		assertNode(node, expected);
	}

	@Test
	public void testEmptyLabelInCaseBlock() {
		SwitchBlock switchBlock = new SwitchBlock();
		switchBlock.getSections().add(new CaseBlock());

		SwitchBlock expected = new SwitchBlock();
		CaseBlock expectedCaseBlock = new CaseBlock();
		expectedCaseBlock.setLabel(constant("0"));
		expected.getSections().add(expectedCaseBlock);

		assertNode(switchBlock, expected);
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

	@Test
	public void testEmptyReturnStatementInMethodDeclaration() {
		IMethodDeclaration methodDecl = declareMethod(method(type("s:System.Boolean"), type("Class1, P1"), "m1"), true,
				new ReturnStatement());
		assertNode(methodDecl, methodDecl(method(type("s:System.Boolean"), type("Class1, P1"), "m1"),
				returnStatement(constant("false"))));
	}

	@Test
	public void testEmptyReturnStatementInPropertyDeclaration() {
		PropertyDeclaration propertyDecl = new PropertyDeclaration();
		propertyDecl.setName(PropertyName.newPropertyName("get [s:System.Int32,P1] [DeclaringType,P1].P"));
		propertyDecl.setGet(Lists.newArrayList(new ReturnStatement()));
		assertPropertyDeclaration(propertyDecl,
				defaultSST(methodDecl(method(type("s:System.Int32"), type("DeclaringType"), "get$P"),
						returnStatement(constant("0")))));
	}

	@Test
	public void removesGotoStatement() {
		assertAroundMethodDeclaration(Lists.newArrayList(new GotoStatement()));
	}

	@Test
	public void removesUnsafeBlock() {
		assertAroundMethodDeclaration(Lists.newArrayList(new UnsafeBlock()));
	}

	@Test
	public void removesEventSubscriptionStatement() {
		assertAroundMethodDeclaration(Lists.newArrayList(new EventSubscriptionStatement()));
	}

	@Test
	public void removesUnknownStatement() {
		assertAroundMethodDeclaration(Lists.newArrayList(new UnknownStatement()));
	}
}
