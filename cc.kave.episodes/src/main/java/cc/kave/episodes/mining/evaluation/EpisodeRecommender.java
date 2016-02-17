/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ervina Cergani - initial API and implementation
 */
package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.PatternWithFreq;
import cc.kave.episodes.model.Query;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.evaluation.data.Measure;

public class EpisodeRecommender {

	public Set<Tuple<Episode, Double>> getProposals(Episode query, Map<Integer, Set<Episode>> patterns,
			int numberOfProposals) throws Exception {
		Map<Episode, Double> episodesWithF1Value = new HashMap<Episode, Double>();

		assertTrue(!patterns.isEmpty(), "The list of learned episodes is empty");
		assertTrue(numberOfProposals > 0, "Request a positive number of proposals to show");

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			for (Episode e : entry.getValue()) {
				episodesWithF1Value.put(e, calcF1(query, e));
			}
		}
		Set<Tuple<Episode, Double>> sortedEpisodes = sortedProposals(episodesWithF1Value);
		Set<Tuple<Episode, Double>> finalProposals = ProposalHelper.createEpisodesSortedSet();

		int idx = 0;
		for (Tuple<Episode, Double> tuple : sortedEpisodes) {
			if (idx < numberOfProposals && tuple.getSecond() > 0.0
					&& !episodeIsPartOfQuery(query, tuple.getFirst())) {
				finalProposals.add(tuple);
				idx++;
			}
		}
		return finalProposals;
	}

	private boolean episodeIsPartOfQuery(Episode query, Episode episode) {
		if (query.getNumEvents() <= episode.getNumEvents()) {
			return false;
		}
		for (Fact fact : episode.getFacts()) {
			if (query.containsFact(fact)) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	private double calcF1(Episode query, Episode e) {
		Set<Fact> factsEpisode = Sets.newHashSet(e.getFacts());
		Set<Fact> factsQuery = Sets.newHashSet(query.getFacts());
		Measure m = Measure.newMeasure(factsQuery, factsEpisode);
		double f1 = m.getF1();
		return f1;
	}

	private Set<Tuple<Episode, Double>> sortedProposals(Map<Episode, Double> proposals) {

		Set<Tuple<Episode, Double>> sortedProposals = ProposalHelper.createEpisodesSortedSet();

		for (Episode episode : proposals.keySet()) {
			Tuple<Episode, Double> tuple = Tuple.newTuple(episode, proposals.get(episode));
			sortedProposals.add(tuple);
		}
		return sortedProposals;
	}
}
