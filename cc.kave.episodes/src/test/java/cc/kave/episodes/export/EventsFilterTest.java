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
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Events;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.episodes.model.EventStream;

public class EventsFilterTest {

	private static final int REMFREQS = 2;

	private List<Event> events;
	private List<Event> partitionEvents1;

	private EventStream expectedStream;
	private String expectedPartition1 = "";

	@Before
	public void setup() {
		events = Lists.newArrayList(firstCtx(1), inv(2), inv(3), firstCtx(0), superCtx(2), inv(5), inv(0), inv(2), firstCtx(3), superCtx(4), inv(3));
//		partitionEvents1 = Lists.newArrayList(ctx(2), inv(5), ctx(1), inv(4), inv(3), inv(2));

		expectedStream = new EventStream();
		expectedStream.addEvent(firstCtx(1)); // 1
		expectedStream.addEvent(inv(2)); // 2
		expectedStream.addEvent(inv(3)); // 3
		expectedStream.addEvent(firstCtx(0));
		expectedStream.addEvent(superCtx(2)); // 4
		expectedStream.addEvent(inv(2));
		expectedStream.addEvent(firstCtx(3)); // 5
		expectedStream.addEvent(superCtx(4));// 6
		expectedStream.addEvent(inv(3));

//		expectedPartition1 += "1,0.000\n2,0.001\n3,0.002\n4,0.503\n2,0.504\n5,1.005\n6,1.006\n3,1.007\n";
	}

	@Test
	public void filterStream() {
		EventStream actuals = EventsFilter.filterStream(events, REMFREQS);

		assertTrue(expectedStream.equals(actuals));
		assertEquals(expectedStream.getStream(), actuals.getStream());
		assertEquals(expectedStream.getMapping(), actuals.getMapping());
		assertEquals(expectedStream.getStreamLength(), actuals.getStreamLength());
		assertEquals(expectedStream.getNumberEvents(), actuals.getNumberEvents());
	}

	@Ignore
	@Test
	public void filterPartition() {
		String actualsPartition = EventsFilter.filterPartition(partitionEvents1, expectedStream.getMapping());

		assertEquals(expectedPartition1, actualsPartition);
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

	private static IMethodName m(int i) {
		if (i == 0) {
			return MethodName.UNKNOWN_NAME;
		} else {
			return MethodName.newMethodName("[T,P] [T,P].m" + i + "()");
		}
	}
}
