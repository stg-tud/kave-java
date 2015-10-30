package cc.kave.commons.mining.episodes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Fact;

public class NoTransitivelyClosedEpisodes {

	public Map<Integer, List<Episode>> removeTransitivelyClosure(Map<Integer, List<Episode>> maximalEpisodes) {

		Map<Integer, List<Episode>> results = new HashMap<Integer, List<Episode>>();

		for (Map.Entry<Integer, List<Episode>> entry : maximalEpisodes.entrySet()) {
			if (entry.getKey() < 3) {
				results.put(entry.getKey(), entry.getValue());
			} else {
				List<Episode> updatedEpisodes = new LinkedList<Episode>();
				for (Episode episode : entry.getValue()) {
					Episode newEpisode = simplifyEpisode(episode);
					updatedEpisodes.add(newEpisode);
				}
				results.put(entry.getKey(), updatedEpisodes);
			}
		}
		return results;
	}

	private Episode simplifyEpisode(Episode episode) {
		List<List<String>> allPaths = new LinkedList<List<String>>();
		Episode episodeResult = new Episode();
		for (Fact fact : episode.getFacts()) {
			if (fact.getRawFact().length() > 1) {
				addPath(fact, allPaths);
			} else {
				episodeResult.addFact(fact.getRawFact());
			}
		}
		List<String> simplifiedRelations = removeClosureRelations(allPaths);
		episodeResult.addListOfFacts(simplifiedRelations);
		return episodeResult;
	}

	private List<String> removeClosureRelations(List<List<String>> allPaths) {
		List<String> positiveRelations = new LinkedList<String>();
		List<String> negativeRelations = new LinkedList<String>();
		for (List<String> list : allPaths) {
			for (int idx = 0; idx < list.size() - 1; idx++) {
				String relation = list.get(idx) + ">" + list.get(idx + 1);
				if (!(positiveRelations.contains(relation) || negativeRelations.contains(relation))) {
					positiveRelations.add(relation);
				}
			}
			if (list.size() > 2) {
				for (int idx = 0; idx < list.size() - 2; idx++) {
					String relation = list.get(idx) + ">" + list.get(idx + 2);
					if (!negativeRelations.contains(relation)) {
						negativeRelations.add(relation);
					}
				}
			}
		}
		return positiveRelations;
	}

	private void addPath(Fact fact, List<List<String>> allPaths) {
		String[] events = fact.getRawFact().split(">");
		boolean pathFound = false;
		for (List<String> list : allPaths) {
			if (list.get(list.size() - 1).equalsIgnoreCase(events[0])) {
				list.add(events[1]);
				pathFound = true;
			}
		}
		if (!pathFound) {
			List<String> newPaths = new LinkedList<String>();
			newPaths.add(events[0]);
			newPaths.add(events[1]);
			allPaths.add(newPaths);
		}
	}
}
