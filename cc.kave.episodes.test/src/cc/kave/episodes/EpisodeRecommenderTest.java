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

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Episode;
import cc.recommenders.datastructures.Tuple;

public class EpisodeRecommenderTest {

	private Set<Tuple<Episode, Double>> expectedProposals;
	private Set<Tuple<Episode, Double>> actualProposals;
	private Map<Integer, List<Episode>> learnedEpisodes;

	private EpisodeRecommender sut;

	@Before
	public void setup() {
		sut = new EpisodeRecommender();
		expectedProposals = Sets.newLinkedHashSet();

		learnedEpisodes = new HashMap<Integer, List<Episode>>();
		learnedEpisodes.put(1, newArrayList(newEpisode(3, 1, "1"), newEpisode(3, 1, "2"), newEpisode(3, 1, "3")));
		learnedEpisodes.put(2, newArrayList(newEpisode(2, 2, "1", "2", "1>2")));
		learnedEpisodes.put(3, newArrayList(newEpisode(1, 3, "1", "2", "3", "1>2")));
	}

	@Test
	public void oneEventQuery() {

		queryWith(1, "1");

		addProposal(newEpisode(3, 1, "1"), 1.0);
		addProposal(newEpisode(2, 2, "1", "2", "1>2"), 0.5);
		addProposal(newEpisode(1, 3, "1", "2", "3", "1>2"), 0.4);
		addProposal(newEpisode(3, 1, "2"), 0.0);
		addProposal(newEpisode(3, 1, "3"), 0.0);

		assertProposals(actualProposals);
	}

	@Test
	public void twoEventQuery() {

		// order relation is not counted in size
		queryWith(2, "1", "2", "1>2");

		addProposal(newEpisode(2, 2, "1", "2", "1>2"), 1.0);
		addProposal(newEpisode(1, 3, "1", "2", "3", "1>2"), 6.0 / 7.0);
		addProposal(newEpisode(3, 1, "2"), 0.5);
		addProposal(newEpisode(3, 1, "1"), 0.5);
		addProposal(newEpisode(3, 1, "3"), 0.0);

		assertProposals(actualProposals);
	}

	@Test
	@Ignore
	public void oneNodeQuery() {

		queryWith(1, "2");

		addProposal(newEpisode(3, 1, "2"), 1.0);
		addProposal(newEpisode(2, 2, "1", "2", "1>2"), 0.5);
		addProposal(newEpisode(1, 3, "1", "2", "3", "1>2"), 0.4);
		addProposal(newEpisode(3, 1, "1"), 0.0);
		addProposal(newEpisode(3, 1, "3"), 0.0);

		assertProposals(actualProposals);
	}

	@Test
	@Ignore
	public void twoNodeQuery() {

		queryWith(2, "1", "2", "1>2");
		
		addProposal(newEpisode(2, 2, "1", "2", "1>2"), 1.0);
		addProposal(newEpisode(1, 3, "1", "2", "3", "1>2"), 6.0 / 7.0);
		addProposal(newEpisode(3, 1, "1"), 0.5);
		addProposal(newEpisode(3, 1, "2"), 0.5);
		addProposal(newEpisode(3, 1, "3"), 0.0);

		assertProposals(actualProposals);
	}

	private Episode newEpisode(int frequency, int numberOfEvents, String... facts) {
		Episode episode = new Episode();
		episode.addFacts(facts);
		episode.setFrequency(frequency);
		episode.setNumEvents(numberOfEvents);
		return episode;
	}

	private void queryWith(int numberOfEvents, String... facts) {
		actualProposals = sut.getProposals(newEpisode(1, numberOfEvents, facts), learnedEpisodes, 3);
	}

	private void addProposal(Episode e, double probability) {
		expectedProposals.add(Tuple.newTuple(e, probability));
	}

	private void assertProposals(Set<Tuple<Episode, Double>> actualProposals) {
		if (expectedProposals.size() != actualProposals.size()) {
			System.out.println("expected\n");
			System.out.println(expectedProposals);
			System.out.println("\nbut was\n");
			System.out.println(actualProposals);
			fail();
		}
		Iterator<Tuple<Episode, Double>> itE = expectedProposals.iterator();
		Iterator<Tuple<Episode, Double>> itA = actualProposals.iterator();
		while (itE.hasNext()) {
			Tuple<Episode, Double> expected = itE.next();
			Tuple<Episode, Double> actual = itA.next();
			assertEquals(expected, actual);
		}
	}
}