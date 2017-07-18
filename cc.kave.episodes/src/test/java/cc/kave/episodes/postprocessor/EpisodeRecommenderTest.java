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
package cc.kave.episodes.postprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Sets;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.episodes.mining.evaluation.EpisodeRecommender;
import cc.kave.episodes.model.Episode;
import cc.recommenders.datastructures.Tuple;

public class EpisodeRecommenderTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Set<Tuple<Episode, Double>> expectedProposals;
	private Set<Tuple<Episode, Double>> actualProposals;
	private Map<Integer, Set<Episode>> learnedPatterns;
	private Map<Integer, Set<Episode>> emptyEpisodes;

	private EpisodeRecommender sut;

	@Before
	public void setup() {
		sut = new EpisodeRecommender();
		expectedProposals = Sets.newLinkedHashSet();
		emptyEpisodes = new HashMap<Integer, Set<Episode>>();

		learnedPatterns = new HashMap<Integer, Set<Episode>>();
		learnedPatterns.put(1, Sets.newHashSet(newPattern(2, "1"), newPattern(1, "2"), newPattern(3, "3")));
		learnedPatterns.put(2, Sets.newHashSet(newPattern(3, "4", "5", "4>5"), newPattern(2, "4", "6", "4>6")));
		learnedPatterns.put(3, Sets.newHashSet(newPattern(1, "6", "7", "8", "6>7", "6>8", "7>8"),
				newPattern(3, "10", "11", "12", "10>11", "10>12", "11>12")));
		learnedPatterns.put(4, Sets.newHashSet(newPattern(2, "10", "11", "12", "13", "10>11", "10>12", "10>13")));
	}

	@Test
	public void emptyQuery() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Input a valid query!");
		sut.getProposals(new Episode(), learnedPatterns, 2);
	}

	@Test
	public void emptyPatterns() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("The list of learned episodes is empty!");
		sut.getProposals(newQuery("11", "12", "11>12"), emptyEpisodes, 2);
	}

	@Test
	public void proposalsNumber() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Request a positive number of proposals to show!");
		sut.getProposals(newQuery("11", "12", "11>12"), learnedPatterns, 0);
	}

	@Test
	public void diffProbabilitydiffFrequency() {
		queryWith("4", "5", "6", "9", "4>5", "4>6", "4>9");

		addProposal(newPattern(3, "4", "5", "4>5"), 1.0 / 3.0);
		addProposal(newPattern(2, "4", "6", "4>6"), 1.0 / 3.0);
		addProposal(newPattern(1, "6", "7", "8", "6>7", "6>8", "7>8"), fract(2, 9));

		assertProposals();
	}

	@Test
	public void diffProbability() {
		queryWith("10", "11", "10>11");

		addProposal(newPattern(3, "10", "11", "12", "10>11", "10>12", "11>12"), fract(2, 7));
		addProposal(newPattern(2, "10", "11", "12", "13", "10>11", "10>12", "10>13"), fract(1, 4));

		assertProposals();
	}

	private Double fract(double numerator, double denominator) {
		// return Double.valueOf(df.format(numerator / denominator));
		return numerator / denominator;
	}

	@Test
	public void oneEventQuery() {
		queryWith("1");

		addProposal(newPattern(3, "10", "11", "12", "10>11", "10>12", "11>12"), 0.0);
		addProposal(newPattern(3, "4", "5", "4>5"), 0.0);
		addProposal(newPattern(3, "3"), 0.0);

		assertProposals();
	}

	@Test
	public void twoEventQuery() {
		queryWith("5", "6", "5>6");

		addProposal(newPattern(2, "4", "6", "4>6"), fract(1, 2));
		addProposal(newPattern(1, "6", "7", "8", "6>7", "6>8", "7>8"), fract(2, 7));

		assertProposals();
	}

	@Test
	public void oneProposal() {
		queryWith("7", "8", "7>8");

		addProposal(newPattern(1, "6", "7", "8", "6>7", "6>8", "7>8"), fract(2, 7));

		assertProposals();
	}

	@Test
	public void noProposal() {
		queryWith("1", "2", "1>2");

		assertProposals();
	}

	@Test
	public void unrelatedQuery() {
		queryWith("20", "21", "20>21");

		assertProposals();
	}

	private Episode newPattern(int freq, String... string) {
		Episode pattern = new Episode();
		pattern.setFrequency(freq);
		for (String s : string) {
			pattern.addFact(s);
		}
		return pattern;
	}

	private Episode newQuery(String... facts) {
		Episode query = new Episode();
		query.addStringsOfFacts(facts);
		return query;
	}

	private void queryWith(String... facts) {
		actualProposals = sut.getProposals(newQuery(facts), learnedPatterns, 3);
	}

	private void addProposal(Episode e, double probability) {
		expectedProposals.add(Tuple.newTuple(e, probability));
	}

	private void assertProposals() {
		if (expectedProposals.isEmpty() && actualProposals.isEmpty()) {
			assertTrue(true);
		}
		if (expectedProposals.size() != actualProposals.size()) {
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