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

import cc.kave.commons.mining.episodes.EpisodeParser;
import cc.kave.commons.model.episodes.Episode;
import cc.recommenders.exceptions.AssertionException;

public class EpisodeParserTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private EpisodeParser sut;

	@Before
	public void setup() {
		sut = new EpisodeParser(rootFolder.getRoot());
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Frequent episode folder does not exist");
		sut = new EpisodeParser(new File("does not exist"));
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Frequent episode folder is not a folder, but a file");
		sut = new EpisodeParser(file);
	}

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

		Map<Integer, List<Episode>> expected = new HashMap<Integer, List<Episode>>();
		List<Episode> episodeList = new LinkedList<Episode>();
		
		Episode episode = new Episode();
		episode.addFact("1");
		episode.setNumEvents(1);
		episode.setFrequency(3);

		episodeList.add(episode);

		expected.put(1, episodeList);

		Map<Integer, List<Episode>> actual = sut.parse();

		assertEquals(expected, actual);
	}

	@Test
	public void emptyNNodeEpisodesTest() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("1-NOde Episodes = 6\n");
		sb.append("1 .	: 3	: 1	:. \n");
		sb.append("2-NOde Episodes = 0\n");
		sb.append("3-NOde Episodes = 0\n");
		sb.append("4-NOde Episodes = 0\n");

		String content = sb.toString();

		File file = getFilePath();

		try {
			FileUtils.writeStringToFile(file, content, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Map<Integer, List<Episode>> expected = new HashMap<Integer, List<Episode>>();
		List<Episode> episodeList = new LinkedList<Episode>();
		
		Episode episode = new Episode();
		episode.addFact("1");
		episode.setNumEvents(1);
		episode.setFrequency(3);

		episodeList.add(episode);

		expected.put(1, episodeList);

		Map<Integer, List<Episode>> actual = sut.parse();

		assertEquals(expected, actual);
	}

	@Test
	public void twoNodeEpisodes() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("1-NOde Episodes = 6\n");
		sb.append("1 .	: 3	: 1	:. \n");
		sb.append("2 .	: 3	: 1	:. \n");
		sb.append("2-NOde Episodes = 1\n");
		sb.append("1 2 .	: 3	: 1	:. 1>2,\n");
		sb.append("3-NOde Episodes = 0\n");

		String content = sb.toString();

		File file = getFilePath();

		try {
			FileUtils.writeStringToFile(file, content, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Map<Integer, List<Episode>> expected = new HashMap<Integer, List<Episode>>();
		List<Episode> episodeList = new LinkedList<Episode>();

		Episode episode = new Episode();
		episode.addFact("1");
		episode.setNumEvents(1);
		episode.setFrequency(3);

		episodeList.add(episode);

		episode = new Episode();
		episode.addFact("2");
		episode.setNumEvents(1);
		episode.setFrequency(3);

		episodeList.add(episode);

		expected.put(1, episodeList);

		episodeList = new LinkedList<Episode>();

		episode = new Episode();
		episode.addFacts("1", "2", "1>2");
		episode.setNumEvents(2);
		episode.setFrequency(3);

		episodeList.add(episode);

		expected.put(2, episodeList);

		Map<Integer, List<Episode>> actual = sut.parse();

		assertEquals(expected, actual);
	}

	private File getFilePath() {
		File fileName = new File(rootFolder.getRoot().getAbsolutePath() + "/output.txt");
		return fileName;
	}
}
