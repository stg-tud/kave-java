package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.data.ContextsParser;
import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.mining.patterns.PatternFilter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EnclosingMethods;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.name.Named;

public class Generalizability {

	private File patternFolder;

	private ContextsParser cxtParser;
	private EventStreamIo streamIo;

	private EpisodeParser episodeParser;
	private PatternFilter patternFilter;

	private TransClosedEpisodes transClosure;
	private EpisodeToGraphConverter graphConverter;
	private EpisodeAsGraphWriter graphWriter;

	@Inject
	public Generalizability(@Named("patterns") File folder,
			ContextsParser ctxParser, EpisodeParser epParser,
			PatternFilter pattFilter, EventStreamIo eventsIo,
			TransClosedEpisodes transClosure,
			EpisodeToGraphConverter graphConverter,
			EpisodeAsGraphWriter graphWriter) {
		assertTrue(folder.exists(), "Patterns folder does not exist!");
		assertTrue(folder.isDirectory(),
				"Patterns is not a folder, but a file!");
		this.patternFolder = folder;
		this.cxtParser = ctxParser;
		this.episodeParser = epParser;
		this.patternFilter = pattFilter;
		this.streamIo = eventsIo;
		this.transClosure = transClosure;
		this.graphConverter = graphConverter;
		this.graphWriter = graphWriter;
	}

	public void validate(int frequency, int threshFreq, double threshEntropy)
			throws Exception {
		List<Tuple<Event, List<Event>>> stream = cxtParser.parse(frequency);

		Logger.log("Reading event's list ...");
		List<Event> events = streamIo.readMapping(frequency);
		Logger.log("Converting to map of events ...");
		Map<Event, Integer> eventsMap = mapConverter(events);

		Logger.log("Converting to stream of facts ...");
		List<Tuple<Event, List<Fact>>> streamOfFacts = convertStreamOfFacts(
				stream, eventsMap);
		stream.clear();

		Logger.log("Parsing list of episodes ...");
		Map<Integer, Set<Episode>> episodes = episodeParser.parser(frequency);

		checkGeneralizability(EpisodeType.GENERAL, threshFreq, threshEntropy,
				events, streamOfFacts, episodes);
		checkGeneralizability(EpisodeType.PARALLEL, threshFreq, threshEntropy,
				events, streamOfFacts, episodes);
		checkGeneralizability(EpisodeType.SEQUENTIAL, threshFreq,
				threshEntropy, events, streamOfFacts, episodes);
	}

	private Map<Event, Integer> mapConverter(List<Event> events) {
		Map<Event, Integer> map = Maps.newLinkedHashMap();
		int id = 0;

		for (Event event : events) {
			map.put(event, id);
			id++;
		}
		return map;
	}

	private void checkGeneralizability(EpisodeType type, int threshFreq,
			double threshEntropy, List<Event> events,
			List<Tuple<Event, List<Fact>>> streamOfFacts,
			Map<Integer, Set<Episode>> episodes) throws Exception {
		Map<String, Set<IMethodName>> repos = cxtParser.getRepoCtxMapper();
		Map<Integer, Set<Episode>> patterns = patternFilter.filter(type,
				episodes, threshFreq, threshEntropy);

		StringBuilder sb = new StringBuilder();
		int patternId = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {

//			if ((type == EpisodeType.SEQUENTIAL)
//					|| (type == EpisodeType.PARALLEL)) {
//				if (entry.getKey() < 6) {
//					continue;
//				}
//				if (entry.getKey() == 6) {
//					if (type == EpisodeType.SEQUENTIAL) {
//						patternId = 1200;
//					}
//					if (type == EpisodeType.PARALLEL) {
//						patternId = 948;
//					}
//				}
//			} else {
//				if (entry.getKey() == 2) {
//					continue;
//				}
//				if (entry.getKey() == 3) {
//					patternId = 390;
//				}
//			}
			Logger.log(
					"Cheking generalizability for %s-order configuration...",
					type.toString());
			Logger.log("Outputing episodes with %d-nodes ...", entry.getKey());
			sb.append("Patterns with " + entry.getKey() + "-nodes:\n");
			sb.append("PatternId\tFacts\tFrequency\tEntropy\t#Repos\n");

			for (Episode pattern : entry.getValue()) {
				int numReposOccurs = getRepoOccs(pattern, streamOfFacts, repos);

				sb.append(patternId + "\t");
				sb.append(pattern.getFacts().toString() + "\t");
				sb.append(pattern.getFrequency() + "\t");
				sb.append(pattern.getEntropy() + "\t");
				sb.append(numReposOccurs + "\n");

				store(pattern, type, patternId, events, threshFreq,
						threshEntropy);
				patternId++;
			}
			sb.append("\n");
		}
		FileUtils.writeStringToFile(
				getEvalPath(threshFreq, threshEntropy, type), sb.toString());

	}

