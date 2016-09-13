/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.recommender_reimplementation.evaluation;

import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;
import exec.recommender_reimplementation.raychev_analysis.RaychevRecommender;

public class RaychevEvaluationRecommender extends EvaluationRecommender {

	private RaychevRecommender raychevRecommender;

	private static final String RAYCHEV_ANALYSIS_SET = "superputty";

	@Override
	public String getName() {
		return "Raychev";
	}

	@Override
	public void analysis(List<Context> contextList) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void initalizeRecommender() {
		raychevRecommender = new RaychevRecommender();
	}

	@Override
	public Set<Tuple<ICoReMethodName, Double>> handleQuery(QueryContext query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supportsAnalysis() {
		return false;
	}
}
