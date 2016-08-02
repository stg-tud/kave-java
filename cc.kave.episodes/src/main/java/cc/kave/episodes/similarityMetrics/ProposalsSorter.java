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
package cc.kave.episodes.similarityMetrics;

import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.mining.evaluation.ProposalHelper;
import cc.kave.episodes.model.Episode;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.evaluation.data.Measure;

public class ProposalsSorter {

	public Set<Tuple<Episode, Double>> sort(Episode query, Set<Episode> patterns, Metrics metric)
			throws Exception {
		Set<Tuple<Episode, Double>> proposals = ProposalHelper.createEpisodesSortedSet();

		for (Episode p : patterns) {
			double sim = getMetric(metric, query, p);
			proposals.add(Tuple.newTuple(p, sim));
		}
		return proposals;
	}

	private double getMetric(Metrics metric, Episode query, Episode episode) throws Exception {
		if (metric == Metrics.F1_FACTS) {
			return calcF1Facts(query, episode);
		} else if (metric == Metrics.F1_EVENTS) {
			return calcF1Events(query, episode);
		} else if (metric == Metrics.MAPO) {
			return calcMapo(query, episode);
		} else if (metric == Metrics.LEVENSTEIN) {
			return calcLevenstein(query, episode);
		} else {
			throw new Exception("This metric is not available!");
		}
	}

	private double calcF1Facts(Episode query, Episode episode) {
		Measure m = Measure.newMeasure(query.getFacts(), episode.getFacts());
		double f1 = m.getF1();
		return f1;
	}
	
	private double calcF1Events(Episode query, Episode episode) {
		Measure m = Measure.newMeasure(query.getEvents(), episode.getEvents());
		double f1 = m.getF1();
		return f1;
	}
	
	private double calcMapo(Episode query, Episode episode) {
		Set<Fact> conjunction = getConjunctionFacts(query, episode);
		Set<Fact> disjunction = getDisjunctionFacts(query, episode);
		
		return fract(conjunction.size(), disjunction.size());
	}
	
	private double calcLevenstein(Episode query, Episode episode) {
		int deletions = getDeletions(query, episode);
		int additions = getAdditions(query, episode);
		return -1 * (deletions + additions);
	}
	
	private int getAdditions(Episode query, Episode episode) {
		int adds = 0;
		Set<Fact> queryFacts = query.getFacts();
		
		for (Fact fact : episode.getFacts()) {
			if (!queryFacts.contains(fact)) {
				adds++;
			}
		}
		return adds;
	}

	private int getDeletions(Episode query, Episode episode) {
		int diffs = 0;
		Set<Fact> episodeFacts = episode.getFacts();
		
		for (Fact fact : query.getFacts()) {
			if (!episodeFacts.contains(fact)) {
				diffs++;
			}
		}
		return diffs;
	}

	private Set<Fact> getDisjunctionFacts(Episode query, Episode episode) {
		Set<Fact> disjunction = Sets.newHashSet();
		Set<Fact> queryFacts = query.getFacts();
		Set<Fact> episodeFacts = episode.getFacts();
		
		disjunction.addAll(queryFacts);
		disjunction.addAll(episodeFacts);
		return disjunction;
	}

	private Set<Fact> getConjunctionFacts(Episode query, Episode episode) {
		Set<Fact> conjuction = Sets.newHashSet();
		Set<Fact> queryFacts = query.getFacts();
		Set<Fact> episodeFacts = episode.getFacts();
		
		for (Fact fact : episodeFacts) {
			if (queryFacts.contains(fact)) {
				conjuction.add(fact);
			}
		}
		return conjuction;
	}

	private Double fract(double numerator, double denominator) {
		return numerator / denominator;
	}
}
