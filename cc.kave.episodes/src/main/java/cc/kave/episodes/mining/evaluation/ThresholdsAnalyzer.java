package cc.kave.episodes.mining.evaluation;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.inject.Inject;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.Threshold;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Sets;

public class ThresholdsAnalyzer {

	private PatternsValidation patternsValidation;

	@Inject
	public ThresholdsAnalyzer(PatternsValidation validation) {
		this.patternsValidation = validation;
	}

	public void analyze(EpisodeType type, int frequency, double entropy,
			int foldNum) throws Exception {
		Map<Episode, Boolean> patterns = patternsValidation.validate(type,
				frequency, entropy, foldNum);
		SortedMap<Integer, Set<Double>> threshDist = getThreshDist(patterns);
		Set<Threshold> results = Sets.newLinkedHashSet();

		for (Map.Entry<Integer, Set<Double>> threshEntry : threshDist
				.entrySet()) {
			int freq = threshEntry.getKey();

			for (Double ent : threshEntry.getValue()) {
				Threshold threshItem = getThreshResults(patterns, freq, ent);
				results.add(threshItem);
			}
		}
		printResults(results);
	}

	private Threshold getThreshResults(Map<Episode, Boolean> patterns,
			int freq, Double ent) {
		Threshold threshResults = new Threshold(freq, ent);

		for (Map.Entry<Episode, Boolean> epEntry : patterns.entrySet()) {
			Episode episode = epEntry.getKey();

			if ((episode.getFrequency() >= freq)
					&& (episode.getEntropy() >= ent)) {

				if (epEntry.getValue()) {
					threshResults.addGenPattern();
				} else {
					threshResults.addSpecPattern();
				}
			}
		}
		return threshResults;
	}

	private void printResults(Set<Threshold> thresholds) {
		Tuple<Integer, Double> bestThreshs = Tuple.newTuple(0, 0.0);
		double bestFract = 0.0;

		Logger.log("\tFrequency\tEntropy\tNumGens\tNumSpecs\tFraction");
		for (Threshold item : thresholds) {
			int frequency = item.getFrequency();
			double entropy = item.getEntropy();
			int gens = item.getNoGenPatterns();
			int specs = item.getNoSpecPatterns();
			double fraction = item.getFraction();

			Logger.log("\t%d\t%.3f\t%d\t%d\t%.3f", frequency, entropy, gens, specs, fraction);

			if (fraction > bestFract) {
				bestThreshs = Tuple.newTuple(frequency, entropy);
				bestFract = fraction;
			}
		}
		Logger.log("\nBest results achieved for:");
		Logger.log("Frequency = %d", bestThreshs.getFirst());
		Logger.log("Entropy = %.3f", bestThreshs.getSecond());
		Logger.log("Generalizability = %.3f", bestFract);
	}

	private SortedMap<Integer, Set<Double>> getThreshDist(
			Map<Episode, Boolean> patterns) {
		SortedMap<Integer, Set<Double>> thresholds = new TreeMap<Integer, Set<Double>>();
		Set<Integer> frequencies = Sets.newHashSet();
		SortedSet<Double> entropies = new TreeSet<Double>();

		for (Map.Entry<Episode, Boolean> entry : patterns.entrySet()) {

			Episode episode = entry.getKey();
			int epFreq = episode.getFrequency();
			double epEntropy = episode.getEntropy();
			
			frequencies.add(epFreq);
			entropies.add(epEntropy);
		}
		for (int freq : frequencies) {
			thresholds.put(freq, entropies);
		}
		return thresholds;
	}
}
