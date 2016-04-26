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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Events;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;

public class StreamMappingTest {

	private StreamMapping sut;
	
	@Before
	public void setup() {
		sut = new StreamMapping();
	}
	
	@Test
	public void defaultValues() {
		assertEquals(Lists.newLinkedList(), sut.getStreamData());
		assertEquals(Lists.newLinkedList(), sut.getMappingData());
	}
	
	@Test
	public void valuesCanBeSet() {
		sut.addEvent(ctx(1));
		
		assertEquals(Lists.newArrayList(ctx(1)), sut.getStreamData());
		assertEquals(Lists.newArrayList(ctx(1)), sut.getMappingData());
		
		assertEquals(1, sut.getStreamLength());
		assertEquals(1, sut.getNumberEvents());
	}
	
	@Test
	public void addMultipleEvents() {
		sut.addEvent(ctx(1));
		sut.addEvent(inv(2));
		sut.addEvent(inv(3));
		sut.addEvent(hld());
		sut.addEvent(inv(2));
		
		assertEquals(Lists.newArrayList(ctx(1), inv(2), inv(3), hld(), inv(2)), sut.getStreamData());
		assertEquals(Lists.newArrayList(ctx(1), inv(2), inv(3)), sut.getMappingData());
		
		assertEquals(5, sut.getStreamLength());
		assertEquals(3, sut.getNumberEvents());
	}
	
	@Test
	public void equality_default() {
		StreamMapping a = new StreamMapping();
		StreamMapping b = new StreamMapping();
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertTrue(a.equals(b));
	}
	
	@Test
	public void equlityReallySame() {
		StreamMapping a = new StreamMapping();
		a.addEvent(ctx(1));
		a.addEvent(inv(2));
		
		StreamMapping b = new StreamMapping();
		b.addEvent(ctx(1));
		b.addEvent(inv(2));
		
		assertEquals(a, b);
		assertEquals(a.getStreamLength(), b.getStreamLength());
		assertEquals(a.getNumberEvents(), b.getNumberEvents());
	}
	
	@Test
	public void notEqual1() {
		StreamMapping a = new StreamMapping();
		a.addEvent(ctx(1));
		a.addEvent(inv(2));
		
		StreamMapping b = new StreamMapping();
		b.addEvent(ctx(1));
		b.addEvent(inv(3));
		
		assertNotEquals(a, b);
		assertEquals(a.getStreamLength(), b.getStreamLength());
		assertEquals(a.getNumberEvents(), b.getNumberEvents());
	}
	
	@Test
	public void notEqual2() {
		StreamMapping a = new StreamMapping();
		a.addEvent(ctx(1));
		a.addEvent(inv(2));
		
		StreamMapping b = new StreamMapping();
		b.addEvent(ctx(1));
		b.addEvent(inv(2));
		b.addEvent(inv(3));
		
		assertNotEquals(a, b);
		assertNotEquals(a.getStreamLength(), b.getStreamLength());
		assertNotEquals(a.getNumberEvents(), b.getNumberEvents());
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
