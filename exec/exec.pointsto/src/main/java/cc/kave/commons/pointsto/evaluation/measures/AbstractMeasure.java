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
package cc.kave.commons.pointsto.evaluation.measures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import cc.recommenders.datastructures.Tuple;
import cc.recommenders.mining.calls.ICallsRecommender;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.Query;

public abstract class AbstractMeasure {

	protected List<ICoReMethodName> getProposals(ICallsRecommender<Query> recommender, Query q) {
		List<Tuple<ICoReMethodName, Double>> recommendations = new ArrayList<>(recommender.query(q));
		recommendations.sort(Comparator.comparingDouble(Tuple<ICoReMethodName, Double>::getSecond).reversed());
		List<ICoReMethodName> proposals = new ArrayList<>(recommendations.size());
		for (Tuple<ICoReMethodName, Double> rec : recommendations) {
			proposals.add(rec.getFirst());
		}
		return proposals;
	}
	
	public abstract double calculate(ICallsRecommender<Query> recommender, Query q, Set<ICoReMethodName> expectation);
}
