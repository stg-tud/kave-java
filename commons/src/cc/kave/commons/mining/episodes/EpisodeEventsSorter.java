package cc.kave.commons.mining.episodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Fact;

public class EpisodeEventsSorter {

	public List<List<Event>> sort(Episode episode) {
		Map<String, Integer> countSuccessorsPerEvent = successorsCounter(episode);
		return null;
	}

	private Map<String, Integer> successorsCounter(Episode episode) {
		Map<String, Integer> counter = new HashMap<String, Integer>();

		for (Fact relation : episode.getFacts()) {
			String[] events = relation.getRawFact().split(">");
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
		return counter;
	}
}
