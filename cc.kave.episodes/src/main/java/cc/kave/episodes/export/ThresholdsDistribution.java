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
package cc.kave.episodes.export;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.episodes.mining.reader.EpisodeParser;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.statistics.EpisodesStatistics;

public class ThresholdsDistribution {

	private File patternsFolder;
	private EpisodeParser parser;
	private EpisodesStatistics statistics;

	@Inject
	public ThresholdsDistribution(@Named("patterns") File folder, EpisodeParser parse, EpisodesStatistics stats) {
		assertTrue(folder.exists(), "Patterns folder does not exist");
		assertTrue(folder.isDirectory(), "Patterns is not a folder, but a file");
		this.patternsFolder = folder;
		this.parser = parse;
		this.statistics = stats;
	}

	public void writer(int numbRepos) {
		Map<Integer, Set<Episode>> episodes = parser.parse(numbRepos);
		
		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			Map<Integer, Integer> frequences = statistics.freqsEpisodes(entry.getValue());
			String freqsLevel = getStringRep(entry.getKey(), frequences);
		}

	}

	private String getStringRep(Integer epLevel, Map<Integer, Integer> frequences) {
		String data = "Frequency distribution for " + epLevel + "-node episodes:";
		
		for (Map.Entry<Integer, Integer> entry : frequences.entrySet()) {
			
		}
		return null;
	}
}
