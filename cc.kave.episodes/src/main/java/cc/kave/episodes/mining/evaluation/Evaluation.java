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
package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;
import cc.kave.episodes.evaluation.queries.QueryGeneratorByPercentage;
import cc.kave.episodes.mining.patterns.MaximalEpisodes;
import cc.kave.episodes.mining.reader.EpisodeParser;
import cc.kave.episodes.mining.reader.EventMappingParser;
import cc.kave.episodes.mining.reader.ValidationContextsParser;
import cc.kave.episodes.model.Episode;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

public class Evaluation {
	
	private static final int PROPOSALS = 1;
	private static final int FREQUENCY = 5;
	private static final double BIDIRECTIONA = 0.01;
	private static final double PROBABILITY = 1;

	private ValidationContextsParser validationParser;
	private EventMappingParser mappingParser;
	private QueryGeneratorByPercentage queryGenerator;
	private EpisodeRecommender recommender;
	private EpisodeParser episodeParser;
	private MaximalEpisodes maxEpisodeTracker;

	@Inject
	public Evaluation(@Named("graph") File directory, ValidationContextsParser parser,
			EventMappingParser mappingParser, QueryGeneratorByPercentage queryGenerator, 
			EpisodeRecommender recommender, EpisodeParser episodeParser, 
			MaximalEpisodes maxEpisodeTracker) {

		assertTrue(directory.exists(), "Validation data folder does not exist");
		assertTrue(directory.isDirectory(), "Validation data folder is not a folder, but a file");

		this.validationParser = parser;
		this.mappingParser = mappingParser;
		this.queryGenerator = queryGenerator;
		this.recommender = recommender;
		this.episodeParser = episodeParser;
		this.maxEpisodeTracker = maxEpisodeTracker;
	}

	public void evaluate() throws Exception {
		
		Logger.setPrinting(false);
		
		Map<Integer, Set<Episode>> patterns = episodeParser.parse(FREQUENCY, BIDIRECTIONA);
		Map<Integer, Set<Episode>> maxPatterns = maxEpisodeTracker.getMaximalEpisodes(patterns);
		
		Logger.log("Reading the mapping file");
		List<Event> eventMapping = mappingParser.parse();
		
		Logger.log("Readng Contexts");
		Set<Episode> validationData = validationParser.parse(eventMapping);
		
		List<Double> metrics = new LinkedList<>();
		int queryCounter = 0;
		int episodeCounter = 0;
		
		for (Episode e : validationData) {
			
			if (e.getNumEvents() > 1) {
				System.out.println("Epiode " + episodeCounter + ": " + e.toString());
				Set<Episode> queries = queryGenerator.generateQueries(e, PROBABILITY);
				
				for (Episode query : queries) {
					System.out.println("Query: " + query.toString());
					queryCounter++;
					
					Set<Tuple<Episode, Double>> proposals = recommender.calculateProposals(query, maxPatterns, PROPOSALS);
					for (Tuple<Episode, Double> tuple : proposals) {
						System.out.println("Similarity value = " + tuple.getSecond());
						metrics.add(tuple.getSecond());
					}
				}
				episodeCounter++;
			}
		}
		Logger.log("Total number of queries = %d", queryCounter);
		Logger.log("Proposals similarity = %d", getAverage(metrics));
	}

	private double getAverage(List<Double> values) {
		int sum = 0;
		for (double v : values) {
			sum += v;
		}
		return (sum / values.size());
	}
}
