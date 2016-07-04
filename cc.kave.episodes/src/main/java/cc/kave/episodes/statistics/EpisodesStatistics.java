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
	
	private static final int ROUNDVALUE = 3;

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
		Map<Integer, Integer> initializer = Maps.newLinkedHashMap();

		for (Episode ep : episodes) {
			int frequency = ep.getFrequency();
			if (!initializer.containsKey(frequency)) {
				initializer.put(frequency, 0);
			}
		}
		return initializer;
	}

	public Map<Double, Integer> bidirectEpisodes(Set<Episode> episodes, int frequency) {
		Map<Double, Integer> total = initBd(episodes, frequency);

		for (Episode ep : episodes) {
			if (!valid(ep, frequency)){
				continue;
			}
			double bd = ep.getBidirectMeasure();
			double roundBd = Precision.round(bd, ROUNDVALUE);
			int count = total.get(roundBd);
			total.put(roundBd, count + 1);
			
			if (roundBd > 0.1) {
				for (double distr = bd - 0.001; distr > 0.0999; distr -= 0.001) {
					double tempDistr = Precision.round(distr, ROUNDVALUE);
					if (total.containsKey(tempDistr)) {
						int tempCount = total.get(tempDistr);
						total.put(tempDistr, tempCount + 1);
					}
				}
			}
		}
		return total;
	}

	private boolean valid(Episode ep, int threshFreq) {
		int epFreq = ep.getFrequency();
		if (epFreq >= threshFreq) {
			return true;
		} 
		return false;
	}

	private Map<Double, Integer> initBd(Set<Episode> episodes, int freqThresh) {
		Map<Double, Integer> initializer = Maps.newLinkedHashMap();

		for (Episode ep : episodes) {
			int epFreq = ep.getFrequency();
			if (epFreq < freqThresh) {
				continue;
			}
			double bd = ep.getBidirectMeasure();
			double reoundBd = Precision.round(bd, ROUNDVALUE);
			if (!initializer.containsKey(reoundBd)) {
				initializer.put(reoundBd, 0);
			}
		}
		return initializer;
	}
}
