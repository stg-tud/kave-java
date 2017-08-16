package cc.kave.episodes.evaluations.thresholds;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.episodes.mining.patterns.PatternFilter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.recommenders.io.Logger;

public class EntropyAnalyzer {

	private PatternFilter filter;

	@Inject
	public EntropyAnalyzer(PatternFilter patternFilter) {
		this.filter = patternFilter;
	}

	public void threshold(int frequency) throws Exception {
		Logger.log("\tEntropy threshold analyses!");
		Logger.log("\tFrequency\tEntropy\t#Patterns");

		int prevValue = 0;

		for (int freq = frequency; freq <= frequency + 50; freq += 5) {

			for (double ent = 0.0; ent <= 1.0; ent += 0.01) {
				Map<Integer, Set<Episode>> patterns = filter.filter(
						EpisodeType.GENERAL, frequency, freq, ent);
				int counter = getNumbPatterns(patterns);
				if (counter != prevValue) {
					prevValue = counter;
					Logger.log("\t%d\t%.2f\t%d", freq, ent, counter);
				}
			}
		}
	}

	private int getNumbPatterns(Map<Integer, Set<Episode>> patterns) {
		int counter = 0;
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			counter += entry.getValue().size();
		}
		return counter;
	}
}
