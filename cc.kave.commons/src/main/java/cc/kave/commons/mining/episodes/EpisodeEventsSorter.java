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
package cc.kave.commons.mining.episodes;

import java.util.HashMap;
import java.util.Map;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.episodes.OrderedEventsInEpisode;

public class EpisodeEventsSorter {

	public OrderedEventsInEpisode sort(Episode episode) throws Exception {

		if (episode.equals(new Episode())) {
			throw new Exception("You passed an empty episode!");
		}

		OrderedEventsInEpisode orderedEvents = new OrderedEventsInEpisode();
		Map<String, Integer> countSuccessorsPerEvent = successorsCounter(episode);

		int numberOfSequentialEvents = countSuccessorsPerEvent.size();

		if (numberOfSequentialEvents > 0) {
			for (int counter = numberOfSequentialEvents - 1; counter >= 0; counter--) {
				if (countSuccessorsPerEvent.containsValue(counter)) {
					String eventsRow = "";
					for (Map.Entry<String, Integer> entry : countSuccessorsPerEvent.entrySet()) {
						if (entry.getValue() == counter) {
							eventsRow += entry.getKey() + " ";
						}
					}
					orderedEvents.addEventIDInSequentialOrderList(eventsRow.substring(0, eventsRow.length() - 1));
				}
			}
		}
		if (numberOfSequentialEvents < episode.getNumEvents()) {
			for (Fact fact : episode.getFacts()) {
				if (!countSuccessorsPerEvent.containsKey(fact.getRawFact()) && (fact.getRawFact().length() == 1)) {
					orderedEvents.addEventIDInPartialOrderList(fact.getRawFact());
				}
			}
		}
		return orderedEvents;
	}

	private Map<String, Integer> successorsCounter(Episode episode) {
		Map<String, Integer> counter = new HashMap<String, Integer>();

		for (Fact fact : episode.getFacts()) {
			if (fact.getRawFact().length() > 1) {
				String[] events = fact.getRawFact().split(">");
				if (counter.containsKey(events[0])) {
					int count = counter.get(events[0]);
					counter.put(events[0], (count + 1));
				} else {
					counter.put(events[0], 1);
				}
				if (!counter.containsKey(events[1])) {
					counter.put(events[1], 0);
				}
			}
		}
		return counter;
	}
}
