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
package cc.kave.episodes.aastart.frameworks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Events;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.episodes.export.EventStreamIo;
import cc.kave.episodes.model.EventStream;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;

public class PreprocessingTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private Directory rootDirectory;
	@Mock
	private ReductionByRepos repos;
	
	private static final int NUMBREPOS = 10;
	private static final int FREQTHRESH = 2;

	private List<Event> events;
	private EventStream stream;
	private Map<Event, Integer> frequencies;

	private Preprocessing sut;

	@Before
	public void setup() throws IOException {
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);

		events = Lists.newArrayList(ctx(1), inv(2), inv(3), ctx(4), inv(5), inv(2), ctx(1), inv(3));
		stream = new EventStream();
		stream.addEvent(ctx(1));
		stream.addEvent(inv(2));
		stream.addEvent(inv(3));
		stream.addEvent(unknown());
		stream.addEvent(inv(2));
		stream.addEvent(ctx(1));
		stream.addEvent(inv(3));
		
		frequencies = Maps.newHashMap();
		frequencies.put(ctx(1), 2);
		frequencies.put(inv(2), 2);
		frequencies.put(inv(3), 2);
		frequencies.put(ctx(4), 1);
		frequencies.put(inv(5), 1);
		
		sut = new Preprocessing(rootDirectory, rootFolder.getRoot(), repos);

		when(repos.select(any(Directory.class), anyInt())).thenReturn(events);

		Logger.setPrinting(false);
	}

	@After
	public void teardown() {
		Logger.reset();
	}
	
	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Events folder does not exist");
		sut = new Preprocessing(rootDirectory, new File("does not exist"), repos);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Events is not a folder, but a file");
		sut = new Preprocessing(rootDirectory, file, repos);
	}

	@Test
	public void mockIsCalled() throws ZipException, IOException {
		sut.generate(NUMBREPOS, FREQTHRESH);

		verify(repos).select(any(Directory.class), anyInt());
	}

	@Test
	public void filesAreCreated() throws IOException {
		sut.generate(NUMBREPOS, FREQTHRESH);

		verify(repos).select(any(Directory.class), anyInt());

		File streamFile = new File(getStreamPath());
		File mappingFile = new File(getMappingPath());

		assertTrue(streamFile.exists());
		assertTrue(mappingFile.exists());
	}
	
	@Test
	public void contentTest() throws IOException {
		sut.generate(NUMBREPOS, FREQTHRESH);

		verify(repos).select(any(Directory.class), anyInt());

		File streamFile = new File(getStreamPath());
		File mappingFile = new File(getMappingPath());
		
		// ctx(1), inv(2), inv(3), ctx(4), inv(5), inv(2), ctx(1), inv(3)
		String expectedStream = "1,0.500\n2,0.501\n3,0.502\n2,1.003\n1,1.504\n3,1.505\n";
		
		List<Event> expectedMapping = Lists.newLinkedList();
		expectedMapping.add(Events.newDummyEvent());
		expectedMapping.add(ctx(1));
		expectedMapping.add(inv(2));
		expectedMapping.add(inv(3));
		expectedMapping.add(Events.newFirstContext(MethodName.UNKNOWN_NAME));
		
		String actualStream = FileUtils.readFileToString(streamFile);
		List<Event> actualMapping = EventStreamIo.readMapping(mappingFile.getAbsolutePath());
		
		assertEquals(expectedStream, actualStream);
		assertEquals(expectedMapping, actualMapping);
	}

	private String getStreamPath() {
		File streamFile = new File(rootFolder.getRoot().getAbsolutePath() + "/" + NUMBREPOS + "Repos/stream.txt");
		return streamFile.getAbsolutePath();
	}

	private String getMappingPath() {
		File streamFile = new File(rootFolder.getRoot().getAbsolutePath() + "/" + NUMBREPOS + "Repos/mapping.txt");
		return streamFile.getAbsolutePath();
	}

	private static Event inv(int i) {
		return Events.newInvocation(m(i));
	}

	private static Event ctx(int i) {
		return Events.newContext(m(i));
	}

	private static Event unknown() {
		return Events.newFirstContext(MethodName.UNKNOWN_NAME);
	}

	private static IMethodName m(int i) {
		return MethodName.newMethodName("[T,P] [T,P].m" + i + "()");
	}
}
