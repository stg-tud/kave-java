/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ervina Cergani - initial API and implementation
 */
package cc.recommenders.mining.episodes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Fact;

public class MaxFreqEpisodes {
	
	public Map<Integer, List<Episode>> getMaxFreqEpisodes(Map<Integer, List<Episode>> freqEpisodes) {
		Map<Integer, List<Episode>> finalEpisodes = new HashMap<Integer, List<Episode>>();
		int numNodes = freqEpisodes.size();
		
		if (numNodes > 1) {
			for (int node = 1; node < numNodes; node ++) {
				List<Episode> reducedList = reduceFreqEpisodes(freqEpisodes.get(node), freqEpisodes.get(node + 1));
				if (reducedList.size() > 0) {
					finalEpisodes.put(node, reducedList);
				}
			}
		}
		finalEpisodes.put(numNodes, freqEpisodes.get(numNodes));
		return finalEpisodes;
	}
	
	private List<Episode> reduceFreqEpisodes(List<Episode> episodeList1, List<Episode> episodeList2) {
		List<Episode> finalList = new LinkedList<Episode>();
		
		for (Episode episode1 : episodeList1) {
			boolean included = false;
			for (Episode episode2 : episodeList2) {
				if (subEpisode(episode1, episode2)) {
					included = true;
					break;
				}
			}
			if (!included) {
				finalList.add(episode1);
			}
		}
		return finalList;
	}

	private boolean subEpisode(Episode episode1, Episode episode2) {
		for (Fact fact1 : episode1.getFacts()) {
			if (episode2.containsFact(fact1)) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}
}
