package cc.kave.episodes.postprocessor;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.evaluation.PatternsValidation;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Sets;

public class SpecificPatterns {

	private PatternsValidation validation;
	private EventStreamIo eventStream;
	private EpisodeToGraphConverter graph;

	@Inject
	public SpecificPatterns(PatternsValidation validate, EventStreamIo stream,
			EpisodeToGraphConverter graphConv) {
		this.validation = validate;
		this.eventStream = stream;
		this.graph = graphConv;
	}

	public void patternsInfo(EpisodeType type, int frequency, double entropy,
			int foldNum) throws Exception {
		Map<Integer, Set<Tuple<Episode, Boolean>>> patterns = validation
				.validate(type, frequency, entropy, foldNum);
		List<Event> events = eventStream.readMapping(frequency, foldNum);

		Set<Integer> patternEvents = Sets.newLinkedHashSet();
		Set<Integer> specificEvents = Sets.newLinkedHashSet();

		for (Map.Entry<Integer, Set<Tuple<Episode, Boolean>>> entry : patterns
				.entrySet()) {
			Logger.log("Episode size = %d", entry.getKey());
			Set<Tuple<Episode, Boolean>> episodes = entry.getValue();

			for (Tuple<Episode, Boolean> tuple : episodes) {
				Episode ep = tuple.getFirst();
				Set<Fact> epEvents = ep.getEvents();
				
				if (!tuple.getSecond()) {
					if (!patternEvents.containsAll(epEvents)) {
						Logger.log("Episode: %s", ep.toString());
						
						for (Fact fact : epEvents) {
							int id = fact.getFactID();
							Event e = events.get(id);
							String label = graph.toLabel(e.getMethod());
							Logger.log("%s", label);

							if (!patternEvents.contains(id)) {
								patternEvents.add(id);
								specificEvents.add(id);
							}
						}
						Logger.log("");
					}
				} else {
					for (Fact fact : epEvents) {
						int id = fact.getFactID();
						
						if (specificEvents.contains(id)) {
							specificEvents.remove(id);
						}
					}
				}
			}
			Logger.log("");
		}
		Logger.log("Printing patterns events ...");
		printEventsInfo(patternEvents, events);
		Logger.log("Printing specific events ...");
		printEventsInfo(specificEvents, events);
	}

	private void printEventsInfo(Set<Integer> specPattEvents, List<Event> events) {
		for (int id : specPattEvents) {
			Event event = events.get(id);
			String label = graph.toLabel(event.getMethod());
			Logger.log("%s", label);
		}
		Logger.log("");
	}
}
