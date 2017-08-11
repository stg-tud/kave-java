package cc.kave.episodes.statistics;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.episodes.io.EpisodeReader;
import cc.kave.episodes.mining.patterns.ParallelPatterns;
import cc.kave.episodes.mining.patterns.PartialPatterns;
import cc.kave.episodes.mining.patterns.SequentialPatterns;
import cc.kave.episodes.model.Episode;
import cc.recommenders.io.Logger;

public class PatternsStatistics {

	private EpisodeReader episodeReader;
	private PartialPatterns partials;
	private SequentialPatterns sequentials;
	private ParallelPatterns parallels;

	@Inject
	public PatternsStatistics(EpisodeReader episodeReader,
			PartialPatterns partials, SequentialPatterns sequentials,
			ParallelPatterns parallels) {
		this.episodeReader = episodeReader;
		this.partials = partials;
		this.sequentials = sequentials;
		this.parallels = parallels;
	}

	public void numPatterns(int frequency, int threshFreq, double threshEnt) {
		Map<Integer, Set<Episode>> episodes = episodeReader.read(frequency);

		Map<Integer, Set<Episode>> partPatterns = partials.filter(episodes,
				threshFreq, threshEnt);
		Logger.log("Number of patterns learned by partial configuration: %d",
				patternsCounter(partPatterns));

		Map<Integer, Set<Episode>> seqPatterns = sequentials.filter(episodes,
				threshFreq);
		Logger.log(
				"Number of patterns learned by sequential configuration: %d",
				patternsCounter(seqPatterns));

		Map<Integer, Set<Episode>> paraPatterns = parallels.filter(episodes,
				threshFreq);
		Logger.log("Number of patterns learned by parallel configuration: %d",
				patternsCounter(paraPatterns));
	}

	private int patternsCounter(Map<Integer, Set<Episode>> patterns) {
		int counter = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			counter += entry.getValue().size();
		}
		return counter;
	}
}
