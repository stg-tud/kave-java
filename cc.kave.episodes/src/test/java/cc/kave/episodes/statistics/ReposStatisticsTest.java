package cc.kave.episodes.statistics;

import static cc.recommenders.io.LoggerUtils.assertLogContains;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
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
import cc.kave.episodes.eventstream.EventStreamGenerator;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

public class ReposStatisticsTest {

	@Mock
	private Directory rootDirectory;

	private static final String REPO1 = "Github/usr1/repo1/ws.zip";
	private static final String REPO3 = "Github/usr1/repo3/ws.zip";
	private static final String REPO2 = "Github/usr1/repo2/ws.zip";

	private static final int FREQUENCY = 2;

	private Map<String, Context> data;
	private Map<String, ReadingArchive> ras;

	private Context context1;
	private Context context3;
	private Context context2;

	private ReposStatistics sut;

	@Before
	public void setup() throws IOException {
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);

		data = Maps.newLinkedHashMap();
		ras = Maps.newLinkedHashMap();
		sut = new ReposStatistics(rootDirectory);

		SST sst1 = new SST();
		sst1.setEnclosingType(Names.newType("T1.T2.T3"));
		MethodDeclaration md = new MethodDeclaration();
		md.setName(Names.newMethod("[T1,P] [T2,P].M()"));
		md.getBody().add(new ContinueStatement());
		sst1.getMethods().add(md);

		MethodDeclaration md2 = new MethodDeclaration();
		md2.setName(Names.newMethod("[T1,P] [T3,P].M2()"));

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
		sst1.getMethods().add(md2);

		context1 = new Context();
		context1.setSST(sst1);
		data.put(REPO1, context1);

		SST sst3 = new SST();
		sst3.setEnclosingType(Names.newType("T1.T2.T3"));
		MethodDeclaration md3 = new MethodDeclaration();
		md3.setName(Names.newMethod("[T3,P] [T4,P].M()"));
		InvocationExpression ie5 = new InvocationExpression();
		IMethodName methodName5 = Names
				.newMethod("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI1()");
		ie5.setMethodName(methodName5);
		md3.getBody().add(wrap(ie5));
		sst3.getMethods().add(md3);

		context3 = new Context();
		context3.setSST(sst3);
		data.put(REPO3, context3);

		SST sst2 = new SST();
		sst2.setEnclosingType(Names.newType("T2.T3.T4"));
		MethodDeclaration md4 = new MethodDeclaration();
		md4.setName(Names.newMethod("[T2,P] [T2,P].M3()"));
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
		context2 = new Context();
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
	public void contextTest() throws Exception {
		sut.generate(FREQUENCY);

		verify(rootDirectory).findFiles(anyPredicateOf(String.class));
		verify(rootDirectory).getReadingArchive(REPO1);
		verify(rootDirectory).getReadingArchive(REPO3);
		verify(rootDirectory).getReadingArchive(REPO2);

		verify(ras.get(REPO1), times(2)).hasNext();
		verify(ras.get(REPO1)).getNext(Context.class);
		verify(ras.get(REPO3), times(2)).hasNext();
		verify(ras.get(REPO3)).getNext(Context.class);
		verify(ras.get(REPO2), times(2)).hasNext();
		verify(ras.get(REPO2)).getNext(Context.class);
	}

	@Test
	public void loggerTest() throws Exception {
		sut.generate(FREQUENCY);

		EventStreamGenerator generator = new EventStreamGenerator();
		generator.addAny(context1);
		generator.addAny(context3);
		generator.addAny(context2);
		
//		for (Event event : generator.getEventStream()) {
//			if (event.getKind() != EventKind.INVOCATION) {
//				System.out.println(event.toString());
//			}
//		}

		assertLogContains(0, "Reading zip file Github/usr1/repo1/ws.zip");
		assertLogContains(1, "Reading zip file Github/usr1/repo3/ws.zip");
		assertLogContains(2, "Reading zip file Github/usr1/repo2/ws.zip");
		assertLogContains(3, "Repositories information ...");
		assertLogContains(4, "Number of unique context types: 2");
		assertLogContains(5, "Number of context type occurrences: 3");
		assertLogContains(6, "Number of unique method declarations: 2");
		assertLogContains(7, "Number of method declaration occurrences: 6");
		assertLogContains(8, "Number of unique method invocations: 3");
		assertLogContains(9, "Number of method invocation occurrences: 6");
		assertLogContains(10, "");
		
		assertLogContains(11, "After filtering overlaping types between different repositories ...");
		assertLogContains(12, "Number of unique method declarations: 2");
		assertLogContains(13, "Number of method declaration occurrences: 4");
		assertLogContains(14, "Number of unique method invocations: 3");
		assertLogContains(15, "Number of method invocation occurrences: 5");
		assertLogContains(16, "");
		
		assertLogContains(17, "After filtering auto-generated code ...");
		assertLogContains(18, "Number of unique method declarations: 2");
		assertLogContains(19, "Number of method declaration occurrences: 4");
		assertLogContains(20, "Number of unique method invocations: 3");
		assertLogContains(21, "Number of method invocation occurrences: 5");
		assertLogContains(22, "");
		
		assertLogContains(23, "After filtering unknown methods ...");
		assertLogContains(24, "Number of unique method declarations: 0");
		assertLogContains(25, "Number of method declaration occurrences: 0");
		assertLogContains(26, "Number of unique method invocations: 3");
		assertLogContains(27, "Number of method invocation occurrences: 5");
		assertLogContains(28, "");
		
		assertLogContains(29, "After filtering local types ...");
		assertLogContains(30, "Number of unique method declarations: 0");
		assertLogContains(31, "Number of method declaration occurrences: 0");
		assertLogContains(32, "Number of unique method invocations: 3");
		assertLogContains(33, "Number of method invocation occurrences: 5");
		assertLogContains(34, "");
		
		assertLogContains(35, "After filtering element contexts ...");
		assertLogContains(36, "Number of unique method declarations: 0");
		assertLogContains(37, "Number of method declaration occurrences: 0");
		assertLogContains(38, "Number of unique method invocations: 3");
		assertLogContains(39, "Number of method invocation occurrences: 5");
		assertLogContains(40, "");
		
		assertLogContains(41, "After filtering for frequency = 2 ...");
		assertLogContains(42, "Number of unique method declarations: 0");
		assertLogContains(43, "Number of method declaration occurrences: 0");
		assertLogContains(44, "Number of unique method invocations: 2");
		assertLogContains(45, "Number of method invocation occurrences: 4");
		assertLogContains(46, "");
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
