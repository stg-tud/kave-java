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
package cc.kave.commons.mining.episodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.evaluation.data.Measure;

public class EpisodeRecommender {

	public Set<Tuple<Episode, Double>> getProposals(Episode query, Map<Integer, List<Episode>> episodes,
			int numberOfProposals) throws Exception {
		Map<Episode, Double> episodesWithF1Value = new HashMap<Episode, Double>();

		if (episodes.isEmpty()) {
			throw new Exception("The list of learned episodes is empty");
		}

		if (numberOfProposals <= 0) {
			throw new Exception("Request a miningful number of proposals to show");
		}

		for (Map.Entry<Integer, List<Episode>> entry : episodes.entrySet()) {
			for (Episode e : entry.getValue()) {
				episodesWithF1Value.put(e, calcF1(query, e));
			}
		}
		Set<Tuple<Episode, Double>> sortedEpisodes = sortedProposals(episodesWithF1Value);
		Set<Tuple<Episode, Double>> finalProposals = ProposalHelper.createEpisodesSortedSet();

		int idx = 0;
		for (Tuple<Episode, Double> tuple : sortedEpisodes) {
			if (idx < numberOfProposals && tuple.getSecond() > 0.0
					&& !EpisodeIsPartOfQuery(query, tuple.getFirst())) {
				finalProposals.add(tuple);
				idx++;
			}
		}
		return finalProposals;
	}

	private boolean EpisodeIsPartOfQuery(Episode query, Episode episode) {
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
