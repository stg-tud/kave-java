/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.episodes.export;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Fact;

public class ToFactsVisitorTest {

	private List<Event> eventMapping;
	private ToFactsVisitor sut;
	private MethodDeclaration decl;

	@Before
	public void setup() {
		eventMapping = Lists.newArrayList();
		decl = new MethodDeclaration();

		Event e = new Event();
		e.setKind(EventKind.METHOD_DECLARATION);
		e.setMethod(decl.getName());
		eventMapping.add(e);

		sut = new ToFactsVisitor(eventMapping);
	}

	@Test
	public void happyPath() {
		IStatement stmt1 = stmt(inv(1));
		IStatement stmt2 = stmt(inv(2));

		decl.getBody().add(stmt1);
		decl.getBody().add(stmt2);

		assertFacts(f(0), f(1), f(2), f(0, 1), f(0, 2), f(1, 2));

	}

	@Test
	public void happyPath2() {
		IStatement stmt1 = stmt(inv(1));
		IStatement stmt2 = stmt(inv(2));
		IStatement stmt3 = stmt(inv(1));
		IStatement stmt4 = stmt(inv(4));

		decl.getBody().add(stmt1);
		decl.getBody().add(stmt2);
		decl.getBody().add(stmt3);
		decl.getBody().add(stmt4);

		assertFacts(f(0), f(1), f(2), f(4), f(0, 1), f(0, 4), f(0, 2), f(1, 4), f(2, 4));

	}

	private IStatement stmt(IAssignableExpression expr) {
		ExpressionStatement stmt = new ExpressionStatement();
		stmt.setExpression(expr);
		return stmt;
	}

	private IInvocationExpression inv(int i) {
		IMethodName m = Names.newMethod(String.format("[T,P] [T,P].m%d()", i));

		InvocationExpression expr = new InvocationExpression();
		expr.setMethodName(m);

		Event e = new Event();
		e.setKind(EventKind.INVOCATION);
		e.setMethod(m);
		eventMapping.add(e);

		return expr;
	}

	private void assertFacts(Fact... fs) {
		Set<Fact> actuals = Sets.newLinkedHashSet();
		Set<Fact> expecteds = Sets.newLinkedHashSet();
		decl.accept(sut, actuals);
		for (Fact f : fs) {
			expecteds.add(f);
		}
		assertEquals(expecteds, actuals);
	}

	private static Fact f(int id) {
		return new Fact(id);
	}

	private static Fact f(int a, int b) {
		return new Fact(new Fact(a), new Fact(b));
	}
}