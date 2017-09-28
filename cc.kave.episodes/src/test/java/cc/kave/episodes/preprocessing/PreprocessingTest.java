package cc.kave.episodes.preprocessing;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq; 
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.data.ContextsParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class PreprocessingTest {

	@Mock
	private ContextsParser ctxParser;
	@Mock
	private EventStreamIo trainingIo;

	private List<Tuple<Event, List<Event>>> stream;

	private static final int FREQUENCY = 5;

	private Preprocessing sut;

	@Before
	public void setup() throws Exception {
		initMocks(this);
		stream = Lists.newLinkedList();

		sut = new Preprocessing(ctxParser, trainingIo);

		stream.add(Tuple.newTuple(enclCtx(20),
				Lists.newArrayList(firstCtx(1), inv(2), inv(3))));
		stream.add(Tuple.newTuple(enclCtx(7), Lists.newArrayList(firstCtx(2),
				superCtx(2), inv(5), inv(20), inv(2))));

		when(ctxParser.parse(FREQUENCY)).thenReturn(stream);

		doNothing().when(trainingIo).write(any(EventStream.class), anyInt());
	}

	@Test
	public void checkAllRepos() throws Exception {
		EventStream expected = new EventStream();
		expected.addEvent(firstCtx(1));
		expected.addEvent(inv(2));
		expected.addEvent(inv(3));
		expected.increaseTimeout();
		
		expected.addEvent(firstCtx(2));
		expected.addEvent(superCtx(2));
		expected.addEvent(inv(5));
		expected.addEvent(inv(20));
		expected.addEvent(inv(2));
		expected.increaseTimeout();
		
		EventStream actuals = sut.run(FREQUENCY);

		verify(ctxParser).parse(FREQUENCY);
		verify(trainingIo).write(any(EventStream.class), anyInt());
		
		assertTrue(expected.equals(actuals));
	}
	
	private static Event inv(int i) {
		return Events.newInvocation(m(i));
	}

	private static Event firstCtx(int i) {
		return Events.newFirstContext(m(i));
	}

	private static Event superCtx(int i) {
		return Events.newSuperContext(m(i));
	}

	private static Event enclCtx(int i) {
		return Events.newElementContext(m(i));
	}

	private static IMethodName m(int i) {
		return Names.newMethod("[T,P, 1.2.3.4] [T,P, 1.2.3.4].m" + i + "()");
	}
}
