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

	public void validate(EpisodeType type, int frequency, int threshFreq,
			double threshEntropy) throws Exception {
		List<Tuple<Event, List<Event>>> stream = cxtParser.parse(frequency);
		List<Event> events = streamIo.readMapping(frequency);
		List<Tuple<Event, List<Fact>>> streamOfFacts = convertStreamToFacts(
				stream, events);

		Map<String, Set<IMethodName>> repos = cxtParser.getRepoCtxMapper();
		Map<Integer, Set<Episode>> episodes = episodeParser.parser(frequency);
		Map<Integer, Set<Episode>> patterns = patternFilter.filter(type,
				episodes, threshFreq, threshEntropy);

		StringBuilder sb = new StringBuilder();
		int patternId = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
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
				
				store(pattern, type, patternId, events, frequency);
				patternId++;
			}
			sb.append("\n");
		}
		FileUtils.writeStringToFile(getEvalPath(frequency, type),
				sb.toString());
	}

	private List<Tuple<Event, List<Fact>>> convertStreamToFacts(
			List<Tuple<Event, List<Event>>> stream, List<Event> events) {
		List<Tuple<Event, List<Fact>>> results = Lists.newLinkedList();

		for (Tuple<Event, List<Event>> tuple : stream) {
			List<Fact> facts = Lists.newLinkedList();

			for (Event event : tuple.getSecond()) {
				int id = events.indexOf(event);
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
		assertTrue(numOccs == pattern.getFrequency(),
				"Frequency and numbOccs do not match!");

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
			List<Event> events, int frequency) throws IOException {
		Episode pattern = transClosure.remTransClosure(episode);
		DirectedGraph<Fact, DefaultEdge> graph = graphConverter.convert(
				pattern, events);
		graphWriter.write(graph, getGraphPath(frequency, type, patternId));
	}
	
	private String getGraphPath(int frequency, EpisodeType type, int patternId) {
		String fileName = getPatternsPath(frequency, type) + "/pattern"
				+ patternId + ".dot";
		return fileName;
	}

	private String getPatternsPath(int frequency, EpisodeType type) {
		File path = new File(getResultPath(frequency, type) + "/allPatterns");
		if (!path.exists()) {
			path.mkdirs();
		}
		return path.getAbsolutePath();
	}

	private String getResultPath(int frequency, EpisodeType type) {
		File path = new File(patternFolder.getAbsolutePath() + "/freq"
				+ frequency + "/" + type.toString());
		if (!path.exists()) {
			path.mkdirs();
		}
		return path.getAbsolutePath();
	}

	private File getEvalPath(int frequency, EpisodeType episodeType) {
		File fileName = new File(getResultPath(frequency, episodeType)
				+ "/evaluations.txt");
		return fileName;
	}
}
