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

import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;

import com.google.common.collect.Sets;

public class EventStreamTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Set<Event> expectedMap;

	private EventStream sut;

	@Before
	public void setup() {
		expectedMap = Sets.newLinkedHashSet();
		sut = new EventStream();
	}

	@Test
	public void defaultValues() {
		expectedMap.add(dummy());

		assertEquals(expectedMap, sut.getMapping());

		assertTrue(sut.getStreamText().equals(""));
	}

	@Test
	public void addContext() {
		sut.addEvent(ctxFirst(2)); // 1
		sut.addEvent(inv(4));

		expectedMap.add(Events.newDummyEvent());
		expectedMap.add(ctxFirst(2));
		expectedMap.add(inv(4));

		String expectedStream = "1,0.000\n2,0.001\n";

		String actualStream = sut.getStreamText();

		assertEquals(expectedMap, sut.getMapping());
		assertEquals(expectedStream, actualStream);
	}

	@Test
	public void addInvocation() {
		sut.addEvent(ctxFirst(0));
		sut.addEvent(inv(1));

		expectedMap.add(Events.newDummyEvent());
		expectedMap.add(ctxFirst(0));
		expectedMap.add(inv(1));

		String streamText = "1,0.000\n2,0.001\n";
		String actualStreamText = sut.getStreamText();

		assertEquals(expectedMap, sut.getMapping());
		assertEquals(streamText, actualStreamText);
	}

	@Test
	public void addMultipleEvents() {
		sut.addEvent(ctxFirst(1)); // 1
		sut.addEvent(ctxSuper(2)); // 2
		sut.addEvent(inv(2)); // 3
		sut.addEvent(inv(3)); // 4
		sut.increaseTimeout();
		sut.addEvent(ctxFirst(0)); // 5
		sut.addEvent(inv(2)); // 3

		Set<Event> expectedMap = Sets.newLinkedHashSet();
		expectedMap.add(Events.newDummyEvent());
		expectedMap.add(ctxFirst(1));
		expectedMap.add(ctxSuper(2));
		expectedMap.add(inv(2));
		expectedMap.add(inv(3));
		expectedMap.add(ctxFirst(0));

		StringBuilder expSb = new StringBuilder();
		expSb.append("1,0.000\n");
		expSb.append("2,0.001\n");
		expSb.append("3,0.002\n");
		expSb.append("4,0.003\n");
		expSb.append("5,5.004\n");
		expSb.append("3,5.005\n");

		assertEquals(expectedMap, sut.getMapping());
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
		a.addEvent(ctxFirst(1));
		a.addEvent(ctxElem(1));
		a.addEvent(inv(2));

		EventStream b = new EventStream();
		b.addEvent(ctxFirst(1));
		b.addEvent(ctxElem(1));
		b.addEvent(inv(2));

		assertEquals(a.getMapping(), b.getMapping());
		assertEquals(a.getStreamText(), b.getStreamText());

		assertTrue(a.equals(b));
	}

	@Test
	public void notEqual1() {
		EventStream a = new EventStream();
		a.addEvent(ctxFirst(1));
		a.addEvent(ctxElem(1));
		a.addEvent(inv(2));

		EventStream b = new EventStream();
		b.addEvent(ctxFirst(1));
		b.addEvent(ctxElem(1));
		b.addEvent(inv(3));

		assertNotEquals(a.getMapping(), b.getMapping());
		assertEquals(a.getStreamText(), b.getStreamText());

		assertFalse(a.equals(b));
	}

	@Test
	public void notEqual2() {
		EventStream a = new EventStream();
		a.addEvent(ctxFirst(1));
		a.addEvent(ctxElem(1));
		a.addEvent(inv(2));

		EventStream b = new EventStream();
		b.addEvent(ctxFirst(1));
		b.addEvent(ctxElem(1));
		b.addEvent(inv(2));
		b.addEvent(inv(3));

		assertNotEquals(a.getMapping(), b.getMapping());
		assertNotEquals(a.getStreamText(), b.getStreamText());

		assertFalse(a.equals(b));
	}

	@Test
	public void notEqualStream() {
		EventStream a = new EventStream();
		a.addEvent(ctxFirst(1));
		a.addEvent(ctxElem(1));
		a.addEvent(inv(2));
		a.addEvent(inv(2));

		EventStream b = new EventStream();
		b.addEvent(ctxFirst(1));
		b.addEvent(ctxElem(1));
		b.addEvent(inv(2));

		assertEquals(a.getMapping(), b.getMapping());
		assertNotEquals(a.getStreamText(), b.getStreamText());

		assertFalse(a.equals(b));
	}

	@Test
	public void notEqualNumMethods() {
		EventStream a = new EventStream();
		a.addEvent(ctxFirst(1));
		a.addEvent(inv(2));
		a.addEvent(inv(2));

		EventStream b = new EventStream();
		b.addEvent(ctxFirst(1));
		b.addEvent(inv(2));
		b.increaseTimeout();
		b.addEvent(ctxFirst(1));
		b.addEvent(inv(2));

		assertEquals(a.getMapping(), b.getMapping());
		assertNotEquals(a.getStreamText(), b.getStreamText());

		assertFalse(a.equals(b));
	}

	@Test
	public void delete() {
		EventStream emptyStream = new EventStream();

		assertTrue(sut.equals(emptyStream));

		sut.addEvent(ctxFirst(1));
		sut.addEvent(ctxSuper(2));
		sut.addEvent(ctxElem(3));
		sut.addEvent(inv(4));

		assertFalse(sut.equals(emptyStream));

		sut.delete();

		assertTrue(sut.getMapping().isEmpty());
		assertTrue(sut.getStreamText().isEmpty());
	}

	private static Event inv(int i) {
		return Events.newInvocation(m(i));
	}

	private static Event ctxFirst(int i) {
		return Events.newFirstContext(m(i));
	}

	private static Event ctxSuper(int i) {
		return Events.newSuperContext(m(i));
	}

	private static Event ctxElem(int i) {
		return Events.newContext(m(i));
	}

	private static Event dummy() {
		return Events.newDummyEvent();
	}

	private static IMethodName m(int i) {
		return Names.newMethod("[T,P, 1.2.3.4] [T,P, 1.2.3.4].m" + i + "()");
	}
}