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

import static cc.recommenders.io.LoggerUtils.assertLogContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cc.kave.episodes.evaluation.queries.QueryStrategy;
import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.io.MappingParser;
import cc.kave.episodes.io.ValidationContextsParser;
import cc.kave.episodes.mining.patterns.MaximalEpisodes;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.TargetsCategorization;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.io.Logger;
import cc.recommenders.utils.LocaleUtils;

import com.google.common.collect.Sets;

public class RecommenderEvaluationTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private ValidationContextsParser validationParser;
	@Mock
	private MappingParser mappingParser;
	@Mock
	private EpisodeParser episodeParser;
	@Mock
	private MaximalEpisodes maxEpisodeTracker;
	@Mock
	private TargetsCategorization categorizer;

	private static final int FREQUENCY = 5;
	private static final double BIDIRECTIONAL = 0.01;
	private static final int REPOS = 2;

	private QueryStrategy queryGenerator;
	private EpisodeRecommender recommender;

	private Set<Episode> validationData = Sets.newLinkedHashSet();
	private List<Event> events = new LinkedList<Event>();
	private Map<Integer, Set<Episode>> patterns = new HashMap<Integer, Set<Episode>>();
	private Map<Integer, Set<Episode>> maxPatterns = new HashMap<Integer, Set<Episode>>();
	private Map<String, Set<Episode>> categories = new LinkedHashMap<String, Set<Episode>>();

	private RecommenderEvaluation sut;

	@Before
	public void setup() throws ZipException, IOException {
		LocaleUtils.setDefaultLocale();

		Logger.reset();
		Logger.setCapturing(true);

		queryGenerator = new QueryStrategy();
		recommender = new EpisodeRecommender();

		MockitoAnnotations.initMocks(this);
		sut = new RecommenderEvaluation(rootFolder.getRoot(), validationParser, mappingParser, queryGenerator, recommender,
				episodeParser, maxEpisodeTracker, categorizer);

		validationData.add(createQuery("11"));
		validationData.add(createQuery("11", "12", "13", "11>12", "11>13", "12>13"));
		validationData.add(createQuery("11", "12", "14", "11>12", "11>14", "12>14"));
		validationData.add(createQuery("11", "15", "16", "11>15", "11>16", "15>16"));
		validationData.add(createQuery("11", "12", "13", "14", "11>12", "11>13", "11>14", "12>13"));
		validationData.add(createQuery("11", "12", "13", "14", "15", "11>12", "11>13", "11>14", "11>15"));
		validationData.add(createQuery("11", "12", "13", "14", "16", "11>12", "11>13", "11>14", "11>16"));
		validationData.add(createQuery("11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"));
		validationData.add(createQuery("11", "20", "11>20"));
		validationData.add(createQuery("11", "20", "21", "11>20", "11>21", "20>21"));

		events.add(new Event());

		patterns.put(2, Sets.newHashSet(createPattern(3, "11", "12", "11>12"), createPattern(4, "11", "13", "11>13"),
				createPattern(3, "11", "14", "11>14"), createPattern(5, "11", "15", "11>15")));
		patterns.put(3, Sets.newHashSet(createPattern(3, "11", "15", "13", "11>15", "11>13", "15>13"),
				createPattern(3, "11", "13", "16", "11>13", "11>16", "13>16")));
		patterns.put(4, Sets.newHashSet(createPattern(3, "11", "12", "13", "14", "11>12", "11>13", "11>14", "12>13")));

		maxPatterns.put(3, Sets.newHashSet(createPattern(4, "11", "15", "13", "11>15", "11>13", "15>13"),
				createPattern(3, "11", "13", "16", "11>13", "11>16", "13>16")));
		maxPatterns.put(4,
				Sets.newHashSet(createPattern(3, "11", "12", "13", "14", "11>12", "11>13", "11>14", "12>13")));

		categories.put("0-1", Sets.newHashSet(createQuery("11"), createQuery("11", "20", "11>20")));
		categories.put("2",
				Sets.newHashSet(createQuery("11", "12", "13", "11>12", "11>13", "12>13"),
						createQuery("11", "15", "16", "11>15", "11>16", "15>16"),
						createQuery("11", "12", "14", "11>12", "11>14", "12>14"),
						createQuery("11", "20", "21", "11>20", "11>21", "20>21")));
		categories.put("3", Sets.newHashSet(createQuery("11", "12", "13", "14", "11>12", "11>13", "11>14", "12>13")));
		categories.put("4",
				Sets.newHashSet(createQuery("11", "12", "13", "14", "15", "11>12", "11>13", "11>14", "11>15"),
						createQuery("11", "12", "13", "14", "16", "11>12", "11>13", "11>14", "11>16")));
		categories.put("10-19",
				Sets.newHashSet(createQuery("11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
						"11>12", "11>13", "11>14", "11>15", "11>16", "11>17", "11>18", "11>19", "11>20", "11>21",
						"11>22")));

		when(categorizer.categorize(validationData)).thenReturn(categories);

		when(episodeParser.parse(any(File.class))).thenReturn(patterns);
		when(mappingParser.parse(REPOS)).thenReturn(events);
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
		sut = new RecommenderEvaluation(new File("does not exist"), validationParser, mappingParser, queryGenerator, recommender,
				episodeParser, maxEpisodeTracker, categorizer);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Evaluations folder is not a folder, but a file");
		sut = new RecommenderEvaluation(file, validationParser, mappingParser, queryGenerator, recommender, episodeParser,
				maxEpisodeTracker, categorizer);
	}

	@Ignore
	@Test
	public void logger() throws ZipException, IOException {
		Logger.clearLog();
		sut.evaluate(REPOS);

		verify(episodeParser).parse(any(File.class));
		verify(mappingParser).parse(REPOS);
		verify(validationParser).parse(events);
		verify(maxEpisodeTracker).getMaximalEpisodes(patterns);

		assertLogContains(0, "Reading the learned patterns");
		assertLogContains(1, "Reading the mapping file");
		assertLogContains(2, "Readng the validation data\n");

		assertLogContains(3, "\n");
		assertLogContains(4, "% - Evaluations configuration:\n");
		assertLogContains(5, "% - Frequency = 5\n");
		assertLogContains(6, "% - Bidirectional measure = 0.01\n");
		assertLogContains(7, "% - Querying strategy = [0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9]\n");
		assertLogContains(8, "% - Proposal strategy = 10\n");
		assertLogContains(9, "% - Similarity metric = F1-value\n");
		assertLogContains(10, "% - Number of maximal queries = all combinations\n\n");

		assertLogContains(11, "Generating queries for episodes with 2 number of invocations\n");
		assertLogContains(12, "Number of targets with no proposals = 1\n\n");
		assertLogContains(13, "\tTop1", "\tTop2", "\tTop3", "\tTop4", "\tTop5", "\tTop6", "\tTop7", "\tTop8", "\tTop9",
				"\tTop10", "\n");
		assertLogContains(24, "Removed 0.10\t", "<0.25, 0.32>\t", "<0.29, 0.22>\t", "<0.22, 0.55>\t", "<0.00, 0.00>\t",
				"<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t",
				"<0.00, 0.00>\t", "\n");

		assertLogContains(36, "Generating queries for episodes with 3 number of invocations\n");
		assertLogContains(37, "Number of targets with no proposals = 0\n\n");
		assertLogContains(38, "\tTop1", "\tTop2", "\tTop3", "\tTop4", "\tTop5", "\tTop6", "\tTop7", "\tTop8", "\tTop9",
				"\tTop10", "\n");
		assertLogContains(49, "Removed 0.10\t", "<0.45, 0.67>", "<0.24, 0.67>", "<0.24, 0.67>", "<0.00, 0.00>\t",
				"<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t",
				"<0.00, 0.00>\t", "\n");
		assertLogContains(61, "Removed 0.40\t", "<0.24, 0.51>", "<0.29, 0.20>", "<0.22, 0.67>", "<0.00, 0.00>\t",
				"<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t",
				"<0.00, 0.00>\t", "\n");
		
		assertLogContains(73, "Generating queries for episodes with 4 number of invocations\n");
//		assertLogContains(37, "\nNumber of targets with no proposals = 0\n\n");
//		assertLogContains(38, "\tTop1", "\tTop2", "\tTop3", "\tTop4", "\tTop5", "\tTop6", "\tTop7", "\tTop8", "\tTop9",
//				"\tTop10", "\n");
//		assertLogContains(49, "Removed 0.10\t", "<0.45, 0.67>", "<0.24, 0.67>", "<0.24, 0.67>", "<0.00, 0.00>\t",
//				"<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t",
//				"<0.00, 0.00>\t", "\n");
//		assertLogContains(61, "Removed 0.40\t", "<0.24, 0.51>", "<0.29, 0.20>", "<0.22, 0.67>", "<0.00, 0.00>\t",
//				"<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t",
//				"<0.00, 0.00>\t", "\n");
	}

	@Ignore
	@Test
	public void Inv2() throws ZipException, IOException {
		sut.evaluate(REPOS);

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
		expected.add("Target query 1\t0.1: [ <0.29, 0.22>; ]\t2");
		expected.add("Target query 2\t0.1: [ <0.5, 0.33>; <0.29, 0.33>; ]\t2");

		assertEquals(expected, actuals);
	}

	@Ignore
	@Test
	public void twoCategoriesLogger() throws ZipException, IOException {
		validationData.add(createQuery("11", "12", "20", "21", "11>12", "11>20", "11>21", "12>20", "12>21", "20>21"));
		when(validationParser.parse(events)).thenReturn(validationData);

		categories.put("3", Sets
				.newHashSet(createQuery("11", "12", "20", "21", "11>12", "11>20", "11>21", "12>20", "12>21", "20>21")));
		when(categorizer.categorize(validationData)).thenReturn(categories);

		Logger.clearLog();
		sut.evaluate(REPOS);

		assertLogContains(0, "Reading the learned patterns");
		assertLogContains(1, "Reading the mapping file");
		assertLogContains(2, "Readng the validation data\n");

		assertLogContains(3, "\n");
		assertLogContains(4, "% - Evaluations configuration:\n");
		assertLogContains(5, "% - Frequency = 5\n");
		assertLogContains(6, "% - Bidirectional measure = 0.01\n");
		assertLogContains(7, "% - Querying strategy = [0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9]\n");
		assertLogContains(8, "% - Proposal strategy = 5\n");
		assertLogContains(9, "% - Similarity metric = F1-value\n");
		assertLogContains(10, "% - Number of maximal queries = all combinations\n\n");

		assertLogContains(11, "Generating queries for episodes with 2 number of invocations\n");
		assertLogContains(12, "\nNumber of targets with no proposals = 1\n\n");

		assertLogContains(13, "\tTop1", "\tTop2", "\tTop3", "\tTop4", "\tTop5", "\n");
		assertLogContains(19, "Removed 0.10\t", "<0.39, 0.28>\t", "<0.29, 0.33>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t",
				"<0.00, 0.00>\t", "\n");

		assertLogContains(26, "Generating queries for episodes with 3 number of invocations\n");
		assertLogContains(27, "\nNumber of targets with no proposals = 0\n\n");

		assertLogContains(28, "\tTop1", "\tTop2", "\tTop3", "\tTop4", "\tTop5", "\n");
		assertLogContains(34, "Removed 0.10\t", "<0.33, 0.22>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t",
				"<0.00, 0.00>\t", "\n");
		assertLogContains(41, "Removed 0.40\t", "<0.50, 0.22>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t",
				"<0.00, 0.00>\t", "\n");
	}

	@Ignore
	@Test
	public void twoCategoriesWriter() throws ZipException, IOException {
		validationData.add(createQuery("11", "12", "20", "21", "11>12", "11>20", "11>21", "12>20", "12>21", "20>21"));
		when(validationParser.parse(events)).thenReturn(validationData);

		categories.put("3", Sets
				.newHashSet(createQuery("11", "12", "20", "21", "11>12", "11>20", "11>21", "12>20", "12>21", "20>21")));
		when(categorizer.categorize(validationData)).thenReturn(categories);

		sut.evaluate(REPOS);

		File fileName1 = new File(rootFolder.getRoot().getAbsolutePath() + "/2.txt");
		assertTrue(fileName1.exists());
		assertFalse(fileName1.isDirectory());

		File fileName2 = new File(rootFolder.getRoot().getAbsolutePath() + "/3.txt");
		assertTrue(fileName2.exists());
		assertFalse(fileName2.isDirectory());

		List<String> actuals1 = new LinkedList<String>();
		try {
			actuals1 = FileUtils.readLines(fileName1);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		List<String> expected1 = new LinkedList<String>();
		expected1.add("Target query 1\t0.1: [ <0.29, 0.22>; ]\t2");
		expected1.add("Target query 2\t0.1: [ <0.5, 0.33>; <0.29, 0.33>; ]\t2");

		assertEquals(expected1, actuals1);

		List<String> actuals2 = new LinkedList<String>();
		try {
			actuals2 = FileUtils.readLines(fileName2);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		List<String> expected2 = new LinkedList<String>();
		expected2.add("Target query 1\t0.1: [ <0.33, 0.22>; ]\t0.4: [ <0.5, 0.22>; ]\t3");

		assertEquals(expected2, actuals2);
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
