package cc.kave.episodes.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

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
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.recommenders.exceptions.AssertionException;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ReposMethodsMapperIoTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Map<String, EventStreamGenerator> reposGenerators;

	private RepoMethodsMapperIO sut;

	@Before
	public void setup() throws ZipException, IOException {
		initMocks(this);

		reposGenerators = generateMapper();

		sut = new RepoMethodsMapperIO(tmp.getRoot());
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Events folder does not exist");
		sut = new RepoMethodsMapperIO(new File("does not exist"));
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = tmp.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Events is not a folder, but a file");
		sut = new RepoMethodsMapperIO(file);
	}

	@Test
	public void fileIsCreated() throws ZipException, IOException {
		File file = new File(getReposMethodsPath());

		sut.writer(reposGenerators);

		assertTrue(file.exists());
	}

	@Test
	public void fileContent() throws ZipException, IOException {
		sut.writer(reposGenerators);
		Map<String, Set<IMethodName>> actMapper = sut.reader();
//		@SuppressWarnings("serial")
//		Type type = new TypeToken<Map<String, Set<Event>>>() {
//		}.getType();
//		Map<String, Set<Event>> actMapper = JsonUtils.fromJson(new File(
//				getReposMethodsPath()), type);

		System.out.println(actMapper.toString());
		assertTrue(generateReposMethods().size() == actMapper.size());
		assertMapEquality(generateReposMethods(), actMapper);
	}

	private void assertMapEquality(Map<String, Set<IMethodName>> expected,
			Map<String, Set<IMethodName>> actuals) {
		
		if (expected.isEmpty() && actuals.isEmpty()) {
			assertTrue(true);
		}
		if (expected.size() != actuals.size()) {
			fail();
		}
		for (Map.Entry<String, Set<IMethodName>> entry : expected.entrySet()) {
			if (!actuals.containsKey(entry.getKey())) {
				fail();
			}
			Set<IMethodName> expMethods = entry.getValue();
			Set<IMethodName> actMethods = actuals.get(entry.getKey());
			
			if (expMethods.isEmpty() && actMethods.isEmpty()) {
				continue;
			}
			Iterator<IMethodName> expIt = expMethods.iterator();
			Iterator<IMethodName> actIt = actMethods.iterator();
			while (actIt.hasNext()) {
				IMethodName expEvent = expIt.next();
				IMethodName actEvent = actIt.next();
				assertEquals(expEvent, actEvent);
			}
 		}
		assertTrue(true);
	}

	private Map<String, Set<IMethodName>> generateReposMethods() {
		Map<String, Set<IMethodName>> mapper = Maps.newLinkedHashMap();
		Set<IMethodName> method = Sets.newLinkedHashSet();
		method.add(enclCtx(0).getMethod());

		mapper.put("Github/usr1/repo1", method);
		mapper.put("Github/usr1/repo2", method);
		mapper.put("Github/usr1/repo3", method);

		return mapper;
	}

	private String getReposMethodsPath() {
		String path = tmp.getRoot().getAbsolutePath()
				+ "/repoMethodsMapper.json";
		return path;
	}

	private Map<String, EventStreamGenerator> generateMapper() {

		Map<String, EventStreamGenerator> mapper = Maps.newLinkedHashMap();
		EventStreamGenerator gen1 = new EventStreamGenerator();
		EventStreamGenerator gen2 = new EventStreamGenerator();
		EventStreamGenerator gen3 = new EventStreamGenerator();

		gen1.add(genCtx1());
		gen2.add(genCtx2());
		gen3.add(genCtx3());

		mapper.put("Github/usr1/repo1", gen1);
		mapper.put("Github/usr1/repo2", gen2);
		mapper.put("Github/usr1/repo3", gen3);

		return mapper;
	}

	private Context genCtx1() {
		Context context = new Context();

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

		context.setSST(sst);

		return context;
	}

	private Context genCtx2() {
		Context context = new Context();

		SST sst3 = new SST();
		MethodDeclaration md3 = new MethodDeclaration();
		md3.setName(Names.newMethod("[T,P] [T4,P].M()"));
		InvocationExpression ie5 = new InvocationExpression();
		IMethodName methodName5 = Names
				.newMethod("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI1()");
		ie5.setMethodName(methodName5);
		md3.getBody().add(wrap(ie5));
		sst3.getMethods().add(md3);

		context.setSST(sst3);

		return context;
	}

	private Context genCtx3() {
		Context context = new Context();

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
		context.setSST(sst2);

		return context;
	}

	private static ExpressionStatement wrap(InvocationExpression ie1) {
		ExpressionStatement expressionStatement = new ExpressionStatement();
		expressionStatement.setExpression(ie1);
		return expressionStatement;
	}

	private static Event enclCtx(int i) {
		return Events.newContext(m(i));
	}

	private static IMethodName m(int i) {
		if (i == 0) {
			return Names.getUnknownMethod();
		} else {
			return Names.newMethod("[T,P] [T,P].m" + i + "()");
		}
	}
}
