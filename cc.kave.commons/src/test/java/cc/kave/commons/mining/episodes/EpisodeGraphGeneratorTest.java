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
package cc.kave.commons.mining.episodes;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;

import cc.kave.commons.mining.reader.EpisodeParser;
import cc.kave.commons.mining.reader.EventMappingParser;
import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.persistence.EpisodeAsGraphWriter;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.io.Directory;

import com.google.common.collect.Lists;

@Ignore
public class EpisodeGraphGeneratorTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private String tmpFolderName;
	private int frequencyThreshold = 12;
	private double bidirectionalThreshold = 0.8;

	@Mock
	private EpisodeParser episodeParser;
	@Mock
	private EventMappingParser mappingParser;
	@Mock
	private Directory episodeDirectory;

	private MaximalFrequentEpisodes episodeLearned;
	private EpisodeToGraphConverter graphConverter;
	private NoTransitivelyClosedEpisodes transitivityClosure;
	private EpisodeAsGraphWriter writer;
	private EpisodeGraphGenerator sut;

	@Before
	public void setup() {
		initMocks(this);

		episodeLearned = new MaximalFrequentEpisodes();
		graphConverter = new EpisodeToGraphConverter();
		transitivityClosure = new NoTransitivelyClosedEpisodes();
		writer = new EpisodeAsGraphWriter();
		sut = new EpisodeGraphGenerator(rootFolder.getRoot(), episodeParser, episodeLearned, mappingParser,
				transitivityClosure, writer, graphConverter);
		tmpFolderName = rootFolder.getRoot().getAbsolutePath();

		when(episodeParser.parse()).thenReturn(createEpisodes());
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Episode-miner folder does not exist");
		sut = new EpisodeGraphGenerator(new File("does not exist"), episodeParser, episodeLearned, mappingParser,
				transitivityClosure, writer, graphConverter);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Episode-miner folder is not a folder, but a file");
		sut = new EpisodeGraphGenerator(file, episodeParser, episodeLearned, mappingParser, transitivityClosure,
				writer, graphConverter);
	}

	@Test
	public void structureIsCreated() throws Exception {

		when(mappingParser.parse()).thenReturn(
				createMapping(eventMethodDeclGeneralAPI("M1"), eventInvocationSpecificAPI("M2"),
						eventMethodDeclSpecificAPI("M3"), eventInvocationGeneralAPI("M4"),
						eventMethodDeclGeneralAPI("M5")));

		sut.generateGraphs(frequencyThreshold, bidirectionalThreshold);

		File folderStructure = new File(tmpFolderName + "/graphs/" + "/configurationF" + frequencyThreshold + "B"
				+ bidirectionalThreshold + "/");
		assertTrue(folderStructure.exists());
		assertTrue(folderStructure.isDirectory());

		File folderGeneralAPI = new File(folderStructure.getAbsolutePath() + "/generalAPI/");
		assertTrue(folderGeneralAPI.exists());
		assertTrue(folderGeneralAPI.isDirectory());

		File folderSpecificAPI = new File(folderStructure.getAbsolutePath() + "/specificAPI/");
		assertTrue(folderSpecificAPI.exists());
		assertTrue(folderSpecificAPI.isDirectory());

		verify(episodeParser).parse();
		verify(mappingParser).parse();
	}

	@Test
	public void generalAPIPatternsStored() throws Exception {
		when(mappingParser.parse()).thenReturn(
				createMapping(eventMethodDeclGeneralAPI("M1"), eventInvocationGeneralAPI("M2"),
						eventMethodDeclGeneralAPI("M3"), eventInvocationGeneralAPI("M4"),
						eventMethodDeclGeneralAPI("M5")));

		sut.generateGraphs(frequencyThreshold, bidirectionalThreshold);

		File folder = new File(tmpFolderName + "/graphs/" + "/configurationF" + frequencyThreshold + "B"
				+ bidirectionalThreshold + "/" + "/generalAPI/");

		for (int i = 0; i < 4; i++) {
			File fileName = new File(folder.getAbsolutePath() + "/graph" + i + ".dot");
			assertTrue(fileName.exists());
			assertFalse(fileName.isDirectory());
		}

		verify(episodeParser).parse();
		verify(mappingParser).parse();
	}

	@Test
	public void specificAPIPatternsStored() throws Exception {
		when(mappingParser.parse()).thenReturn(
				createMapping(eventMethodDeclSpecificAPI("M1"), eventInvocationSpecificAPI("M2"),
						eventMethodDeclSpecificAPI("M3"), eventInvocationSpecificAPI("M4"),
						eventMethodDeclSpecificAPI("M5")));

		sut.generateGraphs(frequencyThreshold, bidirectionalThreshold);

		File folder = new File(tmpFolderName + "/graphs/" + "/configurationF" + frequencyThreshold + "B"
				+ bidirectionalThreshold + "/" + "/specificAPI/");

		for (int i = 0; i < 4; i++) {
			File fileName = new File(folder.getAbsolutePath() + "/graph" + i + ".dot");
			assertTrue(fileName.exists());
			assertFalse(fileName.isDirectory());
		}

		verify(episodeParser).parse();
		verify(mappingParser).parse();
	}

	private Map<Integer, List<Episode>> createEpisodes() {
		Map<Integer, List<Episode>> someEpisodes = new HashMap<Integer, List<Episode>>();
		someEpisodes.put(1, newArrayList(newEpisode(3, 1, "1"), newEpisode(3, 1, "2"), newEpisode(3, 1, "3")));
		someEpisodes.put(2, newArrayList(newEpisode(3, 2, "4", "5", "4>5"), newEpisode(2, 2, "3", "4", "3>4")));
		someEpisodes.put(3,
				newArrayList(newEpisode(1, 3, "1", "3", "5", "3>5"), newEpisode(3, 3, "2", "3", "4", "3>4")));
		someEpisodes.put(4, newArrayList(newEpisode(3, 4, "1", "2", "3", "4")));
		return someEpisodes;
	}

	private Episode newEpisode(int frequency, int numberOfEvents, String... facts) {
		Episode episode = new Episode();
		episode.addStringsOfFacts(facts);
		episode.setFrequency(frequency);
		episode.setNumEvents(numberOfEvents);
		return episode;
	}

	private List<Event> createMapping(Event... events) {
		List<Event> eventMapping = Lists.newArrayList(events);
		return eventMapping;
	}

	private static Event eventInvocationGeneralAPI(String method) {
		Event e = new Event();
		e.setKind(EventKind.INVOCATION);
		e.setMethod(methodGeneralAPI(method));
		return e;
	}

	private static Event eventInvocationSpecificAPI(String method) {
		Event e = new Event();
		e.setKind(EventKind.INVOCATION);
		e.setMethod(methodSpecificAPI(method));
		return e;
	}

	private static Event eventMethodDeclGeneralAPI(String method) {
		Event e = new Event();
		e.setKind(EventKind.METHOD_DECLARATION);
		e.setMethod(methodGeneralAPI(method));
		return e;
	}

	private static Event eventMethodDeclSpecificAPI(String method) {
		Event e = new Event();
		e.setKind(EventKind.METHOD_DECLARATION);
		e.setMethod(methodSpecificAPI(method));
		return e;
	}

	private static IMethodName methodGeneralAPI(String name) {
		ITypeName declType = typeGeneralAPI("T");
		ITypeName retType = typeGeneralAPI("R");
		String methodName = String.format("[%s] [%s].%s()", retType, declType, name);
		return MethodName.newMethodName(methodName);
	}

	private static IMethodName methodSpecificAPI(String name) {
		ITypeName declType = typeSpecificAPI("T");
		ITypeName retType = typeSpecificAPI("R");
		String methodName = String.format("[%s] [%s].%s()", retType, declType, name);
		return MethodName.newMethodName(methodName);
	}

	private static ITypeName typeGeneralAPI(String name) {
		return TypeName.newTypeName("System.namespace." + name + ", P");
	}

	private static ITypeName typeSpecificAPI(String name) {
		return TypeName.newTypeName("some.namespace." + name + ", P");
	}
}
