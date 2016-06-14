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
package cc.kave.episodes.mining.reader;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.episodes.model.Episode;

public class EpisodeParser {

	private File rootFolder;
	private FileReader reader;

	@Inject
	public EpisodeParser(@Named("rootDir") File directory, FileReader reader) {
		assertTrue(directory.exists(), "Frequent episode folder does not exist");
		assertTrue(directory.isDirectory(), "Frequent episode folder is not a folder, but a file");
		this.rootFolder = directory;
		this.reader = reader;
	}

	public Map<Integer, Set<Episode>> parse(int freq, double bd) {

		File filePath = getFilePath(freq, bd);
		List<String> lines = reader.readFile(filePath);

		Map<Integer, Set<Episode>> episodeIndexed = new HashMap<Integer, Set<Episode>>();
		Set<Episode> episodes = Sets.newHashSet();

		String[] rowValues;
		int numNodes = 0;

		for (String line : lines) {
			if (line.contains(":")) {
				rowValues = line.split(":");
				Episode episode = readEpisode(numNodes, rowValues);
				episodes.add(episode);
			} else {
				rowValues = line.split("\\s+");
				if (!episodes.isEmpty()) {
					episodeIndexed.put(numNodes, episodes);
				}
				if (Integer.parseInt(rowValues[3]) > 0) {
					String[] nodeString = rowValues[0].split("-");
					numNodes = Integer.parseInt(nodeString[0]);
					episodes = Sets.newHashSet();
				}
			}
		}
		episodeIndexed.put(numNodes, episodes);
		return episodeIndexed;
	}

	private Episode readEpisode(int numberOfNodes, String[] rowValues) {
		Episode episode = new Episode();
		episode.setFrequency(Integer.parseInt(rowValues[1].trim()));
		String[] events = rowValues[0].split("\\s+");
		for (int idx = 0; idx < numberOfNodes; idx++) {
			episode.addFact(events[idx]);
		}
		if (rowValues[3].contains(",")) {
			String[] relations = rowValues[3].substring(2).split(",");
			for (String relation : relations) {
				episode.addFact(relation);
			}
		}
		return episode;
	}

	private File getFilePath(int freq, double bd) {
		String fileName = rootFolder.getAbsolutePath() + "/outputF" + freq + "B" + bd + ".txt";
		File file = new File(fileName);
		return file;
	}
}
