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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import cc.kave.commons.mining.episodes.MaximalFrequentEpisodes;
import cc.kave.commons.model.episodes.Episode;

public class MaximalFrequentEpisodesTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();

	private Episode episode1;
	private Episode episode2;
	private Episode episode3;
	private Episode episode4;
	private Episode episode5;
	private Episode episode6;

	private MaximalFrequentEpisodes sut;

	@Before
	public void setup() {
		sut = new MaximalFrequentEpisodes();

		episode1 = new Episode();
		episode1.addFact("1");
		episode1.setNumEvents(1);
		episode1.setFrequency(3);

		episode2 = new Episode();
		episode2.addFact("2");
		episode2.setNumEvents(1);
		episode2.setFrequency(3);

		episode3 = new Episode();
		episode3.addFact("3");
		episode3.setNumEvents(1);
		episode3.setFrequency(3);

		episode4 = new Episode();
		episode4.addFacts("1", "2", "1>2");
		episode4.setNumEvents(2);
		episode4.setFrequency(3);

		episode5 = new Episode();
		episode5.addFacts("1", "3", "1>3");
		episode5.setNumEvents(2);
		episode5.setFrequency(4);
		
		episode6 = new Episode();
		episode6.addFacts("2", "3", "2>3");
		episode6.setNumEvents(2);
		episode6.setFrequency(2);
	}
	
	@Test(expected=Exception.class)
	public void cannotBeUsedWithEmptyEpisodeLists() throws Exception {
		Map<Integer, List<Episode>> emptyEpisodeLists = new HashMap<Integer, List<Episode>>();
		sut.getMaximalFrequentEpisodes(emptyEpisodeLists);
	}
	
	@Test
	public void oneLevelNodeEpisodes() throws Exception {
		Map<Integer, List<Episode>> initialEpisodes = new HashMap<Integer, List<Episode>>();
		List<Episode> episodeList = new LinkedList<Episode>();
		
		episodeList.add(episode4);
		episodeList.add(episode5);
		episodeList.add(episode6);
		
		initialEpisodes.put(2, episodeList);
		
		Map<Integer, List<Episode>> expecteds = new HashMap<Integer, List<Episode>>();
		episodeList = new LinkedList<Episode>();
		
		episodeList.add(episode4);
		episodeList.add(episode5);
		episodeList.add(episode6);
		
		expecteds.put(2, episodeList);
		
		Map<Integer, List<Episode>> actuals = sut.getMaximalFrequentEpisodes(initialEpisodes);
		
		assertEquals(expecteds, actuals);
	}
	
	@Test
	public void reduceOneNodeLevelEpisode() throws Exception {
		Map<Integer, List<Episode>> initialEpisodes = new HashMap<Integer, List<Episode>>();
		List<Episode> episodeList = new LinkedList<Episode>();
		
		episodeList.add(episode1);
		episodeList.add(episode2);
		episodeList.add(episode3);
		
		initialEpisodes.put(1, episodeList);
		
		episodeList = new LinkedList<Episode>();
		
		episodeList.add(episode4);
		episodeList.add(episode5);
		
		initialEpisodes.put(2, episodeList);
		
		Map<Integer, List<Episode>> expecteds = new HashMap<Integer, List<Episode>>();
		episodeList = new LinkedList<Episode>();
		
		episodeList.add(episode4);
		episodeList.add(episode5);
		
		expecteds.put(2, episodeList);
		
		Map<Integer, List<Episode>> actuals = sut.getMaximalFrequentEpisodes(initialEpisodes);
		
		assertEquals(expecteds, actuals);
	}

	@Test
	public void twoNodeEpisode() throws Exception {
		Map<Integer, List<Episode>> initialEpisodes = new HashMap<Integer, List<Episode>>();
		List<Episode> episodeList = new LinkedList<Episode>();

		episodeList.add(episode1);
		episodeList.add(episode2);
		episodeList.add(episode3);

		initialEpisodes.put(1, episodeList);

		episodeList = new LinkedList<Episode>();
		
		episodeList.add(episode4);

		initialEpisodes.put(2, episodeList);

		Map<Integer, List<Episode>> expected = new HashMap<Integer, List<Episode>>();
		List<Episode> episodeListExp = new LinkedList<Episode>();

		episodeListExp.add(episode3);

		expected.put(1, episodeListExp);

		episodeListExp = new LinkedList<Episode>();
		
		episodeListExp.add(episode4);

		expected.put(2, episodeListExp);

		Map<Integer, List<Episode>> actuals = sut.getMaximalFrequentEpisodes(initialEpisodes);

		assertEquals(expected, actuals);
	}

	@Test
	public void twoNodeEpisodeEx2() throws Exception {
		Map<Integer, List<Episode>> initialEpisodes = new HashMap<Integer, List<Episode>>();
		List<Episode> episodeList = new LinkedList<Episode>();
		
		episodeList.add(episode1);
		episodeList.add(episode2);
		episodeList.add(episode3);

		initialEpisodes.put(1, episodeList);

		episodeList = new LinkedList<Episode>();

		episodeList.add(episode5);

		initialEpisodes.put(2, episodeList);

		Map<Integer, List<Episode>> expected = new HashMap<Integer, List<Episode>>();
		List<Episode> episodeListExp = new LinkedList<Episode>();
		
		episodeListExp.add(episode2);

		expected.put(1, episodeListExp);

		episodeListExp = new LinkedList<Episode>();
		
		episodeListExp.add(episode5);

		expected.put(2, episodeListExp);

		Map<Integer, List<Episode>> actuals = sut.getMaximalFrequentEpisodes(initialEpisodes);

		assertEquals(expected, actuals);
	}

	@Test
	public void twoNodeEpisodeEx3() throws Exception {
		Map<Integer, List<Episode>> initialEpisodes = new HashMap<Integer, List<Episode>>();
		List<Episode> episodeList = new LinkedList<Episode>();
		
		episodeList.add(episode1);
		episodeList.add(episode2);
		episodeList.add(episode3);

		initialEpisodes.put(1, episodeList);

		episodeList = new LinkedList<Episode>();
		
		episodeList.add(episode6);

		initialEpisodes.put(2, episodeList);

		Map<Integer, List<Episode>> expected = new HashMap<Integer, List<Episode>>();
		List<Episode> episodeListExp = new LinkedList<Episode>();
		
		episodeListExp.add(episode1);

		expected.put(1, episodeListExp);

		episodeListExp = new LinkedList<Episode>();
		
		episodeListExp.add(episode6);

		expected.put(2, episodeListExp);

		Map<Integer, List<Episode>> actuals = sut.getMaximalFrequentEpisodes(initialEpisodes);

		assertEquals(expected, actuals);
	}
}
