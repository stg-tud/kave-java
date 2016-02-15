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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import cc.kave.episodes.model.Pattern;
import cc.recommenders.exceptions.AssertionException;

public class MaximalEpisodesTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Pattern episode1;
	private Pattern episode2;
	private Pattern episode3;
	private Pattern episode41;
	private Pattern episode42;
	private Pattern episode5;
	private Pattern episode6;
	private Pattern episode7;

	private Map<Integer, List<Pattern>> episodes = new HashMap<Integer, List<Pattern>>();
	private MaximalEpisodes sut;

	@Before
	public void setup() {
		sut = new MaximalEpisodes();

		episode1 = newEpisode(3, 1, "1");
		episode2 = newEpisode(3, 1, "2");
		episode3 = newEpisode(3, 1, "3");
		episode41 = newEpisode(3, 2, "1", "2", "1>2");
		episode42 = newEpisode(3, 2, "1", "2", "2>1");
		episode5 = newEpisode(4, 2, "1", "3", "1>3");
		episode6 = newEpisode(2, 2, "2", "3", "2>3");
		episode7 = newEpisode(3, 3, "1", "2", "3", "1>2", "1>3");
	}
	
	@Test
	public void cannotGetEmptyListOfEpisodes() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("The list of learned episodes is empty!");
		sut.getMaximalEpisodes(episodes);
	}
	
	@Test
	public void oneLevelNodeEpisodes() {
		Map<Integer, List<Pattern>> initialEpisodes = new HashMap<Integer, List<Pattern>>();
		List<Pattern> episodeList = new LinkedList<Pattern>();
		
		episodeList.add(episode41);
		episodeList.add(episode5);
		episodeList.add(episode6);
		
		initialEpisodes.put(2, episodeList);
		
		Map<Integer, List<Pattern>> expecteds = new HashMap<Integer, List<Pattern>>();
		episodeList = new LinkedList<Pattern>();
		
		episodeList.add(episode41);
		episodeList.add(episode5);
		episodeList.add(episode6);
		
		expecteds.put(2, episodeList);
		
		Map<Integer, List<Pattern>> actuals = sut.getMaximalEpisodes(initialEpisodes);
		
		assertEquals(expecteds, actuals);
	}
	
	@Test
	public void reduceOneNodeLevelEpisode() {
		Map<Integer, List<Pattern>> initialEpisodes = new HashMap<Integer, List<Pattern>>();
		List<Pattern> episodeList = new LinkedList<Pattern>();
		
		episodeList.add(episode1);
		episodeList.add(episode2);
		episodeList.add(episode3);
		
		initialEpisodes.put(1, episodeList);
		
		episodeList = new LinkedList<Pattern>();
		
		episodeList.add(episode41);
		episodeList.add(episode5);
		
		initialEpisodes.put(2, episodeList);
		
		Map<Integer, List<Pattern>> expecteds = new HashMap<Integer, List<Pattern>>();
		episodeList = new LinkedList<Pattern>();
		
		episodeList.add(episode41);
		episodeList.add(episode5);
		
		expecteds.put(2, episodeList);
		
		Map<Integer, List<Pattern>> actuals = sut.getMaximalEpisodes(initialEpisodes);
		
		assertEquals(expecteds, actuals);
	}

	@Test
	public void twoNodeEpisode() throws Exception {
		Map<Integer, List<Pattern>> initialEpisodes = new HashMap<Integer, List<Pattern>>();
		List<Pattern> episodeList = new LinkedList<Pattern>();

		episodeList.add(episode1);
		episodeList.add(episode2);
		episodeList.add(episode3);

		initialEpisodes.put(1, episodeList);

		episodeList = new LinkedList<Pattern>();
		
		episodeList.add(episode41);

		initialEpisodes.put(2, episodeList);

		Map<Integer, List<Pattern>> expected = new HashMap<Integer, List<Pattern>>();
		List<Pattern> episodeListExp = new LinkedList<Pattern>();

		episodeListExp.add(episode3);

		expected.put(1, episodeListExp);

		episodeListExp = new LinkedList<Pattern>();
		
		episodeListExp.add(episode41);

		expected.put(2, episodeListExp);

		Map<Integer, List<Pattern>> actuals = sut.getMaximalEpisodes(initialEpisodes);

		assertEquals(expected, actuals);
	}

	@Test
	public void twoNodeEpisodeEx2() throws Exception {
		Map<Integer, List<Pattern>> initialEpisodes = new HashMap<Integer, List<Pattern>>();
		List<Pattern> episodeList = new LinkedList<Pattern>();
		
		episodeList.add(episode1);
		episodeList.add(episode2);
		episodeList.add(episode3);

		initialEpisodes.put(1, episodeList);

		episodeList = new LinkedList<Pattern>();

		episodeList.add(episode5);

		initialEpisodes.put(2, episodeList);

		Map<Integer, List<Pattern>> expected = new HashMap<Integer, List<Pattern>>();
		List<Pattern> episodeListExp = new LinkedList<Pattern>();
		
		episodeListExp.add(episode2);

		expected.put(1, episodeListExp);

		episodeListExp = new LinkedList<Pattern>();
		
		episodeListExp.add(episode5);

		expected.put(2, episodeListExp);

		Map<Integer, List<Pattern>> actuals = sut.getMaximalEpisodes(initialEpisodes);

		assertEquals(expected, actuals);
	}

	@Test
	public void twoNodeEpisodeEx3() throws Exception {
		Map<Integer, List<Pattern>> initialEpisodes = new HashMap<Integer, List<Pattern>>();
		List<Pattern> episodeList = new LinkedList<Pattern>();
		
		episodeList.add(episode1);
		episodeList.add(episode2);
		episodeList.add(episode3);

		initialEpisodes.put(1, episodeList);

		episodeList = new LinkedList<Pattern>();
		
		episodeList.add(episode6);

		initialEpisodes.put(2, episodeList);

		Map<Integer, List<Pattern>> expected = new HashMap<Integer, List<Pattern>>();
		List<Pattern> episodeListExp = new LinkedList<Pattern>();
		
		episodeListExp.add(episode1);

		expected.put(1, episodeListExp);

		episodeListExp = new LinkedList<Pattern>();
		
		episodeListExp.add(episode6);

		expected.put(2, episodeListExp);

		Map<Integer, List<Pattern>> actuals = sut.getMaximalEpisodes(initialEpisodes);

		assertEquals(expected, actuals);
	}
	
	@Test
	public void sameEventsDiffOrder() throws Exception {
		Map<Integer, List<Pattern>> initialEpisodes = new HashMap<Integer, List<Pattern>>();
		List<Pattern> episodeList = new LinkedList<Pattern>();
		
		episodeList.add(episode41);
		episodeList.add(episode42);

		initialEpisodes.put(2, episodeList);

		episodeList = new LinkedList<Pattern>();
		
		episodeList.add(episode7);

		initialEpisodes.put(3, episodeList);

		Map<Integer, List<Pattern>> expected = new HashMap<Integer, List<Pattern>>();
		List<Pattern> episodeListExp = new LinkedList<Pattern>();
		
		episodeListExp.add(episode42);

		expected.put(2, episodeListExp);

		episodeListExp = new LinkedList<Pattern>();
		
		episodeListExp.add(episode7);

		expected.put(3, episodeListExp);

		Map<Integer, List<Pattern>> actuals = sut.getMaximalEpisodes(initialEpisodes);

		assertEquals(expected, actuals);
	}
	
	private Pattern newEpisode(int frequency, int numberOfEvents, String... facts) {
		Pattern episode = new Pattern();
		episode.addStringsOfFacts(facts);
		episode.setFrequency(frequency);
		return episode;
	}
}
