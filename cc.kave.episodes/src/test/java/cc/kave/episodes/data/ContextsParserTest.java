package cc.kave.episodes.data;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.episodes.eventstream.Filters;
import cc.kave.episodes.eventstream.Statistics;
import cc.kave.episodes.io.FileReader;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ContextsParserTest {

	@Mock
	private Directory rootDirectory;
	@Mock
	private FileReader reader;
	@Mock
	private Filters filters;
	@Mock
	private Statistics statistics;

	private static final String REPO1 = "Github/usr1/repo1/ws.zip";
	private static final String REPO3 = "Github/usr1/repo3/ws.zip";
	private static final String REPO2 = "Github/usr1/repo2/ws.zip";

	private Map<String, Context> data;
	private Map<String, ReadingArchive> ras;
	private List<String> repositories;

	private Map<String, Set<IMethodName>> repoDecls = Maps.newLinkedHashMap();
	private List<Tuple<Event, List<Event>>> streamRepos = Lists.newLinkedList();
	private List<Tuple<Event, List<Event>>> streamFilters = Lists
			.newLinkedList();

	private ContextsParser sut;

	@Before
	public void setup() throws IOException {
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);

		data = Maps.newLinkedHashMap();
		ras = Maps.newLinkedHashMap();
		sut = new ContextsParser(rootDirectory, filters);

		SST sst = new SST();
		MethodDeclaration md = new MethodDeclaration();
		md.setName(Names.newMethod("[T,P] [T2,P].M()"));
		md.getBody().add(new ContinueStatement());
		sst.getMethods().add(md);

		MethodDeclaration md2 = new MethodDeclaration();
		md2.setName(Names.newMethod("[T,P] [T3,P].M2()"));

		InvocationExpression ie1 = new InvocationExpression();
		IMethodName methodName = Names
				.newMethod("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI1()");
		ie1.setMethodName(methodName);
		InvocationExpression ie2 = new InvocationExpression();
		IMethodName methodName2 = Names
				.newMethod("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI2()");
		ie2.setMethodName(methodName2);

		md2.getBody().add(wrap(ie1));
		md2.getBody().add(wrap(ie2));
		md2.getBody().add(wrap(ie2));
		sst.getMethods().add(md2);

		Context context = new Context();
		context.setSST(sst);
		data.put(REPO1, context);

		SST sst3 = new SST();
		MethodDeclaration md3 = new MethodDeclaration();
		md3.setName(Names.newMethod("[T,P] [T2,P].M()"));
		InvocationExpression ie5 = new InvocationExpression();
		IMethodName methodName5 = Names
				.newMethod("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI1()");
		ie5.setMethodName(methodName5);
		md3.getBody().add(wrap(ie5));
		sst3.getMethods().add(md3);

		Context context3 = new Context();
		context3.setSST(sst3);
		data.put(REPO3, context3);

		SST sst2 = new SST();
		MethodDeclaration md4 = new MethodDeclaration();
		md4.setName(Names.newMethod("[T,P] [T2,P].M3()"));
		md4.getBody().add(new DoLoop());

		InvocationExpression ie3 = new InvocationExpression();
		IMethodName methodName3 = Names
				.newMethod("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI3()");
		ie3.setMethodName(methodName3);

		InvocationExpression ie4 = new InvocationExpression();
		methodName3 = Names
				.newMethod("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI3()");
		ie4.setMethodName(methodName3);

		md4.getBody().add(wrap(ie3));
		md4.getBody().add(wrap(ie4));
		md4.getBody().add(new ExpressionStatement());

		sst2.getMethods().add(md4);
		Context context2 = new Context();
		context2.setSST(sst2);

		data.put(REPO2, context2);

		repositories = new LinkedList<>();
		repositories.add("Github/usr1/repo1");
		repositories.add("Github/usr1/repo3");

		when(rootDirectory.findFiles(anyPredicateOf(String.class))).thenAnswer(
				new Answer<Set<String>>() {
					@Override
					public Set<String> answer(InvocationOnMock invocation)
							throws Throwable {
						return data.keySet();
					}
				});
		when(rootDirectory.getReadingArchive(anyString())).then(
				new Answer<ReadingArchive>() {
					@Override
					public ReadingArchive answer(InvocationOnMock invocation)
							throws Throwable {
						String file = (String) invocation.getArguments()[0];
						ReadingArchive ra = mock(ReadingArchive.class);
						ras.put(file, ra);
						when(ra.hasNext()).thenReturn(true).thenReturn(false);
						when(ra.getNext(Context.class)).thenReturn(
								data.get(file));
						return ra;
					}
				});
		when(reader.readFile(any(File.class))).thenReturn(repositories);

		Logger.setPrinting(false);
	}

	@After
	public void teardown() {
		Logger.reset();
	}

	@Test
	public void testStream() throws IOException {
		List<Tuple<Event, List<Event>>> expected = Lists.newLinkedList();

		List<Event> method = Lists.newLinkedList();
		IMethodName decl1 = Names.newMethod("[T,P] [T3,P].M2()");
		String inv1 = "[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI1()";
		String inv2 = "[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI2()";

		method.add(Events.newInvocation(Names.newMethod(inv1)));
		method.add(Events.newInvocation(Names.newMethod(inv2)));
		expected.add(Tuple.newTuple(Events.newContext(decl1), method));

		IMethodName decl2 = Names.newMethod("[T,P] [T2,P].M()");
		method = Lists.newLinkedList();
		method.add(Events.newInvocation(Names.newMethod(inv1)));
		expected.add(Tuple.newTuple(Events.newContext(decl2), method));

	}

	private static ExpressionStatement wrap(InvocationExpression ie1) {
		ExpressionStatement expressionStatement = new ExpressionStatement();
		expressionStatement.setExpression(ie1);
		return expressionStatement;
	}

	private <T> Predicate<T> anyPredicateOf(Class<T> clazz) {
		@SuppressWarnings("unchecked")
		Predicate<T> p = any(Predicate.class);
		return p;
	}
}
