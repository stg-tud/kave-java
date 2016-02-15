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

import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.model.Pattern;

public class MaximalEpisodes {
	
	public Map<Integer, List<Pattern>> getMaximalEpisodes(Map<Integer, List<Pattern>> episodes) {
		assertTrue(!episodes.isEmpty(), "The list of learned episodes is empty!");
		
		Map<Integer, List<Pattern>> maximalEpisodes = new HashMap<Integer, List<Pattern>>();
		
		if (episodes.size() == 1) {
			return episodes;
		}		
		
		int[] episodeLevels = getEpisodeLevels(episodes);
		
		for (int level = 1; level < episodeLevels.length; level++) {
			List<Pattern> maximalEpisodesList = removeSubepisodes(episodes.get(episodeLevels[level - 1]), episodes.get(episodeLevels[level]));
			if (!maximalEpisodesList.isEmpty()) {
				maximalEpisodes.put((episodeLevels[level - 1]), maximalEpisodesList);
			}
		}
		maximalEpisodes.put(episodeLevels[episodeLevels.length - 1], episodes.get(episodeLevels[episodeLevels.length - 1]));
		return maximalEpisodes;
	}
	
	private int[] getEpisodeLevels(Map<Integer, List<Pattern>> allEpisodes) {
		int[] episodeLevels = new int[allEpisodes.size()];
		int index = 0;
		for (Map.Entry<Integer, List<Pattern>> entry : allEpisodes.entrySet()) {
			episodeLevels[index] = entry.getKey();
			index++;
		}
		return episodeLevels;
	}
	
	private List<Pattern> removeSubepisodes(List<Pattern> smallerNodeEpisodeList, List<Pattern> biggerNodeEpisodeList) {
		List<Pattern> reducedEpisodeList = new LinkedList<Pattern>();
		boolean isSubEpisode = false;
		
		for (Pattern smallerNodeEpisode : smallerNodeEpisodeList) {
			for (Pattern biggerNodeEpisode : biggerNodeEpisodeList) {
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

	private boolean subEpisode(Pattern smallerNodeEpisode, Pattern biggerNodeEpisode) {
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
