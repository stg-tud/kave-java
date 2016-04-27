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
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Events;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.episodes.model.StreamData;

public class EventsFilterTest {

	private static final String DUMMY_METHOD_NAME = "[You, Can] [Safely, Ignore].ThisDummyValue()";
	private static final IMethodName DUMMY_METHOD = MethodName.newMethodName(DUMMY_METHOD_NAME);
	public static final Event DUMMY_EVENT = Events.newContext(DUMMY_METHOD);
	
	private List<Event> events;
	
	@Before
	public void setup() {
		events = Lists.newArrayList(ctx(1), inv(2), inv(3), ctx(4), inv(5), inv(2), ctx(1), inv(3));
	}
	
	@Test
	public void filterTest() {
		StreamData expected = new StreamData();
		expected.addEvent(DUMMY_EVENT);
		expected.addEvent(ctx(1));
		expected.addEvent(inv(2));
		expected.addEvent(inv(3));
		expected.addEvent(hld());
		expected.addEvent(inv(2));
		expected.addEvent(ctx(1));
		expected.addEvent(inv(3));
		
		StreamData actuals = EventsFilter.filter(events);
		
		assertTrue(expected.equals(actuals));
		assertEquals(expected.getMapping(), actuals.getMapping());
		assertEquals(expected.getStreamLength(), actuals.getStreamLength());
		assertEquals(expected.getEventNumber(), actuals.getEventNumber());
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
