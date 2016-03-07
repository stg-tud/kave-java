/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Sebastian Proksch - initial API and implementation
 */
package com.codetrails.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.CoReTypeName;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

@SuppressWarnings("unchecked")
public class UsageConverterTest {

	private ICoReTypeName usageType;
	private ICoReTypeName superType;
	private ICoReTypeName firstType;
	private ICoReMethodName firstMethod;
	private ICoReMethodName enclosingMethod;
	private Set<List<CallSite>> paths;

	private UsageConverter sut;

	@Before
	public void setup() {
		usageType = CoReTypeName.get("LU");
		enclosingMethod = CoReMethodName.get("LT.m()V");
		firstType = CoReTypeName.get("LI");
		firstMethod = CoReMethodName.get("LI.m()V");
		superType = CoReTypeName.get("LS");
		paths = Sets.newLinkedHashSet();
		addPaths(path(r(1), r(2), p(3)), path(p(4), r(5)));

		sut = new UsageConverter();
	}

	@Test
	public void defaultCase() {
		Usage actual = sut.toRecommenderUsage(createUsage());

		Query expected = new Query();
		expected.setType(usageType);
		expected.setClassContext(superType);
		expected.setMethodContext(firstMethod);
		expected.setDefinition(cc.recommenders.usages.DefinitionSites.createDefinitionByConstant());
		expected.setAllCallsites(_(r_new(1), r_new(2), p_new(3), p_new(4), r_new(5)));

		assertUsage(expected, actual);
	}

	@Test
	public void superClassIsNull() {
		superType = null;
		Usage actual = sut.toRecommenderUsage(createUsage());

		Query expected = new Query();
		expected.setType(usageType);
		expected.setClassContext(enclosingMethod.getDeclaringType());
		expected.setMethodContext(firstMethod);
		expected.setDefinition(cc.recommenders.usages.DefinitionSites.createDefinitionByConstant());
		expected.setAllCallsites(_(r_new(1), r_new(2), p_new(3), p_new(4), r_new(5)));

		assertUsage(expected, actual);
	}

	@Test
	public void introducedByIsNull() {
		firstType = null;
		Usage actual = sut.toRecommenderUsage(createUsage());

		Query expected = new Query();
		expected.setType(usageType);
		expected.setClassContext(superType);
		expected.setMethodContext(enclosingMethod);
		expected.setDefinition(cc.recommenders.usages.DefinitionSites.createDefinitionByConstant());
		expected.setAllCallsites(_(r_new(1), r_new(2), p_new(3), p_new(4), r_new(5)));

		assertUsage(expected, actual);
	}

	@Test
	public void initsAreNotCopiedAsCallSites() {
		ICoReMethodName init = CoReMethodName.get("LT.<init>()V");
		assertTrue(init.isInit());
		CallSite initCS = CallSites.createReceiverCallSite(init);
		ObjectUsage ou = createUsage();
		ou.setDef(DefinitionSites.createDefinitionByConstructor(init));
		ou.getPaths().iterator().next().add(initCS);

		Usage u = sut.toRecommenderUsage(ou);
		for (cc.recommenders.usages.CallSite cs : u.getAllCallsites()) {
			assertFalse(cs.getMethod().isInit());
		}
	}

	@Test
	public void nonInitDefinitionsWithConstructorCallAreRewritten() {
		ICoReMethodName init = CoReMethodName.get("LT.<init>()V");
		assertTrue(init.isInit());
		ObjectUsage ou = createUsage();
		ou.setDef(DefinitionSites.createUnknownDefinitionSite());
		ou.getPaths().iterator().next().add(CallSites.createReceiverCallSite(init));

		Usage u = sut.toRecommenderUsage(ou);
		assertEquals(cc.recommenders.usages.DefinitionSiteKind.NEW, u.getDefinitionSite().getKind());
		assertEquals(init, u.getDefinitionSite().getMethod());
	}

	@Test
	@Ignore
	public void moreTestsForDefinitionSiteConversion() {
		fail();
	}

	private Set<cc.recommenders.usages.CallSite> _(cc.recommenders.usages.CallSite... css) {
		Set<cc.recommenders.usages.CallSite> sites = Sets.newLinkedHashSet();
		for (cc.recommenders.usages.CallSite cs : css) {
			sites.add(cs);
		}
		return sites;
	}

	private ObjectUsage createUsage() {
		EnclosingMethodContext ctx = new EnclosingMethodContext();
		ctx.setAnnotations(new ICoReTypeName[0]);
		ctx.setImplementors(new ICoReTypeName[0]);
		ctx.setIntroducedBy(firstType);
		ctx.setName(enclosingMethod);
		ctx.setSuperclass(superType);

		DefinitionSite def = DefinitionSites.createDefinitionByConstant();
		def.setType(usageType);

		ObjectUsage ou = new ObjectUsage();
		ou.setUuid(UUID.randomUUID());
		ou.setContext(ctx);
		ou.setDef(def);
		ou.setPaths(paths);
		return ou;
	}

	private void addPaths(List<CallSite>... paths) {
		for (List<CallSite> path : paths) {
			this.paths.add(path);
		}
	}

	private static List<CallSite> path(CallSite... path) {
		List<CallSite> res = Lists.newLinkedList();
		for (CallSite cs : path) {
			res.add(cs);
		}
		return res;
	}

	private static CallSite p(int i) {
		String methodName = String.format("LT.r%d()V", i);
		return CallSites.createParameterCallSite(methodName, i);
	}

	private static CallSite r(int i) {
		String methodName = String.format("LT.r%d()V", i);
		return CallSites.createReceiverCallSite(methodName);
	}

	private static cc.recommenders.usages.CallSite p_new(int i) {
		String methodName = String.format("LT.r%d()V", i);
		return cc.recommenders.usages.CallSites.createParameterCallSite(methodName, i);
	}

	private static cc.recommenders.usages.CallSite r_new(int i) {
		String methodName = String.format("LT.r%d()V", i);
		return cc.recommenders.usages.CallSites.createReceiverCallSite(methodName);
	}

	private static void assertUsage(Query expected, Usage actual) {
		assertEquals(expected.getType(), actual.getType());
		assertEquals(expected.getClassContext(), actual.getClassContext());
		assertEquals(expected.getMethodContext(), actual.getMethodContext());
		assertEquals(expected.getDefinitionSite(), actual.getDefinitionSite());
		assertEquals(expected.getAllCallsites(), actual.getAllCallsites());
	}
}