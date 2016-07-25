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
package cc.kave.episodes.mining.evaluation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.mining.reader.MappingParser;
import cc.kave.episodes.mining.reader.StreamParser;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.postprocessor.EpisodesPostprocessor;
import cc.recommenders.io.Logger;

public class PatternsIdentifierTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Mock
	private StreamParser streamParser;
	@Mock
	private MappingParser events;
	@Mock
	private EpisodesPostprocessor processor;
	
	Map<Integer, Set<Episode>> patterns;
	List<List<Fact>> stream;

	private PatternsIdentifier sut;

	@Before
	public void setup() {
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);
		
		patterns = Maps.newLinkedHashMap();
		Set<Episode> episodes = Sets.newLinkedHashSet();
		Episode ep = new Episode();
		ep.addFact(new Fact(1));
		episodes.add(ep);
		
		ep = new Episode();
		ep.addFact(new Fact(2));
		episodes.add(ep);
		patterns.put(1, episodes);
		
	}
}
