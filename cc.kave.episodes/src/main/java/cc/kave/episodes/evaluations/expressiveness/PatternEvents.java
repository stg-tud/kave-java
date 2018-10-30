package cc.kave.episodes.evaluations.expressiveness;

import java.util.Map;
import java.util.Set;

import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.mining.patterns.PatternFilter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.io.Logger;

import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class PatternEvents {

	private EpisodeParser parser;
	private PatternFilter filter;

	@Inject
	public PatternEvents(EpisodeParser episodeParser,
			PatternFilter patternFilter) {
		this.parser = episodeParser;
		this.filter = patternFilter;
	}

	public void acrossTypes(int frequency, int freqThresh, double entThresh)
			throws Exception {
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);

		Map<Integer, Set<Episode>> sequentials = filter.filter(episodes,
				freqThresh, entThresh);
		Map<Integer, Set<Episode>> partials = filter.filter(episodes,
				freqThresh, entThresh);
		Map<Integer, Set<Episode>> unordered = filter.filter(episodes,
				freqThresh, entThresh);

		Set<Fact> seqFacts = getFacts(sequentials);
		Set<Fact> pocFacts = getFacts(partials);
		Set<Fact> nocFacts = getFacts(unordered);

		Logger.log("Number of events in sequential-order patterns: %d",
				seqFacts.size());
		Logger.log("Number of events in partial-order patterns: %d",
				pocFacts.size());
		Logger.log("Number of events in no-order patterns: %d", nocFacts.size());

		Logger.log("Partials cover sequential events? %s",
				pocFacts.containsAll(seqFacts));
		Logger.log("No-orders cover all partial events? %s",
				nocFacts.containsAll(pocFacts));
	}

	private Set<Fact> getFacts(Map<Integer, Set<Episode>> patterns) {
		Set<Fact> facts = Sets.newHashSet();
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			for (Episode episode : entry.getValue()) {
				for (Fact fact : episode.getEvents()) {
					facts.add(fact);
				}
			}
			break;
		}
		return facts;
	}
}
