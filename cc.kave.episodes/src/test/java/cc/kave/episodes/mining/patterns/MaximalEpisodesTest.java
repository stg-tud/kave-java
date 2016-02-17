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
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Sets;

import cc.kave.episodes.model.Episode;
import cc.recommenders.exceptions.AssertionException;

public class MaximalEpisodesTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Episode episode1;
	private Episode episode2;
	private Episode episode3;
	private Episode episode41;
	private Episode episode42;
	private Episode episode5;
	private Episode episode6;
	private Episode episode7;

	private Map<Integer, Set<Episode>> episodes;
	private Map<Integer, Set<Episode>> initialEpisodes;
	private Set<Episode> episodeSet;
	
	private Map<Integer, Set<Episode>> expecteds;
	private Map<Integer, Set<Episode>> actuals;
	
	private MaximalEpisodes sut;

	@Before
	public void setup() {
		episode1 = createEpisode(3, 1, "1");
		episode2 = createEpisode(3, 1, "2");
		episode3 = createEpisode(3, 1, "3");
		episode41 = createEpisode(3, 2, "1", "2", "1>2");
		episode42 = createEpisode(3, 2, "1", "2", "2>1");
		episode5 = createEpisode(4, 2, "1", "3", "1>3");
		episode6 = createEpisode(2, 2, "2", "3", "2>3");
		episode7 = createEpisode(3, 3, "1", "2", "3", "1>2", "1>3");
		
		episodes = new HashMap<Integer, Set<Episode>>();
		initialEpisodes = new HashMap<Integer, Set<Episode>>();
		episodeSet = Sets.newHashSet();
		
		expecteds = new HashMap<Integer, Set<Episode>>();
		
		sut = new MaximalEpisodes();
	}
	
	@Test
	public void cannotGetEmptyListOfEpisodes() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("The list of learned episodes is empty!");
		
		sut.getMaximalEpisodes(episodes);
	}
	
	@Test
	public void oneLevelNodeEpisodes() {
		
		episodeSet.add(episode41);
		episodeSet.add(episode5);
		episodeSet.add(episode6);
		
		initialEpisodes.put(2, episodeSet);
		
		episodeSet = Sets.newHashSet();
		
		episodeSet.add(episode41);
		episodeSet.add(episode5);
		episodeSet.add(episode6);
		
		expecteds.put(2, episodeSet);
		
		actuals = sut.getMaximalEpisodes(initialEpisodes);
		
		assertEquals(expecteds, actuals);
	}
	
	@Test
	public void reduceOneNodeLevelEpisode() {
		
		episodeSet.add(episode1);
		episodeSet.add(episode2);
		episodeSet.add(episode3);
		
		initialEpisodes.put(1, episodeSet);
		
		episodeSet = Sets.newHashSet();
		
		episodeSet.add(episode41);
		episodeSet.add(episode5);
		
		initialEpisodes.put(2, episodeSet);
		
		episodeSet = Sets.newHashSet();
		
		episodeSet.add(episode41);
		episodeSet.add(episode5);
		
		expecteds.put(2, episodeSet);
		
		actuals = sut.getMaximalEpisodes(initialEpisodes);
		
		assertEquals(expecteds, actuals);
	}

	@Test
	public void twoNodeEpisode() throws Exception {

		episodeSet.add(episode1);
		episodeSet.add(episode2);
		episodeSet.add(episode3);

		initialEpisodes.put(1, episodeSet);

		episodeSet = Sets.newHashSet();
		
		episodeSet.add(episode41);

		initialEpisodes.put(2, episodeSet);

		Set<Episode> episodeListExp = Sets.newHashSet();

		episodeListExp.add(episode3);

		expecteds.put(1, episodeListExp);

		episodeListExp = Sets.newHashSet();
		
		episodeListExp.add(episode41);

		expecteds.put(2, episodeListExp);

		actuals = sut.getMaximalEpisodes(initialEpisodes);

		assertEquals(expecteds, actuals);
	}

	@Test
	public void twoNodeEpisodeEx2() throws Exception {
		
		episodeSet.add(episode1);
		episodeSet.add(episode2);
		episodeSet.add(episode3);

		initialEpisodes.put(1, episodeSet);

		episodeSet = Sets.newHashSet();

		episodeSet.add(episode5);

		initialEpisodes.put(2, episodeSet);

		Set<Episode> episodeListExp = Sets.newHashSet();
		
		episodeListExp.add(episode2);

		expecteds.put(1, episodeListExp);

		episodeListExp = Sets.newHashSet();
		
		episodeListExp.add(episode5);

		expecteds.put(2, episodeListExp);

		actuals = sut.getMaximalEpisodes(initialEpisodes);

		assertEquals(expecteds, actuals);
	}

	@Test
	public void twoNodeEpisodeEx3() throws Exception {
		
		episodeSet.add(episode1);
		episodeSet.add(episode2);
		episodeSet.add(episode3);

		initialEpisodes.put(1, episodeSet);

		episodeSet = Sets.newHashSet();
		
		episodeSet.add(episode6);

		initialEpisodes.put(2, episodeSet);

		Set<Episode> episodeListExp = Sets.newHashSet();
		
		episodeListExp.add(episode1);

		expecteds.put(1, episodeListExp);

		episodeListExp = Sets.newHashSet();
		
		episodeListExp.add(episode6);

		expecteds.put(2, episodeListExp);

		actuals = sut.getMaximalEpisodes(initialEpisodes);

		assertEquals(expecteds, actuals);
	}
	
	@Test
	public void sameEventsDiffOrder() throws Exception {
		
		episodeSet.add(episode41);
		episodeSet.add(episode42);

		initialEpisodes.put(2, episodeSet);

		episodeSet = Sets.newHashSet();
		
		episodeSet.add(episode7);

		initialEpisodes.put(3, episodeSet);

		Set<Episode> episodeListExp = Sets.newHashSet();
		
		episodeListExp.add(episode42);

		expecteds.put(2, episodeListExp);

		episodeListExp = Sets.newHashSet();
		
		episodeListExp.add(episode7);

		expecteds.put(3, episodeListExp);

		actuals = sut.getMaximalEpisodes(initialEpisodes);

		assertEquals(expecteds, actuals);
	}
	
	private Episode createEpisode(int frequency, int numberOfEvents, String... facts) {
		Episode episode = new Episode();
		episode.addStringsOfFacts(facts);
		episode.setFrequency(frequency);
		return episode;
	}
}
