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

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.evaluation.data.Measure;

public class EpisodeRecommender {

	public Set<Tuple<Episode, Double>> getProposals(Episode query, Map<Integer, List<Episode>> episodes) {

		Map<Episode, Double> results = new HashMap<Episode, Double>();
		int[] numEvents = getNumberOfEvents(episodes);

		for (int idx = 0; idx < numEvents.length; idx++) {
			List<Episode> episodeList = episodes.get(numEvents[idx]);
			for (Episode e : episodeList) {
				results.put(e, calcF1(query, e));
			}
		}
		return sortedProposals(results);
	}

	private double calcF1(Episode query, Episode e) {
		Set<Fact> factsEpisode = Sets.newHashSet(e.getFacts());
		Set<Fact> factsQuery = Sets.newHashSet(query.getFacts());
		Measure m = Measure.newMeasure(factsQuery, factsEpisode);
		double f1 = m.getF1();
		return f1;
	}

	private Set<Tuple<Episode, Double>> sortedProposals(Map<Episode, Double> proposals) {

		Set<Tuple<Episode, Double>> sortedProposals = ProposalHelper.createSortedSetNonAlphabet();

		for (Episode episode : proposals.keySet()) {
			Tuple<Episode, Double> tuple = Tuple.newTuple(episode, proposals.get(episode));
			sortedProposals.add(tuple);
		}
		return sortedProposals;
	}

	public String toString(Set<Tuple<Episode, Double>> listOfProposals) {
		String result = "";
		Iterator<Tuple<Episode, Double>> iterator = listOfProposals.iterator();
		while (iterator.hasNext()) {
			Tuple<Episode, Double> proposal = iterator.next();
			result += proposal.getFirst().toString() + ", " + proposal.getSecond().toString() + "\n";
		}
		return result;
	}

	private int[] getNumberOfEvents(Map<Integer, List<Episode>> episodes) {
		Set<Integer> nodes = Sets.newTreeSet(episodes.keySet());
		Iterator<Integer> itr = nodes.iterator();
		int[] numEvents = new int[episodes.size()];
		int idx = 0;
		while (itr.hasNext()) {
			numEvents[idx] = itr.next();
			idx++;
		}
		return numEvents;
	}

	// public void queryValidation(int queryEvents, Map<Integer,
	// List<EpisodeFacts>> episodes) throws Exception {
	// if (queryEvents > episodes.size()) {
	// throw new Exception("Invalid query, " + queryEvents + "different events
	// in the query");
	// }
	// }
}
