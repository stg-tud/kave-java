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

public class GeneralizabilityV2 {

	private File patternFolder;

	private ContextsParser cxtParser;
	private EventStreamIo streamIo;

	private EpisodeParser episodeParser;
	private PatternFilter patternFilter;

	private TransClosedEpisodes transClosure;
	private EpisodeToGraphConverter graphConverter;
	private EpisodeAsGraphWriter graphWriter;

	@Inject
	public GeneralizabilityV2(@Named("patterns") File folder,
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

	Map<Episode, EnclosingMethods> seqMethods = Maps.newLinkedHashMap();
	Map<Episode, EnclosingMethods> paraMethods = Maps.newLinkedHashMap();
	Map<Episode, EnclosingMethods> partMethods = Maps.newLinkedHashMap();

	public void validate(int frequency, int threshFreq, double threshEntropy)
			throws Exception {
		List<Tuple<Event, List<Event>>> stream = cxtParser.parse(frequency);
		Map<String, Set<IMethodName>> repos = cxtParser.getRepoCtxMapper();

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

		Map<Integer, Set<Episode>> seqPatterns = patternFilter.filter(episodes,
				threshFreq, threshEntropy);
		initializer(seqPatterns, seqMethods);

		Map<Integer, Set<Episode>> paralPatterns = patternFilter.filter(
				episodes, threshFreq, threshEntropy);
		initializer(paralPatterns, paraMethods);

		Map<Integer, Set<Episode>> partPatterns = patternFilter.filter(
				episodes, threshFreq, threshEntropy);
		initializer(partPatterns, partMethods);

		int numMethods = 0;

		for (Tuple<Event, List<Fact>> tuple : streamOfFacts) {
			checkGeneralizability(seqMethods, tuple.getFirst(),
					tuple.getSecond());
			checkGeneralizability(paraMethods, tuple.getFirst(),
					tuple.getSecond());
			checkGeneralizability(partMethods, tuple.getFirst(),
					tuple.getSecond());
			numMethods++;

			if (numMethods % 100 == 0) {
				Logger.log("Processed %d / %d", numMethods,
						streamOfFacts.size());
			}
		}
		outputResults(seqMethods, repos, events, EpisodeType.SEQUENTIAL,
				threshFreq, threshEntropy);
		outputResults(paraMethods, repos, events, EpisodeType.PARALLEL,
				threshFreq, threshEntropy);
		outputResults(partMethods, repos, events, EpisodeType.GENERAL,
				threshFreq, threshEntropy);
	}

	private void outputResults(Map<Episode, EnclosingMethods> patterns,
			Map<String, Set<IMethodName>> repos, List<Event> events,
			EpisodeType type, int frequency, double entropy) throws IOException {
		Logger.log("Outputting results for %s-order configuration ...",
				type.toString());

		StringBuilder sb = new StringBuilder();
		sb.append("PatternId\tFacts\tFrequency\tEntropy\t#Repos\n");

		int patternId = 0;

		for (Map.Entry<Episode, EnclosingMethods> entry : patterns.entrySet()) {
			Episode pattern = entry.getKey();
			EnclosingMethods encls = entry.getValue();
			int numOccs = entry.getValue().getOccurrences();
			assertTrue(numOccs >= entry.getKey().getFrequency(),
					"Found insufficient number of occurences!");

			Set<IMethodName> methodOcc = encls.getMethodNames(numOccs);
			Set<String> repositories = Sets.newLinkedHashSet();

			for (Map.Entry<String, Set<IMethodName>> entryRepos : repos
					.entrySet()) {
				for (IMethodName methodName : entryRepos.getValue()) {
					if (methodOcc.contains(methodName)) {
						repositories.add(entryRepos.getKey());
						break;
					}
				}
			}
			sb.append(patternId + "\t");
			sb.append(pattern.getFacts().toString() + "\t");
			sb.append(pattern.getFrequency() + "\t");
			sb.append(pattern.getEntropy() + "\t");
			sb.append(repositories.size() + "\n");

			store(pattern, type, patternId, events, frequency, entropy);
			patternId++;
		}
		sb.append("\n");
		FileUtils.writeStringToFile(getEvalPath(frequency, entropy, type),
				sb.toString());
	}

	private void checkGeneralizability(Map<Episode, EnclosingMethods> patterns,
			Event elemMethod, List<Fact> method) {

		for (Map.Entry<Episode, EnclosingMethods> entry : patterns.entrySet()) {
			Episode pattern = entry.getKey();

			if (method.containsAll(pattern.getEvents())) {
				patterns.get(pattern).addMethod(pattern, method, elemMethod);
			}
		}
	}

	private void initializer(Map<Integer, Set<Episode>> patterns,
			Map<Episode, EnclosingMethods> enclMethods) {
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			for (Episode pattern : entry.getValue()) {
				enclMethods.put(pattern, new EnclosingMethods(true));
			}
		}
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
