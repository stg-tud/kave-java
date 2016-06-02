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

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import cc.recommenders.evaluation.data.Measure;
import cc.recommenders.mining.calls.ICallsRecommender;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.Query;

public class F1Measure extends AbstractMeasure {

	@Override
	public double calculate(ICallsRecommender<Query> recommender, Query q, Set<ICoReMethodName> expectation) {
		Set<ICoReMethodName> proposals = ImmutableSet.copyOf(getProposals(recommender, q));
		Measure measure = Measure.newMeasure(expectation, proposals);
		return measure.getF1();
	}

}
