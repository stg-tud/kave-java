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
package cc.kave.episodes.eventstream;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.statistics.EpisodesStatistics;
import cc.recommenders.io.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ThresholdsBidirection {

	private File patternsFolder;
	private EpisodesParser parser;
	private EpisodesStatistics statistics;

	@Inject
	public ThresholdsBidirection(@Named("patterns") File folder, EpisodesParser parse, EpisodesStatistics stats) {
		assertTrue(folder.exists(), "Patterns folder does not exist");
		assertTrue(folder.isDirectory(), "Patterns is not a folder, but a file");
		this.patternsFolder = folder;
		this.parser = parse;
		this.statistics = stats;
	}

	public void writer(int frequency, EpisodeType episodeType) throws IOException {
		Map<Integer, Set<Episode>> episodes = parser.parse(frequency, episodeType);
		StringBuilder bdsBuilder = new StringBuilder();
		
		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			if (entry.getKey() == 1) {
				continue;
			}
			Logger.log("Writting %d - node episodes", entry.getKey());
			Map<Double, Integer> bds = statistics.bidirectEpisodes(entry.getValue(), frequency);
			String bdsLevel = getBdsStringRep(entry.getKey(), bds);
			bdsBuilder.append(bdsLevel);
		}
		FileUtils.writeStringToFile(new File(getPath(frequency)), bdsBuilder.toString());
	}

	private String getBdsStringRep(Integer epLevel, Map<Double, Integer> bds) {
		String data = "Bidirectional distribution for " + epLevel + "-node episodes:\n";
		data += "Bidirectional\tCounter\n";
		
		for (Map.Entry<Double, Integer> entry : bds.entrySet()) {
			data += entry.getKey() + "\t" + entry.getValue() + "\n";
		}
		data += "\n";
		return data;
	}
	
	private String getPath(int freq) {
		String paths = patternsFolder.getAbsolutePath() + "Freq" + freq + "Repos.txt";
		return paths;
	}
}
