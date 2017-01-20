package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.eventstream.EventStreamGenerator;
import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.IndivReposParser;
import cc.kave.episodes.io.ValidationDataIO;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EnclosingMethods;
import cc.kave.episodes.postprocessor.EpisodesFilter;
import cc.kave.episodes.postprocessor.MaximalEpisodes;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PatternsValidation {

	private File eventsFolder;
	private File patternsFolder;

	private EventStreamIo eventStream;
	private EpisodesParser episodeParser;
	private EpisodesFilter episodeFilter;
	private MaximalEpisodes maxEpisodes;

	private ValidationDataIO validationIO;
	private TransClosedEpisodes transClosure;
	private EpisodeToGraphConverter episodeGraphConverter;
	private EpisodeAsGraphWriter graphWriter;

	private IndivReposParser repoParser;

	private static final double ENTROPY = 0.0;

	@Inject
	public PatternsValidation(@Named("patterns") File patternsFolder,
			@Named("events") File eventsFolder, EpisodesFilter epFilter,
			MaximalEpisodes maxEpisodes, EventStreamIo eventStream,
			EpisodesParser epParser, TransClosedEpisodes transClosure,
			EpisodeToGraphConverter episodeGraphConverter,
			EpisodeAsGraphWriter graphWriter, ValidationDataIO validationIO,
			IndivReposParser reposParser) {
		assertTrue(patternsFolder.exists(), "Patterns folder does not exist");
		assertTrue(patternsFolder.isDirectory(),
				"Patterns is not a folder, but a file");
		assertTrue(eventsFolder.exists(), "Events folder does not exist");
		assertTrue(eventsFolder.isDirectory(),
				"Events is not a folder, but a file");
		this.eventsFolder = eventsFolder;
		this.patternsFolder = patternsFolder;
		this.eventStream = eventStream;
		this.episodeParser = epParser;
		this.episodeFilter = epFilter;
		this.maxEpisodes = maxEpisodes;
		this.transClosure = transClosure;
		this.episodeGraphConverter = episodeGraphConverter;
		this.graphWriter = graphWriter;
		this.validationIO = validationIO;
		this.repoParser = reposParser;
	}

	public void validate(int frequency, EpisodeType episodeType)
			throws Exception {
		List<Event> events = eventStream.readMapping(getMappingPath(frequency));
		
		Map<Integer, Set<Episode>> episodes = episodeParser.parse(frequency,
				episodeType);
		Map<Integer, Set<Episode>> filteredEpisodes = getFilteredEpisodes(
				episodes, episodeType, frequency);
		Map<Integer, Set<Episode>> patterns = maxEpisodes
				.getMaximalEpisodes(filteredEpisodes);
		
		StringBuilder sb = new StringBuilder();
		int patternId = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			if (entry.getKey() < 2) {
				continue;
			}
			sb.append("Patterns with " + entry.getKey() + "-nodes:\n");
			sb.append("PatternId\tEvents\tFrequency\tEntropy\tNoRepos\tOccValidation\n");
			
			for (Episode episode : entry.getValue()) {
				sb.append(patternId + "\t");
				sb.append(episode.getEvents().toString() + "\t");
				sb.append(episode.getFrequency() + "\t");
				sb.append(episode.getEntropy() + "\t");
				
				int occTraining = getReposOcc(episode, frequency);
				sb.append(occTraining + "\t");
				
				int occValidation = getValOcc(episode, frequency);
				sb.append(occValidation + "\n");
				
				store(episode, patternId, events, frequency);
			}
			sb.append("\n");
			Logger.log("Processed %d-node patterns!", entry.getKey());
		}
		FileUtils.writeStringToFile(getEvalPath(frequency), sb.toString());
	}
	
	private int getValOcc(Episode episode, int frequency) {
		List<Event> trainEvents = eventStream
				.readMapping(getEnclMethodsPath(frequency));

		List<Event> valData = validationIO.read(frequency);

		Map<Event, Integer> mapEvents = mergeTrainingValidationEvents(
				trainEvents, valData);
		List<Event> allEvents = mapToList(mapEvents);
		List<List<Fact>> valStream = streamOfMethods(valData, mapEvents);

		EnclosingMethods enclMethods = new EnclosingMethods(true);

		for (List<Fact> method : valStream) {
			if (method.size() < 2) {
				continue;
			}
			Event methodName = getMethodName(method, allEvents);
			if (method.containsAll(episode.getEvents())) {
				enclMethods.addMethod(episode, method, methodName);
			}
		}
		return enclMethods.getOccurrences();
	}

	private File getEvalPath(int frequency) {
		File fileName = new File(getPatternsPath(frequency) + "/evaluations.txt");
		return fileName;
	}

	private void store(Episode episode, int patternId, List<Event> events, int frequency) throws IOException {
		Episode pattern = transClosure.remTransClosure(episode);
		DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter.convert(pattern, events);
		graphWriter.write(graph, getGraphPath(frequency, patternId));
	}

	private String getGraphPath(int frequency, int patternId) {
		String fileName = getPatternsPath(frequency) + "/pattern" + patternId + ".dot"; 
		return fileName;
	}

	private String getPatternsPath(int frequency) {
		File path = new File(patternsFolder.getAbsoluteFile() + "/freq" + frequency);
		if (!path.exists()) {
			path.mkdir();
		}
		return path.getAbsolutePath();
	}

	private String getEnclMethodsPath(int frequency) {
		String fileName = getTrainingPath(frequency) + "/methods.txt";
		return fileName;
	}

	private String getMappingPath(int frequency) {
		String fileName = getTrainingPath(frequency) + "/mapping.txt";
		return fileName;
	}

	private String getTrainingPath(int frequency) {
		String path = eventsFolder.getAbsolutePath() + "/freq" + frequency
				+ "/TrainingData/fold0";
		return path;
	}

	private String getTrainStreamPath(int frequency) {
		String fileName = getTrainingPath(frequency) + "/stream.txt";
		return fileName;
	}

	private Event getMethodName(List<Fact> method, List<Event> events) {
		for (Fact fact : method) {
			Event event = events.get(fact.getFactID());
			if (event.getKind() == EventKind.METHOD_DECLARATION) {
				return event;
			}
		}
		return null;
	}

	private int getReposOcc(Episode episode, int frequency) throws Exception {
		List<List<Fact>> trainStream = eventStream
				.parseStream(getTrainStreamPath(frequency));
		List<Event> enclMethods = eventStream
				.readMethods(getEnclMethodsPath(frequency));

		Map<String, Set<IMethodName>> repoCtxMapper = getRepoCtxMapper();

		EnclosingMethods methodsOrderRelation = new EnclosingMethods(true);

		for (int i = 0; i < trainStream.size(); i++) {

			List<Fact> method = trainStream.get(i);
			if (method.size() < 2) {
				continue;
			}
			if (method.containsAll(episode.getEvents())) {
				methodsOrderRelation.addMethod(episode, method,
						enclMethods.get(i));
			}
		}
		if (methodsOrderRelation.getOccurrences() < episode.getFrequency()) {
			throw new Exception(
					"Episode is not found sufficient number of times in the Training Data!");
		}

		Set<IMethodName> methodOcc = methodsOrderRelation
				.getMethodNames(methodsOrderRelation.getOccurrences());

		Set<String> repositories = Sets.newLinkedHashSet();

		for (Map.Entry<String, Set<IMethodName>> entry : repoCtxMapper
				.entrySet()) {
			for (IMethodName methodName : entry.getValue()) {
				if (methodOcc.contains(methodName)) {
					repositories.add(entry.getKey());
					break;
				}
			}
			continue;
		}
		repoCtxMapper.clear();
		return repositories.size();
	}

	private Map<String, Set<IMethodName>> getRepoCtxMapper() throws ZipException, IOException {
		Map<String, Set<IMethodName>> results = Maps.newLinkedHashMap();
		
		Map<String, EventStreamGenerator> repoCtxs = repoParser.generateReposEvents();
		
		for (Map.Entry<String, EventStreamGenerator> entry : repoCtxs.entrySet()) {
			List<Event> events = entry.getValue().getEventStream();
			Set<IMethodName> ctx = Sets.newLinkedHashSet();

			for (Event e : events) {
				if (e.getKind() == EventKind.METHOD_DECLARATION) {
					ctx.add(e.getMethod());
				}
			}
			results.put(entry.getKey(), ctx);
		}
		return results;
	}

	private List<Event> mapToList(Map<Event, Integer> events) {
		List<Event> result = new LinkedList<Event>();

		for (Map.Entry<Event, Integer> entry : events.entrySet()) {
			result.add(entry.getKey());
		}
		return result;
	}

	private Map<Integer, Set<Episode>> getFilteredEpisodes(
			Map<Integer, Set<Episode>> episodes, EpisodeType episodeType,
			int frequency) {
		if (episodeType == EpisodeType.GENERAL) {
			return episodeFilter.filter(episodes, frequency, ENTROPY);
		}
		return episodes;
	}

	private Map<Event, Integer> mergeTrainingValidationEvents(
			List<Event> trainEvents, List<Event> valStream) {
		Map<Event, Integer> completeEvents = Maps.newLinkedHashMap();
		int index = 0;
		for (Event event : trainEvents) {
			completeEvents.put(event, index);
			index++;
		}
		for (Event event : valStream) {
			if (!completeEvents.containsKey(event)) {
				completeEvents.put(event, index);
				index++;
			}
		}
		return completeEvents;
	}

	private List<List<Fact>> streamOfMethods(List<Event> stream,
			Map<Event, Integer> events) {
		List<List<Fact>> result = Lists.newLinkedList();
		List<Fact> method = Lists.newLinkedList();

		for (Event event : stream) {
			if (event.getKind() == EventKind.FIRST_DECLARATION) {
				if (!method.isEmpty()) {
					result.add(method);
					method = Lists.newLinkedList();
				}
			}
			int index = events.get(event);
			method.add(new Fact(index));
		}
		if (!method.isEmpty()) {
			result.add(method);
		}
		return result;
	}
}
