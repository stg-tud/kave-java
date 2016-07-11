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
package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.mining.reader.MappingParser;
import cc.kave.episodes.mining.reader.StreamParser;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.postprocessor.EpisodesPostprocessor;

public class PatternsSearch {

	private File patternsFolder;
	private EpisodesPostprocessor episodes;
	private StreamParser streamParser;
	private MappingParser mapper;

	@Inject
	public PatternsSearch(@Named("patterns") File folder, EpisodesPostprocessor eparser, StreamParser sparser,
			MappingParser mparser) {
		assertTrue(folder.exists(), "Patterns folder does not exist");
		assertTrue(folder.isDirectory(), "Patterns is not a folder, but a file");
		this.patternsFolder = folder;
		this.episodes = eparser;
		this.streamParser = sparser;
		this.mapper = mparser;
	}

	public void fromStream(int numbRepos, int freqs, double bidirect) throws Exception {
		Map<Integer, Set<Episode>> patterns = episodes.postprocess(numbRepos, freqs, bidirect);
		Set<Set<Fact>> streamData = streamParser.parseStream(numbRepos);
		List<Event> events = mapper.parse(numbRepos);
		
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			for (Episode episode : entry.getValue()) {
				Set<Fact> episodeEvents = episode.getEvents();
				
				if (episodeEvents.isEmpty()) {
					continue;
				}
				StringBuilder sb = new StringBuilder();
				for (Set<Fact> streamFacts : streamData) {
					if (streamFacts.containsAll(episodeEvents)) {
						
					}
				}
			}
		}
	}
}
