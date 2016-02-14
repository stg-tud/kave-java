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
package cc.kave.commons.pointsto.evaluation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.inject.Inject;

import cc.recommenders.datastructures.Tuple;
import cc.recommenders.evaluation.data.Measure;
import cc.recommenders.evaluation.queries.QueryBuilderFactory;
import cc.recommenders.mining.calls.ICallsRecommender;
import cc.recommenders.mining.calls.pbn.PBNMiner;
import cc.recommenders.names.IMethodName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

import static cc.kave.commons.pointsto.evaluation.Logger.log;

public class CVEvaluator {

	private PBNMiner pbnMiner;
	private QueryBuilderFactory queryBuilderFactory;

	@Inject
	public CVEvaluator(PBNMiner pbnMiner, QueryBuilderFactory queryBuilderFactory) {
		this.pbnMiner = pbnMiner;
		this.queryBuilderFactory = queryBuilderFactory;
	}

	public double evaluate(List<List<Usage>> folds) {
		int numFolds = folds.size();
		double[] evaluationResults = new double[numFolds];

		for (int i = 0; i < numFolds; ++i) {
			List<Usage> validation = folds.get(i);
			List<Usage> training = getTraining(i, folds);

			evaluationResults[i] = evaluateFold(training, validation);
			log("\tFold %d: %.3f\n", i + 1, evaluationResults[i]);
		}

		return StatUtils.mean(evaluationResults);
	}

	private List<Usage> getTraining(int validationIndex, List<List<Usage>> folds) {
		int numFolds = folds.size();
		List<Usage> training = new ArrayList<>((numFolds - 1) * folds.get(0).size());

		for (int i = 0; i < numFolds; ++i) {
			if (i != validationIndex) {
				training.addAll(folds.get(i));
			}
		}

		return training;
	}

	private double evaluateFold(List<Usage> training, List<Usage> validation) {
		ICallsRecommender<Query> recommender = pbnMiner.createRecommender(training);
		DescriptiveStatistics statistics = new DescriptiveStatistics();

		for (Usage validationUsage : validation) {
			List<Query> queries = queryBuilderFactory.get().createQueries(validationUsage);
			for (Query q : queries) {
				Set<IMethodName> expectation = getExpectation(validationUsage, q);
				Set<IMethodName> proposals = getProposals(recommender, q);
				Measure measure = Measure.newMeasure(expectation, proposals);
				statistics.addValue(measure.getF1());
			}
		}

		return statistics.getMean();
	}

	private Set<IMethodName> getExpectation(Usage validationUsage, Query q) {
		Set<CallSite> missingCallsites = new HashSet<>(validationUsage.getReceiverCallsites());
		missingCallsites.removeAll(q.getAllCallsites());

		Set<IMethodName> expectation = new HashSet<>(missingCallsites.size());
		for (CallSite callsite : missingCallsites) {
			expectation.add(callsite.getMethod());
		}
		return expectation;
	}

	private Set<IMethodName> getProposals(ICallsRecommender<Query> recommender, Query q) {
		Set<Tuple<IMethodName, Double>> recommendations = recommender.query(q);
		Set<IMethodName> proposals = new HashSet<>(recommendations.size());
		for (Tuple<IMethodName, Double> rec : recommendations) {
			proposals.add(rec.getFirst());
		}
		return proposals;
	}

}
