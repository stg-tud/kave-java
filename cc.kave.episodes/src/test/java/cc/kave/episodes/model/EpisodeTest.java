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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Fact;
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
		;
		;
	}

	@Test
	public void defaultValues() {
		assertEquals(0, sut.getFrequency());
		assertEquals(0, sut.getNumFacts());

		assertEquals(Sets.newHashSet(), sut.getFacts());
	}

	@Test
	public void valuesCanBeSet() {
		sut.setFrequency(3);
		assertEquals(3, sut.getFrequency());
		sut.addFact("f");
		assertEquals(Sets.newHashSet(new Fact("f")), sut.getFacts());
		assertEquals(1, sut.getNumEvents());
		assertEquals(1, sut.getNumFacts());
	}

	@Test
	public void addMultipleFacts() {
		sut.addStringsOfFacts("f", "g", "f>g");
		assertEquals(Sets.newHashSet(new Fact("f"), new Fact("g"), new Fact("f>g")), sut.getFacts());
		assertEquals(sut.getNumEvents(), 2);
		assertEquals(sut.getNumFacts(), 3);
	}

	@Test
	public void addFactTest() {
		Fact fact = new Fact("f");
		sut.addFact(fact);
		sut.setFrequency(3);

		assertEquals(Sets.newHashSet(new Fact("f")), sut.getFacts());
		assertEquals(1, sut.getNumEvents());
		assertEquals(1, sut.getNumFacts());
		assertEquals(3, sut.getFrequency());
	}

	@Test
	public void addListOfFactTest() {
		List<Fact> facts = new LinkedList<Fact>();
		facts.add(new Fact("f"));
		facts.add(new Fact("g"));
		facts.add(new Fact("f>g"));

		sut.addListOfFacts(facts);
		sut.setFrequency(3);

		assertEquals(Sets.newHashSet(new Fact("f"), new Fact("g"), new Fact("f>g")), sut.getFacts());
		assertEquals(2, sut.getNumEvents());
		assertEquals(3, sut.getNumFacts());
		assertEquals(3, sut.getFrequency());
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

		Episode b = new Episode();
		b.addStringsOfFacts("1", "2", "3", "1>2", "1>3");
		b.setFrequency(3);

		assertEquals(a, b);
		assertTrue(a.equals(b));
		assertEquals(a.hashCode(), b.hashCode());

		assertEquals(a.getFrequency(), b.getFrequency());
		assertEquals(a.getNumEvents(), b.getNumEvents());
		assertEquals(a.getNumFacts(), b.getNumFacts());
		assertEquals(a.getFacts(), b.getFacts());
	}

	@Test
	public void equality_diffEvents() {
		Episode a = new Episode();
		a.addStringsOfFacts("1", "2", "3", "1>2", "1>3");
		a.setFrequency(3);

		Episode b = new Episode();
		b.addStringsOfFacts("3", "4", "3>4");
		b.setFrequency(3);

		assertNotEquals(a, b);
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());

		assertEquals(a.getFrequency(), b.getFrequency());
		assertNotEquals(a.getNumEvents(), b.getNumEvents());
		assertNotEquals(a.getNumFacts(), b.getNumFacts());
		assertNotEquals(a.getFacts(), b.getFacts());
	}

	@Test
	public void equality_sameEventsDiffRelations() {
		Episode a = new Episode();
		a.addStringsOfFacts("1", "2", "3", "1>2", "1>3");
		a.setFrequency(3);

		Episode b = new Episode();
		b.addStringsOfFacts("1", "2", "3", "1>2", "2>3");
		b.setFrequency(3);

		assertNotEquals(a, b);
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());

		assertEquals(a.getFrequency(), b.getFrequency());
		assertEquals(a.getNumEvents(), b.getNumEvents());
		assertEquals(a.getNumFacts(), b.getNumFacts());
		assertNotEquals(a.getFacts(), b.getFacts());
	}

	@Test
	public void diffFreq() {
		Episode a = new Episode();
		a.setFrequency(3);
		a.addStringsOfFacts("1", "2", "3", "1>2", "1>3");

		Episode b = new Episode();
		b.setFrequency(5);
		b.addStringsOfFacts("1", "2", "3", "1>2", "1>3");

		assertNotEquals(a, b);
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());

		assertNotEquals(a.getFrequency(), b.getFrequency());
		assertEquals(a.getNumEvents(), b.getNumEvents());
		assertEquals(a.getNumFacts(), b.getNumFacts());
		assertEquals(a.getFacts(), b.getFacts());
	}

	@Test
	public void containFactTest() {
		sut.addStringsOfFacts("1", "2", "1>2");

		assertTrue(sut.containsFact(new Fact("2")));
		assertFalse(sut.containsFact(new Fact("3")));
	}
}
