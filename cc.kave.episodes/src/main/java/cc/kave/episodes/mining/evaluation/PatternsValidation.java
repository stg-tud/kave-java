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

import cc.kave.commons.model.naming.codeelements.IMethodName;
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

	private static final double ENTROPY = 0.0;

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

	public void validate(int frequency, EpisodeType episodeType)
			throws Exception {
		Logger.log("Reading events ...");
		List<Event> trainEvents = eventStream.readMapping(frequency);
		Logger.log("Reading training stream ...");
		List<Tuple<List<Fact>, Event>> streamContexts = eventStream
				.parseEventStream(frequency);

		Logger.log("Reading episodes ...");
		Map<Integer, Set<Episode>> episodes = episodeParser.parse(frequency,
				episodeType);
		Map<Integer, Set<Episode>> patterns = getFilteredEpisodes(episodes,
				episodeType, frequency);

		Logger.log("Reading repositories -  enclosing method declarations mapper!");
		repoParser.generateReposEvents();
		Map<String, Set<ITypeName>> repoCtxMapper = repoParser.getRepoTypesMapper();
		
		Logger.log("Reading validation data ...");
		List<Event> valData = validationIO.read(frequency);
		Map<Event, Integer> eventsMap = mergeEventsToMap(trainEvents, valData);
		List<Event> eventsList = Lists.newArrayList(eventsMap.keySet());
		List<List<Fact>> valStream = streamOfMethods(valData, eventsMap);

		StringBuilder sb = new StringBuilder();
		int patternId = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			if (entry.getKey() < 2) {
				continue;
			}
			sb.append("Patterns with " + entry.getKey() + "-nodes:\n");
			sb.append("PatternId\tEvents\tFrequency\tEntropy\tNoRepos\tOccValidation\n");

			Logger.log("Processing episodes with %d-nodes ...", entry.getKey());
			for (Episode episode : entry.getValue()) {
				Logger.log("Processing pattern %d ...", patternId);

				int occTraining = getReposOcc(episode, frequency,
						streamContexts, repoCtxMapper);
				if (occTraining == 0) {
					continue;
				}
				sb.append(patternId + "\t");
				sb.append(episode.getFacts().toString() + "\t");
				sb.append(episode.getFrequency() + "\t");
				sb.append(episode.getEntropy() + "\t");
				sb.append(occTraining + "\t");

				int occValidation = getValOcc(episode, frequency, eventsList,
						valStream, streamContexts);
				sb.append(occValidation + "\n");

				store(episode, episodeType, patternId, trainEvents, frequency);
				patternId++;
			}
			sb.append("\n");
		}
		FileUtils.writeStringToFile(getEvalPath(frequency, episodeType),
				sb.toString());
	}

	private int getValOcc(Episode episode, int frequency,
			List<Event> eventsList, List<List<Fact>> valStream,
			List<Tuple<List<Fact>, Event>> trainStream) {

		Set<ITypeName> trainTypes = getTrainTypeNames(trainStream);

		EnclosingMethods enclMethods = new EnclosingMethods(true);

		for (List<Fact> method : valStream) {
			if (method.size() < 2) {
				continue;
			}
			if (method.containsAll(episode.getEvents())) {
				Event methodCtx = getMethodName(method, eventsList);
				ITypeName typeName = null;
				try {
					typeName = methodCtx.getMethod().getDeclaringType();
				} catch (Exception e) {
				}
				if (!trainTypes.contains(typeName)) {
					enclMethods.addMethod(episode, method, methodCtx);
				}
			}
		}
		return enclMethods.getOccurrences();
	}

	private Set<ITypeName> getTrainTypeNames(
			List<Tuple<List<Fact>, Event>> trainStream) {
		Set<ITypeName> results = Sets.newLinkedHashSet();

		for (Tuple<List<Fact>, Event> tuple : trainStream) {
			ITypeName typeName = null;
			try {
				typeName = tuple.getSecond().getMethod().getDeclaringType();
			} catch (Exception e) {
			}
			results.add(typeName);
		}
		return results;
	}

	private File getEvalPath(int frequency, EpisodeType episodeType) {
		File fileName = new File(getPatternsPath(frequency, episodeType)
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

	private String getPatternsPath(int frequency, EpisodeType episodeType) {
		File path = new File(patternsFolder.getAbsoluteFile() + "/freq"
				+ frequency + "/" + episodeType.toString());
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

	private int getReposOcc(Episode episode, int frequency,
			List<Tuple<List<Fact>, Event>> streamContexts,
			Map<String, Set<ITypeName>> repoCtxMapper) {

		EnclosingMethods methodsOrderRelation = new EnclosingMethods(true);
		Set<String> ctxs = Sets.newLinkedHashSet();

		for (Tuple<List<Fact>, Event> tuple : streamContexts) {
			List<Fact> method = tuple.getFirst();
			if (method.size() < 2) {
				continue;
			}
			if (method.containsAll(episode.getEvents())) {
				IMethodName methodCtx = tuple.getSecond().getMethod();
				String typeName = "";
				try {
					typeName = methodCtx.getDeclaringType().getFullName();
				} catch (Exception e) {
				}
				String methodName = "";
				try {
					methodName = methodCtx.getName();
				} catch (Exception e) {
				}
				String ctxName = typeName + "." + methodName;
				if (ctxs.contains(ctxName)) {
					continue;
				}
				ctxs.add(ctxName);
				methodsOrderRelation.addMethod(episode, method,
						tuple.getSecond());
			}
		}
		if (methodsOrderRelation.getOccurrences() < frequency) {
			Logger.log("Episode %s", episode.getFacts());
			Logger.log("Frequencies: %d - %d",
					methodsOrderRelation.getOccurrences(),
					episode.getFrequency());
			return 0;
		}

		Set<IMethodName> methodOcc = methodsOrderRelation
				.getMethodNames(methodsOrderRelation.getOccurrences());
		Set<ITypeName> obsTypeNames = Sets.newLinkedHashSet();
		Set<String> repositories = Sets.newLinkedHashSet();

		for (Map.Entry<String, Set<ITypeName>> entry : repoCtxMapper
				.entrySet()) {
			for (ITypeName methodName : entry.getValue()) {
				ITypeName typeName = null;
				try {
					typeName = methodName.getDeclaringType();
				} catch (Exception e) {
				}
				if (obsTypeNames.contains(typeName)) {
					continue;
				}
				obsTypeNames.add(typeName);
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
			int frequency) {
		if (episodeType == EpisodeType.GENERAL) {
			return episodeFilter.filter(episodes, frequency, ENTROPY);
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
