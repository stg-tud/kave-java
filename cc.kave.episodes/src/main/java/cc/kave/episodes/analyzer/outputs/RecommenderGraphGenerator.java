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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.evaluation.queries.QueryGeneratorByPercentage;
import cc.kave.episodes.mining.evaluation.EpisodeRecommender;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.mining.graphs.TransitivelyClosedEpisodes;
import cc.kave.episodes.mining.patterns.MaximalEpisodes;
import cc.kave.episodes.mining.reader.EpisodeParser;
import cc.kave.episodes.mining.reader.EventMappingParser;
import cc.kave.episodes.mining.reader.ValidationContextsParser;
import cc.kave.episodes.model.Episode;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

public class RecommenderGraphGenerator {
	
	private static final int PROPOSALS = 3;

	private ValidationContextsParser validationParser;
	private EventMappingParser mappingParser;
	private EpisodeToGraphConverter graphConverter;
	private TransitivelyClosedEpisodes transitivityClosure;
	private EpisodeAsGraphWriter writer;
	private QueryGeneratorByPercentage queryGenerator;
	private EpisodeRecommender recommender;
	private EpisodeParser episodeParser;
	private MaximalEpisodes maxEpisodeTracker;

	private File rootFolder;

	@Inject
	public RecommenderGraphGenerator(@Named("graph") File directory, ValidationContextsParser parser,
			EventMappingParser mappingParser,  TransitivelyClosedEpisodes transitivityClosure, 
			EpisodeAsGraphWriter writer, EpisodeToGraphConverter graphConverter,
			QueryGeneratorByPercentage queryGenerator, EpisodeRecommender recommender, 
			EpisodeParser episodeParser, MaximalEpisodes maxEpisodeTracker) {

		assertTrue(directory.exists(), "Validation data folder does not exist");
		assertTrue(directory.isDirectory(), "Validation data folder is not a folder, but a file");

		this.rootFolder = directory;
		this.validationParser = parser;
		this.mappingParser = mappingParser;
		this.graphConverter = graphConverter;
		this.transitivityClosure = transitivityClosure;
		this.writer = writer;
		this.queryGenerator = queryGenerator;
		this.recommender = recommender;
		this.episodeParser = episodeParser;
		this.maxEpisodeTracker = maxEpisodeTracker;
	}

	public void generateGraphs() throws Exception {
		
		Logger.setPrinting(true);
		
		Map<Integer, Set<Episode>> patterns = episodeParser.parse(5, 0.01);
		Map<Integer, Set<Episode>> maxPatterns = maxEpisodeTracker.getMaximalEpisodes(patterns);
		
		List<Event> eventMapping = mappingParser.parse();
		
		Set<Episode> validationData = validationParser.parse(eventMapping);
		
		Map<Integer, Set<Episode>> simPatterns = new HashMap<Integer, Set<Episode>>();
		for (Map.Entry<Integer, Set<Episode>> entry : maxPatterns.entrySet()) {
			Set<Episode> setPatterns = transitivityClosure.removeTransitivelyClosure(entry.getValue());
			simPatterns.put(entry.getKey(), setPatterns);
		}
		String directory = createDirectoryStructure();

		int episodeID = 0;

		for (Episode e : validationData) {
			
			if (e.getNumEvents() > 2) {
				int queryID = 0;
				
				Map<Double, Set<Episode>> queries = queryGenerator.generateQueries(e);
				
				Set<Episode> simpEpisode = transitivityClosure.removeTransitivelyClosure(Sets.newHashSet(e));
				Episode targetQuery = wrap(simpEpisode);
				
				DirectedGraph<Fact, DefaultEdge> epGraph = graphConverter.convert(targetQuery, eventMapping);
				writer.write(epGraph, getEpisodePath(directory, episodeID));
				
				for (Map.Entry<Double, Set<Episode>> entry : queries.entrySet()) {
					for (Episode query : entry.getValue()) {
						Set<Episode> simpQuery = transitivityClosure.removeTransitivelyClosure(Sets.newHashSet(query));
						DirectedGraph<Fact, DefaultEdge> queryGraph = graphConverter.convert(wrap(simpQuery), eventMapping);
						writer.write(queryGraph, getQueryPath(directory, episodeID, queryID));
					
						int proposalID = 0;
					
						Set<Tuple<Episode, Double>> proposals = recommender.calculateProposals(query, maxPatterns, PROPOSALS);
						Set<Double> probProposals = Sets.newLinkedHashSet();
						for (Tuple<Episode, Double> tuple : proposals) {
							probProposals.add(tuple.getSecond());
							Set<Episode> proposal = transitivityClosure.removeTransitivelyClosure(Sets.newHashSet(tuple.getFirst()));
							DirectedGraph<Fact, DefaultEdge> proposalGraph = graphConverter.convert(wrap(proposal), eventMapping);
							writer.write(proposalGraph, getProposalPath(directory, episodeID, queryID, proposalID));
							proposalID++;
						}
						Logger.log("Episode = %d\tQuery = %d", episodeID, queryID);
						System.out.println("Proposals: " + probProposals.toString());
						queryID++;
					}
				} 
				episodeID++;
			}
		}
	}

	private Episode wrap(Set<Episode> simpEpisode) {
		for (Episode episode : simpEpisode) {
			return episode;
		} 
		return null;
	}

	private String getQueryPath(String directory, int episodeID, int queryID) {
		String queryFolder = directory + "/episode" + episodeID + "/query" + queryID + "/";
		if (!(new File(queryFolder).isDirectory())) {
			new File(queryFolder).mkdirs();
		}
		String fileName = directory + "/episode" + episodeID + "/query" + queryID + "/query" + queryID + ".dot";
		return fileName;
	}
	
	private String getProposalPath(String directory, int episodeNumber, int queryNumber, int proposalNumber) {
		String fileName = directory + "/episode" + episodeNumber + "/query" + queryNumber + "/proposal" + proposalNumber + ".dot";
		return fileName;
	}

	private String createDirectoryStructure() {
		String targetDirectory = rootFolder.getAbsolutePath() + "/graphs/validationData/Recommendation/";
		if (!(new File(targetDirectory).isDirectory())) {
			new File(targetDirectory).mkdirs();
		}
		return targetDirectory;
	}

	private String getEpisodePath(String folderPath, int episodeNumber) throws Exception {
		String episodeFolder = folderPath + "/episode" + episodeNumber + "/";
		if (!(new File(episodeFolder).isDirectory())) {
			new File(episodeFolder).mkdirs();
		}
		String fileName = episodeFolder + "/episode" + episodeNumber + ".dot";

		return fileName;
	}
}
