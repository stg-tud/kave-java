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
package cc.kave.episodes.postprocessor;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;

public class TransClosedEpisodesTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private TransClosedEpisodes sut;

	@Before
	public void setup() {
		sut = new TransClosedEpisodes();
	}

	@Test
	public void sameEpisode() {

		Episode episode = newEpisode(3, 3, "a", "b", "c");

		Episode expected = newEpisode(3, 3, "a", "b", "c");

		Episode actuals = sut.remTransClosure(episode);

		assertEquals(expected, actuals);
	}

	private Episode newEpisode(int freq, int numberElements, String... strings) {
		Episode episode = new Episode();
		episode.addStringsOfFacts(strings);
		return episode;
	}

	@Test
	public void removeClosures3() {
		Episode episode = createEpisode("a", "b", "c", "d", "e", "a>d", "a>e", "b>a", "b>c", "b>d", "b>e");

		Episode expected = createEpisode("a", "b", "c", "d", "e", "a>d", "a>e", "b>a", "b>c");

		Episode actuals = sut.remTransClosure(episode);

		assertEquals(expected, actuals);
	}

	@Test
	public void removeClosures4() {
		Episode episode = createEpisode("f", "g", "h", "i", "j", "f>g", "f>h", "f>i", "f>j", "g>h", "g>i", "g>j", "h>j",
				"i>h", "i>j");

		Episode expected = createEpisode("f", "g", "h", "i", "j", "f>g", "g>i", "i>h", "h>j");

		Episode actuals = sut.remTransClosure(episode);

		assertEquals(expected, actuals);
	}

	@Test
	public void removeClosures5() {
		Episode episode = createEpisode("a", "b", "c", "d", "a>b", "a>c", "a>d", "b>d", "c>d");

		Episode expected = createEpisode("a", "b", "c", "d", "a>b", "a>c", "b>d", "c>d");

		Episode actuals = sut.remTransClosure(episode);

		assertEquals(expected, actuals);
	}

	private Episode createEpisode(String... string) {
		Episode episode = new Episode();
		episode.addStringsOfFacts(string);
		return episode;
	}
}
