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

public class Evaluation {
	
	private static final int PROPOSALS = 3;
	private static final int FREQUENCY = 5;
	private static final double BIDIRECTIONAL = 0.01;
	
	private ValidationContextsParser validationParser;
	private EventMappingParser mappingParser;
	private QueryGeneratorByPercentage queryGenerator;
	private EpisodeRecommender recommender;
	private EpisodeParser episodeParser;
	private MaximalEpisodes maxEpisodeTracker;
	
	private Map<Double, List<Averager>> avgQueryProposal = new HashMap<Double, List<Averager>>();
	private Map<Double, List<Averager>> avgTargetProposal = new HashMap<Double, List<Averager>>();
	
	private List<ProposalResults> results = new LinkedList<ProposalResults>();
	
	private Map<Double, List<Tuple<Averager, Averager>>> synthAvg = new HashMap<Double, List<Tuple<Averager, Averager>>>();
	private Map<Double, List<Tuple<Double, Double>>> synthResults = new HashMap<Double, List<Tuple<Double, Double>>>();
	
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

	public void evaluate() throws ZipException, IOException {
		
		Logger.setPrinting(true);
		Map<Integer, Set<Episode>> maxPatterns = readPatterns();
		List<Event> eventMapping = readMapper();
		Set<Episode> validationData = readValidationData(eventMapping);
		configurations();
		
		int episodeNo = 0;
		for (Episode e : validationData) {
			if (e.getNumEvents() > 2 && e.getNumEvents() < 7) {
				Logger.log("Generating queries for episode %d with %d number of invocations", episodeNo, e.getNumEvents() - 1);
				Map<Double, Set<Episode>> queries = queryGenerator.generateQueries(e);
				
				for (Map.Entry<Double, Set<Episode>> entry : queries.entrySet()) {
					initAverager(entry.getKey());
					
					for (Episode query : entry.getValue()) {
						int propCount = 0;
						Set<Tuple<Episode, Double>> proposals = recommender.calculateProposals(query, maxPatterns, PROPOSALS);
						
						if (proposals.size() > 0) {
							for (Tuple<Episode, Double> tuple : proposals) {
								avgQueryProposal.get(entry.getKey()).get(propCount).addValue(tuple.getSecond());
								double evalProp = recommender.calcF1(e, tuple.getFirst());
								avgTargetProposal.get(entry.getKey()).get(propCount).addValue(evalProp);
								propCount++;
							}
						}
					}
				}
				storeResults(e);
				updateAveragers();
				episodeNo++;
			}
		}
		logPerEpisode();
		synthesizeResults();
		logSynthesized();
	}
	
	private void logSynthesized() {
		append("\tTop1\tTop2\tTop3\n");
		for (Map.Entry<Double, List<Tuple<Double, Double>>> entry : synthResults.entrySet()) {
			append("Removed %s\t", df.format(entry.getKey()));
			for (Tuple<Double, Double> pairs : entry.getValue()) {
				append("<%s, %s>\t", df.format(pairs.getFirst()), df.format(pairs.getSecond()));
			}
			append("\n");
		}
	}

	private void synthesizeResults() {
		
		//initialize
		for (double p = 0.25; p < 0.8; p += 0.25) {
			List<Tuple<Averager, Averager>> propPart = new LinkedList<Tuple<Averager, Averager>>();
			
			for (int ind = 0; ind < PROPOSALS; ind++) {
				propPart.add(Tuple.newTuple(new Averager(), new Averager()));
			}
			synthAvg.put(p, propPart);
		}
		
		//synthesize
		for (int ind = 0; ind < results.size(); ind++) {
			Map<Double, List<Tuple<Double, Double>>> epRes = results.get(ind).getResults();
			for (Map.Entry<Double, List<Tuple<Double, Double>>> entry : epRes.entrySet()) {
				for (int p = 0; p < entry.getValue().size(); p++) {
					double qpAvg = epRes.get(entry.getKey()).get(p).getFirst();
					double tpAvg = epRes.get(entry.getKey()).get(p).getSecond();
					synthAvg.get(entry.getKey()).get(p).getFirst().addValue(qpAvg);
					synthAvg.get(entry.getKey()).get(p).getSecond().addValue(tpAvg);
				}
			}
		}
		
		//average
		for (double p = 0.25; p < 0.8; p += 0.25) {
			List<Tuple<Double, Double>> avgs = new LinkedList<Tuple<Double, Double>>();
			
			for (int ind = 0; ind < PROPOSALS; ind++) {
				double qp = synthAvg.get(p).get(ind).getFirst().average();
				double tp = synthAvg.get(p).get(ind).getSecond().average();
				avgs.add(Tuple.newTuple(qp, tp));
			}
			synthResults.put(p, avgs);
		}
	}

	private void initAverager(double percent) {
		avgQueryProposal.put(percent, new LinkedList<Averager>());
		avgTargetProposal.put(percent, new LinkedList<Averager>());
		
		for (int idx = 0; idx < PROPOSALS; idx++) {
			avgQueryProposal.get(percent).add(new Averager());
			avgTargetProposal.get(percent).add(new Averager());
		}
	}

	private void updateAveragers() {
		avgQueryProposal.clear();
		avgTargetProposal.clear();
	}
	
	private void storeResults(Episode episode) {
		ProposalResults episodeResults = new ProposalResults();
		episodeResults.setTarget(episode);
		
		for (Map.Entry<Double, List<Averager>> entry : avgQueryProposal.entrySet()) {
			for (int ind = 0; ind < PROPOSALS; ind++) {
				double qp = entry.getValue().get(ind).average();
				if (qp > 0.0) {
					double tp = avgTargetProposal.get(entry.getKey()).get(ind).average();
					episodeResults.addResult(entry.getKey(), qp, tp);
				}
			}
		}
		results.add(episodeResults);
	}

	private void logPerEpisode() {
		append("\nEpisode:\n");
		
		for (int tId = 0; tId < results.size(); tId++) {
			append("%d\t", tId);
			Map<Double, List<Tuple<Double, Double>>> episodeResults = results.get(tId).getResults();

			for (Map.Entry<Double, List<Tuple<Double, Double>>> entry : episodeResults.entrySet()) {
				append("%s: [ ", df.format(entry.getKey()));
				for (Tuple<Double, Double> values : entry.getValue()) {
					append("<%s, %s> ", df.format(values.getFirst()), df.format(values.getSecond()));
				}
				append("]\t");
			}
			append("%d\n", results.get(tId).getTarget().getNumEvents() - 1);
		}
		append("\n");
	}

	private void configurations() {
		append("%% - Patterns configuration:\n");
		append("%% - Frequency = %d\n", FREQUENCY);
		append("%% - Bidirectional measure = %s\n", df.format(BIDIRECTIONAL));
		append("%% - Querying strategy = [25%%, 50%%, 75%%]\n");
		append("%% - Proposal strategy = %d\n\n", PROPOSALS);
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
