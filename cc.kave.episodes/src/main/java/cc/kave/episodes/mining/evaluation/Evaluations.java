package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

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
import cc.kave.episodes.model.Triplet;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EpisodesFilter;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Evaluations {

	private File patternsFolder;

	private RepositoriesParser repoParser;
	private EventStreamIo eventStream;

	private EpisodesParser episodeParser;
	private EpisodesFilter episodeFilter;

	private PatternsValidation validation;
	private ValidationDataIO validationIo;

	private TransClosedEpisodes transClosure;
	private EpisodeToGraphConverter graphConverter;
	private EpisodeAsGraphWriter graphWriter;

	@Inject
	public Evaluations(@Named("patterns") File folder,
			RepositoriesParser repoParser, EventStreamIo eventsIo,
			EpisodesParser parser, EpisodesFilter filters,
			PatternsValidation validations, ValidationDataIO validationIo,
			TransClosedEpisodes closures,
			EpisodeToGraphConverter graphConverter,
			EpisodeAsGraphWriter graphWriter) {
		assertTrue(folder.exists(), "Patterns folder does not exist!");
		assertTrue(folder.isDirectory(),
				"Patterns is not a folder, but a file!");
		this.patternsFolder = folder;
		this.repoParser = repoParser;
		this.eventStream = eventsIo;
		this.episodeParser = parser;
		this.episodeFilter = filters;
		this.validation = validations;
		this.validationIo = validationIo;
		this.transClosure = closures;
		this.graphConverter = graphConverter;
		this.graphWriter = graphWriter;
	}

	public void patternsOutput(EpisodeType type, int freqEpisode, int foldNum,
			int freqThresh, double entropy) throws Exception {
		Logger.log("Reading repositories - enclosing method declarations mapper!");
		repoParser.generateReposEvents();
		Map<String, Set<ITypeName>> repoCtxMapper = repoParser
				.getRepoTypesMapper();
		Logger.log("Reading events ...");
		List<Event> trainEvents = eventStream.readMapping(freqEpisode, foldNum);
		Logger.log("Reading training stream ...");
		List<Tuple<Event, List<Fact>>> streamContexts = eventStream
				.parseStream(freqEpisode, foldNum);
		Logger.log("Reading validation data ...");
		List<Event> valData = validationIo.read(freqEpisode, foldNum);
		Map<Event, Integer> eventsMap = mergeEventsToMap(trainEvents, valData);
		List<Event> eventsList = Lists.newArrayList(eventsMap.keySet());
		List<List<Fact>> valStream = validationIo.streamOfFacts(valData,
				eventsMap);

		Map<Integer, Set<Episode>> episodes = episodeParser.parse(type,
				freqEpisode, foldNum);
		Map<Integer, Set<Episode>> patterns = episodeFilter.filter(type,
				episodes, freqThresh, entropy);
		Map<Integer, Set<Triplet<Episode, Integer, Integer>>> valPatterns = validation
				.validate(patterns, streamContexts, repoCtxMapper, eventsList,
						valStream);

		StringBuilder sb = new StringBuilder();
		int patternId = 0;

		for (Map.Entry<Integer, Set<Triplet<Episode, Integer, Integer>>> entry : valPatterns
				.entrySet()) {
			Logger.log("Outputing episodes with %d-nodes ...", entry.getKey());
			sb.append("Patterns with " + entry.getKey() + "-nodes:\n");
			sb.append("PatternId\tFacts\tFrequency\tEntropy\tNoRepos\tOccValidation\n");

			Set<Triplet<Episode, Integer, Integer>> patternSet = entry
					.getValue();
			for (Triplet<Episode, Integer, Integer> triplet : patternSet) {
				Episode ep = triplet.getFirst();

				sb.append(patternId + "\t");
				sb.append(ep.getFacts().toString() + "\t");
				sb.append(ep.getFrequency() + "\t");
				sb.append(ep.getEntropy() + "\t");
				sb.append(triplet.getSecond() + "\t");
				sb.append(triplet.getThird() + "\n");

				store(ep, type, patternId, eventsList, freqEpisode);
				patternId++;
			}
			sb.append("\n");
		}
		FileUtils.writeStringToFile(getEvalPath(freqEpisode, type),
				sb.toString());
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
		File path = new File(patternsFolder.getAbsolutePath() + "/freq"
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
