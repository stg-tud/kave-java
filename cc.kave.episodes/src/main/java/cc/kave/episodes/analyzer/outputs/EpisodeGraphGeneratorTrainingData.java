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
package cc.kave.episodes.analyzer.outputs;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.mining.graphs.TransitivelyClosedEpisodes;
import cc.kave.episodes.mining.patterns.MaximalEpisodes;
import cc.kave.episodes.mining.reader.EpisodeParser;
import cc.kave.episodes.mining.reader.EventMappingParser;
import cc.kave.episodes.model.Episode;
import cc.recommenders.io.Logger;

public class EpisodeGraphGeneratorTrainingData {

	private EpisodeParser episodeParser;
	private MaximalEpisodes maxEpisodeTracker;
	private EventMappingParser mappingParser;
	private EpisodeToGraphConverter episodeGraphConverter;
	private TransitivelyClosedEpisodes transitivityClosure;
	private EpisodeAsGraphWriter writer;

	private File rootFolder;

	@Inject
	public EpisodeGraphGeneratorTrainingData(@Named("graph") File directory, EpisodeParser episodeParser,
			MaximalEpisodes episodeLearned, EventMappingParser mappingParser,
			TransitivelyClosedEpisodes transitivityClosure, EpisodeAsGraphWriter writer,
			EpisodeToGraphConverter graphConverter) {

		assertTrue(directory.exists(), "Episode-miner folder does not exist");
		assertTrue(directory.isDirectory(), "Episode-miner folder is not a folder, but a file");

		this.rootFolder = directory;
		this.episodeParser = episodeParser;
		this.maxEpisodeTracker = episodeLearned;
		this.mappingParser = mappingParser;
		this.episodeGraphConverter = graphConverter;
		this.transitivityClosure = transitivityClosure;
		this.writer = writer;
	}

	public void generateGraphs(int frequencyThreshold, double bidirectionalThreshold) throws Exception {
		Map<Integer, List<Episode>> allEpisodes = episodeParser.parse(frequencyThreshold, bidirectionalThreshold);
		Map<Integer, List<Episode>> maxEpisodes = maxEpisodeTracker.getMaximalEpisodes(allEpisodes);
		List<Event> eventMapping = mappingParser.parse();

		String directory = createDirectoryStructure(frequencyThreshold, bidirectionalThreshold);

		int graphIndex = 0;

		for (Map.Entry<Integer, List<Episode>> entry : maxEpisodes.entrySet()) {
			Logger.log("Writting episodes with %d number of events.\n", entry.getKey());
			Logger.append("\n");
			if (entry.getKey() > 1) {
				List<Episode> learnedEpisodes = transitivityClosure.removeTransitivelyClosure(entry.getValue());
				
				for (Episode e : learnedEpisodes) {
					Logger.log("Writting episode number %s.\n", graphIndex);
					DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter.convert(e, eventMapping);
					List<String> types = getAPIType(e, eventMapping);
					for (String t : types) {
						writer.write(graph, getFilePath(directory, t, graphIndex));
					}
					graphIndex++;
				}
			}
		}
	}

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
