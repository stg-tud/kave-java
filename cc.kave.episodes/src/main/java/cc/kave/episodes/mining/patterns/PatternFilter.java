package cc.kave.episodes.mining.patterns;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;

public class PatternFilter {

	private EpisodeParser parser;

	private PartialPatterns partials;
	private SequentialPatterns sequentials;
	private ParallelPatterns parallels;

	@Inject
	public PatternFilter(EpisodeParser epParser, PartialPatterns partials,
			SequentialPatterns sequentials, ParallelPatterns parallels) {
		this.parser = epParser;
		this.partials = partials;
		this.sequentials = sequentials;
		this.parallels = parallels;
	}

	public Map<Integer, Set<Episode>> filter(EpisodeType type, int frequency,
			int fthresh, double ethresh) throws Exception {
		 Map<Integer, Set<Episode>> episodes = parser.parser(frequency);

		if (type == EpisodeType.GENERAL) {
			return partials.filter(episodes, fthresh, ethresh);
		} else if (type == EpisodeType.SEQUENTIAL) {
			return sequentials.filter(episodes, fthresh);
		} else if (type == EpisodeType.PARALLEL) {
			return parallels.filter(episodes, fthresh);
		} else {
			throw new Exception("Not valid episode type!");
		}
	}
}
