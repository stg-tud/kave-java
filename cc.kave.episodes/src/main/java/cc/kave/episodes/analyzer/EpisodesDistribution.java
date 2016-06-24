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
package cc.kave.episodes.analyzer;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import cc.kave.episodes.model.Episode;

public class EpisodesDistribution {

	public Map<Integer, Integer> freqsEpisodes(List<Episode> episodes) {
		Map<Integer, Integer> total = Maps.newHashMap();
		
		for (Episode ep : episodes) {
			int freq = ep.getFrequency();
			if (total.containsKey(freq)) {
				int count = total.get(freq);
				total.put(freq, count + 1);
			} else {
				total.put(freq, 1);
			}
		}
		return total;
	} 
	
	public Map<Double, Integer> bidirectEpisodes(List<Episode> episodes) {
		Map<Double, Integer> total = Maps.newHashMap();
		
		for (Episode ep : episodes) {
			double bd = ep.getBidirectMeasure();
			if (total.containsKey(bd)) {
				int count = total.get(bd);
				total.put(bd, count + 1);
			} else {
				total.put(bd, 1);
			}
		}
		return total;
	}
}
