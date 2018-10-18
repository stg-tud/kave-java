package cc.kave.episodes.mining.patterns;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Fact;

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
			if (ep.getFacts().containsAll(subepisode.getFacts())
					&& ((ep.getFrequency() / subepisode.getFrequency()) < thsp)) {
				return true;
			}
		}
		return false;
	}
}
