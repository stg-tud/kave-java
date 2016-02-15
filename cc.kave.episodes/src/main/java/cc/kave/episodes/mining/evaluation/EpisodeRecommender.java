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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.model.Pattern;
import cc.kave.episodes.model.Query;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.evaluation.data.Measure;

public class EpisodeRecommender {

	public Set<Tuple<Pattern, Double>> getProposals(Query query, Map<Integer, List<Pattern>> patterns,
			int numberOfProposals) throws Exception {
		Map<Pattern, Double> episodesWithF1Value = new HashMap<Pattern, Double>();

		if (patterns.isEmpty()) {
			throw new Exception("The list of learned episodes is empty");
		}

		if (numberOfProposals <= 0) {
			throw new Exception("Request a miningful number of proposals to show");
		}

		for (Map.Entry<Integer, List<Pattern>> entry : patterns.entrySet()) {
			for (Pattern e : entry.getValue()) {
				episodesWithF1Value.put(e, calcF1(query, e));
			}
		}
		Set<Tuple<Pattern, Double>> sortedEpisodes = sortedProposals(episodesWithF1Value);
		Set<Tuple<Pattern, Double>> finalProposals = ProposalHelper.createEpisodesSortedSet();

		int idx = 0;
		for (Tuple<Pattern, Double> tuple : sortedEpisodes) {
			if (idx < numberOfProposals && tuple.getSecond() > 0.0
					&& !episodeIsPartOfQuery(query, tuple.getFirst())) {
				finalProposals.add(tuple);
				idx++;
			}
		}
		return finalProposals;
	}

	private boolean episodeIsPartOfQuery(Query query, Pattern episode) {
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

	private double calcF1(Query query, Pattern e) {
		Set<Fact> factsEpisode = Sets.newHashSet(e.getFacts());
		Set<Fact> factsQuery = Sets.newHashSet(query.getFacts());
		Measure m = Measure.newMeasure(factsQuery, factsEpisode);
		double f1 = m.getF1();
		return f1;
	}

	private Set<Tuple<Pattern, Double>> sortedProposals(Map<Pattern, Double> proposals) {

		Set<Tuple<Pattern, Double>> sortedProposals = ProposalHelper.createEpisodesSortedSet();

		for (Pattern episode : proposals.keySet()) {
			Tuple<Pattern, Double> tuple = Tuple.newTuple(episode, proposals.get(episode));
			sortedProposals.add(tuple);
		}
		return sortedProposals;
	}
}
