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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cc.kave.commons.model.episodes.Episode;
import cc.recommenders.exceptions.AssertionException;

public class EpisodeReaderTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private EpisodeReader sut;
	@Mock
	public Parser parser;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		sut = new EpisodeReader(rootFolder.getRoot(), parser);
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Frequent episode folder does not exist");
		sut = new EpisodeReader(new File("does not exist"), parser);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Frequent episode folder is not a folder, but a file");
		sut = new EpisodeReader(file, parser);
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

		Episode episode = new Episode();
		episode.addFact("1");
		episode.setNumEvents(1);
		;
		episode.setFrequency(2);

		List<Episode> episodeList = new LinkedList<Episode>();
		episodeList.add(episode);

		expected.put(1, episodeList);

		doCallRealMethod().when(parser).parse(eq(file));

		Map<Integer, List<Episode>> actual = sut.read();

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

		Map<Integer, List<Episode>> expected = new HashMap<Integer, List<Episode>>();

		Episode episode = new Episode();
		episode.addFact("1");
		episode.setNumEvents(1);
		;
		episode.setFrequency(3);

		List<Episode> episodeList = new LinkedList<Episode>();
		episodeList.add(episode);

		expected.put(1, episodeList);

		doCallRealMethod().when(parser).parse(eq(file));

		Map<Integer, List<Episode>> actual = sut.read();

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

		Map<Integer, List<Episode>> expected = new HashMap<Integer, List<Episode>>();
		List<Episode> episodeList = new LinkedList<Episode>();

		Episode episode = new Episode();
		episode.addFact("1");
		episode.setNumEvents(1);
		;
		episode.setFrequency(3);

		episodeList.add(episode);

		episode = new Episode();
		episode.addFact("2");
		episode.setNumEvents(1);
		;
		episode.setFrequency(3);

		episodeList.add(episode);

		expected.put(1, episodeList);

		episodeList = new LinkedList<Episode>();

		episode = new Episode();
		episode.addFacts("1", "2", "1>2");
		episode.setNumEvents(2);
		;
		episode.setFrequency(3);

		episodeList.add(episode);

		expected.put(2, episodeList);

		doCallRealMethod().when(parser).parse(eq(file));

		Map<Integer, List<Episode>> actual = sut.read();

		verify(parser).parse(eq(file));

		assertEquals(expected, actual);

		file.delete();
	}

	private File getFilePath() {
		File fileName = new File(rootFolder.getRoot().getAbsolutePath() + "/output.txt");
		return fileName;
	}
}
