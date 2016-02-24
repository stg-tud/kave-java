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
package cc.kave.episodes.analyzer.outputs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.mining.graphs.TransitivelyClosedEpisodes;
import cc.kave.episodes.mining.patterns.MaximalEpisodes;
import cc.kave.episodes.mining.reader.EpisodeParser;
import cc.kave.episodes.mining.reader.EventMappingParser;
import cc.kave.episodes.model.Episode;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.io.Directory;

public class EpisodeGraphGeneratorTrainingDataTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private String tmpFolderName;
	private static final int FREQ = 12;
	private static final double BD = 0.8;

	@Mock
	private EpisodeParser episodeParser;
	@Mock
	private EventMappingParser mappingParser;
	@Mock
	private Directory episodeDirectory;

	private MaximalEpisodes episodeLearned;
	private EpisodeToGraphConverter graphConverter;
	private TransitivelyClosedEpisodes transitivityClosure;
	private EpisodeAsGraphWriter writer;
	private List<Event> events;
	private Map<Integer, Set<Episode>> episodes;
	private File folderStructure;
	
	private TrainingDataGraphGenerator sut;

	@Before
	public void setup() {
		initMocks(this);

		episodeLearned = new MaximalEpisodes();
		graphConverter = new EpisodeToGraphConverter();
		transitivityClosure = new TransitivelyClosedEpisodes();
		writer = new EpisodeAsGraphWriter();
		events = createMapping(eventMethodDeclGeneralAPI("M0"), eventInvocationSpecificAPI("M1"),
				eventMethodDeclSpecificAPI("M2"), eventInvocationGeneralAPI("M3"), eventMethodDeclGeneralAPI("M4"), 
				eventMethodDeclGeneralAPI("M5"), eventMethodDeclGeneralAPI("M6"));
		episodes = createEpisodes();
		
		sut = new TrainingDataGraphGenerator(rootFolder.getRoot(), episodeParser, episodeLearned, mappingParser,
				transitivityClosure, writer, graphConverter);
		tmpFolderName = rootFolder.getRoot().getAbsolutePath();
		folderStructure = new File(tmpFolderName + "/graphs/TrainingData/" + "/configurationF" + FREQ + "B" + BD + "/");

		when(episodeParser.parse(eq(FREQ), eq(BD))).thenReturn(episodes);
		when(mappingParser.parse()).thenReturn(events);
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Episode-miner folder does not exist");
		sut = new TrainingDataGraphGenerator(new File("does not exist"), episodeParser, episodeLearned, mappingParser,
				transitivityClosure, writer, graphConverter);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Episode-miner folder is not a folder, but a file");
		sut = new TrainingDataGraphGenerator(file, episodeParser, episodeLearned, mappingParser, transitivityClosure,
				writer, graphConverter);
	}

	@Test
	public void structureIsCreated() throws Exception {

		sut.generateGraphs(FREQ, BD);

		assertTrue(folderStructure.exists());
		assertTrue(folderStructure.isDirectory());

		verify(episodeParser).parse(eq(FREQ), eq(BD));
		verify(mappingParser).parse();
	}

	@Test
	public void patternsStored() throws Exception {

		sut.generateGraphs(FREQ, BD);
		
		verify(episodeParser).parse(eq(FREQ), eq(BD));
		verify(mappingParser).parse();

		File fileName;
		int epCounter = 0;
		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			if (entry.getKey() > 1) {
				for (Episode e : entry.getValue()) {
					List<String> types = getTypes(e, events);
					for (String type : types) {
						String folder = folderStructure.getAbsolutePath();
						fileName = new File(folder + "/" + type + "/graph" + epCounter + ".dot");
						assertTrue(fileName.exists());
						assertFalse(fileName.isDirectory());
					}
					epCounter++;
				}
			}
		}
	}

	private List<String> getTypes(Episode e, List<Event> events) {
		List<String> types = new LinkedList<String>();
		for (Fact fact : e.getFacts()) {
			if (!fact.isRelation()) {
				int factID = fact.getFactID();
				String type = events.get(factID).getMethod().getDeclaringType().getFullName().toString().replace(".", "/");
				if (!types.contains(type)) {
					types.add(type);
				}
			}
		}
		return types;
	}

	private Map<Integer, Set<Episode>> createEpisodes() {
		Map<Integer, Set<Episode>> episodes = new HashMap<Integer, Set<Episode>>();
		episodes.put(1, Sets.newHashSet(newEpisode("1"), newEpisode("2"), newEpisode("3")));
		episodes.put(2, Sets.newHashSet(newEpisode("4", "5"), newEpisode("3", "4")));
		episodes.put(3,
				Sets.newHashSet(newEpisode("1", "3", "5"), newEpisode("2", "3", "5")));
		episodes.put(4, Sets.newHashSet(newEpisode("1", "2", "3", "6")));
		return episodes;
	}

	private Episode newEpisode(String... facts) {
		Episode episode = new Episode();
		episode.setFrequency(3);
		episode.addStringsOfFacts(facts);
		if (facts.length > 1) {
			for (int idx1 = 0; idx1 < facts.length - 1; idx1++) {
				for (int idx2 = idx1 + 1; idx2 < facts.length; idx2++) {
					Fact fact1 = new Fact(facts[idx1]);
					Fact fact2 = new Fact(facts[idx2]);
					episode.addFact(new Fact(fact1, fact2));
				}
			} 
		}
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
