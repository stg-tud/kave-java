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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.io.IndivReposParser;
import cc.kave.episodes.io.TrainingDataIO;
import cc.kave.episodes.io.ValidationDataIO;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RunWith(MockitoJUnitRunner.class)
public class PreprocessingFoldedTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private IndivReposParser repoParser;
	@Mock
	private TrainingDataIO trainingIo;
	@Mock
	private ValidationDataIO validationIo;

	private static final int NUM_FOLD = 10;
	private static final int FREQTHRESH = 5;

	private PreprocessingFolded sut;

	@Before
	public void setup() throws ZipException, IOException {
		initMocks(this);

		sut = new PreprocessingFolded(repoParser, trainingIo, validationIo);

		when(repoParser.generateReposEvents()).thenReturn(generateMapper());

		doNothing().when(trainingIo).write(any(EventStream.class), anyInt());
		doNothing().when(validationIo).write(anyListOf(Event.class), anyInt());
	}
	
	@Test
	public void checkCrossFold() throws IOException {
		sut.runPreparation(FREQTHRESH);
		
		verify(repoParser).generateReposEvents();
		
		verify(trainingIo, times(10)).write(any(EventStream.class), anyInt());
		verify(validationIo, times(10)).write(anyListOf(Event.class), anyInt());
	}
	
	@Test
	public void checkOverlapping() {
		for (int fold = 0; fold < NUM_FOLD; fold++) {
			
			List<Event> trainingData = sut.createTrainingData(fold, NUM_FOLD, generateMapper());
			List<Event> validationData = sut.createValidationData(fold, NUM_FOLD, generateMapper());
			
			assertTrue(trainingData.size() == 27);
			assertTrue(validationData.size() == 3);
			
			assertNotEquals(trainingData, validationData);
			boolean overlap = trainingData.contains(validationData);
			assertFalse(overlap);
		}
	}

	private Map<String, List<Event>> generateMapper() {
		Map<String, List<Event>> mapper = Maps.newLinkedHashMap();
		mapper.put("Github/usr1/repo1", Lists.newArrayList(firstCtx(0), enclCtx(1), inv(1)));
		mapper.put("Github/usr1/repo2", Lists.newArrayList(firstCtx(0), enclCtx(2), inv(2)));
		mapper.put("Github/usr1/repo3", Lists.newArrayList(firstCtx(0), enclCtx(3), inv(3)));
		mapper.put("Github/usr1/repo4", Lists.newArrayList(firstCtx(0), enclCtx(4), inv(4)));
		mapper.put("Github/usr1/repo5", Lists.newArrayList(firstCtx(0), enclCtx(5), inv(5)));
		mapper.put("Github/usr1/repo6", Lists.newArrayList(firstCtx(0), enclCtx(6), inv(6)));
		mapper.put("Github/usr1/repo7", Lists.newArrayList(firstCtx(0), enclCtx(7), inv(7)));
		mapper.put("Github/usr1/repo8", Lists.newArrayList(firstCtx(0), enclCtx(8), inv(8)));
		mapper.put("Github/usr1/repo9", Lists.newArrayList(firstCtx(0), enclCtx(9), inv(9)));
		mapper.put("Github/usr1/repo10", Lists.newArrayList(firstCtx(0), enclCtx(10), inv(10)));

		return mapper;
	}
	
	private static Event firstCtx(int i) {
		return Events.newFirstContext(m(i));
	}

	private static Event enclCtx(int i) {
		return Events.newContext(m(i));
	}
	
	private static Event inv(int i) {
		return Events.newInvocation(m(i));
	}

	private static IMethodName m(int i) {
		if (i == 0) {
			return Names.getUnknownMethod();
		} else {
			return Names.newMethod("[T,P] [T,P].m" + i + "()");
		}
	}
}
