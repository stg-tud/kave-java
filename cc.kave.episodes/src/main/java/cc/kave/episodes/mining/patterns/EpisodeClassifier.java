package cc.kave.episodes.mining.patterns;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.episodes.io.EpisodeReader;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class EpisodeClassifier {

	private EpisodeReader episodeReader;

	@Inject
	public EpisodeClassifier(EpisodeReader reader) {
		this.episodeReader = reader;
	}

	public Map<Integer, Set<Episode>> getEpisodeType(EpisodeType type, int frequency,
			double entropy) {
		Map<Integer, Set<Episode>> output = Maps.newLinkedHashMap();
		Map<Integer, Set<Episode>> episodes = episodeReader.read(frequency);

		if (type == EpisodeType.GENERAL) {
			return generalEpisode(episodes, frequency, entropy);
		} else if (type == EpisodeType.SEQUENTIAL) {
			return sequentialEpisodes(episodes, frequency);
		} else if (type == EpisodeType.PARALLEL) {
			return (parallelEpisodes(episodes, frequency));
		}

		return output;
	}

	private Map<Integer, Set<Episode>> parallelEpisodes(
			Map<Integer, Set<Episode>> allEpisodes, int frequency) {
		Map<Integer, Set<Episode>> output = Maps.newLinkedHashMap();
		
		for (Map.Entry<Integer, Set<Episode>> entry : allEpisodes.entrySet()) {
			if (entry.getKey() == 1) {
				continue;
			}
			Logger.log("Number of node episodes: %d", entry.getKey());
			Map<Set<Fact>, Set<Episode>> episodeGroups = getEpisodeGroups(entry.getValue());
			Logger.log("Number of sets of facts: %d", episodeGroups.size());
			Set<Episode> currentEpisodes = Sets.newHashSet();
			
			for (Map.Entry<Set<Fact>, Set<Episode>> entryEpisodes : episodeGroups.entrySet()) {
				for (Episode episode : entryEpisodes.getValue()) {
					if (episode.getRelations().isEmpty()) {
						currentEpisodes.add(episode);
						break;
					} 
				}
			}
		}
		return output;
	}

	private Map<Set<Fact>, Set<Episode>> getEpisodeGroups(Set<Episode> episodes) {
		Map<Set<Fact>, Set<Episode>> output = Maps.newLinkedHashMap();
		
		for (Episode ep : episodes) {
			Set<Fact> events = ep.getEvents();
			
			if (output.containsKey(events)) {
				output.get(events).add(ep);
			} else {
				output.put(events, Sets.newHashSet(ep));
			}
		}
		return output;
	}

	private Map<Integer, Set<Episode>> sequentialEpisodes(
			Map<Integer, Set<Episode>> allEpisodes, int frequency) {
		Map<Integer, Set<Episode>> output = Maps.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entry : allEpisodes.entrySet()) {
			Set<Episode> episodes = Sets.newLinkedHashSet();

			for (Episode ep : entry.getValue()) {
				if ((ep.getFrequency() >= frequency) && (ep.getEntropy() == 1)
						&& (!ep.getRelations().isEmpty())) {
					episodes.add(ep);
				}
			}
			if (!episodes.isEmpty()) {
				output.put(entry.getKey(), episodes);
			}
		}
		return output;
	}

	private Map<Integer, Set<Episode>> generalEpisode(
			Map<Integer, Set<Episode>> allEpisodes, int frequency,
			double entropy) {
		Map<Integer, Set<Episode>> output = Maps.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entry : allEpisodes.entrySet()) {
			Set<Episode> episodes = Sets.newLinkedHashSet();

			for (Episode ep : entry.getValue()) {
				if ((ep.getFrequency() >= frequency)
						&& (ep.getEntropy() >= entropy)) {
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
