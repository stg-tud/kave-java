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
package cc.kave.episodes.mining.patterns;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.postprocessor.EpisodesFilter;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class FrequenciesAnalyzerTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private static final int NUMBREPOS = 5;
	private static final int FREQUENCY = 5;
	private static final double ENTROPY = 0.5;
	
	@Mock
	private EpisodesFilter postprocessor;

	private Map<Integer, Set<Episode>> patterns;

	private FrequenciesAnalyzer sut;

	@Before
	public void setup() throws Exception {
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);

		patterns = Maps.newLinkedHashMap();
		patterns.put(1, Sets.newHashSet(createEpisode(5, 0.7, "1")));
		patterns.put(2, Sets.newHashSet(createEpisode(5, 0.8, "1", "2"), createEpisode(6, 1.0, "3", "4", "3>4")));
		patterns.put(3, Sets.newHashSet(createEpisode(4, 0.9, "1", "2", "3", "2>3"),
				createEpisode(3, 0.8, "1", "4", "5", "1>4", "1>5")));
		
		when(postprocessor.postprocess(any(Map.class), anyInt(), anyDouble())).thenReturn(patterns);
		
		sut = new FrequenciesAnalyzer(rootFolder.getRoot(), postprocessor);
	}
	
	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Patterns folder does not exist");
		sut = new FrequenciesAnalyzer(new File("does not exist"), postprocessor);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Patterns folder is not a folder, but a file");
		sut = new FrequenciesAnalyzer(file, postprocessor);
	}

	@Test
	public void mocksAreCalledInTraining() throws Exception {
		sut.analyzeSuperEpisodes(NUMBREPOS, FREQUENCY, ENTROPY);;

		verify(postprocessor).postprocess(any(Map.class), anyInt(), anyDouble());
	}
	
	public void fileIsCreated() throws Exception {
		File fileName = getFileName(NUMBREPOS, FREQUENCY, ENTROPY);
		
		sut.analyzeSuperEpisodes(NUMBREPOS, FREQUENCY, ENTROPY);
		
		assertTrue(fileName.exists());
	}
	
	@Test
	public void checkFileContent() throws Exception {
		StringBuilder expected = new StringBuilder();
	    expected.append("2-node episodes\n");
	    expected.append("[1, 2]\t5\n");
	    expected.append("[1, 2, 3, 2>3]\t4\n\n\n");
	    
	    sut.analyzeSuperEpisodes(NUMBREPOS, FREQUENCY, ENTROPY);
	    
	    String actuals = FileUtils.readFileToString(getFileName(NUMBREPOS, FREQUENCY, ENTROPY));
	    
	    assertEquals(expected.toString(), actuals);
	}

	private Episode createEpisode(int freq, double entropy, String... string) {
		Episode episode = new Episode();
		episode.addStringsOfFacts(string);
		episode.setFrequency(freq);
		episode.setEntropy(entropy);
		return episode;
	}
	
	private File getFileName(int numbRepos, int freqThresh, double entropy) {
		File fileName = new File(rootFolder.getRoot().getAbsolutePath() + "/Repos" + numbRepos + "/Freq" + freqThresh
				+ "/Bidirect" + entropy + "/superEpisodes.txt");
		return fileName;
	}
}
