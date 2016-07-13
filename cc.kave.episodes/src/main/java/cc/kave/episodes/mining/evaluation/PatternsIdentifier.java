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
import cc.kave.commons.model.names.IMethodName;
import cc.kave.episodes.mining.reader.MappingParser;
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
	
	private static final int OUTPUT = 10;

	@Inject
	public PatternsIdentifier(StreamParser streamParser, EpisodesPostprocessor episodes, MappingParser mappingParser,
			PatternExtractor extractor) {
		this.streamParser = streamParser;
		this.mappingParser = mappingParser;
		this.episodeProcessor = episodes;
		this.extractor = extractor;
	}

	public void identifyInCode(int numbRepos, int frequency, double entropy) throws Exception {
		List<List<Fact>> stream = streamParser.parseStream(numbRepos);
		List<Event> events = mappingParser.parse(numbRepos);
		Map<Integer, Set<Episode>> patterns = episodeProcessor.postprocess(numbRepos, frequency, entropy);

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			for (Episode episode : entry.getValue()) {
				Set<IMethodName> enclosingMethods = extractor.getMethods(episode, stream, events);
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
					throw new Exception("The problematic episode is " + episode.getFacts().toString());
				}
			}
		}
		Logger.log("All patterns are identified with a sufficient number of times from the training source code!");
	}
}
