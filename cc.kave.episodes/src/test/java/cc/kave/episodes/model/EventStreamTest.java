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
	
	Map<Event, Integer> expectedMap;

	private EventStream sut;
	
	@Before
	public void setup() {
		expectedMap =  Maps.newLinkedHashMap();
		sut = new EventStream();
	}
	
	@Test
	public void defaultValues() {
		expectedMap.put(Events.newDummyEvent(), 0);
		
		assertEquals(expectedMap, sut.getMapping());
		assertEquals(1, sut.getNumberEvents());
		assertEquals(0, sut.getStreamLength());
		
		assertTrue(sut.getStream().equals(""));
	}
	
	@Test
	public void addUnknownEvent() {
		sut.addEvent(Events.newFirstContext(MethodName.UNKNOWN_NAME));
		
		expectedMap = Maps.newLinkedHashMap();
		expectedMap.put(Events.newDummyEvent(), 0);
		
		assertEquals(expectedMap, sut.getMapping());
		assertEquals(1, sut.getNumberEvents());
		assertEquals(0, sut.getStreamLength());
		
		assertTrue(sut.getStream().equals(""));
	}
	
	@Test
	public void addContext() {
		sut.addEvent(firstCtx(1));
		
		expectedMap = Maps.newLinkedHashMap();
		expectedMap.put(Events.newDummyEvent(), 0);
		expectedMap.put(firstCtx(1), 1);
		
		String expectedStream = "1,0.000\n";
		String actualStream = sut.getStream();
		
		assertEquals(expectedMap, sut.getMapping());
		assertEquals(2, sut.getNumberEvents());
		assertEquals(1, sut.getStreamLength());
		assertEquals(expectedStream, actualStream);
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
		assertEquals(2, sut.getNumberEvents());
		assertEquals(1, sut.getStreamLength());
		assertEquals(expectedStream, actualStream);
	}
	
	@Test
	public void addMultipleEvents() {
		sut.addEvent(firstCtx(1));		//1
		sut.addEvent(superCtx(2));		//2
		sut.addEvent(enclosingCtx(31));	//3
		sut.addEvent(inv(2));			//4
		sut.addEvent(inv(3));			//5
		sut.addEvent(firstCtx(0));
		sut.addEvent(enclosingCtx(32));	//6
		sut.addEvent(inv(2));			//4
		
		Map<Event, Integer> expectedMap = Maps.newLinkedHashMap();
		expectedMap.put(Events.newDummyEvent(), 0);
		expectedMap.put(firstCtx(1), 1);		
		expectedMap.put(superCtx(2), 2);		
		expectedMap.put(enclosingCtx(31), 3);	
		expectedMap.put(inv(2), 4);				
		expectedMap.put(inv(3), 5);				
		expectedMap.put(enclosingCtx(32), 6);
		
		StringBuilder expectedSb = new StringBuilder();
		expectedSb.append("1,0.000\n");
		expectedSb.append("2,0.001\n");
		expectedSb.append("3,0.002\n");
		expectedSb.append("4,0.003\n");
		expectedSb.append("5,0.004\n");
		expectedSb.append("6,0.505\n");
		expectedSb.append("4,0.506\n");
		
		assertEquals(expectedMap, sut.getMapping());
		assertEquals(7, sut.getNumberEvents());
		assertEquals(7, sut.getStreamLength());
		assertEquals(expectedSb.toString(), sut.getStream());
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
		assertEquals(a.getNumberEvents(), b.getNumberEvents());
		assertEquals(a.getStreamLength(), b.getStreamLength());
		assertEquals(a.getStream(), b.getStream());		
		assertTrue(a.equals(b));
	}
	
	@Test
	public void notEqual1() {
		EventStream a = new EventStream();
		a.addEvent(firstCtx(1));
		a.addEvent(inv(2));
		
		EventStream b = new EventStream();
		b.addEvent(firstCtx(1));
		b.addEvent(inv(3));
		
		assertNotEquals(a.getMapping(), b.getMapping());
		assertEquals(a.getNumberEvents(), b.getNumberEvents());
		assertEquals(a.getStreamLength(), b.getStreamLength());
		assertEquals(a.getStream(), b.getStream());
		assertFalse(a.equals(b));
	}
	
	@Test
	public void notEqual2() {
		EventStream a = new EventStream();
		a.addEvent(firstCtx(1));
		a.addEvent(inv(2));
		
		EventStream b = new EventStream();
		b.addEvent(firstCtx(1));
		b.addEvent(inv(2));
		b.addEvent(inv(3));
		
		assertNotEquals(a.getMapping(), b.getMapping());
		assertNotEquals(a.getNumberEvents(), b.getNumberEvents());
		assertNotEquals(a.getStreamLength(), b.getStreamLength());
		assertNotEquals(a.getStream(), b.getStream());
		assertFalse(a.equals(b));
	}
	
	@Test
	public void notEqualStream() {
		EventStream a = new EventStream();
		a.addEvent(firstCtx(1));
		a.addEvent(inv(2));
		a.addEvent(inv(2));
		
		EventStream b = new EventStream();
		b.addEvent(firstCtx(1));
		b.addEvent(inv(2));
		
		assertEquals(a.getMapping(), b.getMapping());
		assertEquals(a.getNumberEvents(), b.getNumberEvents());
		assertNotEquals(a.getStreamLength(), b.getStreamLength());
		assertNotEquals(a.getStream(), b.getStream());
		assertFalse(a.equals(b));
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
	
	private static Event enclosingCtx(int i) {
		return Events.newContext(m(i));
	}
	
	private static IMethodName m(int i) {
		if (i == 0) {
		return MethodName.UNKNOWN_NAME;
		} else {
			return MethodName.newMethodName("[T,P] [T,P].m" + i + "()");
		}
	}
}
