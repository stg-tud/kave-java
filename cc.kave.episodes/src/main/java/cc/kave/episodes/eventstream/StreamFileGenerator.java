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

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cc.kave.episodes.data.ContextsParser;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class StreamFileGenerator {
	
	private ContextsParser ctxParser;
	
	@Inject
	public StreamFileGenerator(ContextsParser parser) {
		this.ctxParser = parser;
	}

	public static EventStream generate(List<Tuple<Event, List<Event>>> stream,
			int frequency) {
		Map<Event, Integer> occurrences = getEventFrequencies(stream);
		EventStream es = new EventStream();
		boolean isFirst = true;
		
		for (Tuple<Event, List<Event>> tuple : stream) {
			List<Event> method = Lists.newLinkedList();
			for (Event event : tuple.getSecond()) {
				if (occurrences.get(event) >= frequency) {
					method.add(event);
				}
			}
			if (validMethod(method)) {
				if (isFirst) {
					isFirst = false;
				} else {
					es.increaseTimeout();
				}
				for (Event e : method) {
					es.addEvent(e);
				}
			}
		}
		occurrences.clear();
		return es;
	}

	private static boolean validMethod(List<Event> method) {
		if (method.size() == 0) {
			return false;
		}
		for (Event event : method) {
			if (event.getKind() == EventKind.INVOCATION) {
				return true;
			}
		}
		return false;
	}

	private static Map<Event, Integer> getEventFrequencies(
			List<Tuple<Event, List<Event>>> stream) {
		Map<Event, Integer> results = Maps.newHashMap();

		for (Tuple<Event, List<Event>> tuple : stream) {
			for (Event event : tuple.getSecond()) {
				if (results.containsKey(event)) {
					int counter = results.get(event);
					results.put(event, counter + 1);
				} else {
					results.put(event, 1);
				}
			}
		}
		return results;
	}
}