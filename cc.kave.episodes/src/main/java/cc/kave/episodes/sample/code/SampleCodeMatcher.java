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
package cc.kave.episodes.sample.code;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.mining.graphs.TransitivelyClosedEpisodes;
import cc.kave.episodes.mining.reader.EpisodeParser;
import cc.kave.episodes.mining.reader.EventMappingParser;
import cc.kave.episodes.mining.reader.FileReader;
import cc.kave.episodes.model.Episode;

public class SampleCodeMatcher {

	private File rootFolder;
	
	@Inject
	public SampleCodeMatcher(@Named("rootDir") File folder) {
		assertTrue(folder.exists(), "Contexts folder does not exist");
		assertTrue(folder.isDirectory(), "Contexts is not a folder, but a file");
		this.rootFolder = folder;
	}
	private FileReader reader = new FileReader();
	private EpisodeParser epParser = new EpisodeParser(rootFolder, reader);
	private EventMappingParser mapParser = new EventMappingParser(rootFolder);
	private EpisodeToGraphConverter graphConverter = new EpisodeToGraphConverter();
	private TransitivelyClosedEpisodes transitivity = new TransitivelyClosedEpisodes();
	private EpisodeAsGraphWriter graphWriter = new EpisodeAsGraphWriter();
	
	public void generateGraphs(int numbRepos) throws IOException {
		String type = "ArrayList";
//		Set<String> types = Sets.newHashSet();
//		types.add("ArrayList");
		
		Map<Integer, Set<Episode>> allEpisodes = epParser.parse(600);
		List<Event> mapper = mapParser.parse(numbRepos);
		
		int nodeLevel = allEpisodes.size() - 1;
		Set<Episode> closedEpisodes = transitivity.remTransClosure(allEpisodes.get(nodeLevel));
		
		int graphID = 0;
		
		for (Episode ep : closedEpisodes) {
			for (Fact fact : ep.getFacts()) {
				int factID = fact.getFactID();
				Event event = mapper.get(factID);
				if (event.getMethod().getDeclaringType().getFullName().contains(type)) {
					DirectedGraph<Fact, DefaultEdge> graph = graphConverter.convert(ep, mapper);
					String fileName = "graph" + graphID + ".txt";
					graphWriter.write(graph, fileName);
					graphID++;
					break;
				}
			}
		}
	} 
}
