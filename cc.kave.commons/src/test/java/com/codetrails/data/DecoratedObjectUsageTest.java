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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.CoReTypeName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSites;

import com.codetrails.data.EnclosingMethodContext;
import com.codetrails.data.ObjectUsage;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class DecoratedObjectUsageTest {

	private ObjectUsage usage;
	private ObjectUsageBuilder builder;

	private DecoratedObjectUsage sut;

	@Before
	public void setup() {
		usage = new ObjectUsage();
		builder = new ObjectUsageBuilder();
		sut = new DecoratedObjectUsage(usage);
	}

	@Test
	public void decoratedUsageIsAccessible() {
		ObjectUsage actual = sut.getOriginal();
		assertSame(usage, actual);
	}

	@Test
	public void typeIsPropagated() {
		ICoReTypeName expected = mock(ICoReTypeName.class);
		com.codetrails.data.DefinitionSite ds = new com.codetrails.data.DefinitionSite();
		ds.setType(expected);
		usage.setDef(ds);
		ICoReTypeName actual = sut.getType();
		assertEquals(expected, actual);
	}

	@Test
	public void classContextIsPropagated() {
		ICoReTypeName expected = mock(ICoReTypeName.class);
		EnclosingMethodContext ctx = new EnclosingMethodContext();
		ctx.setSuperclass(expected);
		usage.setContext(ctx);
		ICoReTypeName actual = sut.getClassContext();
		assertEquals(expected, actual);
	}

	@Test
	public void methodContextIsPropagated() {
		ICoReMethodName method = CoReMethodName.get("LClient.doit()V");
		ICoReTypeName intro = CoReTypeName.get("LFramework");
		ICoReMethodName expected = CoReMethodName.get("LFramework.doit()V");

		EnclosingMethodContext ctx = new EnclosingMethodContext();
		ctx.setName(method);
		ctx.setIntroducedBy(intro);
		usage.setContext(ctx);

		ICoReMethodName actual = sut.getMethodContext();
		assertEquals(expected, actual);
	}

	@Test
	public void definitionSite_constant() {
		usage.setDef(com.codetrails.data.DefinitionSites.createDefinitionByConstant());
		DefinitionSite actual = sut.getDefinitionSite();
		DefinitionSite expected = DefinitionSites.createDefinitionByConstant();
		assertEquals(expected, actual);
	}

	@Test
	public void definitionSite_return() {
		usage.setDef(com.codetrails.data.DefinitionSites.createDefinitionByReturn(getMethodName("a")));
		DefinitionSite actual = sut.getDefinitionSite();
		DefinitionSite expected = DefinitionSites.createDefinitionByReturn(getMethodName("a"));
		assertEquals(expected, actual);
	}

	@Test
	public void callsitesArePropagated() {
		builder.newPath().call("a").param("b");
		builder.newPath().param("c").call("d");

		sut = new DecoratedObjectUsage(builder.build());

		assertSites(sut.getAllCallsites(), "a", "b", "c", "d");

	}

	@Test
	public void callsitesAreUniquelyPropagated() {
		builder.newPath().call("a").param("b");
		builder.newPath().param("c").call("a").call("d");

		sut = new DecoratedObjectUsage(builder.build());

		assertSites(sut.getAllCallsites(), "a", "b", "c", "d");
	}

	@Test
	public void receiverCallsitesArePropagated() {
		builder.newPath().call("a").param();
		builder.newPath().param().call("d");

		sut = new DecoratedObjectUsage(builder.build());

		assertSites(sut.getReceiverCallsites(), "a", "d");

	}

	@Test
	public void receiverCallsitesAreUniquelyPropagated() {
		builder.newPath().call("a").param();
		builder.newPath().param().call("a").call("d");

		sut = new DecoratedObjectUsage(builder.build());

		assertSites(sut.getReceiverCallsites(), "a", "d");
	}

	@Test
	public void parameterCallsitesArePropagated() {
		builder.newPath().call("a").param("b");
		builder.newPath().param("c").call("d");

		sut = new DecoratedObjectUsage(builder.build());

		assertSites(sut.getParameterCallsites(), "b", "c");

	}

	@Test
	public void parameterCallsitesAreUniquelyPropagated() {
		builder.newPath().call().param("b");
		builder.newPath().param("b").param("c").call();

		sut = new DecoratedObjectUsage(builder.build());

		assertSites(sut.getParameterCallsites(), "b", "c");
	}

	private static ICoReMethodName getMethodName(String name) {
		return CoReMethodName.get(String.format("LType.method_%s()V", name));
	}

	private static void assertSites(Set<CallSite> sites, String... names) {
		assertEquals(names.length, sites.size());
		for (String suffix : names) {
			ICoReMethodName methodName = getMethodName(suffix);
			assertTrue(containsMethod(sites, methodName));
		}
	}

	private static boolean containsMethod(Set<CallSite> path, ICoReMethodName methodName) {
		for (CallSite site : path) {
			if (site.getMethod().equals(methodName)) {
				return true;
			}
		}
		return false;
	}

	private static class ObjectUsageBuilder {

		private List<PathBuilder> pathBuilders = Lists.newLinkedList();

		public ObjectUsage build() {
			ObjectUsage result = new ObjectUsage();

			com.codetrails.data.DefinitionSite ds = com.codetrails.data.DefinitionSites.createUnknownDefinitionSite();
			ds.setType(CoReTypeName.get("LType"));
			result.setDef(ds);

			EnclosingMethodContext ctx = new EnclosingMethodContext();

			ctx.setSuperclass(CoReTypeName.get("LSuperType"));
			ctx.setName(CoReMethodName.get("LType.method()V"));
			ctx.setIntroducedBy(CoReTypeName.get("LFirstType"));

			Set<List<com.codetrails.data.CallSite>> paths = Sets.newLinkedHashSet();

			for (PathBuilder pathBuilder : pathBuilders) {
				paths.add(pathBuilder.build());
			}

			result.setPaths(paths);
			return result;
		}

		public PathBuilder newPath() {
			PathBuilder pathBuilder = new PathBuilder();
			pathBuilders.add(pathBuilder);
			return pathBuilder;
		}

		private static class PathBuilder {
			private int methodNumber = 0;
			List<com.codetrails.data.CallSite> sites = Lists.newLinkedList();

			public PathBuilder param() {
				return param(getUniqueName());
			}

			public PathBuilder param(String name) {
				com.codetrails.data.CallSite site = com.codetrails.data.CallSites.createParameterCallSite(
						getMethodName(name), 1);
				sites.add(site);
				return this;
			}

			public PathBuilder call() {
				return call(getUniqueName());
			}

			public PathBuilder call(String name) {
				com.codetrails.data.CallSite site = com.codetrails.data.CallSites
						.createReceiverCallSite(getMethodName(name));
				sites.add(site);
				return this;
			}

			public List<com.codetrails.data.CallSite> build() {
				return sites;
			}

			private String getUniqueName() {
				return "uniqueMethod" + methodNumber++;
			}
		}
	}
}