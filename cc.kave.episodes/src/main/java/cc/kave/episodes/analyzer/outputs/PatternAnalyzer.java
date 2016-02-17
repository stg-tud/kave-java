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
package cc.kave.episodes.analyzer.outputs;

import java.util.Map;
import java.util.Set;

import com.google.inject.Inject;

import cc.kave.episodes.mining.patterns.MaximalEpisodes;
import cc.kave.episodes.mining.reader.EpisodeParser;
import cc.kave.episodes.model.Episode;

public class PatternAnalyzer {

	private EpisodeParser episodeParser;
	private MaximalEpisodes maxEpisodes;
	
	private static final int FREQTHRESH = 3;
	private static final double BDTHRESH = 0.01;
	
	@Inject
	public PatternAnalyzer(EpisodeParser parser, MaximalEpisodes me) {
		this.episodeParser = parser;
		this.maxEpisodes = me;
	}
	
	public void readPatterns() {
		Map<Integer, Set<Episode>> allEpisodes = episodeParser.parse(FREQTHRESH, BDTHRESH);
		Map<Integer, Set<Episode>> maximalEpisodes = maxEpisodes.getMaximalEpisodes(allEpisodes);
		
		int allEpisodeCounter = 0;
		int maxEpisodeCounter = 0;
		
		System.out.println("Maximal node-level is " + allEpisodes.size());
		System.out.println();
		
		for (Map.Entry<Integer, Set<Episode>> entry : allEpisodes.entrySet()) {
			System.out.println("Node level = " + entry.getKey());
			System.out.println("All episodes = " + entry.getValue().size());
			allEpisodeCounter += entry.getValue().size();
		}
		System.out.println("Total number of maximal episodes is " + allEpisodeCounter);
		System.out.println();
		
		for (Map.Entry<Integer, Set<Episode>> entry : maximalEpisodes.entrySet()) {
			System.out.println("Node level = " + entry.getKey());
			System.out.println("Maximal episodes = " + entry.getValue().size());
			maxEpisodeCounter += entry.getValue().size();
		}
		System.out.println("Total number of maximal episodes is " + maxEpisodeCounter);
	}
}
