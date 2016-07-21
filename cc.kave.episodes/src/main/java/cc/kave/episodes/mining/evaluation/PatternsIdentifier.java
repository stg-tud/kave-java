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

import com.google.common.collect.Sets;
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

	public void trainingCode(int numbRepos, int frequency, double entropy, boolean order) throws Exception {
		List<List<Fact>> stream = streamParser.parseStream(numbRepos);
		List<Event> events = mappingParser.parse(numbRepos);
		// for (List<Fact> method : stream) {
		// List<Event> methodEvents = toEvents(method, events);
		// boolean containsEnclMethod = false;
		// for (Event event : methodEvents) {
		// if (event.getKind() == EventKind.METHOD_DECLARATION) {
		// containsEnclMethod = true;
		// }
		// }
		// if (!containsEnclMethod) {
		// Logger.log("Not enclosing method for %s", method.toString());
		// }
		// }
		// int streamLength = 0;
		// for (List<Fact> method : stream) {
		// streamLength += method.size();
		// }
		// Logger.log("Stream length is %d events", streamLength);

		Map<Integer, Set<Episode>> patterns = episodeProcessor.postprocess(numbRepos, frequency, entropy);
		int complete = 0;
		int incomplete = 0;
		int notThere = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			if (entry.getKey() < 2) {
				continue;
			}
			for (Episode episode : entry.getValue()) {
				List<IMethodName> enclosingMethods = extractor.getMethodsFromCode(episode, stream, events, order);
				int numberEnclosingMethods = enclosingMethods.size();
				if (numberEnclosingMethods == 0) {
					notThere++;
				}
				if (numberEnclosingMethods < frequency) {
					incomplete++;
					Logger.log("Frequency vs occurrence is: %d, %d", frequency, numberEnclosingMethods);
					Logger.log("Infrequent episode: %s", episode.toString());
					for (Fact fact : episode.getEvents()) {
						IMethodName method = events.get(fact.getFactID()).getMethod();
						Logger.log("Episode event name: %s.%s", method.getDeclaringType().getFullName(), method.getName());
					}
					Set<IMethodName> observed = Sets.newHashSet();
					for (int i = 0; i < 10; i++) {
						IMethodName enclosMethod = enclosingMethods.get(i);
						if (!observed.contains(enclosMethod)) {
							Logger.log("Enclosing methods: %s.%s", enclosMethod.getDeclaringType().getFullName(), enclosMethod.getName());
							observed.add(enclosMethod);
						}
					}
					// int counter = 0;
					//
					// for (IMethodName methodName : enclosingMethods) {
					// Logger.log("%s",
					// methodName.getDeclaringType().getFullName());
					// counter++;
					// if (counter == OUTPUT) {
					// break;
					// }
					// }
					// throw new Exception(
					// "The problematic episode is pattern" + patternID + ": " +
					// episode.getFacts().toString());
				} else {
					complete++;
				}
//				Logger.log("Nummber of patterns that are not found in the training data is %d!", notThere);
//				Logger.log("Number of patterns that are found an insufficient number of times is %d", incomplete);
//				Logger.log("Number of patterns that are found sufficient number of times is %d\n", complete);
			}
		}
		// Logger.log("All patterns are identified with a sufficient number of
		// times from the training source code!");
	}

	public void validationCode(int numbRepos, int frequency, double entropy, boolean order) throws Exception {
		List<Event> streamOfEvents = repos.validationStream(numbRepos);
		List<Event> events = mappingParser.parse(numbRepos);
		List<List<Fact>> streamOfFacts = eventsToFacts(streamOfEvents, events);
		int found = 0;
		int notFound = 0;

		Map<Integer, Set<Episode>> patterns = episodeProcessor.postprocess(numbRepos, frequency, entropy);
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			if (entry.getKey() < 2) {
				continue;
			}
			for (Episode episode : entry.getValue()) {
				List<IMethodName> enclosingMethods = extractor.getMethodsFromCode(episode, streamOfFacts, events,
						order);
				if (enclosingMethods.size() == 0) {
					notFound++;
				} else {
					found++;
				}
				Logger.log("Patterns not found in the validation repositories are: %d", notFound);
				Logger.log("Patterns found in the validation repositories are: %d", found);
			}
		}
	}

	private List<Event> toEvents(List<Fact> method, List<Event> events) {
		List<Event> methodEvents = new LinkedList<Event>();
		for (Fact fact : method) {
			Event event = events.get(fact.getFactID());
			methodEvents.add(event);
		}
		return methodEvents;
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
