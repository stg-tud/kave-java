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

import java.util.HashMap;
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

public class QueryGeneratorByPercentageTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private Map<Double, Set<Episode>> expected;
	private Map<Double, Set<Episode>> actuals;
	
	private QueryGeneratorByPercentage sut;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		expected = new HashMap<Double, Set<Episode>>();
		
		sut = new QueryGeneratorByPercentage();
	}

	@Test
	public void emptyTarget() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Not valid episode for query generation!");
		sut.generateQueries(new Episode());
	}

	@Test
	public void twoMethodInv() {
		Episode target = createEpisode("11", "12", "13", "11>12", "11>13", "12>13");

		expected.put(0.25, Sets.newHashSet(createEpisode("11", "12", "11>12"), createEpisode("11", "13", "11>13")));

		actuals = sut.generateQueries(target);

		assertEquals(expected, actuals);
	}

	@Test
	public void fourMethodInv() {
		Episode target = createEpisode("11", "12", "13", "14", "15", "11>12", "11>13", "11>14", "11>15", "12>13",
				"12>14");
		
		expected.put(0.25, Sets.newHashSet(createEpisode("11", "12", "13", "14", "11>12", "11>13", "11>14", "12>13", "12>14"), 
											createEpisode("11", "12", "13", "15", "11>12", "11>13", "11>15", "12>13"), 
											createEpisode("11", "12", "14", "15", "11>12", "11>14", "11>15", "12>14"),
											createEpisode("11", "13", "14", "15", "11>13", "11>14", "11>15")));
		
		expected.put(0.5, Sets.newHashSet(createEpisode("11", "12", "13", "11>12", "11>13", "12>13"), 
											createEpisode("11", "12", "14", "11>12", "11>14", "12>14"), 
											createEpisode("11", "12", "15", "11>12", "11>15"), 
											createEpisode("11", "13", "14", "11>13", "11>14"),
											createEpisode("11", "13", "15", "11>13", "11>15"), 
											createEpisode("11", "14", "15", "11>14", "11>15")));
		
		expected.put(0.75, Sets.newHashSet(createEpisode("11", "12", "11>12"), createEpisode("11", "13", "11>13"), 
											createEpisode("11", "14", "11>14"), createEpisode("11", "15", "11>15")));

		actuals = sut.generateQueries(target);

		assertEquals(expected, actuals);
	}

	private Episode createEpisode(String... strings) {
		Episode episode = new Episode();
		for (String string : strings) {
			episode.addFact(string);
		}
		return episode;
	}
}
