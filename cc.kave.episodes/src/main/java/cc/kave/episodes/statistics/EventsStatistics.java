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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class EventsStatistics {

	private EventStreamIo eventStreamIo;

	@Inject
	public EventsStatistics(EventStreamIo streamIo) {
		this.eventStreamIo = streamIo;
	}

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

	public void histogram(int frequency) throws IOException {
		List<List<Fact>> stream = eventStreamIo.parseStream(frequency);
		Map<Fact, Integer> factOccs = getEventFreqs(stream);
		Set<Integer> frequencies = getFreqSet(factOccs);
		Map<Integer, Integer> histogram = Maps.newLinkedHashMap();

		for (Integer freq : frequencies) {
			int counter = 0;
			for (Map.Entry<Fact, Integer> entry : factOccs.entrySet()) {
				if (entry.getValue() >= freq) {
					counter++;
				}
			}
			histogram.put(freq, counter);
		}
		Logger.log("\tFrequency\tNumbEvents");
		for (Map.Entry<Integer, Integer> entry : histogram.entrySet()) {
			Logger.log("\t%d\t%d", entry.getKey(), entry.getValue());
		}
	}

	private Set<Integer> getFreqSet(Map<Fact, Integer> factOccs) {
		Set<Integer> output = Sets.newHashSet();

		for (Map.Entry<Fact, Integer> entry : factOccs.entrySet()) {
			output.add(entry.getValue());
		}
		return output;
	}

	private Map<Fact, Integer> getEventFreqs(List<List<Fact>> stream) {
		Map<Fact, Integer> frequencies = Maps.newLinkedHashMap();

		for (List<Fact> method : stream) {
			for (Fact fact : method) {
				if (frequencies.containsKey(fact)) {
					int counter = frequencies.get(fact);
					frequencies.put(fact, counter + 1);
				} else {
					frequencies.put(fact, 1);
				}
			}
		}
		return frequencies;
	}
}
