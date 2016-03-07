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
package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.testutils.LoggerUtils.assertLogContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Event;
import cc.kave.episodes.evaluation.queries.QueryGeneratorByPercentage;
import cc.kave.episodes.mining.patterns.MaximalEpisodes;
import cc.kave.episodes.mining.reader.EpisodeParser;
import cc.kave.episodes.mining.reader.EventMappingParser;
import cc.kave.episodes.mining.reader.ValidationContextsParser;
import cc.kave.episodes.model.Episode;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.io.Logger;
import cc.recommenders.utils.LocaleUtils;

public class EvaluationTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Mock
	private ValidationContextsParser validationParser;
	@Mock
	private EventMappingParser mappingParser;
	@Mock
	private EpisodeParser episodeParser;
	@Mock
	private MaximalEpisodes maxEpisodeTracker;

	private static final int FREQUENCY = 5;
	private static final double BIDIRECTIONAL = 0.01;
	
	private QueryGeneratorByPercentage queryGenerator;
	private EpisodeRecommender recommender;

	private Set<Episode> validationData = Sets.newLinkedHashSet();
	private List<Event> events = new LinkedList<Event>();
	private Map<Integer, Set<Episode>> patterns = new HashMap<Integer, Set<Episode>>();
	private Map<Integer, Set<Episode>> maxPatterns = new HashMap<Integer, Set<Episode>>();
	
	private Episode categoryQuery = createQuery("11", "20", "21", "11>20", "11>21", "20>21");

	private Evaluation sut;

	@Before
	public void setup() throws ZipException, IOException {
		LocaleUtils.setDefaultLocale();
		
		Logger.reset();
		Logger.setCapturing(true);

		queryGenerator = new QueryGeneratorByPercentage();
		recommender = new EpisodeRecommender();

		MockitoAnnotations.initMocks(this);
		sut = new Evaluation(rootFolder.getRoot(), validationParser, mappingParser, queryGenerator, recommender, episodeParser,
				maxEpisodeTracker);

		validationData.add(createQuery("11"));
		validationData.add(createQuery("11", "12", "13", "11>12", "11>13", "12>13"));
		validationData.add(createQuery("11", "15", "16", "11>15", "11>16", "15>16"));
		validationData.add(createQuery("11", "20", "11>20"));
		validationData.add(categoryQuery);
		
		events.add(new Event());

		patterns.put(2, Sets.newHashSet(createPattern(3, "11", "12", "11>12"), createPattern(3, "11", "14", "11>14")));
		patterns.put(3, Sets.newHashSet(createPattern(3, "11", "15", "13", "11>15", "11>13", "15>13"),
				createPattern(3, "11", "13", "16", "11>13", "11>16", "13>16")));

		maxPatterns.put(2,
				Sets.newHashSet(createPattern(3, "11", "12", "11>12"), createPattern(3, "11", "14", "11>14"), 
								createPattern(3, "11", "13", "11>13")));
		maxPatterns.put(3, Sets.newLinkedHashSet());
				maxPatterns.get(3).add(createPattern(3, "11", "15", "13", "11>15", "11>13", "15>13"));

		when(episodeParser.parse(eq(FREQUENCY), eq(BIDIRECTIONAL))).thenReturn(patterns);
		when(mappingParser.parse()).thenReturn(events);
		when(validationParser.parse(events)).thenReturn(validationData);
		when(maxEpisodeTracker.getMaximalEpisodes(patterns)).thenReturn(maxPatterns);
	}

	@After
	public void teardown() {
		Logger.reset();
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Evaluations folder does not exist");
		sut = new Evaluation(new File("does not exist"), validationParser, mappingParser, 
							queryGenerator, recommender, episodeParser, maxEpisodeTracker);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Evaluations folder is not a folder, but a file");
		sut = new Evaluation(file, validationParser, mappingParser, queryGenerator, 
								recommender, episodeParser, maxEpisodeTracker);
	}
	
	@Test
	public void logger() throws ZipException, IOException {
		Logger.clearLog();
		sut.evaluate();

		verify(episodeParser).parse(eq(FREQUENCY), eq(BIDIRECTIONAL));
		verify(mappingParser).parse();
		verify(validationParser).parse(events);
		verify(maxEpisodeTracker).getMaximalEpisodes(patterns);

		assertLogContains(0, "Reading the learned patterns");
		assertLogContains(1, "Reading the mapping file");
		assertLogContains(2, "Readng the validation data\n");

		assertLogContains(3, "\n");
		assertLogContains(4, "% - Patterns configuration:\n");
		assertLogContains(5, "% - Frequency = 5\n");
		assertLogContains(6, "% - Bidirectional measure = 0.01\n");
		assertLogContains(7, "% - Querying strategy = [25%, 50%, 75%]\n");
		assertLogContains(8, "% - Proposal strategy = 5\n");
		assertLogContains(9, "% - Similarity metric = F1-value\n\n");

		assertLogContains(10, "Generating queries for episodes with 2 number of invocations\n");
		assertLogContains(11, "\nNumber of targets with no proposals = 1\n\n");

		assertLogContains(12, "\tTop1", "\tTop2", "\tTop3", "\tTop4", "\tTop5", "\n");
		assertLogContains(18, "Removed 0.25\t", "<0.39, 0.28>\t", "<0.29, 0.33>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "\n");
		assertLogContains(25, "Removed 0.50\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "\n");
		assertLogContains(32, "Removed 0.75\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "\n");
	}
	
	@Test
	public void Inv2() throws ZipException, IOException {
		sut.evaluate();
		
		File fileName = new File(rootFolder.getRoot().getAbsolutePath() + "/2.txt");
		assertTrue(fileName.exists());
		assertFalse(fileName.isDirectory());
		
		List<String> actuals = new LinkedList<String>(); 
		try {
			actuals = FileUtils.readLines(fileName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		List<String> expected = new LinkedList<String>();
		expected.add("Target query 1\t0.25: [ <0.29, 0.22>; ]\t2");
		expected.add("Target query 2\t0.25: [ <0.50, 0.33>; <0.29, 0.33>; ]\t2");
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void Inv3() throws ZipException, IOException {
		Set<Episode> target = Sets.newHashSet(createQuery("11", "12", "20", "21", 
								"11>12", "11>20", "11>21", "12>20", "12>21", "20>21"));
		
		when(validationParser.parse(events)).thenReturn(target);
		
		Logger.clearLog();
		sut.evaluate();
		
		assertLogContains(0, "Reading the learned patterns");
		assertLogContains(1, "Reading the mapping file");
		assertLogContains(2, "Readng the validation data\n");
		
		assertLogContains(3, "\n");
		assertLogContains(4, "% - Patterns configuration:\n");
		assertLogContains(5, "% - Frequency = 5\n");
		assertLogContains(6, "% - Bidirectional measure = 0.01\n");
		assertLogContains(7, "% - Querying strategy = [25%, 50%, 75%]\n");
		assertLogContains(8, "% - Proposal strategy = 5\n");
		assertLogContains(9, "% - Similarity metric = F1-value\n\n");
		
		assertLogContains(10, "Generating queries for episodes with 3 number of invocations\n");
		assertLogContains(11, "\nNumber of targets with no proposals = 0\n\n");
		
		assertLogContains(12, "\tTop1", "\tTop2", "\tTop3", "\tTop4", "\tTop5", "\n");
		assertLogContains(18, "Removed 0.25\t", "<0.33, 0.22>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "\n");
		assertLogContains(25, "Removed 0.50\t", "<0.50, 0.22>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "\n");
	}

	private Episode createQuery(String... strings) {
		Episode query = new Episode();
		query.addStringsOfFacts(strings);
		return query;
	}

	private Episode createPattern(int freq, String... strings) {
		Episode pattern = new Episode();
		pattern.setFrequency(freq);
		pattern.addStringsOfFacts(strings);
		return pattern;
	}
}
