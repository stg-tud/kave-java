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

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Events;

public class StreamStatistics {

	public int minFreq(Map<Event, Integer> occ) {
		int min = Integer.MAX_VALUE;
		
		for (Map.Entry<Event, Integer> entry : occ.entrySet()) {
			int freq = entry.getValue();
			
			if (!entry.getKey().equals(Events.newDummyEvent()) && freq < min) {
				min = freq;
			}
		}
		return min;
	}

	public Map<Event, Integer> getFrequencies(List<Event> stream) {
		Map<Event, Integer> occurrences = Maps.newHashMap();
		
		for (Event e : stream) {
			if (occurrences.keySet().contains(e)) {
				int freq = occurrences.get(e);
				occurrences.put(e, freq + 1);
			} else {
				occurrences.put(e, 1);
			}
		}
		return occurrences;
	}
}
