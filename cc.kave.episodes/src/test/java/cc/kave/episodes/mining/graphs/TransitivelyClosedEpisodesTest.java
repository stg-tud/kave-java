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
package cc.kave.episodes.mining.graphs;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cc.kave.episodes.model.Episode;

public class TransitivelyClosedEpisodesTest {

	private List<Episode> maximalEpisodes;
	private TransitivelyClosedEpisodes sut;
	
	@Before
	public void setup() {
		sut = new TransitivelyClosedEpisodes();
	}

	@Test(expected=Exception.class)
	public void emptyEpisode() throws Exception {
		sut.removeTransitivelyClosure(new LinkedList<Episode>());
	}
	
	@Test
	public void sameEpisode() throws Exception {
		
		maximalEpisodes = new LinkedList<Episode>();
		Episode episode = newEpisode(3, 3, "a", "b", "c");
		maximalEpisodes.add(episode);
		
		List<Episode> expected = new LinkedList<Episode>();
		episode = newEpisode(3, 3, "a", "b", "c");
		expected.add(episode);
		
		List<Episode> actuals = sut.removeTransitivelyClosure(maximalEpisodes);
				
		for (int idx = 0; idx < expected.size(); idx++) {
			assertTrue(expected.get(idx).equals(actuals.get(idx)));
		}
	}
	
	private Episode newEpisode(int freq, int numberElements, String...strings) {
		Episode episode = new Episode(){};
		episode.addStringsOfFacts(strings);
		return episode;
	}
	
	@Test
	public void removeClosures3() throws Exception {
		maximalEpisodes = createListOfEpisodes(3, true);
		
		List<Episode> expected = createListOfEpisodes(3, false);
		
		List<Episode> actuals = sut.removeTransitivelyClosure(maximalEpisodes);
		
		for (int idx = 0; idx < expected.size(); idx++) {
			assertTrue(expected.get(idx).equals(actuals.get(idx)));
		}
	}
	
	@Test
	public void removeClosures4() throws Exception {
		maximalEpisodes = createListOfEpisodes(4, true);
		
		List<Episode> expected = createListOfEpisodes(4, false);
		
		List<Episode> actuals = sut.removeTransitivelyClosure(maximalEpisodes);
		
		for (int idx = 0; idx < expected.size(); idx++) {
			assertTrue(expected.get(idx).equals(actuals.get(idx)));
		}
	}

	@Test
	public void removeClosures5() throws Exception {
		maximalEpisodes = createListOfEpisodes(5, true);
		
		List<Episode> expected = createListOfEpisodes(5, false);
		
		List<Episode> actuals = sut.removeTransitivelyClosure(maximalEpisodes);
		
		for (int idx = 0; idx < expected.size(); idx++) {
			assertTrue(expected.get(idx).equals(actuals.get(idx)));
		}
	}
	
	private List<Episode> createListOfEpisodes(int i, boolean closure) {
		List<Episode> episodes = new LinkedList<Episode>();
		if (i == 1) {
			episodes.add(createEpisode("a"));
		} else if (i == 2) {
			episodes.add(createEpisode("a", "b"));
			episodes.add(createEpisode("c", "d", "c>d"));
		} else if (i == 3) {
			if (closure) {
				episodes.add(createEpisode("a", "b", "c", "a>b", "a>c", "b>c"));
				episodes.add(createEpisode("c", "d", "e", "c>d", "c>e"));
			} else {
				episodes.add(createEpisode("a", "b", "c", "a>b", "b>c"));
				episodes.add(createEpisode("c", "d", "e", "c>d", "c>e"));
			}
		} else if (i == 4) {
			if (closure) {
				episodes.add(createEpisode("a", "b", "c", "d", "a>b", "a>c", "a>d", "b>d", "c>d"));
			} else {
				episodes.add(createEpisode("a", "b", "c", "d", "a>b", "a>c", "b>d", "c>d"));
			}
		} else if (i == 5) {
			if (closure) {
				episodes.add(createEpisode("a", "b", "c", "d", "e", "a>d", "a>e", "b>a", "b>c", "b>d", "b>e"));
				episodes.add(createEpisode("f", "g", "h", "i", "j", "f>g", "f>h", "f>i", "f>j", "g>h", "g>i", "g>j", "h>j", "i>h", "i>j"));
			} else {
				episodes.add(createEpisode("a", "b", "c", "d", "e", "a>d", "a>e", "b>a", "b>c"));
				episodes.add(createEpisode("f", "g", "h", "i", "j", "f>g", "g>i", "i>h", "h>j"));
			}
		}
		return episodes;
	}

	private Episode createEpisode(String... string) {
		Episode episode = new Episode(){};
		for (String s : string) {
			episode.addFact(s);
		}
		return episode;
	}
}
