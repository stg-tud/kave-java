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
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.ValidationContextsParser;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;
import cc.recommenders.io.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ValidationDataGraphGenerator {

	private ValidationContextsParser validationParser;
	private EventStreamIo streamIo;
	private EpisodeToGraphConverter episodeGraphConverter;
	private TransClosedEpisodes transitivityClosure;
	private EpisodeAsGraphWriter writer;

	private File rootFolder;

	@Inject
	public ValidationDataGraphGenerator(@Named("graph") File directory, ValidationContextsParser parser,
			EventStreamIo streamIo,  TransClosedEpisodes transitivityClosure, 
			EpisodeAsGraphWriter writer, EpisodeToGraphConverter graphConverter) {

		assertTrue(directory.exists(), "Validation data folder does not exist");
		assertTrue(directory.isDirectory(), "Validation data folder is not a folder, but a file");

		this.rootFolder = directory;
		this.validationParser = parser;
		this.streamIo = streamIo;
		this.episodeGraphConverter = graphConverter;
		this.transitivityClosure = transitivityClosure;
		this.writer = writer;
	}

	public void generateGraphs(int frequency) throws Exception {
		
		Logger.setPrinting(false);
		
		Logger.log("Reading the mapping file");
		List<Event> eventMapping = streamIo.readMapping(frequency);
		
		Logger.log("Readng Contexts");
		Set<Episode> validationData = validationParser.parse(eventMapping);
		
		String directory = createDirectoryStructure();

		int graphIndex = 0;

		for (Episode e : validationData) {
			Episode simplEpisode = transitivityClosure.remTransClosure(e);
			Logger.log("Writting episode number %s.\n", graphIndex);
			DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter.convert(simplEpisode, eventMapping);
			List<String> types = getAPIType(simplEpisode, eventMapping);
			for (String t : types) {
				writer.write(graph, getFilePath(directory, t, graphIndex));
			}
			graphIndex++;
		}
	}

	private List<String> getAPIType(Episode episode, List<Event> eventMapper) {
		List<String> apiTypes = new LinkedList<String>();
		for (Fact fact : episode.getFacts()) {
			if (fact.isRelation()) {
				continue;
			}
			int index = fact.getFactID();
			String type = eventMapper.get(index).getMethod().getDeclaringType().getFullName().toString().replace(".",
					"/");
			if (!apiTypes.contains(type)) {
				apiTypes.add(type);
			}
		}
		return apiTypes;
	}

	private String createDirectoryStructure() {
		String targetDirectory = rootFolder.getAbsolutePath() + "/graphs/validationData/";
		if (!(new File(targetDirectory).isDirectory())) {
			new File(targetDirectory).mkdirs();
		}
		return targetDirectory;
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
