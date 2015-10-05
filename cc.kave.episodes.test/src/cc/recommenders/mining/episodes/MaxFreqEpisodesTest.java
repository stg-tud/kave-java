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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import cc.recommenders.exceptions.AssertionException;

public class MaxFreqEpisodesTest {
	
	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();

	private EpisodeReader episodeParser;
	private MaxFreqEpisodes sut;
	
	@Before
	public void setup() {
		episodeParser = mock(EpisodeReader.class);
		sut = new MaxFreqEpisodes();
	}
	

	
	@Test
	public void oneNodeEpisode() {
		Map<Integer, List<EpisodeFacts>> initialEpisodes = new HashMap<Integer,List<EpisodeFacts>>();
		
		 EpisodeFacts episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("1");
		 
		 List<EpisodeFacts> episodeList = new LinkedList<EpisodeFacts>();
		 episodeList.add(episode);
		 
		 initialEpisodes.put(1, episodeList);
		 
		 Map<Integer, List<EpisodeFacts>> expected = new HashMap<Integer,List<EpisodeFacts>>();
			
		 EpisodeFacts episodeExp = new EpisodeFacts();
		 episodeExp.facts = new LinkedList<String>();
		 episodeExp.facts.add("1");
		 
		 List<EpisodeFacts> episodeListExp = new LinkedList<EpisodeFacts>();
		 episodeListExp.add(episodeExp);
		 
		 expected.put(1, episodeListExp);
		 
		 sut.getMaxFreqEpisodes(initialEpisodes);
		 
		 assertEquals(expected, initialEpisodes);
	} 
	
	@Test
	public void twoNodeEpisode() {
		Map<Integer, List<EpisodeFacts>> initialEpisodes = new HashMap<Integer,List<EpisodeFacts>>();
		 List<EpisodeFacts> episodeList = new LinkedList<EpisodeFacts>();
		
		 EpisodeFacts episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("1");
		 	 
		 episodeList.add(episode);
		 
		 episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("2");
		 
		 episodeList.add(episode);
		 
		 episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("3");
		 
		 episodeList.add(episode);
		 
		 initialEpisodes.put(1, episodeList);
		 
		 episodeList = new LinkedList<EpisodeFacts>();
		 
		 episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("1");
		 episode.facts.add("2");
		 episode.facts.add("1>2");
		 	 
		 episodeList.add(episode);
		 
		 initialEpisodes.put(2, episodeList);	 
		 
		 Map<Integer, List<EpisodeFacts>> expected = new HashMap<Integer,List<EpisodeFacts>>();
		 List<EpisodeFacts> episodeListExp = new LinkedList<EpisodeFacts>();
			
		 EpisodeFacts episodeExp = new EpisodeFacts();
		 episodeExp.facts = new LinkedList<String>();
		 episodeExp.facts.add("3"); 
		
		 episodeListExp.add(episodeExp);
		 
		 expected.put(1, episodeListExp);
		 
		 episodeListExp = new LinkedList<EpisodeFacts>();
		 
		 episodeExp = new EpisodeFacts();
		 episodeExp.facts = new LinkedList<String>();
		 episodeExp.facts.add("1"); 
		 episodeExp.facts.add("2"); 
		 episodeExp.facts.add("1>2"); 
		
		 episodeListExp.add(episodeExp);
		 
		 expected.put(2, episodeListExp);
		 
		 Map<Integer, List<EpisodeFacts>> actuals = sut.getMaxFreqEpisodes(initialEpisodes);
		 
		 assertEquals(expected, actuals);
	}
	
	@Test
	public void twoNodeEpisodeEx2() {
		Map<Integer, List<EpisodeFacts>> initialEpisodes = new HashMap<Integer,List<EpisodeFacts>>();
		 List<EpisodeFacts> episodeList = new LinkedList<EpisodeFacts>();
		
		 EpisodeFacts episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("1");
		 	 
		 episodeList.add(episode);
		 
		 episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("2");
		 
		 episodeList.add(episode);
		 
		 episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("3");
		 
		 episodeList.add(episode);
		 
		 initialEpisodes.put(1, episodeList);
		 
		 episodeList = new LinkedList<EpisodeFacts>();
		 
		 episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("1");
		 episode.facts.add("3");
		 episode.facts.add("1>3");
		 	 
		 episodeList.add(episode);
		 
		 initialEpisodes.put(2, episodeList);	 
		 
		 Map<Integer, List<EpisodeFacts>> expected = new HashMap<Integer,List<EpisodeFacts>>();
		 List<EpisodeFacts> episodeListExp = new LinkedList<EpisodeFacts>();
			
		 EpisodeFacts episodeExp = new EpisodeFacts();
		 episodeExp.facts = new LinkedList<String>();
		 episodeExp.facts.add("2"); 
		
		 episodeListExp.add(episodeExp);
		 
		 expected.put(1, episodeListExp);
		 
		 episodeListExp = new LinkedList<EpisodeFacts>();
		 
		 episodeExp = new EpisodeFacts();
		 episodeExp.facts = new LinkedList<String>();
		 episodeExp.facts.add("1"); 
		 episodeExp.facts.add("3"); 
		 episodeExp.facts.add("1>3"); 
		
		 episodeListExp.add(episodeExp);
		 
		 expected.put(2, episodeListExp);
		 
		 Map<Integer, List<EpisodeFacts>> actuals = sut.getMaxFreqEpisodes(initialEpisodes);
		 
		 assertEquals(expected, actuals);
	}
	
	@Test
	public void twoNodeEpisodeEx3() {
		Map<Integer, List<EpisodeFacts>> initialEpisodes = new HashMap<Integer,List<EpisodeFacts>>();
		 List<EpisodeFacts> episodeList = new LinkedList<EpisodeFacts>();
		
		 EpisodeFacts episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("1");
		 	 
		 episodeList.add(episode);
		 
		 episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("2");
		 
		 episodeList.add(episode);
		 
		 episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("3");
		 
		 episodeList.add(episode);
		 
		 initialEpisodes.put(1, episodeList);
		 
		 episodeList = new LinkedList<EpisodeFacts>();
		 
		 episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("2");
		 episode.facts.add("3");
		 episode.facts.add("2>3");
		 	 
		 episodeList.add(episode);
		 
		 initialEpisodes.put(2, episodeList);	 
		 
		 Map<Integer, List<EpisodeFacts>> expected = new HashMap<Integer,List<EpisodeFacts>>();
		 List<EpisodeFacts> episodeListExp = new LinkedList<EpisodeFacts>();
			
		 EpisodeFacts episodeExp = new EpisodeFacts();
		 episodeExp.facts = new LinkedList<String>();
		 episodeExp.facts.add("1"); 
		
		 episodeListExp.add(episodeExp);
		 
		 expected.put(1, episodeListExp);
		 
		 episodeListExp = new LinkedList<EpisodeFacts>();
		 
		 episodeExp = new EpisodeFacts();
		 episodeExp.facts = new LinkedList<String>();
		 episodeExp.facts.add("2"); 
		 episodeExp.facts.add("3"); 
		 episodeExp.facts.add("2>3"); 
		
		 episodeListExp.add(episodeExp);
		 
		 expected.put(2, episodeListExp);
		 
		 Map<Integer, List<EpisodeFacts>> actuals = sut.getMaxFreqEpisodes(initialEpisodes);
		 
		 assertEquals(expected, actuals);
	}
	
	private File getFilePath() {
		File fileName = new File(rootFolder.getRoot().getAbsolutePath() + "/output.txt");
		return fileName;
	}
}
