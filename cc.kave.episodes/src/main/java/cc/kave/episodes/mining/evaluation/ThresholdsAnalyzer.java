package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.Threshold;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Sets;

public class ThresholdsAnalyzer {

	private File patternsFolder;
	private PatternsValidation patternsValidation;

	@Inject
	public ThresholdsAnalyzer(@Named("patterns") File folder,
			PatternsValidation validation) {
		assertTrue(folder.exists(), "Patterns folder does not exist");
		assertTrue(folder.isDirectory(), "Patterns is not a folder, but a file");
		this.patternsFolder = folder;
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

			Threshold threshItem;
			if (type == EpisodeType.GENERAL) {
				for (Double ent : threshEntry.getValue()) {
					threshItem = getThreshResults(patterns, freq, ent);
					results.add(threshItem);
				}
			} else {
				threshItem = getThreshResults(patterns, freq, 0.0);
				results.add(threshItem);
			}
		}
		printResults(results, type, frequency);
	}

	private Threshold getThreshResults(Map<Episode, Boolean> patterns,
			int freq, double ent) {
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

	private void printResults(Set<Threshold> thresholds, EpisodeType type,
			int frequency) throws IOException {
		Tuple<Integer, Double> bestThreshs = Tuple.newTuple(0, 0.0);
		StringBuilder sb = new StringBuilder();
		double bestFract = 0.0;

		sb.append("Frequency\tEntropy\tNumGens\tNumSpecs\tFraction\n");
		for (Threshold item : thresholds) {
			int itemFreq = item.getFrequency();
			double itemEntropy = item.getEntropy();
			int gens = item.getNoGenPatterns();
			int specs = item.getNoSpecPatterns();
			double fraction = Math.floor(item.getFraction() * 1000) / 1000;

			sb.append(itemFreq + "\t");
			sb.append(itemEntropy + "\t");
			sb.append(gens + "\t");
			sb.append(specs + "\t");
			sb.append(fraction + "\n");

			if (fraction > bestFract) {
				bestThreshs = Tuple.newTuple(itemFreq, itemEntropy);
				bestFract = fraction;
			}
		}
		sb.append("\nBest results achieved for:\n");
		sb.append("Frequency = " + bestThreshs.getFirst() + "\n");
		if (type == EpisodeType.GENERAL) {
			sb.append("Entropy = " + bestThreshs.getSecond() + "\n");
		}
		sb.append("Generalizability = " + bestFract);

		FileUtils.writeStringToFile(new File(getFilePath(frequency, type)),
				sb.toString());
		;
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
			double roundEnt = Math.floor(epEntropy * 1000) / 1000;

			frequencies.add(epFreq);
			entropies.add(roundEnt);
		}
		for (int freq : frequencies) {
			thresholds.put(freq, entropies);
		}
		return thresholds;
	}

	private String getFilePath(int frequency, EpisodeType type) {
		File path = new File(patternsFolder.getAbsolutePath() + "/freq"
				+ frequency + "/" + type.toString());
		if (!path.exists()) {
			path.mkdirs();
		}
		String fileName = path.getAbsolutePath() + "/thresholds.txt";
		return fileName;
	}
}
