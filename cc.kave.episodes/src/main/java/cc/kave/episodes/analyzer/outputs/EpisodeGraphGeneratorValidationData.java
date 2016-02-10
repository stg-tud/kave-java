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

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.mining.graphs.TransitivelyClosedEpisodes;
import cc.kave.episodes.mining.reader.EventMappingParser;
import cc.kave.episodes.mining.reader.ValidationContextsParser;
import cc.recommenders.io.Logger;

public class EpisodeGraphGeneratorValidationData {

	private ValidationContextsParser validationParser;
	private EventMappingParser mappingParser;
	private EpisodeToGraphConverter episodeGraphConverter;
	private TransitivelyClosedEpisodes transitivityClosure;
	private EpisodeAsGraphWriter writer;

	private File rootFolder;

	@Inject
	public EpisodeGraphGeneratorValidationData(@Named("graph") File directory, ValidationContextsParser parser,
			EventMappingParser mappingParser,  TransitivelyClosedEpisodes transitivityClosure, 
			EpisodeAsGraphWriter writer, EpisodeToGraphConverter graphConverter) {

		assertTrue(directory.exists(), "Episode-miner folder does not exist");
		assertTrue(directory.isDirectory(), "Episode-miner folder is not a folder, but a file");

		this.rootFolder = directory;
		this.validationParser = parser;
		this.mappingParser = mappingParser;
		this.episodeGraphConverter = graphConverter;
		this.transitivityClosure = transitivityClosure;
		this.writer = writer;
	}

	public void generateGraphs() throws Exception {
		
		System.out.println("Reading the mapping file");
		List<Event> eventMapping = mappingParser.parse();
		
		System.out.println("Readng Contexts");
		List<Episode> allEpisodes = validationParser.parse(eventMapping);
		
//		System.out.println("Removing transitivity closures");
//		List<Episode> learnedEpisodes = transitivityClosure.removeTransitivelyClosure(allEpisodes);
		
		String directory = createDirectoryStructure();

		int graphIndex = 0;

		for (Episode e : allEpisodes) {
			if (e.getNumberOfFacts() == 1) {
				continue;
			}
			Logger.log("Writting episode number %s.\n", graphIndex);
			if (graphIndex == 2 || graphIndex == 3 || graphIndex == 4 || graphIndex == 7 || graphIndex == 8 || graphIndex == 9) {
				graphIndex++;
				continue;
			}
			DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter.convert(e, eventMapping);
			List<String> types = getAPIType(e, eventMapping);
			for (String t : types) {
				writer.write(graph, getFilePath(directory, t, graphIndex));
			}
			graphIndex++;
		}
	}

	private List<String> getAPIType(Episode frequentEpisode, List<Event> eventMapper) {
		List<String> apiTypes = new LinkedList<String>();
		for (Fact fact : frequentEpisode.getFacts()) {
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
		String targetDirectory = rootFolder.getAbsolutePath() + "/graphs/";
		if (!(new File(targetDirectory).isDirectory())) {
			new File(targetDirectory).mkdir();
		}
		String configurationFolder = targetDirectory + "/validationData/";
		if (!(new File(configurationFolder).isDirectory())) {
			new File(configurationFolder).mkdir();
		}
		return configurationFolder;
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
