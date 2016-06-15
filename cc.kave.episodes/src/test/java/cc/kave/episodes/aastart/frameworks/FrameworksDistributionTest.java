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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Maps;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Events;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.episodes.statistics.StreamStatistics;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;

public class FrameworksDistributionTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Mock
	private Directory rootDirectory;
	@Mock
	private ReductionByRepos repos;
	@Mock 
	private StreamStatistics statistics;
	
	private static final int NUMOFREPOS = 100;
	
	private List<Event> events = new LinkedList<Event>();
	
	private FrameworksDistribution sut;
	
	@Before 
	public void setup() throws ZipException, IOException {
		Logger.reset();
		Logger.setCapturing(true);
		
		MockitoAnnotations.initMocks(this);
		
		sut = new FrameworksDistribution(rootDirectory, rootFolder.getRoot(), repos, statistics);
		
		Event ed1 = createDeclaration("[s:System.DateTime, mscorlib, 4.0.0.0] [s:System.DateTime, mscorlib, 4.0.0.0].FromBinary([System.Int64, mscorlib, 4.0.0.0] dateData)");
		Event ed2 = createDeclaration("[System.Void, mscorlib, 4.0.0.0] [System.NotSupportedException, mscorlib, 4.0.0.0]..ctor()");
		
		Event ei1 = createInvocation("[System.String, mscorlib, 4.0.0.0] [System.String, mscorlib, 4.0.0.0].ToUpper()");
		Event ei2 = createInvocation("[Some.Framework] [Some.Framework].ToString()");
		Event ei3 = createInvocation("[TResult] [s:System.Runtime.CompilerServices.TaskAwaiter`1[[TResult]], mscorlib, 4.0.0.0].GetResult()");
		
		events.add(ed1);
		events.add(ei1);
		events.add(ei2);
		events.add(ei3);
		events.add(ed2);
		events.add(ei3);
		events.add(ei2);
		events.add(ei1);
		
		when(repos.select(any(Directory.class), anyInt())).thenReturn(events);
		
		Map<Event, Integer> frequencies = Maps.newHashMap();
		frequencies.put(ed1, 1);
		frequencies.put(ei1, 2);
		frequencies.put(ei2, 2);
		frequencies.put(ei3, 2);
		frequencies.put(ed2, 1);
		
		when(statistics.getFrequences(any(List.class))).thenReturn(frequencies);

		Logger.setPrinting(false);
	}
	
	@Test
	public void cannotBeInitializedWithNonExistingFolder() throws ZipException, IOException {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Statistics folder does not exist");
		sut = new FrameworksDistribution(rootDirectory, new File("does not exist"), repos, statistics);
		
		verify(repos).select(eq(rootDirectory), eq(NUMOFREPOS));
		verify(statistics).getFrequences(eq(events));
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Statistics is not a folder, but a file");
		sut = new FrameworksDistribution(rootDirectory, file, repos, statistics);
		
		verify(repos).select(eq(rootDirectory), eq(NUMOFREPOS));
		verify(statistics).getFrequences(eq(events));
	}
	
	@Test
	public void filesCreated() throws IOException {
		sut.getDistribution();
		
		verify(repos).select(eq(rootDirectory), eq(NUMOFREPOS));
		verify(statistics).getFrequences(eq(events));
		
		assertTrue(new File(getEventsFile()).exists());
		assertTrue(new File(getTypesFile()).exists());
	}
	
	@Test
	public void filesContent() throws IOException {
		sut.getDistribution();
		
		verify(repos).select(eq(rootDirectory), eq(NUMOFREPOS));
		verify(statistics).getFrequences(eq(events));
		
		StringBuilder expEvents = new StringBuilder();
		expEvents.append("mscorlib, 4.0.0.0\t2\t4\n");
		
		StringBuilder expTypes = new StringBuilder();
		expTypes.append("mscorlib, 4.0.0.0\t2\n");
		
		File eventsFile = new File(getEventsFile());
		File typesFile = new File(getTypesFile());
		
		String actEvents = FileUtils.readFileToString(eventsFile);
		String actTypes = FileUtils.readFileToString(typesFile);
		
		assertEquals(expEvents.toString(), actEvents);
		assertEquals(expTypes.toString(), actTypes);
	}
	
	private String getEventsFile() {
		String eventsPath = rootFolder.getRoot().getAbsolutePath() + "/100Repos/eventsPerFramework100.txt";
		return eventsPath;
	}
	
	private String getTypesFile() {
		String typesPath = rootFolder.getRoot().getAbsolutePath() + "/100Repos/typesPerFramework100.txt";
		return typesPath;
	}
	
	private Event createDeclaration(String name) {
		IMethodName methodDecl = MethodName.newMethodName(name);
		Event event = Events.newContext(methodDecl);
		return event;
	}
	
	private Event createInvocation(String name) {
		IMethodName methodInv = MethodName.newMethodName(name);
		Event event = Events.newInvocation(methodInv);
		return event;
	}
}
