package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.FileReader;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.name.Named;

public class APIsGeneralizability {

	private File patternFolder;

	private FileReader reader;
	private EventStreamIo streamIo;

	@Inject
	public APIsGeneralizability(@Named("patterns") File folder,
			FileReader fileReader, EventStreamIo streamIo) {
		assertTrue(folder.exists(), "Patterns folder does not exist!");
		assertTrue(folder.isDirectory(),
				"Patterns is not a folder, but a file!");
		this.patternFolder = folder;
		this.reader = fileReader;
		this.streamIo = streamIo;
	}

	public void analyze(EpisodeType type, int frequeny, int threshFreq,
			double threshEnt) {
		File evaluationFile = getFilePath(type, threshFreq, threshEnt);
		List<String> evalText = reader.readFile(evaluationFile);
		List<Event> events = streamIo.readMapping(frequeny);

		int[][] array = new int[2][2];

		for (String line : evalText) {
			if (line.isEmpty() || line.contains("PatternId")) {
				continue;
			}
			if (line.contains("Patterns")) {
				if (sum(array) > 0) {
					Logger.log("\t%s", line);
					Logger.log("\t\t1 API\tMultiple APIs");
					Logger.log("\tSpecific\t%d\t%d", array[0][0], array[0][1]);
					Logger.log("\tGeneral\t%d\t%d", array[1][0], array[1][1]);
					Logger.log("");

					array = new int[2][2];
				}
				continue;
			}
			String[] elems = line.split("\t");
			String facts = elems[1];
			int freq = Integer.parseInt(elems[2]);
			double ent = Double.parseDouble(elems[3]);
			int gens = Integer.parseInt(elems[4]);

			Episode pattern = createPattern(facts, freq, ent);
			Set<String> apis = Sets.newHashSet();
			for (Fact e : pattern.getEvents()) {
				Event event = events.get(e.getFactID());
				ITypeName typeName = event.getMethod().getDeclaringType();
				apis.add(typeName.getNamespace().getIdentifier());
			}
			if ((gens == 1) && (apis.size() == 1)) {
				array[0][0] += 1;
			} else if ((gens == 1) && (apis.size() > 1)) {
				array[0][1] += 1;
			} else if ((gens > 1) && (apis.size() == 1)) {
				array[1][0] += 1;
			} else if ((gens > 1) && (apis.size() > 1)) {
				array[1][1] += 1;
			}
		}
		Logger.log("\t\t1 API\tMultiple APIs");
		Logger.log("\tSpecific\t%d\t%d", array[0][0], array[0][1]);
		Logger.log("\tGeneral\t%d\t%d", array[1][0], array[1][1]);
	}

	private int sum(int[][] array) {
		int sum = 0;
		for (int[] vect : array) {
			for (int value : vect) {
				sum += value;
			}
		}
		return sum;
	}

	public void apiUsages(EpisodeType type, int frequency, int threshFreq,
			double threshEntropy) {
		List<Event> events = streamIo.readMapping(frequency);
		Set<String> apiSpecific = Sets.newHashSet();
		Set<String> apiGeneral = Sets.newHashSet();

		Map<Episode, Integer> patterns = getEvaluations(type, threshFreq,
				threshEntropy);
		for (Map.Entry<Episode, Integer> entry : patterns.entrySet()) {
			Set<Fact> factEvent = entry.getKey().getEvents();
			if (factEvent.size() == 2) {
				if (entry.getValue() == 1) {
					for (Fact fact : factEvent) {
						int factId = fact.getFactID();
						ITypeName typeName = events.get(factId).getMethod()
								.getDeclaringType();
						apiSpecific
								.add(typeName.getNamespace().getIdentifier());
					}
				} else {
					for (Fact fact : factEvent) {
						int factId = fact.getFactID();
						ITypeName typeName = events.get(factId).getMethod()
								.getDeclaringType();
						apiGeneral.add(typeName.getNamespace().getIdentifier());
					}
				}
			}
		}
		Logger.log("APIs only in specific patterns:");
		for (String api : apiSpecific) {
			if (!apiGeneral.contains(api)) {
				Logger.log("%s", api);
			}
		}
		Logger.log("\nAPIs only in general patterns:");
		for (String api : apiGeneral) {
			if (!apiSpecific.contains(api)) {
				Logger.log("%s", api);
			}
		}
	}

	private Map<Episode, Integer> getEvaluations(EpisodeType type,
			int frequency, double entropy) {
		File evaluationFile = getFilePath(type, frequency, entropy);
		List<String> evalText = reader.readFile(evaluationFile);

		Map<Episode, Integer> evaluations = Maps.newLinkedHashMap();

		for (String line : evalText) {
			if (line.isEmpty() || line.contains("PatternId")
					|| line.contains("Patterns")) {
				continue;
			}
			String[] elems = line.split("\t");
			String facts = elems[1];
			int freq = Integer.parseInt(elems[2]);
			double ent = Double.parseDouble(elems[3]);
			int gens = Integer.parseInt(elems[4]);

			Episode pattern = createPattern(facts, freq, ent);
			evaluations.put(pattern, gens);
		}
		return evaluations;
	}

	private Episode createPattern(String facts, int freq, double ent) {
		Episode pattern = new Episode();

		String[] indFacts = facts.split(",");
		for (String fact : indFacts) {
			if (fact.contains("]")) {
				String factId = fact.substring(1, fact.length() - 1);
				pattern.addFact(new Fact(factId));
				continue;
			}
			String factId = fact.substring(1);
			pattern.addFact(new Fact(factId));
		}
		pattern.setFrequency(freq);
		pattern.setEntropy(ent);

		return pattern;
	}

	private File getFilePath(EpisodeType type, int frequeny, double entropy) {
		String path = patternFolder.getAbsolutePath() + "/freq" + frequeny
				+ "/entropy" + entropy + "/" + type + "/evaluations.txt";
		return new File(path);
	}
}
