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
package cc.kave.episodes.postprocessor;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.Map;
import java.util.Set;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Fact;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class MaximalEpisodes {
	
	public Map<Integer, Set<Episode>> getMaximalEpisodes(Map<Integer, Set<Episode>> episodes) {
		assertTrue(!episodes.isEmpty(), "The list of learned episodes is empty!");
		
		Map<Integer, Set<Episode>> maximalEpisodes = Maps.newLinkedHashMap();
		
		if (episodes.size() == 1) {
			return episodes;
		}		
		
		int[] episodeLevels = getEpisodeLevels(episodes);
		
		for (int level = 1; level < episodeLevels.length; level++) {
			Set<Episode> maximalEpisodesSet = removeSubepisodes(episodes.get(episodeLevels[level - 1]), episodes.get(episodeLevels[level]));
			if (!maximalEpisodesSet.isEmpty()) {
				maximalEpisodes.put((episodeLevels[level - 1]), maximalEpisodesSet);
			}
		}
		maximalEpisodes.put(episodeLevels[episodeLevels.length - 1], episodes.get(episodeLevels[episodeLevels.length - 1]));
		return maximalEpisodes;
	}
	
	private int[] getEpisodeLevels(Map<Integer, Set<Episode>> allEpisodes) {
		int[] episodeLevels = new int[allEpisodes.size()];
		int index = 0;
		for (Map.Entry<Integer, Set<Episode>> entry : allEpisodes.entrySet()) {
			episodeLevels[index] = entry.getKey();
			index++;
		}
		return episodeLevels;
	}
	
	private Set<Episode> removeSubepisodes(Set<Episode> smallerNodeEpisodeList, Set<Episode> biggerNodeEpisodeList) {
		Set<Episode> reducedEpisodeList = Sets.newHashSet();
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
