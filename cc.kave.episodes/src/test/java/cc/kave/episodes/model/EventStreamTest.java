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
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.exceptions.AssertionException;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class EventStreamTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Set<Event> expectedMap;
	private List<Event> expMethods;

	private EventStream sut;

	@Before
	public void setup() {
		expectedMap = Sets.newLinkedHashSet();
		expMethods = Lists.newLinkedList();
		sut = new EventStream();
	}
	
	@Test
	public void nullMethodContext1() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Method element is null!");
		sut.addEvent(inv(1));
		sut.addEvent(firstCtx(0));
	}
	
	@Test
	public void nullMethodContext2() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Method element is null!");
		sut.addEvent(inv(1));
		sut.getStreamData();
	}
	
	@Test
	public void nullMethodContext3() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Method element is null!");
		sut.addEvent(inv(1));
		sut.getStreamText();
	}
	
	@Test
	public void defaultValues() {
		expectedMap.add(dummy());

		assertEquals(expectedMap, sut.getMapping());

		assertTrue(sut.getStreamText().equals(""));
		assertTrue(sut.getStreamData().isEmpty());
	}

	@Test
	public void addUnknownEvent() {
		sut.addEvent(firstCtx(0));
		sut.addEvent(enclCtx(1));

		expectedMap.add(Events.newDummyEvent());

		expMethods.add(enclCtx(1));

		assertEquals(expectedMap, sut.getMapping());
		assertEquals(Lists.newLinkedList(), sut.getStreamData());

		assertTrue(sut.getStreamText().equals(""));
	}

	@Test
	public void addContext() {
		sut.addEvent(firstCtx(0));
		sut.addEvent(enclCtx(0));
		sut.addEvent(firstCtx(1));
		sut.addEvent(enclCtx(1));

		expectedMap.add(Events.newDummyEvent());
		expectedMap.add(firstCtx(1));
		
		String expectedStream = "1,0.500\n";
		String actualStream = sut.getStreamText();
		
		List<Tuple<Event, String>> expStreamData = Lists.newLinkedList();
		expStreamData.add(Tuple.newTuple(enclCtx(1), expectedStream));

		assertEquals(expectedMap, sut.getMapping());
		assertEquals(expectedStream, actualStream);
		assertEquals(expStreamData, sut.getStreamData());
	}

	@Test
	public void addInvocation() {
		sut.addEvent(firstCtx(0));
		sut.addEvent(enclCtx(1));
		sut.addEvent(inv(1));

		expectedMap.add(Events.newDummyEvent());
		expectedMap.add(inv(1));

		String streamText = "1,0.000\n";
		String actualStreamText = sut.getStreamText();

		List<Tuple<Event, String>> expStream = Lists.newLinkedList();
		expStream.add(Tuple.newTuple(enclCtx(1), streamText));

		assertEquals(expectedMap, sut.getMapping());
		assertEquals(streamText, actualStreamText);
		assertEquals(expStream, sut.getStreamData());
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

		Set<Event> expectedMap = Sets.newLinkedHashSet();
		expectedMap.add(Events.newDummyEvent());
		expectedMap.add(firstCtx(1));
		expectedMap.add(superCtx(2));
		expectedMap.add(inv(2));
		expectedMap.add(inv(3));

		StringBuilder expSb = new StringBuilder();
		expSb.append("1,0.000\n");
		expSb.append("2,0.001\n");
		expSb.append("3,0.002\n");
		expSb.append("4,0.003\n");
		expSb.append("3,0.504\n");

		List<Tuple<Event, String>> expStream = Lists.newLinkedList();
		expStream.add(Tuple.newTuple(enclCtx(3),
				"1,0.000\n2,0.001\n3,0.002\n4,0.003\n"));
		expStream.add(Tuple.newTuple(enclCtx(1), "3,0.504\n"));

		assertEquals(expectedMap, sut.getMapping());
		assertEquals(expStream, sut.getStreamData());
		assertEquals(expSb.toString(), sut.getStreamText());
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
		a.addEvent(enclCtx(1));
		a.addEvent(inv(2));

		EventStream b = new EventStream();
		b.addEvent(firstCtx(1));
		b.addEvent(enclCtx(1));
		b.addEvent(inv(2));

		assertEquals(a.getMapping(), b.getMapping());
		assertEquals(a.getStreamData(), b.getStreamData());
		assertEquals(a.getStreamText(), b.getStreamText());

		assertTrue(a.getStreamData().size() == b.getStreamData().size());
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
		assertEquals(a.getStreamData(), b.getStreamData());
		assertEquals(a.getStreamText(), b.getStreamText());

		assertTrue(a.getStreamData().size() == b.getStreamData().size());

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
		assertNotEquals(a.getStreamText(), b.getStreamText());
		assertNotEquals(a.getStreamData(), b.getStreamData());

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
		assertNotEquals(a.getStreamText(), b.getStreamText());
		assertNotEquals(a.getStreamData(), b.getStreamData());

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
		b.addEvent(enclCtx(0));
		b.addEvent(inv(2));

		assertEquals(a.getMapping(), b.getMapping());
		assertNotEquals(a.getStreamText(), b.getStreamText());
		assertNotEquals(a.getStreamData(), b.getStreamData());

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
		assertEquals(a.getStreamText(), b.getStreamText());
		assertNotEquals(a.getStreamData(), b.getStreamData());

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

		assertTrue(sut.getMapping().isEmpty());
		assertTrue(sut.getStreamText().isEmpty());
		assertTrue(sut.getStreamData().isEmpty());
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