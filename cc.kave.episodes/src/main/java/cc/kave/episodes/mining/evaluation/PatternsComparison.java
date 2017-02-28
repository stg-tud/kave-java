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

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EnclosingMethods;
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

	public void coverage(EpisodeType type1, EpisodeType type2, int frequency,
			double entropy, int foldNum) {
		Logger.log("Comparing %s with %s patterns ...", type1.toString(),
				type2.toString());

		Map<Set<Fact>, Set<Episode>> patterns1 = getPatternsSet(type1,
				frequency, entropy, foldNum);
		Map<Set<Fact>, Set<Episode>> patterns2 = getPatternsSet(type2,
				frequency, entropy, foldNum);

		checkCoverage(patterns1, patterns2, type1, type2);
	}

	public void commonPatterns(EpisodeType type1, EpisodeType type2,
			int frequency, double entropy, int foldNum) throws IOException {
		int equal = 0;
		int unequal = 0;
		int set = 0;

		Map<Set<Fact>, Set<Episode>> patterns1 = getPatternsSet(type1,
				frequency, entropy, foldNum);
		Map<Set<Fact>, Set<Episode>> patterns2 = getPatternsSet(type2,
				frequency, entropy, foldNum);

		for (Map.Entry<Set<Fact>, Set<Episode>> entry : patterns2.entrySet()) {
			Set<Fact> events = entry.getKey();
			Set<Episode> episodes2 = entry.getValue();

			if (patterns1.containsKey(events)) {
				if (!equalEpisodes(episodes2, patterns1.get(events))) {
					store(frequency, patterns1.get(events), type1, set, foldNum);
					store(frequency, episodes2, type2, set, foldNum);
					unequal++;
					set++;
				} else {
					equal++;
				}
			}
		}
		Logger.log(
				"Configurations %s and %s have %d equal patterns, and %d different representations of patterns.",
				type1.toString(), type2.toString(), equal, unequal);
	}

	public void extractOverlappingExamples(EpisodeType type1,
			EpisodeType type2, int frequency, double entropy, int foldNum) {
		Map<Set<Fact>, Set<Episode>> patterns1 = getPatternsSet(type1,
				frequency, entropy, foldNum);
		Map<Set<Fact>, Set<Episode>> patterns2 = getPatternsSet(type2,
				frequency, entropy, foldNum);

		Map<Set<Fact>, Set<Episode>> overlaps = Maps.newLinkedHashMap();

		for (Map.Entry<Set<Fact>, Set<Episode>> entry : patterns1.entrySet()) {
			Set<Fact> events = entry.getKey();
			if (patterns2.containsKey(events)) {
				boolean validCand = checkValidity(events, entry.getValue(),
						patterns2.get(events));
				if (validCand) {
					if (entry.getValue().size() > patterns2.get(events).size()) {
						overlaps.put(events, entry.getValue());
						Logger.log("General pattern: %s", patterns2.get(events)
								.toString());
						Logger.log("Sequential patterns: %s", entry.getValue()
								.toString());
					} else {
						overlaps.put(events, patterns2.get(events));
						Logger.log("General pattern: %s", entry.getValue()
								.toString());
						Logger.log("Sequential patterns: %s",
								patterns2.get(events).toString());
					}
				}
			}
		}
	}

	public void extractConcreteCode(int frequency) {
		List<Tuple<Event, List<Fact>>> stream = eventStream.parseStream(
				frequency, 0);

		// List<Tuple<List<Fact>, Event>> stream = eventStream
		// .parseEventStream(frequency);
		Episode pattern = getPattern(643, 0.608086, "118", "121", "564");

		EnclosingMethods enclMethods = new EnclosingMethods(true);
		int numMethods = 0;
		// for (Tuple<List<Fact>, Event> tuple : stream) {
		for (Tuple<Event, List<Fact>> tuple : stream) {
			// List<Fact> method = tuple.getFirst();
			List<Fact> method = tuple.getSecond();
			// Event methodCtx = tuple.getSecond();

			if (method.size() < 2) {
				continue;
			}
			if (method.containsAll(pattern.getEvents())) {
				enclMethods.addMethod(pattern, method, tuple.getFirst());
				numMethods = enclMethods.getOccurrences();
			}
			if (numMethods > 10) {
				break;
			}
		}
		Set<IMethodName> methodNames = enclMethods.getMethodNames(numMethods);

		for (IMethodName eventName : methodNames) {
			Logger.log("General pattern occurrences: %s",
					eventName.getDeclaringType().getFullName() + "."
							+ eventName.getName());
		}
	}

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

	private Episode getPattern(int frequency, double entropy, String... strings) {
		Episode episode = new Episode();

		episode.setFrequency(frequency);
		episode.setEntropy(entropy);
		episode.addStringsOfFacts(strings);

		return episode;
	}

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

	private int checkCoverage(Map<Set<Fact>, Set<Episode>> patterns1,
			Map<Set<Fact>, Set<Episode>> patterns2, EpisodeType type1,
			EpisodeType type2) {
		int covered = 0;
		int notConvered = 0;

		for (Map.Entry<Set<Fact>, Set<Episode>> entry : patterns2.entrySet()) {
			if (patterns1.containsKey(entry.getKey())) {
				covered += entry.getValue().size();
			} else {
				notConvered += entry.getValue().size();
			}
		}
		Logger.log(
				"Configuration %s coveres %d patterns from configuration %s!",
				type1.toString(), covered, type2.toString());
		Logger.log(
				"Configuration %s does not cover %d patterns from configuration %s!",
				type1.toString(), notConvered, type2.toString());
		return covered;
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

	private Map<Set<Fact>, Set<Episode>> getPatternsSet(EpisodeType type,
			int frequency, double entropy, int foldNum) {
		Map<Integer, Set<Episode>> patterns = getPatterns(type, frequency,
				entropy, foldNum);

		Map<Set<Fact>, Set<Episode>> results = Maps.newLinkedHashMap();
		int numPatterns = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			if (entry.getKey() < 2) {
				continue;
			}
			numPatterns += entry.getValue().size();

			for (Episode episode : entry.getValue()) {
				Set<Fact> episodeEvents = episode.getEvents();

				if (results.containsKey(episodeEvents)) {
					results.get(episodeEvents).add(episode);
				} else {
					results.put(episodeEvents, Sets.newHashSet(episode));
				}
			}
		}
		Logger.log("Configuration %s learns %d patterns!", type.toString(),
				numPatterns);
		return results;
	}

	private Map<Integer, Set<Episode>> getPatterns(EpisodeType type,
			int frequency, double entropy, int foldNum) {
		Map<Integer, Set<Episode>> episodes = episodeParser.parse(type,
				frequency, foldNum);

		if (type == EpisodeType.GENERAL) {
			return episodeFilter.filter(episodes, frequency, entropy);
		}
		return episodes;
	}
}
