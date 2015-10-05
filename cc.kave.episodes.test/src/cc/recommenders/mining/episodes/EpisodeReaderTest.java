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

import static org.junit.Assert.*;
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

public class EpisodeReaderTest {
	
	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private EpisodeReader sut;
	private Parser parser;
	
	@Before
	public void setup() {
		parser = mock(Parser.class);
		sut = new EpisodeReader(rootFolder.getRoot(), parser);
	}
	
	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Home folder does not exist");
		sut = new EpisodeReader(new File("does not exist"), parser);
	}
	
	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Home is not a folder, but a file");
		sut = new EpisodeReader(file, parser);
	}
	
//	@Test
//	public void parserTest() throws IOException {
//		
//		StringBuilder sb = new StringBuilder();
//		sb.append("1-NOde Episodes = 6\n");
//		sb.append("1 .	: 3	: 1	:. \n");
//		sb.append("2-NOde Episodes = 0");
//		
//		String content = sb.toString();
//		
//		File file = getFilePath();
//		
//		try {
//			FileUtils.writeStringToFile(file, content, true);
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//		 
//		 doCallRealMethod().when(parser).parse(eq(file));
//
//		 sut.read();
//		 
//		 verify(parser).parse(eq(file));
//		 
//		 file.delete();
//	}
	
	@Test
	public void oneNodeEpisodes() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("1-NOde Episodes = 6\n");
		sb.append("1 .	: 3	: 1	:. \n");
		
		String content = sb.toString();
		
		File file = getFilePath();
		
		try {
			FileUtils.writeStringToFile(file, content, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
				
		 Map<Integer, List<EpisodeFacts>> expected = new HashMap<Integer,List<EpisodeFacts>>();
		
		 EpisodeFacts episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("1");
		 episode.numEvents = 1;
		 
		 List<EpisodeFacts> episodeList = new LinkedList<EpisodeFacts>();
		 episodeList.add(episode);
		 
		 expected.put(1, episodeList);
		 
		 doCallRealMethod().when(parser).parse(eq(file));
		 
		 Map<Integer, List<EpisodeFacts>> actual = sut.read();
		 
		 verify(parser).parse(eq(file));
		 
		 assertEquals(expected, actual);
		 
		 file.delete();
	}
	
	@Test
	public void moreEventsTest() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("1-NOde Episodes = 6\n");
		sb.append("1 .	: 3	: 1	:. \n");
		sb.append("2-NOde Episodes = 12\n");
		
		String content = sb.toString();
		
		File file = getFilePath();
		
		try {
			FileUtils.writeStringToFile(file, content, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
				
		 Map<Integer, List<EpisodeFacts>> expected = new HashMap<Integer,List<EpisodeFacts>>();
		
		 EpisodeFacts episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("1");
		 episode.numEvents = 1;
		 
		 List<EpisodeFacts> episodeList = new LinkedList<EpisodeFacts>();
		 episodeList.add(episode);
		 
		 expected.put(1, episodeList);
		 
		 doCallRealMethod().when(parser).parse(eq(file));
		 
		 Map<Integer, List<EpisodeFacts>> actual = sut.read();
		 
		 verify(parser).parse(eq(file));
		 
		 assertEquals(expected, actual);
		 
		 file.delete();
	}
	
	@Test
	public void twoNodeEpisodes() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("1-NOde Episodes = 6\n");
		sb.append("1 .	: 3	: 1	:. \n");
		sb.append("2 .	: 3	: 1	:. \n");
		sb.append("2-NOde Episodes = 1\n");
		sb.append("1 2 .	: 3	: 1	:. 1>2,\n");
				
		String content = sb.toString();
		
		File file = getFilePath();
		
		try {
			FileUtils.writeStringToFile(file, content, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
				
		 Map<Integer, List<EpisodeFacts>> expected = new HashMap<Integer,List<EpisodeFacts>>();
		 List<EpisodeFacts> episodeList = new LinkedList<EpisodeFacts>();
		
		 EpisodeFacts episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("1");
		 episode.numEvents = 1;
		 	 
		 episodeList.add(episode);
		 
		 episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("2");
		 episode.numEvents = 1;
		 
		 episodeList.add(episode);
		 
		 expected.put(1, episodeList);
		 
		 episodeList = new LinkedList<EpisodeFacts>();
		 
		 episode = new EpisodeFacts();
		 episode.facts = new LinkedList<String>();
		 episode.facts.add("1");
		 episode.facts.add("2");
		 episode.facts.add("1>2");
		 episode.numEvents = 2;
		 	 
		 episodeList.add(episode);
		 
		 expected.put(2, episodeList);
		 
		 doCallRealMethod().when(parser).parse(eq(file));
		 
		 Map<Integer, List<EpisodeFacts>> actual = sut.read();
		 
		 verify(parser).parse(eq(file));
		 		 
		 assertEquals(expected, actual);
		 
		 file.delete();
	}
	
	private File getFilePath() {
		File fileName = new File(rootFolder.getRoot().getAbsolutePath() + "/output.txt");
		return fileName;
	}
}
