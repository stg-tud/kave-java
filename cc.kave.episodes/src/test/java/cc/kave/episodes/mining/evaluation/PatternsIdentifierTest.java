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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.mining.patterns.MaximalEpisodes;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EpisodesFilter;
import cc.kave.episodes.postprocessor.PatternsIdentifier;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PatternsIdentifierTest {

	@Rule
	public TemporaryFolder patternsFolder = new TemporaryFolder();
	@Rule
	public TemporaryFolder eventsFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private static final int FOLDNUM = 0;
	private static final int FREQUENCY = 2;
	private static final double ENTROPY = 0.5;

	@Mock
	private EpisodesFilter processor;
	@Mock
	private MaximalEpisodes maxEpisodes;
	@Mock
	private EpisodesParser epParser;
	@Mock
	private TransClosedEpisodes transClosure;
	@Mock
	private EpisodeToGraphConverter episodeGraphConverter;
	@Mock
	private EpisodeAsGraphWriter graphWriter;
	@Mock
	private EventStreamIo eventStream;

	private Map<Integer, Set<Episode>> patterns;
	private List<List<Fact>> stream;
	private List<Event> enclCtx;
	private List<Event> events;
	private List<Event> validationStream;

	private PatternsIdentifier sut;

	@Before
	public void setup() throws Exception {
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);

		patterns = Maps.newLinkedHashMap();
		Set<Episode> episodes = Sets.newLinkedHashSet();
		Episode ep = new Episode();
		ep.addFact(new Fact(1));
		episodes.add(ep);

		ep = new Episode();
		ep.addFact(new Fact(2));
		episodes.add(ep);
		patterns.put(1, episodes);

		stream = new LinkedList<>();
		List<Fact> method = Lists.newArrayList(new Fact(1), new Fact(2),
				new Fact(3), new Fact(4));
		stream.add(method);
		method = Lists.newArrayList(new Fact(5), new Fact(6), new Fact(7),
				new Fact(3));
		stream.add(method);
		method = Lists.newArrayList(new Fact(8), new Fact(4), new Fact(3));
		stream.add(method);
		method = Lists.newArrayList(new Fact(5), new Fact(9), new Fact(3));
		stream.add(method);
		
		enclCtx = Lists.newLinkedList();
		enclCtx.add(enclosingCtx(2));
		enclCtx.add(enclosingCtx(7));
		enclCtx.add(enclosingCtx(8));
		enclCtx.add(enclosingCtx(9));

		events = Lists.newArrayList(dummy(), firstCtx(1), enclosingCtx(2),
				inv(3), inv(4), firstCtx(5), superCtx(6), enclosingCtx(7),
				enclosingCtx(8), enclosingCtx(9), inv(5));

		validationStream = Lists.newArrayList(firstCtx(1), enclosingCtx(2),
				inv(3), inv(4), firstCtx(5), superCtx(6), enclosingCtx(7),
				inv(3), firstCtx(0), enclosingCtx(8), inv(4), inv(3),
				firstCtx(5), enclosingCtx(9), inv(3));

		sut = new PatternsIdentifier(patternsFolder.getRoot(),
				eventsFolder.getRoot(), processor, maxEpisodes, eventStream,
				epParser, transClosure, episodeGraphConverter, graphWriter, null);

		when(eventStream.parseStream(any(String.class))).thenReturn(stream);
		when(eventStream.readMapping(any(String.class))).thenReturn(events);
		when(eventStream.readMethods(any(String.class))).thenReturn(enclCtx);
		when(epParser.parse(anyInt(), any(EpisodeType.class))).thenReturn(patterns);
		when(maxEpisodes.getMaximalEpisodes(any(Map.class))).thenReturn(
				patterns);
		when(processor.filter(any(Map.class), anyInt(), anyDouble()))
				.thenReturn(patterns);
		when(transClosure.remTransClosure(any(Episode.class))).thenReturn(ep);
	}

	@After
	public void teardown() {
		Logger.reset();
	}

	@Test
	public void cannotBeInitializedWithNonExistingPatternFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Patterns folder does not exist");
		sut = new PatternsIdentifier(new File("does not exist"),
				eventsFolder.getRoot(), processor, maxEpisodes, eventStream,
				epParser, transClosure, episodeGraphConverter, graphWriter, null);
	}

	@Test
	public void cannotBeInitializedWithNonExistingEventsFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Events folder does not exist");
		sut = new PatternsIdentifier(patternsFolder.getRoot(), new File(
				"does not exist"), processor, maxEpisodes, eventStream, epParser,
				transClosure, episodeGraphConverter, graphWriter, null);
	}

	@Test
	public void cannotBeInitializedWithPatternsFile() throws IOException {
		File file = eventsFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Patterns is not a folder, but a file");
		sut = new PatternsIdentifier(file, eventsFolder.getRoot(), processor,
				maxEpisodes, eventStream, epParser, transClosure, episodeGraphConverter,
				graphWriter, null);
	}

	@Test
	public void cannotBeInitializedWithEventsFile() throws IOException {
		File file = eventsFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Events is not a folder, but a file");
		sut = new PatternsIdentifier(patternsFolder.getRoot(), file, processor,
				maxEpisodes, eventStream, epParser, transClosure, episodeGraphConverter,
				graphWriter, null);
	}

	@Test
	public void mocksAreCalledInTraining() throws Exception {
		sut.trainingCode(FREQUENCY, EpisodeType.SEQUENTIAL);
		
		verify(eventStream).parseStream(any(String.class));
		verify(eventStream).readMethods(any(String.class));
		verify(epParser).parse(anyInt(), any(EpisodeType.class));
		verify(maxEpisodes).getMaximalEpisodes(any(Map.class));
		verify(processor).filter(any(Map.class), anyInt(), anyInt());
	}

	@Test
	public void excepsionIsCalled() throws Exception {
		Episode ep = new Episode();
		ep.addStringsOfFacts("3", "4", "3>4");
		ep.setFrequency(3);
		patterns.put(2, Sets.newHashSet(ep));

		thrown.expect(Exception.class);
		thrown.expectMessage("Episode is not found sufficient number of times on the training stream!");

		assertTrue(eventsFolder.getRoot().exists());

		sut.trainingCode(FREQUENCY, EpisodeType.GENERAL);
	}

	@Test
	public void loggerIsCalledInTraining() throws Exception {
		Set<Episode> episodes = Sets.newLinkedHashSet();
		Episode ep = new Episode();
		ep.addStringsOfFacts("3", "4");
		ep.setFrequency(2);
		episodes.add(ep);

		ep = new Episode();
		ep.addStringsOfFacts("5", "3", "5>3");
		ep.setFrequency(2);
		episodes.add(ep);
		patterns.put(2, episodes);

		sut.trainingCode(FREQUENCY, EpisodeType.PARALLEL);

		assertLogContains(0, "Processed 2-node patterns!");
	}

	@Ignore
	@Test
	public void loggerIsCalledInValidation() throws Exception {
		Set<Episode> episodes = Sets.newLinkedHashSet();
		Episode ep = createEpisode(2, "3", "4");
		episodes.add(ep);

		ep = createEpisode(2, "5", "3", "5>3");
		episodes.add(ep);
		patterns.put(2, episodes);

		when(transClosure.remTransClosure(any(Episode.class))).thenReturn(
				createEpisode(2, "5", "3", "5>3"));
		when(episodeGraphConverter.convert(any(Episode.class), any(List.class)))
				.thenReturn(any(DefaultDirectedGraph.class));

		// sut.validationCode(NUMBREPOS, FREQUENCY, ENTROPY);

		verify(transClosure, times(2)).remTransClosure(any(Episode.class));
		verify(episodeGraphConverter, times(2)).convert(any(Episode.class),
				any(List.class));
		verify(graphWriter, times(2)).write(any(DefaultDirectedGraph.class),
				any(String.class));

		assertLogContains(0, "Processed 2-node patterns!");
	}

	@Ignore
	@Test
	public void fileIsCreated() throws Exception {
		File fileName = getFilePath(FOLDNUM, FREQUENCY, ENTROPY);

		// sut.validationCode(NUMBREPOS, FREQUENCY, ENTROPY);

		assertTrue(fileName.exists());
	}

	@Ignore
	@Test
	public void checkFileContent() throws Exception {
		Set<Episode> episodes = Sets.newLinkedHashSet();
		Episode ep = createEpisode(2, "3", "4", "3>4");
		episodes.add(ep);

		ep = createEpisode(2, "5", "3", "5>3");
		episodes.add(ep);
		patterns.put(2, episodes);

		ep = createEpisode(2, "3", "4", "5", "3>4");
		patterns.put(3, Sets.newHashSet(ep));

		// sut.validationCode(NUMBREPOS, FREQUENCY, ENTROPY);

		String actuals = FileUtils.readFileToString(getFilePath(FOLDNUM,
				FREQUENCY, ENTROPY));

		StringBuilder sb = new StringBuilder();
		sb.append("Patterns of size: 2-events\n");
		sb.append("Pattern\tFrequency\toccurrencesAsSet\toccurrencesOrder\n");
		sb.append("0\t2\t2\t1\n");
		sb.append("1\t2\t2\t2\n\n");
		sb.append("Patterns of size: 3-events\n");
		sb.append("Pattern\tFrequency\toccurrencesAsSet\toccurrencesOrder\n");
		sb.append("2\t2\t0\t0\n\n");

		assertEquals(sb.toString(), actuals);
	}

	private static Event inv(int i) {
		return Events.newInvocation(m(i));
	}

	private static Event firstCtx(int i) {
		return Events.newFirstContext(m(i));
	}

	private static Event superCtx(int i) {
		return Events.newSuperContext(m(i));
	}

	private static Event enclosingCtx(int i) {
		return Events.newContext(m(i));
	}

	private static Event dummy() {
		return Events.newDummyEvent();
	}

	private static IMethodName m(int i) {
		if (i == 0) {
			return Names.getUnknownMethod();
		} else {
			return Names.newMethod("[T,P] [T,P].m" + i + "()");
		}
	}

	private Episode createEpisode(int freq, String... strings) {
		Episode episode = new Episode();
		episode.addStringsOfFacts(strings);
		episode.setFrequency(freq);
		return episode;
	}

	private File getFilePath(int numbRepos, int freqThresh,
			double bidirectThresh) {
		File fileName = new File(eventsFolder.getRoot().getAbsolutePath()
				+ "/Repos" + numbRepos + "/Freq" + freqThresh + "/Bidirect"
				+ bidirectThresh + "/patternsValidation.txt");
		return fileName;
	}

	private File getGraphPath(int numbRepos, int freqThresh, double entropy,
			int pId) {
		File fileName = new File(eventsFolder.getRoot().getAbsolutePath()
				+ "/Repos" + numbRepos + "/Freq" + freqThresh + "/Bidirect"
				+ entropy + "/pattern" + pId + ".dot");
		return fileName;
	}

	private File createPath() {
		String fileName = (eventsFolder).getRoot().getAbsolutePath()
				+ "/TrainingData/fold0/";
		File filePath = new File(fileName);
		filePath.mkdirs();
		return filePath;
	}

	private void createStreamPath() {
		String fileName = createPath().getAbsolutePath() + "/stream.txt";
		new File(fileName);
	}

	private void createMappingPath() {
		String fileName = createPath().getAbsolutePath() + "/mapping.txt";
		new File(fileName);
	}
}