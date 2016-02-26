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

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cc.recommenders.datastructures.Tuple;

public class ProposalResultsTest {

	private ProposalResults sut;
	
	@Before
	public void setup() {
		sut = new ProposalResults();
	}
	
	@Test
	public void defaultValues() {
		assertTrue(sut.getTarget().equals(new Episode()));
		assertTrue(sut.getResults().equals(new LinkedList<Tuple<Double, Double>>()));
	}
	
	@Test
	public void valuesCanBeSet() {
		sut.setTarget(createEpisode(2, "11", "12", "11>12"));
		assertEquals(createEpisode(2, "11", "12", "11>12"), sut.getTarget());
		
		sut.addResult(0.8, 0.7);
		sut.addResult(0.5, 0.4);
		sut.addResult(0.3, 0.2);
		
		List<Tuple<Double, Double>> expected = new LinkedList<Tuple<Double, Double>>();
		
		expected.add(Tuple.newTuple(0.8, 0.7));
		expected.add(Tuple.newTuple(0.5, 0.4));
		expected.add(Tuple.newTuple(0.3, 0.2));
		
		assertEquals(expected, sut.getResults());
	}
	
	@Test
	public void equality_default() {
		ProposalResults a = new ProposalResults();
		a.setTarget(new Episode());
		a.addResult(0.0, 0.0);
		
		ProposalResults b = new ProposalResults();
		b.setTarget(new Episode());
		b.addResult(0.0, 0.0);
		
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertTrue(a.equals(b));
	}
	
	@Test
	public void equality_reallyTheSame() {
		ProposalResults a = new ProposalResults();
		a.setTarget(createEpisode(2, "11", "12", "11>12"));
		a.addResult(0.9, 0.7);
		a.addResult(0.6, 0.4);
		a.addResult(0.4, 0.2);
		
		ProposalResults b = new ProposalResults();
		b.setTarget(createEpisode(2, "11", "12", "11>12"));
		b.addResult(0.9, 0.7);
		b.addResult(0.6, 0.4);
		b.addResult(0.4, 0.2);
		
		assertTrue(a.equals(b));
	}
	
	@Test
	public void diffResults() {
		ProposalResults a = new ProposalResults();
		a.setTarget(createEpisode(2, "11", "12", "11>12"));
		a.addResult(0.9, 0.7);
		a.addResult(0.6, 0.4);
		a.addResult(0.4, 0.2);
		
		ProposalResults b = new ProposalResults();
		b.setTarget(createEpisode(2, "11", "12", "11>12"));
		b.addResult(0.9, 0.5);
		b.addResult(0.5, 0.4);
		b.addResult(0.3, 0.2);
		
		assertNotEquals(a, b);
		assertFalse(a.equals(b));
	}
	
	@Test
	public void diffTargets() {
		ProposalResults a = new ProposalResults();
		a.setTarget(createEpisode(2, "11", "12", "11>12"));
		a.addResult(0.9, 0.7);
		a.addResult(0.6, 0.4);
		a.addResult(0.4, 0.2);
		
		ProposalResults b = new ProposalResults();
		b.setTarget(createEpisode(2, "11", "13", "11>13"));
		b.addResult(0.9, 0.7);
		b.addResult(0.6, 0.4);
		b.addResult(0.4, 0.2);
		
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
