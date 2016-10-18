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
package cc.kave.episodes.preprocessing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.utils.TypeErasure;
import cc.kave.episodes.export.EventStreamIo;
import cc.kave.episodes.io.IndivReposParser;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ReposPreprocessTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private IndivReposParser repos;

	private Map<String, List<Event>> reposEvents;

	private File repoFile0;
	private File repoFile1;
	private File streamFile0;
	private File streamFile1;
	private File mappingFile0;
	private File mappingFile1;

	private static final int FREQTHRESH = 2;

	private ReposPreprocess sut;

	@Before
	public void setup() throws IOException {
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);

		repoFile0 = new File(getRepoPath(0));
		repoFile1 = new File(getRepoPath(1));
		streamFile0 = new File(getStreamPath(0));
		streamFile1 = new File(getStreamPath(1));
		mappingFile0 = new File(getMappingPath(0));
		mappingFile1 = new File(getMappingPath(1));

		reposEvents = Maps.newLinkedHashMap();
		reposEvents.put("Github/usr1/repo0", Lists.newArrayList(firstCtx(1),
				enclosingCtx(6), inv(2), inv(3), inv(2), inv(3), firstCtx(1),
				superCtx(0), enclosingCtx(8), inv(3), inv(2), inv(3), inv(3)));
		reposEvents.put("Github/usr1/repo1", Lists.newArrayList(firstCtx(0),
				superCtx(2), enclosingCtx(7), inv(5), inv(2), inv(2), inv(5),
				firstCtx(1), enclosingCtx(5), inv(2), inv(5)));

		sut = new ReposPreprocess(rootFolder.getRoot(), repos);

		when(repos.generateReposEvents()).thenReturn(reposEvents);

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
		sut = new ReposPreprocess(new File("does not exist"), repos);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Events is not a folder, but a file");
		sut = new ReposPreprocess(file, repos);
	}

	@Test
	public void mockIsCalled() throws ZipException, IOException {
		sut.generate(FREQTHRESH);

		verify(repos).generateReposEvents();
	}

	@Test
	public void filesAreCreated() throws IOException {
		sut.generate(FREQTHRESH);

		verify(repos).generateReposEvents();

		assertTrue(repoFile0.exists());
		assertTrue(repoFile1.exists());

		assertTrue(streamFile0.exists());
		assertTrue(streamFile1.exists());

		assertTrue(mappingFile0.exists());
		assertTrue(mappingFile1.exists());
	}
	
	@Test
	public void contentTest() throws IOException {
		sut.generate(FREQTHRESH);

		verify(repos).generateReposEvents();

		String expRepo0 = "Github/usr1/repo0";
		String expRepo1 = "Github/usr1/repo1";

		String expStream0 = "1,0.000\n2,0.001\n3,0.002\n4,0.003\n3,0.004\n4,0.005\n1,0.506\n5,0.507\n4,0.508\n3,0.509\n4,0.510\n4,0.511\n";
		String expStream1 = "1,0.000\n2,0.001\n3,0.002\n3,0.003\n2,0.004\n4,0.505\n5,0.506\n3,0.507\n2,0.508\n";

		List<Event> expMapping0 = Lists.newLinkedList();
		expMapping0.add(dummy());
		expMapping0.add(firstCtx(1));
		expMapping0.add(enclosingCtx(6));
		expMapping0.add(inv(2));
		expMapping0.add(inv(3));
		expMapping0.add(enclosingCtx(8));

		List<Event> expMapping1 = Lists.newLinkedList();
		expMapping1.add(dummy());
		expMapping1.add(enclosingCtx(7));
		expMapping1.add(inv(5));
		expMapping1.add(inv(2));
		expMapping1.add(firstCtx(1));
		expMapping1.add(enclosingCtx(5));

		String actRepo0 = FileUtils.readFileToString(new File(getRepoPath(0)));
		String actRepo1 = FileUtils.readFileToString(new File(getRepoPath(1)));

		String actStream0 = FileUtils.readFileToString(new File(
				getStreamPath(0)));
		String actStream1 = FileUtils.readFileToString(new File(
				getStreamPath(1)));

		List<Event> actMapping0 = EventStreamIo.readMapping(getMappingPath(0));
		List<Event> actMapping1 = EventStreamIo.readMapping(getMappingPath(1));

		assertEquals(expRepo0, actRepo0);
		assertEquals(expRepo1, actRepo1);

		assertEquals(expStream0, actStream0);
		assertEquals(expStream1, actStream1);

		assertEquals(expMapping0, actMapping0);
		assertEquals(expMapping1, actMapping1);
	}

	private String getRepoPath(int repoID) {
		String path = getPath(repoID);
		String repoPath = path + "/repository.txt";
		return repoPath;
	}

	private String getStreamPath(int repoID) {
		String path = getPath(repoID);
		String streamFile = path + "/stream.txt";
		return streamFile;
	}

	private String getMappingPath(int repoID) {
		String path = getPath(repoID);
		String streamFile = path + "/mapping.txt";
		return streamFile;
	}

	private String getPath(int repoID) {
		String path = rootFolder.getRoot().getAbsolutePath() + "/repo" + repoID;
		return path;
	}

	private static Event inv(int i) {
		return Events.newInvocation(m(i));
	}

	private static Event firstCtx(int i) {
		return Events.newFirstContext(TypeErasure.of(m(i)));
	}

	private static Event superCtx(int i) {
		return Events.newSuperContext(TypeErasure.of(m(i)));
	}

	private static Event enclosingCtx(int i) {
		return Events.newContext(TypeErasure.of(m(i)));
	}

	private static Event dummy() {
		return Events.newDummyEvent();
	}

	private static IMethodName m(int i) {
		if (i == 0) {
			return Names.getUnknownMethod();
		} else {
			return Names
					.newMethod("[T,P, 1.2.3.4] [T,P, 1.2.3.4].m" + i + "()");
		}
	}
}