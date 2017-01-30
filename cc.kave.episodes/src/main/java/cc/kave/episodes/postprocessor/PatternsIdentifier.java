/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.episodes.postprocessor;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.ValidationDataIO;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PatternsIdentifier {

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

	private static final double ENTROPY = 0.0;

	@Inject
	public PatternsIdentifier(@Named("patterns") File patternsFolder,
			@Named("events") File eventsFolder, EpisodesFilter epFilter,
			MaximalEpisodes maxEpisodes, EventStreamIo eventStream,
			EpisodesParser epParser, TransClosedEpisodes transClosure,
			EpisodeToGraphConverter episodeGraphConverter,
			EpisodeAsGraphWriter graphWriter, ValidationDataIO validationIO) {
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
	}

	public void trainingCode(int frequency, EpisodeType episodeType)
			throws Exception {
		List<List<Fact>> stream = eventStream.parseStream(frequency);
		List<Event> enclMethods = eventStream.readMethods(frequency);
		Logger.log("Stream size is: %d", stream.size());
		Logger.log("Methods size is: %d", enclMethods.size());

		Map<Integer, Set<Episode>> episodes = episodeParser.parse(frequency,
				episodeType);
		Map<Integer, Set<Episode>> filteredEpisodes = getFilteredEpisodes(
				episodes, episodeType, frequency);
		Map<Integer, Set<Episode>> patterns = maxEpisodes
				.getMaximalEpisodes(filteredEpisodes);

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			if ((entry.getKey() < 2) || (entry.getValue().isEmpty())) {
				continue;
			}
			// Episode debug = createDebuggingEpisode();

			for (Episode episode : entry.getValue()) {
				Set<Fact> episodeEvents = episode.getEvents();
				EnclosingMethods methodsOrderRelation = new EnclosingMethods(
						true);

				for (int i = 0; i < stream.size(); i++) {

					List<Fact> method = stream.get(i);
					if (method.size() < 2) {
						continue;
					}
					// if (episode.equals(debug)) {
					// if (method.contains(new Fact(9))) {
					// Logger.log("Method: %s", method.toString());
					// }
					// }
					if (method.containsAll(episodeEvents)) {
						methodsOrderRelation.addMethod(episode, method,
								enclMethods.get(i));

						// if (episode.equals(debug)) {
						// Logger.log("Method: %s\noccurrence: %d",
						// method.toString(),
						// methodsOrderRelation.getOccurrences());
						// }

					}
				}
				if (methodsOrderRelation.getOccurrences() < episode
						.getFrequency()) {
					Logger.log("Episode: %s", episode.toString());
					Logger.log("Frequency = %d, occurrence = %d",
							episode.getFrequency(),
							methodsOrderRelation.getOccurrences());
					throw new Exception(
							"Episode is not found sufficient number of times on the training stream!");
				}
			}
			Logger.log("Processed %d-node patterns!", entry.getKey());
		}
		Logger.log("All patterns are identified in the training data!");
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

	private String getEventsPath(String dataType, int frequency) {
		String fileName = eventsFolder.getAbsolutePath() + "/freq" + frequency
				+ "/" + dataType + "Data/fold0";
		return fileName;
	}

	private Episode createDebuggingEpisode() {
		Episode episode = new Episode();
		episode.addStringsOfFacts("9", "3063");
		episode.setFrequency(356);
		episode.setEntropy(0.8157);
		return episode;
	}

	public void validationCode(int frequency, EpisodeType episodeType)
			throws Exception {
		List<Event> trainEvents = eventStream.readMapping(frequency);
		Map<Integer, Set<Episode>> episodes = episodeParser.parse(frequency,
				episodeType);
		Map<Integer, Set<Episode>> patterns = getFilteredEpisodes(episodes,
				episodeType, frequency);

		List<Event> valStream = validationIO.read(frequency);
		Map<Event, Integer> mapEvents = mergeTrainingValidationEvents(
				trainEvents, valStream);
		List<Event> allEvents = mapToList(mapEvents);
		List<List<Fact>> streamMethods = streamOfMethods(valStream, mapEvents);

		StringBuilder sb = new StringBuilder();
		int patternId = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			if ((entry.getKey() < 2) || (entry.getValue().size() == 0)) {
				continue;
			}
			sb.append("Patterns of size: " + entry.getKey() + "-events\n");
			sb.append("PatternId\tEvents\tFrequency\tEntropy\toccurrencesAsSet\toccurrencesOrder\n");
			for (Episode episode : entry.getValue()) {
				EnclosingMethods methodsWithoutOrder = new EnclosingMethods(
						false);
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
//		FileUtils.writeStringToFile(
//				getValidationPath(getPath(numbRepos, frequency, entropy)),
//				sb.toString());
	}

	public void inRepos(int numbRepos, int frequency, double entropy)
			throws Exception {
		// Map<String, List<Event>> repos = irp.generateReposEvents();
		//
		// List<Event> trainEvents = mappingParser.parse(numbRepos);
		// Map<Integer, Set<Episode>> patterns = episodeProcessor.postprocess(
		// numbRepos, frequency, entropy);
		//
		// // List<Event> stream = repos.validationStream(numbRepos);
		// Map<Event, Integer> mapEvents = mergeTrainingValidationEvents(repos,
		// trainEvents);
		// Map<String, List<List<Fact>>> streamMethods = streamOfMethods(repos,
		// mapEvents);
		// List<Event> listEvents = mapToList(mapEvents);
		// StringBuilder sb = new StringBuilder();
		// int patternId = 0;
		//
		// List<Integer> debugs = Lists.newArrayList(0, 15, 22, 29, 44, 48, 66,
		// 68, 72, 83, 90, 104, 110, 113, 120, 127, 130, 136, 144, 146,
		// 148, 149, 150, 151, 153, 157, 158, 160, 161);
		//
		// for (Map.Entry<Integer, Set<Episode>> patternsEntry : patterns
		// .entrySet()) {
		// if ((patternsEntry.getKey() < 2)
		// || (patternsEntry.getValue().size() == 0)) {
		// continue;
		// }
		// // sb.append("Patterns of size: " + patternsEntry.getKey()
		// // + "-events\n");
		// // sb.append("Pattern\tFreq\tEntropy\trepos\tsetOcc\torderOcc\n");
		// for (Episode episode : patternsEntry.getValue()) {
		// EnclosingMethods methodsNoOrderRelation = new EnclosingMethods(
		// false);
		// EnclosingMethods methodsOrderRelation = new EnclosingMethods(
		// true);
		// Set<String> reposNames = Sets.newLinkedHashSet();
		//
		// for (Map.Entry<String, List<List<Fact>>> reposEntry : streamMethods
		// .entrySet()) {
		// for (List<Fact> method : reposEntry.getValue()) {
		// if (method.containsAll(episode.getEvents())) {
		// methodsNoOrderRelation.addMethod(episode, method,
		// listEvents);
		// methodsOrderRelation.addMethod(episode, method,
		// listEvents);
		// reposNames.add(reposEntry.getKey());
		// }
		// }
		// }
		// if (debugs.contains(patternId)) {
		// sb.append("pattern id = " + patternId + "\t" +
		// episode.getFacts().toString() + "\n");
		// sb.append("repository name is" + reposNames.toString() + "\n");
		// Set<IMethodName> methods =
		// methodsOrderRelation.getMethodNames(methodsOrderRelation.getOccurrences());
		// for (IMethodName m : methods) {
		// String methodName = m.getDeclaringType().getFullName() + "." +
		// m.getName();
		// sb.append(methodName + "\n");
		// }
		// }
		//
		// // sb.append(patternId + "\t" + episode.getFrequency() + "\t"
		// // + episode.getEntropy() + "\t" + reposNames.size()
		// // + "\t" + methodsNoOrderRelation.getOccurrences() + "\t"
		// // + methodsOrderRelation.getOccurrences() + "\n");
		// // patternsWriter(episode, trainEvents, numbRepos, frequency,
		// // entropy, patternId);
		// patternId++;
		// }
		// sb.append("\n");
		// Logger.log("Processed %d-node patterns!", patternsEntry.getKey());
		// }
		// FileUtils.writeStringToFile(
		// getValidationPath(getPath(numbRepos, frequency, entropy)),
		// sb.toString());
	}

	private void patternsWriter(Episode episode, List<Event> events,
			int numbRepos, int frequency, double entropy, int pId)
			throws IOException {
		Episode closedEpisodes = transClosure.remTransClosure(episode);

		File filePath = getPath(numbRepos, frequency, entropy);

		DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter.convert(
				closedEpisodes, events);
		graphWriter.write(graph, getGraphPaths(filePath, pId));
	}

	private List<Event> mapToList(Map<Event, Integer> events) {
		List<Event> result = new LinkedList<Event>();

		for (Map.Entry<Event, Integer> entry : events.entrySet()) {
			result.add(entry.getKey());
		}
		return result;
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
