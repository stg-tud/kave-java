package cc.kave.episodes.io;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import cc.kave.episodes.model.events.Event;
import cc.recommenders.exceptions.AssertionException;

import com.google.common.collect.Lists;

public class ValidationDataIOTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private static final int FOLDNUM = 5;

	private ValidationDataIO sut;

	@Before
	public void setup() {
		sut = new ValidationDataIO(tmp.getRoot());
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Repositories folder does not exist");
		sut = new ValidationDataIO(new File("does not exist"));
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = tmp.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Repositories is not a folder, but a file");
		sut = new ValidationDataIO(file);
	}

	@Test
	public void fileIsCreated() {
		List<Event> stream = Lists.newLinkedList();

		sut.write(stream, FOLDNUM);

		File validationData = getValidationPath();
		
		assertTrue(validationData.exists());
	}

	private File getValidationPath() {
		File fileName = new File(tmp.getRoot().getAbsolutePath()
				+ "/ValidationData/stream" + FOLDNUM + ".json");
		return fileName;
	}
}
