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
package cc.kave.episodes.evaluation.queries;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

import cc.kave.episodes.model.Episode;
import cc.recommenders.exceptions.AssertionException;

public class QueryStrategyTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Map<Double, Set<Episode>> expectedByPercent;
	private Map<Double, Set<Episode>> actualsByPercent;

	private Map<Double, Set<Episode>> expectedByNumber;
	private Map<Double, Set<Episode>> actualsByNumber;

	private QueryStrategy sut;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		expectedByPercent = new LinkedHashMap<Double, Set<Episode>>();
		actualsByPercent = new LinkedHashMap<Double, Set<Episode>>();

		expectedByNumber = new LinkedHashMap<Double, Set<Episode>>();
		actualsByNumber = new LinkedHashMap<Double, Set<Episode>>();

		sut = new QueryStrategy();
	}

	@Test
	public void emptyTarget() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Not valid episode for query generation!");
		sut.byPercentage(new Episode());
	}

	@Test
	public void twoMethodInv() {
		Episode target = createEpisode("11", "12", "13", "11>12", "11>13", "12>13");

		expectedByPercent.put(0.10,
				Sets.newHashSet(createEpisode("11", "12", "11>12"), createEpisode("11", "13", "11>13")));

		actualsByPercent = sut.byPercentage(target);

		assertEquals(expectedByPercent, actualsByPercent);
	}

	@Test
	public void fourMethodInv() {
		Episode target = createEpisode("11", "12", "13", "14", "15", "11>12", "11>13", "11>14", "11>15", "12>13",
				"12>14");

		expectedByPercent.put(0.10,
				Sets.newHashSet(createEpisode("11", "12", "13", "14", "11>12", "11>13", "11>14", "12>13", "12>14"),
						createEpisode("11", "12", "13", "15", "11>12", "11>13", "11>15", "12>13"),
						createEpisode("11", "12", "14", "15", "11>12", "11>14", "11>15", "12>14"),
						createEpisode("11", "13", "14", "15", "11>13", "11>14", "11>15")));

		expectedByPercent.put(0.30, Sets.newHashSet(createEpisode("11", "12", "13", "11>12", "11>13", "12>13"),
				createEpisode("11", "12", "14", "11>12", "11>14", "12>14"),
				createEpisode("11", "12", "15", "11>12", "11>15"), createEpisode("11", "13", "14", "11>13", "11>14"),
				createEpisode("11", "13", "15", "11>13", "11>15"), createEpisode("11", "14", "15", "11>14", "11>15")));

		expectedByPercent.put(0.60,
				Sets.newHashSet(createEpisode("11", "12", "11>12"), createEpisode("11", "13", "11>13"),
						createEpisode("11", "14", "11>14"), createEpisode("11", "15", "11>15")));

		actualsByPercent = sut.byPercentage(target);

		assertByPercent(expectedByPercent, actualsByPercent);
	}

	@Test
	public void byNumbertest() {
		Episode target = createEpisode("11", "12", "13", "14", "15", "11>12", "11>13", "11>14", "11>15", "12>13",
				"12>14");

		expectedByNumber.put(1.0,
				Sets.newHashSet(createEpisode("11", "12", "13", "14", "11>12", "11>13", "11>14", "12>13", "12>14"),
						createEpisode("11", "12", "13", "15", "11>12", "11>13", "11>15", "12>13"),
						createEpisode("11", "12", "14", "15", "11>12", "11>14", "11>15", "12>14"),
						createEpisode("11", "13", "14", "15", "11>13", "11>14", "11>15")));

		expectedByNumber.put(2.0, Sets.newHashSet(createEpisode("11", "12", "13", "11>12", "11>13", "12>13"),
				createEpisode("11", "12", "14", "11>12", "11>14", "12>14"),
				createEpisode("11", "12", "15", "11>12", "11>15"), createEpisode("11", "13", "14", "11>13", "11>14"),
				createEpisode("11", "13", "15", "11>13", "11>15"), createEpisode("11", "14", "15", "11>14", "11>15")));

		expectedByNumber.put(3.0, Sets.newHashSet(createEpisode("11", "12", "11>12"), createEpisode("11", "13", "11>13"),
				createEpisode("11", "14", "11>14"), createEpisode("11", "15", "11>15")));

		actualsByNumber = sut.byNumber(target);

		assertByNumber(expectedByNumber, actualsByNumber);
	}

	private void assertByNumber(Map<Double, Set<Episode>> expected, Map<Double, Set<Episode>> actuals) {
		assertTrue(expected.size() == actuals.size());
		for (Map.Entry<Double, Set<Episode>> entry : expected.entrySet()) {
			assertTrue(entry.getValue().size() == actuals.get(entry.getKey()).size());
			
			Iterator<Episode> itE = entry.getValue().iterator();
			Iterator<Episode> itA = actuals.get(entry.getKey()).iterator();
			while (itE.hasNext()) {
				assertEquals(itE.next(), itA.next());
			} 
		}

	}

	private void assertByPercent(Map<Double, Set<Episode>> expected, Map<Double, Set<Episode>> actuals) {
		assertTrue(expected.size() == actuals.size());
		for (Map.Entry<Double, Set<Episode>> entry : expected.entrySet()) {
			assertTrue(entry.getValue().size() == actuals.get(entry.getKey()).size());

			Iterator<Episode> itE = entry.getValue().iterator();
			Iterator<Episode> itA = actuals.get(entry.getKey()).iterator();
			while (itE.hasNext()) {
				assertEquals(itE.next(), itA.next());
			}
		}
	}

	private Episode createEpisode(String... strings) {
		Episode episode = new Episode();
		for (String string : strings) {
			episode.addFact(string);
		}
		return episode;
	}
}
