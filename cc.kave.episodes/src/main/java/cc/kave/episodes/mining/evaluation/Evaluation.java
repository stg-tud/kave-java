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
import static cc.recommenders.io.Logger.append;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

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
	
	private File rootFolder;
	
	private static final int PROPOSALS = 5;
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
	
	private Map<Double, List<Tuple<Double, Double>>> categoryResults = new HashMap<Double, List<Tuple<Double, Double>>>();
	
	private DecimalFormat df = new DecimalFormat("0.00"); 
	private StringBuilder sb;

	@Inject
	public Evaluation(@Named("evaluation") File directory, ValidationContextsParser parser, 
			EventMappingParser mappingParser, QueryGeneratorByPercentage queryGenerator, 
			EpisodeRecommender recommender, EpisodeParser episodeParser, 
			MaximalEpisodes maxEpisodeTracker) {
		
		assertTrue(directory.exists(), "Evaluations folder does not exist");
		assertTrue(directory.isDirectory(), "Evaluations folder is not a folder, but a file");
		this.rootFolder = directory;
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
		Map<String, Set<Episode>> targets = categorizeTargets(validationData);
		configurations();
		
		for (Map.Entry<String, Set<Episode>> categoryEntry : targets.entrySet()) {
			if (categoryEntry.getKey().equalsIgnoreCase("0-1")) {
				continue;
			}
			if (categoryEntry.getValue().isEmpty()) {
				continue;
			}
			sb = new StringBuilder();
			int targetID = 0;
			int targetsWithoutProposals = 0;
			Logger.log("Generating queries for episodes with %s number of invocations\n", categoryEntry.getKey());
			for (Episode e : categoryEntry.getValue()) {
//				Logger.log("Target %d with %d number of invocations\n", targetID);
				boolean hasProposals = false;
				Map<Double, Set<Episode>> queries = queryGenerator.generateQueries(e);
				
				for (Map.Entry<Double, Set<Episode>> queryEntry : queries.entrySet()) {
					initAverager(queryEntry.getKey());
					
					for (Episode query : queryEntry.getValue()) {
						int propCount = 0;
						Set<Tuple<Episode, Double>> proposals = recommender.calculateProposals(query, maxPatterns, PROPOSALS);
						
						if (proposals.size() > 0) {
							hasProposals = true;
							double maxEval = 0.0;
							
							for (Tuple<Episode, Double> tuple : proposals) {
								avgQueryProposal.get(queryEntry.getKey()).get(propCount).addValue(tuple.getSecond());
								
								double eval = recommender.calcF1(e, tuple.getFirst());
								if (eval > maxEval) {
									avgTargetProposal.get(queryEntry.getKey()).get(propCount).addValue(eval);
									maxEval = eval;
								} else {
									avgTargetProposal.get(queryEntry.getKey()).get(propCount).addValue(maxEval);
								}
								propCount++;
							}
						}
					}
				}
				if (hasProposals) {
					targetID++;
					getTargetResults(targetID, e);
					updateAveragers();
				} else {
					targetsWithoutProposals++;
				}
			}
			writeCategoryResults(categoryEntry.getKey());
			
			append("\nNumber of targets with no proposals = %d\n\n", targetsWithoutProposals);
			synthesizeResults();
			logSynthesized();
			categoryResults.clear();
			results.clear();
		}
	}
	
	private void writeCategoryResults(String fileName) {
		File filePath = new File(rootFolder.getAbsolutePath() + "/" + fileName + ".txt");
		String content = sb.toString();
		
		try {
			FileUtils.writeStringToFile(filePath, content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Map<String, Set<Episode>> categorizeTargets(Set<Episode> validationData) {
		Map<String, Set<Episode>> categories = new LinkedHashMap<String, Set<Episode>>();
		categories.put("0-1", Sets.newHashSet());
		categories.put("2", Sets.newHashSet());
		categories.put("3", Sets.newHashSet());
		categories.put("4", Sets.newHashSet());
		categories.put("5", Sets.newHashSet());
		categories.put("6-9", Sets.newHashSet());
		categories.put("10-19", Sets.newHashSet());
		categories.put("20-29", Sets.newHashSet());
		categories.put("30-66", Sets.newHashSet());
		
		for (Episode target : validationData) {
			int numInvoc = target.getNumEvents() - 1;
			
			if (numInvoc == 0 || numInvoc == 1) {
				categories.get("0-1").add(target);
				continue;
			}
			if (numInvoc == 2) {
				categories.get("2").add(target);
				continue;
			}
			if (numInvoc == 3) {
				categories.get("3").add(target);
				continue;
			}
			if (numInvoc == 4) {
				categories.get("4").add(target);
				continue;
			}
			if (numInvoc == 5) {
				categories.get("5").add(target);
				continue;
			}
			if (numInvoc > 5 && numInvoc < 10) {
				categories.get("6-9").add(target);
				continue;
			}
			if (numInvoc > 9 && numInvoc < 20) {
				categories.get("10-19").add(target);
				continue;
			}
			if (numInvoc > 19 && numInvoc < 30) {
				categories.get("20-29").add(target);
				continue;
			}
			if (numInvoc > 29) {
				categories.get("30-66").add(target);
				continue;
			}
		}
		return categories;
	}

	private void logSynthesized() {
		for (int p = 0; p < PROPOSALS; p++) {
			append("\tTop%d", (p + 1));
		}
		append("\n");
		for (Map.Entry<Double, List<Tuple<Double, Double>>> entry : categoryResults.entrySet()) {
			append("Removed %s\t", df.format(entry.getKey()));
			for (Tuple<Double, Double> pairs : entry.getValue()) {
				append("<%s, %s>\t", df.format(pairs.getFirst()), df.format(pairs.getSecond()));
			}
			append("\n");
		}
	}

	private void synthesizeResults() {
		Map<Double, List<Tuple<Averager, Averager>>> categoryAverager = new HashMap<Double, List<Tuple<Averager, Averager>>>();
		
		//initialize
		for (double p = 0.25; p < 0.8; p += 0.25) {
			List<Tuple<Averager, Averager>> queryTarget = new LinkedList<Tuple<Averager, Averager>>();
			
			for (int ind = 0; ind < PROPOSALS; ind++) {
				queryTarget.add(Tuple.newTuple(new Averager(), new Averager()));
			}
			categoryAverager.put(p, queryTarget);
		}
		
		//synthesize
		for (int ind = 0; ind < results.size(); ind++) {
			Map<Double, List<Tuple<Double, Double>>> targetResults = results.get(ind).getResults();
			for (Map.Entry<Double, List<Tuple<Double, Double>>> entry : targetResults.entrySet()) {
				for (int p = 0; p < entry.getValue().size(); p++) {
					double qpAvg = targetResults.get(entry.getKey()).get(p).getFirst();
					double tpAvg = targetResults.get(entry.getKey()).get(p).getSecond();
					categoryAverager.get(entry.getKey()).get(p).getFirst().addValue(qpAvg);
					categoryAverager.get(entry.getKey()).get(p).getSecond().addValue(tpAvg);
				}
			}
		}
		
		//average
		for (Map.Entry<Double, List<Tuple<Averager, Averager>>> qsEntry : categoryAverager.entrySet()) {
			List<Tuple<Double, Double>> avgs = new LinkedList<Tuple<Double, Double>>();
			
			for (Tuple<Averager, Averager> ps : qsEntry.getValue()) {
				avgs.add(Tuple.newTuple(ps.getFirst().average(), ps.getSecond().average()));
			}
			categoryResults.put(qsEntry.getKey(), avgs);
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
	
	private void getTargetResults(int targetID, Episode target) {
		ProposalResults episodeResults = new ProposalResults();
		episodeResults.setTarget(target);
		
		sb.append("Target query " + targetID + "\t");
		for (Map.Entry<Double, List<Averager>> entry : avgQueryProposal.entrySet()) {
			
			sb.append(df.format(entry.getKey()) + ": [ ");
			for (int p = 0; p < PROPOSALS; p++) {
				double qp = entry.getValue().get(p).average();
				
				if (qp > 0.0) {
					double tp = avgTargetProposal.get(entry.getKey()).get(p).average();
					episodeResults.addResult(entry.getKey(), qp, tp);
					sb.append("<" + df.format(qp) + ", " + df.format(tp) + ">; ");
				}
			}
			sb.append("]\t");
		}
		sb.append(target.getNumEvents() - 1 + "\n");
		results.add(episodeResults);
	}

	private void configurations() {
		append("\n");
		append("%% - Patterns configuration:\n");
		append("%% - Frequency = %d\n", FREQUENCY);
		append("%% - Bidirectional measure = %s\n", df.format(BIDIRECTIONAL));
		append("%% - Querying strategy = [25%%, 50%%, 75%%]\n");
		append("%% - Proposal strategy = %d\n", PROPOSALS);
		append("%% - Similarity metric = F1-value\n\n");
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
