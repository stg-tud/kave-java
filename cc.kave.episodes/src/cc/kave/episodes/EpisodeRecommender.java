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
package cc.kave.episodes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.mining.episodes.ProposalHelper;
import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.evaluation.data.Measure;

public class EpisodeRecommender {

	public Set<Tuple<Episode, Double>> getProposals(Episode query, Map<Integer, List<Episode>> episodes, int numberOfProposalsToShow) {
		Map<Episode, Double> episodesWithF1Value = new HashMap<Episode, Double>();
		
		for (Map.Entry<Integer, List<Episode>> entry : episodes.entrySet()) {
			for (Episode e : entry.getValue()) {
				episodesWithF1Value.put(e, calcF1(query, e));
			}
		}
		Set<Tuple<Episode, Double>> sortedEpisodes = sortedProposals(episodesWithF1Value, numberOfProposalsToShow);
		Set<Tuple<Episode, Double>> finalProposals = ProposalHelper.createEpisodesSortedSet();
		
		if (sortedEpisodes.size() <= numberOfProposalsToShow) {
			return sortedEpisodes;
		} else {
			int idx = 0;
			for (Tuple<Episode, Double> tuple : sortedEpisodes) {
				while (idx < numberOfProposalsToShow) {
					finalProposals.add(tuple);
					idx++;
				}
			}
		}
		return finalProposals;
	}

	private double calcF1(Episode query, Episode e) {
		Set<Fact> factsEpisode = Sets.newHashSet(e.getFacts());
		Set<Fact> factsQuery = Sets.newHashSet(query.getFacts());
		Measure m = Measure.newMeasure(factsQuery, factsEpisode);
		double f1 = m.getF1();
		return f1;
	}

	private Set<Tuple<Episode, Double>> sortedProposals(Map<Episode, Double> proposals, int numberOFProposals) {

		Set<Tuple<Episode, Double>> sortedProposals = ProposalHelper.createEpisodesSortedSet();

		for (int number = 0; number < numberOFProposals; number++) {
			for (Episode episode : proposals.keySet()) {
				Tuple<Episode, Double> tuple = Tuple.newTuple(episode, proposals.get(episode));
				sortedProposals.add(tuple);
			}
		}
		return sortedProposals;
	}

	public String toString(Set<Tuple<Episode, Double>> listOfProposals) {
		String result = "";
		Iterator<Tuple<Episode, Double>> iterator = listOfProposals.iterator();
		while (iterator.hasNext()) {
			Tuple<Episode, Double> proposal = iterator.next();
			result += proposal.getFirst().toString() + ": " + proposal.getSecond() + "\n";
		}
		return result;
	}
}
