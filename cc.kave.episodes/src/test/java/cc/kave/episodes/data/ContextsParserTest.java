package cc.kave.episodes.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
import cc.kave.commons.utils.io.Directory;
import cc.kave.commons.utils.io.Logger;
import cc.kave.commons.utils.io.ReadingArchive;
import cc.kave.episodes.eventstream.Statistics;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.recommenders.datastructures.Tuple;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ContextsParserTest {

	@Mock
	private Directory rootDirectory;
	@Mock
	private Statistics statistics;

	private static final String REPO0 = "Github/usr1/repo0/ws.zip";
	private static final String REPO1 = "Github/usr1/repo1/ws.zip";
	private static final String REPO3 = "Github/usr1/repo3/ws.zip";
	private static final String REPO2 = "Github/usr1/repo2/ws.zip";
	
	private static final int FREQUENCY = 2;

	private Map<String, Context> data;
	private Map<String, ReadingArchive> ras;

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
		sut = new ContextsParser(rootDirectory);

		SST sst = new SST();
		MethodDeclaration md = new MethodDeclaration();
		md.setName(Names.newMethod("[T,P] [T2,P].M()"));
		md.getBody().add(new ContinueStatement());
		sst.getMethods().add(md);
		
		MethodDeclaration md2 = new MethodDeclaration();
		md2.setName(Names.newMethod("[T,P] [T1,P].M2()"));

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
		md4.setName(Names.newMethod("[T,P] [T3,P].M3()"));
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

		Logger.setPrinting(false);
	}

	@After
	public void teardown() {
		Logger.reset();
	}

	@Test
	public void testStream() throws Exception {
		List<Tuple<Event, List<Event>>> expected = Lists.newLinkedList();

		List<Event> method = Lists.newLinkedList();
		IMethodName decl1 = Names.newMethod("[T,P] [T1,P].M2()");
		
		String inv1 = "[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI1()";
		method.add(Events.newInvocation(Names.newMethod(inv1)));
		expected.add(Tuple.newTuple(Events.newContext(decl1), method));

		IMethodName decl2 = Names.newMethod("[T,P] [T2,P].M()");
		method = Lists.newLinkedList();
		method.add(Events.newInvocation(Names.newMethod(inv1)));
		expected.add(Tuple.newTuple(Events.newContext(decl2), method));

		IMethodName decl3 = Names.newMethod("[T,P] [T3,P].M3()");
		String inv3 = "[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI3()";
		method = Lists.newLinkedList();
		method.add(Events.newInvocation(Names.newMethod(inv3)));
		method.add(Events.newInvocation(Names.newMethod(inv3)));
		expected.add(Tuple.newTuple(Events.newContext(decl3), method));
		
		List<Tuple<Event, List<Event>>> actuals = sut.parse(FREQUENCY);
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void testRepoCtxMapper() throws Exception {
		Map<String, Set<IMethodName>> expected = Maps.newLinkedHashMap();
		
		String repo1 = "Github/usr1/repo1";
		String repo3 = "Github/usr1/repo3";
		String repo2 = "Github/usr1/repo2";
		
		IMethodName decl1 = Names.newMethod("[T,P] [T1,P].M2()");
		IMethodName decl2 = Names.newMethod("[T,P] [T2,P].M()");
		IMethodName decl3 = Names.newMethod("[T,P] [T3,P].M3()");
		
		expected.put(repo1, Sets.newHashSet(decl1));
		expected.put(repo3, Sets.newHashSet(decl2));
		expected.put(repo2, Sets.newHashSet(decl3));
		
		sut.parse(FREQUENCY);
		Map<String, Set<IMethodName>> actuals = sut.getRepoCtxMapper();
		
		assertRepoCtxs(expected, actuals);
	}
	
	private void assertRepoCtxs(Map<String, Set<IMethodName>> expected,
			Map<String, Set<IMethodName>> actuals) {
		if (expected.size() != actuals.size()) {
			assertTrue(false);
		} else {
			for (Map.Entry<String, Set<IMethodName>> entry : expected.entrySet()) {
				String repoName = entry.getKey();
				if (!actuals.containsKey(repoName)) {
					assertTrue(false);
				} else {
					assertEquals(entry.getValue(), actuals.get(repoName));
				}
			}
		}
		
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
