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

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.evaluation.queries.Separator;
import cc.kave.episodes.model.Episode;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.evaluation.data.Measure;

public class EpisodeRecommender {
	
	private Separator separator = new Separator();

	public Set<Tuple<Episode, Double>> getProposals(Episode query, Map<Integer, Set<Episode>> patterns, 
															int numberOfProposals) {
		assertTrue(query.getNumEvents() > 0, "Input a valid query!");
		assertTrue(!patterns.isEmpty(), "The list of learned episodes is empty!");
		assertTrue(numberOfProposals > 0, "Request a positive number of proposals to show!");
		
		Set<Tuple<Episode, Double>> allProposals = sortProposals(query, patterns);
		Set<Tuple<Episode, Double>> limitedProposals = Sets.newLinkedHashSet();
		
		int counter = 0;
		for (Tuple<Episode, Double> tuple : allProposals) {
			if ((counter < numberOfProposals) && !episodeIsPartOfQuery(query, tuple.getFirst())) {
				if ((tuple.getSecond() > 0.0) || (tuple.getSecond() == 0.0) && (query.getNumEvents() == 1)) {
					limitedProposals.add(tuple);
					counter++;
				}
			}
		}
		return limitedProposals;
	}
	
	private Set<Tuple<Episode, Double>> sortProposals(Episode query, Map<Integer, Set<Episode>> patterns) {
		Set<Tuple<Episode, Double>> allProposals = ProposalHelper.createEpisodesSortedSet();
		
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			for (Episode e : entry.getValue()) {
				allProposals.add(Tuple.newTuple(e, calcF1(query, e)));
			}
		}
		return allProposals;
	}

	private boolean episodeIsPartOfQuery(Episode query, Episode episode) {
		Set<Fact> factsQuery = separator.getEpisodeBody(query);
		for (Fact fact : episode.getFacts()) {
			if (factsQuery.contains(fact)) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	public double calcF1(Episode query, Episode episode) {
		Set<Fact> factsQuery = separator.getEpisodeBody(query);
		
		Measure m = Measure.newMeasure(factsQuery, episode.getFacts());
		double f1 = m.getF1();
		return f1;
	}
	
	public double calcPrecision(Episode query, Episode episode) {
		Set<Fact> factsQuery = separator.getEpisodeBody(query);
		
		Measure m = Measure.newMeasure(factsQuery, episode.getFacts());
		double precision = m.getPrecision();
		return precision;
	}
}
