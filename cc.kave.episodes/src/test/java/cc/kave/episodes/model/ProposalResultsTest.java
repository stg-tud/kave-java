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

package cc.kave.episodes.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cc.recommenders.datastructures.Tuple;

public class ProposalResultsTest {
	
	private Map<Double, List<Tuple<Double, Double>>> expected;
	List<Tuple<Double, Double>> list;

	private TargetResults sut;
	
	@Before
	public void setup() {
		expected = new HashMap<Double, List<Tuple<Double, Double>>>();
		list = new LinkedList<Tuple<Double, Double>>();
		
		sut = new TargetResults();
	}
	
	@Test
	public void defaultValues() {
		assertTrue(sut.getTarget().equals(new Episode()));
		assertTrue(sut.getResults().equals(new HashMap<Double, List<Tuple<Double, Double>>>()));
	}
	
	@Test
	public void valuesCanBeSet() {
		sut.setTarget(createEpisode(2, "11", "12", "11>12"));
		assertEquals(createEpisode(2, "11", "12", "11>12"), sut.getTarget());
		
		sut.addResult(0.25, 0.8, 0.7);
		sut.addResult(0.5, 0.5, 0.4);
		sut.addResult(0.75, 0.3, 0.2);
		
		list.add(Tuple.newTuple(0.8, 0.7));
		expected.put(0.25, list);
		
		list = new LinkedList<Tuple<Double, Double>>();
		list.add(Tuple.newTuple(0.5, 0.4));
		expected.put(0.5, list);
		
		list = new LinkedList<Tuple<Double, Double>>();
		list.add(Tuple.newTuple(0.3, 0.2));
		expected.put(0.75, list);
		
		assertEquals(expected, sut.getResults());
	}
	
	@Test
	public void equality_default() {
		TargetResults a = new TargetResults();
		a.setTarget(new Episode());
		a.addResult(0.0, 0.0, 0.0);
		
		TargetResults b = new TargetResults();
		b.setTarget(new Episode());
		b.addResult(0.0, 0.0, 0.0);
		
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertTrue(a.equals(b));
	}
	
	@Test
	public void equality_reallyTheSame() {
		TargetResults a = new TargetResults();
		a.setTarget(createEpisode(2, "11", "12", "11>12"));
		a.addResult(0.25, 0.9, 0.7);
		a.addResult(0.25, 0.6, 0.4);
		a.addResult(0.75, 0.4, 0.2);
		
		TargetResults b = new TargetResults();
		b.setTarget(createEpisode(2, "11", "12", "11>12"));
		b.addResult(0.25, 0.9, 0.7);
		b.addResult(0.25, 0.6, 0.4);
		b.addResult(0.75, 0.4, 0.2);
		
		assertTrue(a.equals(b));
	}
	
	@Test
	public void diffResults() {
		TargetResults a = new TargetResults();
		a.setTarget(createEpisode(2, "11", "12", "11>12"));
		a.addResult(0.25, 0.9, 0.7);
		a.addResult(0.5, 0.6, 0.4);
		a.addResult(0.5, 0.4, 0.2);
		
		TargetResults b = new TargetResults();
		b.setTarget(createEpisode(2, "11", "12", "11>12"));
		b.addResult(0.25, 0.9, 0.5);
		b.addResult(0.5, 0.5, 0.4);
		b.addResult(0.5, 0.3, 0.2);
		
		assertNotEquals(a, b);
		assertFalse(a.equals(b));
	}
	
	@Test
	public void diffTargets() {
		TargetResults a = new TargetResults();
		a.setTarget(createEpisode(2, "11", "12", "11>12"));
		a.addResult(0.25, 0.9, 0.7);
		a.addResult(0.25, 0.6, 0.4);
		a.addResult(0.25, 0.4, 0.2);
		
		TargetResults b = new TargetResults();
		b.setTarget(createEpisode(2, "11", "13", "11>13"));
		b.addResult(0.25, 0.9, 0.7);
		b.addResult(0.25, 0.6, 0.4);
		b.addResult(0.25, 0.4, 0.2);
		
		assertNotEquals(a, b);
		assertFalse(a.equals(b));
	}
	
	private Episode createEpisode(int freq, String...strings) {
		Episode episode = new Episode();
		episode.setFrequency(freq);
		episode.addStringsOfFacts(strings);
		return episode;
	}
}
