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
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.model.Episode;
import cc.recommenders.datastructures.Tuple;

public class FactsSeparatorTest {
	
	private Episode episode;
	private FactsSeparator sut;
	
	@Before
	public void setup() {
		episode = createEpisode("a", "b", "c", "d", "a>b", "a>c", "a>d");
		sut = new FactsSeparator();
	}
	
	@Test
	public void correctnessTest() {
		Set<Fact> invocations = Sets.newHashSet(new Fact("b"), new Fact("c"), new Fact("d"));
		Tuple<Fact, Set<Fact>> expected = Tuple.newTuple(new Fact("a"), invocations);
		
		Tuple<Fact, Set<Fact>> actuals = sut.separate(episode.getFacts());
		
		assertEquals(expected, actuals);
	}

	private Episode createEpisode(String...strings) {
		Episode episode = new Episode();
		for (String s : strings) {
			episode.addFact(s);
		}
		return episode;
	}
}
