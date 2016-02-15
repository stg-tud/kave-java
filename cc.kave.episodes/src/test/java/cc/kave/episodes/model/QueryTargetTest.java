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
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Fact;

public class QueryTargetTest {

	private QueryTarget sut;
	
	@Before
	public void setup() {
		sut = new QueryTarget();
	}
	
	@Test
	public void defaultValues() {
		assertEquals(Sets.newHashSet(), sut.getFacts());
		assertEquals(0, sut.getNumFacts());
		assertEquals(0, sut.getNumEvents());
	}
	
	@Test
	public void valuesCanBeSet() {
		sut.addFact("a");
		assertEquals(Sets.newHashSet(new Fact("a")), sut.getFacts());
		assertEquals(1, sut.getNumEvents());
	}
	
	@Test
	public void addMultipleFacts() {
		sut.addStringsOfFacts("a", "b");
		assertEquals(Sets.newHashSet(new Fact("a"), new Fact("b")), sut.getFacts());
		assertTrue(sut.getNumFacts() == 2);
	}
	
	@Test
	public void addListOfFacts() {
		List<Fact> facts = new LinkedList<Fact>();
		facts.add(new Fact("a"));
		facts.add(new Fact("b"));
		facts.add(new Fact("a>b"));
		
		sut.addListOfFacts(facts);
		
		assertEquals(Sets.newHashSet(new Fact("a"), new Fact("b"), new Fact("a>b")), sut.getFacts());
	}
	
	@Test
	public void equality_default() {
		QueryTarget a = new QueryTarget();
		QueryTarget b = new QueryTarget();
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertTrue(a.equals(b));
	}
	
	@Test
	public void equality_reallyTheSame() {
		QueryTarget a = new QueryTarget();
		a.addStringsOfFacts("a", "b", "c");

		QueryTarget b = new QueryTarget();
		b.addStringsOfFacts("a", "b", "c");

		assertEquals(a, b);
		assertTrue(a.equals(b));
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a.getNumEvents(), b.getNumEvents());
		assertEquals(a.getFacts(), b.getFacts());
		assertEquals(a.getNumFacts(), b.getNumFacts());
	}
	
	@Test
	public void differentFacts() {
		QueryTarget a = new QueryTarget();
		a.addStringsOfFacts("a", "b", "c");

		QueryTarget b = new QueryTarget();
		b.addStringsOfFacts("d", "e");
		
		assertNotEquals(a, b);
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		assertNotEquals(a.getNumEvents(), b.getNumEvents());
		assertNotEquals(a.getFacts(), b.getFacts());
		assertNotEquals(a.getNumFacts(), b.getNumFacts());
	}
}
