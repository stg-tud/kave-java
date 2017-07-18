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

import static cc.kave.assertions.Asserts.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.episodes.mining.evaluation.ProposalHelper;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ProposalsSorter {

	public Set<Tuple<Episode, Double>> sort(Episode query,
			Set<Episode> patterns, Metrics metric) throws Exception {
		assertTrue(!isEmptyEpisode(query), "Query is empty!");
		assertTrue(!patterns.isEmpty(), "Pattern list is empty!");

		Set<Tuple<Episode, Double>> proposals = ProposalHelper
				.createEpisodesSortedSet();

		for (Episode p : patterns) {
			if (!isEmptyEpisode(p)) {
				double sim = getMetric(metric, query, p);
				proposals.add(Tuple.newTuple(p, sim));
			}
		}
		return proposals;
	}

	private double getMetric(Metrics metric, Episode query, Episode pattern)
			throws Exception {

		int episodeRelations = pattern.getRelations().size();
		int episodeEvents = pattern.getNumEvents();
		Set<Fact> queryFacts = Sets.newHashSet();

		if (episodeRelations < triangleNumber(episodeEvents)) {
			queryFacts = getQueryFacts(query, pattern);
		} else {
			queryFacts = query.getFacts();
		}

		if (metric == Metrics.F1_FACTS) {
			return F1Measure.calcF1(queryFacts, pattern.getFacts());
		} else if (metric == Metrics.F1_EVENTS) {
			return F1Measure.calcF1(query.getEvents(), pattern.getEvents());
		} else if (metric == Metrics.MAPO) {
			return Mapo.calcMapo(queryFacts, pattern.getFacts());
		} else if (metric == Metrics.LEVENSTEIN) {
			return calcLevenstein(query, pattern);
		} else {
			throw new Exception("This metric is not available!");
		}
	}

	private double calcLevenstein(Episode query, Episode episode) {
		Map<Integer, Set<Fact>> qe = sortFactsInEpisode(query);
		assertTrue(qe.size() == query.getNumEvents(),
				"Query is not with strict sequential order!");
		List<Fact> queryEvents = wrapQuery(qe);
		Map<Integer, Set<Fact>> ee = sortFactsInEpisode(episode);
		List<Set<Fact>> episodeEvents = wrapEpisode(ee);

		int eventsInQuery = query.getNumEvents();
		int eventsInEpisode = episode.getNumEvents();

		int adds = 0;
		int subs = 0;
		int dels = 0;
		int eidx = 0;
		int epElems = 0;
		int qidx = 0;

		while (qidx < queryEvents.size()) {
			epElems++;
			// deletions
			if (epElems > episode.getNumEvents()) {
				dels += query.getNumEvents() - qidx;
				break;
			}
			Set<Fact> eFacts = episodeEvents.get(eidx);
			Fact qEvent = queryEvents.get(qidx);
			// substitutions
			if (!eFacts.contains(qEvent)) {
				if (episode.getEvents().contains(qEvent)) {
					adds++;
					eventsInQuery++;
					eidx++;
					continue;
				} else {
					if (eventsInQuery == eventsInEpisode) {
						subs++;
					} else {
						dels++;
						eventsInQuery--;
					}
				}
			}
			int size = episodeEvents.get(eidx).size();
			if ((size == 1) || ((eidx + size) == epElems)) {
				eidx++;
			}
			qidx++;
		}
		// additions
		while (eidx < episodeEvents.size()) {
			adds += episodeEvents.get(eidx).size();
			eidx++;
		}
		return -1 * (dels + adds + subs);
	}

	private List<Set<Fact>> wrapEpisode(Map<Integer, Set<Fact>> ee) {
		List<Set<Fact>> episode = Lists.newLinkedList();

		for (Map.Entry<Integer, Set<Fact>> entry : ee.entrySet()) {
			episode.add(entry.getValue());
		}
		return episode;
	}

	private List<Fact> wrapQuery(Map<Integer, Set<Fact>> qe) {
		List<Fact> query = Lists.newLinkedList();

		for (Map.Entry<Integer, Set<Fact>> entry : qe.entrySet()) {
			for (Fact fact : entry.getValue()) {
				query.add(fact);
				break;
			}
		}
		return query;
	}

	private Map<Integer, Set<Fact>> sortFactsInEpisode(Episode episode) {
		Map<Integer, Set<Fact>> results = Maps.newLinkedHashMap();

		Map<Fact, Set<Fact>> factRelations = getFactRelations(episode);
		Map<Integer, Set<Fact>> rankFacts = getFactsRanking(factRelations);
		Set<Tuple<Integer, Set<Fact>>> sortedFacts = sorter(rankFacts);

		for (Tuple<Integer, Set<Fact>> tuple : sortedFacts) {
			results.put(tuple.getFirst(), tuple.getSecond());
		}
		return results;
	}

	private Set<Tuple<Integer, Set<Fact>>> sorter(
			Map<Integer, Set<Fact>> rankFacts) {
		Set<Tuple<Integer, Set<Fact>>> sortedFacts = ProposalHelper
				.createFactsSortedSet();

		for (Map.Entry<Integer, Set<Fact>> entry : rankFacts.entrySet()) {
			sortedFacts.add(Tuple.newTuple(entry.getKey(), entry.getValue()));
		}
		return sortedFacts;
	}

	private Map<Integer, Set<Fact>> getFactsRanking(
			Map<Fact, Set<Fact>> factRelations) {
		Map<Integer, Set<Fact>> result = Maps.newHashMap();

		for (Map.Entry<Fact, Set<Fact>> entry : factRelations.entrySet()) {
			Fact event = entry.getKey();
			int rank = entry.getValue().size();
			if (result.containsKey(rank)) {
				result.get(rank).add(event);
			} else {
				result.put(rank, Sets.newHashSet(event));
			}
		}
		return result;
	}

	private Map<Fact, Set<Fact>> getFactRelations(Episode episode) {
		Map<Fact, Set<Fact>> result = Maps.newHashMap();

		for (Fact relation : episode.getRelations()) {
			Tuple<Fact, Fact> relFacts = relation.getRelationFacts();
			Fact firstFact = relFacts.getFirst();
			if (result.containsKey(firstFact)) {
				result.get(firstFact).add(relation);
			} else {
				result.put(firstFact, Sets.newHashSet(relation));
			}
		}
		for (Fact event : episode.getEvents()) {
			if (!result.containsKey(event)) {
				result.put(event, Sets.newHashSet());
			}
		}
		return result;
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

	private Double fract(double numerator, double denominator) {
		return numerator / denominator;
	}

	private int triangleNumber(int num) {
		if (num < 2) {
			return 0;
		} else if (num == 2) {
			return 1;
		} else {
			return (num - 1) + triangleNumber(num - 1);
		}
	}

	private Set<Fact> getQueryFacts(Episode query, Episode pattern) {
		Set<Fact> epFacts = pattern.getFacts();

		Set<Fact> queryFacts = Sets.newHashSet();
		queryFacts.addAll(query.getFacts());
		Set<Fact> queryRel = query.getRelations();

		for (Fact rel : queryRel) {
			Tuple<Fact, Fact> relFacts = rel.getRelationFacts();
			Fact order1 = new Fact(relFacts.getFirst() + ">"
					+ relFacts.getSecond());
			Fact order2 = new Fact(relFacts.getSecond() + ">"
					+ relFacts.getFirst());
			if (epFacts.contains(relFacts.getFirst())
					&& epFacts.contains(relFacts.getSecond())
					&& !(epFacts.contains(order1) || epFacts.contains(order2))) {
				queryFacts.remove(rel);
			}
		}
		return queryFacts;
	}

	private boolean isEmptyEpisode(Episode episode) {
		if (episode.getNumFacts() == 0) {
			return true;
		} else {
			return false;
		}
	}
}
