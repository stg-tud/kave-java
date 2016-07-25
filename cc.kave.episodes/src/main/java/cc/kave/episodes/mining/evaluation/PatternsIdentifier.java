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
import cc.kave.episodes.mining.patterns.MaximalEpisodes;
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
	private MaximalEpisodes maxEpisodes;

	private ReposParser repos;

	@Inject
	public PatternsIdentifier(StreamParser streamParser, EpisodesPostprocessor episodes, MappingParser mappingParser,
			MaximalEpisodes maxEpisodes, ReposParser repos) {
		this.streamParser = streamParser;
		this.mappingParser = mappingParser;
		this.episodeProcessor = episodes;
		this.maxEpisodes = maxEpisodes;
		this.repos = repos;
	}

	public void trainingCode(int numbRepos, int frequency, double entropy) throws Exception {
		List<List<Fact>> stream = streamParser.parseStream(numbRepos);
		List<Event> events = mappingParser.parse(numbRepos);
		Map<Integer, Set<Episode>> postpEpisodes = episodeProcessor.postprocess(numbRepos, frequency, entropy);
		Map<Integer, Set<Episode>> patterns = maxEpisodes.getMaximalEpisodes(postpEpisodes);

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			if (entry.getKey() < 2) {
				continue;
			}
			for (Episode episode : entry.getValue()) {
				Set<Fact> episodeFacts = episode.getEvents();
				EnclosingMethods methodsNoOrderRelation = new EnclosingMethods(false);
				EnclosingMethods methodsOrderRelation = new EnclosingMethods(true);

				for (List<Fact> method : stream) {
					if (method.containsAll(episodeFacts)) {
						methodsNoOrderRelation.addMethod(episode, method, events);
						methodsOrderRelation.addMethod(episode, method, events);
					}
				}
				if (methodsOrderRelation.getOccurrences() < frequency) {
					throw new Exception("Episode is not found sufficient number of times on the training stream!");
				}
			}
			Logger.log("Processed %d-node patterns!", entry.getKey());
		}
	}

	public void validationCode(int numbRepos, int frequency, double entropy, boolean order) throws Exception {
		List<Event> streamOfEvents = repos.validationStream(numbRepos);
		List<Event> events = mappingParser.parse(numbRepos);
		List<List<Fact>> streamOfFacts = eventsToFacts(streamOfEvents, events);
		Map<Integer, Set<Episode>> patterns = episodeProcessor.postprocess(numbRepos, frequency, entropy);
		StringBuilder sb = new StringBuilder();
		int patternID = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			if (entry.getKey() < 2) {
				continue;
			}
			sb.append("Patterns of size: " + entry.getKey() + "-events\n");
			sb.append("PatternID\tFrequency\toccurrencesAsSet\toccurrencesOrder\n");
			for (Episode episode : entry.getValue()) {
				Set<Fact> episodeFacts = episode.getEvents();
				EnclosingMethods methodsNoOrderRelation = new EnclosingMethods(false);
				EnclosingMethods methodsOrderRelation = new EnclosingMethods(true);

				for (List<Fact> method : streamOfFacts) {
					if (method.containsAll(episodeFacts)) {
						methodsNoOrderRelation.addMethod(episode, method, events);
						methodsOrderRelation.addMethod(episode, method, events);
					}
				}
				sb.append(patternID + "\t" + episode.getFrequency() + "\t" + methodsNoOrderRelation.getOccurrences()
						+ "\t" + methodsOrderRelation.getOccurrences() + "\n");
				patternID++;
			}
			sb.append("\n");
		}
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
