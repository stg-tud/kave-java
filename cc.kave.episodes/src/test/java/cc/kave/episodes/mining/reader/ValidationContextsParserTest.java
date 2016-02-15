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
package cc.kave.episodes.mining.reader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.episodes.model.Episode;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

public class ValidationContextsParserTest {

	@Mock
	private Directory rootDirectory;

	private ValidationContextsParser sut;

	private Map<String, Context> data;
	private Map<String, ReadingArchive> ras;

	@Before
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);
		data = Maps.newHashMap();
		ras = Maps.newHashMap();
		sut = new ValidationContextsParser(rootDirectory);

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

		SST sst = new SST();
		MethodDeclaration md = new MethodDeclaration();
		md.setName(MethodName.newMethodName("[T,P] [T2,P].M()"));
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

		sst.getMethods().add(md2);
		Context context = new Context();
		context.setSST(sst);
		data.put("a", context);

		Logger.setPrinting(false);
	}

	@After
	public void teardown() {
		Logger.setPrinting(false);
	}

	private static ExpressionStatement wrap(InvocationExpression ie1) {
		ExpressionStatement expressionStatement = new ExpressionStatement();
		expressionStatement.setExpression(ie1);
		return expressionStatement;
	}

	@Test
	public void contextTest() throws ZipException, IOException {
		List<Event> events = new LinkedList<Event>();
		List<Episode> expected = new LinkedList<Episode>();

		Episode episode = createEpisode("0");
		expected.add(episode);

		episode = createEpisode("1", "2", "3");
		expected.add(episode);

		List<Episode> actuals = sut.parse(events);

		verify(rootDirectory).findFiles(anyPredicateOf(String.class));
		verify(rootDirectory).getReadingArchive("a");
		verify(ras.get("a"), times(2)).hasNext();
		verify(ras.get("a")).getNext(Context.class);

		assertEquals(expected, actuals);
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

		data.put("b", context);

		List<Event> events = new LinkedList<Event>();
		List<Episode> expected = new LinkedList<Episode>();

		Episode episode = createEpisode("0");
		expected.add(episode);

		episode = createEpisode("1", "2", "3");
		expected.add(episode);

		episode = createEpisode("4", "5");
		expected.add(episode);

		List<Episode> actuals = sut.parse(events);

		verify(rootDirectory).findFiles(anyPredicateOf(String.class));
		verify(rootDirectory).getReadingArchive("a");
		verify(rootDirectory).getReadingArchive("b");

		verify(ras.get("a"), times(2)).hasNext();
		verify(ras.get("a")).getNext(Context.class);
		verify(ras.get("b"), times(2)).hasNext();
		verify(ras.get("b")).getNext(Context.class);

		assertEquals(expected, actuals);
	}

	private <T> Predicate<T> anyPredicateOf(Class<T> clazz) {
		@SuppressWarnings("unchecked")
		Predicate<T> p = any(Predicate.class);
		return p;
	}

	private Episode createEpisode(String... strings) {
		Episode episode = new Episode();
		episode.setFrequency(1);
		int numEvents = strings.length;
		episode.setNumEvents(numEvents);
		for (String s : strings) {
			episode.addFact(new Fact(s));
		}
		if (strings.length > 1) {
			for (int idx1 = 0; idx1 < numEvents - 1; idx1++) {
				for (int idx2 = idx1 + 1; idx2 < numEvents; idx2++) {
					String raw1 = strings[idx1];
					String raw2 = strings[idx2];

					episode.addFact(new Fact(new Fact(raw1), new Fact(raw2)));
				}
			}
		}
		return episode;
	}
}
