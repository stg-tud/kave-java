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

import cc.kave.episodes.model.PatternWithFreq;
import cc.recommenders.datastructures.Tuple;

public class ProposalHelperTest {
	
	private PatternWithFreq pattern1;
	private PatternWithFreq pattern2;
	private PatternWithFreq pattern3;
	private PatternWithFreq pattern4;
	private PatternWithFreq pattern5;
	private PatternWithFreq pattern6;
	private PatternWithFreq pattern7;

	private Set<Tuple<PatternWithFreq, Double>> actuals;
	private Set<Tuple<PatternWithFreq, Double>> expecteds;

	@Before
	public void setup() {
		pattern1 = createPattern("1");
		pattern1.setFrequency(3);
		
		pattern2 = createPattern("1", "2", "1>2");
		pattern2.setFrequency(3);
		pattern3 = createPattern("1", "3", "1>3");
		pattern3.setFrequency(4);
		pattern4 = createPattern("2", "3", "2>3");
		pattern4.setFrequency(2);
		
		pattern5 = createPattern("1", "2", "3", "1>2");
		pattern5.setFrequency(3);
		pattern6 = createPattern("1", "2", "4", "2>4");
		pattern6.setFrequency(3);
		pattern7 = createPattern("1", "3", "4", "1>3");
		pattern7.setFrequency(3);
		
		actuals = ProposalHelper.createEpisodesSortedSet();
		expecteds = Sets.newLinkedHashSet();
	}

	private PatternWithFreq createPattern(String...string) {
		PatternWithFreq pattern = new PatternWithFreq();
		for (String s : string) {
			pattern.addFact(s);
		}
		return pattern;
	}

	@Test
	public void firstElemIsNUllAndSecondIsEqual() {
		actuals.add(Tuple.newTuple(pattern1, 1.0));
		actuals.add(Tuple.newTuple(new PatternWithFreq(), 1.0));
	}

	@Test
	public void firstElemIsNUllAndSecondIsEqual_2() {
		actuals.add(Tuple.newTuple(new PatternWithFreq(), 1.0));
		actuals.add(Tuple.newTuple(pattern1, 1.0));
	}

	@Test
	public void isInDescendingOrder() {
		actuals.add(Tuple.newTuple(pattern4, 0.5));
		actuals.add(Tuple.newTuple(pattern1, 1.0));
		actuals.add(Tuple.newTuple(pattern2, 0.7));

		expecteds.add(Tuple.newTuple(pattern1, 1.0));
		expecteds.add(Tuple.newTuple(pattern2, 0.7));
		expecteds.add(Tuple.newTuple(pattern4, 0.5));

		assertSets();
	}

	@Test
	public void frequencySorting() {
		actuals.add(Tuple.newTuple(pattern2, 0.9));
		actuals.add(Tuple.newTuple(pattern3, 0.9));
		actuals.add(Tuple.newTuple(pattern4, 0.9));

		expecteds.add(Tuple.newTuple(pattern3, 0.9));
		expecteds.add(Tuple.newTuple(pattern2, 0.9));
		expecteds.add(Tuple.newTuple(pattern4, 0.9));

		assertSets();
	}

	@Test
	public void numberOfEventsSorting() {
		actuals.add(Tuple.newTuple(pattern2, 0.9));
		actuals.add(Tuple.newTuple(pattern5, 0.9));
		actuals.add(Tuple.newTuple(pattern1, 0.9));

		expecteds.add(Tuple.newTuple(pattern5, 0.9));
		expecteds.add(Tuple.newTuple(pattern2, 0.9));
		expecteds.add(Tuple.newTuple(pattern1, 0.9));

		assertSets();
	}
	
	@Test 
	public void eventSorting() {
		actuals.add(Tuple.newTuple(pattern7, 0.9));
		actuals.add(Tuple.newTuple(pattern5, 0.9));
		actuals.add(Tuple.newTuple(pattern6, 0.9));
		
		expecteds.add(Tuple.newTuple(pattern5, 0.9));
		expecteds.add(Tuple.newTuple(pattern6, 0.9));
		expecteds.add(Tuple.newTuple(pattern7, 0.9));
		
		assertSets();
	}

	private void assertSets() {
		assertEquals(expecteds.size(), actuals.size());
		Iterator<Tuple<PatternWithFreq, Double>> itA = expecteds.iterator();
		Iterator<Tuple<PatternWithFreq, Double>> itB = actuals.iterator();
		while (itA.hasNext()) {
			assertEquals(itA.next(), itB.next());
		}
	}
}