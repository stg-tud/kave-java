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
package cc.kave.episodes.io;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class EpisodesParser {

	private File eventsDir;
	private FileReader reader;
	
	@Inject
	public EpisodesParser(@Named("events") File rootFolder, FileReader reader) {
		assertTrue(rootFolder.exists(), "Events folder does not exist");
		assertTrue(rootFolder.isDirectory(),
				"Events is not a folder, but a file");
		this.eventsDir = rootFolder;
		this.reader = reader;
	}
	
	public Map<Integer, Set<Episode>> parse(int frequency, EpisodeType episodeType) {

		List<String> lines = reader.readFile(getEpisodesFile(frequency, episodeType));

		Map<Integer, Set<Episode>> episodeIndexed = Maps.newLinkedHashMap();
		Set<Episode> episodes = Sets.newLinkedHashSet();

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
					episodes = Sets.newLinkedHashSet();
				} else {
					break;
				}
			}
		}
		if (!episodeIndexed.containsKey(numNodes)) {
			episodeIndexed.put(numNodes, episodes);
		}
		return episodeIndexed;
	}
	
	private String getPath(int frequency) {
		File path = new File(eventsDir.getAbsolutePath() + "/freq" + frequency + "/TrainingData/fold0");
		if (!path.isDirectory()) {
			path.mkdirs();
		}
		return path.getAbsolutePath();
	}

	private File getEpisodesFile(int frequency, EpisodeType episodeType) {
		String type = "";
		if (episodeType == EpisodeType.SEQUENTIAL) {
			type = "Seq";
		} else if (episodeType == EpisodeType.GENERAL) {
			type = "Mix";
		} else {
			type = "Par";
		}
		File fileName = new File(getPath(frequency) + "/episodes" + type + ".txt");
		return fileName;
	}
	
	private File getEpisodesJsons(int frequency, EpisodeType episodeType) {
		String type = "";
		if (episodeType == EpisodeType.SEQUENTIAL) {
			type = "Seq";
		} else if (episodeType == EpisodeType.GENERAL) {
			type = "Mix";
		} else {
			type = "Par";
		}
		File fileName = new File(getPath(frequency) + "/episodes" + type + "1.json");
		return fileName;
	}

	private Episode readEpisode(int numberOfNodes, String[] rowValues) {
		Episode episode = new Episode();
		episode.setFrequency(Integer.parseInt(rowValues[1].trim()));
		episode.setEntropy(Double.parseDouble(rowValues[2].trim()));
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
}
