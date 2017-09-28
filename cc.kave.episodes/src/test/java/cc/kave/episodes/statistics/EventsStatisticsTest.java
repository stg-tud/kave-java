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
package cc.kave.episodes.statistics;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class EventsStatisticsTest {

	private static final String DUMMY_METHOD_NAME = "[You, Can] [Safely, Ignore].ThisDummyValue()";
	private static final IMethodName DUMMY_METHOD = Names.newMethod(DUMMY_METHOD_NAME);
	public static final Event DUMMY_EVENT = Events.newElementContext(DUMMY_METHOD);

	private List<Tuple<Event, List<Event>>> stream;
	private Map<Event, Integer> occurrences;

	@Before
	public void setup() {
		stream = Lists.newLinkedList();
		stream.add(Tuple.newTuple(ctx(1), Lists.newArrayList(inv(2), inv(3))));
		stream.add(Tuple.newTuple(ctx(4), Lists.newArrayList(inv(5), inv(2))));
		stream.add(Tuple.newTuple(ctx(1), Lists.newArrayList(inv(3))));

		occurrences = Maps.newHashMap();
		occurrences.put(inv(2), 2);
		occurrences.put(inv(3), 2);
		occurrences.put(inv(5), 1);
	}

	@Test
	public void aggregationTest() {
		Map<Event, Integer> actuals = EventsStatistics.getFrequencies(stream);

		assertEquals(occurrences, actuals);
	}

	@Test
	public void freqTest() {
		int actuals = EventsStatistics.minFreq(occurrences);

		assertEquals(1, actuals);
	}

	private static Event inv(int i) {
		return Events.newInvocation(m(i));
	}

	private static Event ctx(int i) {
		return Events.newElementContext(m(i));
	}

	private static IMethodName m(int i) {
		return Names.newMethod("[T,P] [T,P].m" + i + "()");
	}
}