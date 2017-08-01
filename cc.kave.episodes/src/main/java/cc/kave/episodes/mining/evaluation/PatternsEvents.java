package cc.kave.episodes.mining.evaluation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.episodes.io.EpisodesReader;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EpisodesFilter;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PatternsEvents {

	private EventStreamIo eventsStream;
	private EpisodesReader episodeParser;
	private EpisodesFilter episodeFilter;

	@Inject
	public PatternsEvents(EventStreamIo eventsIo, EpisodesReader parser,
			EpisodesFilter filter) {
		this.eventsStream = eventsIo;
		this.episodeParser = parser;
		this.episodeFilter = filter;
	}

	public void getEventsType(EpisodeType type, int frequency, double entropy,
			int foldNum) {
		List<Event> events = eventsStream.readMapping(frequency);
		Map<Integer, Set<Episode>> episodes = episodeParser.parse(frequency);
		Map<Integer, Set<Episode>> patterns = episodeFilter.filter(type,
				episodes, frequency, entropy);

		int numPatterns = 0;
		Map<EventKind, Set<Event>> eventKinds = Maps.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			for (Episode ep : entry.getValue()) {
				numPatterns++;
				for (Fact fact : ep.getEvents()) {
					int factId = fact.getFactID();
					Event event = events.get(factId);
					EventKind kind = event.getKind();

					if (eventKinds.containsKey(kind)) {
						eventKinds.get(kind).add(event);
					} else {
						eventKinds.put(kind, Sets.newHashSet(event));
					}
				}
			}
		}
		Logger.log("Configuration %s learns %d patterns", type.toString(),
				numPatterns);
		Logger.log("Patterns contains the following event kinds:");
		for (Map.Entry<EventKind, Set<Event>> entry : eventKinds.entrySet()) {
			Logger.log("Kind %s: %d events", entry.getKey().toString(), entry
					.getValue().size());
		}
	}
}
