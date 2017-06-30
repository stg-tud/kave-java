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
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Lists;

public class StreamFileGeneratorTest {

	private static final int FREQUENCY = 2;

	private List<Tuple<Event, List<Event>>> events1;
	private List<Tuple<Event, List<Event>>> events2;

	private EventStream expStream1;
	private EventStream expStream2;

	@Before
	public void setup() {
		events1 = Lists.newLinkedList();
		events1.add(Tuple.newTuple(enclCtx(20),
				Lists.newArrayList(firstCtx(1), inv(2), inv(3))));
		events1.add(Tuple.newTuple(enclCtx(7), Lists.newArrayList(firstCtx(2),
				superCtx(2), inv(5), inv(20), inv(2))));
		events1.add(Tuple.newTuple(enclCtx(6),
				Lists.newArrayList(firstCtx(1), inv(2), inv(3))));
		events1.add(Tuple.newTuple(enclCtx(20),
				Lists.newArrayList(firstCtx(1), inv(2), inv(3))));
		events1.add(Tuple.newTuple(enclCtx(8),
				Lists.newArrayList(firstCtx(2), inv(2))));
		events1.add(Tuple.newTuple(enclCtx(6),
				Lists.newArrayList(firstCtx(1), inv(2), inv(3))));
		events1.add(Tuple.newTuple(enclCtx(20),
				Lists.newArrayList(firstCtx(3), superCtx(4), inv(3))));

		expStream1 = new EventStream();
		expStream1.addEvent(firstCtx(1)); // 1
		expStream1.addEvent(inv(2)); // 2
		expStream1.addEvent(inv(3)); // 3

		expStream1.increaseTimeout();
		expStream1.addEvent(firstCtx(2));
		expStream1.addEvent(inv(2)); // 2

		expStream1.increaseTimeout();
		expStream1.addEvent(firstCtx(1)); // 1
		expStream1.addEvent(inv(2)); // 2
		expStream1.addEvent(inv(3)); // 3

		expStream1.increaseTimeout();
		expStream1.addEvent(firstCtx(1)); // 1
		expStream1.addEvent(inv(2)); // 2
		expStream1.addEvent(inv(3)); // 3

		expStream1.increaseTimeout();
		expStream1.addEvent(firstCtx(2));
		expStream1.addEvent(inv(2)); // 2

		expStream1.increaseTimeout();
		expStream1.addEvent(firstCtx(1));
		expStream1.addEvent(inv(2));
		expStream1.addEvent(inv(3));

		expStream1.increaseTimeout();
		expStream1.addEvent(inv(3)); // 3

		events2 = Lists.newLinkedList();
		events2.add(Tuple.newTuple(enclCtx(20),
				Lists.newArrayList(firstCtx(1), inv(30))));
		events2.add(Tuple.newTuple(enclCtx(7), Lists.newArrayList(firstCtx(2),
				superCtx(2), inv(20), inv(5), inv(30))));
		events2.add(Tuple.newTuple(enclCtx(6),
				Lists.newArrayList(firstCtx(1), inv(2), inv(3))));
		events2.add(Tuple.newTuple(enclCtx(8),
				Lists.newArrayList(firstCtx(2), inv(30), inv(30))));
		events2.add(Tuple.newTuple(enclCtx(9),
				Lists.newArrayList(firstCtx(1), inv(3), inv(2))));
		events2.add(Tuple.newTuple(enclCtx(0),
				Lists.newArrayList(firstCtx(3), superCtx(4), inv(20))));

		expStream2 = new EventStream();
		expStream2.addEvent(firstCtx(1));
		expStream2.addEvent(inv(30));

		expStream2.increaseTimeout();
		expStream2.addEvent(firstCtx(2)); // 1
		expStream2.addEvent(inv(20)); // 2
		expStream2.addEvent(inv(30)); // 3

		expStream2.increaseTimeout();
		expStream2.addEvent(firstCtx(1));
		expStream2.addEvent(inv(2));
		expStream2.addEvent(inv(3));

		expStream2.increaseTimeout();
		expStream2.addEvent(firstCtx(2)); // 1
		expStream2.addEvent(inv(30)); // 3
		expStream2.addEvent(inv(30)); // 2

		expStream2.increaseTimeout();
		expStream2.addEvent(firstCtx(1));
		expStream2.addEvent(inv(3));
		expStream2.addEvent(inv(2));

		expStream2.increaseTimeout();
		expStream2.addEvent(inv(20));
	}

	@Test
	public void emptyStream() {
		List<Tuple<Event, List<Event>>> events = Lists.newLinkedList();

		EventStream expected = new EventStream();

//		EventStream actuals = StreamFileGenerator.generate(events, FREQUENCY);
//
//		assertTrue(expected.equals(actuals));
	}

	@Test
	public void filterStream1() {
//		EventStream actuals = StreamFileGenerator.generate(events1, FREQUENCY);
//
//		assertEquals(expStream1.getStreamText(), actuals.getStreamText());
//		assertEquals(expStream1.getMapping(), actuals.getMapping());
//		assertTrue(expStream1.equals(actuals));
	}

	@Test
	public void filterStream2() {
//		EventStream actuals = StreamFileGenerator.generate(events2, FREQUENCY);
//
//		assertEquals(expStream2.getStreamText(), actuals.getStreamText());
//		assertTrue(expStream2.equals(actuals));
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
		if (i == 10) {
			return Names
					.newMethod("[mscorlib,P, 4.0.0.0] [mscorlib,P, 4.0.0.0].m()");
		} else if (i == 20) {
			return Names.newMethod("[mscorlib,P] [mscorlib,P].m()");
		} else if (i == 30) {
			return Names.newMethod("[T,P] [T,P].m" + i + "()");
		} else {
			return Names
					.newMethod("[T,P, 1.2.3.4] [T,P, 1.2.3.4].m" + i + "()");
		}
	}
}