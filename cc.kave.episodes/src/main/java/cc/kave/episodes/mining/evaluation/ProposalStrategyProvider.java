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

import static cc.recommenders.io.Logger.append;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import com.google.inject.Inject;

import cc.kave.commons.model.episodes.Event;
import cc.kave.episodes.evaluation.queries.QueryGeneratorByPercentage;
import cc.kave.episodes.mining.patterns.MaximalEpisodes;
import cc.kave.episodes.mining.reader.EpisodeParser;
import cc.kave.episodes.mining.reader.EventMappingParser;
import cc.kave.episodes.mining.reader.ValidationContextsParser;
import cc.kave.episodes.model.Averager;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.ProposalResults;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

public class ProposalStrategyProvider {
	
	private static final int PROPOSALS = 3;
	private static final int FREQUENCY = 5;
	private static final double BIDIRECTIONAL = 0.01;
	private static final double PROBABILITY = 0.5;
	
	private ValidationContextsParser validationParser;
	private EventMappingParser mappingParser;
	private QueryGeneratorByPercentage queryGenerator;
	private EpisodeRecommender recommender;
	private EpisodeParser episodeParser;
	private MaximalEpisodes maxEpisodeTracker;
	
	private Map<Integer, Averager> avgQueryProposal = new LinkedHashMap<Integer, Averager>();
	private Map<Integer, Averager> avgTargetProposal = new HashMap<Integer, Averager>();
	private Map<Integer, Integer> targetStruct = new HashMap<Integer, Integer>();
	
	private List<ProposalResults> results = new LinkedList<ProposalResults>();
	
	private DecimalFormat df = new DecimalFormat("0.00"); 

	@Inject
	public ProposalStrategyProvider(ValidationContextsParser parser, EventMappingParser mappingParser, 
			QueryGeneratorByPercentage queryGenerator, EpisodeRecommender recommender, 
			EpisodeParser episodeParser, MaximalEpisodes maxEpisodeTracker) {

		this.validationParser = parser;
		this.mappingParser = mappingParser;
		this.queryGenerator = queryGenerator;
		this.recommender = recommender;
		this.episodeParser = episodeParser;
		this.maxEpisodeTracker = maxEpisodeTracker;
	}

	public void evaluate() throws ZipException, IOException {
		
		Logger.setPrinting(true);
		Map<Integer, Set<Episode>> maxPatterns = readPatterns();
		List<Event> eventMapping = readMapper();
		Set<Episode> validationData = readValidationData(eventMapping);
		configurations();
		
		initAverager();
		int episodeNo = 0;
		for (Episode e : validationData) {
			getStructure(e);
			
			if (e.getNumEvents() > 2 && e.getNumEvents() < 12) {
				Logger.log("Generating queries for episode %d with %d number of invocations:", episodeNo, e.getNumEvents() - 1);
				Set<Episode> queries = queryGenerator.generateQueries(e, PROBABILITY);
				for (Episode query : queries) {
					int proposalCounter = 1;
					Set<Tuple<Episode, Double>> proposals = recommender.calculateProposals(query, maxPatterns, PROPOSALS);
					if (proposals.size() > 0) {
						for (Tuple<Episode, Double> tuple : proposals) {
							avgQueryProposal.get(proposalCounter).addValue(tuple.getSecond());
							double evalProp = recommender.calcF1(e, tuple.getFirst());
							avgTargetProposal.get(proposalCounter).addValue(evalProp);
							proposalCounter++;
						}
						
					}
				}
				storeResults(e);
				updateAveragers();
				episodeNo++;
			}
		}
		logResults();
	}
	
	private void getStructure(Episode e) {
		int numInv = e.getNumEvents() - 1;
		
		if (targetStruct.containsKey(numInv)) {
			int counter = targetStruct.get(numInv);
			targetStruct.put(numInv, counter + 1);
		} else {
			targetStruct.put(numInv, 1);
		}
	}

	private void initAverager() {
		for (int idx = 0; idx < PROPOSALS; idx++) {
			avgQueryProposal.put(idx + 1, new Averager());
			avgTargetProposal.put(idx + 1, new Averager());
		}
	}

	private void updateAveragers() {
		for (int ind = 0; ind < PROPOSALS; ind++) {
		avgQueryProposal.get(ind + 1).clear();
		avgTargetProposal.get(ind + 1).clear();
		}
	}
	
	private void storeResults(Episode episode) {
		ProposalResults episodeResults = new ProposalResults();
		episodeResults.setTarget(episode);
		
		for (int ind = 0; ind < PROPOSALS; ind++) {
			episodeResults.addResult(avgQueryProposal.get(ind + 1).average(), avgTargetProposal.get(ind + 1).average());
		}
		results.add(episodeResults);
	}

	private void logResults() {
		append("Invocations\ttotal\n");
		for (Map.Entry<Integer, Integer> entry : targetStruct.entrySet()) {
			if (entry.getKey() > 10) {
				append("%d\t%d\n", entry.getKey(), entry.getValue());
			}
		}
		append("\n");
		
		append("Episode\t");
		for (int i = 0; i < PROPOSALS; i++) {
			append("F1-QPTop%d\tF1-TPTop%d\t", i + 1, i + 1);
		}
		append("\n");
		for (int tId = 0; tId < results.size(); tId++) {
			List<Tuple<Double, Double>> episodeResults = results.get(tId).getResults();

			if (episodeResults.get(0).getFirst() > 0.0) {
				append("%d\t", tId);
				for (Tuple<Double, Double> tuple : episodeResults) {
					append("%s\t%s\t", df.format(tuple.getFirst()), df.format(tuple.getSecond()));
				}
				append("\n");
			}
		}
	}

	private void configurations() {
		append("%% - Patterns configuration:\n");
		append("%% - Frequency = %d\n", FREQUENCY);
		append("%% - Bidirectional measure: %s\n\n", df.format(BIDIRECTIONAL));
		
		append("%% - Querying strategy: %s\n", df.format(PROBABILITY));
		append("%% - Proposal strategy: %d\n\n", PROPOSALS);
	}

	private Set<Episode> readValidationData(List<Event> eventMapping) throws ZipException, IOException {
		Logger.log("Readng the validation data\n");
		Set<Episode> validationData = validationParser.parse(eventMapping);
		return validationData;
	}

	private List<Event> readMapper() {
		Logger.log("Reading the mapping file");
		List<Event> eventMapping = mappingParser.parse();
		return eventMapping;
	}

	private Map<Integer, Set<Episode>> readPatterns() {
		Logger.log("Reading the learned patterns");
		Map<Integer, Set<Episode>> patterns = episodeParser.parse(FREQUENCY, BIDIRECTIONAL);
		Map<Integer, Set<Episode>> maxPatterns = maxEpisodeTracker.getMaximalEpisodes(patterns);
		return maxPatterns;
	}
}
