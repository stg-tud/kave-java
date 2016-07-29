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
package cc.kave.episodes.mining.patterns;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.postprocessor.EpisodesPostprocessor;
import cc.recommenders.io.Logger;

public class FrequenciesAnalyzerTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Mock
	EpisodesPostprocessor postprocessor;
	
	private Map<Integer, Set<Episode>> patterns;
	
	private FrequenciesAnalyzer sut;
	
	@Before
	public void setup() {
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);
		
		patterns = Maps.newLinkedHashMap();
		patterns.put(1, Sets.newHashSet(createEpisode(5, 0.7, "1")));
	}

	private Episode createEpisode(int freq, double entropy, String...string) {
		Episode episode = new Episode();
		episode.addStringsOfFacts(string);
		episode.setFrequency(freq);
		episode.setBidirectMeasure(entropy);
		return episode;
	}
}
