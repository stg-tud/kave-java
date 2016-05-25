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
package cc.kave.episodes.export;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Events;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.episodes.statistics.StreamStatistics;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

public class FrameworkDataTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private Directory rootDirectory;
	@Mock
	private EventStreamGenerator generator;
	@Mock
	private EventStreamIo streamer;
	@Mock
	private FileUtils fileUtils;
	@Mock
	private StreamStatistics stats;

	private static final String REPO1 = "Github/usr1/repo1/ws.zip";
	private static final String REPO2 = "Github/usr1/repo2/ws.zip";

	private Map<String, Context> data;
	private Map<String, ReadingArchive> ras;
	Map<Event, Integer> events = Maps.newHashMap();

	private FrameworksData sut;

	@Before
	public void setup() throws IOException {
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);

		data = Maps.newLinkedHashMap();
		ras = Maps.newLinkedHashMap();
		sut = new FrameworksData(rootDirectory, rootFolder.getRoot(), stats);

		SST sst = new SST();
		MethodDeclaration md = new MethodDeclaration();
		md.setName(MethodName.newMethodName("[T,P] [T2,P].M()"));
		// InvocationExpression ie5 = new InvocationExpression();
		// IMethodName methodName5 = MethodName.newMethodName("[System.Void,
		// mscore, 4.0.0.0] [T, P, 1.2.3.4].MI1()");
		// ie5.setMethodName(methodName5);
		// md.getBody().add(wrap(ie5));
		md.getBody().add(new ContinueStatement());
		sst.getMethods().add(md);

		MethodDeclaration md2 = new MethodDeclaration();
		md2.setName(MethodName.newMethodName("[T,P] [T3,P].M2()"));

		InvocationExpression ie1 = new InvocationExpression();
		IMethodName methodName = MethodName.newMethodName("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI1()");
		ie1.setMethodName(methodName);
		InvocationExpression ie2 = new InvocationExpression();
		IMethodName methodName2 = MethodName.newMethodName("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI2()");
		ie2.setMethodName(methodName2);

		md2.getBody().add(wrap(ie1));
		md2.getBody().add(wrap(ie2));
		md2.getBody().add(wrap(ie2));
		sst.getMethods().add(md2);

		Context context = new Context();
		context.setSST(sst);
		data.put(REPO1, context);

		when(rootDirectory.findFiles(anyPredicateOf(String.class))).thenAnswer(new Answer<Set<String>>() {
			@Override
			public Set<String> answer(InvocationOnMock invocation) throws Throwable {
				return data.keySet();
			}
		});
		when(rootDirectory.getReadingArchive(anyString())).then(new Answer<ReadingArchive>() {
			@Override
			public ReadingArchive answer(InvocationOnMock invocation) throws Throwable {
				String file = (String) invocation.getArguments()[0];
				ReadingArchive ra = mock(ReadingArchive.class);
				ras.put(file, ra);
				when(ra.hasNext()).thenReturn(true).thenReturn(false);
				when(ra.getNext(Context.class)).thenReturn(data.get(file));
				return ra;
			}
		});
		when(stats.getFrequences(anyList())).thenReturn(expectedFreqs());

		Logger.setPrinting(false);
	}

	@After
	public void teardown() {
		Logger.reset();
	}

	@Ignore
	@Test
	public void contextTest() throws ZipException, IOException {
		sut.getFrameworks();

		verify(rootDirectory, times(2)).findFiles(anyPredicateOf(String.class));
		verify(rootDirectory, times(2)).getReadingArchive(REPO1);
		verify(ras.get(REPO1), times(2)).hasNext();
		verify(ras.get(REPO1)).getNext(Context.class);
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Data folder does not exist");
		sut = new FrameworksData(rootDirectory, new File("does not exist"), stats);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Data folder is not a folder, but a file");
		sut = new FrameworksData(rootDirectory, file, stats);
	}

	@Ignore
	@Test
	public void readTwoArchives() throws IOException {
		SST sst = new SST();
		MethodDeclaration md3 = new MethodDeclaration();
		md3.setName(MethodName.newMethodName("[T,P] [T2,P].M3()"));
		md3.getBody().add(new DoLoop());

		InvocationExpression ie3 = new InvocationExpression();
		IMethodName methodName = MethodName.newMethodName("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI3()");
		ie3.setMethodName(methodName);

		md3.getBody().add(wrap(ie3));

		InvocationExpression ie4 = new InvocationExpression();
		methodName = MethodName.newMethodName("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI3()");
		ie4.setMethodName(methodName);

		md3.getBody().add(wrap(ie4));

		md3.getBody().add(new ExpressionStatement());

		sst.getMethods().add(md3);
		Context context = new Context();
		context.setSST(sst);

		data.put(REPO2, context);

		sut.getFrameworks();

		verify(rootDirectory, times(2)).findFiles(anyPredicateOf(String.class));
		verify(rootDirectory, times(2)).getReadingArchive(REPO1);
		verify(rootDirectory, times(2)).getReadingArchive(REPO2);

		verify(ras.get(REPO1), times(2)).hasNext();
		verify(ras.get(REPO1)).getNext(Context.class);
		verify(ras.get(REPO2), times(2)).hasNext();
		verify(ras.get(REPO2)).getNext(Context.class);

		File streamFile = new File(getStreamPath());
		File partitionFile1 = new File(getStreamPartitionPath(1));
		File partitionFile2 = new File(getStreamPartitionPath(2));
		File mappingFile = new File(getMappingPath());

		String expectedStream = "2,0.500\n2,0.501\n3,1.002\n3,1.003\n";

		String expectedPartition1 = "1,0.000\n2,0.001\n2,0.002\n";
		String expectedPartition2 = "1,0.000\n3,0.001\n3,0.002\n";

		// ctx1 ctx2 inv1 inv2 inv2 ctx2 inv3 inv3
		String actualStream = FileUtils.readFileToString(streamFile);
		List<Event> actualMapping = EventStreamIo.readMapping(mappingFile.getAbsolutePath());
		String actualStream1 = FileUtils.readFileToString(partitionFile1);
		String actualStream2 = FileUtils.readFileToString(partitionFile2);
		
		Map<String, Integer> expectedMapper = Maps.newLinkedHashMap();
		expectedMapper.put("Github/usr1/repo1", 1);
		expectedMapper.put("Github/usr1/repo2", 2);

		assertEquals(expectedStream, actualStream);
		assertEquals(expectedFreqs(), actualMapping);
		assertEquals(expectedPartition1, actualStream1);
		assertEquals(expectedPartition2, actualStream2);
		
		assertTrue(streamFile.exists());
		assertTrue(mappingFile.exists());
		assertTrue(partitionFile1.exists());
		assertTrue(partitionFile2.exists());
	}

	private <T> Predicate<T> anyPredicateOf(Class<T> clazz) {
		@SuppressWarnings("unchecked")
		Predicate<T> p = any(Predicate.class);
		return p;
	}

	private static ExpressionStatement wrap(InvocationExpression ie1) {
		ExpressionStatement expressionStatement = new ExpressionStatement();
		expressionStatement.setExpression(ie1);
		return expressionStatement;
	}

	private String getStreamPath() {
		File streamFile = new File(rootFolder.getRoot().getAbsolutePath() + "/patterns/eventStream.txt");
		return streamFile.getAbsolutePath();
	}

	private String getMappingPath() {
		File streamFile = new File(rootFolder.getRoot().getAbsolutePath() + "/patterns/eventMapping.txt");
		return streamFile.getAbsolutePath();
	}

	private String getStreamPartitionPath(int number) {
		File streamFile = new File(
				rootFolder.getRoot().getAbsolutePath() + "/patterns/partitions/eventStream" + number + ".txt");
		return streamFile.getAbsolutePath();
	}

	private Map<Event, Integer> expectedFreqs() {
		Map<Event, Integer> events = Maps.newHashMap();
		
		String ctx1 = "[T,P] [T3,P].M2()";
		IMethodName methodCtx1 = MethodName.newMethodName(ctx1);
		Event c1 = Events.newContext(methodCtx1);
		c1.setKind(EventKind.METHOD_DECLARATION);
		events.put(c1, 1);
		
		String inv1 = "[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI1()";
		IMethodName methodInv1 = MethodName.newMethodName(inv1);
		Event e1 = Events.newInvocation(methodInv1);
		events.put(e1, 1);
		
		String inv2 = "[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI2()";
		IMethodName methodInv2 = MethodName.newMethodName(inv2);
		Event e2 = Events.newInvocation(methodInv2);
		events.put(e2, 2);
		
		String ctx2 = "[T,P] [T2,P].M3()";
		IMethodName methodCtx2 = MethodName.newMethodName(ctx2);
		Event c2 = Events.newContext(methodCtx2);
		c2.setKind(EventKind.METHOD_DECLARATION);
		events.put(c2, 1);
		
		String inv3 = "[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI3()";
		IMethodName methodInv3 = MethodName.newMethodName(inv3);
		Event e3 = Events.newInvocation(methodInv3);
		events.put(e3, 2);
		
		return events;
	}
}
