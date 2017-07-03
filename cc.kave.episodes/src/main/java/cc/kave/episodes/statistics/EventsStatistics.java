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

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.recommenders.datastructures.Tuple;

public class EventsStatistics {

	public static int minFreq(Map<Event, Integer> occ) {
		int min = Integer.MAX_VALUE;
		
		for (Map.Entry<Event, Integer> entry : occ.entrySet()) {
			int freq = entry.getValue();
			
			if (!entry.getKey().equals(Events.newDummyEvent()) && freq < min) {
				min = freq;
			}
		}
		return min;
	}

	public static Map<Event, Integer> getFrequencies(
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
