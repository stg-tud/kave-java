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

import org.apache.commons.io.FileUtils;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.mining.evaluation.EpisodeExtraction;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.mining.graphs.TransitivelyClosedEpisodes;
import cc.kave.episodes.mining.reader.MappingParser;
import cc.kave.episodes.mining.reader.StreamParser;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.postprocessor.EpisodesPostprocessor;

public class PatternsOutput {

	private File patternsFolder;

	private EpisodesPostprocessor episodesProcessor;
	private MappingParser mappingParser;
	private StreamParser streamParser;
	private EpisodeExtraction extractor;
	private TransitivelyClosedEpisodes transClosure;
	private EpisodeToGraphConverter episodeGraphConverter;
	private EpisodeAsGraphWriter graphWriter;

	@Inject
	public PatternsOutput(@Named("patterns") File folder, EpisodesPostprocessor episodes, MappingParser mappingParser,
			StreamParser streamParser, EpisodeExtraction extractor, TransitivelyClosedEpisodes transitivityClosure,
			EpisodeToGraphConverter graphConverter, EpisodeAsGraphWriter writer) {

		assertTrue(folder.exists(), "Patterns folder does not exist");
		assertTrue(folder.isDirectory(), "Patterns folder is not a folder, but a file");

		this.patternsFolder = folder;
		this.episodesProcessor = episodes;
		this.mappingParser = mappingParser;
		this.streamParser = streamParser;
		this.extractor = extractor;
		this.transClosure = transitivityClosure;
		this.episodeGraphConverter = graphConverter;
		this.graphWriter = writer;
	}

	public void write(int numbRepos, int freqThresh, double bidirectThresh) throws Exception {
		Map<Integer, Set<Episode>> patterns = episodesProcessor.postprocess(numbRepos, freqThresh, bidirectThresh);
		List<Event> events = mappingParser.parse(numbRepos);
		Set<Set<Fact>> stream = streamParser.parseStream(numbRepos);

		int graphNumber = 0;
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			if (entry.getKey() == 1) {
				continue;
			}
			Set<Episode> closedEpisodes = transClosure.remTransClosure(entry.getValue());

			for (Episode episode : closedEpisodes) {
				File filePath = getPath(numbRepos, freqThresh, bidirectThresh, graphNumber);
				
				DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter.convert(episode, events);
				graphWriter.write(graph, getGraphPaths(filePath, graphNumber));

				StringBuilder sb = extractor.getMethods(episode, stream, events);
				
				if (!sb.toString().isEmpty()) {
					FileUtils.writeStringToFile(getMethodsPath(filePath, graphNumber), sb.toString());
				}
				graphNumber++;
			}
		}
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
	
	private File getMethodsPath(File folderPath, int patternNumber) {
		File methodFolder = new File(folderPath.getAbsolutePath() + "/methods/");
		if (!methodFolder.isDirectory()) {
			methodFolder.mkdir();
		}
		File methodPath = new File(methodFolder.getAbsolutePath() + "/pattern" + patternNumber + ".txt");
		return methodPath;
	}
}
