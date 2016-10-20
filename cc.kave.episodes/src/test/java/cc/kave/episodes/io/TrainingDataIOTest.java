package cc.kave.episodes.io;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import cc.kave.episodes.model.EventStream;
import cc.recommenders.exceptions.AssertionException;

public class TrainingDataIOTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private static final int FOLDNUM = 5;
	
	private TrainingDataIO sut;
	
	@Before
	public void setup() {
		sut = new TrainingDataIO(tmp.getRoot());
	}
	
	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Repositories folder does not exist");
		sut = new TrainingDataIO(new File("does not exist"));
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = tmp.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Repositories is not a folder, but a file");
		sut = new TrainingDataIO(file);
	}
	
	@Test
	public void filesAreCreated() {
		EventStream es = new EventStream();
		
		sut.write(es, FOLDNUM);
		
		File stream = getStreamFile();
		File mapping = getMappingFile();
		File methods = getMethodsFile();
		
		assertTrue(stream.exists());
		assertTrue(mapping.exists());
		assertTrue(methods.exists());
	}

	private File getMethodsFile() {
		File fileName = new File(getPath() + "/methods.txt");
		return fileName;
	}

	private File getMappingFile() {
		File fileName = new File(getPath() + "/mapping.txt");
		return fileName;
	}

	private File getStreamFile() {
		File fileName = new File(getPath() + "/stream.txt");
		return fileName;
	}

	private String getPath() {
		File path = new File(tmp.getRoot().getAbsolutePath() + "/TrainingData/fold" + FOLDNUM);
		if (!path.isDirectory()) {
			path.mkdirs();
		}
		return path.getAbsolutePath();
	}
}
