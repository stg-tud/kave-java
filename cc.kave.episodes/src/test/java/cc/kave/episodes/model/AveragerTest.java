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
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cc.kave.commons.exceptions.AssertionException;

public class AveragerTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Averager sut;

	@Before
	public void setup() {
		sut = new Averager();
	}

	@Test
	public void valueTest() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Enter a proper similarity value, between 0 and 1!");
		sut.addValue(2.0);
	}

	@Test
	public void value2Test() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Enter a proper similarity value, between 0 and 1!");
		sut.addValue(-2.0);
	}

	@Test
	public void addValues() {
		sut.addValue(0.7);
		sut.addValue(0.3);
		sut.addValue(0.5);

		List<Double> expected = new LinkedList<Double>();
		expected.add(0.7);
		expected.add(0.3);
		expected.add(0.5);

		assertEquals(expected, sut.getValues());
	}

	@Test
	public void clearAverager() {
		sut.addValue(0.7);
		sut.addValue(0.3);
		sut.addValue(0.5);

		assertTrue(sut.clear().isEmpty());
	}

	@Test
	public void average() {
		sut.addValue(0.7);
		sut.addValue(0.3);
		sut.addValue(0.5);

		double expected = 0.5;

		assertTrue(expected == sut.average());
	}
}
