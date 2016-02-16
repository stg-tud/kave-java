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
package cc.kave.episodes.mining.reader;

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

import cc.kave.episodes.model.Episode;
import cc.recommenders.exceptions.AssertionException;

public class EpisodeParserTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private static final int FREQ = 2;
	private static final double BD = 0.1;

	private FileReader reader;
	private EpisodeParser sut;

	@Before
	public void setup() {
		reader = mock(FileReader.class);
		sut = new EpisodeParser(rootFolder.getRoot(), reader);
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Frequent episode folder does not exist");
		sut = new EpisodeParser(new File("does not exist"), reader);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Frequent episode folder is not a folder, but a file");
		sut = new EpisodeParser(file, reader);
	}

	@Test
	public void oneNodeEpisodes() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("1-NOde Episodes = 6\n");
		sb.append("1 .	: 3	: 1	:. \n");

		String content = sb.toString();

		File file = getFilePath();

		try {
			FileUtils.writeStringToFile(file, content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Map<Integer, List<Episode>> expected = new HashMap<Integer, List<Episode>>();
		List<Episode> episodeList = new LinkedList<Episode>();

		Episode episode = createEpisode(3, 1, "1");
		episodeList.add(episode);

		expected.put(1, episodeList);

		doCallRealMethod().when(reader).readFile(eq(file));

		Map<Integer, List<Episode>> actual = sut.parse(FREQ, BD);

		verify(reader).readFile(file);

		assertEquals(expected, actual);
	}

	@Test
	public void emptyNodeEpisodesTest() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("1-NOde Episodes = 6\n");
		sb.append("1 .	: 3	: 1	:. \n");
		sb.append("2-NOde Episodes = 0\n");
		sb.append("3-NOde Episodes = 0\n");
		sb.append("4-NOde Episodes = 0\n");

		String content = sb.toString();

		File file = getFilePath();

		try {
			FileUtils.writeStringToFile(file, content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Map<Integer, List<Episode>> expected = new HashMap<Integer, List<Episode>>();
		List<Episode> episodeList = new LinkedList<Episode>();

		Episode episode = createEpisode(3, 1, "1");
		episodeList.add(episode);

		expected.put(1, episodeList);

		doCallRealMethod().when(reader).readFile(eq(file));

		Map<Integer, List<Episode>> actual = sut.parse(FREQ, BD);

		verify(reader).readFile(file);

		assertEquals(expected, actual);
	}

	@Test
	public void twoNodeEpisodes() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("1-NOde Episodes = 6\n");
		sb.append("1 .	: 3	: 1	:. \n");
		sb.append("2 .	: 2	: 1	:. \n");
		sb.append("2-NOde Episodes = 1\n");
		sb.append("1 2 .	: 3	: 1	:. 1>2,\n");
		sb.append("3-NOde Episodes = 0\n");

		String content = sb.toString();

		File file = getFilePath();

		try {
			FileUtils.writeStringToFile(file, content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Map<Integer, List<Episode>> expected = new HashMap<Integer, List<Episode>>();
		List<Episode> episodeList = new LinkedList<Episode>();

		Episode episode = createEpisode(3, 1, "1");
		episodeList.add(episode);

		episode = createEpisode(2, 1, "2");
		episodeList.add(episode);

		expected.put(1, episodeList);

		episodeList = new LinkedList<Episode>();

		episode = createEpisode(3, 2, "1", "2", "1>2");
		episodeList.add(episode);

		expected.put(2, episodeList);

		doCallRealMethod().when(reader).readFile(eq(file));

		Map<Integer, List<Episode>> actual = sut.parse(FREQ, BD);

		verify(reader).readFile(file);

		assertEquals(expected, actual);
	}

	private Episode createEpisode(int freq, int numberOfExistenceFacts, String...strings) {
		Episode episode = new Episode();
		episode.setFrequency(freq);
		for (String fact : strings) {
			episode.addFact(fact);
		}
		return episode;
	}

	private File getFilePath() {
		File fileName = new File(rootFolder.getRoot().getAbsolutePath() + "/outputF2B0.1.txt");
		return fileName;
	}
}
