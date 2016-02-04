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

import java.util.Comparator;
import java.util.TreeSet;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Episode;
import cc.recommenders.datastructures.Tuple;

public class ProposalHelper {
	public static <T extends Comparable<T>> TreeSet<Tuple<T, Double>> createSortedSet() {
		final TreeSet<Tuple<T, Double>> res = Sets.newTreeSet(new Comparator<Tuple<T, Double>>() {
			@Override
			public int compare(final Tuple<T, Double> o1, final Tuple<T, Double> o2) {
				// higher probabilities will be sorted above lower ones
				int valueOrdering = Double.compare(o2.getSecond(), o1.getSecond());
				boolean areValuesEqual = valueOrdering == 0;
				if (areValuesEqual) {
					int orderOfFirstTupleMember = o1.getFirst().compareTo(o2.getFirst());
					return orderOfFirstTupleMember;
				} else {
					return valueOrdering;
				}
			}
		});
		return res;
	}
	
	public static TreeSet<Tuple<Episode, Double>> createEpisodesSortedSet() {
		final TreeSet<Tuple<Episode, Double>> res = Sets.newTreeSet(new Comparator<Tuple<Episode, Double>>() {
			@Override
			public int compare(final Tuple<Episode, Double> o1, final Tuple<Episode, Double> o2) {
				int valueOrdering = Double.compare(o2.getSecond(), o1.getSecond());
				boolean areValuesEqual = valueOrdering == 0;
				if (areValuesEqual) {
					int frequencyOrdering = Integer.compare(o2.getFirst().getFrequency(), o1.getFirst().getFrequency());
					boolean areFrequenciesEqual = frequencyOrdering == 0;
					if (areFrequenciesEqual) {
						int numberOfEventsOrdering = Integer.compare(o2.getFirst().getNumEvents(), o1.getFirst().getNumEvents());
						boolean areNumberOfEventsEqual = numberOfEventsOrdering == 0;
						if (areNumberOfEventsEqual) {
							int eventsOrdering = o1.getFirst().getFacts().toString().compareTo(o2.getFirst().getFacts().toString());
							return eventsOrdering;
						} else {
							return numberOfEventsOrdering;
						}
					} else {
						return frequencyOrdering;
					}
				} else {
					return valueOrdering;
				}
			}
		});
		return res;
	}
}