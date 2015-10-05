/**
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package episodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.episodes.Episode;

public class EpisodeTest {

	private Episode sut;

	@Before
	public void setup() {
		sut = new Episode();
	}

	@Test
	public void defaultValues() {
		assertEquals(0, sut.getNumEvents());
		assertEquals(Lists.newLinkedList(), sut.getFacts());
	}

	@Test
	public void valuesCanBeSet() {
		sut.setNumEvents(2);
		assertEquals(2, sut.getNumEvents());
		sut.addFact("f");
		assertEquals(Lists.newArrayList("f"), sut.getFacts());
	}

	@Test
	public void equality_default() {
		Episode a = new Episode();
		Episode b = new Episode();
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equality_reallyTheSame() {
		Episode a = new Episode();
		a.addFact("1");
		a.addFact("2");
		a.addFact("3");
		a.addFact("1>2");
		a.addFact("1>3");

		Episode b = new Episode();
		b.addFact("1");
		b.addFact("2");
		b.addFact("3");
		b.addFact("1>2");
		b.addFact("1>3");

		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a.getFacts(), b.getFacts());
	}

	@Test
	public void equality_diffEvents() {
		Episode a = new Episode();
		a.addFact("1");
		a.addFact("2");
		a.addFact("3");
		a.addFact("1>2");
		a.addFact("1>3");

		Episode b = new Episode();
		b.addFact("3");
		b.addFact("4");
		b.addFact("3>4");

		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
		assertNotEquals(a.getFacts(), b.getFacts());
	}

	@Test
	public void equality_sameEventsDiffRelations() {
		Episode a = new Episode();
		a.addFact("1");
		a.addFact("2");
		a.addFact("3");
		a.addFact("1>2");
		a.addFact("1>3");

		Episode b = new Episode();
		b.addFact("1");
		b.addFact("2");
		b.addFact("3");
		b.addFact("1>2");

		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
		assertNotEquals(a.getFacts(), b.getFacts());
	}
}