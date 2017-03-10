package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.RepositoriesParser;
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
import cc.kave.episodes.postprocessor.TransClosedEpisodes;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PatternsValidation {

	private File patternsFolder;

	private EventStreamIo eventStream;
	private EpisodesParser episodeParser;
	private EpisodesFilter episodeFilter;

	private ValidationDataIO validationIO;
	private TransClosedEpisodes transClosure;
	private EpisodeToGraphConverter episodeGraphConverter;
	private EpisodeAsGraphWriter graphWriter;

	private RepositoriesParser repoParser;

	@Inject
	public PatternsValidation(@Named("patterns") File patternsFolder,
			EpisodesFilter epFilter, EventStreamIo eventStream,
			EpisodesParser epParser, TransClosedEpisodes transClosure,
			EpisodeToGraphConverter episodeGraphConverter,
			EpisodeAsGraphWriter graphWriter, ValidationDataIO validationIO,
			RepositoriesParser reposParser) {
		assertTrue(patternsFolder.exists(), "Patterns folder does not exist");
		assertTrue(patternsFolder.isDirectory(),
				"Patterns is not a folder, but a file");
		this.patternsFolder = patternsFolder;
		this.eventStream = eventStream;
		this.episodeParser = epParser;
		this.episodeFilter = epFilter;
		this.transClosure = transClosure;
		this.episodeGraphConverter = episodeGraphConverter;
		this.graphWriter = graphWriter;
		this.validationIO = validationIO;
		this.repoParser = reposParser;
	}

	public Map<Integer, Set<Tuple<Episode, Boolean>>> validate(EpisodeType episodeType, int frequency,
			double entropy, int foldNum) throws Exception {
		Map<Integer, Set<Tuple<Episode, Boolean>>> results = Maps.newLinkedHashMap();
		
		Logger.log("Reading events ...");
		List<Event> trainEvents = eventStream.readMapping(frequency, foldNum);
		Logger.log("Reading training stream ...");
		List<Tuple<Event, List<Fact>>> streamContexts = eventStream
				.parseStream(frequency, foldNum);

		Logger.log("Reading episodes ...");
		Map<Integer, Set<Episode>> episodes = episodeParser.parse(episodeType,
				frequency, foldNum);
//		Map<Integer, Set<Episode>> patterns = getFilteredEpisodes(episodes,
//				episodeType, frequency, entropy);

		Logger.log("Reading repositories -  enclosing method declarations mapper!");
		repoParser.generateReposEvents();
		Map<String, Set<ITypeName>> repoCtxMapper = repoParser
				.getRepoTypesMapper();

		Logger.log("Reading validation data ...");
		List<Event> valData = validationIO.read(frequency, foldNum);
		Map<Event, Integer> eventsMap = mergeEventsToMap(trainEvents, valData);
		List<Event> eventsList = Lists.newArrayList(eventsMap.keySet());
		List<List<Fact>> valStream = validationIO.streamOfFacts(valData,
				eventsMap);

		StringBuilder sb = new StringBuilder();
		int patternId = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			if (entry.getKey() < 2) {
				continue;
			}
			Set<Tuple<Episode, Boolean>> pattsVal = Sets.newLinkedHashSet();
			sb.append("Patterns with " + entry.getKey() + "-nodes:\n");
			sb.append("PatternId\tEvents\tFrequency\tEntropy\tNoRepos\tOccValidation\n");

			Logger.log("Processing episodes with %d-nodes ...", entry.getKey());
			for (Episode episode : entry.getValue()) {
				int numReposOccur = getReposOcc(episode, streamContexts,
						repoCtxMapper);
				sb.append(patternId + "\t");
				sb.append(episode.getFacts().toString() + "\t");
				sb.append(episode.getFrequency() + "\t");
				sb.append(episode.getEntropy() + "\t");
				sb.append(numReposOccur + "\t");

				int occValidation = getValOcc(episode, eventsList, valStream);
				sb.append(occValidation + "\n");
				
				if ((occValidation == 0) && (numReposOccur < 2)) {
					pattsVal.add(Tuple.newTuple(episode, false));
				} else {
					pattsVal.add(Tuple.newTuple(episode, true));
				}
				store(episode, episodeType, patternId, trainEvents, frequency);
				patternId++;
			}
			results.put(entry.getKey(), pattsVal);
			sb.append("\n");
		}
		FileUtils.writeStringToFile(getEvalPath(frequency, episodeType),
				sb.toString());
		return results;
	}

	private int getValOcc(Episode episode, List<Event> eventsList,
			List<List<Fact>> valStream) {

		EnclosingMethods enclMethods = new EnclosingMethods(true);

		for (List<Fact> method : valStream) {
			if (method.size() < 2) {
				continue;
			}
			if (method.containsAll(episode.getEvents())) {
				Event methodCtx = getMethodName(method, eventsList);
				enclMethods.addMethod(episode, method, methodCtx);
			}
		}
		return enclMethods.getOccurrences();
	}

	private File getEvalPath(int frequency, EpisodeType episodeType) {
		File fileName = new File(getResultPath(frequency, episodeType)
				+ "/evaluations.txt");
		return fileName;
	}

	private void store(Episode episode, EpisodeType epiosodeType,
			int patternId, List<Event> events, int frequency)
			throws IOException {
		Episode pattern = transClosure.remTransClosure(episode);
		DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter.convert(
				pattern, events);
		graphWriter.write(graph,
				getGraphPath(frequency, epiosodeType, patternId));
	}

	private String getGraphPath(int frequency, EpisodeType episodeType,
			int patternId) {
		String fileName = getPatternsPath(frequency, episodeType) + "/pattern"
				+ patternId + ".dot";
		return fileName;
	}

	private String getResultPath(int frequency, EpisodeType type) {
		File path = new File(patternsFolder.getAbsolutePath() + "/freq"
				+ frequency + "/" + type.toString());
		if (!path.exists()) {
			path.mkdirs();
		}
		return path.getAbsolutePath();
	}

	private String getPatternsPath(int frequency, EpisodeType type) {
		File path = new File(getResultPath(frequency, type) + "/allPatterns");
		if (!path.exists()) {
			path.mkdirs();
		}
		return path.getAbsolutePath();
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

	private int getReposOcc(Episode episode,
			List<Tuple<Event, List<Fact>>> streamContexts,
			Map<String, Set<ITypeName>> repoCtxMapper) {

		EnclosingMethods methodsOrderRelation = new EnclosingMethods(true);

		for (Tuple<Event, List<Fact>> tuple : streamContexts) {
			List<Fact> method = tuple.getSecond();
			if (method.size() < 2) {
				continue;
			}
			if (method.containsAll(episode.getEvents())) {
				methodsOrderRelation.addMethod(episode, method,
						tuple.getFirst());
			}
		}
		int trainOcc = methodsOrderRelation.getOccurrences();
		assertTrue(trainOcc >= episode.getFrequency(),
				"Episode is not found sufficient number of times in the Training Data!");

		Set<ITypeName> methodOcc = methodsOrderRelation.getTypeNames(trainOcc);
		Set<String> repositories = Sets.newLinkedHashSet();

		for (Map.Entry<String, Set<ITypeName>> entry : repoCtxMapper.entrySet()) {
			for (ITypeName methodName : entry.getValue()) {
				if (methodOcc.contains(methodName)) {
					repositories.add(entry.getKey());
					break;
				}
			}
		}
		return repositories.size();
	}

	private Map<Integer, Set<Episode>> getFilteredEpisodes(
			Map<Integer, Set<Episode>> episodes, EpisodeType episodeType,
			int frequency, double entropy) {
		if (episodeType == EpisodeType.GENERAL) {
			return episodeFilter.filter(episodes, frequency, entropy);
		}
		return episodes;
	}

	private Map<Event, Integer> mergeEventsToMap(List<Event> lista,
			List<Event> listb) {
		Map<Event, Integer> events = Maps.newLinkedHashMap();
		int id = 0;

		for (Event event : lista) {
			events.put(event, id);
			id++;
		}
		for (Event event : listb) {
			if (!events.containsKey(event)) {
				events.put(event, id);
				id++;
			}
		}
		return events;
	}
}
