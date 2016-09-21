package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.io.LoggerUtils.assertLogContains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.mining.reader.MappingParser;
import cc.kave.episodes.mining.reader.StreamParser;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.io.Logger;
import cc.recommenders.utils.LocaleUtils;

import com.google.common.collect.Lists;

public class MethodSizeTest {

	@Mock
	private StreamParser streamParser;
	@Mock
	private MappingParser mapParser;

	private List<List<Fact>> stream = Lists.newLinkedList();
	private List<Event> events = Lists.newLinkedList();

	private static final int NUMBREPOS = 5;

	private MethodSize sut;

	@Before
	public void setup() {
		LocaleUtils.setDefaultLocale();

		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);

		sut = new MethodSize(streamParser, mapParser);

		List<Fact> method1 = createMethod("1", "2", "3", "4", "5");
		List<Fact> method2 = createMethod("1", "6");
		List<Fact> method3 = createMethod("2", "7", "5");
		List<Fact> method4 = createMethod("8", "4", "5");

		stream.add(method1);
		stream.add(method2);
		stream.add(method3);
		stream.add(method4);

		events.add(dummy());
		events.add(first(1));
		events.add(sup(2));
		events.add(ctx(3));
		events.add(inv(4));
		events.add(inv(5));
		events.add(ctx(6));
		events.add(ctx(7));
		events.add(ctx(8));

		when(streamParser.parse(NUMBREPOS)).thenReturn(stream);
		when(mapParser.parse(NUMBREPOS)).thenReturn(events);
	}

	@After
	public void teardown() {
		Logger.reset();
	}

	@Test
	public void logger() {
		Logger.clearLog();

		sut.identifier(NUMBREPOS, 3);

		verify(streamParser).parse(NUMBREPOS);
		verify(mapParser).parse(NUMBREPOS);

		assertLogContains(0, "Size of the largest method is: 5");
		assertLogContains(1, "The longest method is: T.m3");
		assertLogContains(2, "Methods with more than 3 events are: 3");
		assertLogContains(3, "Method T.m3\thas 5 events");
		assertLogContains(4, "Method T.m7\thas 3 events");
		assertLogContains(5, "Method T.m8\thas 3 events");
	}
	
	@Test
	public void noBigMethods() {
		Logger.clearLog();

		sut.identifier(NUMBREPOS, 6);

		verify(streamParser).parse(NUMBREPOS);
		verify(mapParser).parse(NUMBREPOS);

		assertLogContains(0, "Size of the largest method is: 5");
		assertLogContains(1, "The longest method is: T.m3");
	}

	private List<Fact> createMethod(String... strings) {
		List<Fact> method = Lists.newLinkedList();
		for (String s : strings) {
			Fact fact = new Fact(s);
			method.add(fact);
		}
		return method;
	}

	private static Event dummy() {
		return Events.newDummyEvent();
	}

	private static Event inv(int i) {
		return Events.newInvocation(m(i));
	}

	private static Event first(int i) {
		return Events.newFirstContext(m(i));
	}

	private static Event sup(int i) {
		return Events.newSuperContext(m(i));
	}

	private static Event ctx(int i) {
		return Events.newContext(m(i));
	}

	private static IMethodName m(int i) {
		return Names.newMethod("[T,P] [T,P].m" + i + "()");
	}
}
