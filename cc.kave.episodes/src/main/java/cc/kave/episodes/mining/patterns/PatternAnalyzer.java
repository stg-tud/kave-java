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
package cc.kave.episodes.mining.patterns;

import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.episodes.mining.reader.EpisodeParser;

public class PatternAnalyzer {

	private EpisodeParser episodeParser;
	private MaximalEpisodes maxEpisodes;
	
	@Inject
	public PatternAnalyzer(EpisodeParser parser, MaximalEpisodes me) {
		this.episodeParser = parser;
		this.maxEpisodes = me;
	}
	
	public void readPatterns() {
		Map<Integer, List<Episode>> allEpisodes = episodeParser.parse(4, 0.5);
		Map<Integer, List<Episode>> maximalEpisodes = maxEpisodes.getMaximalEpisodes(allEpisodes);
		
		int allEpisodeCounter = 0;
		int maxEpisodeCounter = 0;
		
		System.out.println("Maximal node-level is " + allEpisodes.size());
		System.out.println();
		
		for (Map.Entry<Integer, List<Episode>> entry : allEpisodes.entrySet()) {
			System.out.println("Node level = " + entry.getKey());
			System.out.println("All episodes = " + entry.getValue().size());
			allEpisodeCounter += entry.getValue().size();
		}
		System.out.println("Total number of maximal episodes is " + allEpisodeCounter);
		System.out.println();
		
		for (Map.Entry<Integer, List<Episode>> entry : maximalEpisodes.entrySet()) {
			System.out.println("Node level = " + entry.getKey());
			System.out.println("Maximal episodes = " + entry.getValue().size());
			maxEpisodeCounter += entry.getValue().size();
		}
		System.out.println("Total number of maximal episodes is " + maxEpisodeCounter);
	}
}
