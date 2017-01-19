package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.RepoMethodsMapperIO;
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

	private RepoMethodsMapperIO reposIo;

	private static final double ENTROPY = 0.0;

	@Inject
	public PatternsValidation(@Named("patterns") File patternsFolder,
			@Named("events") File eventsFolder, EpisodesFilter epFilter,
			MaximalEpisodes maxEpisodes, EventStreamIo eventStream,
			EpisodesParser epParser, TransClosedEpisodes transClosure,
			EpisodeToGraphConverter episodeGraphConverter,
			EpisodeAsGraphWriter graphWriter, ValidationDataIO validationIO,
			RepoMethodsMapperIO reposMethods) {
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
		this.reposIo = reposMethods;
	}
	
	public void inTraining(int frequency, EpisodeType episodeType) {
		List<Event> trainEvents = eventStream.readMapping(getMethodsPath(
				"Training", frequency));

		Map<Integer, Set<Episode>> episodes = episodeParser.parse(frequency,
				episodeType);
		Map<Integer, Set<Episode>> filteredEpisodes = getFilteredEpisodes(
				episodes, episodeType, frequency);
		Map<Integer, Set<Episode>> patterns = maxEpisodes
				.getMaximalEpisodes(filteredEpisodes);

		Map<String, Set<IMethodName>> repoCtxMapper = reposIo.reader();
		
		StringBuilder sb = new StringBuilder();
		int patternId = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			if ((entry.getKey() < 2) || (entry.getValue().size() == 0)) {
				continue;
			}
			sb.append("Patterns of size: " + entry.getKey() + "-events\n");
			sb.append("PatternId\tEvents\tFrequency\tEntropy\tNoRepos\toccurrencesAsSet\toccurrencesOrder\n");
			for (Episode episode : entry.getValue()) {

				int numbRepos = getReposOcc(episode, frequency);

				EnclosingMethods methodsOrderRelation = new EnclosingMethods(
						true);

//				for (List<Fact> method : streamMethods) {
//					if (method.containsAll(episode.getEvents())) {
//						methodsWithoutOrder.addMethod(episode, method,
//								allEvents);
//						methodsOrderRelation.addMethod(episode, method,
//								allEvents);
//					}
//				}
//				sb.append(patternId + "\t" + episode.getFrequency() + "\t"
//						+ methodsWithoutOrder.getOccurrences() + "\t"
//						+ methodsOrderRelation.getOccurrences() + "\n");
//				patternsWriter(episode, trainEvents, numbRepos, frequency,
//						entropy, patternId);
				patternId++;
			}
			sb.append("\n");
			Logger.log("Processed %d-node patterns!", entry.getKey());
		}
	}

	public void inValidation(int frequency, EpisodeType episodeType) {
		List<Event> trainEvents = eventStream.readMapping(getMethodsPath(
				"Training", frequency));
		
		List<Event> valData = validationIO.read(frequency);
		Map<Event, Integer> mapEvents = mergeTrainingValidationEvents(
				trainEvents, valData);
		List<Event> allEvents = mapToList(mapEvents);
		List<List<Fact>> valStream = streamOfMethods(valData, mapEvents);
	}
	
	private int getReposOcc(Episode episode, int frequency) {
		List<List<Fact>> trainStream = eventStream.parseStream(getStreamPath(
				"Training", frequency));
		List<Event> enclMethods = eventStream.readMethods(getMethodsPath(
				"Training", frequency));

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
		Set<IMethodName> methodOcc = methodsOrderRelation
				.getMethodNames(methodsOrderRelation.getOccurrences());
		
		Map<String, Set<IMethodName>> repoCtxMapper = reposIo.reader();
		return 0;
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

	private String getMethodsPath(String dataType, int frequency) {
		String methods = getEventsPath(dataType, frequency) + "/methods.txt";
		return methods;
	}

	private String getStreamPath(String foldType, int frequency) {
		String fileName = getEventsPath(foldType, frequency) + "/stream.txt";
		return fileName;
	}

	private String getEventsPath(String dataType, int frequency) {
		String fileName = eventsFolder.getAbsolutePath() + "/freq" + frequency
				+ "/" + dataType + "Data/fold0";
		return fileName;
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

	private File getPath(int numbRepos, int freqThresh, double bidirectThresh) {
		File folderPath = new File(patternsFolder.getAbsolutePath() + "/Repos"
				+ numbRepos + "/Freq" + freqThresh + "/Bidirect"
				+ bidirectThresh + "/");
		if (!folderPath.isDirectory()) {
			folderPath.mkdirs();
		}
		return folderPath;
	}

	private String getGraphPaths(File folderPath, int patternNumber) {
		String graphPath = folderPath + "/pattern" + patternNumber + ".dot";
		return graphPath;
	}

	private File getValidationPath(File folderPath) {
		File fileName = new File(folderPath.getAbsolutePath()
				+ "/patternsValidation.txt");
		return fileName;
	}
}
