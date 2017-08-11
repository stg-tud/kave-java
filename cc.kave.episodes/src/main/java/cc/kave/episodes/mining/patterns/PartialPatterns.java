package cc.kave.episodes.mining.patterns;

import java.util.Map;
import java.util.Set;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PartialPatterns {

	public Map<Integer, Set<Episode>> filter(
			Map<Integer, Set<Episode>> episodes, int frequency, double entropy) {
		Map<Integer, Set<Episode>> output = Maps.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			if (entry.getKey() == 1) {
				continue;
			}
			Set<Episode> episodesLevel = Sets.newLinkedHashSet();
			Set<Episode> filtered = Sets.newLinkedHashSet();

			for (Episode ep : entry.getValue()) {
				if ((ep.getFrequency() >= frequency)
						&& (ep.getEntropy() >= entropy)) {
					episodesLevel.add(ep);
				}
			}
			if (!episodesLevel.isEmpty()) {
				Map<Set<Fact>, Set<Episode>> groups = grouping(episodesLevel);
				for (Map.Entry<Set<Fact>, Set<Episode>> entryGroup : groups
						.entrySet()) {
					Set<Episode> currGroup = entryGroup.getValue();
					if (currGroup.size() == 1) {
						filtered.addAll(currGroup);
					} else {
						Set<Episode> reprs = getRepresentatives(entryGroup
								.getValue());
						filtered.addAll(reprs);
					}
				}
			}
			output.put(entry.getKey(), filtered);
		}
		return output;
	}

	private Set<Episode> getRepresentatives(Set<Episode> episodes) {
		Set<Episode> results = Sets.newLinkedHashSet();
		Map<Integer, Set<Episode>> relsLevels = getRelLevels(episodes);

		if (relsLevels.size() == 1) {
			results.addAll(getSingleValue(relsLevels));
			return results;
		}
		while (!representative(results, relsLevels)) {
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
	
	private Set<Episode> getSingleValue(Map<Integer, Set<Episode>> relsLevels) {
		for (Map.Entry<Integer, Set<Episode>> entry : relsLevels.entrySet()) {
			return entry.getValue();
		}
		return null;
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
}
