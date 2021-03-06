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
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.mining.graphs.TransitivelyClosedEpisodes;
import cc.kave.episodes.mining.reader.MappingParser;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.postprocessor.EpisodesPostprocessor;
import cc.recommenders.io.Logger;

public class PatternsOutput {

	private File patternsFolder;

	private EpisodesPostprocessor episodesProcessor;
	private MappingParser mappingParser;
	private TransitivelyClosedEpisodes transClosure;
	private EpisodeToGraphConverter episodeGraphConverter;
	private EpisodeAsGraphWriter graphWriter;

	@Inject
	public PatternsOutput(@Named("patterns") File folder, EpisodesPostprocessor episodes, MappingParser mappingParser,
			TransitivelyClosedEpisodes transitivityClosure, EpisodeToGraphConverter graphConverter,
			EpisodeAsGraphWriter writer) {

		assertTrue(folder.exists(), "Patterns folder does not exist");
		assertTrue(folder.isDirectory(), "Patterns folder is not a folder, but a file");

		this.patternsFolder = folder;
		this.episodesProcessor = episodes;
		this.mappingParser = mappingParser;
		this.transClosure = transitivityClosure;
		this.episodeGraphConverter = graphConverter;
		this.graphWriter = writer;
	}

	public void write(int numbRepos, int freqThresh, double bidirectThresh) throws Exception {
		Map<Integer, Set<Episode>> patterns = episodesProcessor.postprocess(numbRepos, freqThresh, bidirectThresh);
		List<Event> events = mappingParser.parse(numbRepos);
		Logger.log("Number of unique event is %d", events.size());
		int graphNumber = 0;

//		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
//			if (entry.getKey() == 1) {
//				continue;
//			}
//			Set<Episode> closedEpisodes = transClosure.remTransClosure(entry.getValue());
//
//			for (Episode episode : closedEpisodes) {
//				File filePath = getPath(numbRepos, freqThresh, bidirectThresh, graphNumber);
//
//				DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter.convert(episode, events);
//				graphWriter.write(graph, getGraphPaths(filePath, graphNumber));
//				graphNumber++;
//			}
//		}
	}

	private File getPath(int numbRepos, int freqThresh, double bidirectThresh, int graphNum) {
		File folderPath = new File(patternsFolder.getAbsolutePath() + "/Repos" + numbRepos + "/Freq" + freqThresh
				+ "/Bidirect" + bidirectThresh + "/");
		if (!folderPath.isDirectory()) {
			folderPath.mkdirs();
		}
		return folderPath;
	}

	private String getGraphPaths(File folderPath, int patternNumber) {
		String graphPath = folderPath + "/pattern" + patternNumber + ".dot";
		return graphPath;
	}
}
