package cc.kave.episodes.preprocessing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.episodes.eventstream.EventStreamGenerator;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.RepositoriesParser;
import cc.kave.episodes.io.ValidationDataIO;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;

import com.google.common.collect.Maps;

@RunWith(MockitoJUnitRunner.class)
public class PreprocessingFoldedTest {

	@Mock
	private RepositoriesParser repoParser;
	@Mock
	private EventStreamIo trainingIo;
	@Mock
	private ValidationDataIO validationIo;

	private static final int NUM_FOLD = 10;
	private static final int FOLDNUM = 10;
	private static final int FREQTHRESH = 5;

	private PreprocessingFolded sut;

	@Before
	public void setup() throws Exception {
		initMocks(this);

		sut = new PreprocessingFolded(repoParser, trainingIo, validationIo);

		when(repoParser.generateReposEvents()).thenReturn(generateMapper());

		doNothing().when(trainingIo).write(any(EventStream.class), anyInt(), anyInt());
		doNothing().when(validationIo).write(anyListOf(Event.class), anyInt());
	}

	@Test
	public void checkCrossFold() throws Exception {
		sut.runPreparation(FREQTHRESH);

		verify(repoParser).generateReposEvents();

		verify(trainingIo, times(10)).write(any(EventStream.class), anyInt(), anyInt());
		verify(validationIo, times(10)).write(anyListOf(Event.class), anyInt());
	}

	@Test
	public void checkOverlapping() {
		for (int fold = 0; fold < NUM_FOLD; fold++) {

			List<Event> trainingData = sut.createTrainingData(fold, NUM_FOLD,
					generateMapper());
			List<Event> validationData = sut.createValidationData(fold,
					NUM_FOLD, generateMapper());

			assertTrue(trainingData.size() == 27);
			assertTrue(validationData.size() == 3);

			assertNotEquals(trainingData, validationData);
			boolean overlap = trainingData.contains(validationData);
			assertFalse(overlap);
		}
	}

	private Map<String, EventStreamGenerator> generateMapper() {

		Map<String, EventStreamGenerator> mapper = Maps.newLinkedHashMap();
		mapper.put("Github/usr1/repo1", genCtx(1));
		mapper.put("Github/usr1/repo2", genCtx(2));
		mapper.put("Github/usr1/repo3", genCtx(3));
		mapper.put("Github/usr1/repo4", genCtx(4));
		mapper.put("Github/usr1/repo5", genCtx(5));
		mapper.put("Github/usr1/repo6", genCtx(6));
		mapper.put("Github/usr1/repo7", genCtx(7));
		mapper.put("Github/usr1/repo8", genCtx(8));
		mapper.put("Github/usr1/repo9", genCtx(9));
		mapper.put("Github/usr1/repo10", genCtx(10));

		return mapper;
	}

	private EventStreamGenerator genCtx(int i) {

		SST sst = new SST();
		Context context = new Context();
		EventStreamGenerator generator = new EventStreamGenerator();
		
		MethodDeclaration md = new MethodDeclaration();
		md.setName(Names.newMethod("[T,P] [T3,P].M" + i + "()"));

		InvocationExpression ie1 = new InvocationExpression();
		IMethodName methodName = Names
				.newMethod("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MI"
						+ i + "()");
		ie1.setMethodName(methodName);

		md.getBody().add(wrap(ie1));
		sst.getMethods().add(md);

		context.setSST(sst);
		generator.add(context);
		
		return generator;
	}
	
	private static ExpressionStatement wrap(InvocationExpression ie1) {
		ExpressionStatement expressionStatement = new ExpressionStatement();
		expressionStatement.setExpression(ie1);
		return expressionStatement;
	}
}
