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
import cc.kave.episodes.model.PatternWithFreq;
import cc.kave.episodes.model.Query;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.evaluation.data.Measure;

public class EpisodeRecommender {

	public Set<Tuple<PatternWithFreq, Double>> getProposals(Query query, Map<Integer, List<PatternWithFreq>> patterns,
			int numberOfProposals) throws Exception {
		Map<PatternWithFreq, Double> episodesWithF1Value = new HashMap<PatternWithFreq, Double>();

		if (patterns.isEmpty()) {
			throw new Exception("The list of learned episodes is empty");
		}

		if (numberOfProposals <= 0) {
			throw new Exception("Request a miningful number of proposals to show");
		}

		for (Map.Entry<Integer, List<PatternWithFreq>> entry : patterns.entrySet()) {
			for (PatternWithFreq e : entry.getValue()) {
				episodesWithF1Value.put(e, calcF1(query, e));
			}
		}
		Set<Tuple<PatternWithFreq, Double>> sortedEpisodes = sortedProposals(episodesWithF1Value);
		Set<Tuple<PatternWithFreq, Double>> finalProposals = ProposalHelper.createEpisodesSortedSet();

		int idx = 0;
		for (Tuple<PatternWithFreq, Double> tuple : sortedEpisodes) {
			if (idx < numberOfProposals && tuple.getSecond() > 0.0
					&& !episodeIsPartOfQuery(query, tuple.getFirst())) {
				finalProposals.add(tuple);
				idx++;
			}
		}
		return finalProposals;
	}

	private boolean episodeIsPartOfQuery(Query query, PatternWithFreq episode) {
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

	private double calcF1(Query query, PatternWithFreq e) {
		Set<Fact> factsEpisode = Sets.newHashSet(e.getFacts());
		Set<Fact> factsQuery = Sets.newHashSet(query.getFacts());
		Measure m = Measure.newMeasure(factsQuery, factsEpisode);
		double f1 = m.getF1();
		return f1;
	}

	private Set<Tuple<PatternWithFreq, Double>> sortedProposals(Map<PatternWithFreq, Double> proposals) {

		Set<Tuple<PatternWithFreq, Double>> sortedProposals = ProposalHelper.createEpisodesSortedSet();

		for (PatternWithFreq episode : proposals.keySet()) {
			Tuple<PatternWithFreq, Double> tuple = Tuple.newTuple(episode, proposals.get(episode));
			sortedProposals.add(tuple);
		}
		return sortedProposals;
	}
}
