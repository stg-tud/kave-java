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
import static org.junit.Assert.assertNull;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

import cc.kave.episodes.model.Episode;

public class QueryGeneratorByPercentageTest {

	private QueryGeneratorByPercentage sut;
	
	@Before 
	public void setup() {
		MockitoAnnotations.initMocks(this);
		sut = new QueryGeneratorByPercentage();
	}
	
	@Test
	public void noMethodInv() {
		Episode target = createEpisode("a");
		
		Set<Episode> actuals = sut.generateQueries(target, 0.5);
		
		assertNull(actuals);
	}
	
	@Test
	public void oneMethodInv() {
		Episode target = createEpisode("a", "b", "a>b");
		
		Set<Episode> expected = Sets.newHashSet(createEpisode("a"));
		
		Set<Episode> actuals = sut.generateQueries(target, 0.5);
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void twoMethodInv() {
		Episode target = createEpisode("a", "b", "c", "a>b", "a>c", "b>c");
		
		Set<Episode> expected = Sets.newHashSet(createEpisode("a", "b", "a>b"), createEpisode("a", "c", "a>c"));
		
		Set<Episode> actuals = sut.generateQueries(target, 0.25);
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void fourMethodInv() {
		Episode target = createEpisode("a", "b", "c", "d", "e", "a>b", "a>c", "a>d", "a>e",
											"b>c", "b>d");
		
		Set<Episode> expected = Sets.newHashSet(createEpisode("a", "b", "a>b"), createEpisode("a", "c", "a>c"), 
													createEpisode("a", "d", "a>d"), createEpisode("a", "e", "a>e"));
		
		Set<Episode> actuals = sut.generateQueries(target, 0.75);
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void fourMethod50pInv() {
		Episode target = createEpisode("a", "b", "c", "d", "e", "a>b", "a>c", "a>d", "a>e",
											"b>c", "b>d");
		
		Set<Episode> expected = Sets.newHashSet(createEpisode("a", "b", "c", "a>b", "a>c", "b>c"), 
												createEpisode("a", "b", "d", "a>b", "a>d", "b>d"), 
												createEpisode("a", "b", "e", "a>b", "a>e"), 
												createEpisode("a", "c", "d", "a>c", "a>d"),
												createEpisode("a", "c", "e", "a>c", "a>e"),
												createEpisode("a", "d", "e", "a>d", "a>e"));
		
		Set<Episode> actuals = sut.generateQueries(target, 0.5);
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void removeAllInvs() {
		Episode target = createEpisode("a", "b", "c", "a>b", "a>c");
		
		Set<Episode> expected = Sets.newHashSet(createEpisode("a"));
		
		Set<Episode> actuals = sut.generateQueries(target, 1.0);
		
		assertEquals(expected, actuals);
	}

	private Episode createEpisode(String...strings) {
		Episode episode = new Episode();
		for (String string : strings) {
			episode.addFact(string);
		}
		return episode;
	}
}
