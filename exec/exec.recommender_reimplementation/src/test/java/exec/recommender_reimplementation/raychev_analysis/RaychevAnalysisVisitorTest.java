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
package exec.recommender_reimplementation.raychev_analysis;

import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.intType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.objectType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.stringType;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.voidType;
import static exec.recommender_reimplementation.raychev_analysis.Interaction.RETURN;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.typeshapes.TypeHierarchy;

public class RaychevAnalysisVisitorTest extends RaychevAnalysisBaseTest {

	private RaychevAnalysisVisitor sut;

	@Test
	public void addsNewAbstractHistoryForObjectAllocation() {
		setupDefaultEnclosingMethod(//
				varDecl("foo", objectType), //
				assign("foo", constructor(stringType)));

		extractHistories();

		AbstractHistory abstractHistory = createAbstractHistory(//
				callAtPosition(method(voidType, stringType, "<init>"), 0));

		assertHistories(abstractHistory);
	}

	@Test
	public void ignoresIsInit() {
		setupDefaultEnclosingMethod(//
				varDecl("foo", objectType), //
				assign("foo", invokeStatic(method(voidType, type("T1"), ".init"))));

		extractHistories();

		assertHistories();
	}

	@Test
	public void addsInteractionForInvocations() {
		IMethodName methodName = method(stringType, DefaultClassContext, "m1",
				parameter(stringType, "p1"));
		setupDefaultEnclosingMethod(
				varDecl("bar", objectType),
				assign("bar", constant("someValue")),
				varDecl("foo", objectType),
				assign("foo", constant("someValue2")),
				varDecl("foobar", stringType),
				assign("foobar",
						invokeWithParameters("bar", methodName,
								referenceExpr(varRef("foo")))));

		extractHistories();

		assertHistories(
				//
				createAbstractHistory(callAtPosition(methodName, 1)),
				createAbstractHistory(callAtPosition(methodName, 0)),
				createAbstractHistory(callAtPosition(methodName, RETURN)));
	}
	
	@Test
	public void ignoresInvocationWithGenericsInDeclaringType() {
		IMethodName methodName = method(stringType,
				type("System.Collections.Dictionary`2[[TKey -> Int32, P1],[TValue -> String, P1]]"), "m1");
		setupDefaultEnclosingMethod(
				varDecl("bar", objectType),
				assign("foobar",
						invoke("bar", methodName)));

		extractHistories();

		assertHistories();
	}

	@Test
	public void ignoresInvocationWithGenericsInParameter() {
		IMethodName methodName = method(stringType,
				DefaultClassContext, "m1",
				parameter(type("System.Collections.Dictionary`2[[TKey -> Int32, P1],[TValue -> String, P1]]"), "p1"));
		setupDefaultEnclosingMethod(
				varDecl("someVar", type("System.Collections.Dictionary`2[[TKey -> Int32, P1],[TValue -> String, P1]]")),
				invokeStmt(
						invokeWithParameters("bar", methodName, referenceExpr(varRef("someVar")))));

		extractHistories();

		assertHistories();
	}

