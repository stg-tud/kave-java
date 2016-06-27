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
package cc.kave.episodes.statistics;

import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.util.Precision;

import com.google.common.collect.Maps;

import cc.kave.episodes.model.Episode;

public class EpisodesStatistics {

	public Map<Integer, Integer> freqsEpisodes(Set<Episode> episodes) {
		Map<Integer, Integer> total = initFreqs(episodes);

		for (Episode ep : episodes) {
			int freq = ep.getFrequency();
			int count = total.get(freq);
			total.put(freq, count + 1);

			if (freq > 2) {
				for (int distr = freq - 1; distr >= 2; distr--) {
					if (total.containsKey(distr)) {
						int currCounter = total.get(distr);
						total.put(distr, currCounter + 1);
					}
				}
			}
		}
		return total;
	}

	private Map<Integer, Integer> initFreqs(Set<Episode> episodes) {
		Map<Integer, Integer> initializer = Maps.newHashMap();

		for (Episode ep : episodes) {
			int frequency = ep.getFrequency();
			if (!initializer.containsKey(frequency)) {
				initializer.put(frequency, 0);
			}
		}
		return initializer;
	}

	public Map<Double, Integer> bidirectEpisodes(Set<Episode> episodes) {
		Map<Double, Integer> total = initBd(episodes);

		for (Episode ep : episodes) {
			double bd = ep.getBidirectMeasure();
			int count = total.get(bd);
			total.put(bd, count + 1);
			
			if (bd > 0.1) {
				for (double distr = bd - 0.1; distr >= 0.1; distr -= 0.1) {
					double tempBd = Precision.round(distr, 1);
					if (total.containsKey(tempBd)) {
						int tempCount = total.get(tempBd);
						total.put(tempBd, tempCount + 1);
					}
				}
			}
		}
		return total;
	}

	private Map<Double, Integer> initBd(Set<Episode> episodes) {
		Map<Double, Integer> initializer = Maps.newHashMap();

		for (Episode ep : episodes) {
			double bd = ep.getBidirectMeasure();
			if (!initializer.containsKey(bd)) {
				initializer.put(bd, 0);
			}
		}
		return initializer;
	}
}
