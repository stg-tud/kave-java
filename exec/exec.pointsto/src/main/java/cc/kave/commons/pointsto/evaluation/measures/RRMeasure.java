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

import java.util.List;
import java.util.Set;

import cc.recommenders.mining.calls.ICallsRecommender;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.Query;

/**
 * Calculates the reciprocal rank.
 */
public class RRMeasure extends AbstractMeasure {

	@Override
	public double calculate(ICallsRecommender<Query> recommender, Query q, Set<ICoReMethodName> expectation) {
		if (expectation.size() != 1) {
			throw new IllegalArgumentException("expectation must be a single method name");
		}

		return calculate(recommender, q, expectation.iterator().next());
	}

	public double calculate(ICallsRecommender<Query> recommender, Query q, ICoReMethodName expectation) {
		int rank = getRank(expectation, getProposals(recommender, q));
		if (rank == 0) {
			return 0.0;
		} else {
			return 1.0 / rank;
		}
	}

	private int getRank(ICoReMethodName expectedMethod, List<ICoReMethodName> proposals) {
		for (int r = 0; r < proposals.size(); ++r) {
			ICoReMethodName method = proposals.get(r);
			if (expectedMethod.equals(method)) {
				return r + 1;
			}
		}
		return 0;
	}

}
