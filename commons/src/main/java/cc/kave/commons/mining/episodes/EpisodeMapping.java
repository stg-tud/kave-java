package cc.kave.commons.mining.episodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.kave.commons.model.episodes.Episode;

public class EpisodeMapping {

	public Map<Episode, Integer> generateEpisodeIds(Map<Integer, List<Episode>> learnedEpisodes) throws Exception {

		if (learnedEpisodes.isEmpty()) {
			throw new Exception("The list of learned episodes is empty");
		}

		Map<Episode, Integer> mapping = new HashMap<Episode, Integer>();
		int episodeId = 0;
		for (Map.Entry<Integer, List<Episode>> entry : learnedEpisodes.entrySet()) {
			for (Episode episode : entry.getValue()) {
				mapping.put(episode, episodeId);
				episodeId++;
			}
		}
		return mapping;
	}
}
