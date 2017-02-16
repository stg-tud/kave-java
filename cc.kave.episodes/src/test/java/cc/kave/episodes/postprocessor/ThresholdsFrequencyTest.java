/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.episodes.postprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.postprocessor.ThresholdsFrequency;
import cc.kave.episodes.statistics.EpisodesStatistics;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ThresholdsFrequencyTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private EpisodesParser parser;

	private EpisodesStatistics stats;
	
	private static final int FREQUENCY = 2;
	
	private ThresholdsFrequency sut;
	
	@Before
	public void setup() throws IOException {
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);
		
		Map<Integer, Set<Episode>> episodes = Maps.newLinkedHashMap();
		Set<Episode> currEpLevel = Sets.newLinkedHashSet();
		
		currEpLevel.add(createEpisode(3, 0.7, "1"));
		currEpLevel.add(createEpisode(2, 0.5, "2"));
		episodes.put(1, currEpLevel);
		
		currEpLevel = Sets.newLinkedHashSet();
		currEpLevel.add(createEpisode(3, 0.6, "1", "2", "1>2"));
		currEpLevel.add(createEpisode(2, 0.6, "1", "3", "1>3"));
		currEpLevel.add(createEpisode(3, 0.8, "2", "3", "2>3"));
		episodes.put(2, currEpLevel);
		
		currEpLevel = Sets.newLinkedHashSet();
		currEpLevel.add(createEpisode(2, 0.9, "1", "2", "3", "1>2", "1>3"));
		currEpLevel.add(createEpisode(3, 1.0, "1", "3", "4", "1>3", "1>4"));
		episodes.put(3, currEpLevel);
		
		stats = new EpisodesStatistics();
		
		sut = new ThresholdsFrequency(rootFolder.getRoot(), parser, stats);
		
		when(parser.parse(anyInt(), any(EpisodeType.class))).thenReturn(episodes);
	}
	
	@After
	public void teardown() {
		Logger.reset();
	}
	
	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Patterns folder does not exist");
		sut = new ThresholdsFrequency(new File("does not exist"), parser, stats);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Patterns is not a folder, but a file");
		sut = new ThresholdsFrequency(file, parser, stats);
	}

	@Test
	public void mockIsCalled() throws ZipException, IOException {
		sut.writer(FREQUENCY, EpisodeType.GENERAL);

		verify(parser).parse(anyInt(), any(EpisodeType.class));
	}
	
	@Test
	public void filesAreCreated() throws IOException {
		sut.writer(FREQUENCY, EpisodeType.GENERAL);

		verify(parser).parse(anyInt(), any(EpisodeType.class));

		File freqsFile = new File(getFreqsPath());

		assertTrue(freqsFile.exists());
	}
	
	@Test
	public void contentTest() throws IOException {
		sut.writer(FREQUENCY, EpisodeType.GENERAL);

		verify(parser).parse(anyInt(), any(EpisodeType.class));

		File freqsFile = new File(getFreqsPath());
		
		StringBuilder expFreqs = new StringBuilder();
		expFreqs.append("Frequency distribution for 2-node episodes:\n");
		expFreqs.append("Frequency\tCounter\n");
		expFreqs.append("3\t2\n");
		expFreqs.append("2\t3\n\n");
		
		expFreqs.append("Frequency distribution for 3-node episodes:\n");
		expFreqs.append("Frequency\tCounter\n");
		expFreqs.append("2\t2\n");
		expFreqs.append("3\t1\n\n");
		
		String actualFreqs = FileUtils.readFileToString(freqsFile);
		
		assertEquals(expFreqs.toString(), actualFreqs);
	}
	
	private String getFreqsPath() {
		File streamFile = new File(rootFolder.getRoot().getAbsolutePath() + "/freqs" + FREQUENCY + "Repos.txt");
		return streamFile.getAbsolutePath();
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
}
