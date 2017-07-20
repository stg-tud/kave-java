package cc.kave.episodes.eventstream;

import static cc.kave.commons.utils.io.LoggerAsserts.assertLogContains;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.LocaleUtils;
import cc.kave.commons.utils.io.Logger;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Lists;

public class StatisticsTest {

	private List<Tuple<Event, List<Event>>> stream;

	private Statistics sut;

	@Before
	public void setup() {
		LocaleUtils.setDefaultLocale();
		Logger.reset();
		Logger.setCapturing(true);
		
		sut = new Statistics();

		stream = Lists.newLinkedList();
		List<Event> method = Lists.newLinkedList();
		method.add(Events.newFirstContext(m(10)));
		method.add(Events.newSuperContext(m(3)));
		method.add(Events.newInvocation(m(41)));
		method.add(Events.newInvocation(m(42)));
		stream.add(Tuple.newTuple(Events.newContext(m(0)), method));
		
		method = Lists.newLinkedList();
		method.add(Events.newFirstContext(m(12)));
		method.add(Events.newInvocation(m(43)));
		method.add(Events.newInvocation(m(41)));
		method.add(Events.newInvocation(m(44)));
		stream.add(Tuple.newTuple(Events.newContext(m(1)), method));
	}
	
	@After
	public void teardown() {
		Logger.reset();
	}
	
	@Test
	public void testLogs() {
		sut.addStats(stream);
		sut.printStats();
		
		assertLogContains(0, "typeDecls:\t(2 unique)");
		assertLogContains(1, "apiTypes:\t8 (2 unique)");
		assertLogContains(2, "ctxFirst:\t2 (2 unique)");
		assertLogContains(3, "ctxSuper:\t1 (1 unique)");
		assertLogContains(4, "ctxElement:\t2 (2 unique)");
		assertLogContains(5, "invExpr:\t5 (4 unique)");
		assertLogContains(6, "------");
		assertLogContains(7, "full stream:\t10 events (excl. types)");
	}
	
	@Test
	public void testErrMsg() {
		List<Event> method = Lists.newLinkedList();
		method.add(Events.newDummyEvent());
		stream.add(Tuple.newTuple(Events.newContext(m(1)), method));
		
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setErr(new PrintStream(outContent));
		
		sut.addStats(stream);
		
		assertEquals("should not happen\n", outContent.toString());
	}

	private static IMethodName m(int i) {
		if (i == 0) {
			return Names.newMethod("[T,P] [T,P].m" + i + "()");
		} else if (i == 10) {
			return Names
					.newMethod("[mscorlib,P, 4.0.0.0] [mscorlib,P, 4.0.0.0].m()");
		} else {
			return Names
					.newMethod("[T,P, 1.2.3.4] [T,P, 1.2.3.4].m" + i + "()");
		}
	}
	
	private ITypeName t(int typeNum) {
		return Names.newType(String.format("T%d,P", typeNum));
	}
}