	private List<Tuple<Event, List<Fact>>> convertStreamOfFacts(
			List<Tuple<Event, List<Event>>> stream, Map<Event, Integer> events) {
		List<Tuple<Event, List<Fact>>> results = Lists.newLinkedList();

		for (Tuple<Event, List<Event>> tuple : stream) {
			List<Fact> facts = Lists.newLinkedList();

			for (Event event : tuple.getSecond()) {
				int id = events.get(event);
				facts.add(new Fact(id));
			}
			results.add(Tuple.newTuple(tuple.getFirst(), facts));
		}
		return results;
	}

	private int getRepoOccs(Episode pattern,
			List<Tuple<Event, List<Fact>>> stream,
			Map<String, Set<IMethodName>> repoCtxMapper) {

		EnclosingMethods methodsOrderRelation = new EnclosingMethods(true);

		for (Tuple<Event, List<Fact>> tuple : stream) {
			List<Fact> method = tuple.getSecond();
			if (method.size() < 2) {
				continue;
			}
			if (method.containsAll(pattern.getEvents())) {
				Event ctx = tuple.getFirst();
				methodsOrderRelation.addMethod(pattern, method, ctx);
			}
		}
		int numOccs = methodsOrderRelation.getOccurrences();
		assertTrue(numOccs >= pattern.getFrequency(),
				"Found insufficient number of occurences!");

		Set<IMethodName> methodOcc = methodsOrderRelation
				.getMethodNames(numOccs);
		Set<String> repositories = Sets.newLinkedHashSet();

		for (Map.Entry<String, Set<IMethodName>> entry : repoCtxMapper
				.entrySet()) {
			for (IMethodName methodName : entry.getValue()) {
				if (methodOcc.contains(methodName)) {
					repositories.add(entry.getKey());
					break;
				}
			}
		}
		return repositories.size();
	}

	private void store(Episode episode, EpisodeType type, int patternId,
			List<Event> events, int frequency, double entropy)
			throws IOException {
		Episode pattern = transClosure.remTransClosure(episode);
		DirectedGraph<Fact, DefaultEdge> graph = graphConverter.convert(
				pattern, events);
		graphWriter.write(graph,
				getGraphPath(frequency, entropy, type, patternId));
	}

	private String getGraphPath(int frequency, double entropy,
			EpisodeType type, int patternId) {
		String fileName = getPatternsPath(frequency, entropy, type)
				+ "/pattern" + patternId + ".dot";
		return fileName;
	}

	private String getPatternsPath(int frequency, double entropy,
			EpisodeType type) {
		File path = new File(getResultPath(frequency, entropy, type)
				+ "/allPatterns");
		if (!path.exists()) {
			path.mkdirs();
		}
		return path.getAbsolutePath();
	}

	private String getResultPath(int frequency, double entropy, EpisodeType type) {
		File path = new File(patternFolder.getAbsolutePath() + "/freq"
				+ frequency + "/entropy" + entropy + "/" + type.toString());
		if (!path.exists()) {
			path.mkdirs();
		}
		return path.getAbsolutePath();
	}

	private File getEvalPath(int frequency, double entropy,
			EpisodeType episodeType) {
		File fileName = new File(getResultPath(frequency, entropy, episodeType)
				+ "/evaluations.txt");
		return fileName;
	}
}