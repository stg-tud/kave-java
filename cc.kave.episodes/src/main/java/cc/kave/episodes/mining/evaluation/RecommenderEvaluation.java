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

import static cc.kave.assertions.Asserts.assertTrue;
import static cc.recommenders.io.Logger.append;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.math.util.MathUtils;
import org.apache.mahout.math.Arrays;

import cc.kave.episodes.evaluation.queries.QueryStrategy;
import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.ValidationContextsParser;
import cc.kave.episodes.model.Averager;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.TargetResults;
import cc.kave.episodes.model.TargetsCategorization;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.postprocessor.MaximalEpisodes;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class RecommenderEvaluation {

	private File rootFolder;

	private static final int PROPOSALS = 10;
	private static final int FREQUENCY = 5;
	private static final double BIDIRECTIONAL = 0.01;

	private ValidationContextsParser validationParser;
	private EventStreamIo streamIo;
	private QueryStrategy queryGenerator;
	private EpisodeRecommender recommender;
	private EpisodesParser episodeParser;
	private MaximalEpisodes maxEpisodeTracker;
	private TargetsCategorization categorizer;

	private static final double[] percentages = new double[] { 0.10, 0.20, 0.30, 0.40, 0.50, 0.60, 0.70, 0.80, 0.90 };
	Map<Double, Set<Episode>> queries = new LinkedHashMap<Double, Set<Episode>>();

	private Map<Double, List<Averager>> avgQueryProposal = new HashMap<Double, List<Averager>>();
	private Map<Double, List<Averager>> avgTargetProposal = new HashMap<Double, List<Averager>>();

	private List<TargetResults> results = new LinkedList<TargetResults>();

	private Map<Double, List<Tuple<Double, Double>>> categoryResults = new LinkedHashMap<Double, List<Tuple<Double, Double>>>();

	private StringBuilder sb;

	@Inject
	public RecommenderEvaluation(@Named("evaluation") File directory, ValidationContextsParser parser,
			EventStreamIo streamIo, QueryStrategy queryGenerator, EpisodeRecommender recommender,
			EpisodesParser episodeParser, MaximalEpisodes maxEpisodeTracker, TargetsCategorization categorizer) {

		assertTrue(directory.exists(), "Evaluations folder does not exist");
		assertTrue(directory.isDirectory(), "Evaluations folder is not a folder, but a file");
		this.rootFolder = directory;
		this.validationParser = parser;
		this.streamIo = streamIo;
		this.queryGenerator = queryGenerator;
		this.recommender = recommender;
		this.episodeParser = episodeParser;
		this.maxEpisodeTracker = maxEpisodeTracker;
		this.categorizer = categorizer;
	}

	public void evaluate(int numbRepos) throws ZipException, IOException {

		Logger.setPrinting(true);
		Map<Integer, Set<Episode>> maxPatterns = readPatterns();
		List<Event> eventMapping = readMapper(numbRepos);
		Set<Episode> validationData = readValidationData(eventMapping);
		Map<String, Set<Episode>> targets = categorizer.categorize(validationData);
		configurations();

		for (Map.Entry<String, Set<Episode>> categoryEntry : targets.entrySet()) {
			if (categoryEntry.getKey().equalsIgnoreCase("0-1")) {
				continue;
			}
			sb = new StringBuilder();
			int targetID = 0;
			int noProposals = 0;
			Logger.log("Generating queries for episodes with %s number of invocations\n", categoryEntry.getKey());
			for (Episode e : categoryEntry.getValue()) {
				// append("%d - %d; ", targetID, e.getNumEvents() - 1);
				boolean hasProposals = false;
				// Logger.log("Start query generations 1");
				queries = queryGenerator.byPercentage(e);
				// Logger.log("End query generator 1");
				if (e.getNumEvents() > 11) {
					// Logger.log("Start query generations 2");
					queries.putAll(queryGenerator.byNumber(e));
					// Logger.log("End query generator 2");
				}
				// Logger.log("Start recommender");
				for (Map.Entry<Double, Set<Episode>> queryEntry : queries.entrySet()) {
					initQueryAverager(queryEntry.getKey());

					for (Episode query : queryEntry.getValue()) {
						int propCount = 0;
						Set<Tuple<Episode, Double>> proposals = recommender.getProposals(query, maxPatterns, PROPOSALS);
						if (proposals.size() == 0) {
							continue;
						}
						hasProposals = true;
						double maxEval = 0.0;
						for (Tuple<Episode, Double> tuple : proposals) {
							avgQueryProposal.get(queryEntry.getKey()).get(propCount).addValue(tuple.getSecond());
							double eval = recommender.calcPrecision(e, tuple.getFirst());
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
				// Logger.log("End recommender");
				if (hasProposals) {
					targetID++;
					getTargetResults(targetID, e);
					avgQueryProposal.clear();
					avgTargetProposal.clear();
				} else {
					noProposals++;
				}
			}
			// Logger.log("\n");
			writeCategoryResults(categoryEntry.getKey());

			append("Number of targets with no proposals = %d\n\n", noProposals);
			synthesizeResults(categoryEntry.getKey());
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

	private void logSynthesized() {
		for (int p = 0; p < PROPOSALS; p++) {
			append("\tTop%d", (p + 1));
		}
		append("\n");
		for (Map.Entry<Double, List<Tuple<Double, Double>>> entry : categoryResults.entrySet()) {
			if (entry.getValue().get(0).getFirst() == 0.0 || entry.getValue().get(0).getSecond() == 0.0) {
				continue;
			}
			append("Removed %.2f\t", (entry.getKey()));
			for (Tuple<Double, Double> pairs : entry.getValue()) {
				append("<%.2f, %.2f>\t", pairs.getFirst(), pairs.getSecond());
			}
			append("\n");
		}
	}

	private void synthesizeResults(String category) {
		Map<Double, List<Tuple<Averager, Averager>>> categoryAverager = new LinkedHashMap<Double, List<Tuple<Averager, Averager>>>();

		// initialize
		for (double percent : percentages) {
			List<Tuple<Averager, Averager>> percAvg = new LinkedList<Tuple<Averager, Averager>>();
			for (int propId = 0; propId < PROPOSALS; propId++) {
				percAvg.add(Tuple.newTuple(new Averager(), new Averager()));
			}
			categoryAverager.put(MathUtils.round(percent, 2), percAvg);
		}
		if (category.length() == 5) {
			int range = Integer.parseInt(category.substring(3));
			if (range > 10) {
				for (double p = 1.0; p < range; p += 1.0) {
					List<Tuple<Averager, Averager>> percAvg = new LinkedList<Tuple<Averager, Averager>>();
					for (int propId = 0; propId < PROPOSALS; propId++) {
						percAvg.add(Tuple.newTuple(new Averager(), new Averager()));
					}
					categoryAverager.put(MathUtils.round(p, 2), percAvg);
				}
			}
		}

		// synthesize
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

		// average
		for (Map.Entry<Double, List<Tuple<Averager, Averager>>> qsEntry : categoryAverager.entrySet()) {
			List<Tuple<Double, Double>> avgs = new LinkedList<Tuple<Double, Double>>();

			for (Tuple<Averager, Averager> ps : qsEntry.getValue()) {
				avgs.add(Tuple.newTuple(ps.getFirst().average(), ps.getSecond().average()));
			}
			categoryResults.put(qsEntry.getKey(), avgs);
		}
	}

	private void initQueryAverager(double percent) {
		avgQueryProposal.put(percent, new LinkedList<Averager>());
		avgTargetProposal.put(percent, new LinkedList<Averager>());

		for (int idx = 0; idx < PROPOSALS; idx++) {
			avgQueryProposal.get(percent).add(new Averager());
			avgTargetProposal.get(percent).add(new Averager());
		}
	}

	private void getTargetResults(int targetID, Episode target) {
		TargetResults episodeResults = new TargetResults();
		episodeResults.setTarget(target);

		sb.append("Target query " + targetID + "\t");
		for (Map.Entry<Double, List<Averager>> entry : avgQueryProposal.entrySet()) {
			sb.append(MathUtils.round(entry.getKey(), 2) + ": [ ");
			for (int p = 0; p < PROPOSALS; p++) {
				double qp = entry.getValue().get(p).average();

				if (qp > 0.0) {
					double tp = avgTargetProposal.get(entry.getKey()).get(p).average();
					episodeResults.addResult(entry.getKey(), qp, tp);
					sb.append("<" + MathUtils.round(qp, 2) + ", " + MathUtils.round(tp, 2) + ">; ");
				}
			}
			sb.append("]\t");
		}
		sb.append(target.getNumEvents() - 1 + "\n");
		results.add(episodeResults);
	}

	private void configurations() {
		append("\n");
		append("%% - Evaluations configuration:\n");
		append("%% - Frequency = %d\n", FREQUENCY);
		append("%% - Bidirectional measure = %s\n", MathUtils.round(BIDIRECTIONAL, 2));
		append("%% - Querying strategy = %s\n", Arrays.toString(percentages));
		append("%% - Proposal strategy = %d\n", PROPOSALS);
		append("%% - Similarity metric = F1-value\n");
		append("%% - Number of maximal queries = all combinations\n\n");
	}

	private Set<Episode> readValidationData(List<Event> eventMapping) throws ZipException, IOException {
		Logger.log("Readng the validation data\n");
		Set<Episode> validationData = validationParser.parse(eventMapping);
		return validationData;
	}

	private List<Event> readMapper(int frequency) {
		Logger.log("Reading the mapping file");
		List<Event> eventMapping = streamIo.readMapping(frequency);
		return eventMapping;
	}

	private Map<Integer, Set<Episode>> readPatterns() {
		Logger.log("Reading the learned patterns");
		Map<Integer, Set<Episode>> patterns = episodeParser.parse(EpisodeType.GENERAL, 100, 0);
		Map<Integer, Set<Episode>> maxPatterns = maxEpisodeTracker.getMaximalEpisodes(patterns);
		return maxPatterns;
	}
}
