package cc.kave.episodes.evaluations.thresholds;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.mining.patterns.PatternFilter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.recommenders.io.Logger;

public class EntropyAnalyzer {

	
	private EpisodeParser parser;
	private PatternFilter filter;

	@Inject
	public EntropyAnalyzer(EpisodeParser episodeParser, PatternFilter patternFilter) {
		this.parser = episodeParser;
		this.filter = patternFilter;
	}

	public void threshold(int frequency) throws Exception {
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);
		Logger.log("\tEntropy threshold analyses!");
		Logger.log("\tFrequency\tEntropy\t#Patterns");

		int prevValue = 0;

		for (int freq = frequency; freq < frequency + 100; freq += 10) {

			for (double ent = 0.0; ent < 1.01; ent += 0.01) {
				double entropy = Math.round(ent * 100.0) / 100.0;
//				System.out.println(entropy);;
				Map<Integer, Set<Episode>> patterns = filter.filter(
						EpisodeType.GENERAL, episodes, freq, entropy);
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
