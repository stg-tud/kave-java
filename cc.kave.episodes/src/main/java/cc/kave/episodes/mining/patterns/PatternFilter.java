package cc.kave.episodes.mining.patterns;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.Map;
import java.util.Set;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PatternFilter {

	public Map<Integer, Set<Episode>> filter(
			Map<Integer, Set<Episode>> episodes, int frequency, double entropy)
			throws Exception {
		Map<Integer, Set<Episode>> results = Maps.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			if (entry.getKey() == 1) {
				continue;
			}
			Set<Episode> current = Sets.newLinkedHashSet();
			for (Episode episode : entry.getValue()) {
				if ((episode.getFrequency() >= frequency)
						&& (episode.getEntropy() >= entropy)) {
					current.add(episode);
				}
			}
			if (!current.isEmpty()) {
				results.put(entry.getKey(), current);
			}
		}
		return results;
	}

	public Map<Integer, Set<Episode>> subEpisodes(
			Map<Integer, Set<Episode>> episodes, double thsp) throws Exception {

		assertTrue(!episodes.isEmpty(), "The list of episodes is empty!");

		Map<Integer, Set<Episode>> results = Maps.newLinkedHashMap();

		Map<Integer, Map<Set<Fact>, Set<Episode>>> episodeStruct = getEpStruct(episodes);

		for (Map.Entry<Integer, Map<Set<Fact>, Set<Episode>>> entry : episodeStruct
				.entrySet()) {
			if (entry.getKey() == 1) {
				continue;
			}
			if (entry.getKey() == episodes.size()) {
				results.put(entry.getKey(), episodes.get(entry.getKey()));
				break;
			}
			results.put(
					entry.getKey(),
					getSuperEpisodes(entry.getValue(),
							episodeStruct.get(entry.getKey() + 1), thsp));
		}
		return results;
	}

	public Map<Integer, Set<Episode>> representatives(
			Map<Integer, Set<Episode>> episodes) {
		Map<Integer, Set<Episode>> results = Maps.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			Map<Set<Fact>, Set<Episode>> groups = grouping(entry.getValue());
			Set<Episode> episodeSet = Sets.newLinkedHashSet();

			for (Map.Entry<Set<Fact>, Set<Episode>> entryGroup : groups
					.entrySet()) {
				if (entryGroup.getValue().size() == 1) {
					episodeSet.addAll(entryGroup.getValue());
				} else {
					Set<Episode> reprs = getRepresentatives(entryGroup
							.getValue());
					episodeSet.addAll(reprs);
				}
			}
			results.put(entry.getKey(), episodeSet);
		}
		return results;
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

	private boolean isCovered(Episode episode, Set<Episode> reprs) {
		for (Episode rep : reprs) {
			if (represents(rep, episode)) {
				return true;
			}
		}
		return false;
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

	private Set<Episode> getSingleValue(Map<Integer, Set<Episode>> relsLevels) {
		for (Map.Entry<Integer, Set<Episode>> entry : relsLevels.entrySet()) {
			return entry.getValue();
		}
		return null;
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

	private Map<Integer, Map<Set<Fact>, Set<Episode>>> getEpStruct(
			Map<Integer, Set<Episode>> episodes) {
		Map<Integer, Map<Set<Fact>, Set<Episode>>> results = Maps
				.newLinkedHashMap();
		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			results.put(entry.getKey(), getStruct(entry.getValue()));
		}
		return results;
	}

	private Map<Set<Fact>, Set<Episode>> getStruct(Set<Episode> episodes) {
		Map<Set<Fact>, Set<Episode>> results = Maps.newLinkedHashMap();
		for (Episode episode : episodes) {
			Set<Fact> events = episode.getEvents();
			if (results.containsKey(events)) {
				results.get(events).add(episode);
			} else {
				results.put(events, Sets.newHashSet(episode));
			}
		}
		return results;
	}

	private Set<Episode> getSuperEpisodes(Map<Set<Fact>, Set<Episode>> subs,
			Map<Set<Fact>, Set<Episode>> supers, double thsp) {
		Set<Episode> results = Sets.newLinkedHashSet();
		for (Map.Entry<Set<Fact>, Set<Episode>> smallEntry : subs.entrySet()) {
			Set<Episode> potSSupers = Sets.newLinkedHashSet();
			for (Map.Entry<Set<Fact>, Set<Episode>> bigEntry : supers
					.entrySet()) {
				if (bigEntry.getKey().containsAll(smallEntry.getKey())) {
					potSSupers.addAll(bigEntry.getValue());
				}
			}
			for (Episode episode : smallEntry.getValue()) {
				if (!isSubepisode(episode, potSSupers, thsp)) {
					results.add(episode);
				}
			}
		}
		return results;
	}

	private boolean isSubepisode(Episode subepisode, Set<Episode> superepisode,
			double thsp) {
		for (Episode ep : superepisode) {
			if (ep.getFacts().containsAll(subepisode.getFacts())) {
				return true;
			}
		}
		return false;
	}
}
