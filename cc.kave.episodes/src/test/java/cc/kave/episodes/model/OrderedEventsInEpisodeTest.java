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

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class OrderedEventsInEpisodeTest {

	private OrderedEventsInEpisode sut;
	
	@Before
	public void setup() {
		sut = new OrderedEventsInEpisode();
	}
	
	@Test
	public void defaultValues() {
		assertEquals(Lists.newLinkedList(), sut.getPartialOrderList());
		assertEquals(Lists.newLinkedList(), sut.getSequentialOrderList());
	}
	
	@Test
	public void valuesCanBeSet() {	
		sut.setPartialOrderList(Lists.newArrayList("a"));;
		assertEquals(Lists.newArrayList("a"), sut.getPartialOrderList());
		sut.setSequentialOrderList(Lists.newArrayList("b c"));
		assertEquals(Lists.newArrayList("b c"), sut.getSequentialOrderList());
	}
	
	@Test
	public void addEventInSequentialList() {
		sut.addEventIDInSequentialOrderList("a");;
		assertEquals(Lists.newArrayList("a"), sut.getSequentialOrderList());
	}
	
	@Test
	public void addPartialEventsInSequentialList() {
		sut.addPartialEventsIDInSequentialOrderList("a", "b", "c");
		assertEquals(Lists.newArrayList("a b c"), sut.getSequentialOrderList());
	}
	
	@Test
	public void eventContainedInSequentialList() {
		sut.addEventIDInSequentialOrderList("a");
		assertTrue(sut.eventInSequentialOrderList("a"));
	}
	
	@Test
	public void eventNotContainedInSequentialList() {
		sut.addEventIDInSequentialOrderList("a");
		assertFalse(sut.eventInSequentialOrderList("b"));
	}
	
	@Test
	public void setEventIDInPartialOrderList() {
		sut.addEventIDInPartialOrderList("a");
		assertEquals(Lists.newArrayList("a"), sut.getPartialOrderList());
	}
	
	@Test
	public void equality_default() {
		OrderedEventsInEpisode a = new OrderedEventsInEpisode();
		OrderedEventsInEpisode b = new OrderedEventsInEpisode();
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void equality_reallyTheSame() {
		OrderedEventsInEpisode a = new OrderedEventsInEpisode();
		a.addEventIDInSequentialOrderList("a");

		OrderedEventsInEpisode b = new OrderedEventsInEpisode();
		b.addEventIDInSequentialOrderList("a");

		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a.getSequentialOrderList(), b.getSequentialOrderList());
	}
	
	@Test
	public void equality_diffSequentialList() {
		OrderedEventsInEpisode a = new OrderedEventsInEpisode();
		a.addEventIDInSequentialOrderList("a");

		OrderedEventsInEpisode b = new OrderedEventsInEpisode();
		b.addEventIDInSequentialOrderList("b");

		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
		Assert.assertNotEquals(a.getSequentialOrderList(), b.getSequentialOrderList());
	}
	
	@Test
	public void equality_diffPartialList() {
		OrderedEventsInEpisode a = new OrderedEventsInEpisode();
		a.addEventIDInPartialOrderList("a");

		OrderedEventsInEpisode b = new OrderedEventsInEpisode();
		b.addEventIDInPartialOrderList("b");

		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
		Assert.assertNotEquals(a.getPartialOrderList(), b.getPartialOrderList());
	}
	
	@Test
	public void equality_sameSequentialdiffPartialList() {
		OrderedEventsInEpisode a = new OrderedEventsInEpisode();
		a.addPartialEventsIDInSequentialOrderList("a", "b", "c");
		a.addEventIDInPartialOrderList("d");

		OrderedEventsInEpisode b = new OrderedEventsInEpisode();
		b.addPartialEventsIDInSequentialOrderList("a", "b", "c");
		b.addEventIDInPartialOrderList("e");

		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
		assertEquals(a.getSequentialOrderList(), b.getSequentialOrderList());
		Assert.assertNotEquals(a.getPartialOrderList(), b.getPartialOrderList());
	}
}
