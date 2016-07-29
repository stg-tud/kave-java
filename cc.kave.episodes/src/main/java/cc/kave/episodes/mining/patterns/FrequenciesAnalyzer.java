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

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.postprocessor.EpisodesPostprocessor;
import cc.recommenders.io.Logger;

public class FrequenciesAnalyzer {
	
	private File patternsFolder;
	private EpisodesPostprocessor postprocessor;
	
	@Inject
	public FrequenciesAnalyzer(@Named("patterns") File folder, EpisodesPostprocessor processor) {
		assertTrue(folder.exists(), "Patterns folder does not exist");
		assertTrue(folder.isDirectory(), "Patterns folder is not a folder, but a file");
		this.patternsFolder = folder;
		this.postprocessor = processor;
	}
	
	public void analyzeSuperEpisodes(int numbRepos, int frequency, int entropy) throws IOException {
		Map<Integer, Set<Episode>> patterns = postprocessor.postprocess(numbRepos, frequency, entropy);
		StringBuilder sb = new StringBuilder();
		
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			if ((entry.getKey() < 2) || (entry.getKey() == patterns.size())) {
				continue;
			}
			sb.append(entry.getKey() + "-node episodes\n");
			Logger.log("Analyzing for %d-node episodes ...", entry.getKey());
			for (Episode episode : entry.getValue()) {
				Set<Episode> superEpisodes = getSuperEpisodes(episode, patterns.get(entry.getKey() + 1));
				if (superEpisodes.size() > 0) {
					sb.append(episode.toString() + "\t" + episode.getFrequency() + "\n");
					for (Episode sep : superEpisodes) {
						sb.append(sep.toString() + "\t" + sep.getFrequency() + "\n");
					}
				}
				sb.append("\n");
			}
			sb.append("\n");
		}
		FileUtils.writeStringToFile(getFilePath(numbRepos, frequency, entropy), sb.toString());
	}

	private Set<Episode> getSuperEpisodes(Episode episode, Set<Episode> condidates) {
		Set<Episode> superEpisodes = Sets.newHashSet();
		
		for (Episode sep : condidates) {
			if (sep.getEvents().containsAll(episode.getEvents())) {
				superEpisodes.add(sep);
			}
		}
		return superEpisodes;
	}
	
	private File getFilePath(int numbRepos, int freqThresh, double bidirectThresh) {
		File folderPath = new File(patternsFolder.getAbsolutePath() + "/Repos" + numbRepos + "/Freq" + freqThresh
				+ "/Bidirect" + bidirectThresh + "/");
		if (!folderPath.isDirectory()) {
			folderPath.mkdirs();
		}
		File fileName = new File(folderPath.getAbsolutePath() + "/superEpisodes.txt");
		return fileName;
	}
}
