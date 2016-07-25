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

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.mining.reader.MappingParser;
import cc.kave.episodes.mining.reader.ReposParser;
import cc.kave.episodes.mining.reader.StreamParser;
import cc.kave.episodes.model.EnclosingMethods;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.postprocessor.EpisodesPostprocessor;
import cc.recommenders.io.Logger;

public class PatternsIdentifier {

	private StreamParser streamParser;
	private MappingParser mappingParser;
	private EpisodesPostprocessor episodeProcessor;

	private ReposParser repos;

	@Inject
	public PatternsIdentifier(StreamParser streamParser, EpisodesPostprocessor episodes, MappingParser mappingParser,
			ReposParser repos) {
		this.streamParser = streamParser;
		this.mappingParser = mappingParser;
		this.episodeProcessor = episodes;
		this.repos = repos;
	}

	public void trainingCode(int numbRepos, int frequency, double entropy, boolean order) throws Exception {
		List<List<Fact>> stream = streamParser.parseStream(numbRepos);
		List<Event> events = mappingParser.parse(numbRepos);
		Map<Integer, Set<Episode>> patterns = episodeProcessor.postprocess(numbRepos, frequency, entropy);

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			if (entry.getKey() < 2) {
				continue;
			}
			for (Episode episode : entry.getValue()) {
				Set<Fact> episodeFacts = episode.getEvents();
				EnclosingMethods methodsNoOrderRelation = new EnclosingMethods();
				EnclosingMethods methodsOrderRelation = new EnclosingMethods();

				for (List<Fact> method : stream) {
					if (method.containsAll(episodeFacts)) {
						methodsNoOrderRelation.addMethod(episode, method, events, false);
						methodsOrderRelation.addMethod(episode, method, events, true);
					}
				}
				if (methodsOrderRelation.getOccurrences() < frequency) {
					throw new Exception("Episode is not found sufficient number of times on the training stream!");
				}
			}
			Logger.log("Processed %d-node patterns!", entry.getKey());
		}
	}

	// public void validationCode(int numbRepos, int frequency, double entropy,
	// boolean order) throws Exception {
	// List<Event> streamOfEvents = repos.validationStream(numbRepos);
	// List<Event> events = mappingParser.parse(numbRepos);
	// List<List<Fact>> streamOfFacts = eventsToFacts(streamOfEvents, events);
	// int found = 0;
	// int notFound = 0;
	//
	// Map<Integer, Set<Episode>> patterns =
	// episodeProcessor.postprocess(numbRepos, frequency, entropy);
	// for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
	// if (entry.getKey() < 2) {
	// continue;
	// }
	// for (Episode episode : entry.getValue()) {
	// List<IMethodName> enclosingMethods =
	// extractor.getMethodsFromCode(episode, streamOfFacts, events,
	// order);
	// if (enclosingMethods.size() == 0) {
	// notFound++;
	// } else {
	// found++;
	// }
	// Logger.log("Patterns not found in the validation repositories are: %d",
	// notFound);
	// Logger.log("Patterns found in the validation repositories are: %d",
	// found);
	// }
	// }
	// }
}
