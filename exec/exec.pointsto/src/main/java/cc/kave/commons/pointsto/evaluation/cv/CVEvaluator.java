/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.evaluation.cv;

import static cc.kave.commons.pointsto.evaluation.Logger.log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.inject.Inject;
import com.google.inject.Provider;

import cc.recommenders.datastructures.Tuple;
import cc.recommenders.evaluation.data.Measure;
import cc.recommenders.evaluation.queries.QueryBuilder;
import cc.recommenders.evaluation.queries.QueryBuilderFactory;
import cc.recommenders.mining.calls.ICallsRecommender;
import cc.recommenders.mining.calls.pbn.PBNMiner;
import cc.recommenders.names.IMethodName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

public class CVEvaluator {

	private Provider<PBNMiner> pbnMinerProvider;
	private QueryBuilderFactory queryBuilderFactory;
	private ExecutorService executorService;

	@Inject
	public CVEvaluator(Provider<PBNMiner> pbnMinerProvider, QueryBuilderFactory queryBuilderFactory,
			ExecutorService executorService) {
		this.pbnMinerProvider = pbnMinerProvider;
		this.queryBuilderFactory = queryBuilderFactory;
		this.executorService = executorService;
	}

	public double evaluate(List<List<Usage>> folds) {
		int numFolds = folds.size();
		List<Future<Pair<Integer, Double>>> futures = new ArrayList<>(numFolds);
		double[] evaluationResults = new double[numFolds];

		for (int i = 0; i < numFolds; ++i) {
			futures.add(executorService.submit(new FoldEvaluation(i, folds)));
		}

		for (int i = 0; i < evaluationResults.length; ++i) {
			try {
				Pair<Integer, Double> result = futures.get(i).get();
				evaluationResults[i] = result.getValue();
				log("\tFold %d: %.3f\n", i + 1, evaluationResults[i]);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				return Double.NaN;
			}

		}

		log("\tFold size σ: %.1f\n", calcFoldSizeStdDev(folds));

		return StatUtils.mean(evaluationResults);
	}

	private <T> double calcFoldSizeStdDev(List<List<T>> folds) {
		DescriptiveStatistics statistics = new DescriptiveStatistics();
		for (List<T> fold : folds) {
			statistics.addValue(fold.size());
		}

		return statistics.getStandardDeviation();
	}

	private static List<Usage> getTraining(int validationIndex, List<List<Usage>> folds) {
		int numFolds = folds.size();
		List<Usage> training = new ArrayList<>((numFolds - 1) * folds.get(0).size());

		for (int i = 0; i < numFolds; ++i) {
			if (i != validationIndex) {
				training.addAll(folds.get(i));
			}
		}

		return training;
	}

	private static Set<IMethodName> getExpectation(Usage validationUsage, Query q) {
		Set<CallSite> missingCallsites = new HashSet<>(validationUsage.getReceiverCallsites());
		missingCallsites.removeAll(q.getAllCallsites());

		Set<IMethodName> expectation = new HashSet<>(missingCallsites.size());
		for (CallSite callsite : missingCallsites) {
			expectation.add(callsite.getMethod());
		}
		return expectation;
	}

	private static Set<IMethodName> getProposals(ICallsRecommender<Query> recommender, Query q) {
		Set<Tuple<IMethodName, Double>> recommendations = recommender.query(q);
		Set<IMethodName> proposals = new HashSet<>(recommendations.size());
		for (Tuple<IMethodName, Double> rec : recommendations) {
			proposals.add(rec.getFirst());
		}
		return proposals;
	}

	private class FoldEvaluation implements Callable<Pair<Integer, Double>> {

		private int validationFoldIndex;
		private List<List<Usage>> folds;

		public FoldEvaluation(int validationFoldIndex, List<List<Usage>> folds) {
			this.validationFoldIndex = validationFoldIndex;
			this.folds = folds;
		}

		@Override
		public Pair<Integer, Double> call() throws Exception {
			List<Usage> training = getTraining(validationFoldIndex, folds);
			List<Usage> validation = folds.get(validationFoldIndex);

			PBNMiner pbnMiner = pbnMinerProvider.get();
			ICallsRecommender<Query> recommender = pbnMiner.createRecommender(training);
			DescriptiveStatistics statistics = new DescriptiveStatistics();

			for (Usage validationUsage : validation) {
				QueryBuilder<Usage, Query> queryBuilder = queryBuilderFactory.get();
				List<Query> queries;
				// PartialUsageQueryBuilder is not thread safe due to the static Random in Collections.shuffle
				synchronized (queryBuilder) {
					queries = queryBuilder.createQueries(validationUsage);
				}

				for (Query q : queries) {
					Set<IMethodName> expectation = getExpectation(validationUsage, q);
					Set<IMethodName> proposals = getProposals(recommender, q);
					Measure measure = Measure.newMeasure(expectation, proposals);
					statistics.addValue(measure.getF1());
				}
			}

			return ImmutablePair.of(validationFoldIndex, statistics.getMean());
		}

	}

}
