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

import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.model.Episode;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.exceptions.AssertionException;

public class SeparatorTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Episode episode;
	private Separator sut;

	@Before
	public void setup() {
		episode = createEpisode("11", "12", "13", "14", "11>12", "11>13", "11>14", "12>13", "12>14");
		sut = new Separator();
	}

	@Test
	public void oneEventEpisode() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Episode contains only one event!");
		sut.separateFacts(createEpisode("10"));
	}

	@Test
	public void oneEventgetBody() {
		Set<Fact> expected = Sets.newHashSet();
		Set<Fact> actuals = sut.getEpisodeBody(createEpisode("11"));

		assertEquals(expected, actuals);
	}

	@Test
	public void correctnessTest() {
		Set<Fact> invocations = Sets.newHashSet(new Fact("12"), new Fact("13"), new Fact("14"));
		Tuple<Fact, Set<Fact>> expected = Tuple.newTuple(new Fact(11), invocations);

		Tuple<Fact, Set<Fact>> actuals = sut.separateFacts(episode);

		assertEquals(expected, actuals);
	}

	@Test
	public void oneInvTest() {
		Episode ep = createEpisode("11", "12", "11>12");
		Tuple<Fact, Set<Fact>> expected = Tuple.newTuple(new Fact(11), Sets.newHashSet(new Fact(12)));

		Tuple<Fact, Set<Fact>> actuals = sut.separateFacts(ep);

		assertEquals(expected, actuals);
	}

	@Test
	public void getBodyTest() {
		Set<Fact> expected = Sets.newHashSet(new Fact(12), new Fact(13), new Fact(14), new Fact("12>13"),
				new Fact("12>14"));

		Set<Fact> actuals = sut.getEpisodeBody(episode);

		assertEquals(expected, actuals);
	}

	private Episode createEpisode(String... strings) {
		Episode episode = new Episode();
		for (String s : strings) {
			episode.addFact(s);
		}
		return episode;
	}
}
