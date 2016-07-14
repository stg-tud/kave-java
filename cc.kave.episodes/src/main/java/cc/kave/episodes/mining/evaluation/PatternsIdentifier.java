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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.episodes.mining.reader.MappingParser;
import cc.kave.episodes.mining.reader.ReposParser;
import cc.kave.episodes.mining.reader.StreamParser;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.patterns.PatternExtractor;
import cc.kave.episodes.postprocessor.EpisodesPostprocessor;
import cc.recommenders.io.Logger;

public class PatternsIdentifier {

	private StreamParser streamParser;
	private MappingParser mappingParser;
	private EpisodesPostprocessor episodeProcessor;
	private PatternExtractor extractor;

	private ReposParser repos;

	private static final int OUTPUT = 10;

	@Inject
	public PatternsIdentifier(StreamParser streamParser, EpisodesPostprocessor episodes, MappingParser mappingParser,
			PatternExtractor extractor, ReposParser repos) {
		this.streamParser = streamParser;
		this.mappingParser = mappingParser;
		this.episodeProcessor = episodes;
		this.extractor = extractor;
		this.repos = repos;
	}

	public void trainingCode(int numbRepos, int frequency, double entropy) throws Exception {
		List<List<Fact>> stream = streamParser.parseStream(numbRepos);
		List<Event> events = mappingParser.parse(numbRepos);
		Map<Integer, Set<Episode>> patterns = episodeProcessor.postprocess(numbRepos, frequency, entropy);
		int patternID = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			for (Episode episode : entry.getValue()) {
				Set<IMethodName> enclosingMethods = extractor.getMethodsFromCode(episode, stream, events);
				int numberEnclosingMethods = enclosingMethods.size();
				Logger.log("Episode is identified in %d methods, while the frequency is %d!", numberEnclosingMethods,
						frequency);
				if (numberEnclosingMethods < frequency) {
					int counter = 0;

					for (IMethodName methodName : enclosingMethods) {
						Logger.log("%s", methodName.getDeclaringType().getFullName());
						counter++;
						if (counter == OUTPUT) {
							break;
						}
					}
					throw new Exception(
							"The problematic episode is pattern" + patternID + ": " + episode.getFacts().toString());
				}
				patternID++;
			}
		}
		Logger.log("All patterns are identified with a sufficient number of times from the training source code!");
	}

	public void validationCode(int numbRepos, int frequency, double entropy) throws Exception {
		List<Event> streamOfEvents = repos.validationStream(numbRepos);
		List<Event> events = mappingParser.parse(numbRepos);
		List<List<Fact>> streamOfFacts = eventsToFacts(streamOfEvents, events);

		Map<Integer, Set<Episode>> patterns = episodeProcessor.postprocess(numbRepos, frequency, entropy);
		int patternID = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			for (Episode episode : entry.getValue()) {
				Set<IMethodName> enclosingMethods = extractor.getMethodsFromCode(episode, streamOfFacts, events);

				if (enclosingMethods.size() == 0) {
					Logger.log("Pattern%d does not occur in the reppositories used for validation!", patternID);
					throw new Exception("The pattern is: " + episode.toString());
				}
				patternID++;
			}
		}
		Logger.log("All patterns are identified to be used in the repositories used for validation!");
	}

	private List<List<Fact>> eventsToFacts(List<Event> stream, List<Event> events) {
		List<List<Fact>> result = new LinkedList<>();
		List<Fact> method = new LinkedList<Fact>();

		for (Event event : stream) {
			if (event.getKind() == EventKind.FIRST_DECLARATION) {
				if (!method.isEmpty()) {
					result.add(method);
					method = new LinkedList<Fact>();
				}
			}
			int factID = events.indexOf(event);
			method.add(new Fact(factID));
		}
		return result;
	}
}
