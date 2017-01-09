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
package cc.kave.episodes.eventstream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;

import com.google.common.collect.Lists;

public class EventsFilterTest {

	private static final int REMFREQS = 2;

	private List<Event> events;

	private EventStream expectedStream;

	@Before
	public void setup() {
		events = Lists.newArrayList(firstCtx(1), enclCtx(0), inv(2), inv(3), 
									firstCtx(0), superCtx(2), enclCtx(7), inv(5), inv(0), inv(2), 
									firstCtx(1), enclCtx(6), inv(2), inv(3), 
									firstCtx(1), enclCtx(0), inv(2), inv(3),
									firstCtx(0), enclCtx(8), inv(2),
									firstCtx(1), enclCtx(6), inv(2), inv(3), 
									firstCtx(3), superCtx(4), enclCtx(0), inv(3));

		expectedStream = new EventStream();
		expectedStream.addEvent(firstCtx(1));	// 1
		expectedStream.addEvent(enclCtx(0));  	
		expectedStream.addEvent(inv(2)); 		// 2
		expectedStream.addEvent(inv(3)); 		// 3
		expectedStream.addEvent(firstCtx(0));
		expectedStream.addEvent(enclCtx(7)); 	
		expectedStream.addEvent(inv(2)); 		// 2
		expectedStream.addEvent(firstCtx(1));	// 1
		expectedStream.addEvent(enclCtx(6));
		expectedStream.addEvent(inv(2));		// 2
		expectedStream.addEvent(inv(3));		// 3
		expectedStream.addEvent(firstCtx(1));	// 1	
		expectedStream.addEvent(enclCtx(0));
		expectedStream.addEvent(inv(2));		// 2
		expectedStream.addEvent(inv(3));		// 3
		expectedStream.addEvent(firstCtx(0));
		expectedStream.addEvent(enclCtx(8));
		expectedStream.addEvent(inv(2));		// 2
		expectedStream.addEvent(firstCtx(3)); 	// 4
		expectedStream.addEvent(enclCtx(0)); 
		expectedStream.addEvent(inv(3)); 		// 3
	}
	
	@Test
	public void emptyStream() {
		List<Event> events = Lists.newLinkedList();
		
		EventStream expected = new EventStream();
		
		EventStream actuals = EventsFilter.filterStream(events, REMFREQS);
		
		assertTrue(expected.equals(actuals));
	}

	@Test
	public void filterStream() {
		EventStream actuals = EventsFilter.filterStream(events, REMFREQS);

		
		assertEquals(expectedStream.getStream(), actuals.getStream());
		assertEquals(expectedStream.getMapping(), actuals.getMapping());
		assertEquals(expectedStream.getStreamLength(), actuals.getStreamLength());
		assertEquals(expectedStream.getNumberEvents(), actuals.getNumberEvents());
		assertTrue(expectedStream.equals(actuals));
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

	private static IMethodName m(int i) {
		if (i == 0) {
			return Names.getUnknownMethod();
		} else {
			return Names.newMethod("[T,P, 1.2.3.4] [T,P, 1.2.3.4].m" + i + "()");
		}
	}
}