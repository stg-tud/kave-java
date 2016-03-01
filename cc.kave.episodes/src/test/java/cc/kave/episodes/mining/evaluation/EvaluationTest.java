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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
import cc.recommenders.io.Logger;

public class EvaluationTest {

	private static final int FREQUENCY = 5;
	private static final double BIDIRECTIONAL = 0.01;

	@Mock
	private ValidationContextsParser validationParser;
	@Mock
	private EventMappingParser mappingParser;
	@Mock
	private EpisodeParser episodeParser;
	@Mock
	private MaximalEpisodes maxEpisodeTracker;
	
	private QueryGeneratorByPercentage queryGenerator;
	private EpisodeRecommender recommender;

	private Set<Episode> validationData = Sets.newLinkedHashSet();
	private List<Event> events = new LinkedList<Event>();
	private Map<Integer, Set<Episode>> patterns = new HashMap<Integer, Set<Episode>>();
	private Map<Integer, Set<Episode>> maxPatterns = new HashMap<Integer, Set<Episode>>();

	private Evaluation sut;

	@Before
	public void setup() throws ZipException, IOException {
		Logger.reset();
		Logger.setCapturing(true);
		
		queryGenerator = new QueryGeneratorByPercentage();
		recommender = new EpisodeRecommender();
		
		MockitoAnnotations.initMocks(this);
		sut = new Evaluation(validationParser, mappingParser, queryGenerator, recommender, episodeParser,
				maxEpisodeTracker);

		validationData.add(createQuery("11"));
		validationData.add(createQuery("11", "12", "13", "11>12", "11>13", "12>13"));
		validationData.add(createQuery("11", "15", "16", "11>15", "11>16", "15>16"));
		validationData.add(createQuery("11", "20", "11>20"));
		validationData.add(createQuery("11", "20", "21", "11>20", "11>21", "20>21"));

		events.add(new Event());

		patterns.put(2, Sets.newHashSet(createPattern(3, "11", "12", "11>12"), createPattern(3, "11", "14", "11>14")));
		patterns.put(3, Sets.newHashSet(createPattern(3, "11", "15", "13", "11>15", "11>13", "15>13"),
				createPattern(3, "11", "13", "16", "11>13", "11>16", "13>16")));

		maxPatterns.put(2,
				Sets.newHashSet(createPattern(3, "11", "12", "11>12"), createPattern(3, "11", "14", "11>14")));
		maxPatterns.put(3, Sets.newHashSet(createPattern(3, "11", "15", "13", "11>15", "11>13", "15>13"),
				createPattern(3, "11", "13", "16", "11>13", "11>16", "13>16")));

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
		
		assertLogContains(3, "Generating queries for episode 0 with 2 number of invocations");
		assertLogContains(4, "Generating queries for episode 1 with 2 number of invocations");
		assertLogContains(5, "Generating queries for episode 2 with 2 number of invocations");
		
		assertLogContains(6, "% - Patterns configuration:\n");
		assertLogContains(7, "% - Frequency = 5\n");
		assertLogContains(8, "% - Bidirectional measure = 0.01\n");
		assertLogContains(9, "% - Querying strategy = [25%, 50%, 75%]\n");
		assertLogContains(10, "% - Proposal strategy = 3\n\n");
		
		assertLogContains(11, "\nEpisode:\n");
		assertLogContains(12, "0\t", "0.25: [ ", "<0.39, 0.28> ", "<0.29, 0.22> ", "]\t", "2\n");
		assertLogContains(18, "1\t", "0.25: [ ", "<0.29, 0.22> ", "]\t", "2\n", "2\t", "2\n", "\n");
		
		assertLogContains(26, "\tTop1\tTop2\tTop3\n");
		assertLogContains(27, "Removed 0.25\t", "<0.34, 0.25>\t", "<0.29, 0.22>\t", "<0.00, 0.00>\t", "\n");
		assertLogContains(32, "Removed 0.50\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "\n");
		assertLogContains(37, "Removed 0.75\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "<0.00, 0.00>\t", "\n");
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
