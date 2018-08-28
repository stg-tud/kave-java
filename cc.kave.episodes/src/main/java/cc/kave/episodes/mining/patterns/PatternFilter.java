package cc.kave.episodes.mining.patterns;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PatternFilter {

	private PartialPatterns partials;
	private SequentialPatterns sequentials;
	private ParallelPatterns parallels;

	@Inject
	public PatternFilter(PartialPatterns partials,
			SequentialPatterns sequentials, ParallelPatterns parallels) {
		this.partials = partials;
		this.sequentials = sequentials;
		this.parallels = parallels;
	}

	public Map<Integer, Set<Episode>> filter(EpisodeType type,
			Map<Integer, Set<Episode>> episodes, int fthresh, double ethresh)
			throws Exception {

		if (type == EpisodeType.GENERAL) {
			return partials.filter(episodes, fthresh, ethresh);
		} else if (type == EpisodeType.SEQUENTIAL) {
			return sequentials.filter(episodes, fthresh);
		} else if (type == EpisodeType.PARALLEL) {
			return parallels.filter(episodes, fthresh);
		} else {
			throw new Exception("Not valid episode type!");
		}
	}

	public Map<Integer, Set<Episode>> subEpisodes(
			Map<Integer, Set<Episode>> episodes, double thsp) throws Exception {

		assertTrue(!episodes.isEmpty(), "The list of episodes is empty!");

		Map<Integer, Set<Episode>> results = Maps.newLinkedHashMap();
		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			if (entry.getKey() == 1) {
				continue;
			}
			if (entry.getKey() == episodes.size()) {
				results.put(entry.getKey(), entry.getValue());
				break;
			}
			Set<Episode> superepisodes = episodes.get(entry.getKey() + 1);
			results.put(entry.getKey(),
					getSubEpisodes(entry.getValue(), superepisodes, thsp));
		}
		return results;
	}

	private Set<Episode> getSubEpisodes(Set<Episode> subEpisodes,
			Set<Episode> superEpisodes, double thsp) {
		Set<Episode> results = Sets.newLinkedHashSet();

		for (Episode sp : subEpisodes) {
			if (!isSubepisode(sp, superEpisodes, thsp)) {
				results.add(sp);
			}
		}
		return results;
	}

	private boolean isSubepisode(Episode subepisode, Set<Episode> superepisode,
			double thsp) {
		for (Episode ep : superepisode) {
			if (ep.getFacts().containsAll(subepisode.getFacts())
					&& ((ep.getFrequency() / subepisode.getFrequency()) < thsp)) {
				return true;
			}
		}
		return false;
	}
}
