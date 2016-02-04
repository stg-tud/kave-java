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
package cc.kave.commons.mining.episodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.kave.commons.model.episodes.Episode;

public class EpisodeMapping {

	public Map<Episode, Integer> generateEpisodeIds(Map<Integer, List<Episode>> learnedEpisodes) throws Exception {

		if (learnedEpisodes.isEmpty()) {
			throw new Exception("The list of learned episodes is empty");
		}

		Map<Episode, Integer> mapping = new HashMap<Episode, Integer>();
		int episodeId = 0;
		for (Map.Entry<Integer, List<Episode>> entry : learnedEpisodes.entrySet()) {
			for (Episode episode : entry.getValue()) {
				mapping.put(episode, episodeId);
				episodeId++;
			}
		}
		return mapping;
	}
}
