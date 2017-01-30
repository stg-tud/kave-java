package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.io.LoggerUtils.assertLogContains;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.MockitoAnnotations;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.recommenders.io.Logger;
import cc.recommenders.utils.LocaleUtils;

public class MethodSizeTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private EventStream eventStream = new EventStream();
	private EventStreamIo streamIo;

	private static final int FREQUENCY = 5;

	private MethodSize sut;

	@Before
	public void setup() {
		LocaleUtils.setDefaultLocale();

		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);

		streamIo = new EventStreamIo(rootFolder.getRoot());

		sut = new MethodSize();

		eventStream.addEvent(first(1)); // 1
		eventStream.addEvent(sup(2)); // 2
		eventStream.addEvent(ctx(3));
		eventStream.addEvent(inv(4)); // 3
		eventStream.addEvent(inv(5)); // 4
		eventStream.addEvent(first(1));
		eventStream.addEvent(ctx(6));
		eventStream.addEvent(first(1));
		eventStream.addEvent(sup(2));
		eventStream.addEvent(ctx(7));
		eventStream.addEvent(inv(5));
		eventStream.addEvent(first(1));
		eventStream.addEvent(ctx(8));
		eventStream.addEvent(inv(4));
		eventStream.addEvent(inv(5));
	}

	@After
	public void teardown() {
		Logger.reset();
	}

	@Test
	public void logger() {
		Logger.clearLog();

		streamIo.write(eventStream, FREQUENCY);

		assertTrue(new File(getStreamPath(FREQUENCY)).exists());
		assertTrue(new File(getMappingPath(FREQUENCY)).exists());
		assertTrue(new File(getMethodsPath(FREQUENCY)).exists());

		sut.statistics(FREQUENCY, 3);

		assertLogContains(0, "Number of methods in stream data is 4");
		assertLogContains(1, "Number of events in the event stream is 11");
		assertLogContains(2, "Number of unique events is 5");
		assertLogContains(3, "Number of enclosing methods is 4");
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

	private File getPath(int numRepos) {
		File path = new File(rootFolder.getRoot().getAbsolutePath() + "/freq"
				+ FREQUENCY + "/TrainingData/fold0");
		return path;
	}

	private String getStreamPath(int numRepos) {
		String fileName = getPath(numRepos).getAbsolutePath() + "/stream.txt";
		return fileName;
	}

	private String getMappingPath(int numRepos) {
		String fileName = getPath(numRepos).getAbsolutePath() + "/mapping.txt";
		return fileName;
	}

	private String getMethodsPath(int numRepos) {
		String fileName = getPath(numRepos).getAbsolutePath() + "/methods.txt";
		return fileName;
	}
}
