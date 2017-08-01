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
package cc.kave.episodes.GraphGenerator;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import cc.kave.episodes.io.EpisodesReader;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.MaximalEpisodes;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class TrainingDataGraphGenerator {

	private EpisodesReader episodeParser;
	private MaximalEpisodes maxEpisodeTracker;
	private EventStreamIo streamIo;
	private EpisodeToGraphConverter episodeGraphConverter;
	private TransClosedEpisodes transitivityClosure;
	private EpisodeAsGraphWriter writer;

	private File rootFolder;

	@Inject
	public TrainingDataGraphGenerator(@Named("graph") File directory, EpisodesReader episodeParser,
			MaximalEpisodes episodeLearned, EventStreamIo streamIo,
			TransClosedEpisodes transitivityClosure, EpisodeAsGraphWriter writer,
			EpisodeToGraphConverter graphConverter) {

		assertTrue(directory.exists(), "Episode-miner folder does not exist");
		assertTrue(directory.isDirectory(), "Episode-miner folder is not a folder, but a file");

		this.rootFolder = directory;
		this.episodeParser = episodeParser;
		this.maxEpisodeTracker = episodeLearned;
		this.streamIo = streamIo;
		this.episodeGraphConverter = graphConverter;
		this.transitivityClosure = transitivityClosure;
		this.writer = writer;
	}

//	public void generateGraphs(int frequencyThreshold, double bidirectionalThreshold) throws Exception {
//		Map<Integer, Set<Episode>> allEpisodes = episodeParser.parse(frequencyThreshold, bidirectionalThreshold);
//		Map<Integer, Set<Episode>> maxEpisodes = maxEpisodeTracker.getMaximalEpisodes(allEpisodes);
//		List<Event> eventMapping = mappingParser.parse();
//
//		String directory = createDirectoryStructure(frequencyThreshold, bidirectionalThreshold);
//
//		int graphIndex = 0;
//
//		for (Map.Entry<Integer, Set<Episode>> entry : maxEpisodes.entrySet()) {
//			Logger.log("Writting episodes with %d number of events.\n", entry.getKey());
//			Logger.append("\n");
//			if (entry.getKey() > 1) {
//				Set<Episode> learnedEpisodes = transitivityClosure.removeTransitivelyClosure(entry.getValue());
//				
//				for (Episode e : learnedEpisodes) {
//					Logger.log("Writting episode number %s.\n", graphIndex);
//					DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter.convert(e, eventMapping);
//					List<String> types = getAPIType(e, eventMapping);
//					for (String t : types) {
//						writer.write(graph, getFilePath(directory, t, graphIndex));
//					}
//					graphIndex++;
//				}
//			}
//		}
//	}

	private List<String> getAPIType(Episode episode, List<Event> events) {
		List<String> apiTypes = new LinkedList<String>();
		for (Fact fact : episode.getFacts()) {
			if (!fact.isRelation()) {
				int index = fact.getFactID();
				String type = events.get(index).getMethod().getDeclaringType().getFullName().toString().replace(".",
						"/");
				if (!apiTypes.contains(type)) {
					apiTypes.add(type);
				}
			}
		}
		return apiTypes;
	}

	private String createDirectoryStructure(int freqThresh, double bdThresh) {
		String directory = rootFolder.getAbsolutePath() + "/graphs/TrainingData/" + 
										"/configurationF" + freqThresh + "B" + bdThresh + "/";
		if (!(new File(directory).isDirectory())) {
			new File(directory).mkdirs();
		}
		return directory;
	}

	private String getFilePath(String folderPath, String apiType, int fileNumber) throws Exception {
		String typeFolder = folderPath + "/" + apiType + "/";
		if (!(new File(typeFolder).isDirectory())) {
			new File(typeFolder).mkdirs();
		}
		String fileName = typeFolder + "/graph" + fileNumber + ".dot";

		return fileName;
	}
}
