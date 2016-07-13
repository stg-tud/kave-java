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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Test;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.csharp.PropertyName;
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
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.PointsToContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import exec.recommender_reimplementation.pbn.PBNAnalysisBaseTest;

public class RaychevAnalysisVisitorTest extends PBNAnalysisBaseTest {

	@Test
	public void addsNewAbstractHistoryForObjectAllocation() {
		setupDefaultEnclosingMethod(varDecl("foo", objectType), assign("foo", constructor(stringType)));

		Map<Set<AbstractLocation>, AbstractHistory> historyMap = new HashMap<>();
		context.getSST().accept(new RaychevAnalysisVisitor(context), historyMap);

		AbstractHistory abstractHistory = getAbstractHistoryWithInteractions(new Interaction(method(voidType,
				stringType, ".ctor"), Interaction.RETURN, InteractionType.MethodCall));

		assertEquals(1, historyMap.size());
		assertThat(historyMap, Matchers.hasValue(abstractHistory));
	}

	@Test
	public void addsInteractionForInvocations() {
		IMethodName methodName = method(stringType, DefaultClassContext, "m1", parameter(stringType, "p1"));
		setupDefaultEnclosingMethod(varDecl("bar", objectType), assign("bar", constant("someValue")),
				varDecl("foo", objectType), assign("foo", constant("someValue2")), varDecl("foobar", stringType),
				assign("foobar", invokeWithParameters("bar", methodName, referenceExpr(varRef("foo")))));

		Map<Set<AbstractLocation>, AbstractHistory> historyMap = new HashMap<>();
		context.getSST().accept(new RaychevAnalysisVisitor(context), historyMap);

		AbstractHistory abstractHistoryFooBar = getAbstractHistoryWithInteractions(new Interaction(methodName,
				Interaction.RETURN, InteractionType.MethodCall));
		AbstractHistory abstractHistoryBar = getAbstractHistoryWithInteractions(new Interaction(methodName, 0,
				InteractionType.MethodCall));
		AbstractHistory abstractHistoryFoo = getAbstractHistoryWithInteractions(new Interaction(methodName, 1,
				InteractionType.MethodCall));

		assertThat(historyMap, Matchers.hasValue(abstractHistoryFooBar));
		assertThat(historyMap, Matchers.hasValue(abstractHistoryBar));
		assertThat(historyMap, Matchers.hasValue(abstractHistoryFoo));
	}

	@Test
	public void ifElseBlockCreatesMultipleConcreteHistories() {
		IIfElseBlock ifElseBlock = new IfElseBlock();
		ifElseBlock.getThen().add(invokeStmt("foo", method(voidType, DefaultClassContext, "m1")));
		ifElseBlock.getElse().add(invokeStmt("foo", method(voidType, DefaultClassContext, "m2")));
		setupDefaultEnclosingMethod(varDecl("foo", objectType), assign("foo", constructor(stringType)), ifElseBlock,
				invokeStmt("foo", method(voidType, DefaultClassContext, "m3")));

		Map<Set<AbstractLocation>, AbstractHistory> historyMap = new HashMap<>();
		context.getSST().accept(new RaychevAnalysisVisitor(context), historyMap);

		assertEquals(1, historyMap.size());

		AbstractHistory actual = (AbstractHistory) historyMap.values().toArray()[0];

		assertThat(actual.getAbstractHistory(), Matchers.contains(new Interaction(
				method(voidType, stringType, ".ctor"), Interaction.RETURN, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m1"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m2"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m3"), 0, InteractionType.MethodCall)));

		assertEquals(2, actual.getHistorySet().size());

		assertThat(actual.getHistorySet(), Matchers.containsInAnyOrder(
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m3"), 0,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m3"), 0,
						InteractionType.MethodCall))));
	}
	
