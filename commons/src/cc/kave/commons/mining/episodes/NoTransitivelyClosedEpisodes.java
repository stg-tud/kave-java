package cc.kave.commons.mining.episodes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Fact;

public class NoTransitivelyClosedEpisodes {

	public Map<Integer, List<Episode>> removeTransitivelyClosure(Map<Integer, List<Episode>> maximalEpisodes)
			throws Exception {

		if (maximalEpisodes.isEmpty()) {
			throw new Exception("You passed an empty list of maximal episode!");
		}

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
		episodeResult.setFrequency(episode.getFrequency());
		episodeResult.setNumEvents(episode.getNumEvents());
		for (Fact fact : episode.getFacts()) {
			if (fact.getRawFact().length() > 1) {
				allPaths = addPath(fact, allPaths);
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

	private List<List<String>> addPath(Fact fact, List<List<String>> allPaths) {
		List<List<String>> newPathsList = new LinkedList<List<String>>();
		String[] events = fact.getRawFact().split(">");
		boolean pathFound = false;
		if (!allPaths.isEmpty()) {
			for (List<String> list : allPaths) {
				if (list.get(list.size() - 1).equalsIgnoreCase(events[0])) {
					list.add(events[1]);
					pathFound = true;
				} else if (list.get(0).equalsIgnoreCase(events[1])) {
					int listIdx = allPaths.indexOf(list);
					List<String> addToNewPathsList = allPaths.get(listIdx);
					newPathsList.add(addToNewPathsList);
					pathFound = true;
				}
			}
			if (!newPathsList.isEmpty()) {
				return recreateList(allPaths, events);
			}
		}
		if (!pathFound) {
			List<String> newPaths = new LinkedList<String>();
			newPaths.add(events[0]);
			newPaths.add(events[1]);
			allPaths.add(newPaths);
		}
		return allPaths;
	}

	private List<List<String>> recreateList(List<List<String>> allPaths, String[] events) {
		List<List<String>> newList = new LinkedList<List<String>>();
		for (List<String> path : allPaths) {
			List<String> newPath = new LinkedList<>();
			newPath.add(events[0]);
			for (String s : path) {
				newPath.add(s);
			}
			newList.add(newPath);
		}
		return newList;
	}
}
