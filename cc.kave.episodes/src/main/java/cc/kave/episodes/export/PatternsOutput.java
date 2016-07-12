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
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.csharp.MethodName;
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
				DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter.convert(episode, events);
				graphWriter.write(graph, getFilePaths(numbRepos, freqThresh, bidirectThresh, graphNumber).graphPath);

				StringBuilder sb = getEpisodeLocations(episode, stream, events);
				File fileName = new File(getFilePaths(numbRepos, freqThresh, bidirectThresh, graphNumber).methodPath);
				FileUtils.writeStringToFile(fileName, sb.toString());
				graphNumber++;
			}
		}
	}

	private StringBuilder getEpisodeLocations(Episode episode, Set<Set<Fact>> stream, List<Event> events) {
		StringBuilder sb = new StringBuilder();
		Set<Fact> episodeFacts = episode.getEvents();

		for (Set<Fact> streamFacts : stream) {
			if (streamFacts.containsAll(episodeFacts)) {
				String methodName = getSuperMethod(streamFacts, events);
				sb.append(methodName + "\n");
			}
		}
		return sb;
	}

	private String getSuperMethod(Set<Fact> streamFacts, List<Event> events) {
		for (Fact fact : streamFacts) {
			int eventID = fact.getFactID();
			Event event = events.get(eventID);
			if (event.getKind() == EventKind.SUPER_DECLARATION) {
				return event.getMethod().getDeclaringType().getFullName();
			}
		}
		return MethodName.UNKNOWN_NAME.getName();
	}

	private FilePaths getFilePaths(int numbRepos, int freqThresh, double bidirectThresh, int graphNum) {
		FilePaths filePaths = new FilePaths();

		File graphFolder = new File(patternsFolder.getAbsolutePath() + "/Repos" + numbRepos + "/Freq" + freqThresh
				+ "/Bidirect" + bidirectThresh + "/");
		if (!graphFolder.isDirectory()) {
			graphFolder.mkdirs();
		}
		filePaths.graphPath = graphFolder.getAbsolutePath() + "/pattern" + graphNum + ".dot";

		File methodFolder = new File(graphFolder.getAbsolutePath() + "/methods/");
		if (!methodFolder.isDirectory()) {
			methodFolder.mkdir();
		}
		filePaths.methodPath = methodFolder.getAbsolutePath() + "/pattern" + graphNum + ".txt";
		return filePaths;
	}

	private class FilePaths {
		String graphPath;
		String methodPath;
	}
}
