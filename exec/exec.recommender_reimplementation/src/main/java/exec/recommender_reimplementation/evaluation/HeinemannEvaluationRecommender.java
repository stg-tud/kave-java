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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;
import exec.recommender_reimplementation.heinemann_analysis.Entry;
import exec.recommender_reimplementation.heinemann_analysis.HeinemannQuery;
import exec.recommender_reimplementation.heinemann_analysis.HeinemannQueryExtractor;
import exec.recommender_reimplementation.heinemann_analysis.HeinemannRecommender;
import exec.recommender_reimplementation.heinemann_analysis.IndexExtractor;

public class HeinemannEvaluationRecommender extends EvaluationRecommender {

	private static final int HEINEMANN_LOOKBACK = 5;

	private static final double HEINEMANN_MINIMUM_PROBABILITY = 0.8;

	private Map<ITypeName, Set<Entry>> heinemannModel;

	private HeinemannQueryExtractor heinemannQueryExtractor;

	private HeinemannRecommender heinemannRecommender;

	public HeinemannEvaluationRecommender() {
		heinemannModel = new HashMap<>();
	}

	@Override
	public String getName() {
		return "Heinemann";
	}

	@Override
	public void analysis(List<Context> contextList) {
		Map<ITypeName, Set<Entry>> model = IndexExtractor.buildModel(contextList, HEINEMANN_LOOKBACK, false, false);
		mergeModel(model);
	}

	private void mergeModel(Map<ITypeName, Set<Entry>> model) {
		for (java.util.Map.Entry<ITypeName, Set<Entry>> entry : model.entrySet()) {
			if (heinemannModel.containsKey(entry.getKey())) {
				heinemannModel.get(entry.getKey()).addAll(entry.getValue());
			} else {
				heinemannModel.put(entry.getKey(), entry.getValue());
			}
		}
	}

	@Override
	public void initalizeRecommender() {
		heinemannRecommender = new HeinemannRecommender(heinemannModel, HEINEMANN_MINIMUM_PROBABILITY);
		heinemannQueryExtractor = new HeinemannQueryExtractor();
	}

	@Override
	public Set<Tuple<ICoReMethodName, Double>> handleQuery(QueryContext query) {
		HeinemannQuery heinemannQuery = heinemannQueryExtractor.extractQueryFromCompletion(query.getCompletionEvent(), HEINEMANN_LOOKBACK,
				false, false);
		Set<Tuple<ICoReMethodName, Double>> proposals = heinemannRecommender.query(heinemannQuery);
		return proposals;
	}

	@Override
	public boolean supportsAnalysis() {
		return true;
	}

}
