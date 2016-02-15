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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Fact;

public class QueryTest {

	private Query sut;
	
	@Before
	public void setup() {
		sut = new Query();
	}
	
	@Test
	public void defaultValues() {
		assertEquals(0, sut.getNumEvents());
		assertEquals(Sets.newHashSet(), sut.getFacts());
		assertEquals(0, sut.getNumRemovedEvents());
		assertNull(sut.getQueryTarget());
	}
	
	@Test
	public void valuesCanBeSet() {
		sut.addStringsOfFacts("f");
		assertEquals(Sets.newHashSet(new Fact("f")), sut.getFacts());
		assertEquals(1, sut.getNumEvents());
		sut.setNumRemovedEvents(1);
		assertEquals(1, sut.getNumRemovedEvents());
		sut.setQueryTarget(new QueryTarget());
		assertEquals(new QueryTarget(), sut.getQueryTarget());
	}
	
	@Test
	public void addMultipleFacts() {
		sut.addStringsOfFacts("f", "g");
		assertEquals(Sets.newHashSet(new Fact("f"), new Fact("g")), sut.getFacts());
		assertTrue(sut.getNumFacts() == 2);
	}
	
	@Test
	public void addFactTest() {
		sut.addFact(new Fact("a"));
		assertEquals(Sets.newHashSet(new Fact("a")), sut.getFacts());
	}
	
	@Test
	public void addListFacts() {
		List<Fact> facts = new LinkedList<>();
		facts.add(new Fact("a"));
		facts.add(new Fact("b"));
		facts.add(new Fact("a>b"));
		
		sut.addListOfFacts(facts);
		
		assertEquals(Sets.newHashSet(new Fact("a"), new Fact("b"), new Fact("a>b")), sut.getFacts());
	}
	
	@Test
	public void containsFactTest() {
		sut.addFact("a");
		sut.addFact("b");
		sut.addFact("a>b");
		sut.addFact("c");
		
		assertTrue(sut.containsFact(new Fact("a>b")));
		assertFalse(sut.containsFact(new Fact("d")));
	}
	
	@Test
	public void equality_default() {
		Query a = new Query();
		a.setQueryTarget(new QueryTarget());
		Query b = new Query();
		b.setQueryTarget(new QueryTarget());
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertTrue(a.equals(b));
	}
	
	@Test
	public void equality_reallyTheSame() {
		QueryTarget qt = new QueryTarget();
		qt.addStringsOfFacts("1", "2", "3", "4", "1>2", "1>3", "1>4", "2>4");
		
		Query a = new Query();
		a.addStringsOfFacts("1", "2", "3", "1>2", "1>3");
		a.setQueryTarget(qt);

		Query b = new Query();
		b.addStringsOfFacts("1", "2", "3", "1>2", "1>3");
		b.setQueryTarget(qt);

		assertEquals(a, b);
		assertTrue(a.equals(b));
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a.getFacts(), b.getFacts());
	}
	
	@Test
	public void diffNumEvents() {
		QueryTarget qt = new QueryTarget();
		qt.addStringsOfFacts("1", "2", "3", "4", "1>2", "1>3", "1>4", "2>4");
		
		Query a = new Query();
		a.addStringsOfFacts("1", "2", "1>2");
		a.setQueryTarget(qt);
		a.setNumRemovedEvents(3);

		Query b = new Query();
		b.addStringsOfFacts("1", "2", "3", "1>2", "1>3");
		b.setQueryTarget(qt);
		b.setNumRemovedEvents(5);

		assertNotEquals(a, b);
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		assertNotEquals(a.getFacts(), b.getFacts());
		assertNotEquals(a.getNumEvents(), b.getNumEvents());
	}
	
	@Test
	public void diffNumFacts() {
		QueryTarget qt = new QueryTarget();
		qt.addStringsOfFacts("1", "2", "3", "4", "1>2", "1>3", "1>4", "2>4");
		
		Query a = new Query();
		a.addStringsOfFacts("1", "2", "3", "1>2");
		a.setQueryTarget(qt);
		a.setNumRemovedEvents(3);

		Query b = new Query();
		b.addStringsOfFacts("1", "2", "3", "1>2", "1>3");
		b.setQueryTarget(qt);
		b.setNumRemovedEvents(5);

		assertNotEquals(a, b);
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		assertNotEquals(a.getFacts(), b.getFacts());
		assertEquals(a.getNumEvents(), b.getNumEvents());
		assertNotEquals(a.getNumFacts(), b.getNumFacts());
	}
	
	@Test
	public void diffFacts() {
		QueryTarget qt = new QueryTarget();
		qt.addStringsOfFacts("1", "2", "3", "4", "1>2", "1>3", "1>4", "2>4");
		
		Query a = new Query();
		a.addStringsOfFacts("1", "2", "3", "1>2");
		a.setQueryTarget(qt);
		a.setNumRemovedEvents(3);

		Query b = new Query();
		b.addStringsOfFacts("1", "2", "3", "1>3");
		b.setQueryTarget(qt);
		b.setNumRemovedEvents(5);

		assertNotEquals(a, b);
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		assertEquals(a.getNumEvents(), b.getNumEvents());
		assertEquals(a.getNumFacts(), b.getNumFacts());
		assertNotEquals(a.getFacts(), b.getFacts());
	}
	
	@Test
	public void diffRemovedEvents() {
		QueryTarget qt = new QueryTarget();
		qt.addStringsOfFacts("1", "2", "3", "4", "1>2", "1>3", "1>4", "2>4");
		
		Query a = new Query();
		a.addStringsOfFacts("1", "2", "3", "1>2");
		a.setQueryTarget(qt);
		a.setNumRemovedEvents(3);

		Query b = new Query();
		b.addStringsOfFacts("1", "2", "3", "1>2");
		b.setQueryTarget(qt);
		b.setNumRemovedEvents(5);

		assertNotEquals(a, b);
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		assertEquals(a.getNumEvents(), b.getNumEvents());
		assertEquals(a.getNumFacts(), b.getNumFacts());
		assertEquals(a.getFacts(), b.getFacts());
		assertNotEquals(a.getNumRemovedEvents(), b.getNumRemovedEvents());
	}
	
	@Test
	public void diffQueryTarget() {
		QueryTarget qt1 = new QueryTarget();
		qt1.addStringsOfFacts("1", "2", "3", "4", "1>2", "1>3", "1>4", "2>4");
		
		Query a = new Query();
		a.addStringsOfFacts("1", "2", "3", "1>2");
		a.setQueryTarget(qt1);
		a.setNumRemovedEvents(1);

		QueryTarget qt2 = new QueryTarget();
		qt2.addStringsOfFacts("1", "2", "3", "4", "1>2", "2>4");
		
		Query b = new Query();
		b.addStringsOfFacts("1", "2", "3", "1>2");
		b.setQueryTarget(qt2);
		b.setNumRemovedEvents(1);

		assertNotEquals(a, b);
		assertFalse(a.equals(b));
		assertNotEquals(a.hashCode(), b.hashCode());
		assertEquals(a.getNumEvents(), b.getNumEvents());
		assertEquals(a.getNumFacts(), b.getNumFacts());
		assertEquals(a.getFacts(), b.getFacts());
		assertEquals(a.getNumRemovedEvents(), b.getNumRemovedEvents());
		assertNotEquals(a.getQueryTarget(), b.getQueryTarget());
	}
}
