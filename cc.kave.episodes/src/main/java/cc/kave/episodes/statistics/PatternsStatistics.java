package cc.kave.episodes.statistics;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.mining.patterns.ParallelPatterns;
import cc.kave.episodes.mining.patterns.PartialPatterns;
import cc.kave.episodes.mining.patterns.SequentialPatterns;
import cc.kave.episodes.model.Episode;
import cc.recommenders.io.Logger;

public class PatternsStatistics {

	private EpisodeParser episodeReader;
	private PartialPatterns partials;
	private SequentialPatterns sequentials;
	private ParallelPatterns parallels;

	@Inject
	public PatternsStatistics(EpisodeParser episodeReader,
			PartialPatterns partials, SequentialPatterns sequentials,
			ParallelPatterns parallels) {
		this.episodeReader = episodeReader;
		this.partials = partials;
		this.sequentials = sequentials;
		this.parallels = parallels;
	}

	public void numPatterns(int frequency, int threshFreq, double threshEnt) {
		Map<Integer, Set<Episode>> episodes = episodeReader.parser(frequency);

		Logger.log("\tPartial-order configuration:");
		Map<Integer, Set<Episode>> partPatterns = partials.filter(episodes,
				threshFreq, threshEnt);
		outputStats(partPatterns);

		Logger.log("\tSequential-order configuration:");
		Map<Integer, Set<Episode>> seqPatterns = sequentials.filter(episodes,
				threshFreq);
		outputStats(seqPatterns);

		Logger.log("\tNo-order configuration:");
		Map<Integer, Set<Episode>> paraPatterns = parallels.filter(episodes,
				threshFreq);
		outputStats(paraPatterns);
	}

	private void outputStats(Map<Integer, Set<Episode>> patterns) {
		Logger.log("\tPatternSize\tNumPatterns");
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			Logger.log("\t%d-node\t%d", entry.getKey(), entry.getValue().size());
		}
		Logger.log("");
	}

	private int patternsCounter(Map<Integer, Set<Episode>> patterns) {
		int counter = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			counter += entry.getValue().size();
		}
		return counter;
	}
}
