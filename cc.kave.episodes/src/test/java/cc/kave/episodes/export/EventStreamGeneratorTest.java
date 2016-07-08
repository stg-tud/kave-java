/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.episodes.export;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Events;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.MethodHierarchy;

public class EventStreamGeneratorTest {

	private EventStreamGenerator sut;

	@Before
	public void setup() {
		sut = new EventStreamGenerator();
	}

	@Test
	public void notOverwritingAnything() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, m(1, 1), null, null);

		ctx.setSST(sst(1, methodDecl(1, 1, //
				inv("o", m(2, 3)))));

		sut.add(ctx);

		assertStream(Events.newFirstContext(unknown()), //
				Events.newInvocation(m(2, 3)));

	}

	@Test
	public void usesSuperMethod() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, m(1, 1), m(3, 1), null);

		ctx.setSST(sst(1, methodDecl(1, 1, //
				inv("o", m(2, 3)))));

		sut.add(ctx);

		assertStream(Events.newFirstContext(unknown()), //
					Events.newSuperContext(m(3, 1)), //
					Events.newInvocation(m(2, 3)));

	}

	@Test
	public void prefersFirstOverSuperContext() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, m(1, 1), m(3, 1), m(4, 1));

		ctx.setSST(sst(1, methodDecl(1, 1, //
				inv("o", m(2, 3)))));

		sut.add(ctx);

		assertStream(Events.newFirstContext(m(4, 1)), //
				Events.newSuperContext(m(3, 1)), //
				Events.newInvocation(m(2, 3)));

	}

	@Test
	public void doesNotAddEmptyMethods() {
		Context ctx = new Context();

		ctx.setSST(sst(1, methodDecl(1, 1)));

		sut.add(ctx);

		assertStream();

	}

	@Test
	public void doesNotAddUnknownMethodCalls() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, m(1, 1), null, null);

		ctx.setSST(sst(1,
				methodDecl(1, 1, //
						inv("o", m(2, 3)), //
						inv("o", unknown()))));

		sut.add(ctx);

		assertStream(Events.newFirstContext(unknown()), //
				Events.newInvocation(m(2, 3)));
	}

	@Test
	public void unknownCallsDoNotCountAsInput() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, m(1, 1), null, null);

		ctx.setSST(sst(1, methodDecl(1, 1, //
				inv("o", unknown()))));

		sut.add(ctx);

		assertStream();
	}

	@Test
	public void multipleMethodDeclsWork() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, m(1, 1), null, m(11, 1));
		addMethodHierarchy(ctx, m(1, 2), null, m(12, 2));

		ctx.setSST(sst(1,
				methodDecl(1, 1, //
						inv("o", m(2, 3))), //
				methodDecl(1, 2, //
						inv("o", m(3, 4)))));

		sut.add(ctx);

		assertStream(Events.newFirstContext(m(11, 1)), //
				Events.newInvocation(m(2, 3)), //
				Events.newFirstContext(m(12, 2)), //
				Events.newInvocation(m(3, 4)) //
		);
	}

	@Test
	public void multipleContextsWork() {
		Context ctx1 = new Context();
		Context ctx2 = new Context();

		addMethodHierarchy(ctx1, m(1, 1), null, m(11, 1));
		addMethodHierarchy(ctx2, m(2, 1), null, m(12, 1));

		ctx1.setSST(sst(1, methodDecl(1, 1, //
				inv("o", m(2, 3)))));

		ctx2.setSST(sst(2, methodDecl(2, 1, //
				inv("o", m(3, 4)))));

		sut.add(ctx1);
		sut.add(ctx2);

		assertStream(Events.newFirstContext(m(11, 1)), //
				Events.newInvocation(m(2, 3)), //
				Events.newFirstContext(m(12, 1)), //
				Events.newInvocation(m(3, 4))//
		);
	}

	private IMethodName unknown() {
		return MethodName.UNKNOWN_NAME;
	}

	private IStatement inv(String string, IMethodName m) {
		InvocationExpression expr = new InvocationExpression();
		expr.setMethodName(m);
		ExpressionStatement stmt = new ExpressionStatement();
		stmt.setExpression(expr);
		return stmt;
	}

	private void assertStream(Event... es) {
		List<Event> actuals = sut.getEventStream();
		List<Event> expecteds = Lists.newArrayList(es);
		assertEquals(expecteds, actuals);
	}

	private void addMethodHierarchy(Context ctx, IMethodName m, IMethodName s, IMethodName f) {
		MethodHierarchy h = new MethodHierarchy();
		h.setElement(m);
		if (s != null) {
			h.setSuper(s);
		}
		if (f != null) {
			h.setFirst(f);
		}
		Set<IMethodHierarchy> hs = ctx.getTypeShape().getMethodHierarchies();
		hs.add(h);
	}

	private ISST sst(int typeNum, IMethodDeclaration... decls) {
		SST sst = new SST();
		sst.setEnclosingType(t(typeNum));
		sst.setMethods(Sets.newHashSet(decls));
		return sst;
	}

	private IMethodDeclaration methodDecl(int typeNum, int methodNum, IStatement... stmtArr) {
		MethodDeclaration decl = new MethodDeclaration();
		decl.setName(m(typeNum, methodNum));
		decl.setBody(Lists.newArrayList(stmtArr));
		return decl;
	}

	private IMethodName m(int typeNum, int methodNum) {
		return MethodName.newMethodName(String.format("[R,P] [%s].m%d()", t(typeNum), methodNum));
	}

	private ITypeName t(int typeNum) {
		return TypeName.newTypeName(String.format("T%d,P", typeNum));
	}
}