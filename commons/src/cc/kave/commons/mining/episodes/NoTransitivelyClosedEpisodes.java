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
					Episode newEpisode = reduceRelations(episode);
					updatedEpisodes.add(newEpisode);
				}
				results.put(entry.getKey(), updatedEpisodes);
			}
		}
		return results;
	}

	private Episode reduceRelations(Episode episode) {
		List<List<String>> allPaths = new LinkedList<List<String>>();
		Episode episodeResult = new Episode();
		episodeResult.setFrequency(episode.getFrequency());
		episodeResult.setNumEvents(episode.getNumEvents());
		for (Fact fact : episode.getFacts()) {
			if (fact.getRawFact().contains(">")) {
				addFactPath(fact, allPaths);
			} else {
				episodeResult.addFact(fact.getRawFact());
			}
		}
		List<String> simplifiedRelations = removeClosureRelations(allPaths);
		List<String> finalRelations = doubleCheckClosureRemoval(simplifiedRelations);
		episodeResult.addListOfFacts(finalRelations);
		return episodeResult;

	}

	private List<String> doubleCheckClosureRemoval(List<String> simplifiedRelations) {
		List<String> positiveRelations = new LinkedList<String>();
		List<String> negativeRelations = new LinkedList<String>();
		for (int idStart = 0; idStart < simplifiedRelations.size() - 1; idStart++) {
			String relation = simplifiedRelations.get(idStart);
			if (!(positiveRelations.contains(relation) || negativeRelations.contains(relation))) {
				positiveRelations.add(relation);
			}
			String[] eventsStart = relation.split(">");
			for (int idContinue = idStart + 1; idContinue < simplifiedRelations.size(); idContinue++) {
				String[] eventsContinue = simplifiedRelations.get(idContinue).split(">");
				if (eventsStart[1].equalsIgnoreCase(eventsContinue[0])) {
					negativeRelations.add(eventsStart[0] + ">" + eventsContinue[1]);
				}
				if (eventsStart[0].equalsIgnoreCase(eventsContinue[1])) {
					negativeRelations.add(eventsContinue[0] + ">" + eventsStart[1]);
				}
			}
		}
		String lastRelation = simplifiedRelations.get(simplifiedRelations.size() - 1);
		if (!(negativeRelations.contains(lastRelation) || positiveRelations.contains(lastRelation))) {
			positiveRelations.add(lastRelation);
		}
		for (String relation : negativeRelations) {
			if (positiveRelations.contains(relation)) {
				positiveRelations.remove(relation);
			}
		}
		return positiveRelations;
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
					if (positiveRelations.contains(relation)) {
						positiveRelations.remove(relation);
					}
				}
			}
		}
		return positiveRelations;
	}

	private void addFactPath(Fact fact, List<List<String>> allPaths) {
		String[] events = fact.getRawFact().split(">");
		boolean pathFound = false;
		if (!allPaths.isEmpty()) {
			for (List<String> list : allPaths) {
				if (list.get(list.size() - 1).equalsIgnoreCase(events[0])) {
					list.add(events[1]);
					pathFound = true;
				} else if (list.get(0).equalsIgnoreCase(events[1])) {
					list.add(0, events[0]);
					pathFound = true;
				}
			}
		}
		if (!pathFound) {
			List<String> factPaths = new LinkedList<String>();
			factPaths.add(events[0]);
			factPaths.add(events[1]);
			allPaths.add(factPaths);
		}
	}
}
