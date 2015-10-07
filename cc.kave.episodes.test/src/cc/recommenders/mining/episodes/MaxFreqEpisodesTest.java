///**
// * Copyright (c) 2010, 2011 Darmstadt University of Technology.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// * 
// * Contributors:
// *     Ervina Cergani - initial API and implementation
// */
//package cc.recommenders.mining.episodes;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.mock;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.TemporaryFolder;
//
//import cc.kave.commons.model.episodes.Episode;
//
//public class MaxFreqEpisodesTest {
//
//	@Rule
//	public TemporaryFolder rootFolder = new TemporaryFolder();
//
//	private Episode episode1;
//	private Episode episode2;
//	private Episode episode3;
//	private Episode episode4;
//	private Episode episode5;
//
//	private EpisodeReader episodeParser;
//	private MaxFreqEpisodes sut;
//
//	@Before
//	public void setup() {
//		episodeParser = mock(EpisodeReader.class);
//		sut = new MaxFreqEpisodes();
//
//		episode1 = new Episode();
//		episode1.addFact("1");
//		episode1.setNumEvents(1);
//		episode1.setFrequency(3);
//
//		episode2 = new Episode();
//		episode2.addFact("2");
//		episode2.setNumEvents(1);
//		episode2.setFrequency(3);
//
//		episode3 = new Episode();
//		episode3.addFact("3");
//		episode3.setNumEvents(1);
//		episode3.setFrequency(3);
//
//		episode4 = new Episode();
//		episode4.addFacts("1", "2", "1>2");
//		episode4.setNumEvents(1);
//		episode4.setFrequency(3);
//
//		episode5 = new Episode();
//		episode5.addFacts("1", "3", "1>3");
//	}
//
//	@Test
//	public void twoNodeEpisode() {
//		Map<Integer, List<Episode>> initialEpisodes = new HashMap<Integer, List<Episode>>();
//		List<Episode> episodeList = new LinkedList<Episode>();
//
//		episodeList.add(episode1);
//		episodeList.add(episode2);
//		episodeList.add(episode3);
//
//		initialEpisodes.put(1, episodeList);
//
//		episodeList = new LinkedList<Episode>();
//
//		episode = new Episode();
//		episode.addFacts("1", "2", "1>2");
//		episode.setNumEvents(1);
//		episode.setFrequency(3);
//
//		episodeList.add(episode);
//
//		initialEpisodes.put(2, episodeList);
//
//		Map<Integer, List<Episode>> expected = new HashMap<Integer, List<Episode>>();
//		List<Episode> episodeListExp = new LinkedList<Episode>();
//
//		Episode episodeExp = new Episode();
//		episodeExp.addFact("1");
//		episodeExp.setNumEvents(1);
//		episodeExp.setFrequency(3);
//
//		episodeListExp.add(episodeExp);
//
//		expected.put(1, episodeListExp);
//
//		episodeListExp = new LinkedList<Episode>();
//
//		episodeExp = new Episode();
//		episodeExp.addFacts("1", "2", "1>2");
//		episodeExp.setNumEvents(1);
//		episodeExp.setFrequency(3);
//
//		episodeListExp.add(episodeExp);
//
//		expected.put(2, episodeListExp);
//
//		Map<Integer, List<Episode>> actuals = sut.getMaxFreqEpisodes(initialEpisodes);
//
//		assertEquals(expected, actuals);
//	}
//
//	@Test
//	public void twoNodeEpisodeEx2() {
//		Map<Integer, List<Episode>> initialEpisodes = new HashMap<Integer, List<Episode>>();
//		List<Episode> episodeList = new LinkedList<Episode>();
//
//		Episode episode = new EpisodeFacts();
//		episode.facts = new LinkedList<String>();
//		episode.facts.add("1");
//
//		episodeList.add(episode);
//
//		episode = new EpisodeFacts();
//		episode.facts = new LinkedList<String>();
//		episode.facts.add("2");
//
//		episodeList.add(episode);
//
//		episode = new EpisodeFacts();
//		episode.facts = new LinkedList<String>();
//		episode.facts.add("3");
//
//		episodeList.add(episode);
//
//		initialEpisodes.put(1, episodeList);
//
//		episodeList = new LinkedList<EpisodeFacts>();
//
//		episode = new EpisodeFacts();
//		episode.facts = new LinkedList<String>();
//		episode.facts.add("1");
//		episode.facts.add("3");
//		episode.facts.add("1>3");
//
//		episodeList.add(episode);
//
//		initialEpisodes.put(2, episodeList);
//
//		Map<Integer, List<EpisodeFacts>> expected = new HashMap<Integer, List<EpisodeFacts>>();
//		List<EpisodeFacts> episodeListExp = new LinkedList<EpisodeFacts>();
//
//		EpisodeFacts episodeExp = new EpisodeFacts();
//		episodeExp.facts = new LinkedList<String>();
//		episodeExp.facts.add("2");
//
//		episodeListExp.add(episodeExp);
//
//		expected.put(1, episodeListExp);
//
//		episodeListExp = new LinkedList<EpisodeFacts>();
//
//		episodeExp = new EpisodeFacts();
//		episodeExp.facts = new LinkedList<String>();
//		episodeExp.facts.add("1");
//		episodeExp.facts.add("3");
//		episodeExp.facts.add("1>3");
//
//		episodeListExp.add(episodeExp);
//
//		expected.put(2, episodeListExp);
//
//		Map<Integer, List<EpisodeFacts>> actuals = sut.getMaxFreqEpisodes(initialEpisodes);
//
//		assertEquals(expected, actuals);
//	}
//
//	@Test
//	public void twoNodeEpisodeEx3() {
//		Map<Integer, List<EpisodeFacts>> initialEpisodes = new HashMap<Integer, List<EpisodeFacts>>();
//		List<EpisodeFacts> episodeList = new LinkedList<EpisodeFacts>();
//
//		EpisodeFacts episode = new EpisodeFacts();
//		episode.facts = new LinkedList<String>();
//		episode.facts.add("1");
//
//		episodeList.add(episode);
//
//		episode = new EpisodeFacts();
//		episode.facts = new LinkedList<String>();
//		episode.facts.add("2");
//
//		episodeList.add(episode);
//
//		episode = new EpisodeFacts();
//		episode.facts = new LinkedList<String>();
//		episode.facts.add("3");
//
//		episodeList.add(episode);
//
//		initialEpisodes.put(1, episodeList);
//
//		episodeList = new LinkedList<EpisodeFacts>();
//
//		episode = new EpisodeFacts();
//		episode.facts = new LinkedList<String>();
//		episode.facts.add("2");
//		episode.facts.add("3");
//		episode.facts.add("2>3");
//
//		episodeList.add(episode);
//
//		initialEpisodes.put(2, episodeList);
//
//		Map<Integer, List<EpisodeFacts>> expected = new HashMap<Integer, List<EpisodeFacts>>();
//		List<EpisodeFacts> episodeListExp = new LinkedList<EpisodeFacts>();
//
//		EpisodeFacts episodeExp = new EpisodeFacts();
//		episodeExp.facts = new LinkedList<String>();
//		episodeExp.facts.add("1");
//
//		episodeListExp.add(episodeExp);
//
//		expected.put(1, episodeListExp);
//
//		episodeListExp = new LinkedList<EpisodeFacts>();
//
//		episodeExp = new EpisodeFacts();
//		episodeExp.facts = new LinkedList<String>();
//		episodeExp.facts.add("2");
//		episodeExp.facts.add("3");
//		episodeExp.facts.add("2>3");
//
//		episodeListExp.add(episodeExp);
//
//		expected.put(2, episodeListExp);
//
//		Map<Integer, List<EpisodeFacts>> actuals = sut.getMaxFreqEpisodes(initialEpisodes);
//
//		assertEquals(expected, actuals);
//	}
//
//	private File getFilePath() {
//		File fileName = new File(rootFolder.getRoot().getAbsolutePath() + "/output.txt");
//		return fileName;
//	}
//}