	@Test
	public void ifElseBlockCreatesMultipleConcreteHistories() {
		IIfElseBlock ifElseBlock = new IfElseBlock();
		ifElseBlock.getThen().add(
				invokeStmt("foo", method(voidType, DefaultClassContext, "m1")));
		ifElseBlock.getElse().add(
				invokeStmt("foo", method(voidType, DefaultClassContext, "m2")));

		setupDefaultEnclosingMethod(varDecl("foo", objectType),
				assign("foo", constructor(stringType)), ifElseBlock,
				invokeStmt("foo", method(voidType, DefaultClassContext, "m3")));

		extractHistories();

		AbstractHistory abstractHistory = createAbstractHistory(//
				Lists.newArrayList(
						//
						constructorCall(),
						callInDefaultContextAsReceiver("m1"),
						callInDefaultContextAsReceiver("m2"),
						callInDefaultContextAsReceiver("m3")), //
				Sets.newHashSet(
						createConcreteHistory(constructorCall(),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m3")),
						createConcreteHistory(constructorCall(),
								callInDefaultContextAsReceiver("m2"),
								callInDefaultContextAsReceiver("m3"))));

		assertHistories(abstractHistory);
	}

	@Test
	public void ifWithoutElseBlockCreatesMultipleConcreteHistories() {
		IIfElseBlock ifElseBlock = new IfElseBlock();
		ifElseBlock.getThen().add(
				invokeStmt("foo", method(voidType, DefaultClassContext, "m1")));
		setupDefaultEnclosingMethod(varDecl("foo", objectType),
				assign("foo", constructor(stringType)), ifElseBlock,
				invokeStmt("foo", method(voidType, DefaultClassContext, "m2")));

		extractHistories();

		AbstractHistory abstractHistory = createAbstractHistory(//
				Lists.newArrayList(
				//
						constructorCall(), //
						callInDefaultContextAsReceiver("m1"), //
						callInDefaultContextAsReceiver("m2")), //
				Sets.newHashSet(
						//
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2")), //
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m2"))));

		assertHistories(abstractHistory);
	}

	@Test
	public void tryCatchBlockCreatesMultipleConcreteHistories() {
		CatchBlock catchBlock = new CatchBlock();
		catchBlock.getBody().add(
				invokeStmt("foo", method(voidType, DefaultClassContext, "m2")));
		CatchBlock catchBlock2 = new CatchBlock();
		catchBlock2.getBody().add(
				invokeStmt("foo", method(voidType, DefaultClassContext, "m3")));

		TryBlock tryBlock = new TryBlock();
		tryBlock.getBody().add(
				invokeStmt("foo", method(voidType, DefaultClassContext, "m1")));
		tryBlock.setCatchBlocks(Lists.newArrayList(catchBlock, catchBlock2));
		tryBlock.getFinally().add(
				invokeStmt("foo", method(voidType, DefaultClassContext, "m4")));

		setupDefaultEnclosingMethod(varDecl("foo", objectType),
				assign("foo", constructor(stringType)), tryBlock);

		extractHistories();

		AbstractHistory abstractHistory = createAbstractHistory(//
				Lists.newArrayList(
						//
						constructorCall(),
						callInDefaultContextAsReceiver("m1"),
						callInDefaultContextAsReceiver("m2"),
						callInDefaultContextAsReceiver("m3"),
						callInDefaultContextAsReceiver("m4")), //
				Sets.newHashSet(
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m4")),
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2"),
								callInDefaultContextAsReceiver("m4")),
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m3"),
								callInDefaultContextAsReceiver("m4"))));
		assertHistories(abstractHistory);
	}

	@Test
	public void doLoopCreatesMultipleConcreteHistories() {
		DoLoop doLoop = new DoLoop();
		doLoop.getBody().add(
				invokeStmt("foo", method(voidType, DefaultClassContext, "m1")));
		setupDefaultEnclosingMethod(varDecl("foo", objectType),
				assign("foo", constructor(stringType)), doLoop,
				invokeStmt("foo", method(voidType, DefaultClassContext, "m2")));

		extractHistories();

		AbstractHistory abstractHistory = createAbstractHistory(//
				Lists.newArrayList(
						//
						constructorCall(),
						callInDefaultContextAsReceiver("m1"),
						callInDefaultContextAsReceiver("m1"),
						callInDefaultContextAsReceiver("m2")), //
				Sets.newHashSet(
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2")),
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2"))));

		assertHistories(abstractHistory);
	}

	@Test
	public void forEachLoopCreatesMultipleConcreteHistories() {
		ForEachLoop forEachLoop = new ForEachLoop();
		forEachLoop.getBody().add(
				invokeStmt("foo", method(voidType, DefaultClassContext, "m1")));
		setupDefaultEnclosingMethod(varDecl("foo", objectType),
				assign("foo", constructor(stringType)), forEachLoop,
				invokeStmt("foo", method(voidType, DefaultClassContext, "m2")));

		extractHistories();

		AbstractHistory abstractHistory = createAbstractHistory(//
				Lists.newArrayList(
						//
						constructorCall(),
						callInDefaultContextAsReceiver("m1"),
						callInDefaultContextAsReceiver("m1"),
						callInDefaultContextAsReceiver("m2")), //
				Sets.newHashSet(
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m2")),
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2")),
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2"))));

		assertHistories(abstractHistory);
	}

	@Test
	public void forLoopCreatesMultipleConcreteHistories() {
		ForLoop forLoop = new ForLoop();
		LoopHeaderBlockExpression loopHeader = new LoopHeaderBlockExpression();
		loopHeader.getBody().add(
				invokeStmt("foo", method(voidType, DefaultClassContext, "m2")));
		forLoop.setCondition(loopHeader);
		forLoop.getInit().add(
				invokeStmt("foo", method(voidType, DefaultClassContext, "m1")));
		forLoop.getBody().add(
				invokeStmt("foo", method(voidType, DefaultClassContext, "m3")));
		forLoop.getStep().add(
				invokeStmt("foo", method(voidType, DefaultClassContext, "m4")));
		setupDefaultEnclosingMethod(varDecl("foo", objectType),
				assign("foo", constructor(stringType)), forLoop,
				invokeStmt("foo", method(voidType, DefaultClassContext, "m5")));

		extractHistories();

		AbstractHistory abstractHistory = createAbstractHistory(//
				Lists.newArrayList(
						//
						constructorCall(),
						callInDefaultContextAsReceiver("m1"), //
						callInDefaultContextAsReceiver("m2"), //
						callInDefaultContextAsReceiver("m3"), //
						callInDefaultContextAsReceiver("m4"), //
						callInDefaultContextAsReceiver("m2"), //
						callInDefaultContextAsReceiver("m3"), //
						callInDefaultContextAsReceiver("m4"), //
						callInDefaultContextAsReceiver("m2"), //
						callInDefaultContextAsReceiver("m5")), //
				Sets.newHashSet(
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2"),
								callInDefaultContextAsReceiver("m5")),
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2"),
								callInDefaultContextAsReceiver("m3"),
								callInDefaultContextAsReceiver("m4"),
								callInDefaultContextAsReceiver("m2"),
								callInDefaultContextAsReceiver("m5")),
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2"),
								callInDefaultContextAsReceiver("m3"),
								callInDefaultContextAsReceiver("m4"),
								callInDefaultContextAsReceiver("m2"),
								callInDefaultContextAsReceiver("m3"),
								callInDefaultContextAsReceiver("m4"),
								callInDefaultContextAsReceiver("m2"),
								callInDefaultContextAsReceiver("m5"))));

		assertHistories(abstractHistory);
	}

	@Test
	public void whileLoopCreatesMultipleConcreteHistories() {
		WhileLoop whileLoop = new WhileLoop();
		whileLoop.getBody().add(
				invokeStmt("foo", method(voidType, DefaultClassContext, "m1")));
		setupDefaultEnclosingMethod(varDecl("foo", objectType),
				assign("foo", constructor(stringType)), whileLoop,
				invokeStmt("foo", method(voidType, DefaultClassContext, "m2")));

		extractHistories();

		AbstractHistory abstractHistory = createAbstractHistory(//
				Lists.newArrayList(
						//
						constructorCall(),
						callInDefaultContextAsReceiver("m1"),
						callInDefaultContextAsReceiver("m1"),
						callInDefaultContextAsReceiver("m2")), //
				Sets.newHashSet(
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m2")),
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2")),
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2"))));

		assertHistories(abstractHistory);
	}

	@Test
	public void returnCreatesAdditionalConcreteHistories() {
		IfElseBlock ifElseBlock = new IfElseBlock();
		ifElseBlock.getThen().add(
				invokeStmt("foo", method(voidType, DefaultClassContext, "m1")));
		ifElseBlock.setElse(Lists.newArrayList(
				invokeStmt("foo", method(voidType, DefaultClassContext, "m2")),
				new ReturnStatement()));
		setupDefaultEnclosingMethod(varDecl("foo", objectType),
				assign("foo", constructor(stringType)), ifElseBlock,
				invokeStmt("foo", method(voidType, DefaultClassContext, "m3")));

		extractHistories();

		AbstractHistory abstractHistory = createAbstractHistory(//
				Lists.newArrayList(
						//
						constructorCall(),
						callInDefaultContextAsReceiver("m1"),
						callInDefaultContextAsReceiver("m2"), //
						callInDefaultContextAsReceiver("m3")), //
				Sets.newHashSet(
						//
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m3")), //
						createConcreteHistory(
								//
								constructorCall(),
								callInDefaultContextAsReceiver("m2"))));

		assertHistories(abstractHistory);
	}

	@Test
	public void supportsProperties() {
		PropertyDeclaration propertyDecl = new PropertyDeclaration();
		IPropertyName propertyName = Names
				.newProperty(String
						.format("get set [%1$s] [%2$s].%3$s()", intType.getIdentifier(),
								DefaultClassContext.getIdentifier(),
						"SomeProperty"));
		propertyDecl.setName(propertyName);

		PropertyReference propertyReference = new PropertyReference();
		propertyReference.setPropertyName(propertyName);

		Assignment assignment1 = new Assignment();
		assignment1.setExpression(referenceExpr(propertyReference));

		Assignment assignment2 = new Assignment();
		assignment2.setReference(propertyReference);

		SST sst = sst(
				DefaultClassContext,
				methodDecl(DefaultMethodContext, true, assignment1, assignment2));
		sst.getProperties().add(propertyDecl);

		context = getContextFor(sst, new TypeHierarchy());

		extractHistories();

		AbstractHistory abstractHistory = createAbstractHistory(
				new Interaction(method(intType, DefaultClassContext,
						"SomeProperty"), 0, InteractionType.PROPERTY_GET),
				new Interaction(method(intType, DefaultClassContext,
						"SomeProperty"), 0, InteractionType.PROPERTY_SET));

		assertThat(historyMap, Matchers.hasValue(abstractHistory));
	}

	private void extractHistories() {
		sut = new RaychevAnalysisVisitor(context);
		context.getSST().accept(sut, historyMap);
	}
}
