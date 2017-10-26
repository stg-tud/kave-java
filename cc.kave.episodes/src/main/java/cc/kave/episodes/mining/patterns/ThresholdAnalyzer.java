package cc.kave.episodes.mining.patterns;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Sets;

public class ThresholdAnalyzer {

	private EventStreamIo streamIo;

	private EpisodeParser parser;
	private PatternFilter filter;

	@Inject
	public ThresholdAnalyzer(EventStreamIo streamIo,
			EpisodeParser episodeParser, PatternFilter patternFilter) {
		this.streamIo = streamIo;
		this.parser = episodeParser;
		this.filter = patternFilter;
	}

	public void EntDim(int frequency) throws Exception {
		Logger.log("\tEntropy threshold analyses!");
		printData(frequency, frequency + 100);
	}

	public void EntFreqDim(int frequency) throws Exception {
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);
		Logger.log("\tFrequency-entropy analyzes!");

		Set<Integer> frequencies = getFrequencies(episodes);
		int maxFreq = getMax(frequencies);

		printData(frequency, maxFreq);
	}

	public void createHistogram(EpisodeType type, int frequency, double entropy)
			throws Exception {
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);
		Set<Integer> frequencies = getFrequencies(episodes);
		int maxFreq = getMax(frequencies);
		// Logger.log("Max frequency = %d", maxFreq);

		Logger.log("\tHistogram for %s-configuration:", type.toString());
		Logger.log("\tEntropy threshold = %.2f", entropy);
		Logger.log("\tFrequency\t#Patterns");
		for (int freq = frequency; freq < maxFreq + 1; freq++) {
			Map<Integer, Set<Episode>> patterns = filter.filter(type, episodes,
					freq, entropy);
			int numbPatterns = getNumbPatterns(patterns);
			Logger.log("\t%d\t%d", freq, numbPatterns);
		}
	}

	public void generalizability(int frequency) throws Exception {
		List<Tuple<IMethodName, List<Fact>>> stream = streamIo
				.readStreamObject(frequency);
		Map<String, Set<IMethodName>> repos = streamIo.readRepoCtxs(frequency);
		List<Event> events = streamIo.readMapping(frequency);
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);
		Set<Integer> frequencies = getFrequencies(episodes);

		int bestFreq = 0;
		double bestEntr = 0;
		
		for (int freq : frequencies) {
			if (freq > 638) {
				continue;
			}
			for (double ent = 0.0; ent < 1.01; ent += 0.01) {
				double entropy = Math.round(ent * 100.0) / 100.0;
				Map<Integer, Set<Episode>> patterns = filter.filter(
						EpisodeType.GENERAL, episodes, freq, ent);
			}
		}
	}

	private void printData(int frequency, int maxFreq) throws Exception {
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);
		Logger.log("\tFrequency\tEntropy\t#Patterns");

		int prevValue = 0;

		for (int freq = frequency; freq < maxFreq; freq += 10) {

			for (double ent = 0.0; ent < 1.01; ent += 0.01) {
				double entropy = Math.round(ent * 100.0) / 100.0;
				// System.out.println(entropy);;
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

	private int getMax(Set<Integer> frequencies) {
		int max = Integer.MIN_VALUE;
		for (int freq : frequencies) {
			if (freq > max) {
				max = freq;
			}
		}
		return max;
	}

	private Set<Integer> getFrequencies(Map<Integer, Set<Episode>> episodes) {
		TreeSet<Integer> results = Sets.newTreeSet();

		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			if (entry.getKey() == 1) {
				continue;
			}
			for (Episode ep : entry.getValue()) {
				int frequency = ep.getFrequency();
				results.add(frequency);
			}
		}
		return results;
	}

	private int getNumbPatterns(Map<Integer, Set<Episode>> patterns) {
		int counter = 0;
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			counter += entry.getValue().size();
		}
		return counter;
	}
}
