package cc.kave.episodes.mining.patterns;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.episodes.model.Episode;

public class SequentialPatterns {

	public Map<Integer, Set<Episode>> filter(
			Map<Integer, Set<Episode>> episodes, int frequency) {
		Map<Integer, Set<Episode>> results = Maps.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			if (entry.getKey() == 1) {
				continue;
			}
			Set<Episode> filtered = Sets.newLinkedHashSet();

			for (Episode ep : entry.getValue()) {
				if ((ep.getFrequency() >= frequency)
						&& (ep.getEntropy() == 1.0)
						&& (ep.getRelations().size() == numRels(ep
								.getNumEvents()))) {
					filtered.add(ep);
				}
			}
			if (!filtered.isEmpty()) {
				results.put(entry.getKey(), filtered);
			}
		}
		return results;
	}

	private int numRels(int numEvents) {
		int numRels = 0;
		if (numEvents == 2) {
			return 1;
		} else {
			numRels = numEvents - 1 + numRels(numEvents - 1);
			return numRels;
		}
	}
}
