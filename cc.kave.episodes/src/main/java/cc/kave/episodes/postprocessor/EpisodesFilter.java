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
		Map<Integer, Set<Episode>> patterns = Maps.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			if (entry.getKey() == 1) {
				continue;
			}
			Set<Episode> threshFilter = Sets.newLinkedHashSet();

			for (Episode ep : entry.getValue()) {
				int freq = ep.getFrequency();

				if (freq >= frequency) {
					threshFilter.add(ep);
				}
			}
			patterns.put(entry.getKey(), threshFilter);
		}
		if (type == EpisodeType.GENERAL) {
			return repPatterns(patterns, entropy);
		}
		return patterns;
	}

	private Map<Integer, Set<Episode>> repPatterns(
			Map<Integer, Set<Episode>> patterns, double entropy) {
		Map<Integer, Set<Episode>> results = Maps.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			Map<Set<Fact>, Episode> filtered = Maps.newLinkedHashMap();

			for (Episode episode : entry.getValue()) {
				if (episode.getEntropy() >= entropy) {
					if (filtered.containsKey(episode.getEvents())) {
						Set<Fact> events = episode.getEvents();
						Episode filterEp = filtered.get(events);
						filtered.remove(events);

						Episode repEp = getRepresentative(filterEp, episode);
						filtered.put(repEp.getEvents(), repEp);
					} else {
						filtered.put(episode.getEvents(), episode);
					}
				} 
			}
			Set<Episode> repEpisodes = getfilteredEp(filtered);
			results.put(entry.getKey(), repEpisodes);
		}
		return results;
	}

	private Set<Episode> getfilteredEp(Map<Set<Fact>, Episode> filtered) {
		Set<Episode> episodes = Sets.newLinkedHashSet();

		for (Map.Entry<Set<Fact>, Episode> entry : filtered.entrySet()) {
			episodes.add(entry.getValue());
		}
		return episodes;
	}

	private Episode getRepresentative(Episode filterEp, Episode currEp) {
		int ffreq = filterEp.getFrequency();
		double fentropy = filterEp.getEntropy();

		int cfreq = currEp.getFrequency();
		double centropy = currEp.getEntropy();

		if (ffreq > cfreq) {
			return filterEp;
		} else if (ffreq < cfreq) {
			return currEp;
		} else {
			if (fentropy < centropy) {
				return filterEp;
			} else if (fentropy > centropy) {
				return currEp;
			} else {
				Set<Fact> e1Order = filterEp.getRelations();
				Set<Fact> e2Order = currEp.getRelations();
				Set<Fact> repFacts = Sets.newHashSet();

				for (Fact fact : e1Order) {
					Tuple<Fact, Fact> orderFacts = fact.getRelationFacts();
					Fact repFact = new Fact(orderFacts.getSecond(),
							orderFacts.getFirst());

					if (!e2Order.contains(repFact)) {
						repFacts.add(fact);
					}
				}
				Episode representative = createEpisode(repFacts, filterEp);
				return representative;
			}
		}
	}

	private Episode createEpisode(Set<Fact> repFacts, Episode filterEp) {
		Episode episode = new Episode();
		episode.setFrequency(filterEp.getFrequency());
		episode.setEntropy(filterEp.getEntropy());

		for (Fact event : filterEp.getEvents()) {
			episode.addFact(event);
		}
		for (Fact fact : repFacts) {
			episode.addFact(fact);
		}
		return episode;
	}
}