	@Test
	public void ifWithoutElseBlockCreatesMultipleConcreteHistories() {
		IIfElseBlock ifElseBlock = new IfElseBlock();
		ifElseBlock.getThen().add(invokeStmt("foo", method(voidType, DefaultClassContext, "m1")));
		setupDefaultEnclosingMethod(varDecl("foo", objectType), assign("foo", constructor(stringType)), ifElseBlock,
				invokeStmt("foo", method(voidType, DefaultClassContext, "m2")));

		Map<Set<AbstractLocation>, AbstractHistory> historyMap = new HashMap<>();
		context.getSST().accept(new RaychevAnalysisVisitor(context), historyMap);

		assertEquals(1, historyMap.size());

		AbstractHistory actual = (AbstractHistory) historyMap.values().toArray()[0];

		assertThat(actual.getAbstractHistory(), Matchers.contains(new Interaction(
				method(voidType, stringType, ".ctor"), Interaction.RETURN, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m1"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m2"), 0, InteractionType.MethodCall)));

		assertEquals(2, actual.getHistorySet().size());

		assertThat(actual.getHistorySet(), Matchers.containsInAnyOrder(
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall))));
	}

	@Test
	public void tryCatchBlockCreatesMultipleConcreteHistories() {
		CatchBlock catchBlock = new CatchBlock();
		catchBlock.getBody().add(invokeStmt("foo", method(voidType, DefaultClassContext, "m2")));
		CatchBlock catchBlock2 = new CatchBlock();
		catchBlock2.getBody().add(invokeStmt("foo", method(voidType, DefaultClassContext, "m3")));

		TryBlock tryBlock = new TryBlock();
		tryBlock.getBody().add(invokeStmt("foo", method(voidType, DefaultClassContext, "m1")));
		tryBlock.setCatchBlocks(Lists.newArrayList(catchBlock, catchBlock2));
		tryBlock.getFinally().add(invokeStmt("foo", method(voidType, DefaultClassContext, "m4")));

		setupDefaultEnclosingMethod(varDecl("foo", objectType), assign("foo", constructor(stringType)), tryBlock);

		Map<Set<AbstractLocation>, AbstractHistory> historyMap = new HashMap<>();
		context.getSST().accept(new RaychevAnalysisVisitor(context), historyMap);

		assertEquals(1, historyMap.size());

		AbstractHistory actual = (AbstractHistory) historyMap.values().toArray()[0];

		assertThat(actual.getAbstractHistory(), Matchers.contains(new Interaction(
				method(voidType, stringType, ".ctor"), Interaction.RETURN, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m1"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m2"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m3"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m4"), 0, InteractionType.MethodCall)));

		assertEquals(3, actual.getHistorySet().size());

		assertThat(actual.getHistorySet(), Matchers.containsInAnyOrder(
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m4"), 0,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m4"), 0,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m3"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m4"), 0,
						InteractionType.MethodCall))));
	}

	@Test
	public void doLoopCreatesMultipleConcreteHistories() {
		DoLoop doLoop = new DoLoop();
		doLoop.getBody().add(invokeStmt("foo", method(voidType, DefaultClassContext, "m1")));
		setupDefaultEnclosingMethod(varDecl("foo", objectType), assign("foo", constructor(stringType)), doLoop,
				invokeStmt("foo", method(voidType, DefaultClassContext, "m2")));

		Map<Set<AbstractLocation>, AbstractHistory> historyMap = new HashMap<>();
		context.getSST().accept(new RaychevAnalysisVisitor(context), historyMap);

		assertEquals(1, historyMap.size());

		AbstractHistory actual = (AbstractHistory) historyMap.values().toArray()[0];

		assertThat(actual.getAbstractHistory(), Matchers.contains(new Interaction(
				method(voidType, stringType, ".ctor"), Interaction.RETURN, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m1"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m1"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m2"), 0, InteractionType.MethodCall)));

		assertEquals(2, actual.getHistorySet().size());

		assertThat(actual.getHistorySet(), Matchers.containsInAnyOrder(
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall))));
	}

	@Test
	public void forEachLoopCreatesMultipleConcreteHistories() {
		ForEachLoop forEachLoop = new ForEachLoop();
		forEachLoop.getBody().add(invokeStmt("foo", method(voidType, DefaultClassContext, "m1")));
		setupDefaultEnclosingMethod(varDecl("foo", objectType), assign("foo", constructor(stringType)), forEachLoop,
				invokeStmt("foo", method(voidType, DefaultClassContext, "m2")));

		Map<Set<AbstractLocation>, AbstractHistory> historyMap = new HashMap<>();
		context.getSST().accept(new RaychevAnalysisVisitor(context), historyMap);

		assertEquals(1, historyMap.size());

		AbstractHistory actual = (AbstractHistory) historyMap.values().toArray()[0];

		assertThat(actual.getAbstractHistory(), Matchers.contains(new Interaction(
				method(voidType, stringType, ".ctor"), Interaction.RETURN, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m1"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m1"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m2"), 0, InteractionType.MethodCall)));

		assertEquals(3, actual.getHistorySet().size());

		assertThat(actual.getHistorySet(), Matchers.containsInAnyOrder(
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall))));
	}

	@Test
	public void forLoopCreatesMultipleConcreteHistories() {
		ForLoop forLoop = new ForLoop();
		forLoop.getInit().add(invokeStmt("foo", method(voidType, DefaultClassContext, "m1")));
		forLoop.getStep().add(invokeStmt("foo", method(voidType, DefaultClassContext, "m2")));
		forLoop.getBody().add(invokeStmt("foo", method(voidType, DefaultClassContext, "m3")));
		setupDefaultEnclosingMethod(varDecl("foo", objectType), assign("foo", constructor(stringType)), forLoop,
				invokeStmt("foo", method(voidType, DefaultClassContext, "m4")));

		Map<Set<AbstractLocation>, AbstractHistory> historyMap = new HashMap<>();
		context.getSST().accept(new RaychevAnalysisVisitor(context), historyMap);

		assertEquals(1, historyMap.size());

		AbstractHistory actual = (AbstractHistory) historyMap.values().toArray()[0];

		assertThat(actual.getAbstractHistory(), Matchers.contains(new Interaction(
				method(voidType, stringType, ".ctor"), Interaction.RETURN, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m1"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m2"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m3"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m1"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m2"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m3"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m4"), 0, InteractionType.MethodCall)));

		assertEquals(3, actual.getHistorySet().size());

		assertThat(actual.getHistorySet(), Matchers.containsInAnyOrder(
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m4"), 0,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m3"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m4"), 0,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m3"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m3"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m4"), 0,
						InteractionType.MethodCall))));
	}

	@Test
	public void whileLoopCreatesMultipleConcreteHistories() {
		WhileLoop whileLoop = new WhileLoop();
		whileLoop.getBody().add(invokeStmt("foo", method(voidType, DefaultClassContext, "m1")));
		setupDefaultEnclosingMethod(varDecl("foo", objectType), assign("foo", constructor(stringType)), whileLoop,
				invokeStmt("foo", method(voidType, DefaultClassContext, "m2")));

		Map<Set<AbstractLocation>, AbstractHistory> historyMap = new HashMap<>();
		context.getSST().accept(new RaychevAnalysisVisitor(context), historyMap);

		assertEquals(1, historyMap.size());

		AbstractHistory actual = (AbstractHistory) historyMap.values().toArray()[0];

		assertThat(actual.getAbstractHistory(), Matchers.contains(new Interaction(
				method(voidType, stringType, ".ctor"), Interaction.RETURN, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m1"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m1"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m2"), 0, InteractionType.MethodCall)));

		assertEquals(3, actual.getHistorySet().size());

		assertThat(actual.getHistorySet(), Matchers.containsInAnyOrder(
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall))));
	}

	@Test
	public void returnCreatesAdditionalConcreteHistories() {
		IfElseBlock ifElseBlock = new IfElseBlock();
		ifElseBlock.getThen().add(invokeStmt("foo", method(voidType, DefaultClassContext, "m1")));
		ifElseBlock.setElse(Lists.newArrayList(invokeStmt("foo", method(voidType, DefaultClassContext, "m2")),
				new ReturnStatement()));
		setupDefaultEnclosingMethod(varDecl("foo", objectType), assign("foo", constructor(stringType)), ifElseBlock,
				invokeStmt("foo", method(voidType, DefaultClassContext, "m3")));

		Map<Set<AbstractLocation>, AbstractHistory> historyMap = new HashMap<>();
		context.getSST().accept(new RaychevAnalysisVisitor(context), historyMap);

		assertEquals(1, historyMap.size());

		AbstractHistory actual = (AbstractHistory) historyMap.values().toArray()[0];

		assertThat(actual.getAbstractHistory(), Matchers.contains(new Interaction(
				method(voidType, stringType, ".ctor"), Interaction.RETURN, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m1"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m2"), 0, InteractionType.MethodCall),
				new Interaction(method(voidType, DefaultClassContext, "m3"), 0, InteractionType.MethodCall)));

		assertEquals(3, actual.getHistorySet().size());

		assertThat(actual.getHistorySet(), Matchers.containsInAnyOrder(
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m1"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m3"), 0,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(voidType, stringType, ".ctor"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m2"), 0,
						InteractionType.MethodCall), new Interaction(method(voidType, DefaultClassContext, "m3"), 0,
						InteractionType.MethodCall))));
	}

	@Test
	public void supportsProperties() {
		PropertyDeclaration propertyDecl = new PropertyDeclaration();
		IPropertyName propertyName = PropertyName.newPropertyName(String.format("[%1$s] [%2$s].%3$s", intType,
				DefaultClassContext, "SomeProperty"));
		propertyDecl.setName(propertyName);

		PropertyReference propertyReference = new PropertyReference();
		propertyReference.setPropertyName(propertyName);

		Assignment assignment1 = new Assignment();
		assignment1.setExpression(referenceExpr(propertyReference));

		Assignment assignment2 = new Assignment();
		assignment2.setReference(propertyReference);

		SST sst = sst(DefaultClassContext, methodDecl(DefaultMethodContext, true, assignment1, assignment2));
		sst.getProperties().add(propertyDecl);

		PointsToContext context = getContextFor(sst, new TypeHierarchy());
		Map<Set<AbstractLocation>, AbstractHistory> historyMap = new HashMap<>();

		context.getSST().accept(new RaychevAnalysisVisitor(context), historyMap);

		AbstractHistory abstractHistory = getAbstractHistoryWithInteractions(
				new Interaction(method(intType, DefaultClassContext, "SomeProperty"), 0, InteractionType.PropertyGet),
				new Interaction(method(intType, DefaultClassContext, "SomeProperty"), 0, InteractionType.PropertySet));

		assertThat(historyMap, Matchers.hasValue(abstractHistory));
	}

	@Test
	public void evictsRandomEntryFromHashmap() {
		Map<Set<AbstractLocation>, AbstractHistory> historyMap = new HashMap<>();
		historyMap.put(Sets.newHashSet(new AbstractLocation()), new AbstractHistory());
		historyMap.put(Sets.newHashSet(new AbstractLocation()), new AbstractHistory());
		historyMap.put(Sets.newHashSet(new AbstractLocation()), new AbstractHistory());

		setupDefaultEnclosingMethod();

		RaychevAnalysisVisitor uut = new RaychevAnalysisVisitor(context);

		uut.evictRandomAbstractHistory(historyMap);

		assertEquals(2, historyMap.size());
	}

	private AbstractHistory getAbstractHistoryWithInteractions(Interaction... interactions) {
		Set<ConcreteHistory> historySet = new HashSet<>();
		historySet.add(new ConcreteHistory(Lists.newArrayList(interactions)));
		return new AbstractHistory(Lists.newArrayList(interactions), historySet);
	}

	public void assertConcreteHistoryContainsInteraction(ConcreteHistory concreteHistory, IMethodName methodName,
			int position) {
		assertThat(concreteHistory.getHistory(),
				Matchers.contains(new Interaction(methodName, position, InteractionType.MethodCall)));
	}
}
