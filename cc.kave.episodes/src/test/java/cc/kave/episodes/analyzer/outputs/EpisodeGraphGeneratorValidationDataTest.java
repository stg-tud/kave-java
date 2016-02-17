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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipException;

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
import cc.kave.episodes.mining.reader.EventMappingParser;
import cc.kave.episodes.mining.reader.ValidationContextsParser;
import cc.kave.episodes.model.Episode;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.io.Directory;

public class EpisodeGraphGeneratorValidationDataTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private String tmpFolderName;
	
	@Mock
	private ValidationContextsParser contextParser;
	@Mock
	private EventMappingParser mappingParser;
	@Mock
	private Directory episodeDirectory;
	
	private EpisodeToGraphConverter graphConverter;
	private TransitivelyClosedEpisodes transitivityClosure;
	private EpisodeAsGraphWriter writer;
	private List<Event> events;
	private Set<Episode> episodes;
	private File folderStructure;
	
	private EpisodeGraphGeneratorValidationData sut;
	
	@Before
	public void setup() throws ZipException, IOException {
		initMocks(this);

		graphConverter = new EpisodeToGraphConverter();
		transitivityClosure = new TransitivelyClosedEpisodes();
		writer = new EpisodeAsGraphWriter();
		events = createMapping(eventMethodDeclGeneralAPI("M0"), eventInvocationGeneralAPI("M1"),
				eventMethodDeclGeneralAPI("M2"), eventInvocationGeneralAPI("M3"), eventMethodDeclGeneralAPI("M4"), 
				eventMethodDeclGeneralAPI("M5"), eventMethodDeclGeneralAPI("M6"));
		episodes = createEpisodes();
		
		sut = new EpisodeGraphGeneratorValidationData(rootFolder.getRoot(), contextParser, mappingParser,
				transitivityClosure, writer, graphConverter);
		tmpFolderName = rootFolder.getRoot().getAbsolutePath();
		folderStructure = new File(tmpFolderName + "/graphs/ValidationData/");

		when(mappingParser.parse()).thenReturn(events);
		when(contextParser.parse(eq(events))).thenReturn(episodes);
	}
	
	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Validation data folder does not exist");
		sut = new EpisodeGraphGeneratorValidationData(new File("does not exist"), contextParser, mappingParser,
				transitivityClosure, writer, graphConverter);
	}
	
	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Validation data folder is not a folder, but a file");
		sut = new EpisodeGraphGeneratorValidationData(file, contextParser, mappingParser,
				transitivityClosure, writer, graphConverter);
	}
	
	@Test
	public void structureIsCreated() throws Exception {

		sut.generateGraphs();

		assertTrue(folderStructure.exists());
		assertTrue(folderStructure.isDirectory());

		verify(mappingParser).parse();
		verify(contextParser).parse(events);
	}
	
	@Test
	public void validationGraphStored() throws Exception {

		sut.generateGraphs();
		
		verify(mappingParser).parse();
		verify(contextParser).parse(events);

		File fileName;
		int epCounter = 0;
		for (Episode episode : episodes) {
			List<String> types = getTypes(episode, events);
			for (String type : types) {
				String folder = folderStructure.getAbsolutePath();
				fileName = new File(folder + "/" + type + "/graph" + epCounter + ".dot");
				assertTrue(fileName.exists());
				assertFalse(fileName.isDirectory());
			}
			epCounter++;
		}
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
	
	private Set<Episode> createEpisodes() {
		Set<Episode> episodes = Sets.newHashSet();
		episodes.add(newEpisode("1"));
		episodes.add(newEpisode("2"));
		episodes.add(newEpisode("3"));
		episodes.add(newEpisode("4", "5"));
		episodes.add(newEpisode("3", "4"));
		episodes.add(newEpisode("1", "3", "5"));
		episodes.add(newEpisode("2", "3", "5"));
		episodes.add(newEpisode("1", "2", "3", "6"));

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
}
