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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Sets;

import cc.kave.episodes.model.Episode;
import cc.recommenders.exceptions.AssertionException;

public class TransitivelyClosedEpisodesTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Set<Episode> maximalEpisodes;

	private TransitivelyClosedEpisodes sut;

	@Before
	public void setup() {
		sut = new TransitivelyClosedEpisodes();
	}

	@Test
	public void emptyEpisode() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("The list of learned episodes is empty!");

		sut.remTransClosure(Sets.newHashSet());
	}

	@Test
	public void sameEpisode() {

		maximalEpisodes = Sets.newHashSet();
		Episode episode = newEpisode(3, 3, "a", "b", "c");
		maximalEpisodes.add(episode);

		Set<Episode> expected = Sets.newHashSet();
		episode = newEpisode(3, 3, "a", "b", "c");
		expected.add(episode);

		Set<Episode> actuals = sut.remTransClosure(maximalEpisodes);

		for (Episode ep : expected) {
			assertTrue(actuals.contains(ep));
		}
	}

	private Episode newEpisode(int freq, int numberElements, String... strings) {
		Episode episode = new Episode() {
		};
		episode.addStringsOfFacts(strings);
		return episode;
	}

	@Test
	public void removeClosures3() {
		maximalEpisodes = createSetOfEpisodes(3, true);

		Set<Episode> expected = createSetOfEpisodes(3, false);

		Set<Episode> actuals = sut.remTransClosure(maximalEpisodes);

		assertEquals(expected.size(), actuals.size());
		boolean comparison = false;
		for (Episode epExp : expected) {
			for (Episode epAct : actuals) {
				if (epExp.equals(epAct)) {
					comparison = true;
				}
			}
			assertTrue(comparison);
		}
	}

	@Test
	public void removeClosures4() {
		maximalEpisodes = createSetOfEpisodes(4, true);

		Set<Episode> expected = createSetOfEpisodes(4, false);

		Set<Episode> actuals = sut.remTransClosure(maximalEpisodes);

		assertEquals(expected.size(), actuals.size());
		boolean comparison = false;
		for (Episode epExp : expected) {
			for (Episode epAct : actuals) {
				if (epExp.equals(epAct)) {
					comparison = true;
				}
			}
			assertTrue(comparison);
		}
	}

	@Test
	public void removeClosures5() {
		maximalEpisodes = createSetOfEpisodes(5, true);

		Set<Episode> expected = createSetOfEpisodes(5, false);

		Set<Episode> actuals = sut.remTransClosure(maximalEpisodes);

		assertEquals(expected.size(), actuals.size());
		boolean comparison = false;
		for (Episode epExp : expected) {
			for (Episode epAct : actuals) {
				if (epExp.equals(epAct)) {
					comparison = true;
				}
			}
			assertTrue(comparison);
		}
	}

	private Set<Episode> createSetOfEpisodes(int i, boolean closure) {
		Set<Episode> episodes = Sets.newHashSet();
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
				episodes.add(createEpisode("f", "g", "h", "i", "j", "f>g", "f>h", "f>i", "f>j", "g>h", "g>i", "g>j",
						"h>j", "i>h", "i>j"));
			} else {
				episodes.add(createEpisode("a", "b", "c", "d", "e", "a>d", "a>e", "b>a", "b>c"));
				episodes.add(createEpisode("f", "g", "h", "i", "j", "f>g", "g>i", "i>h", "h>j"));
			}
		}
		return episodes;
	}

	private Episode createEpisode(String... string) {
		Episode episode = new Episode() {
		};
		for (String s : string) {
			episode.addFact(s);
		}
		return episode;
	}
}
