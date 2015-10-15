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
package cc.kave.commons.mining.episodes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Fact;

public class MaximalFrequentEpisodes {
	
	public Map<Integer, List<Episode>> getMaximalFrequentEpisodes(Map<Integer, List<Episode>> freqEpisodes) throws Exception {
		Map<Integer, List<Episode>> maximalFrequentEpisodes = new HashMap<Integer, List<Episode>>();
		
		if (freqEpisodes.isEmpty()) {
			throw new Exception("The list of learned episodes is empty!");
		} 
		
		if (freqEpisodes.size() == 1) {
			return freqEpisodes;
		}		
		
		int[] nodeEpisodes = getNodeEpisodes(freqEpisodes);
		
		for (int idx = 1; idx < nodeEpisodes.length; idx++) {
			List<Episode> reducedEpisodeList = reduceToMaximalEpisodes(freqEpisodes.get(nodeEpisodes[idx - 1]), freqEpisodes.get(nodeEpisodes[idx]));
			if (!reducedEpisodeList.isEmpty()) {
				maximalFrequentEpisodes.put((nodeEpisodes[idx - 1]), reducedEpisodeList);
			}
		}
		maximalFrequentEpisodes.put(nodeEpisodes[nodeEpisodes.length - 1], freqEpisodes.get(nodeEpisodes[nodeEpisodes.length - 1]));
		return maximalFrequentEpisodes;
	}
	
	private int[] getNodeEpisodes(Map<Integer, List<Episode>> allEpisodes) {
		int[] nodeEpisodes = new int[allEpisodes.size()];
		int index = 0;
		for (Map.Entry<Integer, List<Episode>> entry : allEpisodes.entrySet()) {
			nodeEpisodes[index] = entry.getKey();
			index++;
		}
		return nodeEpisodes;
	}
	
	private List<Episode> reduceToMaximalEpisodes(List<Episode> smallerNodeEpisodeList, List<Episode> biggerNodeEpisodeList) {
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
