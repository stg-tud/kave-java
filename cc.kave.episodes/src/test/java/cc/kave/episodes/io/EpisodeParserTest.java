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
package cc.kave.episodes.io;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.recommenders.exceptions.AssertionException;

import com.google.common.collect.Sets;

public class EpisodeParserTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private static final int FREQUENCY = 100;

	private FileReader reader;
	private Map<Integer, Set<Episode>> expected;
	private Set<Episode> episodes;
	private EpisodeParser sut;

	@Before
	public void setup() {
		reader = mock(FileReader.class);

		expected = new HashMap<Integer, Set<Episode>>();
		episodes = Sets.newHashSet();

		sut = new EpisodeParser(rootFolder.getRoot(), reader);
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Events folder does not exist");
		sut = new EpisodeParser(new File("does not exist"), reader);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Events is not a folder, but a file");
		sut = new EpisodeParser(file, reader);
	}

	@Test
	public void oneNodeEpisodes() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("1-NOde Episodes = 6\n");
		sb.append("1 .	: 3	: 1	:. \n");

		String content = sb.toString();

		File file = getFilePath(EpisodeType.GENERAL);

		try {
			FileUtils.writeStringToFile(file, content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Episode episode = createEpisode(3, 1.0, "1");
		episodes.add(episode);

		expected.put(1, episodes);

		doCallRealMethod().when(reader).readFile(eq(file));

		Map<Integer, Set<Episode>> actual = sut.parser(FREQUENCY);

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

		File file = getFilePath(EpisodeType.SEQUENTIAL);

		try {
			FileUtils.writeStringToFile(file, content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Episode episode = createEpisode(3, 1, "1");
		episodes.add(episode);

		expected.put(1, episodes);

		doCallRealMethod().when(reader).readFile(eq(file));

		Map<Integer, Set<Episode>> actual = sut.parser(FREQUENCY);

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

		File file = getFilePath(EpisodeType.PARALLEL);

		try {
			FileUtils.writeStringToFile(file, content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Episode episode = createEpisode(3, 1, "1");
		episodes.add(episode);

		episode = createEpisode(2, 1, "2");
		episodes.add(episode);

		expected.put(1, episodes);

		episodes = Sets.newHashSet();

		episode = createEpisode(3, 1, "1", "2", "1>2");
		episodes.add(episode);

		expected.put(2, episodes);

		doCallRealMethod().when(reader).readFile(eq(file));

		Map<Integer, Set<Episode>> actual = sut.parser(FREQUENCY);

		verify(reader).readFile(file);

		assertEquals(expected, actual);
	}

	private Episode createEpisode(int freq, double bdmeas, String... strings) {
		Episode episode = new Episode();
		episode.setFrequency(freq);
		episode.setEntropy(bdmeas);
		for (String fact : strings) {
			episode.addFact(fact);
		}
		return episode;
	}

	private File getFilePath(EpisodeType type) {
		File fileName = new File(rootFolder.getRoot().getAbsolutePath()
				+ "/freq" + FREQUENCY + "/episodes.txt");
		return fileName;
	}
}
