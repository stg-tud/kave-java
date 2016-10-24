package cc.kave.episodes.preprocessing;

import static cc.recommenders.io.LoggerUtils.assertLogContains;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.io.Logger;
import cc.recommenders.utils.LocaleUtils;

public class EventStreamSizeTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private EventStream es0;
	private EventStream es1;

	private static final int NUM_FOLDS = 2;
	private static final int SIZELIMIT = 3;

	private EventStreamSize sut;

	@Before
	public void setup() {
		LocaleUtils.setDefaultLocale();

		Logger.reset();
		Logger.setCapturing(true);

		es0 = addEvents(first(1), ctx(1), inv(1), inv(6), first(0), sup(1),
				ctx(2), inv(2), inv(3), inv(4), inv(5));
		es1 = addEvents(first(0), sup(2), ctx(3), inv(7), inv(5), first(8),
				ctx(4), inv(4), inv(5));

		EventStreamIo.write(es0, getFilePath(0, "stream.txt"),
				getFilePath(0, "mapping.txt"), getFilePath(0, "methods.txt"));
		EventStreamIo.write(es1, getFilePath(1, "stream.txt"),
				getFilePath(1, "mapping.txt"), getFilePath(1, "methods.txt"));

		sut = new EventStreamSize(rootFolder.getRoot());
	}

	@After
	public void teardown() {
		Logger.reset();
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Repositories folder does not exist");
		sut = new EventStreamSize(new File("does not exist"));
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Repositories is not a folder, but a file");
		sut = new EventStreamSize(file);
	}

	@Test
	public void numEvents() {
		Logger.clearLog();

		sut.printNumberOfEvents(NUM_FOLDS);

		assertLogContains(0, "Number of unique events is 9");
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
		if (i == 0) {
			return Names.getUnknownMethod();
		} else {
			return Names.newMethod("[T,P] [T,P].m" + i + "()");
		}
	}

	private File getPath(int fold) {
		File path = new File(rootFolder.getRoot().getAbsolutePath()
				+ "/TrainingData/fold" + fold);

		if (!path.isDirectory()) {
			path.mkdirs();
		}
		return path;
	}

	private String getFilePath(int fold, String file) {
		String fileName = getPath(fold) + "/" + file;
		return fileName;
	}

	private EventStream addEvents(Event... events) {
		EventStream es = new EventStream();

		for (Event e : events) {
			es.addEvent(e);
		}
		return es;
	}

}
