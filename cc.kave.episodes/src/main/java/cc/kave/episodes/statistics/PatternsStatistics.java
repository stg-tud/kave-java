package cc.kave.episodes.statistics;

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

public class PatternsStatistics {

	private File patternsFile;
	
	private EventStreamIo streamIo;

	private EpisodeParser parser;
	private PatternFilter filter;

	private TransClosedEpisodes transClosure;
	private EpisodeToGraphConverter episodeGraphConverter;

	private EpisodeAsGraphWriter graphWriter;

	@Inject
	public PatternsStatistics(@Named("patterns") File folder, EventStreamIo eventsStreamIo,
			EpisodeParser episodeReader, PatternFilter patternFilter,
			TransClosedEpisodes transClosure,
			EpisodeToGraphConverter graphConverter,
			EpisodeAsGraphWriter graphWriter) {
		assertTrue(folder.exists(), "Patterns folder does not exist");
		assertTrue(folder.isDirectory(), "Patterns is not a folder, but a file");
		this.patternsFile = folder;
		this.streamIo = eventsStreamIo;
		this.parser = episodeReader;
		this.filter = patternFilter;
		this.transClosure = transClosure;
		this.episodeGraphConverter = graphConverter;
		this.graphWriter = graphWriter;
	}

	public void numPatterns(int frequency, int threshFreq, double threshEnt)
			throws Exception {
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);

		Logger.log("\tPartial-order configuration:");
		Map<Integer, Set<Episode>> partPatterns = filter.filter(
				EpisodeType.GENERAL, episodes, threshFreq, threshEnt);
		outputStats(partPatterns);

		Logger.log("\tSequential-order configuration:");
		Map<Integer, Set<Episode>> seqPatterns = filter.filter(
				EpisodeType.SEQUENTIAL, episodes, threshFreq, threshEnt);
		outputStats(seqPatterns);

		Logger.log("\tNo-order configuration:");
		Map<Integer, Set<Episode>> paraPatterns = filter.filter(
				EpisodeType.PARALLEL, episodes, threshFreq, threshEnt);
		outputStats(paraPatterns);
	}

	public void partialSequentials(int frequency, int threshFreq,
			double threshEntr) throws Exception {
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);

		Map<Integer, Set<Episode>> sequentials = filter.filter(
				EpisodeType.SEQUENTIAL, episodes, threshFreq, threshEntr);
		int counterSeq = patternsCounter(sequentials);
		Map<Set<Fact>, Set<Episode>> groups = Maps.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entry : sequentials.entrySet()) {
			for (Episode pattern : entry.getValue()) {
				Set<Fact> events = pattern.getEvents();

				if (groups.containsKey(events)) {
					groups.get(events).add(pattern);
				} else {
					groups.put(events, Sets.newHashSet(pattern));
				}
			}
		}
		Map<Integer, Set<Episode>> partials = filter.filter(
				EpisodeType.GENERAL, episodes, threshFreq, threshEntr);
		int counterPart = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : partials.entrySet()) {
			for (Episode pattern : entry.getValue()) {
				Set<Fact> events = pattern.getEvents();

				if (groups.containsKey(events)) {
					if (isPartial(pattern)) {
						Logger.log("%s", pattern.getFacts().toString());
					} else {
						counterPart++;
					}
				}
			}
		}
		Logger.log("Sequential-order patterns: %d", counterSeq);
		Logger.log("Partial-order patterns with strict-order: %d", counterPart);
	}

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
								tempPartials.put(numRels,
										Sets.newHashSet(partPat));
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
			for (Map.Entry<Integer, Set<Episode>> tmpEntry : tempPartials
					.entrySet()) {
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
		List<Event> events = streamIo.readMapping(frequency);
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

	private Map<Integer, Set<Episode>> getPatterns(EpisodeType type,
			int frequency, int threshFreq, double threshEnt) throws Exception {
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);
		return filter.filter(type, episodes, threshFreq, threshEnt);
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
