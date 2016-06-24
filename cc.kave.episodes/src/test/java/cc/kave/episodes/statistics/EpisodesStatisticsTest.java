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
package cc.kave.episodes.statistics;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.statistics.EpisodesStatistics;

public class EpisodesStatisticsTest {
	
	private List<Episode> episodes;

	private EpisodesStatistics sut;
	
	@Before
	public void setup() {
		episodes = new LinkedList<>();
		episodes.add(createEpisode(2, 1, "1", "2", "3", "2>1", "2>3", "3>1"));
		episodes.add(createEpisode(3, 0.9, "1", "3", "4", "3>1", "4>1"));
		episodes.add(createEpisode(7, 0.6, "1", "3", "5", "3>1", "5>1"));
		episodes.add(createEpisode(2, 0.9, "1", "3", "4", "3>1", "4>1"));
		episodes.add(createEpisode(7, 1, "1", "3", "5"));
		
		sut = new EpisodesStatistics();
	}
	
	@Test
	public void frequencyTest() {
		Map<Integer, Integer> expected = Maps.newHashMap();
		expected.put(2, 2);
		expected.put(3, 1);
		expected.put(7, 2);
		
		Map<Integer, Integer> actuals = sut.freqsEpisodes(episodes);
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void bidirectTest() {
		Map<Double, Integer> expected = Maps.newHashMap();
		expected.put(1.0, 2);
		expected.put(0.9, 2);
		expected.put(0.6, 1);
		
		Map<Double, Integer> actuals = sut.bidirectEpisodes(episodes);
		
		assertEquals(expected, actuals);
	}
	
	private Episode createEpisode(int freq, double bdmeas, String... strings) {
		Episode episode = new Episode();
		episode.setFrequency(freq);
		episode.setBidirectMeasure(bdmeas);
		for (String fact : strings) {
			episode.addFact(fact);
		}
		return episode;
	} 
}
