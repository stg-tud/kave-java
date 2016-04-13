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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import cc.kave.episodes.evaluation.queries.QueryStrategy;
import cc.kave.episodes.model.Averager;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.TargetResults;
import cc.recommenders.datastructures.Tuple;

public class Synthesizer {

	private EpisodeRecommender recommender;
	private QueryStrategy queryGenerator;

	@Inject
	public Synthesizer(EpisodeRecommender recommender, QueryStrategy queryGenerator) {
		this.recommender = recommender;
		this.queryGenerator = queryGenerator;
	}

	public TargetResults getTargetResults(Episode target, Map<Integer, Set<Episode>> episodes, int numberOfProposals) {
		TargetResults targetResults = new TargetResults();
		targetResults.setTarget(target);

		Map<Double, List<Averager>> avgQueryProposal = new HashMap<Double, List<Averager>>();
		Map<Double, List<Averager>> avgTargetProposal = new HashMap<Double, List<Averager>>();

		if (target.getNumEvents() < 3) {
			return targetResults;
		}
		Map<Double, Set<Episode>> queries = queryGenerator.byPercentage(target);
		if (target.getNumEvents() > 11) {
			queries.putAll(queryGenerator.byNumber(target));
		}
		for (Map.Entry<Double, Set<Episode>> queryEntry : queries.entrySet()) {
			avgQueryProposal.put(queryEntry.getKey(), new LinkedList<Averager>());
			avgTargetProposal.put(queryEntry.getKey(), new LinkedList<Averager>());

			for (Episode query : queryEntry.getValue()) {
				Set<Tuple<Episode, Double>> proposals = recommender.getProposals(query, episodes, numberOfProposals);

				if (proposals.size() == 0) {
					continue;
				}
				if (avgQueryProposal.get(queryEntry.getKey()).size() < proposals.size()) {
					for (int p = 0; p < proposals.size(); p++) {
						avgQueryProposal.get(queryEntry.getKey()).add(new Averager());
						avgTargetProposal.get(queryEntry.getKey()).add(new Averager());
					}
				}
				int propCount = 0;
				double maxEval = 0.0;
				for (Tuple<Episode, Double> tuple : proposals) {
					avgQueryProposal.get(queryEntry.getKey()).get(propCount).addValue(tuple.getSecond());
					double eval = recommender.calcPrecision(target, tuple.getFirst());
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
		return avgTargetResults(target, avgQueryProposal, avgTargetProposal);
	}

	private TargetResults avgTargetResults(Episode target, Map<Double, List<Averager>> avgQueryProposal,
			Map<Double, List<Averager>> avgTargetProposal) {
		TargetResults targetResults = new TargetResults();
		targetResults.setTarget(target);

		for (Map.Entry<Double, List<Averager>> entry : avgQueryProposal.entrySet()) {
			for (int p = 0; p < entry.getValue().size(); p++) {
				double qp = entry.getValue().get(p).average();
				double tp = avgTargetProposal.get(entry.getKey()).get(p).average();
				targetResults.addResult(entry.getKey(), qp, tp);
			}
		}
		return targetResults;
	}
}
