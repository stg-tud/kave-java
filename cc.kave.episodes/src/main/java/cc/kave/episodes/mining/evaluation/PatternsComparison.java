package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EpisodesFilter;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PatternsComparison {

	private File patternsFile;

	private EventStreamIo eventStream;

	private EpisodesParser episodeParser;
	private EpisodesFilter episodeFilter;
	private TransClosedEpisodes transClosure;

	private EpisodeToGraphConverter episodeGraphConverter;
	private EpisodeAsGraphWriter graphWriter;

	private static final int THRESHFREQ = 491;
	private static final double THRESHENT = 0.1;

	@Inject
	public PatternsComparison(@Named("patterns") File folder,
			EventStreamIo streamIo, EpisodesParser parser,
			EpisodesFilter filter, TransClosedEpisodes transClosed,
			EpisodeToGraphConverter graphConverter,
			EpisodeAsGraphWriter graphWriter) {
		assertTrue(folder.exists(), "Patterns folder does not exist");
		assertTrue(folder.isDirectory(), "Patterns is not a folder, but a file");
		this.patternsFile = folder;
		this.eventStream = streamIo;
		this.episodeParser = parser;
		this.episodeFilter = filter;
		this.transClosure = transClosed;
		this.episodeGraphConverter = graphConverter;
		this.graphWriter = graphWriter;
	}

	public void storePatterns(EpisodeType type, int foldNum, int frequency,
			double entropy) throws IOException {
		List<Event> events = eventStream.readMapping(frequency, foldNum);
		Map<Integer, Set<Episode>> patterns = getPatterns(type, foldNum,
				frequency);
		int patternId = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			for (Episode ep : entry.getValue()) {
				Episode epGraph = transClosure.remTransClosure(ep);
				DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter
						.convert(epGraph, events);
				graphWriter.write(
						graph,
						getGraphPath(type, foldNum, frequency, entropy,
								patternId));
				patternId++;
			}
		}
		Logger.log("Number of patterns for %s: %d", type.toString(), patternId);
	}

	private Map<Integer, Set<Episode>> getPatterns(EpisodeType type,
			int foldNum, int frequency) {
		Map<Integer, Set<Episode>> episodes = episodeParser.parse(type,
				frequency, foldNum);
		Map<Integer, Set<Episode>> patterns = episodeFilter.filter(type,
				episodes, THRESHFREQ, THRESHENT);
		return patterns;
	}

	private String getGraphPath(EpisodeType type, int foldNum, int frequency,
			double entropy, int patternId) {
		File filePath = new File(patternsFile.getAbsolutePath() + "/freq"
				+ frequency + "/entropy" + entropy + "/fold" + foldNum + "/"
				+ type.toString());
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		String fileName = filePath.getAbsolutePath() + "/pattern" + patternId
				+ ".dot";
		return fileName;
	}

	public void thresholdAnalyzer(int foldNum, int frequency) {
		Logger.log("\tNumber of patterns not covered");
		Logger.log("\tFrequency\tEntropy\tSequentials\tParallels");

		Map<Integer, Set<Episode>> episodeSeqs = episodeParser.parse(
				EpisodeType.SEQUENTIAL, frequency, foldNum);
		Map<Integer, Set<Episode>> episodePars = episodeParser.parse(
				EpisodeType.PARALLEL, frequency, foldNum);
		Map<Integer, Set<Episode>> episodeGens = episodeParser.parse(
				EpisodeType.GENERAL, frequency, foldNum);
		Set<Integer> frequencies = getFrequencies(episodePars);

		for (int freq : frequencies) {
			Map<Integer, Set<Episode>> patternSeqs = episodeFilter.filter(
					EpisodeType.SEQUENTIAL, episodeSeqs, freq, 0.0);
			Map<Integer, Set<Episode>> patternPars = episodeFilter.filter(
					EpisodeType.PARALLEL, episodePars, freq, 0.0);

			for (double ent = 0.1; ent <= 1.0; ent += 0.1) {
				Map<Integer, Set<Episode>> patternGens = episodeFilter.filter(
						EpisodeType.GENERAL, episodeGens, freq, ent);

				Tuple<Integer, Integer> withSeqs = getCoverage(patternGens,
						patternSeqs);
				Tuple<Integer, Integer> withPars = getCoverage(patternGens,
						patternPars);

				int numbPS = getNumbPatterns(patternSeqs);
				int numPP = getNumbPatterns(patternPars);

				Logger.log("\t%d\t%.3f\t%d\t%d", freq, ent,
						withSeqs.getSecond(), withPars.getSecond());
			}
		}
	}

	public void frequencyAnalyzer(int foldNum, int frequency, double entropy) {
		Logger.log("\tNumber of patterns not covered");
		Logger.log("\tConfiguration: %s-order", EpisodeType.SEQUENTIAL);
		Logger.log("\tEntropy = %.1f", entropy);
		Logger.log("\tFrequency\tGenerals\tPercentage\tParallels\tPercentage");

		Map<Integer, Set<Episode>> episodeSeqs = episodeParser.parse(
				EpisodeType.SEQUENTIAL, frequency, foldNum);
		Map<Integer, Set<Episode>> episodePars = episodeParser.parse(
				EpisodeType.PARALLEL, frequency, foldNum);
		Map<Integer, Set<Episode>> episodeGens = episodeParser.parse(
				EpisodeType.GENERAL, frequency, foldNum);
		Set<Integer> frequencies = getFrequencies(episodePars);

		for (int freq : frequencies) {
			Map<Integer, Set<Episode>> patternSeqs = episodeFilter.filter(
					EpisodeType.SEQUENTIAL, episodeSeqs, freq, 0.0);
			Map<Integer, Set<Episode>> patternPars = episodeFilter.filter(
					EpisodeType.PARALLEL, episodePars, freq, 0.0);
			Map<Integer, Set<Episode>> patternGens = episodeFilter.filter(
					EpisodeType.GENERAL, episodeGens, freq, entropy);

			Tuple<Integer, Integer> withGens = getCoverage(patternSeqs,
					patternGens);
			Tuple<Integer, Integer> withPars = getCoverage(patternSeqs,
					patternPars);

			int numbPG = getNumbPatterns(patternGens);
			int numbPP = getNumbPatterns(patternPars);

			Logger.log("\t%d\t%d\t%.3f\t%d\t%.3f", freq, withGens.getSecond(),
					getPercentage(withGens.getSecond(), numbPG),
					withPars.getSecond(),
					getPercentage(withPars.getSecond(), numbPP));
		}
	}

	private double getPercentage(Integer num1, int total) {
		double percentage = (num1 * 1.0) / (total * 1.0);
		return percentage;
	}

	private int getNumbPatterns(Map<Integer, Set<Episode>> patternSeqs) {
		int counter = 0;
		for (Map.Entry<Integer, Set<Episode>> entry : patternSeqs.entrySet()) {
			counter += entry.getValue().size();
		}
		return counter;
	}

	private Set<Integer> getFrequencies(Map<Integer, Set<Episode>> episodes) {
		Set<Integer> results = Sets.newLinkedHashSet();

		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			if (entry.getKey() == 1) {
				continue;
			}
			for (Episode ep : entry.getValue()) {
				int frequency = ep.getFrequency();
				if (frequency <= 491) {
					results.add(frequency);
				}
			}
		}
		return results;
	}

	public void entropyAnalyzer(int foldNum, int frequency) {
		Logger.log("\tNumber of patterns not covered for frequency = %d",
				THRESHFREQ);
		Logger.log("\tEntropy\tSequentials\tParallels");

		Map<Integer, Set<Episode>> episodeSeqs = episodeParser.parse(
				EpisodeType.SEQUENTIAL, frequency, foldNum);
		Map<Integer, Set<Episode>> patternsSeqs = episodeFilter.filter(
				EpisodeType.SEQUENTIAL, episodeSeqs, THRESHFREQ, 0.0);

		Map<Integer, Set<Episode>> episodePars = episodeParser.parse(
				EpisodeType.PARALLEL, frequency, foldNum);
		Map<Integer, Set<Episode>> patternsPars = episodeFilter.filter(
				EpisodeType.PARALLEL, episodePars, THRESHFREQ, 0.0);

		Map<Integer, Set<Episode>> episodeGens = episodeParser.parse(
				EpisodeType.GENERAL, frequency, foldNum);

		for (double entropy = 0.057; entropy <= 1.0; entropy += 0.001) {
			Map<Integer, Set<Episode>> patternGens = episodeFilter.filter(
					EpisodeType.GENERAL, episodeGens, THRESHFREQ, entropy);

			Tuple<Integer, Integer> withSeqs = getCoverage(patternGens,
					patternsSeqs);
			Tuple<Integer, Integer> withPars = getCoverage(patternGens,
					patternsPars);
			Logger.log("\t%.3f\t%d\t%d", entropy, withSeqs.getSecond(),
					withPars.getSecond());
		}
	}

	public void coverage(EpisodeType type1, EpisodeType type2, int foldNum,
			int frequency) {
		Logger.log("Comparing %s with %s patterns ...", type1.toString(),
				type2.toString());
		Logger.log("Frequency threshold = %d", THRESHFREQ);
		Logger.log("Entropy threshold = %.1f", THRESHENT);

		Map<Integer, Set<Episode>> patterns1 = getPatterns(type1, foldNum, frequency);
		Map<Integer, Set<Episode>> patterns2 = getPatterns(type2, foldNum, frequency);

		Tuple<Integer, Integer> result = getCoverage(patterns1, patterns2);
		Logger.log(
				"Configuration %s convers %d patterns from configuration %s",
				type1, result.getFirst(), type2);
		Logger.log(
				"Configuration %s does not cover %d patterns from configuration %s",
				type1, result.getSecond(), type2);
	}

	private Tuple<Integer, Integer> getCoverage(
			Map<Integer, Set<Episode>> patterns1,
			Map<Integer, Set<Episode>> patterns2) {
		Map<Set<Fact>, Set<Episode>> sets1 = patternsSetFact(patterns1);
		Map<Set<Fact>, Set<Episode>> sets2 = patternsSetFact(patterns2);

		int covered = 0;
		int notCovered = 0;

		for (Map.Entry<Set<Fact>, Set<Episode>> entry : sets2.entrySet()) {
			Set<Fact> events = entry.getKey();

			if (!sets1.containsKey(events)) {
				notCovered += entry.getValue().size();
				for (Episode pattern : entry.getValue()) {
					// Logger.log("%s", pattern.getFacts().toString());
				}
				continue;
			} else {
				for (Episode pattern : entry.getValue()) {
					if (isCovered(pattern, sets1.get(events))) {
						covered++;
					} else {
						notCovered++;
						// Logger.log("%s", pattern.getFacts().toString());
					}
				}
			}
		}
		return Tuple.newTuple(covered, notCovered);
	}

	private boolean isCovered(Episode pattern, Set<Episode> others) {
		for (Episode o : others) {
			if (represents(o, pattern)) {
				return true;
			}
		}
		return false;
	}

	private boolean represents(Episode other, Episode pattern) {
		Set<Fact> relations = pattern.getRelations();
		for (Fact rel : relations) {
			Tuple<Fact, Fact> facts = rel.getRelationFacts();
			Fact opp = new Fact(facts.getSecond() + ">" + facts.getFirst());
			if (other.getRelations().contains(opp)) {
				return false;
			}
		}
		return true;
	}

	private Map<Set<Fact>, Set<Episode>> patternsSetFact(
			Map<Integer, Set<Episode>> patterns) {
		Map<Set<Fact>, Set<Episode>> results = Maps.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			for (Episode p : entry.getValue()) {
				Set<Fact> events = p.getEvents();
				if (results.containsKey(events)) {
					results.get(events).add(p);
				} else {
					results.put(events, Sets.newHashSet(p));
				}
			}
		}
		return results;
	}

	// public void commonPatterns(EpisodeType type1, EpisodeType type2,
	// int freqEpisode, int foldNum, int freqThresh, double entropy)
	// throws IOException {
	// int equal = 0;
	// int unequal = 0;
	// int set = 0;
	//
	// Map<Integer, Map<Set<Fact>, Set<Episode>>> patterns1 = patternsSetFact(
	// type1, freqEpisode, foldNum, freqThresh, entropy);
	// Map<Integer, Map<Set<Fact>, Set<Episode>>> patterns2 = patternsSetFact(
	// type2, freqEpisode, foldNum, freqThresh, entropy);
	//
	// for (Map.Entry<Integer, Map<Set<Fact>, Set<Episode>>> entry : patterns2
	// .entrySet()) {
	// for (Map.Entry<Set<Fact>, Set<Episode>> entryPatterns : entry
	// .getValue().entrySet()) {
	// Set<Fact> events = entryPatterns.getKey();
	// Set<Episode> episodes2 = entryPatterns.getValue();
	// Map<Set<Fact>, Set<Episode>> pattMap1 = patterns1.get(entry
	// .getKey());
	//
	// if (pattMap1.containsKey(events)) {
	// if (!equalEpisodes(episodes2, pattMap1.get(events))) {
	// store(freqEpisode, pattMap1.get(events), type1, set,
	// foldNum);
	// store(freqEpisode, episodes2, type2, set, foldNum);
	// unequal++;
	// set++;
	// } else {
	// equal++;
	// }
	// }
	// }
	// }
	// Logger.log(
	// "Configurations %s and %s have %d equal patterns, and %d different representations of patterns.",
	// type1.toString(), type2.toString(), equal, unequal);
	// }

	// public void nonoverlappings(EpisodeType type1, EpisodeType type2,
	// int freqEpisode, int foldNum, int freqThresh, double entropy)
	// throws IOException {
	// int id = 0;
	//
	// Map<Integer, Map<Set<Fact>, Set<Episode>>> patterns1 = patternsSetFact(
	// type1, freqEpisode, foldNum, freqThresh, entropy);
	// Map<Integer, Map<Set<Fact>, Set<Episode>>> patterns2 = patternsSetFact(
	// type2, freqEpisode, foldNum, freqThresh, entropy);
	//
	// for (Map.Entry<Integer, Map<Set<Fact>, Set<Episode>>> entry : patterns2
	// .entrySet()) {
	// for (Map.Entry<Set<Fact>, Set<Episode>> entryPatterns : entry
	// .getValue().entrySet()) {
	// Set<Fact> events = entryPatterns.getKey();
	// Set<Episode> episodes2 = entryPatterns.getValue();
	// Map<Set<Fact>, Set<Episode>> pattMap1 = patterns1.get(entry
	// .getKey());
	//
	// if (!pattMap1.containsKey(events)) {
	// saveUniques(freqEpisode, episodes2, type2, id, foldNum);
	// id++;
	// }
	// }
	// }
	// }

	public void extractOverlappingExamples(EpisodeType type1,
			EpisodeType type2, int frequency, double entropy, int foldNum) {
		// Map<Set<Fact>, Set<Episode>> patterns1 = getPatternsSet(type1,
		// frequency, entropy, foldNum);
		// Map<Set<Fact>, Set<Episode>> patterns2 = getPatternsSet(type2,
		// frequency, entropy, foldNum);
		//
		// Map<Set<Fact>, Set<Episode>> overlaps = Maps.newLinkedHashMap();
		//
		// for (Map.Entry<Set<Fact>, Set<Episode>> entry : patterns1.entrySet())
		// {
		// Set<Fact> events = entry.getKey();
		// if (patterns2.containsKey(events)) {
		// boolean validCand = checkValidity(events, entry.getValue(),
		// patterns2.get(events));
		// if (validCand) {
		// if (entry.getValue().size() > patterns2.get(events).size()) {
		// overlaps.put(events, entry.getValue());
		// Logger.log("General pattern: %s", patterns2.get(events)
		// .toString());
		// Logger.log("Sequential patterns: %s", entry.getValue()
		// .toString());
		// } else {
		// overlaps.put(events, patterns2.get(events));
		// Logger.log("General pattern: %s", entry.getValue()
		// .toString());
		// Logger.log("Sequential patterns: %s",
		// patterns2.get(events).toString());
		// }
		// }
		// }
		// }
	}

	// public void extractConcreteCode(int frequency, int foldNum) {
	// List<Tuple<Event, List<Fact>>> stream = eventStream.parseStream(
	// frequency, foldNum);
	// Episode pattern = getPattern(459, 0.672295, "4", "6", "7", "17", "4>6",
	// "4>7", "4>17", "6>17", "6>7", "17>7");
	//
	// EnclosingMethods enclMethods = new EnclosingMethods(true);
	// int numMethods = 0;
	// for (Tuple<Event, List<Fact>> tuple : stream) {
	// List<Fact> method = tuple.getSecond();
	//
	// if (method.size() < 2) {
	// continue;
	// }
	// if (method.containsAll(pattern.getEvents())) {
	// enclMethods.addMethod(pattern, method, tuple.getFirst());
	// numMethods = enclMethods.getOccurrences();
	// }
	// if (numMethods > 30) {
	// break;
	// }
	// }
	// Set<IMethodName> methodNames = enclMethods.getMethodNames(numMethods);
	//
	// for (IMethodName eventName : methodNames) {
	// Logger.log("General pattern occurrences: %s",
	// eventName.getDeclaringType().getFullName() + "."
	// + eventName.getName());
	// }
	// }

	private boolean equalEpisodes(Set<Episode> set1, Set<Episode> set2) {
		if (set1.size() != set2.size()) {
			return false;
		}
		Iterator<Episode> it1 = set1.iterator();
		Iterator<Episode> it2 = set2.iterator();
		while (it1.hasNext()) {
			if (!assertFactSets(it1.next().getFacts(), it2.next().getFacts())) {
				return false;
			}
		}
		return true;
	}

	private boolean assertFactSets(Set<Fact> facts1, Set<Fact> facts2) {
		if (facts1.size() != facts2.size()) {
			return false;
		}
		Iterator<Fact> it1 = facts1.iterator();
		Iterator<Fact> it2 = facts2.iterator();
		while (it1.hasNext()) {
			if (!it1.next().equals(it2.next())) {
				return false;
			}
		}
		return true;
	}

	// private Episode getPattern(int frequency, double entropy, String...
	// strings) {
	// Episode episode = new Episode();
	//
	// episode.setFrequency(frequency);
	// episode.setEntropy(entropy);
	// episode.addStringsOfFacts(strings);
	//
	// return episode;
	// }

	private boolean checkValidity(Set<Fact> events, Set<Episode> episodes1,
			Set<Episode> episodes2) {
		if ((events.size() < 3) || (events.size() > 5)) {
			return false;
		}

		if (validEpisode(episodes1) || validEpisode(episodes2)) {
			return true;
		}
		return false;
	}

	private boolean validEpisode(Set<Episode> episodes) {
		if (episodes.size() == 1) {
			for (Episode ep : episodes) {
				if (ep.getRelations().size() == 0) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	private void checkCoverage(
			Map<Integer, Map<Set<Fact>, Set<Episode>>> patterns1,
			Map<Integer, Map<Set<Fact>, Set<Episode>>> patterns2,
			EpisodeType type1, EpisodeType type2) {

		Logger.log("Checking coverage between type %s and type %s!",
				type1.toString(), type2.toString());
		for (Map.Entry<Integer, Map<Set<Fact>, Set<Episode>>> entry : patterns2
				.entrySet()) {
			Logger.log("Pattern size: %d-nodes", entry.getKey());
			Map<Set<Fact>, Set<Episode>> factPatts1 = patterns1.get(entry
					.getKey());
			int covered = 0;
			int notConvered = 0;

			Logger.log("Printing uncovered patterns ...");
			for (Map.Entry<Set<Fact>, Set<Episode>> entryFacts : entry
					.getValue().entrySet()) {
				if (factPatts1.containsKey(entryFacts.getKey())) {
					covered += entryFacts.getValue().size();
				} else {
					notConvered += entryFacts.getValue().size();
					Logger.log("%s", entryFacts.getKey().toString());
				}
			}
			Logger.log(
					"Configuration %s coveres %d patterns from configuration %s!",
					type1.toString(), covered, type2.toString());
			Logger.log(
					"Configuration %s does not cover %d patterns from configuration %s!",
					type1.toString(), notConvered, type2.toString());
		}
	}

	private void store(int freq, Set<Episode> episodes, EpisodeType type,
			int setNum, int foldNum) throws IOException {
		List<Event> events = eventStream.readMapping(freq, foldNum);
		int episodeId = 0;

		for (Episode ep : episodes) {
			Episode epGraph = transClosure.remTransClosure(ep);
			DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter
					.convert(epGraph, events);
			graphWriter.write(graph,
					getGraphPath(freq, type, setNum, episodeId));
			episodeId++;
		}
	}

	private void saveUniques(int freq, Set<Episode> episodes, EpisodeType type,
			int id, int foldNum) throws IOException {
		List<Event> events = eventStream.readMapping(freq, foldNum);

		for (Episode ep : episodes) {
			Episode epGraph = transClosure.remTransClosure(ep);
			DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter
					.convert(epGraph, events);
			graphWriter.write(graph, getGraphPath(freq, type, -1, id));
		}
	}

	private String getGraphPath(int frequency, EpisodeType type, int setNum,
			int episodeId) {
		String fileName = getSetPath(frequency, type, setNum) + "/graph"
				+ episodeId + ".dot";
		return fileName;
	}

	private String getSetPath(int freq, EpisodeType type, int setNum) {
		File path = new File(patternsFile.getAbsolutePath() + "/freq" + freq
				+ "/" + type.toString() + "/set" + setNum);
		if (!path.exists()) {
			path.mkdirs();
		}
		return path.getAbsolutePath();
	}

	// private Map<Integer, Map<Set<Fact>, Set<Episode>>> patternsSetFact(
	// EpisodeType type, int freqEpisode, int foldNum, int freqThresh,
	// double entropy) {
	// Map<Integer, Set<Episode>> patterns = getPatterns(type, freqEpisode,
	// foldNum, freqThresh, entropy);
	// Map<Integer, Map<Set<Fact>, Set<Episode>>> results = Maps
	// .newLinkedHashMap();
	// // Map<Set<Fact>, Set<Episode>> results = Maps.newLinkedHashMap();
	// int numPatterns = 0;
	//
	// for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
	// if (entry.getKey() < 2) {
	// continue;
	// }
	// numPatterns += entry.getValue().size();
	// Map<Set<Fact>, Set<Episode>> currPatts = Maps.newLinkedHashMap();
	//
	// for (Episode episode : entry.getValue()) {
	// Set<Fact> episodeEvents = episode.getEvents();
	//
	// if (currPatts.containsKey(episodeEvents)) {
	// currPatts.get(episodeEvents).add(episode);
	// } else {
	// currPatts.put(episodeEvents, Sets.newHashSet(episode));
	// }
	// }
	// results.put(entry.getKey(), currPatts);
	// }
	// Logger.log("Configuration %s learns %d patterns!", type.toString(),
	// numPatterns);
	// return results;
	// }

	// private Map<Integer, Set<Episode>> getPatterns(EpisodeType type,
	// int freqEpisode, int foldNum, int freqThresh, double entropy) {
	// Map<Integer, Set<Episode>> episodes = episodeParser.parse(type,
	// freqEpisode, foldNum);
	// return episodeFilter.filter(type, episodes, freqThresh, entropy);
	// }
}
