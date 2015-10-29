package cc.kave.commons.mining.episodes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Fact;

public class NoTransitivelyClosedEpisodes {

	public Map<Integer, List<Episode>> skipTransitivelyClosure(Map<Integer, List<Episode>> learnedEpisodes) {

		Map<Integer, List<Episode>> results = new HashMap<Integer, List<Episode>>();
		List<List<String>> paths = new LinkedList<List<String>>();

		for (Map.Entry<Integer, List<Episode>> entry : learnedEpisodes.entrySet()) {
			if (entry.getKey() > 2) {
				for (Episode episode : entry.getValue()) {
					Episode newEpisode = simplifyEpisode(episode);
				}
			}
		}

		return null;
	}

	private Episode simplifyEpisode(Episode episode) {
		List<List<String>> paths = new LinkedList<List<String>>();
		Episode episodeResult = new Episode();
		for (Fact fact : episode.getFacts()) {
			if (fact.getRawFact().length() > 1) {
				String[] events = fact.getRawFact().split(">");
				boolean pathFound = false;
				for (List<String> list : paths) {
					if (list.get(list.size() - 1).equalsIgnoreCase(events[0])) {
						list.add(events[1]);
						pathFound = true;
					}
				}
				if (!pathFound) {
					List<String> newPaths = new LinkedList<String>();
					newPaths.add(events[0]);
					newPaths.add(events[1]);
					paths.add(newPaths);
				}
			} else {
				episodeResult.addFact(fact.getRawFact());
			}
		}
		for (int outerIdx = 0; outerIdx < paths.size(); outerIdx++) {
			if (paths.get(outerIdx).size() > 2) {
				for (int idx = 0; idx < paths.get(outerIdx).size() - 1; idx++) {
					episodeResult.addFact(paths.get(outerIdx).get(idx) + ">" + paths.get(outerIdx).get(idx + 1));
				}
			}
			removeClosures(paths, outerIdx);
		}
		return null;
	}

	private void removeClosures(List<List<String>> paths, int outerIdx) {
		List<String> currentList = paths.get(outerIdx);
		for (int currIdx = 0; currIdx < currentList.size() - 2; currIdx++) {
			for (int pathsIdx = outerIdx + 1; pathsIdx < paths.size(); pathsIdx++) {
				if (paths.get(pathsIdx).contains(currentList.get(currIdx))
						&& paths.get(pathsIdx).contains(currentList.get(currIdx))) {
					int firstEventIdx = paths.indexOf(currentList.get(currIdx));
					int secondEventIndex = paths.indexOf(currentList.get(currIdx + 2));
					if ((firstEventIdx + 1) == secondEventIndex) {
						if (paths.get(pathsIdx).size() == 2) {
							paths.remove(pathsIdx);
						} else {
							paths.get(pathsIdx).remove(firstEventIdx);
						}
					}
				}
			}
		}
	}
}
