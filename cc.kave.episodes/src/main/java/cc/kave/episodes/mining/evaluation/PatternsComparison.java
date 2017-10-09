package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.mining.patterns.PatternFilter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PatternsComparison {

	private File patternsFile;

	private EventStreamIo eventStream;

	private EpisodeParser episodeParser;
	private PatternFilter episodeFilter;
	private TransClosedEpisodes transClosure;

	private EpisodeToGraphConverter episodeGraphConverter;
	private EpisodeAsGraphWriter graphWriter;

	@Inject
	public PatternsComparison(@Named("patterns") File folder,
			EventStreamIo streamIo, EpisodeParser parser, PatternFilter filter,
			TransClosedEpisodes transClosed,
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

	// public void storePatterns(EpisodeType type, int foldNum, int frequency)
	// throws IOException {
	// List<Event> events = eventStream.readMapping(frequency);
	// Map<Integer, Set<Episode>> patterns = getPatterns(type, frequency);
	// int patternId = 0;
	//
	// for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
	// for (Episode ep : entry.getValue()) {
	// Episode epGraph = transClosure.remTransClosure(ep);
	// DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter
	// .convert(epGraph, events);
	// graphWriter.write(
	// graph,
	// getGraphPath(type, foldNum, THRESHFREQ, THRESHENT,
	// patternId));
	// patternId++;
	// }
	// }
	// Logger.log("Number of patterns for %s: %d", type.toString(), patternId);
	// }
	//
	// private Map<Integer, Set<Episode>> getPatterns(EpisodeType type,
	// int frequency) {
	// Map<Integer, Set<Episode>> episodes = episodeParser.read(frequency);
	// Map<Integer, Set<Episode>> patterns = episodeFilter.filter(type,
	// episodes, THRESHFREQ, THRESHENT);
	// return patterns;
	// }
	// // public void thresholdAnalyzer(int foldNum, int frequency) {
	// // Logger.log("\tNumber of patterns not covered");
	// // Logger.log("\tFrequency\tEntropy\tSequentials\tParallels");
	// //
	// // Map<Integer, Set<Episode>> episodeSeqs = episodeParser.parse(
	// // EpisodeType.SEQUENTIAL, frequency, foldNum);
	// // Map<Integer, Set<Episode>> episodePars = episodeParser.parse(
	// // EpisodeType.PARALLEL, frequency, foldNum);
	// // Map<Integer, Set<Episode>> episodeGens = episodeParser.parse(
	// // EpisodeType.GENERAL, frequency, foldNum);
	// // Set<Integer> frequencies = getFrequencies(episodePars);
	// //
	// // for (int freq : frequencies) {
	// // Map<Integer, Set<Episode>> patternSeqs = episodeFilter.filter(
	// // EpisodeType.SEQUENTIAL, episodeSeqs, freq, 0.0);
	// // Map<Integer, Set<Episode>> patternPars = episodeFilter.filter(
	// // EpisodeType.PARALLEL, episodePars, freq, 0.0);
	// //
	// // for (double ent = 0.1; ent <= 1.0; ent += 0.1) {
	// // Map<Integer, Set<Episode>> patternGens = episodeFilter.filter(
	// // EpisodeType.GENERAL, episodeGens, freq, ent);
	// //
	// // // Tuple<Integer, Integer> withSeqs = getCoverage(patternGens,
	// // // patternSeqs);
	// // // Tuple<Integer, Integer> withPars = getCoverage(patternGens,
	// // // patternPars);
	// //
	// // int numbPS = getNumbPatterns(patternSeqs);
	// // int numPP = getNumbPatterns(patternPars);
	// //
	// // Logger.log("\t%d\t%.3f\t%d\t%d", freq, ent,
	// // withSeqs.getSecond(), withPars.getSecond());
	// // }
	// // }
	// // }
	//
	// public void thresholdPartials(int foldNum, int frequency) {
	// Logger.log("\tThreshold analyzes for partial-order configuration!");
	// Logger.log("\tFrequency\tEntropy\t#Patterns");
	//
	// Map<Integer, Set<Episode>> episodeGens = episodeParser.read(frequency);
	// Set<Integer> frequencies = getFrequencies(episodeGens);
	// int maxFreq = getMax(frequencies);
	// int prevValue = 0;
	//
	// for (int freq = 300; freq <= 400; freq += 10) {
	//
	// for (double ent = 0.0; ent <= 1.0; ent += 0.01) {
	// Map<Integer, Set<Episode>> patternGens = episodeFilter.filter(
	// EpisodeType.GENERAL, episodeGens, freq, ent);
	// int counter = getNumbPatterns(patternGens);
	// if (counter != prevValue) {
	// prevValue = counter;
	// Logger.log("\t%d\t%.2f\t%d", freq, ent, counter);
	// }
	// }
	// }
	// }
	//
	// public void entropyMedium(int foldNum, int frequency) {
	// Logger.log("\tEntropy analyzes for partial-order configuration!");
	// Logger.log("\tFrequency\tEntropy\t#Patterns");
	//
	// Map<Integer, Set<Episode>> episodeGens = episodeParser.read(frequency);
	// Set<Integer> frequencies = getFrequencies(episodeGens);
	// int maxFreq = getMax(frequencies);
	// int prevValue = 0;
	//
	// for (int freq = 300; freq <= 1700; freq++) {
	// double entropy = 0.0;
	// int numPatterns = 0;
	// for (double ent = 0.0; ent <= 1.0; ent += 0.01) {
	// Map<Integer, Set<Episode>> patternGens = episodeFilter.filter(
	// EpisodeType.GENERAL, episodeGens, freq, ent);
	// int counter = getNumbPatterns(patternGens);
	// if (counter >= numPatterns) {
	// numPatterns = counter;
	// entropy = ent;
	// }
	// }
	// Logger.log("\t%d\t%.2f\t%d", freq, entropy, numPatterns);
	// }
	// }
	//
	// // public void frequencyAnalyzer(int foldNum, int frequency, double
	// entropy)
	// // {
	// // Logger.log("\tNumber of patterns not covered");
	// // Logger.log("\tConfiguration: %s-order", EpisodeType.SEQUENTIAL);
	// // Logger.log("\tEntropy = %.1f", entropy);
	// //
	// Logger.log("\tFrequency\tGenerals\tPercentage\tParallels\tPercentage");
	// //
	// // Map<Integer, Set<Episode>> episodeSeqs = episodeParser.parse(
	// // EpisodeType.SEQUENTIAL, frequency, foldNum);
	// // Map<Integer, Set<Episode>> episodePars = episodeParser.parse(
	// // EpisodeType.PARALLEL, frequency, foldNum);
	// // Map<Integer, Set<Episode>> episodeGens = episodeParser.parse(
	// // EpisodeType.GENERAL, frequency, foldNum);
	// // Set<Integer> frequencies = getFrequencies(episodePars);
	// //
	// // for (int freq : frequencies) {
	// // Map<Integer, Set<Episode>> patternSeqs = episodeFilter.filter(
	// // EpisodeType.SEQUENTIAL, episodeSeqs, freq, 0.0);
	// // Map<Integer, Set<Episode>> patternPars = episodeFilter.filter(
	// // EpisodeType.PARALLEL, episodePars, freq, 0.0);
	// // Map<Integer, Set<Episode>> patternGens = episodeFilter.filter(
	// // EpisodeType.GENERAL, episodeGens, freq, entropy);
	// //
	// // Tuple<Integer, Integer> withGens = getCoverage(patternSeqs,
	// // patternGens);
	// // Tuple<Integer, Integer> withPars = getCoverage(patternSeqs,
	// // patternPars);
	// //
	// // int numbPG = getNumbPatterns(patternGens);
	// // int numbPP = getNumbPatterns(patternPars);
	// //
	// // Logger.log("\t%d\t%d\t%.3f\t%d\t%.3f", freq, withGens.getSecond(),
	// // getPercentage(withGens.getSecond(), numbPG),
	// // withPars.getSecond(),
	// // getPercentage(withPars.getSecond(), numbPP));
	// // }
	// // }
	//
	// public void createHistogram(EpisodeType type, int foldNum, int frequency)
	// {
	// Map<Integer, Set<Episode>> episodes = episodeParser.read(frequency);
	// Set<Integer> frequencies = getFrequencies(episodes);
	// int maxFreq = getMax(frequencies);
	// int prevValue = 0;
	//
	// Logger.log("\tHistogram for %s-configuration:", type.toString());
	// Logger.log("\tFrequency\t#Patterns");
	// for (int freq = frequency; freq <= 1700; freq += 5) {
	// // for (int freq : frequencies) {
	// Map<Integer, Set<Episode>> patterns = episodeFilter.filter(type,
	// episodes, freq, THRESHENT);
	// int numbPatterns = getNumbPatterns(patterns);
	// // if (prevValue != numbPatterns) {
	// prevValue = numbPatterns;
	// Logger.log("\t%d\t%d", freq, numbPatterns);
	// // }
	// }
	// }
	//
	// private int getMax(Set<Integer> frequencies) {
	// int max = Integer.MIN_VALUE;
	// for (int freq : frequencies) {
	// if (freq > max) {
	// max = freq;
	// }
	// }
	// return max;
	// }
	//
	// private double getPercentage(Integer num1, int total) {
	// double percentage = (num1 * 1.0) / (total * 1.0);
	// return percentage;
	// }
	//
	// private int getNumbPatterns(Map<Integer, Set<Episode>> patternSeqs) {
	// int counter = 0;
	// for (Map.Entry<Integer, Set<Episode>> entry : patternSeqs.entrySet()) {
	// counter += entry.getValue().size();
	// }
	// return counter;
	// }
	//
	// private Set<Integer> getFrequencies(Map<Integer, Set<Episode>> episodes)
	// {
	// Set<Integer> results = Sets.newLinkedHashSet();
	//
	// for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
	// if (entry.getKey() == 1) {
	// continue;
	// }
	// for (Episode ep : entry.getValue()) {
	// int frequency = ep.getFrequency();
	// results.add(frequency);
	// }
	// }
	// return results;
	// }
	//
	// public void entropyAnalyzer(int foldNum, int frequency) {
	// // Logger.log("\tNumber of patterns not covered for frequency = %d",
	// // THRESHFREQ);
	// // Logger.log("\tEntropy\tSequentials\tParallels");
	// Logger.log("\tEntropy analyzer for frequency = %d", THRESHFREQ);
	// Logger.log("\tEntropy\t#Patterns");
	//
	// // Map<Integer, Set<Episode>> patternsSeqs = getPatterns(
	// // EpisodeType.SEQUENTIAL, foldNum, frequency);
	// // Map<Integer, Set<Episode>> patternsPars = getPatterns(
	// // EpisodeType.PARALLEL, foldNum, frequency);
	//
	// Map<Integer, Set<Episode>> episodeGens = episodeParser.read(frequency);
	//
	// for (double entropy = 0.0; entropy <= 1.0; entropy += 0.01) {
	// Map<Integer, Set<Episode>> patternGens = episodeFilter.filter(
	// EpisodeType.GENERAL, episodeGens, THRESHFREQ, entropy);
	// Logger.log("\t%.2f\t%d", entropy, getNumbPatterns(patternGens));
	//
	// // Tuple<Integer, Integer> withSeqs = getCoverage(patternGens,
	// // patternsSeqs);
	// // Tuple<Integer, Integer> withPars = getCoverage(patternGens,
	// // patternsPars);
	// // Logger.log("\t%.3f\t%d\t%d", entropy, withSeqs.getSecond(),
	// // withPars.getSecond());
	// }
	// }
	//
	// public void coverage(EpisodeType type1, EpisodeType type2, int foldNum,
	// int frequency) throws IOException {
	// Logger.log("Comparing %s with %s patterns ...", type1.toString(),
	// type2.toString());
	// Logger.log("Frequency threshold = %d", THRESHFREQ);
	// Logger.log("Entropy threshold = %.2f", THRESHENT);
	//
	// Map<Integer, Set<Episode>> patterns1 = getPatterns(type1, frequency);
	// Map<Integer, Set<Episode>> patterns2 = getPatterns(type2, frequency);
	//
	// Tuple<Integer, Integer> result = getCoverage(type1, type2, patterns1,
	// patterns2);
	// Logger.log(
	// "Configuration %s convers %d patterns from configuration %s",
	// type1, result.getFirst(), type2);
	// Logger.log(
	// "Configuration %s does not cover %d patterns from configuration %s",
	// type1, result.getSecond(), type2);
	// }
	//
	// public void compStats(EpisodeType type1, EpisodeType type2, int foldNum,
	// int frequency) {
	// Logger.log("Producing statistics for %s and %s-configurations ...",
	// type1.toString(), type2.toString());
	// Logger.log("Frequency threshold = %d", THRESHFREQ);
	// Logger.log("Entropy threshold = %.2f", THRESHENT);
	//
	// Map<Set<Fact>, Set<Episode>> set1 = patternsSetFact(getPatterns(type1,
	// frequency));
	// Map<Set<Fact>, Set<Episode>> set2 = patternsSetFact(getPatterns(type2,
	// frequency));
	// int numbEquals = 0;
	// int numbRepr = 0;
	// int numbNew = 0;
	//
	// for (Map.Entry<Set<Fact>, Set<Episode>> entry : set1.entrySet()) {
	// Set<Fact> events = entry.getKey();
	// Set<Episode> patterns1 = entry.getValue();
	// if (set2.containsKey(events)) {
	// Set<Episode> patterns2 = set2.get(events);
	//
	// for (Episode p1 : patterns1) {
	// boolean isCounted = false;
	// for (Episode p2 : patterns2) {
	// if (assertFactSets(p1.getFacts(), p2.getFacts())) {
	// numbEquals++;
	// isCounted = true;
	// break;
	// }
	// if (represents(p1, p2)) {
	// numbRepr++;
	// isCounted = true;
	// break;
	// }
	// }
	// if (!isCounted) {
	// numbNew++;
	// }
	// }
	// } else {
	// numbNew += patterns1.size();
	// }
	// }
	// Logger.log(
	// "Number of equal patterns between %s and %s-configurations: %d",
	// type1.toString(), type2.toString(), numbEquals);
	// Logger.log(
	// "%s-configurations has %d representative patterns for %s-configuration",
	// type1.toString(), numbRepr, type2.toString());
	// Logger.log("%s-configuration learns %d new patterns", type1.toString(),
	// numbNew);
	// }
	//
	// private Tuple<Integer, Integer> getCoverage(EpisodeType type1,
	// EpisodeType type2, Map<Integer, Set<Episode>> patterns1,
	// Map<Integer, Set<Episode>> patterns2) throws IOException {
	// Map<Set<Fact>, Set<Episode>> sets1 = patternsSetFact(patterns1);
	// Map<Set<Fact>, Set<Episode>> sets2 = patternsSetFact(patterns2);
	//
	// int covered = 0;
	// int notCovered = 0;
	// int patternId = 300;
	//
	// for (Map.Entry<Set<Fact>, Set<Episode>> entry : sets2.entrySet()) {
	// Set<Fact> events = entry.getKey();
	//
	// if (!sets1.containsKey(events)) {
	// Logger.log("Not covered events");
	// notCovered += entry.getValue().size();
	// for (Episode pattern : entry.getValue()) {
	// Logger.log("\t%s", pattern.getFacts().toString());
	// storeUniques(type2, 0, pattern, patternId);
	// patternId++;
	// }
	// continue;
	// } else {
	// for (Episode pattern : entry.getValue()) {
	// if (isCovered(pattern, sets1.get(events))) {
	// covered++;
	// } else {
	// notCovered++;
	// storeUniques(type2, 0, pattern, patternId);
	// patternId++;
	// Logger.log("Not covered order constraints");
	// Logger.log("\t%s", pattern.getFacts().toString());
	// }
	// }
	// }
	// }
	// return Tuple.newTuple(covered, notCovered);
	// }
	//
	// private void storeUniques(EpisodeType type, int foldNum, Episode pattern,
	// int patternId) throws IOException {
	// List<Event> events = eventStream.readMapping(300);
	//
	// Episode epGraph = transClosure.remTransClosure(pattern);
	// DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter.convert(
	// epGraph, events);
	//
	// graphWriter.write(graph,
	// getGraphPath(type, foldNum, THRESHFREQ, THRESHENT, patternId));
	// }
	//
	// private boolean isCovered(Episode pattern, Set<Episode> others) {
	// for (Episode o : others) {
	// if (represents(o, pattern)) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// private boolean represents(Episode other, Episode pattern) {
	// Set<Fact> relations = pattern.getRelations();
	// for (Fact rel : relations) {
	// Tuple<Fact, Fact> facts = rel.getRelationFacts();
	// Fact opp = new Fact(facts.getSecond() + ">" + facts.getFirst());
	// if (other.getRelations().contains(opp)) {
	// return false;
	// }
	// }
	// return true;
	// }
	//
	// private Map<Set<Fact>, Set<Episode>> patternsSetFact(
	// Map<Integer, Set<Episode>> patterns) {
	// Map<Set<Fact>, Set<Episode>> results = Maps.newLinkedHashMap();
	//
	// for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
	// for (Episode p : entry.getValue()) {
	// Set<Fact> events = p.getEvents();
	// if (results.containsKey(events)) {
	// results.get(events).add(p);
	// } else {
	// results.put(events, Sets.newHashSet(p));
	// }
	// }
	// }
	// return results;
	// }
	//
	public void overlappingPatterns(int frequency, int threshFreq,
			double threshEnt) throws Exception {
		int equals = 0;
		int notEquals = 0;
		int covered = 0;
		int notCovered = 0;

		Set<Episode> covSeqs = Sets.newLinkedHashSet();
		Map<Integer, Set<Episode>> partials = getPatterns(EpisodeType.GENERAL,
				frequency, threshFreq, threshEnt);
		Map<Integer, Set<Episode>> sequentials = getPatterns(
				EpisodeType.SEQUENTIAL, frequency, threshFreq, threshEnt);

		for (Map.Entry<Integer, Set<Episode>> entSeq : sequentials.entrySet()) {
			Map<Integer, Set<Episode>> tempPartials = Maps.newLinkedHashMap();
			for (Episode seqPat : entSeq.getValue()) {
				Set<Episode> partialPatt = partials.get(entSeq.getKey());
				boolean areEqual = false;
				for (Episode partPat : partialPatt) {
					areEqual = equalEpisodes(seqPat, partPat);
					if (areEqual) {
						equals++;
						break;
					}
				}
				if (!areEqual) {
					notEquals++;
					boolean isCovered = false;
					for (Episode partPat : partialPatt) {
						isCovered = doesCover(partPat, seqPat);
						if (isCovered) {
							int numRels = partPat.getRelations().size();
							if (tempPartials.containsKey(numRels)) {
								tempPartials.get(numRels).add(partPat);
							} else {
								tempPartials.put(numRels, Sets.newHashSet(partPat));
							}
							covered++;
							break;
						}
					}
					if (!isCovered) {
						notCovered++;
						Logger.log("Sequential: %s", seqPat.getFacts()
								.toString());
					}
				}
			}
			for (Map.Entry<Integer, Set<Episode>> tmpEntry : tempPartials.entrySet()) {
				covSeqs.addAll(tmpEntry.getValue());
			}
		}
		Logger.log("");
		Logger.log("Number of equal patterns: %d", equals);
		Logger.log("Number of not equal patterns: %d", notEquals);
		Logger.log("Partials covers %d from sequentials", covered);
		Logger.log("Partials do not cover %d from sequentials", notCovered);
		Logger.log(
				"Number of partial patterns for covering sequential patterns: %d",
				covSeqs.size());

		Set<Episode> adds = Sets.newLinkedHashSet();
		for (Map.Entry<Integer, Set<Episode>> entry : partials.entrySet()) {
			for (Episode pattern : entry.getValue()) {
				if (!covSeqs.contains(pattern) && isPartial(pattern)) {
					adds.add(pattern);
				}
			}
		}
		writePatterns(covSeqs, "coverSeqs", frequency, threshFreq, threshEnt);
		writePatterns(adds, "additionals", frequency, threshFreq, threshEnt);
	}

	private void writePatterns(Set<Episode> patterns, String folderName,
			int frequency, int threshFreq, double threshEnt) throws IOException {
		List<Event> events = eventStream.readMapping(frequency);
		int patternId = 0;
		for (Episode pattern : patterns) {
			Episode epGraph = transClosure.remTransClosure(pattern);
			DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter
					.convert(epGraph, events);
			graphWriter.write(graph,
					getFilepath(threshFreq, threshEnt, folderName, patternId));
			patternId++;
		}
	}

	private String getFilepath(int frequency, double entropy, String folderName,
			int patternId) {
		File filePath = new File(patternsFile.getAbsolutePath() + "/freq"
				+ frequency + "/entropy" + entropy + "/" + folderName);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		String fileName = filePath.getAbsolutePath() + "/pattern" + patternId
				+ ".dot";
		return fileName;
	}

	private boolean isPartial(Episode pattern) {
		int numRels = pattern.getRelations().size();
		if (numRels < maxRels(pattern.getNumEvents())) {
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

	private boolean doesCover(Episode partial, Episode sequential) {
		Set<Fact> partEvents = partial.getEvents();
		Set<Fact> seqEvents = sequential.getEvents();

		if (!partEvents.containsAll(seqEvents)) {
			return false;
		}
		Set<Fact> seqRels = sequential.getRelations();
		Set<Fact> partRels = partial.getRelations();
		for (Fact relation : seqRels) {
			Tuple<Fact, Fact> relFacts = relation.getRelationFacts();
			String factString = relFacts.getSecond().getFactID() + ">"
					+ relFacts.getFirst().getFactID();
			Fact fact = new Fact(factString);
			if (partRels.contains(fact)) {
				return false;
			}
		}
		return true;
	}

	private boolean equalEpisodes(Episode episode1, Episode episode2) {
		Set<Fact> facts1 = episode1.getFacts();
		Set<Fact> facts2 = episode2.getFacts();
		if (facts1.size() != facts2.size()) {
			return false;
		}
		if (facts1.containsAll(facts2) && facts2.containsAll(facts1)) {
			return true;
		} else {
			return false;
		}
	}

	// // public void nonoverlappings(EpisodeType type1, EpisodeType type2,
	// // int freqEpisode, int foldNum, int freqThresh, double entropy)
	// // throws IOException {
	// // int id = 0;
	// //
	// // Map<Integer, Map<Set<Fact>, Set<Episode>>> patterns1 =
	// patternsSetFact(
	// // type1, freqEpisode, foldNum, freqThresh, entropy);
	// // Map<Integer, Map<Set<Fact>, Set<Episode>>> patterns2 =
	// patternsSetFact(
	// // type2, freqEpisode, foldNum, freqThresh, entropy);
	// //
	// // for (Map.Entry<Integer, Map<Set<Fact>, Set<Episode>>> entry :
	// patterns2
	// // .entrySet()) {
	// // for (Map.Entry<Set<Fact>, Set<Episode>> entryPatterns : entry
	// // .getValue().entrySet()) {
	// // Set<Fact> events = entryPatterns.getKey();
	// // Set<Episode> episodes2 = entryPatterns.getValue();
	// // Map<Set<Fact>, Set<Episode>> pattMap1 = patterns1.get(entry
	// // .getKey());
	// //
	// // if (!pattMap1.containsKey(events)) {
	// // saveUniques(freqEpisode, episodes2, type2, id, foldNum);
	// // id++;
	// // }
	// // }
	// // }
	// // }
	//
	// public void extractOverlappingExamples(EpisodeType type1,
	// EpisodeType type2, int frequency, double entropy, int foldNum) {
	// // Map<Set<Fact>, Set<Episode>> patterns1 = getPatternsSet(type1,
	// // frequency, entropy, foldNum);
	// // Map<Set<Fact>, Set<Episode>> patterns2 = getPatternsSet(type2,
	// // frequency, entropy, foldNum);
	// //
	// // Map<Set<Fact>, Set<Episode>> overlaps = Maps.newLinkedHashMap();
	// //
	// // for (Map.Entry<Set<Fact>, Set<Episode>> entry : patterns1.entrySet())
	// // {
	// // Set<Fact> events = entry.getKey();
	// // if (patterns2.containsKey(events)) {
	// // boolean validCand = checkValidity(events, entry.getValue(),
	// // patterns2.get(events));
	// // if (validCand) {
	// // if (entry.getValue().size() > patterns2.get(events).size()) {
	// // overlaps.put(events, entry.getValue());
	// // Logger.log("General pattern: %s", patterns2.get(events)
	// // .toString());
	// // Logger.log("Sequential patterns: %s", entry.getValue()
	// // .toString());
	// // } else {
	// // overlaps.put(events, patterns2.get(events));
	// // Logger.log("General pattern: %s", entry.getValue()
	// // .toString());
	// // Logger.log("Sequential patterns: %s",
	// // patterns2.get(events).toString());
	// // }
	// // }
	// // }
	// // }
	// }
	//
	// public void extractConcreteCode(int frequency, int foldNum) {
	// List<Tuple<Event, List<Fact>>> stream = eventStream.parseStream(
	// frequency, foldNum);
	// Episode pattern = createEpisode(312, 0.985606, "46671", "46672");
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
	// if (numMethods > 500) {
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

	private Episode createEpisode(int frequency, double entropy,
			String... strings) {
		Episode episode = new Episode();

		episode.setFrequency(frequency);
		episode.setEntropy(entropy);
		episode.addStringsOfFacts(strings);

		return episode;
	}

	//
	// private boolean checkValidity(Set<Fact> events, Set<Episode> episodes1,
	// Set<Episode> episodes2) {
	// if ((events.size() < 3) || (events.size() > 5)) {
	// return false;
	// }
	//
	// if (validEpisode(episodes1) || validEpisode(episodes2)) {
	// return true;
	// }
	// return false;
	// }
	//
	// private boolean validEpisode(Set<Episode> episodes) {
	// if (episodes.size() == 1) {
	// for (Episode ep : episodes) {
	// if (ep.getRelations().size() == 0) {
	// return false;
	// } else {
	// return true;
	// }
	// }
	// }
	// return false;
	// }

	// private void store(int frequency, Set<Episode> episodes, EpisodeType
	// type,
	// int setNum, int foldNum) throws IOException {
	// List<Event> events = eventStream.readMapping(frequency);
	// int episodeId = 0;
	//
	// for (Episode ep : episodes) {
	// Episode epGraph = transClosure.remTransClosure(ep);
	// DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter
	// .convert(epGraph, events);
	// graphWriter.write(graph,
	// getGraphPath(frequency, type, setNum, episodeId));
	// episodeId++;
	// }
	// }
	//
	// private void saveUniques(int freq, Set<Episode> episodes, EpisodeType
	// type,
	// int id, int foldNum) throws IOException {
	// List<Event> events = eventStream.readMapping(freq);
	//
	// for (Episode ep : episodes) {
	// Episode epGraph = transClosure.remTransClosure(ep);
	// DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter
	// .convert(epGraph, events);
	// graphWriter.write(graph, getGraphPath(freq, type, -1, id));
	// }
	// }
	//
	// private String getGraphPath(int frequency, EpisodeType type, int setNum,
	// int episodeId) {
	// String fileName = getSetPath(frequency, type, setNum) + "/graph"
	// + episodeId + ".dot";
	// return fileName;
	// }
	//
	// private String getSetPath(int freq, EpisodeType type, int setNum) {
	// File path = new File(patternsFile.getAbsolutePath() + "/freq"
	// + THRESHFREQ + "/entropy" + THRESHENT + "/" + type.toString()
	// + "/set" + setNum);
	// if (!path.exists()) {
	// path.mkdirs();
	// }
	// return path.getAbsolutePath();
	// }

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

	private Map<Integer, Set<Episode>> getPatterns(EpisodeType type,
			int frequency, int threshFreq, double threshEnt) throws Exception {
		Map<Integer, Set<Episode>> episodes = episodeParser.parser(frequency);
		return episodeFilter.filter(type, episodes, threshFreq, threshEnt);
	}
}
