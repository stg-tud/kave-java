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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cc.kave.commons.exceptions.AssertionException;
import cc.kave.episodes.mining.evaluation.ProposalHelper;
import cc.kave.episodes.model.Episode;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Sets;

public class ProposalsSorterTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Episode query;
	private Set<Episode> patterns;

	private ProposalsSorter sut;

	@Before
	public void setup() {
		query = new Episode();
		patterns = Sets.newHashSet();
		sut = new ProposalsSorter();
	}

	@Test
	public void throwMessage() throws Exception {
		query = createEpisode("1", "2", "3", "1>2", "1>3", "2>3");
		Episode p1 = createEpisode("1", "2", "3", "4", "1>2", "1>3", "2>3");
		Episode p2 = createEpisode("1", "2", "3", "4", "5", "1>2", "1>3",
				"2>3", "3>4", "4>5");
		Episode p3 = createEpisode("1", "2", "3", "4", "1>2", "1>3", "2>3",
				"2>4");
		patterns = Sets.newHashSet(p1, p2, p3);

		thrown.expect(Exception.class);
		thrown.expectMessage("This metric is not available!");
		sut.sort(query, patterns, Metrics.UNDEFINED);
	}

	@Test
	public void emptyQueryF1Events() throws Exception {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Query is empty!");

		Episode pattern = createEpisode("1", "2", "1>2");

		patterns = Sets.newHashSet(pattern);

		sut.sort(query, patterns, Metrics.F1_EVENTS);
	}

	@Test
	public void emptyPatternListF1Events() throws Exception {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Pattern list is empty!");

		query = createEpisode("1", "2", "1>2");

		sut.sort(query, patterns, Metrics.F1_EVENTS);
	}

	@Test
	public void emptyPatternF1Events() throws Exception {

		query = createEpisode("1", "2", "1>2");
		Episode pattern = new Episode();

		patterns = Sets.newHashSet(pattern);

		Set<Tuple<Episode, Double>> expected = ProposalHelper
				.createEpisodesSortedSet();

		Set<Tuple<Episode, Double>> actuals = sut.sort(query, patterns,
				Metrics.F1_EVENTS);

		assertEquals(expected, actuals);
	}

	@Test
	public void emptyQueryF1Facts() throws Exception {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Query is empty!");

		Episode pattern = createEpisode("1", "2", "1>2");

		patterns = Sets.newHashSet(pattern);

		sut.sort(query, patterns, Metrics.F1_FACTS);
	}

	@Test
	public void emptyPatternListF1Facts() throws Exception {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Pattern list is empty!");

		query = createEpisode("1", "2", "1>2");

		sut.sort(query, patterns, Metrics.F1_FACTS);
	}

	@Test
	public void emptyPatternF1Facts() throws Exception {

		query = createEpisode("1", "2", "1>2");
		Episode pattern = new Episode();

		patterns = Sets.newHashSet(pattern);

		Set<Tuple<Episode, Double>> expected = ProposalHelper
				.createEpisodesSortedSet();

		Set<Tuple<Episode, Double>> actuals = sut.sort(query, patterns,
				Metrics.F1_FACTS);

		assertEquals(expected, actuals);
	}

	@Test
	public void emptyQueryMapo() throws Exception {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Query is empty!");

		Episode pattern = createEpisode("1", "2", "1>2");

		patterns = Sets.newHashSet(pattern);

		sut.sort(query, patterns, Metrics.MAPO);
	}

	@Test
	public void emptyPatternListMapo() throws Exception {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Pattern list is empty!");

		query = createEpisode("1", "2", "1>2");

		sut.sort(query, patterns, Metrics.MAPO);
	}

	@Test
	public void emptyPatternMapo() throws Exception {

		query = createEpisode("1", "2", "1>2");
		Episode pattern = new Episode();

		patterns = Sets.newHashSet(pattern);

		Set<Tuple<Episode, Double>> expected = ProposalHelper
				.createEpisodesSortedSet();

		Set<Tuple<Episode, Double>> actuals = sut.sort(query, patterns,
				Metrics.MAPO);

		assertEquals(expected, actuals);
	}

	@Test
	public void oneEmptyPatternF1Events() throws Exception {

		query = createEpisode("1", "2", "1>2");
		Episode p1 = new Episode();
		Episode p2 = createEpisode("1", "2", "3", "1>2", "1>3");

		patterns = Sets.newHashSet(p1, p2);

		Set<Tuple<Episode, Double>> expected = ProposalHelper
				.createEpisodesSortedSet();
		expected.add(Tuple.newTuple(p2, fract(4.0, 5.0)));

		Set<Tuple<Episode, Double>> actuals = sut.sort(query, patterns,
				Metrics.F1_EVENTS);

		assertEquals(expected, actuals);
	}

	@Test
	public void oneEmptyPatternF1Facts() throws Exception {

		query = createEpisode("1", "2", "1>2");
		Episode p1 = new Episode();
		Episode p2 = createEpisode("1", "2", "3", "1>2", "1>3");

		patterns = Sets.newHashSet(p1, p2);

		Set<Tuple<Episode, Double>> expected = ProposalHelper
				.createEpisodesSortedSet();
		expected.add(Tuple.newTuple(p2, fract(3.0, 4.0)));

		Set<Tuple<Episode, Double>> actuals = sut.sort(query, patterns,
				Metrics.F1_FACTS);

		assertProposals(expected, actuals);
	}

	@Test
	public void oneEmptyPatternMapo() throws Exception {

		query = createEpisode("1", "2", "1>2");
		Episode p1 = new Episode();
		Episode p2 = createEpisode("1", "2", "3", "1>2", "1>3");

		patterns = Sets.newHashSet(p1, p2);

		Set<Tuple<Episode, Double>> expected = ProposalHelper
				.createEpisodesSortedSet();
		expected.add(Tuple.newTuple(p2, fract(3.0, 5.0)));

		Set<Tuple<Episode, Double>> actuals = sut.sort(query, patterns,
				Metrics.MAPO);

		assertEquals(expected, actuals);
	}

	@Test
	public void f1Events() throws Exception {
		query = createEpisode("1", "2", "3", "1>2", "1>3", "2>3");

		Episode p1 = createEpisode("1", "2", "3", "1>2", "1>3");
		Episode p2 = createEpisode("1", "2", "3", "4", "5", "1>2", "1>3",
				"1>4", "1>5", "2>4", "2>5", "3>4", "3>5", "4>5");
		Episode p3 = createEpisode("1", "2", "3", "4", "1>2", "1>3", "1>4",
				"2>4", "3>4");
		Episode p4 = createEpisode("1", "2", "3", "3>1", "3>2", "1>2");
		Episode p5 = createEpisode("1", "2", "3", "4", "3>1", "3>2", "3>4",
				"1>4", "2>4");
		Episode p6 = createEpisode("1", "3", "4", "1>3", "1>4");
		Episode p7 = createEpisode("4", "5");
		patterns = Sets.newHashSet(p1, p2, p3, p4, p5, p6, p7);

		Set<Tuple<Episode, Double>> expected = Sets.newLinkedHashSet();
		expected.add(Tuple.newTuple(p1, 1.0));
		expected.add(Tuple.newTuple(p4, 1.0));
		expected.add(Tuple.newTuple(p3, fract(6.0, 7.0)));
		expected.add(Tuple.newTuple(p5, fract(6.0, 7.0)));
		expected.add(Tuple.newTuple(p2, fract(3.0, 4.0)));
		expected.add(Tuple.newTuple(p6, fract(2.0, 3.0)));
		expected.add(Tuple.newTuple(p7, 0.0));

		Set<Tuple<Episode, Double>> actuals = sut.sort(query, patterns,
				Metrics.F1_EVENTS);

		assertProposals(expected, actuals);
	}

	@Test
	public void f1Facts() throws Exception {
		query = createEpisode("1", "2", "3", "1>2", "1>3", "2>3");

		Episode p1 = createEpisode("1", "2", "3", "1>2", "1>3");
		Episode p2 = createEpisode("1", "2", "3", "4", "5", "1>2", "1>3",
				"1>4", "1>5", "2>4", "2>5", "3>4", "3>5", "4>5");
		Episode p3 = createEpisode("1", "2", "3", "4", "1>2", "1>3", "1>4",
				"2>4", "3>4");
		Episode p4 = createEpisode("1", "2", "3", "3>1", "3>2", "1>2");
		Episode p5 = createEpisode("1", "2", "3", "4", "3>1", "3>2", "3>4",
				"1>4", "2>4");
		Episode p6 = createEpisode("1", "3", "4", "1>3", "1>4");
		Episode p7 = createEpisode("4", "5");
		patterns = Sets.newHashSet(p1, p2, p3, p4, p5, p6, p7);

		Set<Tuple<Episode, Double>> expected = Sets.newLinkedHashSet();
		expected.add(Tuple.newTuple(p1, 1.0));
		expected.add(Tuple.newTuple(p3, fract(5.0, 7.0)));
		expected.add(Tuple.newTuple(p4, fract(2.0, 3.0)));
		expected.add(Tuple.newTuple(p6, fract(6.0, 11.0)));
		expected.add(Tuple.newTuple(p2, fract(10.0, 19.0)));
		expected.add(Tuple.newTuple(p5, fract(3.0, 7.0)));
		expected.add(Tuple.newTuple(p7, 0.0));

		Set<Tuple<Episode, Double>> actuals = sut.sort(query, patterns,
				Metrics.F1_FACTS);

		assertProposals(expected, actuals);
	}

	@Test
	public void mapo() throws Exception {
		query = createEpisode("1", "2", "3", "1>2", "1>3", "2>3");

		Episode p1 = createEpisode("1", "2", "3", "1>2", "1>3");
		Episode p2 = createEpisode("1", "2", "3", "4", "5", "1>2", "1>3",
				"1>4", "1>5", "2>4", "2>5", "3>4", "3>5", "4>5");
		Episode p3 = createEpisode("1", "2", "3", "4", "1>2", "1>3", "1>4",
				"2>4", "3>4");
		Episode p4 = createEpisode("1", "2", "3", "3>1", "3>2", "1>2");
		Episode p5 = createEpisode("1", "2", "3", "4", "3>1", "3>2", "3>4",
				"1>4", "2>4");
		Episode p6 = createEpisode("1", "3", "4", "1>3", "1>4");
		Episode p7 = createEpisode("4", "5");
		patterns = Sets.newHashSet(p1, p2, p3, p4, p5, p6, p7);

		Set<Tuple<Episode, Double>> expected = Sets.newLinkedHashSet();
		expected.add(Tuple.newTuple(p1, 1.0));
		expected.add(Tuple.newTuple(p3, fract(5.0, 9.0)));
		expected.add(Tuple.newTuple(p4, fract(1.0, 2.0)));
		expected.add(Tuple.newTuple(p6, fract(3.0, 8.0)));
		expected.add(Tuple.newTuple(p2, fract(5.0, 14.0)));
		expected.add(Tuple.newTuple(p5, fract(3.0, 11.0)));
		expected.add(Tuple.newTuple(p7, 0.0));

		Set<Tuple<Episode, Double>> actuals = sut.sort(query, patterns,
				Metrics.MAPO);

		assertProposals(expected, actuals);
	}

	@Ignore
	@Test
	public void example41() throws Exception {
		query = createEpisode("1", "2", "3", "1>2", "1>3", "2>3");

		Episode p1 = createEpisode("1", "2", "3", "4", "1>2", "1>3", "2>3");
		Episode p2 = createEpisode("1", "2", "3", "4", "5", "1>2", "1>3",
				"2>3", "3>4", "4>5");
		Episode p3 = createEpisode("1", "2", "3", "4", "1>2", "1>3", "2>3",
				"2>4");
		patterns = Sets.newHashSet(p1, p2, p3);

		Set<Tuple<Episode, Double>> expected = Sets.newLinkedHashSet();
		expected.add(Tuple.newTuple(p1, fract(-1, 1)));
		expected.add(Tuple.newTuple(p3, fract(-2, 1)));
		expected.add(Tuple.newTuple(p2, fract(-4, 1)));

		Set<Tuple<Episode, Double>> actuals = sut.sort(query, patterns,
				Metrics.LEVENSTEIN);

		assertEquals(expected, actuals);
		assertProposals(expected, actuals);
	}

	@Ignore
	@Test
	public void example42() throws Exception {
		query = createEpisode("1", "2", "3", "4", "1>2", "1>3", "1>4", "2>3",
				"2>4", "3>4");
		// same
		Episode p1 = createEpisode("1", "2", "3", "4", "1>2", "1>3", "1>4",
				"2>4", "3>4");
		// additions
		Episode p2 = createEpisode("1", "2", "3", "5", "4", "1>2", "1>3",
				"1>5", "1>4", "2>4", "3>4", "5>4");
		Episode p3 = createEpisode("5", "1", "2", "3", "4", "5>1", "5>2",
				"5>3", "5>4", "1>2", "1>3", "1>4", "2>4", "3>4");
		Episode p4 = createEpisode("1", "2", "3", "4", "5", "1>2", "1>3",
				"1>4", "1>5", "2>4", "2>5", "3>4", "3>5", "4>5");
		// substitutions
		Episode p5 = createEpisode("1", "2", "5", "4", "1>2", "1>5", "1>4",
				"2>4", "5>4");
		Episode p6 = createEpisode("5", "2", "3", "4", "5>2", "5>3", "5>4",
				"2>4", "3>4");
		Episode p7 = createEpisode("1", "2", "3", "5", "1>2", "1>3", "1>5",
				"2>5", "3>5");
		// deletions
		Episode p8 = createEpisode("1", "2", "3", "1>2", "1>3");
		Episode p9 = createEpisode("2", "3", "4", "2>4", "3>4");
		patterns = Sets.newHashSet(p1, p2, p3, p4, p5, p6, p7, p8, p9);

		Set<Tuple<Episode, Double>> expected = Sets.newLinkedHashSet();
		expected.add(Tuple.newTuple(p1, 0.0));
		expected.add(Tuple.newTuple(p2, -1.0));
		expected.add(Tuple.newTuple(p3, -1.0));
		expected.add(Tuple.newTuple(p4, -1.0));
		expected.add(Tuple.newTuple(p5, -1.0));
		expected.add(Tuple.newTuple(p6, -1.0));
		expected.add(Tuple.newTuple(p7, -1.0));
		expected.add(Tuple.newTuple(p8, -1.0));

		Set<Tuple<Episode, Double>> actuals = sut.sort(query, patterns,
				Metrics.LEVENSTEIN);

		assertEquals(expected, actuals);
	}

	private Episode createEpisode(String... strings) {
		Episode episode = new Episode();
		episode.addStringsOfFacts(strings);
		return episode;
	}

	private Double fract(double numerator, double denominator) {
		return numerator / denominator;
	}

	private void assertProposals(Set<Tuple<Episode, Double>> expected,
			Set<Tuple<Episode, Double>> actuals) {
		if (expected.isEmpty() && actuals.isEmpty()) {
			assertTrue(true);
		}
		if (expected.size() != actuals.size()) {
			fail();
		}
		Iterator<Tuple<Episode, Double>> itE = expected.iterator();
		Iterator<Tuple<Episode, Double>> itA = actuals.iterator();
		while (itE.hasNext()) {
			Tuple<Episode, Double> expect = itE.next();
			Tuple<Episode, Double> actual = itA.next();
			assertEquals(expect.getFirst(), actual.getFirst());
			assertEquals(expect.getSecond(), actual.getSecond(), 0.001d);
		}
	}
}
