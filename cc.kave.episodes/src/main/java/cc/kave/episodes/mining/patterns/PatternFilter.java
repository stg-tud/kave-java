package cc.kave.episodes.mining.patterns;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.google.common.collect.Sets;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;

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

	public Set<Episode> subPatterns(EpisodeType type,
			Map<Integer, Set<Episode>> episodes, int thf, double the,
			double thsp) throws Exception {
		Map<Integer, Set<Episode>> patterns = filter(type, episodes, thf, the);

		assertTrue(!patterns.isEmpty(),
				"The list of learned patterns is empty!");

		Set<Episode> results = Sets.newLinkedHashSet();
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			if (entry.getKey() == (patterns.size() + 1)) {
				results.addAll(entry.getValue());
				break;
			}
			results.addAll(getPatterns(entry.getValue(),
					patterns.get(entry.getKey() + 1), thsp));
		}
		return results;
	}

	private Set<Episode> getPatterns(Set<Episode> subPatterns,
			Set<Episode> patterns, double thsp) {
		Set<Episode> results = Sets.newLinkedHashSet();

		for (Episode sp : subPatterns) {
			if (!isSubpattern(sp, patterns, thsp)) {
				results.add(sp);
			}
		}
		return results;
	}

	private boolean isSubpattern(Episode sp, Set<Episode> patterns, double thsp) {
		for (Episode p : patterns) {
			if (p.getFacts().containsAll(sp.getFacts())
					&& ((p.getFrequency() / sp.getFrequency()) < thsp)) {
				return true;
			}
		}
		return false;
	}
}
