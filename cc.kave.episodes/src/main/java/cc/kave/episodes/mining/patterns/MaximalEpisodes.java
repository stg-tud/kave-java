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
package cc.kave.episodes.mining.patterns;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Fact;

public class MaximalEpisodes {
	
	public Map<Integer, List<Episode>> getMaximalEpisodes(Map<Integer, List<Episode>> episodes) {
		assertTrue(!episodes.isEmpty(), "The list of learned episodes is empty!");
		
		Map<Integer, List<Episode>> maximalEpisodes = new HashMap<Integer, List<Episode>>();
		
		if (episodes.size() == 1) {
			return episodes;
		}		
		
		int[] episodeLevels = getEpisodeLevels(episodes);
		
		for (int level = 1; level < episodeLevels.length; level++) {
			List<Episode> maximalEpisodesList = removeSubepisodes(episodes.get(episodeLevels[level - 1]), episodes.get(episodeLevels[level]));
			if (!maximalEpisodesList.isEmpty()) {
				maximalEpisodes.put((episodeLevels[level - 1]), maximalEpisodesList);
			}
		}
		maximalEpisodes.put(episodeLevels[episodeLevels.length - 1], episodes.get(episodeLevels[episodeLevels.length - 1]));
		return maximalEpisodes;
	}
	
	private int[] getEpisodeLevels(Map<Integer, List<Episode>> allEpisodes) {
		int[] episodeLevels = new int[allEpisodes.size()];
		int index = 0;
		for (Map.Entry<Integer, List<Episode>> entry : allEpisodes.entrySet()) {
			episodeLevels[index] = entry.getKey();
			index++;
		}
		return episodeLevels;
	}
	
	private List<Episode> removeSubepisodes(List<Episode> smallerNodeEpisodeList, List<Episode> biggerNodeEpisodeList) {
		List<Episode> reducedEpisodeList = new LinkedList<Episode>();
		boolean isSubEpisode = false;
		
		for (Episode smallerNodeEpisode : smallerNodeEpisodeList) {
			for (Episode biggerNodeEpisode : biggerNodeEpisodeList) {
				if (subEpisode(smallerNodeEpisode, biggerNodeEpisode)) {
					isSubEpisode = true;
					break;
				}
			}
			if (!isSubEpisode) {
				reducedEpisodeList.add(smallerNodeEpisode);
			}
			isSubEpisode = false;
		}
		return reducedEpisodeList;
	}

	private boolean subEpisode(Episode smallerNodeEpisode, Episode biggerNodeEpisode) {
		for (Fact fact : smallerNodeEpisode.getFacts()) {
			if (biggerNodeEpisode.containsFact(fact)) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}
}
