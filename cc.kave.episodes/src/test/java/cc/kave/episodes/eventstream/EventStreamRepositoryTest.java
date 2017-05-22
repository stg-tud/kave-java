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
package cc.kave.episodes.eventstream;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class EventStreamRepositoryTest {

	private EventStreamRepository sut;

	@Before
	public void setup() {
		sut = new EventStreamRepository();
	}

	@Test
	public void notOverwritingAnything() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, m(1, 1), null, null);

		ctx.setSST(sst(1, methodDecl(1, 1, //
				inv("o", m(2, 3)))));

		sut.add(ctx);

		assertStream(Events.newType(t(1)), //
				Events.newContext(m(1, 1)), Events.newInvocation(m(2, 3)));

	}

	@Test
	public void handlesGenerics() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, mGenericBound(1, 2, 3),
				mGenericBound(11, 12, 13), mGenericBound(21, 22, 23));

		ctx.setSST(sst(1, methodDeclGenericBound(1, 2, 3, //
				inv("o", mGenericBound(2, 3, 4)))));

		sut.add(ctx);

		assertStream(
				Events.newType(t(1)), //
				Events.newFirstContext(mGenericFree(21, 22)), //
				Events.newSuperContext(mGenericFree(11, 12)), //
				Events.newContext(mGenericFree(1, 2)),
				Events.newInvocation(mGenericFree(2, 3)));

	}

	@Test
	public void handlesPartialClass1() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, mGenericBound(1, 2, 3),
				mGenericBound(11, 12, 13), mGenericBound(21, 22, 23));

		ctx.setSST(sstPartialClass1(1, methodDeclGenericBound(1, 2, 3, //
				inv("o", mGenericBound(2, 3, 4)))));

		sut.add(ctx);

		assertStream(Events.newType(t(1)), //
				Events.newFirstContext(mGenericFree(21, 22)), //
				Events.newSuperContext(mGenericFree(11, 12)), //
				Events.newContext(mGenericFree(1, 2)), //
				Events.newInvocation(mGenericFree(2, 3)));

	}

	@Test
	public void handlesPartialClass2() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, mGenericBound(1, 2, 3),
				mGenericBound(11, 12, 13), mGenericBound(21, 22, 23));

		ctx.setSST(sstPartialClass2(1, methodDeclGenericBound(1, 2, 3, //
				inv("o", mGenericBound(2, 3, 4)))));

		sut.add(ctx);

		assertStream(Events.newType(t(1)), //
				Events.newFirstContext(mGenericFree(21, 22)), //
				Events.newSuperContext(mGenericFree(11, 12)), //
				Events.newContext(mGenericFree(1, 2)), //
				Events.newInvocation(mGenericFree(2, 3)));

	}

	@Test
	public void handlesUserCode() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, mGenericBound(1, 2, 3),
				mGenericBound(11, 12, 13), mGenericBound(21, 22, 23));

		ctx.setSST(sstUserCode(1, methodDeclGenericBound(1, 2, 3, //
				inv("o", mGenericBound(2, 3, 4)))));

		sut.add(ctx);

		assertStream(
				Events.newType(t(1)), //
				Events.newFirstContext(mGenericFree(21, 22)), //
				Events.newSuperContext(mGenericFree(11, 12)), //
				Events.newContext(mGenericFree(1, 2)),
				Events.newInvocation(mGenericFree(2, 3)));

	}

	@Test
	public void handlesGenericsFree() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, mGenericFree(1, 2), mGenericFree(11, 12),
				mGenericFree(21, 22));

		ctx.setSST(sst(1, methodDeclGenericFree(1, 2, //
				inv("o", mGenericFree(2, 3)))));

		sut.add(ctx);

		assertStream(
				Events.newType(t(1)), //
				Events.newFirstContext(mGenericFree(21, 22)), //
				Events.newSuperContext(mGenericFree(11, 12)), //
				Events.newContext(mGenericFree(1, 2)),
				Events.newInvocation(mGenericFree(2, 3)));

	}

	@Test
	public void handlesPartialFreeClass1() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, mGenericFree(1, 2), mGenericFree(11, 12),
				mGenericFree(21, 22));

		ctx.setSST(sstPartialClass1(1, methodDeclGenericFree(1, 2, //
				inv("o", mGenericBound(2, 3, 4)))));

		sut.add(ctx);

		assertStream(
				Events.newType(t(1)), //
				Events.newFirstContext(mGenericFree(21, 22)), //
				Events.newSuperContext(mGenericFree(11, 12)), //
				Events.newContext(mGenericFree(1, 2)), //
				Events.newInvocation(mGenericFree(2, 3)));

	}

	@Test
	public void handlesPartialFreeClass2() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, mGenericFree(1, 2), mGenericFree(11, 12),
				mGenericFree(21, 22));

		ctx.setSST(sstPartialClass2(1, methodDeclGenericFree(1, 2, //
				inv("o", mGenericFree(2, 3)))));

		sut.add(ctx);

		assertStream(Events.newType(t(1)), //
				Events.newFirstContext(mGenericFree(21, 22)), //
				Events.newSuperContext(mGenericFree(11, 12)), //
				Events.newContext(mGenericFree(1, 2)), //
				Events.newInvocation(mGenericFree(2, 3)));

	}

	@Test
	public void handlesUserFreeCode() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, mGenericFree(1, 2), mGenericFree(11, 12),
				mGenericFree(21, 22));

		ctx.setSST(sstUserCode(1, methodDeclGenericFree(1, 2, //
				inv("o", mGenericFree(2, 3)))));

		sut.add(ctx);

		assertStream(
				Events.newType(t(1)), //
				Events.newFirstContext(mGenericFree(21, 22)), //
				Events.newSuperContext(mGenericFree(11, 12)), //
				Events.newContext(mGenericFree(1, 2)),
				Events.newInvocation(mGenericFree(2, 3)));

	}

	@Test
	public void usesSuperMethod() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, m(1, 1), m(3, 1), null);

		ctx.setSST(sst(1, methodDecl(1, 1, //
				inv("o", m(2, 3)))));

		sut.add(ctx);

		assertStream(Events.newType(t(1)), //
				Events.newSuperContext(m(3, 1)), //
				Events.newContext(m(1, 1)), Events.newInvocation(m(2, 3)));

	}

	@Test
	public void prefersFirstOverSuperContext() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, m(1, 1), m(3, 1), m(4, 1));

		ctx.setSST(sst(1, methodDecl(1, 1, //
				inv("o", m(2, 3)))));

		sut.add(ctx);

		assertStream(Events.newType(t(1)), //
				Events.newFirstContext(m(4, 1)), //
				Events.newSuperContext(m(3, 1)), //
				Events.newContext(m(1, 1)), Events.newInvocation(m(2, 3)));

	}

	@Test
	public void doesNotAddEmptyMethods() {
		Context ctx = new Context();

		ctx.setSST(sst(1, methodDecl(1, 1)));

		sut.add(ctx);

		assertStream(Events.newType(t(1)));

	}

	@Test
	public void doesNotAddUnknownMethodCalls() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, m(1, 1), null, null);

		ctx.setSST(sst(1, methodDecl(1, 1, //
				inv("o", m(2, 3)), //
				inv("o", unknown()))));

		sut.add(ctx);

		assertStream(Events.newType(t(1)), //
				Events.newContext(m(1, 1)), //
				Events.newInvocation(m(2, 3)), //
				Events.newInvocation(Names.getUnknownMethod()));
	}

	@Test
	public void unknownCallsDoNotCountAsInput() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, m(1, 1), null, null);

		ctx.setSST(sst(1, methodDecl(1, 1, //
				inv("o", unknown()))));

		sut.add(ctx);

		assertStream(Events.newType(t(1)), //
				Events.newContext(m(1, 1)), //
				Events.newInvocation(Names.getUnknownMethod()));
	}

	@Test
	public void multipleMethodDeclsWork() {
		Context ctx = new Context();

		addMethodHierarchy(ctx, m(1, 1), null, m(11, 1));
		addMethodHierarchy(ctx, m(1, 2), null, m(12, 2));

		ctx.setSST(sst(1, methodDecl(1, 1, //
				inv("o", m(2, 3))), //
				methodDecl(1, 2, //
						inv("o", m(3, 4)))));

		sut.add(ctx);

		assertStream(Events.newType(t(1)), //
				Events.newFirstContext(m(11, 1)), //
				Events.newContext(m(1, 1)), Events.newInvocation(m(2, 3)), //
				Events.newFirstContext(m(12, 2)), //
				Events.newContext(m(1, 2)), Events.newInvocation(m(3, 4)) //
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

		assertStream(Events.newType(t(1)), //
				Events.newFirstContext(m(11, 1)), //
				Events.newContext(m(1, 1)), Events.newInvocation(m(2, 3)), //
				Events.newType(t(2)), //
				Events.newFirstContext(m(12, 1)), //
				Events.newContext(m(2, 1)), Events.newInvocation(m(3, 4))//
		);
	}

	private IMethodName unknown() {
		return Names.getUnknownMethod();
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

	private void addMethodHierarchy(Context ctx, IMethodName m, IMethodName s,
			IMethodName f) {
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

	private ISST sstUserCode(int typeNum, IMethodDeclaration... decls) {
		SST sst = new SST();
		sst.setEnclosingType(t(typeNum));
		sst.setMethods(Sets.newHashSet(decls));
		sst.setPartialClassIdentifier("fileName.cs");
		return sst;
	}

	private ISST sstPartialClass1(int typeNum, IMethodDeclaration... decls) {
		SST sst = new SST();
		sst.setEnclosingType(t(typeNum));
		sst.setMethods(Sets.newHashSet(decls));
		sst.setPartialClassIdentifier("fileName.designer.cs");
		return sst;
	}

	private ISST sstPartialClass2(int typeNum, IMethodDeclaration... decls) {
		SST sst = new SST();
		sst.setEnclosingType(t(typeNum));
		sst.setMethods(Sets.newHashSet(decls));
		sst.setPartialClassIdentifier("fileName.Designer.cs");
		return sst;
	}

	private IMethodDeclaration methodDecl(int typeNum, int methodNum,
			IStatement... stmtArr) {
		MethodDeclaration decl = new MethodDeclaration();
		decl.setName(m(typeNum, methodNum));
		decl.setBody(Lists.newArrayList(stmtArr));
		return decl;
	}

	private IMethodName m(int typeNum, int methodNum) {
		return Names.newMethod(String.format("[R,P] [%s].m%d()", t(typeNum),
				methodNum));
	}

	private ITypeName t(int typeNum) {
		return Names.newType(String.format("T%d,P", typeNum));
	}

	private IMethodDeclaration methodDeclGenericBound(int typeNum,
			int methodNum, int typeParamNum, IStatement... stmtArr) {
		MethodDeclaration decl = new MethodDeclaration();
		decl.setName(mGenericBound(typeNum, methodNum, typeParamNum));
		decl.setBody(Lists.newArrayList(stmtArr));
		return decl;
	}

	private IMethodDeclaration methodDeclGenericFree(int typeNum,
			int methodNum, IStatement... stmtArr) {
		MethodDeclaration decl = new MethodDeclaration();
		decl.setName(mGenericFree(typeNum, methodNum));
		decl.setBody(Lists.newArrayList(stmtArr));
		return decl;
	}

	private IMethodName mGenericBound(int typeNum, int methodNum,
			int typeParamNum) {
		return Names.newMethod(String.format("[R,P] [%s].m%d`1[[T -> %s]]()",
				tGenericBound(typeNum, typeParamNum), methodNum,
				t(typeParamNum)));
	}

	private IMethodName mGenericFree(int typeNum, int methodNum) {
		return Names.newMethod(String.format("[R,P] [%s].m%d`1[[T]]()",
				tGenericFree(typeNum), methodNum));
	}

	private ITypeName tGenericFree(int typeNum) {
		return Names.newType(String.format("T%d`1[[T]],P", typeNum));
	}

	private ITypeName tGenericBound(int typeNum, int typeParamNum) {
		return Names.newType(String.format("T%d`1[[T -> %s]],P", typeNum,
				t(typeParamNum)));
	}
}