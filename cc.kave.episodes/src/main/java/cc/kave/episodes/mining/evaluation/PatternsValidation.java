package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
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

	// private OverlapingTypes overlaps;

	private IndivReposParser repoParser;

	private static final double ENTROPY = 0.0;

	@Inject
	public PatternsValidation(@Named("patterns") File patternsFolder,
			EpisodesFilter epFilter, EventStreamIo eventStream,
			EpisodesParser epParser, TransClosedEpisodes transClosure,
			EpisodeToGraphConverter episodeGraphConverter,
			EpisodeAsGraphWriter graphWriter, ValidationDataIO validationIO,
			IndivReposParser reposParser) {
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
		// this.overlaps = overlaps;
	}

	public void validate(int frequency, EpisodeType episodeType)
			throws ZipException, IOException {
		Logger.log("Reading events ...");
		List<Event> trainEvents = eventStream.readMapping(frequency);

		Logger.log("Reading training stream ...");
		List<Tuple<List<Fact>, Event>> streamContexts = eventStream
				.parseEventStream(frequency);
		Logger.log("Reading repositories -  enclosing method declarations mapper!");
		Map<String, Set<IMethodName>> repoCtxMapper = repoParser
				.getRepoCtxMapper();

		Logger.log("Reading episodes ...");
		Map<Integer, Set<Episode>> episodes = episodeParser.parse(frequency,
				episodeType);
		Map<Integer, Set<Episode>> patterns = getFilteredEpisodes(episodes,
				episodeType, frequency);

		Logger.log("Reading validation stream ...");
		List<Event> valData = validationIO.read(frequency);
		Logger.log("Merging events ...");
		Map<Event, Integer> eventsMap = mergeEventsToMap(trainEvents, valData);
		List<Event> eventsList = Lists.newArrayList(eventsMap.keySet());
		Logger.log("Parsing validation stream ...");
		List<List<Fact>> valStream = streamOfMethods(valData, eventsMap);

		// Set<ITypeName> overlapingTypes = overlaps.getOverlaps(frequency);

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
				sb.append(patternId + "\t");
				sb.append(episode.getFacts().toString() + "\t");
				sb.append(episode.getFrequency() + "\t");
				sb.append(episode.getEntropy() + "\t");

				Logger.log("Validating from the training data ...");
				int occTraining = getReposOcc(episode, frequency,
						streamContexts, repoCtxMapper);
				sb.append(occTraining + "\t");

				Logger.log("Validating from the test data ...");
				int occValidation = getValOcc(episode, frequency, eventsList,
						valStream, streamContexts);
				sb.append(occValidation + "\n");

				Logger.log("Saving pattern %d ...", patternId);
				store(episode, episodeType, patternId, trainEvents, frequency);
				patternId++;
			}
			sb.append("\n");
		}
		FileUtils.writeStringToFile(getEvalPath(frequency, episodeType),
				sb.toString());
	}

	private int getValOcc(Episode episode, int frequency, List<Event> events,
			List<List<Fact>> valStream,
			List<Tuple<List<Fact>, Event>> trainStream) {
		Set<ITypeName> trainTypes = getTrainTypeNames(trainStream);

		EnclosingMethods enclMethods = new EnclosingMethods(true);

		for (List<Fact> method : valStream) {
			if (method.size() < 2) {
				continue;
			}
			if (method.containsAll(episode.getEvents())) {
				Event methodName = getMethodName(method, events);

				if (!trainTypes.contains(methodName.getMethod()
						.getDeclaringType())) {
					enclMethods.addMethod(episode, method, methodName);
				}
			}
		}
		return enclMethods.getOccurrences();
	}

	private Set<ITypeName> getTrainTypeNames(
			List<Tuple<List<Fact>, Event>> trainStream) {
		Set<ITypeName> results = Sets.newLinkedHashSet();

		for (Tuple<List<Fact>, Event> tuple : trainStream) {
			results.add(tuple.getSecond().getMethod().getDeclaringType());
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
			Map<String, Set<IMethodName>> repoCtxMapper) {

		EnclosingMethods methodsOrderRelation = new EnclosingMethods(true);
		Set<String> ctxs = Sets.newLinkedHashSet();

		for (Tuple<List<Fact>, Event> tuple : streamContexts) {
			List<Fact> method = tuple.getFirst();
			if (method.size() < 2) {
				continue;
			}
			if (method.containsAll(episode.getEvents())) {
				IMethodName methodName = tuple.getSecond().getMethod();
				String ctxName = methodName.getDeclaringType().getFullName()
						+ "." + methodName.getName();
				if (!ctxs.contains(ctxName)) {
					methodsOrderRelation.addMethod(episode, method,
							tuple.getSecond());
					ctxs.add(ctxName);
				}
			}
		}
		if (methodsOrderRelation.getOccurrences() < frequency) {
			Logger.log("Episode %s", episode.getFacts());
			Logger.log("Frequencies: %d - %d",
					methodsOrderRelation.getOccurrences(),
					episode.getFrequency());
		}

		Set<IMethodName> methodOcc = methodsOrderRelation
				.getMethodNames(methodsOrderRelation.getOccurrences());
		Set<ITypeName> obsTypeNames = Sets.newLinkedHashSet();
		Set<String> repositories = Sets.newLinkedHashSet();

		for (Map.Entry<String, Set<IMethodName>> entry : repoCtxMapper
				.entrySet()) {
			for (IMethodName methodName : entry.getValue()) {
				if (obsTypeNames.contains(methodName.getDeclaringType())) {
					continue;
				}
				if (methodOcc.contains(methodName)) {
					repositories.add(entry.getKey());
					obsTypeNames.add(methodName.getDeclaringType());
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
		Logger.log("Mergin into a map structure ...");
		Map<Event, Integer> events = Maps.newLinkedHashMap();
		int id = 0;

		Logger.log("Copying list a ...");
		for (Event event : lista) {
			events.put(event, id);
			id++;
		}
		Logger.log("Copying list b ...");
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
