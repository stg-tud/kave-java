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
package cc.kave.episodes.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.recommenders.exceptions.AssertionException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class EventStreamTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Map<Event, Integer> expectedMap;
	private List<Event> expMethods;

	private EventStream sut;

	@Before
	public void setup() {
		expectedMap = Maps.newLinkedHashMap();
		expMethods = Lists.newLinkedList();
		sut = new EventStream();
	}

	@Test
	public void missmatchMethods() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Mismatch between number of methods and enclosing methods!");
		sut.addEvent(enclCtx(1));
	}

	@Test
	public void defaultValues() {
		expectedMap.put(dummy(), 0);

		assertEquals(expectedMap, sut.getMapping());

		assertTrue(sut.getStream().equals(""));
		assertTrue(sut.getNumMethods() == 0);
		assertTrue(sut.getMethodCtxs().isEmpty());
	}

	@Test
	public void addUnknownEvent() {
		sut.addEvent(firstCtx(0));
		sut.addEvent(enclCtx(1));

		expectedMap = Maps.newLinkedHashMap();
		expectedMap.put(Events.newDummyEvent(), 0);

		expMethods.add(enclCtx(1));

		assertEquals(expectedMap, sut.getMapping());
		assertEquals(expMethods, sut.getMethodCtxs());

		assertTrue(sut.getStream().equals(""));
		assertTrue(sut.getNumMethods() == 1);
		assertTrue(sut.getMethodCtxs().size() == 1);
	}

	@Test
	public void addContext() {
		sut.addEvent(firstCtx(1));
		sut.addEvent(enclCtx(0));

		expectedMap = Maps.newLinkedHashMap();
		expectedMap.put(Events.newDummyEvent(), 0);
		expectedMap.put(firstCtx(1), 1);

		expMethods.add(enclCtx(0));

		String expectedStream = "1,0.000\n";
		String actualStream = sut.getStream();

		assertEquals(expectedMap, sut.getMapping());
		assertEquals(expectedStream, actualStream);
		assertEquals(expMethods, sut.getMethodCtxs());

		assertTrue(sut.getNumMethods() == 1);
		assertTrue(sut.getMethodCtxs().size() == 1);
	}

	@Test
	public void addInvocation() {
		sut.addEvent(inv(1));

		expectedMap = Maps.newLinkedHashMap();
		expectedMap.put(Events.newDummyEvent(), 0);
		expectedMap.put(inv(1), 1);

		String expectedStream = "1,0.000\n";
		String actualStream = sut.getStream();

		assertEquals(expectedMap, sut.getMapping());
		assertEquals(expectedStream, actualStream);

		assertTrue(sut.getNumMethods() == 0);
		assertTrue(sut.getMethodCtxs().isEmpty());
	}

	@Test
	public void addMultipleEvents() {
		sut.addEvent(firstCtx(1)); // 1
		sut.addEvent(superCtx(2)); // 2
		sut.addEvent(enclCtx(3));
		sut.addEvent(inv(2)); // 3
		sut.addEvent(inv(3)); // 4
		sut.addEvent(firstCtx(0));
		sut.addEvent(enclCtx(1));
		sut.addEvent(inv(2)); // 3

		Map<Event, Integer> expectedMap = Maps.newLinkedHashMap();
		expectedMap.put(Events.newDummyEvent(), 0);
		expectedMap.put(firstCtx(1), 1);
		expectedMap.put(superCtx(2), 2);
		expectedMap.put(inv(2), 3);
		expectedMap.put(inv(3), 4);

		expMethods.add(enclCtx(3));
		expMethods.add(enclCtx(1));

		StringBuilder expectedSb = new StringBuilder();
		expectedSb.append("1,0.000\n");
		expectedSb.append("2,0.001\n");
		expectedSb.append("3,0.002\n");
		expectedSb.append("4,0.003\n");
		expectedSb.append("3,0.504\n");

		assertEquals(expectedMap, sut.getMapping());
		assertEquals(expectedSb.toString(), sut.getStream());
		assertEquals(expMethods, sut.getMethodCtxs());

		assertTrue(sut.getNumMethods() == 2);
		assertTrue(sut.getMethodCtxs().size() == 2);
	}

	@Test
	public void equality_default() {
		EventStream a = new EventStream();
		EventStream b = new EventStream();

		assertTrue(a.equals(b));
	}

	@Test
	public void equlityReallySame() {
		EventStream a = new EventStream();
		a.addEvent(firstCtx(1));
		a.addEvent(inv(2));

		EventStream b = new EventStream();
		b.addEvent(firstCtx(1));
		b.addEvent(inv(2));

		assertEquals(a.getMapping(), b.getMapping());
		assertEquals(a.getStream(), b.getStream());
		assertEquals(a.getMethodCtxs(), b.getMethodCtxs());

		assertTrue(a.getNumMethods() == b.getNumMethods());
		assertTrue(a.getMethodCtxs().size() == b.getMethodCtxs().size());
		assertTrue(a.equals(b));
	}

	@Test
	public void notEqual1() {
		EventStream a = new EventStream();
		a.addEvent(firstCtx(1));
		a.addEvent(enclCtx(1));
		a.addEvent(inv(2));

		EventStream b = new EventStream();
		b.addEvent(firstCtx(1));
		b.addEvent(enclCtx(1));
		b.addEvent(inv(3));

		assertNotEquals(a.getMapping(), b.getMapping());
		assertEquals(a.getStream(), b.getStream());
		assertEquals(a.getMethodCtxs(), b.getMethodCtxs());

		assertTrue(a.getNumMethods() == b.getNumMethods());
		assertTrue(a.getMethodCtxs().size() == b.getMethodCtxs().size());

		assertFalse(a.equals(b));
	}

	@Test
	public void notEqual2() {
		EventStream a = new EventStream();
		a.addEvent(firstCtx(1));
		a.addEvent(enclCtx(1));
		a.addEvent(inv(2));

		EventStream b = new EventStream();
		b.addEvent(firstCtx(1));
		b.addEvent(enclCtx(1));
		b.addEvent(inv(2));
		b.addEvent(inv(3));

		assertNotEquals(a.getMapping(), b.getMapping());
		assertNotEquals(a.getStream(), b.getStream());
		assertEquals(a.getMethodCtxs(), b.getMethodCtxs());

		assertTrue(a.getNumMethods() == b.getNumMethods());

		assertFalse(a.equals(b));
	}

	@Test
	public void notEqualStream() {
		EventStream a = new EventStream();
		a.addEvent(firstCtx(1));
		a.addEvent(enclCtx(1));
		a.addEvent(inv(2));
		a.addEvent(inv(2));

		EventStream b = new EventStream();
		b.addEvent(firstCtx(1));
		b.addEvent(enclCtx(1));
		b.addEvent(inv(2));

		assertEquals(a.getMapping(), b.getMapping());
		assertNotEquals(a.getStream(), b.getStream());
		assertEquals(a.getMethodCtxs(), b.getMethodCtxs());

		assertTrue(a.getNumMethods() == b.getNumMethods());

		assertFalse(a.equals(b));
	}

	@Test
	public void notEqualNumMethods() {
		EventStream a = new EventStream();
		a.addEvent(firstCtx(1));
		a.addEvent(enclCtx(1));
		a.addEvent(inv(2));
		a.addEvent(inv(2));

		EventStream b = new EventStream();
		b.addEvent(firstCtx(1));
		b.addEvent(enclCtx(1));
		b.addEvent(inv(2));
		b.addEvent(firstCtx(0));
		b.addEvent(inv(2));

		assertEquals(a.getMapping(), b.getMapping());
		assertNotEquals(a.getStream(), b.getStream());
		assertEquals(a.getMethodCtxs(), b.getMethodCtxs());

		assertTrue(a.getNumMethods() != b.getNumMethods());

		assertFalse(a.equals(b));
	}
	
	@Test
	public void notEqualEnclMethods() {
		EventStream a = new EventStream();
		a.addEvent(firstCtx(1));
		a.addEvent(enclCtx(1));
		a.addEvent(inv(2));
		a.addEvent(inv(2));

		EventStream b = new EventStream();
		b.addEvent(firstCtx(1));
		b.addEvent(enclCtx(2));
		b.addEvent(inv(2));
		b.addEvent(inv(2));

		assertEquals(a.getMapping(), b.getMapping());
		assertEquals(a.getStream(), b.getStream());
		assertNotEquals(a.getMethodCtxs(), b.getMethodCtxs());

		assertTrue(a.getNumMethods() == b.getNumMethods());

		assertFalse(a.equals(b));
	}
	
	@Test
	public void delete() {
		EventStream emptyStream = new EventStream();
		
		assertTrue(sut.equals(emptyStream));
		
		sut.addEvent(firstCtx(1));
		sut.addEvent(superCtx(2));
		sut.addEvent(enclCtx(3));
		sut.addEvent(inv(4));
		
		assertFalse(sut.equals(emptyStream));
		
		sut.delete();

		assertTrue(sut.getMethodCtxs().isEmpty());
		assertTrue(sut.getMapping().isEmpty());
		assertTrue(sut.getStream().isEmpty());
	}

	private static Event inv(int i) {
		return Events.newInvocation(m(i));
	}

	private static Event firstCtx(int i) {
		return Events.newFirstContext(m(i));
	}

	private static Event superCtx(int i) {
		return Events.newSuperContext(m(i));
	}

	private static Event enclCtx(int i) {
		return Events.newContext(m(i));
	}

	private static Event dummy() {
		return Events.newDummyEvent();
	}

	private static IMethodName m(int i) {
		if (i == 0) {
			return Names.getUnknownMethod();
		} else {
			return Names.newMethod("[T,P] [T,P].m" + i + "()");
		}
	}
}