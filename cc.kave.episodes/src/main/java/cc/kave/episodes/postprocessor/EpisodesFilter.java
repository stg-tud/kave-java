/**
 * * Copyright 2016 Technische Universität Darmstadt
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
package cc.kave.episodes.postprocessor;

import java.util.Map;
import java.util.Set;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class EpisodesFilter {

	public Map<Integer, Set<Episode>> filter(EpisodeType type,
			Map<Integer, Set<Episode>> episodes, int frequency, double entropy) {
		Map<Integer, Set<Episode>> threshFilter = Maps.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			if (entry.getKey() == 1) {
				continue;
			}
			Set<Episode> episodeLevel = Sets.newLinkedHashSet();
			for (Episode ep : entry.getValue()) {
				int epFreq = ep.getFrequency();
				if (epFreq >= frequency) {
					if (type == EpisodeType.GENERAL) {
						double epEnt = ep.getEntropy();
						if (epEnt >= entropy) {
							episodeLevel.add(ep);
						}
					} else {
						episodeLevel.add(ep);
					}
				}
			}
			threshFilter.put(entry.getKey(), episodeLevel);
		}
		if (type == EpisodeType.GENERAL) {
			return filterPartials(threshFilter);
		}
		return threshFilter;
	}

	private Map<Integer, Set<Episode>> filterPartials(
			Map<Integer, Set<Episode>> episodes) {
		Map<Integer, Set<Episode>> results = Maps.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entryEpisodes : episodes
				.entrySet()) {
			Map<Set<Fact>, Set<Episode>> epGroups = grouping(entryEpisodes
					.getValue());
			Set<Episode> patterns = Sets.newLinkedHashSet();

			for (Map.Entry<Set<Fact>, Set<Episode>> entryGroups : epGroups
					.entrySet()) {
				if (entryGroups.getValue().size() == 1) {
					for (Episode episode : entryGroups.getValue()) {
						patterns.add(episode);
						break;
					}
				} else {
					Set<Episode> group = entryGroups.getValue();
					Set<Episode> epFilter = filter(group);
					patterns.addAll(epFilter);
				}
			}
			results.put(entryEpisodes.getKey(), patterns);
		}
		return results;
	}

	private Set<Episode> filter(Set<Episode> episodes) {
		Set<Episode> results = Sets.newLinkedHashSet();
		Map<Integer, Set<Episode>> relsLevels = getRelLevels(episodes);

		while (!representative(results, relsLevels)) {
			if ((relsLevels.size() == 1) && (results.size() == 0)) {
				results.addAll(getSingleValue(relsLevels));
				return results;
			}
			int minRelations = getMinRels(relsLevels);
			Set<Episode> candidates = relsLevels.get(minRelations);
			for (Episode ep : candidates) {
				if (!isCovered(ep, results)) {
					results.add(ep);
				}
			}
			relsLevels.remove(minRelations);
		}
		return results;
	}

	private Set<Episode> getSingleValue(Map<Integer, Set<Episode>> relsLevels) {
		for (Map.Entry<Integer, Set<Episode>> entry : relsLevels.entrySet()) {
			return entry.getValue();
		}
		return null;
	}

	private boolean representative(Set<Episode> repr,
			Map<Integer, Set<Episode>> episodes) {
		if (repr.size() == 0) {
			return false;
		}
		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			for (Episode episode : entry.getValue()) {
				if (!isCovered(episode, repr)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isCovered(Episode episode, Set<Episode> reprs) {
		for (Episode rep : reprs) {
			if (represents(rep, episode)) {
				return true;
			}
		}
		return false;
	}

	private boolean represents(Episode rep, Episode episode) {
		Set<Fact> relations = episode.getRelations();
		for (Fact rel : relations) {
			Tuple<Fact, Fact> facts = rel.getRelationFacts();
			Fact opp = new Fact(facts.getSecond() + ">" + facts.getFirst());
			if (rep.getRelations().contains(opp)) {
				return false;
			}
		}
		return true;
	}

	private Map<Integer, Set<Episode>> getRelLevels(Set<Episode> episodes) {
		Map<Integer, Set<Episode>> results = Maps.newLinkedHashMap();
		for (Episode episode : episodes) {
			int numbRels = episode.getRelations().size();
			if (results.containsKey(numbRels)) {
				results.get(numbRels).add(episode);
			} else {
				results.put(numbRels, Sets.newHashSet(episode));
			}
		}
		return results;
	}

	private int getMinRels(Map<Integer, Set<Episode>> episodes) {
		int min = Integer.MAX_VALUE;

		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			int numRels = entry.getKey();
			if (numRels < min) {
				min = numRels;
			}
		}
		return min;
	}

	private Map<Set<Fact>, Set<Episode>> grouping(Set<Episode> episodes) {
		Map<Set<Fact>, Set<Episode>> results = Maps.newLinkedHashMap();

		for (Episode ep : episodes) {
			Set<Fact> events = ep.getEvents();
			if (results.containsKey(events)) {
				results.get(events).add(ep);
			} else {
				results.put(events, Sets.newHashSet(ep));
			}
		}
		return results;
	}
}
