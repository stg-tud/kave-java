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

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import cc.kave.commons.model.episodes.Event;
import cc.kave.episodes.evaluation.queries.QueryGeneratorByPercentage;
import cc.kave.episodes.mining.patterns.MaximalEpisodes;
import cc.kave.episodes.mining.reader.EpisodeParser;
import cc.kave.episodes.mining.reader.EventMappingParser;
import cc.kave.episodes.mining.reader.ValidationContextsParser;
import cc.kave.episodes.model.Averager;
import cc.kave.episodes.model.Episode;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

public class Evaluation {
	
	private static final int PROPOSALS = 1;
	private static final int FREQUENCY = 5;
	private static final double BIDIRECTIONAL = 0.01;
	private static final double PROBABILITY = 0.5;
	
	private ValidationContextsParser validationParser;
	private EventMappingParser mappingParser;
	private QueryGeneratorByPercentage queryGenerator;
	private EpisodeRecommender recommender;
	private EpisodeParser episodeParser;
	private MaximalEpisodes maxEpisodeTracker;
	
	private Averager averager = new Averager();
	private DecimalFormat df = new DecimalFormat("0.00"); 

	@Inject
	public Evaluation(ValidationContextsParser parser, EventMappingParser mappingParser, 
			QueryGeneratorByPercentage queryGenerator, EpisodeRecommender recommender, 
			EpisodeParser episodeParser, MaximalEpisodes maxEpisodeTracker) {

		this.validationParser = parser;
		this.mappingParser = mappingParser;
		this.queryGenerator = queryGenerator;
		this.recommender = recommender;
		this.episodeParser = episodeParser;
		this.maxEpisodeTracker = maxEpisodeTracker;
	}

	public void evaluate() throws Exception {
		
		Logger.setPrinting(true);
		
		Logger.log("Reading the learned patterns");
		Map<Integer, Set<Episode>> patterns = episodeParser.parse(FREQUENCY, BIDIRECTIONAL);
		Map<Integer, Set<Episode>> maxPatterns = maxEpisodeTracker.getMaximalEpisodes(patterns);
		
		Logger.log("Reading the mapping file");
		List<Event> eventMapping = mappingParser.parse();
		
		Logger.log("Readng the validation data");
		Set<Episode> validationData = validationParser.parse(eventMapping);

		Logger.log("Patterns configuration:");
		Logger.log("Frequency = %d", FREQUENCY);
		Logger.log("Bidirectional measure: " + df.format(BIDIRECTIONAL));
		Logger.log("");
		
		Logger.log("Querying strategy: " + df.format(PROBABILITY));
		Logger.log("Proposal strategy: %d", PROPOSALS);
		Logger.log("");
		
		Logger.log("Episode\tF1-value");
		
		int targetCounter = 0;
		for (Episode e : validationData) {
			
			if (e.getNumEvents() > 2) {
				Set<Episode> queries = queryGenerator.generateQueries(e, PROBABILITY);
				
				for (Episode query : queries) {
					Set<Tuple<Episode, Double>> proposals = recommender.calculateProposals(query, maxPatterns, PROPOSALS);
					if (proposals.size() > 0) {
						for (Tuple<Episode, Double> tuple : proposals) {
							averager.addValue(tuple.getSecond());
						}
					}
				}
//				System.out.println("Episode = " + targetCounter + ", F1-value = " + averager.average());
				Logger.log("" + targetCounter + "\t" + df.format(averager.average()));
//				Logger.log("Episode =\t%d\tF1-value = %.00f", targetCounter, averager.average());
				averager.clear();
				targetCounter++;
			}
		}
	}
}
