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

import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.Threshold;
import cc.kave.episodes.model.Triplet;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Sets;

public class ThresholdsAnalyzer {

	private File patternsFolder;
	private EpisodesParser episodeParser;
	private PatternsValidation patternsValidation;

	@Inject
	public ThresholdsAnalyzer(@Named("patterns") File folder,
			EpisodesParser parser, PatternsValidation validation) {
		assertTrue(folder.exists(), "Patterns folder does not exist");
		assertTrue(folder.isDirectory(), "Patterns is not a folder, but a file");
		this.patternsFolder = folder;
		this.episodeParser = parser;
		this.patternsValidation = validation;
	}

	public void analyze(EpisodeType type, int frequency, double entropy,
			int foldNum) throws Exception {
		Map<Integer, Set<Episode>> episodes = episodeParser.parse(type,
				frequency, foldNum);
		Map<Integer, Set<Triplet<Episode, Integer, Integer>>> validations = patternsValidation
				.validate(episodes, frequency, foldNum);
		SortedMap<Integer, Set<Double>> threshDist = getThreshDist(validations);
		Set<Threshold> results = Sets.newLinkedHashSet();

		for (Map.Entry<Integer, Set<Double>> threshEntry : threshDist
				.entrySet()) {
			int freq = threshEntry.getKey();

			Threshold threshItem;
			for (Double ent : threshEntry.getValue()) {
				threshItem = getThreshResults(type, validations, freq, ent);
				results.add(threshItem);
			}
		}
		printResults(results, type, frequency);
	}

	private Threshold getThreshResults(EpisodeType type,
			Map<Integer, Set<Triplet<Episode, Integer, Integer>>> patterns,
			int freq, double ent) {
		Threshold threshResults = new Threshold(freq, ent);

		for (Map.Entry<Integer, Set<Triplet<Episode, Integer, Integer>>> epEntry : patterns
				.entrySet()) {
			Set<Triplet<Episode, Integer, Integer>> episodeSet = epEntry
					.getValue();

			for (Triplet<Episode, Integer, Integer> triplet : episodeSet) {
				Episode episode = triplet.getFirst();

				if (episode.getFrequency() >= freq) {
					if (type == EpisodeType.GENERAL) {
						if (valid(episode) && (episode.getEntropy() >= ent)) {
							if ((triplet.getThird() == 0)
									&& (triplet.getSecond() < 2)) {
								threshResults.addSpecPattern();
							} else {
								threshResults.addGenPattern();
							}
						}
					} else {
						if ((triplet.getThird() == 0)
								&& (triplet.getSecond() < 2)) {
							threshResults.addSpecPattern();
						} else {
							threshResults.addGenPattern();
						}
					}
				}
			}
		}
		return threshResults;
	}

	private boolean valid(Episode episode) {
		Set<Fact> events = episode.getEvents();
		Set<Fact> relations = episode.getRelations();
		int numRels = relations.size();

		if ((numRels != 0) && (numRels < maxRels(events.size()))) {
			return true;
		}
		return false;
	}

	private int maxRels(int numEvents) {
		if (numEvents < 3) {
			return 1;
		} else {
			return (numEvents - 1) + maxRels(numEvents - 1);
		}
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
			Map<Integer, Set<Triplet<Episode, Integer, Integer>>> patterns) {
		SortedMap<Integer, Set<Double>> thresholds = new TreeMap<Integer, Set<Double>>();
		Set<Integer> frequencies = Sets.newHashSet();
		SortedSet<Double> entropies = new TreeSet<Double>();

		for (Map.Entry<Integer, Set<Triplet<Episode, Integer, Integer>>> entry : patterns
				.entrySet()) {
			if (entry.getKey() < 3) {
				continue;
			}
			Set<Triplet<Episode, Integer, Integer>> episodeSet = entry
					.getValue();
			for (Triplet<Episode, Integer, Integer> triplet : episodeSet) {
				Episode episode = triplet.getFirst();

				if (!valid(episode)) {
					continue;
				}
				int epFreq = episode.getFrequency();
				double epEntropy = episode.getEntropy();
				double roundEnt = Math.floor(epEntropy * 1000) / 1000;

				frequencies.add(epFreq);
				entropies.add(roundEnt);
			}
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
