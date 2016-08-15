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
package cc.kave.episodes.mining.evaluation;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;

public class ProposalHelperTest {

	private Episode pattern1;
	private Episode pattern2;
	private Episode pattern3;
	private Episode pattern4;
	private Episode pattern5;
	private Episode pattern6;
	private Episode pattern7;

	private Set<Tuple<Episode, Double>> actuals1;
	private Set<Tuple<Integer, Set<Fact>>> actuals2;
	private Set<Tuple<Episode, Double>> expected;
	private Set<Tuple<Integer, Set<Fact>>> expected2;

	@Before
	public void setup() {
		pattern1 = createPattern(3, "1");

		pattern2 = createPattern(3, "1", "2", "1>2");
		pattern3 = createPattern(4, "1", "3", "1>3");
		pattern4 = createPattern(2, "2", "3", "2>3");

		pattern5 = createPattern(4, "1", "2", "3", "1>2");
		pattern6 = createPattern(2, "1", "2", "4", "2>4");
		pattern7 = createPattern(3, "1", "3", "4", "1>3");

		actuals1 = ProposalHelper.createEpisodesSortedSet();
		actuals2 = ProposalHelper.createFactsSortedSet();
		expected = Sets.newLinkedHashSet();
		expected2 = Sets.newLinkedHashSet();
	}

	@Test
	public void oneNodeEpisodes() {
		actuals1.add(Tuple.newTuple(new Episode(), 1.0));
		actuals1.add(Tuple.newTuple(pattern1, 1.0));

		expected.add(Tuple.newTuple(pattern1, 1.0));
		expected.add(Tuple.newTuple(new Episode(), 1.0));

		assertSets();
	}

	@Test
	public void probabilitySorting() {
		actuals1.add(Tuple.newTuple(pattern4, 0.5));
		actuals1.add(Tuple.newTuple(pattern1, 1.0));
		actuals1.add(Tuple.newTuple(pattern2, 0.7));

		expected.add(Tuple.newTuple(pattern1, 1.0));
		expected.add(Tuple.newTuple(pattern2, 0.7));
		expected.add(Tuple.newTuple(pattern4, 0.5));

		assertSets();
	}

	@Test
	public void frequencySorting() {
		actuals1.add(Tuple.newTuple(pattern2, 0.9));
		actuals1.add(Tuple.newTuple(pattern3, 0.9));
		actuals1.add(Tuple.newTuple(pattern4, 0.9));

		expected.add(Tuple.newTuple(pattern3, 0.9));
		expected.add(Tuple.newTuple(pattern2, 0.9));
		expected.add(Tuple.newTuple(pattern4, 0.9));

		assertSets();
	}

	@Test
	public void numberOfEventsSorting() {
		actuals1.add(Tuple.newTuple(pattern2, 0.9));
		actuals1.add(Tuple.newTuple(pattern7, 0.9));
		actuals1.add(Tuple.newTuple(pattern1, 0.9));

		expected.add(Tuple.newTuple(pattern7, 0.9));
		expected.add(Tuple.newTuple(pattern2, 0.9));
		expected.add(Tuple.newTuple(pattern1, 0.9));

		assertSets();
	}

	@Test
	public void factsSorter() {
		Episode pattern = createPattern(5, "1", "2", "3", "4", "1>2", "1>3", "1>4", "2>4", "3>4");
		actuals2.add(Tuple.newTuple(0, Sets.newHashSet(new Fact(4))));
		actuals2.add(Tuple.newTuple(3, Sets.newHashSet(new Fact(1))));
		actuals2.add(Tuple.newTuple(1, Sets.newHashSet(new Fact(2), new Fact(3))));
		
		expected2.add(Tuple.newTuple(3, Sets.newHashSet(new Fact(1))));
		expected2.add(Tuple.newTuple(1, Sets.newHashSet(new Fact(2), new Fact(3))));
		expected2.add(Tuple.newTuple(0, Sets.newHashSet(new Fact(4))));
		
		assertSetsFacts();
	}

	private Episode createPattern(int freq, String... string) {
		Episode pattern = new Episode();
		pattern.addStringsOfFacts(string);
		pattern.setFrequency(freq);
		return pattern;
	}

	private void assertSets() {
		assertEquals(expected.size(), actuals1.size());
		Iterator<Tuple<Episode, Double>> itA = expected.iterator();
		Iterator<Tuple<Episode, Double>> itB = actuals1.iterator();
		while (itA.hasNext()) {
			assertEquals(itA.next(), itB.next());
		}
	}
	
	private void assertSetsFacts() {
		assertEquals(expected2.size(), actuals2.size());
		Iterator<Tuple<Integer, Set<Fact>>> itA = expected2.iterator();
		Iterator<Tuple<Integer, Set<Fact>>> itB = actuals2.iterator();
		while (itA.hasNext()) {
			assertEquals(itA.next(), itB.next());
		}
	}
}