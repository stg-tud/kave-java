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
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Sets;

import cc.kave.episodes.model.events.Fact;
import cc.recommenders.exceptions.AssertionException;

public class EpisodeTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Episode sut;

	@Before
	public void setup() {
		sut = new Episode();
	}

	@Test
	public void freqIsPositive() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Frequency cannot be a negative value!");
		sut.setFrequency(-1);
	}
	
	@Test
	public void BiDirectIsPositive() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Bidirectional measure should be a probability value!");
		sut.setEntropy(-0.5);
	}
	
	@Test
	public void BiDirectIsLessThenOne() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Bidirectional measure should be a probability value!");
		sut.setEntropy(1.5);
	}

	@Test
	public void defaultValues() {
		assertEquals(0, sut.getFrequency());
		assertTrue(sut.getEntropy() == 0.0);
		assertEquals(0, sut.getNumFacts());
		assertEquals(0, sut.getNumEvents());
		assertEquals(Sets.newHashSet(), sut.getEvents());
		assertEquals(Sets.newHashSet(), sut.getRelations());
		assertEquals(Sets.newHashSet(), sut.getFacts());
	}

	@Test
	public void valuesCanBeSet() {
		sut.setFrequency(3);
		sut.setEntropy(0.5);
		sut.addFact("f");
		
		Set<Fact> facts = Sets.newHashSet(new Fact("f"));
		
		assertEquals(3, sut.getFrequency());
		assertTrue(sut.getEntropy() == 0.5);
		assertEquals(facts, sut.getFacts());
		assertEquals(facts, sut.getEvents());
		assertEquals(Sets.newHashSet(), sut.getRelations());
		assertEquals(1, sut.getNumEvents());
		assertEquals(1, sut.getNumFacts());
	}

	@Test
	public void addMultipleFacts() {
		sut.addStringsOfFacts("f", "g", "f>g");
		
		Set<Fact> facts = Sets.newHashSet(new Fact("f"), new Fact("g"), new Fact("f>g"));
		Set<Fact> events = Sets.newHashSet(new Fact("f"), new Fact("g"));
		Set<Fact> relations = Sets.newHashSet(new Fact("f>g"));
		
		assertEquals(facts, sut.getFacts());
		assertEquals(events, sut.getEvents());
		assertEquals(relations, sut.getRelations());
		assertEquals(sut.getNumEvents(), 2);
		assertEquals(sut.getNumFacts(), 3);
	}

	@Test
	public void addListOfFactTest() {
		List<Fact> ListOffacts = new LinkedList<Fact>();
		ListOffacts.add(new Fact("f"));
		ListOffacts.add(new Fact("g"));
		ListOffacts.add(new Fact("f>g"));

		sut.addListOfFacts(ListOffacts);
		
		Set<Fact> facts = Sets.newHashSet(new Fact("f"), new Fact("g"), new Fact("f>g"));
		Set<Fact> events = Sets.newHashSet(new Fact("f"), new Fact("g"));

		assertEquals(facts, sut.getFacts());
		assertEquals(events, sut.getEvents());
		assertEquals(2, sut.getNumEvents());
		assertEquals(3, sut.getNumFacts());
	}

	@Test
	public void equality_default() {
		Episode a = new Episode();
		Episode b = new Episode();
		
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertTrue(a.equals(b));
	}

	@Test
	public void equality_reallyTheSame() {
		Episode a = new Episode();
		a.addStringsOfFacts("1", "2", "3", "1>2", "1>3");
		a.setFrequency(3);
		a.setEntropy(0.5);

		Episode b = new Episode();
		b.addStringsOfFacts("1", "2", "3", "1>2", "1>3");
		b.setFrequency(3);
		b.setEntropy(0.5);
		
		assertEquals(a, b);
		assertTrue(a.equals(b));
		assertEquals(a.hashCode(), b.hashCode());

		assertEquals(a.getFrequency(), b.getFrequency());
		assertTrue(a.getEntropy() == b.getEntropy());
		assertEquals(a.getNumEvents(), b.getNumEvents());
		assertEquals(a.getNumFacts(), b.getNumFacts());
		assertEquals(a.getFacts(), b.getFacts());
		assertEquals(a.getEvents(), b.getEvents());
		assertEquals(a.getRelations(), b.getRelations());
	}
	
	@Test
	public void diffFreq() {
		Episode a = new Episode();
		a.setFrequency(3);
		a.setEntropy(0.5);
		a.addStringsOfFacts("1", "2", "3", "1>2", "1>3");

		Episode b = new Episode();
		b.setFrequency(5);
		b.setEntropy(0.5);
		b.addStringsOfFacts("1", "2", "3", "1>2", "1>3");

		assertNotEquals(a, b);
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());

		assertNotEquals(a.getFrequency(), b.getFrequency());
		assertTrue(a.getEntropy() == b.getEntropy());
		assertEquals(a.getNumEvents(), b.getNumEvents());
		assertEquals(a.getNumFacts(), b.getNumFacts());
		assertEquals(a.getFacts(), b.getFacts());
		assertEquals(a.getEvents(), b.getEvents());
	}
	
	@Test
	public void diffBiDirect() {
		Episode a = new Episode();
		a.setFrequency(3);
		a.setEntropy(0.5);
		a.addStringsOfFacts("1", "2", "3", "1>2", "1>3");

		Episode b = new Episode();
		b.setFrequency(3);
		b.setEntropy(0.8);
		b.addStringsOfFacts("1", "2", "3", "1>2", "1>3");

		assertNotEquals(a, b);
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());

		assertEquals(a.getFrequency(), b.getFrequency());
		assertTrue(a.getEntropy() != b.getEntropy());
		assertEquals(a.getNumEvents(), b.getNumEvents());
		assertEquals(a.getNumFacts(), b.getNumFacts());
		assertEquals(a.getFacts(), b.getFacts());
		assertEquals(a.getEvents(), b.getEvents());
	}

	@Test
	public void equality_diffEvents() {
		Episode a = new Episode();
		a.setFrequency(3);
		a.setEntropy(0.5);
		a.addStringsOfFacts("1", "2", "1>2");

		Episode b = new Episode();
		b.setFrequency(3);
		b.setEntropy(0.5);
		b.addStringsOfFacts("3", "4", "3>4");

		assertNotEquals(a, b);
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());

		assertEquals(a.getFrequency(), b.getFrequency());
		assertTrue(a.getEntropy() == b.getEntropy());
		assertEquals(a.getNumEvents(), b.getNumEvents());
		assertEquals(a.getNumFacts(), b.getNumFacts());
		assertNotEquals(a.getFacts(), b.getFacts());
		assertNotEquals(a.getEvents(), b.getEvents());
		assertNotEquals(a.getRelations(), b.getRelations());
	}

	@Test
	public void equality_sameEventsDiffRelations() {
		Episode a = new Episode();
		a.setFrequency(3);
		a.setEntropy(0.5);
		a.addStringsOfFacts("1", "2", "3", "1>2", "1>3");

		Episode b = new Episode();
		b.setFrequency(3);
		b.setEntropy(0.5);
		b.addStringsOfFacts("1", "2", "3", "1>2", "2>3");

		assertNotEquals(a, b);
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());

		assertEquals(a.getFrequency(), b.getFrequency());
		assertTrue(a.getEntropy() == b.getEntropy());
		assertEquals(a.getNumEvents(), b.getNumEvents());
		assertEquals(a.getNumFacts(), b.getNumFacts());
		assertNotEquals(a.getFacts(), b.getFacts());
		assertEquals(a.getEvents(), b.getEvents());
		assertNotEquals(a.getRelations(), b.getRelations());
	}

	@Test
	public void containFactTest() {
		sut.addStringsOfFacts("1", "2", "1>2");

		assertTrue(sut.containsFact(new Fact("2")));
		assertFalse(sut.containsFact(new Fact("3")));
	}
}
