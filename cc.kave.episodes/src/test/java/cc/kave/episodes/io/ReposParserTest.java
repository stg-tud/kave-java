///**
// * Copyright 2016 Technische Universität Darmstadt
// * 
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * 
// *     http://www.apache.org/licenses/LICENSE-2.0
// * 
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package cc.kave.episodes.io;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.zip.ZipException;
//
//import org.apache.commons.io.FileUtils;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.junit.rules.TemporaryFolder;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;
//
//import com.google.common.base.Predicate;
//import com.google.common.collect.Maps;
//
//import cc.kave.commons.model.events.completionevents.Context;
//import cc.kave.commons.model.naming.Names;
//import cc.kave.commons.model.naming.codeelements.IMethodName;
//import cc.kave.commons.model.ssts.impl.SST;
//import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
//import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
//import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
//import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
//import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
//import cc.kave.episodes.io.FileReader;
//import cc.kave.episodes.io.ReposParser;
//import cc.kave.episodes.model.events.Event;
//import cc.kave.episodes.model.events.Events;
//import cc.recommenders.exceptions.AssertionException;
//import cc.recommenders.io.Directory;
//import cc.recommenders.io.Logger;
//import cc.recommenders.io.ReadingArchive;
//
//public class ReposParserTest {
//
//	@Rule
//	public TemporaryFolder rootFolder = new TemporaryFolder();
//	@Rule
//	public ExpectedException thrown = ExpectedException.none();
//
//	@Mock
//	private Directory rootDirectory;
//	@Mock
//	FileReader reader;
//
//	private static final String REPO1 = "Github/usr1/repo1/ws.zip";
//	private static final String REPO3 = "Github/usr1/repo3/ws.zip";
//	private static final String REPO2 = "Github/usr1/repo2/ws.zip";
//	private static final int NUMBEROFREPOS = 2;
//
//	private Map<String, Context> data;
//	private Map<String, ReadingArchive> ras;
//	private List<String> repositories;
//
//	private ReposParser sut;
//
//	@Before
//	public void setup() throws IOException {
//		Logger.reset();
//		Logger.setCapturing(true);
//
//		MockitoAnnotations.initMocks(this);
//
//		data = Maps.newLinkedHashMap();
//		ras = Maps.newLinkedHashMap();
//		sut = new ReposParser(rootDirectory, rootFolder.getRoot(), reader);
//
//		SST sst = new SST();
//		MethodDeclaration md = new MethodDeclaration();
//		md.setName(Names.newMethod("[T,P] [T2,P].M()"));
//		md.getBody().add(new ContinueStatement());
//		sst.getMethods().add(md);
//
//		MethodDeclaration md2 = new MethodDeclaration();
//		md2.setName(Names.newMethod("[T,P] [T3,P].M2()"));
//
//		InvocationExpression ie1 = new InvocationExpression();
//		IMethodName methodName = Names.newMethod("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI1()");
//		ie1.setMethodName(methodName);
//		InvocationExpression ie2 = new InvocationExpression();
//		IMethodName methodName2 = Names.newMethod("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI2()");
//		ie2.setMethodName(methodName2);
//
//		md2.getBody().add(wrap(ie1));
//		md2.getBody().add(wrap(ie2));
//		md2.getBody().add(wrap(ie2));
//		sst.getMethods().add(md2);
//
//		Context context = new Context();
//		context.setSST(sst);
//		data.put(REPO1, context);
//
//		SST sst3 = new SST();
//		MethodDeclaration md3 = new MethodDeclaration();
//		md3.setName(Names.newMethod("[T,P] [T2,P].M()"));
//		InvocationExpression ie5 = new InvocationExpression();
//		IMethodName methodName5 = Names.newMethod("[System.Void,mscore, 4.0.0.0] [T, P, 1.2.3.4].MI1()");
//		ie5.setMethodName(methodName5);
//		md3.getBody().add(wrap(ie5));
//		sst3.getMethods().add(md3);
//
//		Context context3 = new Context();
//		context3.setSST(sst3);
//		data.put(REPO3, context3);
//
//		SST sst2 = new SST();
//		MethodDeclaration md4 = new MethodDeclaration();
//		md4.setName(Names.newMethod("[T,P] [T2,P].M3()"));
//		md4.getBody().add(new DoLoop());
//
//		InvocationExpression ie3 = new InvocationExpression();
//		IMethodName methodName3 = Names.newMethod("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI3()");
//		ie3.setMethodName(methodName3);
//
//		InvocationExpression ie4 = new InvocationExpression();
//		methodName3 = Names.newMethod("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI3()");
//		ie4.setMethodName(methodName3);
//
//		md4.getBody().add(wrap(ie3));
//		md4.getBody().add(wrap(ie4));
//		md4.getBody().add(new ExpressionStatement());
//
//		sst2.getMethods().add(md4);
//		Context context2 = new Context();
//		context2.setSST(sst2);
//
//		data.put(REPO2, context2);
//
//		repositories = new LinkedList<>();
//		repositories.add("Github/usr1/repo1");
//		repositories.add("Github/usr1/repo3");
//
//		when(rootDirectory.findFiles(anyPredicateOf(String.class))).thenAnswer(new Answer<Set<String>>() {
//			@Override
//			public Set<String> answer(InvocationOnMock invocation) throws Throwable {
//				return data.keySet();
//			}
//		});
//		when(rootDirectory.getReadingArchive(anyString())).then(new Answer<ReadingArchive>() {
//			@Override
//			public ReadingArchive answer(InvocationOnMock invocation) throws Throwable {
//				String file = (String) invocation.getArguments()[0];
//				ReadingArchive ra = mock(ReadingArchive.class);
//				ras.put(file, ra);
//				when(ra.hasNext()).thenReturn(true).thenReturn(false);
//				when(ra.getNext(Context.class)).thenReturn(data.get(file));
//				return ra;
//			}
//		});
//		when(reader.readFile(any(File.class))).thenReturn(repositories);
//
//		Logger.setPrinting(false);
//	}
//
//	@After
//	public void teardown() {
//		Logger.reset();
//	}
//
//	@Test
//	public void cannotBeInitializedWithNonExistingFolder() {
//		thrown.expect(AssertionException.class);
//		thrown.expectMessage("Events folder does not exist");
//		sut = new ReposParser(rootDirectory, new File("does not exist"), reader);
//	}
//
//	@Test
//	public void cannotBeInitializedWithFile() throws IOException {
//		File file = rootFolder.newFile("a");
//		thrown.expect(AssertionException.class);
//		thrown.expectMessage("Events is not a folder, but a file");
//		sut = new ReposParser(rootDirectory, file, reader);
//	}
//
//	@Test
//	public void filesAreCreated() throws IOException {
//		sut.learningStream(NUMBEROFREPOS);
//
//		File fileName = new File(getReposPath());
//
//		assertTrue(fileName.exists());
//	}
//
//	@Test
//	public void contentTest() throws IOException {
//		sut.learningStream(NUMBEROFREPOS);
//
//		File fileName = new File(getReposPath());
//
//		StringBuilder expected = new StringBuilder();
//		expected.append("Github/usr1/repo1\n");
//		expected.append("Github/usr1/repo3\n");
//
//		String actuals = FileUtils.readFileToString(fileName);
//
//		assertEquals(expected.toString(), actuals);
//	}
//
//	@Test
//	public void contextTest() throws ZipException, IOException {
//		sut.learningStream(NUMBEROFREPOS);
//
//		verify(rootDirectory).findFiles(anyPredicateOf(String.class));
//		verify(rootDirectory).getReadingArchive(REPO1);
//		verify(rootDirectory).getReadingArchive(REPO3);
//		verify(ras.get(REPO1), times(2)).hasNext();
//		verify(ras.get(REPO1)).getNext(Context.class);
//		verify(ras.get(REPO3), times(2)).hasNext();
//		verify(ras.get(REPO3)).getNext(Context.class);
//	}
//
//	@Test
//	public void readThreeArchives() throws IOException {
//		List<Event> expectedEvents = getExpectedEvents();
//		List<Event> actualEvents = sut.learningStream(NUMBEROFREPOS);
//
//		verify(rootDirectory).findFiles(anyPredicateOf(String.class));
//		verify(rootDirectory).getReadingArchive(REPO1);
//		verify(rootDirectory).getReadingArchive(REPO3);
//
//		verify(ras.get(REPO1), times(2)).hasNext();
//		verify(ras.get(REPO1)).getNext(Context.class);
//		verify(ras.get(REPO3), times(2)).hasNext();
//		verify(ras.get(REPO3)).getNext(Context.class);
//
//		assertEquals(expectedEvents.size(), actualEvents.size());
//		assertEquals(expectedEvents, actualEvents);
//	}
//
//	@Test
//	public void validationArchives() throws IOException {
//		List<Event> expectedEvents = new LinkedList<Event>();
//
//		IMethodName declName = Names.newMethod("[?] [?].???()");
//		Event firstMethod = Events.newFirstContext(declName);
//		Event enclosingMethod = Events.newContext(declName);
//		expectedEvents.add(firstMethod);
//		expectedEvents.add(enclosingMethod);
//
//		IMethodName invName = Names.newMethod("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI3()");
//		Event invocation = Events.newInvocation(invName);
//		expectedEvents.add(invocation);
//		expectedEvents.add(invocation);
//
//		List<Event> actualEvents = sut.validationStream(NUMBEROFREPOS);
//
//		verify(rootDirectory).findFiles(anyPredicateOf(String.class));
//		verify(rootDirectory).getReadingArchive(REPO2);
//
//		verify(ras.get(REPO2), times(2)).hasNext();
//		verify(ras.get(REPO2)).getNext(Context.class);
//
//		verify(reader).readFile(any(File.class));
//
//		assertEquals(expectedEvents.size(), actualEvents.size());
//		assertEquals(expectedEvents, actualEvents);
//	}
//
//	private <T> Predicate<T> anyPredicateOf(Class<T> clazz) {
//		@SuppressWarnings("unchecked")
//		Predicate<T> p = any(Predicate.class);
//		return p;
//	}
//
//	private static ExpressionStatement wrap(InvocationExpression ie1) {
//		ExpressionStatement expressionStatement = new ExpressionStatement();
//		expressionStatement.setExpression(ie1);
//		return expressionStatement;
//	}
//
//	private List<Event> getExpectedEvents() {
//		List<Event> events = new LinkedList<Event>();
//
//		String md1 = "[?] [?].???()";
//		IMethodName methodDecl1 = Names.newMethod(md1);
//		Event e1 = Events.newFirstContext(methodDecl1);
//		events.add(e1);
//		events.add(Events.newContext(methodDecl1));
//
//		String inv1 = "[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI1()";
//		IMethodName methodInv1 = Names.newMethod(inv1);
//		Event e2 = Events.newInvocation(methodInv1);
//		events.add(e2);
//
//		String inv2 = "[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI2()";
//		IMethodName methodInv2 = Names.newMethod(inv2);
//		Event e3 = Events.newInvocation(methodInv2);
//		events.add(e3);
//		events.add(e3);
//
//		events.add(e1);
//		events.add(Events.newContext(methodDecl1));
//
//		String inv3 = "[System.Void,mscore, 4.0.0.0] [T, P, 1.2.3.4].MI1()";
//		IMethodName methodInv3 = Names.newMethod(inv3);
//		Event e5 = Events.newInvocation(methodInv3);
//		events.add(e5);
//
//		return events;
//	}
//
//	private String getReposPath() {
//		String fileName = rootFolder.getRoot().getAbsolutePath() + "/" + NUMBEROFREPOS + "Repos/repositories.txt";
//		return fileName;
//	}
//}