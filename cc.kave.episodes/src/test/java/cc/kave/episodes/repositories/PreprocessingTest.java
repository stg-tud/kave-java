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
package cc.kave.episodes.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
import cc.kave.episodes.mining.reader.ReposParser;
import cc.kave.episodes.model.EventStream;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.io.Logger;

public class PreprocessingTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private ReposParser repos;

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

		events = Lists.newArrayList(firstCtx(1), inv(2), inv(3), firstCtx(0), superCtx(2), inv(5), inv(2), firstCtx(1), superCtx(0), inv(3));
		stream = new EventStream();
		stream.addEvent(firstCtx(1));
		stream.addEvent(inv(2));
		stream.addEvent(inv(3));
		stream.addEvent(firstCtx(0));
		stream.addEvent(superCtx(2));
		stream.addEvent(inv(2));
		stream.addEvent(firstCtx(1));
		stream.addEvent(superCtx(0));
		stream.addEvent(inv(3));

		frequencies = Maps.newHashMap();
		frequencies.put(firstCtx(1), 2);
		frequencies.put(inv(2), 2);
		frequencies.put(inv(3), 2);
		frequencies.put(firstCtx(0), 1);
		frequencies.put(superCtx(2), 1);
		frequencies.put(inv(5), 1);
		frequencies.put(superCtx(0), 1);

		sut = new Preprocessing(rootFolder.getRoot(), repos);

		when(repos.learningStream(anyInt())).thenReturn(events);

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
		sut = new Preprocessing(new File("does not exist"), repos);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Events is not a folder, but a file");
		sut = new Preprocessing(file, repos);
	}

	@Test
	public void mockIsCalled() throws ZipException, IOException {
		sut.generate(NUMBREPOS, FREQTHRESH);

		verify(repos).learningStream(anyInt());
	}

	@Test
	public void filesAreCreated() throws IOException {
		sut.generate(NUMBREPOS, FREQTHRESH);

		verify(repos).learningStream(anyInt());

		File streamFile = new File(getStreamPath());
		File mappingFile = new File(getMappingPath());

		assertTrue(streamFile.exists());
		assertTrue(mappingFile.exists());
	}

	@Test
	public void contentTest() throws IOException {
		sut.generate(NUMBREPOS, FREQTHRESH);
//			1			2		3						4				2			1						3
//		firstCtx(1), inv(2), inv(3), firstCtx(0), superCtx(2), inv(5), inv(2), firstCtx(1), superCtx(0), inv(3)

		verify(repos).learningStream(anyInt());

		File streamFile = new File(getStreamPath());
		File mappingFile = new File(getMappingPath());

		String expectedStream = "1,0.000\n2,0.001\n3,0.002\n4,0.503\n2,0.504\n1,1.005\n3,1.006\n";

		List<Event> expectedMapping = Lists.newLinkedList();
		expectedMapping.add(Events.newDummyEvent());
		expectedMapping.add(firstCtx(1));
		expectedMapping.add(inv(2));
		expectedMapping.add(inv(3));
		expectedMapping.add(superCtx(2));

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

	private static Event firstCtx(int i) {
		return Events.newFirstContext(m(i));
	}
	
	private static Event superCtx(int i) {
		return Events.newSuperContext(m(i));
	}

	private static IMethodName m(int i) {
		if (i == 0) {
			return MethodName.UNKNOWN_NAME;
		} else {
			return MethodName.newMethodName("[T,P] [T,P].m" + i + "()");
		}
	}
}
