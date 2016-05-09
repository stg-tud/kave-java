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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
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
import cc.kave.episodes.model.EventStream;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

public class StreamPartitionTest {

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

	private static final String REPO1 = "Github/repo1/usr1/ws";
	private static final String REPO2 = "Github/repo1/usr2/ws";

	private Map<String, Context> data;
	private Map<String, ReadingArchive> ras;

	private StreamPartition sut;

	@Before
	public void setup() throws IOException {
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);

		data = Maps.newHashMap();
		ras = Maps.newHashMap();
		sut = new StreamPartition(rootDirectory, rootFolder.getRoot());

		SST sst = new SST();
		MethodDeclaration md = new MethodDeclaration();
		md.setName(MethodName.newMethodName("[T,P] [T2,P].M()"));
//		InvocationExpression ie5 = new InvocationExpression();
//		IMethodName methodName5 = MethodName.newMethodName("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI1()");
//		ie5.setMethodName(methodName5);
//		md.getBody().add(wrap(ie5));
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

		Logger.setPrinting(false);
	}

	@After
	public void teardown() {
		Logger.reset();
	}

	@Test
	public void contextTest() throws ZipException, IOException {
		sut.partition();

		verify(rootDirectory, times(2)).findFiles(anyPredicateOf(String.class));
		verify(rootDirectory, times(2)).getReadingArchive(REPO1);
		verify(ras.get(REPO1), times(2)).hasNext();
		verify(ras.get(REPO1)).getNext(Context.class);
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Contexts folder does not exist");
		sut = new StreamPartition(rootDirectory, new File("does not exist"));
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Contexts is not a folder, but a file");
		sut = new StreamPartition(rootDirectory, file);
	}

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

		sut.partition();

		verify(rootDirectory, times(2)).findFiles(anyPredicateOf(String.class));
		verify(rootDirectory, times(2)).getReadingArchive(REPO1);
		verify(rootDirectory, times(2)).getReadingArchive(REPO2);

		verify(ras.get(REPO1), times(2)).hasNext();
		verify(ras.get(REPO1)).getNext(Context.class);
		verify(ras.get(REPO2), times(2)).hasNext();
		verify(ras.get(REPO2)).getNext(Context.class);

		File streamFile1 = new File(getStreamPath(1));
		File mappingFile1 = new File(getMappingPath(1));
		
		File streamFile2 = new File(getStreamPath(2));
		File mappingFile2 = new File(getMappingPath(2));

		EventStream expected1 = new EventStream();
		
		Event dummy = new Event();
		dummy.createDummyEvent();
		expected1.addEvent(dummy);
		
		Event ctx3 = new Event();
		ctx3.setMethod(MethodName.newMethodName("[T,P] [T2,P].M3()"));
		expected1.addEvent(ctx3);

		Event inv3 = new Event();
		inv3.setKind(EventKind.INVOCATION);
		inv3.setMethod(MethodName.newMethodName("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI3()"));
		expected1.addEvent(inv3);
		expected1.addEvent(inv3);
		
		EventStream expected2 = new EventStream();
		expected2.addEvent(dummy);
		
		Event ctx2 = new Event();
		ctx2.setMethod(MethodName.newMethodName("[T,P] [T3,P].M2()"));
		expected2.addEvent(ctx2);

		Event inv2 = new Event();
		inv2.setKind(EventKind.INVOCATION);
		inv2.setMethod(MethodName.newMethodName("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI2()"));
		expected2.addEvent(inv2);
		expected2.addEvent(inv2);
		
		// ctx1 ctx2 inv1 inv2 inv2 ctx2 inv3 inv3

		String actualStream1 = FileUtils.readFileToString(streamFile1);
		String actualMapping1 = FileUtils.readFileToString(mappingFile1);
		
		String actualStream2 = FileUtils.readFileToString(streamFile2);
		String actualMapping2 = FileUtils.readFileToString(mappingFile2);

		assertEquals(expected1.getStream(), actualStream1);
		assertEquals(getMapping(1), actualMapping1);
		
		assertEquals(expected2.getStream(), actualStream2);
		assertEquals(getMapping(2), actualMapping2);
	}

	@Test
	public void filesCreated() throws IOException {
		sut.partition();

		File streamFile1 = new File(getStreamPath(1));
		File mappingFile1 = new File(getMappingPath(1));
		
		assertTrue(streamFile1.exists());
		assertTrue(mappingFile1.exists());
	}

	@Test
	public void filesContentTest() throws IOException {
		sut.partition();

		File streamFile1 = new File(getStreamPath(1));
		File mappingFile1 = new File(getMappingPath(1));
		
		EventStream expected = new EventStream();
		
		Event dummy = new Event();
		dummy.createDummyEvent();
		expected.addEvent(dummy);
		expected.addEvent(Events.newHolder());

		Event inv2 = new Event();
		inv2.setKind(EventKind.INVOCATION);
		inv2.setMethod(MethodName.newMethodName("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI2()"));
		expected.addEvent(inv2);
		expected.addEvent(inv2);

		// ctx1 ctx2 inv1 inv2 inv2

		String actualStream = FileUtils.readFileToString(streamFile1);
		String actualMapping = FileUtils.readFileToString(mappingFile1);

		assertEquals(expected.getStream(), actualStream);
		assertEquals(getMapping(), actualMapping);
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

	private String getStreamPath(int number) {
		File streamFile = new File(rootFolder.getRoot().getAbsolutePath() + "/partition" + number + "/eventStream.txt");
		return streamFile.getAbsolutePath();
	}

	private String getMappingPath(int number) {
		File mappingFile = new File(rootFolder.getRoot().getAbsolutePath() + "/partition" + number + "/eventMapping.txt");
		return mappingFile.getAbsolutePath();
	}
	
	private String getMapping(int number) {
		if (number == 1) {
			StringBuilder mappingBuilder = new StringBuilder();
			mappingBuilder.append("[{\"Kind\":0,\"Method\":\"CSharp.MethodName:[You, Can] [Safely, Ignore].ThisDummyValue()\"},");
			mappingBuilder.append("{\"Kind\":0,\"Method\":\"CSharp.MethodName:[?] [?].???()\"},");
			mappingBuilder.append("{\"Kind\":1,\"Method\":\"CSharp.MethodName:[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI3()\"}]");
			
			return mappingBuilder.toString();
		}
		StringBuilder mappingBuilder = new StringBuilder();
		mappingBuilder.append("[{\"Kind\":0,\"Method\":\"CSharp.MethodName:[You, Can] [Safely, Ignore].ThisDummyValue()\"},");
		mappingBuilder.append("{\"Kind\":0,\"Method\":\"CSharp.MethodName:[?] [?].???()\"},");
		mappingBuilder.append("{\"Kind\":1,\"Method\":\"CSharp.MethodName:[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI2()\"}]");
		
		return mappingBuilder.toString();
	}
	
	private String getMapping() {
		StringBuilder mappingBuilder = new StringBuilder();
		mappingBuilder.append("[{\"Kind\":0,\"Method\":\"CSharp.MethodName:[You, Can] [Safely, Ignore].ThisDummyValue()\"},");
		mappingBuilder.append("{\"Kind\":1,\"Method\":\"CSharp.MethodName:[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI2()\"}]");
		
		return mappingBuilder.toString();
	}
}
