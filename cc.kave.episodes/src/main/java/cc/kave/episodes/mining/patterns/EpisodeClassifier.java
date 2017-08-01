package cc.kave.episodes.mining.patterns;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.episodes.io.EpisodeReader;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class EpisodeClassifier {

	private EpisodeReader episodeReader;

	@Inject
	public EpisodeClassifier(EpisodeReader reader) {
		this.episodeReader = reader;
	}

	Map<Integer, Set<Episode>> getEpisodeType(EpisodeType type, int frequency,
			double entropy) {
		Map<Integer, Set<Episode>> output = Maps.newLinkedHashMap();
		Map<Integer, Set<Episode>> episodes = episodeReader.read(frequency);

		if (type == EpisodeType.GENERAL) {
			return generalEpisode(episodes, frequency, entropy);
		}
		if (type == EpisodeType.SEQUENTIAL) {
			return sequentialEpisodes(episodes, frequency);
		}

		return output;
	}

	private Map<Integer, Set<Episode>> sequentialEpisodes(
			Map<Integer, Set<Episode>> episodes, int frequency) {
		// TODO Auto-generated method stub
		return null;
	}

	private Map<Integer, Set<Episode>> generalEpisode(
			Map<Integer, Set<Episode>> allEpisodes, int frequency, double entropy) {
		Map<Integer, Set<Episode>> output = Maps.newLinkedHashMap();
		
		for (Map.Entry<Integer, Set<Episode>> entry : allEpisodes.entrySet()) {
			Set<Episode> episodes = Sets.newLinkedHashSet();
			
			for (Episode ep : entry.getValue()) {
				if (ep.getEntropy() >= entropy) {
					episodes.add(ep);
				}
			}
			if (!episodes.isEmpty()) {
				output.put(entry.getKey(), episodes);
			}
		}
		return output;
	}
}
