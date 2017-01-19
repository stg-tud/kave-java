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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.episodes.evaluation.queries.QueryStrategy;
import cc.kave.episodes.io.MappingParser;
import cc.kave.episodes.io.ValidationContextsParser;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;
import cc.recommenders.io.Logger;

public class QueriesGraphGenerator {

	private ValidationContextsParser validationParser;
	private MappingParser mappingParser;
	private EpisodeToGraphConverter episodeGraphConverter;
	private TransClosedEpisodes transitivityClosure;
	private EpisodeAsGraphWriter writer;
	private QueryStrategy queryGenerator;

	private File rootFolder;

	@Inject
	public QueriesGraphGenerator(@Named("graph") File directory, ValidationContextsParser parser,
			MappingParser mappingParser,  TransClosedEpisodes transitivityClosure, 
			EpisodeAsGraphWriter writer, EpisodeToGraphConverter graphConverter,
			QueryStrategy queryGenerator) {

		assertTrue(directory.exists(), "Validation data folder does not exist");
		assertTrue(directory.isDirectory(), "Validation data folder is not a folder, but a file");

		this.rootFolder = directory;
		this.validationParser = parser;
		this.mappingParser = mappingParser;
		this.episodeGraphConverter = graphConverter;
		this.transitivityClosure = transitivityClosure;
		this.writer = writer;
		this.queryGenerator = queryGenerator;
	}

	public void generateGraphs(int numbRepos) throws Exception {
		
		Logger.setPrinting(true);
		
		Logger.log("Reading the mapping file");
		List<Event> eventMapping = mappingParser.parse(numbRepos);
		
		Logger.log("Readng Contexts");
		Set<Episode> validationData = validationParser.parse(eventMapping);
		
		
		String directory = createDirectoryStructure();

		int episodeID = 0;

		for (Episode e : validationData) {
			
			if (e.getNumEvents() > 1) {
				int queryID = 0;
				
				Map<Double, Set<Episode>> queries = queryGenerator.byPercentage(e);
				
				Logger.log("Removing transitivity closures");
				Episode ep = transitivityClosure.remTransClosure(e);
				
				Logger.log("Writting episode number %s.\n", episodeID);
				DirectedGraph<Fact, DefaultEdge> epGraph = episodeGraphConverter.convert(ep, eventMapping);
				writer.write(epGraph, getEpisodePath(directory, episodeID));
				
				if (!queries.isEmpty()) {
					for (Map.Entry<Double, Set<Episode>> entry : queries.entrySet()) {
						for (Episode query : entry.getValue()) {
							Episode simQuery = transitivityClosure.remTransClosure(query);
							DirectedGraph<Fact, DefaultEdge> queryGraph = episodeGraphConverter.convert(simQuery, eventMapping);
							writer.write(queryGraph, getQueryPath(directory, episodeID, queryID));
							queryID++;
						}
					}
				}
				episodeID++;
			}
		}
	}

	private String getQueryPath(String directory, int episodeID, int queryID) {
		String fileName = directory + "/Episode" + episodeID + "/query" + queryID + ".dot";
		return fileName;
	}

	private String createDirectoryStructure() {
		String targetDirectory = rootFolder.getAbsolutePath() + "/graphs/validationData/Queries/";
		if (!(new File(targetDirectory).isDirectory())) {
			new File(targetDirectory).mkdirs();
		}
		return targetDirectory;
	}

	private String getEpisodePath(String folderPath, int episodeNumber) throws Exception {
		String typeFolder = folderPath + "/Episode" + episodeNumber + "/";
		if (!(new File(typeFolder).isDirectory())) {
			new File(typeFolder).mkdirs();
		}
		String fileName = typeFolder + "/episode" + episodeNumber + ".dot";

		return fileName;
	}
}
