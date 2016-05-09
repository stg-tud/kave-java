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

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Events;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;

public class EventStreamTest {

	private EventStream sut;
	
	@Before
	public void setup() {
		sut = new EventStream();
	}
	
	@Test
	public void defaultValues() {
		assertEquals(Maps.newLinkedHashMap(), sut.getMapping());
		assertEquals(0, sut.getEventNumber());
		assertEquals(0, sut.getStreamLength());
		
		assertTrue(sut.getStream().equals(""));
	}
	
	@Test
	public void valuesCanBeSet() {
		sut.addEvent(ctx(1));
		
		Map<Event, Integer> expMap = Maps.newLinkedHashMap();
		expMap.put(ctx(1), 0);
		
		assertEquals(expMap, sut.getMapping());
		
		assertEquals(1, sut.getStreamLength());
		assertEquals(1, sut.getEventNumber());
	}
	
	@Test
	public void addMultipleEvents() {
		sut.addEvent(ctx(0));
		sut.addEvent(ctx(1));
		sut.addEvent(inv(2));
		sut.addEvent(inv(3));
		sut.addEvent(hld());
		sut.addEvent(inv(2));
		
		Map<Event, Integer> expectedMap = Maps.newLinkedHashMap();
		expectedMap.put(ctx(0), 0);
		expectedMap.put(ctx(1), 1);
		expectedMap.put(inv(2), 2);
		expectedMap.put(inv(3), 3);
		
		StringBuilder expSb = new StringBuilder();
		expSb.append("1,0.500\n");
		expSb.append("2,0.501\n");
		expSb.append("3,0.502\n");
		expSb.append("2,1.003\n");
		
		assertEquals(expectedMap, sut.getMapping());
		assertEquals(sut.getStream(), expSb.toString());
		
		assertEquals(6, sut.getStreamLength());
		assertEquals(4, sut.getEventNumber());
	}
	
	@Test
	public void addHolderAsStartEvent() {
		sut.addEvent(ctx(0));
		sut.addEvent(hld());
		sut.addEvent(inv(1));
		sut.addEvent(inv(2));
		sut.addEvent(hld());
		sut.addEvent(inv(1));
		
		Map<Event, Integer> expectedMap = Maps.newLinkedHashMap();
		expectedMap.put(ctx(0), 0);
		expectedMap.put(inv(1), 1);
		expectedMap.put(inv(2), 2);
		
		StringBuilder expSb = new StringBuilder();
		expSb.append("1,0.500\n");
		expSb.append("2,0.501\n");
		expSb.append("1,1.002\n");
		
		assertEquals(expectedMap, sut.getMapping());
		assertTrue(sut.getStream().equals(expSb.toString()));
		
		assertEquals(6, sut.getStreamLength());
		assertEquals(3, sut.getEventNumber());
	}
	
	@Test
	public void twoHolders() {
		sut.addEvent(ctx(0));
		sut.addEvent(hld());
		sut.addEvent(hld());
		sut.addEvent(inv(1));
		sut.addEvent(inv(2));
		sut.addEvent(ctx(1));
		sut.addEvent(inv(1));
		
		Map<Event, Integer> expectedMap = Maps.newLinkedHashMap();
		expectedMap.put(ctx(0), 0);
		expectedMap.put(inv(1), 1);
		expectedMap.put(inv(2), 2);
		expectedMap.put(ctx(1), 3);
		
		StringBuilder expSb = new StringBuilder();
		expSb.append("1,1.000\n");
		expSb.append("2,1.001\n");
		expSb.append("3,1.502\n");
		expSb.append("1,1.503\n");
		
		assertEquals(expectedMap, sut.getMapping());
		assertTrue(sut.getStream().equals(expSb.toString()));
		
		assertEquals(7, sut.getStreamLength());
		assertEquals(4, sut.getEventNumber());
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
		a.addEvent(ctx(1));
		a.addEvent(inv(2));
		
		EventStream b = new EventStream();
		b.addEvent(ctx(1));
		b.addEvent(inv(2));
		
		assertTrue(a.equals(b));
		assertTrue(a.getStream().equalsIgnoreCase(b.getStream()));
		assertEquals(a.getStream(), b.getStream());
		assertEquals(a.getMapping(), b.getMapping());
		assertEquals(a.getStreamLength(), b.getStreamLength());
		assertEquals(a.getEventNumber(), b.getEventNumber());
	}
	
	@Test
	public void notEqual1() {
		EventStream a = new EventStream();
		a.addEvent(ctx(1));
		a.addEvent(inv(2));
		
		EventStream b = new EventStream();
		b.addEvent(ctx(1));
		b.addEvent(inv(3));
		
		assertFalse(a.equals(b));
		assertNotEquals(a.getMapping(), b.getMapping());
		assertEquals(a.getStream(), b.getStream());
		assertTrue(a.getStreamLength() == b.getStreamLength());
		assertEquals(a.getEventNumber(), b.getEventNumber());
	}
	
	@Test
	public void notEqual2() {
		EventStream a = new EventStream();
		a.addEvent(ctx(1));
		a.addEvent(inv(2));
		
		EventStream b = new EventStream();
		b.addEvent(ctx(1));
		b.addEvent(inv(2));
		b.addEvent(inv(3));
		
		assertFalse(a.equals(b));
		assertNotEquals(a.getMapping(), b.getMapping());
		assertNotEquals(a.getStream(), b.getStream());
		assertTrue(a.getStreamLength() != b.getStreamLength());
		assertNotEquals(a.getEventNumber(), b.getEventNumber());
	}
	
	@Test
	public void notEqualStream() {
		EventStream a = new EventStream();
		a.addEvent(ctx(1));
		a.addEvent(inv(2));
		a.addEvent(inv(2));
		
		EventStream b = new EventStream();
		b.addEvent(ctx(1));
		b.addEvent(inv(2));
		
		assertFalse(a.equals(b));
		assertEquals(a.getMapping(), b.getMapping());
		assertNotEquals(a.getStream(), b.getStream());
		assertTrue(a.getStreamLength() != b.getStreamLength());
		assertEquals(a.getEventNumber(), b.getEventNumber());
	}
	
	private static Event inv(int i) {
		return Events.newInvocation(m(i));
	}

	private static Event ctx(int i) {
		return Events.newContext(m(i));
	}
	
	private static Event hld() {
		return Events.newHolder();
	}
	
	private static IMethodName m(int i) {
		return MethodName.newMethodName("[T,P] [T,P].m" + i + "()");
	}
}
