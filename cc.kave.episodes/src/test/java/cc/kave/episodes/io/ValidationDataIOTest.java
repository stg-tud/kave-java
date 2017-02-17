package cc.kave.episodes.io;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.exceptions.AssertionException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ValidationDataIOTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private static final int FREQUENCY = 5;
	private static final int FOLDNUM = 0;
	
	private List<Event> eventStream;
	private Map<Event, Integer> events;

	private ValidationDataIO sut;

	@Before
	public void setup() {
		sut = new ValidationDataIO(tmp.getRoot());
		
		eventStream = Lists.newArrayList(firstCtx(1), enclCtx(0), inv(2), inv(3), 
				firstCtx(0), superCtx(2), enclCtx(7), inv(5), inv(0), inv(2), 
				firstCtx(1), enclCtx(6), inv(2), inv(3), 
				firstCtx(0), enclCtx(8), inv(2),
				firstCtx(3), superCtx(4), enclCtx(0), inv(3));
		
		events = Maps.newLinkedHashMap();
		events.put(firstCtx(1), 0);
		events.put(enclCtx(0), 1);
		events.put(inv(2), 2);
		events.put(inv(3), 3);
		events.put(firstCtx(0), 4);
		events.put(superCtx(2), 5);
		events.put(enclCtx(7), 6);
		events.put(inv(5), 7);
		events.put(inv(0), 8);
		events.put(enclCtx(6), 9);
		events.put(enclCtx(8), 10);
		events.put(firstCtx(3), 11);
		events.put(superCtx(4), 12);
	}
	
	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Events folder does not exist");
		sut = new ValidationDataIO(new File("does not exist"));
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = tmp.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Events is not a folder, but a file");
		sut = new ValidationDataIO(file);
	}

	@Test
	public void fileIsCreated() {
		List<Event> stream = Lists.newLinkedList();

		sut.write(stream, FREQUENCY, FOLDNUM);

		File validationData = getValidationPath();

		assertTrue(validationData.exists());
	}
	
	@Test
	public void readerCheck() {
		List<Event> stream = Lists.newLinkedList();
		sut.write(stream, FREQUENCY, FOLDNUM);
		
		List<Event> actuals = sut.read(FREQUENCY, FOLDNUM);
		
		assertTrue(actuals.isEmpty());
		assertTrue(actuals.size() == 0);
	}
	
	@Test
	public void streamOfFacts() {
		List<List<Fact>> expected = Lists.newLinkedList();
		
		List<Fact> method = Lists.newArrayList(new Fact(0), new Fact(1), new Fact(2), new Fact(3));
		expected.add(method);
		
		method = Lists.newArrayList(new Fact(4), new Fact(5), new Fact(6), new Fact(7), new Fact(8), new Fact(2));
		expected.add(method);
		
		method = Lists.newArrayList(new Fact(0), new Fact(9), new Fact(2), new Fact(3));
		expected.add(method);
		
		method = Lists.newArrayList(new Fact(4), new Fact(10), new Fact(2));
		expected.add(method);
		
		method = Lists.newArrayList(new Fact(11), new Fact(12), new Fact(1), new Fact(3));
		expected.add(method);
		
		List<List<Fact>> actuals = sut.streamOfFacts(eventStream, events);
		
		assertTrue(expected.size() == actuals.size());
		assertEquals(expected, actuals);
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
		return Events.newContext(m(i));
	}

	private static IMethodName m(int i) {
		if (i == 0) {
			return Names.getUnknownMethod();
		} else if (i == 10) {
			return Names.newMethod("[T,P] [T,P].m()");
		} else if (i == 20) {
			return Names.newMethod("[mscorlib,P, 4.0.0.0] [mscorlib,P, 4.0.0.0].m()");
		} else if (i == 30) {
			return Names.newMethod("[mscorlib,P] [mscorlib,P].m()");
		} else {
			return Names.newMethod("[T,P, 1.2.3.4] [T,P, 1.2.3.4].m" + i + "()");
		}
	}

	private File getValidationPath() {
		File fileName = new File(tmp.getRoot().getAbsolutePath() + "/freq"
				+ FREQUENCY + "/ValidationData/stream" + FOLDNUM + ".txt");
		return fileName;
	}
}
