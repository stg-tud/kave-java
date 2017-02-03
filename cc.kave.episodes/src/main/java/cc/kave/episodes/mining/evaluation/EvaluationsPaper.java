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
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class EvaluationsPaper {

	private File eventsFile;

	private EventStreamIo eventStreamIo;
	private EpisodesParser episodeParser;
	private EpisodesFilter episodeFilter;

	private TransClosedEpisodes closedEpisode;
	private EpisodeToGraphConverter graphConverter;
	private EpisodeAsGraphWriter graphWriter;

	private static final double ENTROPY = 0.0;

	@Inject
	public EvaluationsPaper(@Named("events") File folder,
			EventStreamIo streamIo, EpisodesParser parser,
			EpisodesFilter filter, TransClosedEpisodes closedEp,
			EpisodeToGraphConverter graphConverter,
			EpisodeAsGraphWriter graphWriter) {
		assertTrue(folder.exists(), "Events folder does not exists!");
		assertTrue(folder.isDirectory(), "Events is not a folder, but a file!");
		this.eventsFile = folder;
		this.eventStreamIo = streamIo;
		this.episodeParser = parser;
		this.episodeFilter = filter;
		this.closedEpisode = closedEp;
		this.graphConverter = graphConverter;
		this.graphWriter = graphWriter;
	}

	public void part1(int frequency) throws IOException {
		List<Event> events = eventStreamIo
				.readMapping(frequency);

		Map<Integer, Set<Episode>> genEpisodes = episodeParser.parse(frequency,
				EpisodeType.GENERAL);
		Map<Integer, Set<Episode>> genPatterns = episodeFilter.filter(
				genEpisodes, frequency, ENTROPY);
		
		Set<Set<Fact>> genEvents = getEpisodeEvents(genPatterns);
//		Logger.log("Number of learned patterns is %d", genEvents.size());
//
		Map<Integer, Set<Episode>> seqPatterns = episodeParser.parse(frequency,
				EpisodeType.PARALLEL);
		Set<Set<Fact>> seqEvents = getEpisodeEvents(seqPatterns);

		Set<Episode> genNoSeq = getNotOverlaps(genPatterns, seqEvents);
		Set<Episode> seqNoGen = getNotOverlaps(seqPatterns, genEvents);

		storePatternsGraph(genNoSeq, frequency, events, EpisodeType.GENERAL);
		storePatternsGraph(seqNoGen, frequency, events, EpisodeType.PARALLEL);
	}

	private void storePatternsGraph(Set<Episode> nonOverlapsPatterns,
			int frequency, List<Event> events, EpisodeType type) throws IOException {
		if (nonOverlapsPatterns.size() > 0) {
			int genNo = 0;

			for (Episode episode : nonOverlapsPatterns) {
				DirectedGraph<Fact, DefaultEdge> genGraph = graphConverter
						.convert(closedEpisode.remTransClosure(episode), events);
				graphWriter.write(genGraph,
						getGraphPath(frequency, type, genNo));
				genNo++;
			}
		}
	}

	private Set<Episode> getNotOverlaps(Map<Integer, Set<Episode>> patterns,
			Set<Set<Fact>> events) {
		Set<Episode> results = Sets.newLinkedHashSet();

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			if (entry.getKey() < 2) {
				continue;
			}
			for (Episode episode : entry.getValue()) {
				if (!events.contains(episode.getEvents())) {
					results.add(episode);
				}
			}
		}
		return results;
	}

	private Set<Set<Fact>> getEpisodeEvents(Map<Integer, Set<Episode>> patterns) {
		Set<Set<Fact>> episodeEvents = Sets.newLinkedHashSet();

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			if (entry.getKey() < 2) {
				continue;
			}
			for (Episode episode : entry.getValue()) {
				episodeEvents.add(episode.getEvents());
			}
		}
		return episodeEvents;
	}

	public void part2(int frequency) {
		List<List<Fact>> stream = eventStreamIo
				.parseStream(frequency);
		List<Event> enclMethods = eventStreamIo
				.readMethods(frequency);

//		Episode genPattern = createEpisode("7", "8", "34", "7>8");
//		EnclosingMethods genPatternMethods = new EnclosingMethods(true);
//
//		Episode seqPattern1 = createEpisode("7", "8", "34", "7>8", "7>34",
//				"8>34");
//		EnclosingMethods seqPatternMethods1 = new EnclosingMethods(true);
//
//		Episode seqPattern2 = createEpisode("7", "8", "34", "34>7", "34>8",
//				"7>8");
//		EnclosingMethods seqPatternMethods2 = new EnclosingMethods(true);
//
//		Episode seqPattern3 = createEpisode("7", "8", "34", "7>8", "7>34",
//				"34>8");
//		EnclosingMethods seqPatternMethods3 = new EnclosingMethods(true);
		
		Episode pattern1 = createEpisode(846, 0.950654, "70", "71", "672", "64",
				"70>71", "70>672", "70>64", "71>64", "672>64");
		EnclosingMethods genPatternMethods = new EnclosingMethods(true);

		for (int i = 0; i < stream.size(); i++) {

			List<Fact> method = stream.get(i);
			if (method.size() < 2) {
				continue;
			}
			if (method.containsAll(pattern1.getEvents())) {
				genPatternMethods.addMethod(pattern1, method,
						enclMethods.get(i));
			}
//			if (method.containsAll(pattern2.getEvents())) {
//				seqPatternMethods1.addMethod(pattern2, method,
//						enclMethods.get(i));
//			}
//			if (method.containsAll(seqPattern2.getEvents())) {
//				seqPatternMethods2.addMethod(seqPattern2, method,
//						enclMethods.get(i));
//			}
//			if (method.containsAll(seqPattern3.getEvents())) {
//				seqPatternMethods3.addMethod(seqPattern3, method,
//						enclMethods.get(i));
//			}
			int occ0 = genPatternMethods.getOccurrences();
//			int occ1 = seqPatternMethods1.getOccurrences();
//			int occ2 = seqPatternMethods2.getOccurrences();
//			int occ3 = seqPatternMethods3.getOccurrences();

			if (occ0 >= 10) {
				break;
			}
		}
		Set<IMethodName> genMethod = genPatternMethods.getMethodNames(10);
		for (IMethodName method : genMethod) {
			Logger.log("General pattern occurrences: %s", method
					.getDeclaringType().getFullName() + "." + method.getName());
		}
//		Set<IMethodName> seqMethod1 = seqPatternMethods1.getMethodNames(3);
//		for (IMethodName method : seqMethod1) {
//			Logger.log("Sequential pattern1 occurrences: %s", method
//					.getDeclaringType().getFullName() + "." + method.getName());
//		}
//		Set<IMethodName> seqMethod2 = seqPatternMethods2.getMethodNames(3);
//		for (IMethodName method : seqMethod2) {
//			Logger.log("Sequential pattern2 occurrences: %s", method
//					.getDeclaringType().getFullName() + "." + method.getName());
//		}
//		Set<IMethodName> seqMethod3 = seqPatternMethods3.getMethodNames(3);
//		for (IMethodName method : seqMethod3) {
//			Logger.log("Sequential pattern3 occurrences: %s", method
//					.getDeclaringType().getFullName() + "." + method.getName());
//		}
	}

	private String getMethodsPath(int frequency) {
		String fileName = eventsFile.getAbsolutePath() + "/freq" + frequency
				+ "/TrainingData/fold0/methods.txt";
		return fileName;
	}

	private String getStreamPath(int frequency) {
		String fileName = eventsFile.getAbsolutePath() + "/freq" + frequency
				+ "/TrainingData/fold0/stream.txt";
		return fileName;
	}

	private Episode createEpisode(int frequency, double entropy, String... strings) {
		Episode episode = new Episode();
		
		episode.setFrequency(frequency);
		episode.setEntropy(entropy);
		episode.addStringsOfFacts(strings);
		return episode;
	}

	public void diff(int frequency) throws IOException {
		List<Event> events = eventStreamIo
				.readMapping(frequency);

		Map<Integer, Set<Episode>> genEpisodes = episodeParser.parse(frequency,
				EpisodeType.GENERAL);
		Map<Integer, Set<Episode>> genPatterns = episodeFilter.filter(
				genEpisodes, frequency, ENTROPY);
		// printInfo(genPatterns, EpisodeType.GENERAL);
		// getPatternsGroups(genPatterns, EpisodeType.GENERAL);

		Map<Integer, Set<Episode>> seqPatterns = episodeParser.parse(frequency,
				EpisodeType.SEQUENTIAL);
		// printInfo(seqPatterns, EpisodeType.SEQUENTIAL);
		Map<Set<Fact>, Set<Episode>> seqExamples = getPatternsGroups(
				seqPatterns, EpisodeType.SEQUENTIAL);

		Set<Episode> generals = genPatterns.get(3);
		int genNo = 0;
		for (Episode episode : generals) {
			if (seqExamples.containsKey(episode.getEvents())
					&& (episode.getEntropy() < 1.0)) {
				// Logger.log("General Episode: %s", episode.toString());
				// Logger.log("Sequential episode: %s", tuple.getSecond()
				// .toString());

				Logger.log("Generating partial episode graph ...");
				DirectedGraph<Fact, DefaultEdge> genGraph = graphConverter
						.convert(closedEpisode.remTransClosure(episode), events);
				graphWriter.write(genGraph,
						getGraphPath(frequency, EpisodeType.GENERAL, genNo));
				int seqNo = genNo * 10;
				genNo++;

				Logger.log("Generating sequential episode graph ...");
				for (Episode seqEpisode : seqExamples.get(episode.getEvents())) {
					DirectedGraph<Fact, DefaultEdge> seqGraph = graphConverter
							.convert(closedEpisode.remTransClosure(seqEpisode),
									events);
					graphWriter.write(
							seqGraph,
							getGraphPath(frequency, EpisodeType.SEQUENTIAL,
									seqNo));
					seqNo++;
				}
			}
		}

		// Map<Integer, Set<Episode>> parPatterns =
		// episodeParser.parse(frequency,
		// EpisodeType.PARALLEL);
		// printInfo(parPatterns, EpisodeType.PARALLEL);
	}

	private String getGraphPath(int frequency, EpisodeType type, int no) {
		String fileName = eventsFile.getAbsolutePath() + "/freq" + frequency
				+ "/TrainingData/fold0/patterns/" + type.toString() + "Graph"
				+ no + ".dot";
		return fileName;
	}

	private String getMappingFile(int frequency) {
		String fileName = eventsFile.getAbsolutePath() + "/freq" + frequency
				+ "/TrainingData/fold0/mapping.txt";
		return fileName;
	}

	private Map<Set<Fact>, Set<Episode>> getPatternsGroups(
			Map<Integer, Set<Episode>> genPatterns, EpisodeType type) {
		// Logger.log("Printing information for %s patterns", type.toString());
		//
		// Logger.log("Nodes\tMaxNoPatterns");
		Map<Set<Fact>, Set<Episode>> results = Maps.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entry : genPatterns.entrySet()) {

			if (entry.getKey() != 3) {
				continue;
			}
			Map<Set<Fact>, Set<Episode>> groups = Maps.newHashMap();
			// int maxPatterns = 1;

			for (Episode pattern : entry.getValue()) {
				Set<Fact> patternEvents = pattern.getEvents();

				if (groups.containsKey(patternEvents)) {
					groups.get(patternEvents).add(pattern);

					// if (groups.get(patternEvents).size() > maxPatterns) {
					// maxPatterns = groups.get(patternEvents).size();
					// }
				} else {
					groups.put(pattern.getEvents(), Sets.newHashSet(pattern));
				}
			}

			for (Map.Entry<Set<Fact>, Set<Episode>> entryGroup : groups
					.entrySet()) {
				if (entryGroup.getValue().size() > 1) {
					results.put(entryGroup.getKey(), entryGroup.getValue());
				}
			}
			// Logger.log("%d\t%d", entry.getKey(), maxPatterns);
		}
		return results;
	}

	private void printInfo(Map<Integer, Set<Episode>> genPatterns,
			EpisodeType type) {
		Logger.log("Printing information for %s patterns", type.toString());

		Logger.log("Nodes\tNo patterns");
		for (Map.Entry<Integer, Set<Episode>> entry : genPatterns.entrySet()) {
			Logger.log("%d\t%d", entry.getKey(), entry.getValue().size());
		}

	}
}
