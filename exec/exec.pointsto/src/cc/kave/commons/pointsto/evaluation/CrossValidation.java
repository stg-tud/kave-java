/**
 * Copyright 2015 Simon Reuß
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
import java.util.Collections;
import java.util.List;
import java.util.Random;

//import cc.recommenders.evaluation.data.Boxplot;
//import cc.recommenders.evaluation.evaluators.F1Evaluator;
//import cc.recommenders.mining.calls.ICallsRecommender;
//import cc.recommenders.mining.calls.pbn.PBNMiner;
//import cc.recommenders.usages.Query;
//import cc.recommenders.usages.Usage;

public class CrossValidation {

//	private final int numFolds;
//	private Random rnd;
//	
//	private final PBNMiner miner;
//	private final F1Evaluator evaluator;
//
//	public CrossValidation(int numFolds, Random rnd, PBNMiner miner, F1Evaluator evaluator) {
//		this.numFolds = numFolds;
//		this.rnd = rnd;
//		this.miner = miner;
//		this.evaluator = evaluator;
//	}
//
//	public Boxplot run(List<Usage> usages) {
//		evaluator.reinit();
//		List<List<Usage>> folds = createFolds(usages);
//		
//		for (int i = 0; i < numFolds; ++i) {
//			ICallsRecommender<Query> rec = miner.createRecommender(getTrainingData(i, folds));
//			evaluator.query(rec, folds.get(i));
//		}
//		
//		return evaluator.getResults();
//	}
//	
//	protected List<Usage> getTrainingData(int validationIndex,List<List<Usage>> folds) {
//		List<Usage> trainingData = new ArrayList<>();
//		for (int i = 0; i < numFolds; ++i) {
//			if (i != validationIndex) {
//				trainingData.addAll(folds.get(i));
//			}
//		}
//		
//		return trainingData;
//	}
//
//	protected List<List<Usage>> createFolds(List<Usage> usages) {
//		usages = new ArrayList<>(usages);
//		Collections.shuffle(usages, rnd);
//
//		int expectedUsagesPerFold = usages.size() / numFolds;
//		List<List<Usage>> folds = new ArrayList<>(numFolds);
//		int nextStartIndex = 0;
//		for (int i = 0; i < numFolds; ++i) {
//			List<Usage> fold = new ArrayList<>(usages.subList(nextStartIndex, nextStartIndex + expectedUsagesPerFold));
//			folds.add(fold);
//			nextStartIndex += expectedUsagesPerFold;
//		}
//
//		// add rest to last fold
//		folds.get(folds.size() - 1).addAll(usages.subList(nextStartIndex, usages.size()));
//
//		return folds;
//	}

}
